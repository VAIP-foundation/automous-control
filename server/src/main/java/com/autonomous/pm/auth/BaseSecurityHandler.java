package com.autonomous.pm.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.autonomous.pm.auth.jwt.JwtInfo;
import com.autonomous.pm.domain.common.ResultCode;
import com.autonomous.pm.domain.common.ResultValue;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.LoginInfo;
import com.autonomous.pm.model.Dto.ResUsr;
import com.autonomous.pm.service.LoginService;
import com.autonomous.pm.service.restful.LoginHstService;
import com.autonomous.pm.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BaseSecurityHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

	public static final Logger logger = LoggerFactory.getLogger(BaseSecurityHandler.class);

	private final LoginHstService loginHstService;
	private final LoginService loginService;

	public BaseSecurityHandler(LoginHstService loginHstService, LoginService loginService) {
		this.loginService = loginService;
		this.loginHstService = loginHstService;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		UsrDetailsImpl userDetails = (UsrDetailsImpl) authentication.getPrincipal();		
		logger.debug("onAuthenticationSuccess  " + userDetails);
		
		// 로그인 성공시 로그 insert
		loginHstService.insertLoginHst(request, userDetails);
		// 로그인 성공시 로그인실패이력 초기화
		loginService.success(userDetails.getLoginId());
		
		// 응답객체 생성
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		String accessToken = JwtUtil.createAccessToken(userDetails);
		String refreshToken = JwtUtil.createRefreshToken(userDetails, accessToken);
		response.setHeader(JwtInfo.HEADER_NAME, accessToken);
		try {
			GrantedAuthority author = userDetails.getAuthorities().iterator().next();
			com.autonomous.pm.model.Dto.ResUsr u = new ResUsr(userDetails.getUsername(), author.getAuthority(), accessToken);
			
			DecodedJWT jwt = JwtUtil.tokenToJwt(accessToken);
			
			u.setLoginId(userDetails.getDetail());
			u.setUsrNm(userDetails.getUsrNm());
			u.setGrpNm(userDetails.getGrpNm());
			u.setAthNm(userDetails.getAthNm());
			u.setAthLvl(userDetails.getAthLvl());
			u.setExpireTime(JwtUtil.remainExpiresDateByISO8601(jwt));
			u.setRefreshToken(refreshToken);
			
			ResultValue rv = new ResultValue(ResultCode.SUCCESS, u);

			ObjectMapper m = new ObjectMapper();
			String jsonBody = m.writeValueAsString(rv);
			response.setContentType("application/json; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.println(jsonBody);
		} catch (JsonProcessingException e) {
			logger.error(e.toString());
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
		HashMap<String, String> map = new HashMap<String, String>();
		String loginId = null;
		String exceptionMessage = exception.getMessage();
		System.out.println(exceptionMessage);
		
		if (exceptionMessage.contains("::")) {
			String infos = exceptionMessage.split("::")[1];
			String[] sets = infos.split(",");
			for ( String set : sets ) {
				String key = set.split("=")[0];
				String value = set.split("=")[1];
				map.put(key, value);
			}
		}
		loginId = (String) map.get("loginId");
		
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.setHeader(JwtInfo.HEADER_NAME, null);
		try {
			PrintWriter out = response.getWriter();
			
			// 횟수제한에 의한 실패 이외에는 failCnt를 저장한다.
			if ( exception instanceof FailCountOverException ) {
			} else {
				// 로그인 실패시 로그인실패이력 기록
				loginService.fail(loginId);
			}

			// fail 정보를 조회한다.
			Integer failCount = 1;
//			Date lastDt = null;
			String lastDtStr = "";
			if ( loginId != null ) {
				LoginInfo loginInfo = MemDB.LOGIN_INFO.select(loginId);
				if ( loginInfo != null ) {
					failCount = loginInfo.getFailCount();
//					lastDt = loginService.getPossibleDt(loginId);
					lastDtStr = loginService.getPossibleDtString(loginId);
				}
			}
			
			// 사용자에게 Response 메시지를 보낸다.
			if ( exception instanceof FailCountOverException || failCount > 4 ) {
				System.out.println(exception.getMessage());
				String msg = "{"
						+ "\"resultCode\": \"" + ResultCode.BLOCKED_LOGIN + "\""
						+ ",\"resultMessage\": \"" + ResultCode.BLOCKED_LOGIN.toString() + "\""
						+ ",\"resultData\": {"
						+     "\"msg\": \"인증에 " + failCount + "회 실패하였습니다. " + lastDtStr + "이후 로그인 가능합니다.\"" 
						+   "}"
						+ "}";
				out.println( msg );
				
			} else if ( exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException ) {
				System.out.println(exception.getMessage());
				String msg = "{"
						+ "\"resultCode\": \"" + ResultCode.NOT_MATCH_PWD + "\""
						+ ",\"resultMessage\": \"" + ResultCode.NOT_MATCH_PWD.toString() + "\"" 
						+ ",\"resultData\": {"
						+     "\"msg\": \"인증에 " + failCount + "회 실패하였습니다. 5회 실패시 10분간 로그인이 불가능합니다.\"" 
						+   "}"
						+ "}";
				out.println( msg );
				
			} else {
				out.println( "{\"resultCode\":\"500\"}");
			}
			
		} catch (IOException e) {
			logger.error(e.toString());
//		} catch (NullPointerException e) {
//			logger.error(e.toString());
		}
	}
	
}
