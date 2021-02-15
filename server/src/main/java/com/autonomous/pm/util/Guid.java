package com.autonomous.pm.util;

import java.util.UUID;

public class Guid {

	/**
	 * generate UUID without '-'
	 * 
	 * @return String UUID
	 */
	public static String genUUID() {
		return Guid.genUUID(true);
	}

	/**
	 * generate UUID
	 * 
	 * @param removeHyphen - 제거 여부
	 * @return String
	 */
	public static String genUUID(boolean removeHyphen) {
		if (removeHyphen)
			return UUID.randomUUID().toString().replaceAll("-", "");
		else
			return UUID.randomUUID().toString();
	}

}