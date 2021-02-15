package com.autonomous.pm.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public abstract class DateUtil {

	public static Date nowToDate() {
		return Date.from(LocalDateTime.now().toInstant(ZoneOffset.ofHours(9)));
	}

	public static Date nowAfterDaysToDate(Long days) {
		return Date.from(LocalDateTime.now().plusDays(days).toInstant(ZoneOffset.ofHours(9)));
	}
	
	public static Date nowAfterMinutesToDate(Long minutes) {
		return Date.from(LocalDateTime.now().plusMinutes(minutes).toInstant(ZoneOffset.ofHours(9)));
	}

	/**
	 * 
	 * @return
	 */
	public static String getTimeNowFormat() {

		Calendar cal = Calendar.getInstance();

		String ret = String.format("%04d%02d%02d%02d%02d%02d%03d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND));

		return ret;
	}

	/**
	 * 
	 * @return
	 */
	public static String getTodayFormat() {

		Calendar cal = Calendar.getInstance();

		String ret = String.format("%04d%02d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH));

		return ret;
	}


	/**
	 * 날짜를 YYYYMMDDhhmissfff format 문자열로 변환한다.
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeFormat(Date date) {

		if (date == null) {
			return "";
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		String ret = String.format("%04d%02d%02d%02d%02d%02d%03d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND));

		return ret;
	}

	/**
	 * 
	 * 날짜를 YYYY/MM/DD format 문자열로 변환한다.
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateFormat(Date date) {

		if (date == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		String ret = String.format("%04d/%02d/%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH));

		return ret;
	}

	/**
	 * 
	 * 
	 * @param date
	 * @return
	 */
	public static String convertToYYYYMMDD(Date date,String sep) {

		if (date == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String ret ;

		if(sep == null){
			ret = String.format("%04d%02d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH));
		}else
		{
			ret = String.format("%04d%s%02d%s%02d", cal.get(Calendar.YEAR),sep, cal.get(Calendar.MONTH) + 1,sep,
			cal.get(Calendar.DAY_OF_MONTH));
		}

		return ret;
	}


	/**
	 * 
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeFormat2(Date date) {

		if (date == null) {
			return "";
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		String ret = String.format("%04d/%02d/%02d %02d:%02d:%02d.%03d", cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND));

		return ret;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getTimeFormatYYYYMM(Date date) {

		if (date == null) {
			return "";
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		String ret = String.format("%04d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);

		return ret;
	}
	

	/**
	 * 
	 * @param ymdhmsm
	 * @return Date
	 */
	public static Date convertStringToDate(String ymdhmsm) {

		Date date;
		Calendar cal = Calendar.getInstance();

		int y = Integer.valueOf(ymdhmsm.substring(0, 4));
		int m = Integer.valueOf(ymdhmsm.substring(4, 6));
		int d = Integer.valueOf(ymdhmsm.substring(6, 8));
		int h = Integer.valueOf(ymdhmsm.substring(8, 10));
		int mi = Integer.valueOf(ymdhmsm.substring(10, 12));
		int s = Integer.valueOf(ymdhmsm.substring(12, 14));

		cal.set(Calendar.YEAR, y);
		cal.set(Calendar.MONTH, m - 1);
		cal.set(Calendar.DAY_OF_MONTH, d);
		cal.set(Calendar.HOUR_OF_DAY, h);
		cal.set(Calendar.MINUTE, mi);
		cal.set(Calendar.SECOND, s);

		date = cal.getTime();

		//String str = date.toString();

		return date;
	}

	/**
	 * String 을 Date 형으로 변환
	 * 
	 * @param ymdhmsm
	 * @return Date
	 */
	public static Date convertYYYYMMDDToDate(String ymdhmsm) {
		Date date;

		if(ymdhmsm == null)
			return null;
		
		if(ymdhmsm.length()<8)
			return null;

		ymdhmsm= ymdhmsm.replaceAll("-", "");

		Calendar cal = Calendar.getInstance();

		int y = Integer.valueOf(ymdhmsm.substring(0, 4));
		int m = Integer.valueOf(ymdhmsm.substring(4, 6));
		int d = Integer.valueOf(ymdhmsm.substring(6, 8));


		cal.set(Calendar.YEAR, y);
		cal.set(Calendar.MONTH, m - 1);
		cal.set(Calendar.DAY_OF_MONTH, d);

		date = cal.getTime();
		return date;
	}
	
	
	
	
	


	/**
	 * 날짜를 주어진 format 형식에 맞게 String으로 변환한다
	 * @param date
	 * @param formatter
	 * @return
	 */
	public static String getUtcDatetimeByFormat(Date date, String formatter) {
		
		if (date == null) {
			return "";
		}

//		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		String ret = formatter
				.replace("YYYY", String.format("%04d", cal.get(Calendar.YEAR)))
				.replace("MM", String.format("%02d", cal.get(Calendar.MONTH)+1))
				.replace("DD", String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)))
				.replace("hh", String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)))
				.replace("mi", String.format("%02d", cal.get(Calendar.MINUTE)))
				.replace("ss", String.format("%02d", cal.get(Calendar.SECOND)))
				.replace("fff", String.format("%03d", cal.get(Calendar.MILLISECOND)));

		return ret;
	}
	
	/**
	 * String 을 Date 형으로 변환
	 * 
	 * @param ymdhmsm
	 * @param formatter: YYYYMMDDhhmissfff
	 * @return Date
	 */
	public static Date convertStringToUtcDate(String dateStr, String formatter) {
		
		Date date;
//		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		Calendar cal = Calendar.getInstance();
		
		if ( formatter.contains("YYYY") ) {
			int start = formatter.indexOf("YYYY");
			int end = formatter.indexOf("YYYY")+4;
			int y = Integer.valueOf(dateStr.substring(start, end));
			cal.set(Calendar.YEAR, y);
		}
		if ( formatter.contains("MM") ) {
			int start = formatter.indexOf("MM");
			int end = formatter.indexOf("MM")+2;
			int m = Integer.valueOf(dateStr.substring(start, end));
			cal.set(Calendar.MONTH, m - 1);
		}
		if ( formatter.contains("DD") ) {
			int start = formatter.indexOf("DD");
			int end = formatter.indexOf("DD")+2;
			int d = Integer.valueOf(dateStr.substring(start, end));
			cal.set(Calendar.DAY_OF_MONTH, d);
		}
		if ( formatter.contains("hh") ) {
			int start = formatter.indexOf("hh");
			int end = formatter.indexOf("hh")+2;
			int h = Integer.valueOf(dateStr.substring(start, end));
			cal.set(Calendar.HOUR_OF_DAY, h);
		}
		if ( formatter.contains("mi") ) {
			int start = formatter.indexOf("mi");
			int end = formatter.indexOf("mi")+2;
			int mi = Integer.valueOf(dateStr.substring(start, end));
			cal.set(Calendar.MINUTE, mi);
		}
		if ( formatter.contains("ss") ) {
			int start = formatter.indexOf("ss");
			int end = formatter.indexOf("ss")+2;
			int s = Integer.valueOf(dateStr.substring(start, end));
			cal.set(Calendar.SECOND, s);
		}
		if ( formatter.contains("fff") ) {
			int start = formatter.indexOf("fff");
			int end = formatter.indexOf("fff")+3;
			int f = Integer.valueOf(dateStr.substring(start, end));
			cal.set(Calendar.MILLISECOND, f);
		}

		date = cal.getTime();

		//String str = date.toString();

		return date;
	}
	
}