package com.autonomous.pm.service;

import javax.servlet.http.HttpServletRequest;

public interface LogService {

	void saveAccessLog(HttpServletRequest request);
	void saveAccessLog(HttpServletRequest request, String parameters);
	void saveAccessLog(HttpServletRequest request, String[] parametersArr);

}
