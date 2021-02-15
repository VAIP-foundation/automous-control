package com.autonomous.pm.model.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ResRefreshJWT
{
    
	@NonNull
    @JsonProperty("pm_as_key")
    String pmAsKey;
	
	@NonNull
    String refreshToken;
    
    @JsonProperty("expire_time")
    String expireTime;
    
    
}