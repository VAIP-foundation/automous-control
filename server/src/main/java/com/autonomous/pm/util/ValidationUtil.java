package com.autonomous.pm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
//@Slf4j
public class ValidationUtil {

	/**
	 * @param passwd
	 * @return
	 */
	static public boolean isPasswordValidate(String passwd) {
//		return "success".equals(passwordValidator(passwd));
		return true;	// 미사용
	}
	static public boolean isNotPasswordValidate(String passwd) {
		return !isPasswordValidate(passwd);
	}
	
	/**
	 *
	 * @param passwd
	 * @return
	 */
	static public String passwordValidator(String passwd) {
		String returnValue = "success";

		int length_min = 10;
		int length_max = 100;
		String length_msg = "비밀번호는 " + length_min + "자 이상, " + length_max + "자 이하로 구성하세요.";
		
		Pattern space_p = Pattern.compile("[ ]");
		Matcher space_m = space_p.matcher(passwd);
		String  space_msg = "비밀번호에 공백문자를 허용하지 않습니다.";
		
		Pattern complex1_p = Pattern.compile("[a-zA-Z]");	// 영문 검사
		Matcher complex1_m = complex1_p.matcher(passwd);
		Pattern complex2_p = Pattern.compile("[0-9]");		// 숫자 검사
		Matcher complex2_m = complex2_p.matcher(passwd);
		Pattern complex3_p = Pattern.compile("[!@$%^&*]");	// 특수문자 검사
		Matcher complex3_m = complex3_p.matcher(passwd);
		String  complex_msg = "비밀번호는 영문,숫자,특수문자(!@$%^&* 만 허용)를 조합하여 구성하세요.";
		
		// 동일문자 연속 체크
		Pattern continue1_p = Pattern.compile("(\\w)\\1\\1\\1");		// 동일문자 4번 입력 체크
		Matcher continue1_m = continue1_p.matcher(passwd);
		Pattern continue2_p = Pattern.compile("([\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-_+<>@\\#$%&\\\\\\=\\(\\'\\\"])\\1\\1\\1");
		Matcher continue2_m = continue2_p.matcher(passwd);
		String  continue_msg = "비밀번호에 동일문자를 4번 이상 사용할 수 없습니다.";
		
			
		if (passwd.length() < length_min || passwd.length() > length_max) { // 자릿수 검증
			returnValue = length_msg;
			
		} else if (space_m.find()) { // 패스워드 공백 문자열 체크
			returnValue = space_msg;
			
		} else if (!(complex1_m.find() && complex2_m.find() && complex3_m.find())) { // 정규식 이용한 패턴 체크
			returnValue = complex_msg;
			
		}

		return returnValue;
	}

	/**
	 *
	 * @param spaceCheck
	 * @return
	 */
	static public boolean spaceCheck(String spaceCheck) {
		for (int i = 0; i < spaceCheck.length(); i++) {
			if (spaceCheck.charAt(i) == ' ')
				return true;
		}

		return false;
	}

	/**
	 *
	 * @param numberCheck
	 * @return
	 */
	static public boolean continueNumberCheck(String numberCheck) {
		int o = 0;
		int d = 0;
		int p = 0;
		int n = 0;
		int limit = 4;

		for (int i = 0; i < numberCheck.length(); i++) {
			char tempVal = numberCheck.charAt(i);
			if (i > 0 && (p = o - tempVal) > -2 && p < 2 && (n = p == d ? n + 1 : 0) > limit - 3)
				return true;
			d = p;
			o = tempVal;
		}

		return false;
	}
	
	
	static public boolean isEnableAPI() {
		return false;
	}
}