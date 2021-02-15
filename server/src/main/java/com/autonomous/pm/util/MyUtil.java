package com.autonomous.pm.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autonomous.pm.domain.structure.Obs;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.TGrpPoi;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyUtil {
	public static final Logger logger = LoggerFactory.getLogger(MyUtil.class);
	
	/**
	 * @param booleans
	 * @return
	 */
	public static byte booleansToByte(Boolean[] booleans) {
		if ( booleans == null ) {
			return (byte)0;
		}
		
		byte byteResult = 0;
		for (int i = 0; i < booleans.length; i++) {
			if (booleans[i]) {
				byteResult += (byte) (1 << i);
			}
		}
		return byteResult;
	}
	
	/**
	 * @param byte
	 * @return
	 */
	public static Boolean[] byteToBooleans(Byte b) {
		Boolean booleans[] = new Boolean[4];
		if ( b == null ) {
			Arrays.fill(booleans, Boolean.FALSE);
			return booleans;
		}
		
		String bitStr = String.format("%4s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
		
		for (int i = 0; i < bitStr.length(); i++) {
			if ( "0".equals(bitStr.substring(i, i+1)) ) {
				booleans[i] = Boolean.FALSE;
			} else {
				booleans[i] = Boolean.TRUE;
			}
		}
		return booleans;
	}
	
	
	/**
	 * 
	 * 
	 * @param kmPerHour
	 * @return
	 */
	public static double speedToDistance(int kmPerHour) {
		double distanceMeter = 0;

		distanceMeter = (double) kmPerHour / 3.6d;

		return distanceMeter;
	}

	/**
	 * 
	 * @param m  - 이동 거리
	 * @param cc - 연료 소모량
	 * @return
	 */
	public static BigDecimal calcFuelEco(BigDecimal m, BigDecimal cc) {
		if (cc.intValue() == 0)
			return new BigDecimal("0.0");
		m = m.setScale(30, BigDecimal.ROUND_HALF_UP);
		return m.divide(cc, 2, BigDecimal.ROUND_CEILING);
	}


	/**
	 * 
	 * 
	 * @param start
	 * @param end
	 * @param unit
	 * @return
	 */
	public static long calcTimeDuration(Date start, Date end, int unit) {

		if (start == null || end == null)
			return 0;
		if (unit == Calendar.SECOND)
			return (end.getTime() - start.getTime()) / 1000;
		else
			return (end.getTime() - start.getTime());

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

		String str = date.toString();

		return date;
	}

	public static int fromByteArray(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getInt();
	}

	public static String toHexString(byte[] bytes) {
		String msg = "";
		for (int i = 0; i < bytes.length; i++) {
			msg += String.format("%02X", bytes[i]);
		}
		return msg;
	}

	public static String toHexString(long value) {
		String msg = String.format("%016X", value);
		return msg;
	}

	public static String toHexString(int value) {
		String msg = String.format("%08X", value);
		return msg;
	}

	public static String toHexString(short value) {
		String msg = String.format("%04X", value);
		return msg;
	}

	public static String toHexString(byte value) {
		String msg = String.format("%02X", value);
		return msg;
	}

	/**
	 * hex string 을 byte array 로 변환
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] hexStringToBytes(String s) {
		// 짝수가 아니면 return null
		if (s.length() % 2 != 0) {
			return null;
		}
		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < b.length; i++) {
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		return b;
	}

	public static String byteArrayToString(byte[] array) {

		String ret = "";
		for (int i = 0; i < array.length; i++) {
			if (array[i] != 0)
				ret += (char) array[i];
		}

		return ret;
	}

	public static byte[] copyByteArray(byte[] src, byte[] dst, int len) {
		for (int i = 0; i < len; i++) {
			if (i < src.length && i < dst.length)
				dst[i] = src[i];
		}
		return dst;
	}

	/**
	 * 
	 * 0 을 앞자리에 padding 한다.
	 * 
	 * @param originLength
	 * @param destLength
	 * @return
	 */
	static String repeatZero(int originLength, int destLength) {
		int gap = destLength - originLength;
		if (gap <= 0)
			return "";
		String ret = "";
		for (int i = 0; i < gap; i++) {
			ret += "0";
		}
		return ret;
	}

	public static String intToLonString(long iLon) {
		String sLon = "", str = String.valueOf(iLon);
		if (str.length() < 10)
			str += repeatZero(str.length(), 10);
		if (str.length() > 3) {
			sLon = str.substring(0, 3) + ".";
			sLon += str.substring(3);
		} else {
			sLon = str;
		}
		return sLon;
	}

	public static String intToLatString(long iLat) {
		String sLat = "", str = String.valueOf(iLat);
		if (str.length() < 9)
			str += repeatZero(str.length(), 9);
		if (str.length() > 2) {
			sLat = str.substring(0, 2) + ".";
			sLat += str.substring(2);
		} else {
			sLat = str;
		}
		return sLat;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String doubleToString(Double value) {
		if (value == null)
			return "";
		String sLat = String.valueOf(value);
		return sLat;
	}

	public static String doubleToGpsString(Double value) {
		if (value == null)
			return "";
		String sLat = doubleRoundToString(value,9);//String.valueOf(value);
		return sLat;
	}

	/**
	 * 
	 * 
	 * @param val
	 * @return
	 */
	public static Integer increamentInteger(Integer val) {
		Integer newVal = 0;
		if (val == null)
			newVal = 0;
		else
			newVal = val + 1;
		return newVal;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static long floatStrToLong(String str) {
		if (str == null)
			return 0;
		long val = 0;
		if (str.indexOf(".") >= 0) {
			str = str.replace(".", "");
		}
		val = Long.valueOf(str);
		return val;
	}

	/**
	 * 
	 * 
	 * @param str
	 * @return
	 */
	public static double doubleStrToDouble(String str) {
		if (str == null)
			return 0;
		double val = 0;
		val = Double.valueOf(str);
		return val;
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public static boolean isTimePast(Date time, int timeGapMs) {
		Date now = new Date();

		long t = calcTimeDuration(time, now, Calendar.MILLISECOND);
		// logger.debug("== calcTimeDuration={} = {} -
		// {}",t,time.toString(),now.toString());
		if (t < timeGapMs)
			return false;
		return true;
	}

	public static String isNullStringZero(String val) {

		return val == null ? "0" : val;

	}

	/**
	 * 
	 * @param val
	 * @return
	 */
	public static Integer isNullZero(Integer val) {

		return val == null ? 0 : val;

	}

	/**
	 * 
	 * @param val
	 * @return
	 */
	public static float isNullFloatZero(Float val) {

		return val == null ? 0 : val;

	}

	public static String isNull(String val) {

		return val == null ? "" : val;

	}

	public static boolean isEmpty(String val) {
		if (val.equals(null) || val.equals("")) {
			return true;
		}
		return false;

	}

	/**
	 * 
	 * 
	 * @param d
	 * @param decimalPlace
	 * @return
	 */
	public static Float floatRound(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(d);// Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	/**
	 * 
	 * 
	 * @param d
	 * @param decimalPlace
	 * @return
	 */
	public static Double doubleRound(Double d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(d);// Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	public static String doubleRoundToString(Double d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(d);// Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.toPlainString();
	}

	/**
	 *
	 * @param dType
	 * @return
	 */
	public static int v2xDeivceTypeToTerminalType(int dType) {
		int tType;
		switch (dType) {
		case 0xDA: // V2X OBD
			tType = 11;
			break;
		case 0xDB: // V2X Client
			tType = 12;
			break;
		case 0xEF: // HD_MAP
			tType = 13;
			break;
		case 0xEA: // IoT
			tType = 00;
			break;
		case 0xEB: // T-Remote Eye terminal
			tType = 21;
			break;
		default:
			tType = 0;
			break;
		}
		return tType;
	}

	/**
	 * 
	 * generate UUID without '-'
	 * 
	 * @return String UUID
	 */
	public static String genUUID() {
		return MyUtil.genUUID(true);
	}

	public static String genUUID(boolean removeHyphen ) {
		if(removeHyphen)
			return UUID.randomUUID().toString().replaceAll("-", "");
		else
			return UUID.randomUUID().toString();
	}

	/**
	 * 
	 * @param vrn
	 * @return
	 */
	public static String getV2xVid(String vrn) {

		String str0 = "0" + vrn.substring(0, 1);
		String str1 = "0" + vrn.substring(1, 2);
		String str2 = "0" + vrn.substring(2, 3);
		String str3 = "0" + vrn.substring(3, 4);
		String str4 = str0 + str1 + str2 + str3;
		return str4.trim();
	}

	public static int byteToInt(byte b) {
		return (int) ((int) b & 0xff);
	}

	/**
	 * @param utc
	 * @return
	 */
	public static Date utcToLocal(Date utc) {
		long lutc = utc.getTime();
		TimeZone z = TimeZone.getDefault();
		int offset = z.getOffset(lutc);
		lutc += offset;
		Date local = new Date(lutc);
		return local;
	}

	/**
	 * 
	 * @param local
	 * @return
	 */
	public static Date localToUtc(Date local) {
		long llocal = local.getTime();
		TimeZone z = TimeZone.getDefault();
		int offset = z.getOffset(llocal);
		llocal -= offset;
		Date utc = new Date(llocal);
		return utc;
	}

	public static int toInteger(String val) {
		int i = Integer.parseInt(val);
		return i;
	}

	public static byte[] reverse(byte[] s) {
		byte[] d = new byte[s.length];
		for (int i = 0; i < s.length; i++) {
			d[s.length - 1 - i] = s[i];
		}
		return d;
	}

	public static float floatFromString(String param) {
		float f = 0.0f;
		
		if(param == null || param.isEmpty())
			return f;
		
		try {

			f = Float.parseFloat(param);
		} catch (NumberFormatException nfe) {
			f = 0.0f;
		}
		return f;
	}

	/**
	 * @param in
	 * @param digit
	 * @return
	 */
	public static long longRoundHalfUp(long in, int digit) {
		long i = (long) Math.pow(10, digit);
		long newVal = in + i / 2;
		newVal = newVal / i;
		newVal *= i;
		return newVal;
	}
	
	/**
	 * @param <T>
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static <T> T getValueOrDefault(T value, T defaultValue) {
	    return value == null ? defaultValue : value;
	}
	
	
	public static String getTerminalByType(String ty) {
		String terminal = null;
		if ( "ARV".equals(ty) ) {
			terminal = "T1";
		} else if ( "DPT".equals(ty) ) {
			terminal = "T2";
		}
		return terminal;
	}
	
	public static String getTypeByTerminal(String terminal) {
		String ty = null;
		if ( "T1".equals(terminal) ) {
			ty = "ARV";
		} else if ( "T2".equals(terminal) ) {
			ty = "DPT";
		}
		return ty;
	}
	
	public static boolean isT1(String text) {
		if ( "T1".equals(text) ) {
			return true;
		} else if ( "ARV".equals(text) ) {
			return true;
		}
		return false;
	}
	
	public static boolean isT2(String terminal) {
		if ( "T2".equals(terminal) ) {
			return true;
		} else if ( "DPT".equals(terminal) ) {
			return true;
		}
		return false;
	}
	
	public static Long getIdGopByTerminal(String terminal) {
		Long idGop = null;
		TGrpPoi gPoi = MemDB.GRP_POI.selectAll().stream().filter(g->g.getTerm().equals(terminal)).findFirst().orElse(null);
		if ( gPoi != null ) {
			idGop = gPoi.getIdGop();
		}
		return idGop;
	}

	public static boolean isBatLow(Long idV) {
		Integer drivingAbleSec = 1200;	// TODO: application.properties 에서 설정한 값으로 써야 함.. 현재 null 값 들어있음
		Integer dr_s_abl = MemDB.SNSR.select(idV).getData().getDr_s_abl();	// 운행가능시간.초단위(분단위갱신)
		return (dr_s_abl < drivingAbleSec);
	}
}
