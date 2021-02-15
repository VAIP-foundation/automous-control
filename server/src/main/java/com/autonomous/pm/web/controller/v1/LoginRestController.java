package com.autonomous.pm.web.controller.v1;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.autonomous.pm.auth.UsrDetailsImpl;
import com.autonomous.pm.auth.ajax.AjaxUserDetailsService;
import com.autonomous.pm.auth.jwt.JwtInfo;
import com.autonomous.pm.auth.jwt.JwtUserDetailsService;
import com.autonomous.pm.dao.UsrGrpMapper;
import com.autonomous.pm.dao.UsrMapper;
import com.autonomous.pm.domain.common.ResultCode;
import com.autonomous.pm.domain.common.ResultValue;
import com.autonomous.pm.model.Dto.ReqAdminLoginHst;
import com.autonomous.pm.model.Dto.ResAdminLoginHst;
import com.autonomous.pm.model.Dto.ResRefreshJWT;
import com.autonomous.pm.service.LogServiceImpl;
import com.autonomous.pm.service.restful.LoginHstServiceImpl;
import com.autonomous.pm.service.restful.UsrAthServiceImpl;
import com.autonomous.pm.util.JwtUtil;
import com.autonomous.pm.web.controller.BaseRestController;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class LoginRestController extends BaseRestController {

	public static final Logger logger = LoggerFactory.getLogger(LoginRestController.class);

	@Autowired
	private AjaxUserDetailsService ajaxUserDetailsService;
	@Autowired
	private UsrAthServiceImpl usrAthServiceImpl;
	@Autowired
	private LoginHstServiceImpl loginHstServiceImpl;
	@Autowired
	LogServiceImpl logService;

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public void getAccessTokenByRefreshToken(HttpServletRequest request, HttpServletResponse response)
			throws BindException {
		String refreshToken = request.getHeader(JwtInfo.HEADER_NAME);
		logger.info("getAccessTokenByRefreshToken() refreshToken={}", refreshToken);
		logService.saveAccessLog(request, refreshToken);

		if (StringUtils.isEmpty(refreshToken)) {
			throw new AccessDeniedException("Not empty Token");
		} else if (!JwtUtil.verify(refreshToken)) {
			throw new AccessDeniedException("Not verify Token");
		} else {

			// 응답객체 생성
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

			DecodedJWT jwt = JwtUtil.tokenToJwt(refreshToken);
			String username = jwt.getClaim("id").asString();
			UsrDetailsImpl userDetails = (UsrDetailsImpl) ajaxUserDetailsService.loadUserByUsername(username);
//			UserDetails userDetails = userDetailsService.loadUserByUsername(refreshToken);

			String newAccessToken = JwtUtil.createAccessToken(userDetails);
			String newRefreshToken = JwtUtil.createRefreshToken(userDetails, newAccessToken);
			response.setHeader(JwtInfo.HEADER_NAME, newAccessToken);
			try {

				ResRefreshJWT res = new ResRefreshJWT(newAccessToken, newRefreshToken);
				DecodedJWT jwt2 = JwtUtil.tokenToJwt(newAccessToken);
				res.setExpireTime(JwtUtil.remainExpiresDateByISO8601(jwt2));

				ResultValue rv = new ResultValue(ResultCode.SUCCESS, res);

				ObjectMapper m = new ObjectMapper();
				String jsonBody = m.writeValueAsString(rv);
				response.setContentType("application/json; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter();
				out.println(jsonBody);
				
			} catch (JsonParseException e) {
				logger.error(e.toString());
			} catch (JsonMappingException e) {
				logger.error(e.toString());
			} catch (IOException e) {
				logger.error(e.toString());
			}
		}

	}

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> logout(HttpServletRequest request) throws BindException {
		String accessToken = request.getHeader(JwtInfo.HEADER_NAME);
		logger.info("logout() accessToken={}", accessToken);
		logService.saveAccessLog(request, accessToken);

		if (StringUtils.isEmpty(accessToken)) {
			throw new AccessDeniedException("Not empty Token");
		} else if (!JwtUtil.verify(accessToken)) {
			throw new AccessDeniedException("Not verify Token");
		} else {
			DecodedJWT jwt = JwtUtil.tokenToJwt(accessToken);
			String username = jwt.getClaim("id").asString();
			UsrDetailsImpl userDetails = (UsrDetailsImpl) ajaxUserDetailsService.loadUserByUsername(username);

			usrAthServiceImpl.removeJwtSessionByAccessToken(accessToken);

			loginHstServiceImpl.insertLogoutHst(request, userDetails);
		}
		return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, null), HttpStatus.OK);
	}
}