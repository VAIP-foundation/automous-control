package com.autonomous.pm.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.autonomous.pm.auth.jwt.JwtInfo;
import com.autonomous.pm.dao.gen.TAccessLogHstMapper;
import com.autonomous.pm.model.Do.TAccessLogHst;
import com.autonomous.pm.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LogServiceImpl implements LogService {

	@Autowired
	TAccessLogHstMapper accessLogHstMapper;
	
	@Override
	public void saveAccessLog(HttpServletRequest request) {
		String parameters = request.getQueryString();
		save(request, parameters);
	}
	
	@Override
	public void saveAccessLog(HttpServletRequest request, String parameters) {
		save(request, parameters);
	}

	@Override
	public void saveAccessLog(HttpServletRequest request, String... parametersArr) {
		StringBuffer parameters = new StringBuffer();
		for(String param : parametersArr) {
			parameters.append(",").append(param);
		}
		save(request, parameters.toString().substring(1));
	}
	
	private void save(HttpServletRequest request, String parameters) {
		try {
			String url = request.getRequestURL().toString();
			String method = request.getMethod();
			
			String accessToken = request.getHeader(JwtInfo.HEADER_NAME);
			String username = "";
			if ( accessToken != null ) {	// token요청 API 경우에는 아직 accessToken이 없음
				DecodedJWT jwt = JwtUtil.tokenToJwt(accessToken);
				username = jwt.getClaim("id").asString();
			}
			
			TAccessLogHst record =
					TAccessLogHst.builder()
					.url(url)
					.method(method)
					.username(username)
					.parameters(parameters)
					.build();
			
			accessLogHstMapper.insertSelective(record);
			
		} catch (RuntimeException e) {
			log.error(e.toString());
		}
		
		
	}
	
}
