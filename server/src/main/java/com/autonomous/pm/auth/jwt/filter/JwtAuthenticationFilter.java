package com.autonomous.pm.auth.jwt.filter;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.autonomous.pm.auth.jwt.JwtAuthenticationToken;
import com.autonomous.pm.auth.jwt.JwtInfo;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public JwtAuthenticationFilter(RequestMatcher requestMatcher) {
		super(requestMatcher);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader(JwtInfo.HEADER_NAME);
		if (StringUtils.isEmpty(token)) {
			log.info(">>>>>>>>>>AccessDeniedException: \"Not empty Token\"<<<<<<<<<<");
			throw new AccessDeniedException("Not Empty Token");

		} else if (!JwtUtil.verify(token)) {
			log.info(">>>>>>>>>>AccessDeniedException: \"Not Verify Token\"<<<<<<<<<<");
			throw new AccessDeniedException("Not Verify Token");

		} else {
			String type = JwtUtil.tokenToJwt(token).getClaim("type").asString();
			// 유저 토큰인 경우 logout 처리 된 세션인지 확인한다.
			if ("user".equals(type) && MemDB.JWT.select(token) == null) {
				log.info(">>>>>>>>>>AccessDeniedException: \"Not Verify Token. Cause: user-logout \"<<<<<<<<<<");
				throw new AccessDeniedException("Not Verify Token.");
			}
			return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authResult);
		SecurityContextHolder.setContext(context);
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		getFailureHandler().onAuthenticationFailure(request, response, failed);
	}
}