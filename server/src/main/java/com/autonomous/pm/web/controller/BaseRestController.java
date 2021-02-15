package com.autonomous.pm.web.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.autonomous.pm.auth.jwt.JwtInfo;
import com.autonomous.pm.util.JwtUtil;

public class BaseRestController {

    /**
     * 
     * @param target list object
     * @return String 변환된 문자열
     */
    public String convertString(Object target) {
        StringBuilder sb = new StringBuilder();

        if (target.getClass() == Collection.class) {

            Collection collection = (Collection) target;

            for (Object object : collection) {
                sb.append(object.toString());
            }
            return sb.toString();
        } else
            return target.toString();
    }

    /**
     * 
     * @param request
     * @return String
     */
    public String getUserId(HttpServletRequest request) {

        String token = request.getHeader(JwtInfo.HEADER_NAME);

        DecodedJWT jwt = JwtUtil.tokenToJwt(token);
        String userId = jwt.getClaim("id").asString();
        return userId;
    }

    /**
     * 
     * @param request
     * @return String remote ip address
     */
    public String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null)
            ip = request.getRemoteAddr();

        return ip;
    }
}
