package com.autonomous.pm.util;

/**
 * 
 * 시간 측정을 위한 class
 * 
 * @author promaker
 *
 */
public class Timewatch {
	long start;
//	Date start;
	public Timewatch() {
		start = System.nanoTime();//new Date();
	}
	
	/**
	 * 
	 * Nano sec 단위 값을 반환한다.
	 * 
	 * @return
	 */
	public long stopNs() {
		long end = System.nanoTime();
//		Date end = new Date();
//		long due = end.getTime()  - start.getTime();
		long due = end - start;
		return due;
	}

	/**
	 * 
	 * float type의 msec 값을 반환한다.
	 * 
	 * @return
	 */
	public float stopMs() {
		long end = System.nanoTime();
//		Date end = new Date();
//		long due = end.getTime()  - start.getTime();
		long due = end - start;
		float val = due/1000000f;
		return val;
	}
}

