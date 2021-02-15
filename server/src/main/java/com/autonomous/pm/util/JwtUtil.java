package com.autonomous.pm.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.security.core.userdetails.UserDetails;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.autonomous.pm.auth.UsrDetailsImpl;
import com.autonomous.pm.auth.jwt.JwtInfo;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.JwtSession;

public abstract class JwtUtil {

	
	public static String createAccessToken(UserDetails userDetails) {
		String token = createToken(userDetails, DateUtil.nowAfterMinutesToDate(JwtInfo.EXPIRES_LIMIT_MINS));
		
		if ( userDetails instanceof UsrDetailsImpl ) {
			JwtSession jwtSession = JwtSession.builder()
					.token(token)
					.type("1")
					.userDetails(userDetails)
					.accessToken(token)
					.build();
			MemDB.JWT.insertSafety(token, jwtSession);
		}
		return token;
	}
	
	public static String createRefreshToken(UserDetails userDetails, String accessToken) {
		String token = createToken(userDetails, DateUtil.nowAfterMinutesToDate(JwtInfo.REFRESH_EXPIRES_LIMIT_MINS));
		
		if ( userDetails instanceof UsrDetailsImpl ) {
			JwtSession jwtSession = JwtSession.builder()
					.token(token)
					.type("2")
					.userDetails(userDetails)
					.accessToken(accessToken)	// 로그 아웃 시 accessToken을 이용한 로그아웃 처리를 위해 저장한다.
					.build();
			MemDB.JWT.insertSafety(token, jwtSession);
		}
		return token;
	}
	
	
	private static String createToken(UserDetails userDetails, Date date) {
		String athLvl = "0";
		String type = "vhcl";
		if ( userDetails instanceof UsrDetailsImpl ) {
			athLvl = ((UsrDetailsImpl)userDetails).getAthLvl().toString();
			type = "user";
		}
		try {
			return JWT.create()
					.withIssuer(JwtInfo.ISSUER)
					.withClaim("id", userDetails.getUsername())
					.withClaim("role", userDetails.getAuthorities().toArray()[0].toString())
					.withClaim("athLvl", athLvl)
					.withClaim("type", type)
					.withExpiresAt(date)
					.sign(JwtInfo.getAlgorithm());
		} catch (JWTCreationException createEx) {
			return null;
		}
	}
	
	public static Boolean verify(String token) {
		try {
			JWTVerifier verifier = JWT.require(JwtInfo.getAlgorithm()).build();
			verifier.verify(token);
			
			return Boolean.TRUE;
		} catch (JWTVerificationException verifyEx) {
			return Boolean.FALSE;
		}
	}

	
	public static String refreshToken(UserDetails userDetails) {
		return createToken(userDetails, DateUtil.nowAfterMinutesToDate(JwtInfo.REFRESH_EXPIRES_LIMIT_MINS));
	}
	
	
	public static DecodedJWT tokenToJwt(String token) {
		try {
			return JWT.decode(token);
		} catch (JWTDecodeException decodeEx) {
			return null;
		}
	}
	
	public static int remainExpiresMinutes(DecodedJWT decodedJWT) {
		Date expiresDt = decodedJWT.getExpiresAt();
		long sysMillis = System.currentTimeMillis();
		long expiresMillis = expiresDt.getTime();
		int expiresSec = (int) ((expiresMillis - sysMillis)/1000);
		int expiresMin = (int) (expiresSec/60);
		
		return expiresMin;
	}
	
	public static String remainExpiresDateByISO8601(DecodedJWT decodedJWT) {
		java.util.Date expiresDt = decodedJWT.getExpiresAt();
		
		SimpleDateFormat sdf;
		sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		sdf.setTimeZone(TimeZone.getTimeZone("KST"));
		
		return sdf.format(expiresDt);
	}
	
}