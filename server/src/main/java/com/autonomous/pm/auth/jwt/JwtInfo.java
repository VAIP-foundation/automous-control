package com.autonomous.pm.auth.jwt;

import com.auth0.jwt.algorithms.Algorithm;

public abstract class JwtInfo {

//	public static final String HEADER_NAME = "jwt-header";
	public static final String HEADER_NAME = "PM-AS-KEY";
//	public static final String HEADER_NAME = "token";

    public static final String ISSUER = "ICN";

    public static final String TOKEN_KEY = "ICN-PM-CNTR-SVR";

//    public static final long EXPIRES_LIMIT = 1L/24L;				// 1 hour
//    public static final long REFRESH_EXPIRES_LIMIT = 1L;			// 1 day
    public static final long EXPIRES_LIMIT_MINS = 1*1*30L;			// 30 min
    public static final long REFRESH_EXPIRES_LIMIT_MINS = 1*2*60L;	// 2 hour
//    public static final long EXPIRES_LIMIT_MINS = 5L;			// 5 min	// test
//    public static final long REFRESH_EXPIRES_LIMIT_MINS = 10L;	// 10 min	// test

    public static Algorithm getAlgorithm() {
        try {
            return Algorithm.HMAC256(JwtInfo.TOKEN_KEY);
        } catch (IllegalArgumentException e) {
            return Algorithm.none();
        }
    }
}