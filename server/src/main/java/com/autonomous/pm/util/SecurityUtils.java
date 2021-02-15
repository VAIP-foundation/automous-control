package com.autonomous.pm.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.autonomous.pm.auth.UsrDetailsImpl;

public abstract class SecurityUtils {

	public static String currentUserName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if ( authentication.getPrincipal() instanceof UsrDetailsImpl ) {
//			UsrDetailsImpl userDetails = (UsrDetailsImpl) authentication.getPrincipal();
//			return userDetails.getLoginId();
//		} else {
//			return "unknown";
//		}
		String username = authentication.getPrincipal().toString();
		return username;
	}

}