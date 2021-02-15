package com.autonomous.pm.auth;

import org.springframework.security.core.AuthenticationException;

public class FailCountOverException extends AuthenticationException {
	private static final long serialVersionUID = 8411690680996859495L;
	public FailCountOverException(String msg) {
		super(msg);
	}
	public FailCountOverException(String msg, Throwable t) {
		super(msg, t);
	}
}
