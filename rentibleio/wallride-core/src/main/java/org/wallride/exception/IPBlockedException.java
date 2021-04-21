package org.wallride.exception;

import org.springframework.security.core.AuthenticationException;

public class IPBlockedException extends AuthenticationException {

	public IPBlockedException(String msg) {
		super(msg);
	}

	public IPBlockedException(String msg, Throwable t) {
		super(msg, t);
	}

}
