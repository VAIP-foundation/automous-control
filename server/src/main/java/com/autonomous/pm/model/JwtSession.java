package com.autonomous.pm.model;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
//@RequiredArgsConstructor
@Builder
public class JwtSession
{
    @NonNull
    String token;

    @NonNull
    String type;	// 1: User Access-Token
    				// 2: User Refresh-Token
				    // 3: Vehicle Access-Token
				    // 4: Vehicle Refresh-Token
    
    @NonNull
    UserDetails userDetails;
    
    @NonNull
    String accessToken;
    
    public boolean isAccessToken() {
    	return "1".equals(this.type) || "3".contentEquals(this.type);
    }
}