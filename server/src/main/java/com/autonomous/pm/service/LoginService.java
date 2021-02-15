package com.autonomous.pm.service;

import java.util.Date;

import com.autonomous.pm.model.LoginInfo;

public interface LoginService {

	void success(String loginId);
	LoginInfo fail(String loginId);
	Date getPossibleDt(String loginId);
	String getPossibleDtString(String loginId);
	boolean isPossible(String loginId);
	boolean isNotPossible(String loginId);
	
}