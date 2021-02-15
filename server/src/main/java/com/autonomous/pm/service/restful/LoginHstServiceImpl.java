package com.autonomous.pm.service.restful;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.autonomous.pm.auth.UsrDetailsImpl;
import com.autonomous.pm.auth.ajax.AjaxUserDetailsService;
import com.autonomous.pm.dao.LoginHstMapper;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.TLoginHst;
import com.autonomous.pm.model.Dto.ReqAdminLoginHst;
import com.autonomous.pm.model.Dto.ResAdminLoginHst;
import com.autonomous.pm.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginHstServiceImpl implements LoginHstService {

	public static final Logger logger = LoggerFactory.getLogger(LoginHstServiceImpl.class);
	
	@Autowired
	private AjaxUserDetailsService ajaxUserDetailsService;

	@Autowired
	LoginHstMapper loginHstMapper;

	
	// 로그인 성공시 login-log 입력
	public void insertLoginHst(HttpServletRequest request, UsrDetailsImpl userDetails) {
		insertLoginHst(request, userDetails, "1");
	}

	// 로그아웃 성공시 logout-log 입력
	public void insertLogoutHst(HttpServletRequest request, UsrDetailsImpl userDetails) {
		insertLoginHst(request, userDetails, "2");
	}
	
	private void insertLoginHst(HttpServletRequest request, UsrDetailsImpl userDetails, String loginTy) {
		TLoginHst tLoginHst = new TLoginHst();
		tLoginHst.setLoginDt(new Date());
		tLoginHst.setIdUsr(userDetails.getIdUsr());
		tLoginHst.setLoginId(userDetails.getLoginId());
		tLoginHst.setPeerIp(getClientIp(request));
		tLoginHst.setHttpUa(request.getHeader("user-agent"));
		tLoginHst.setLoginTy(loginTy);
		
		log.debug(">>"+tLoginHst);
		postAdminLoginHstLs(tLoginHst);
	}

	public static String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	@Override
	public List<ResAdminLoginHst> getAdminLoginHstLs(ReqAdminLoginHst reqParam) {
		return loginHstMapper.getAdminLoginHstLs(reqParam);
	}

	@Override
	public int postAdminLoginHstLs(TLoginHst reqParam) {
		return loginHstMapper.postAdminLoginHstLs(reqParam);
	}

	@Override
	public void postAdminLogoutHstByToken(String token, String loginTy) {
		DecodedJWT jwt = JwtUtil.tokenToJwt(token);
		String username = jwt.getClaim("id").asString();
		UsrDetailsImpl userDetails = (UsrDetailsImpl) ajaxUserDetailsService.loadUserByUsername(username);
		
		TLoginHst tLoginHst = new TLoginHst();
		tLoginHst.setLoginDt(new Date());
		tLoginHst.setIdUsr(userDetails.getIdUsr());
		tLoginHst.setLoginId(userDetails.getLoginId());
		tLoginHst.setPeerIp(null);
		tLoginHst.setHttpUa(null);
		tLoginHst.setLoginTy(loginTy);	// logout - self
		
		postAdminLoginHstLs(tLoginHst);
	}
	
}
