package com.autonomous.pm.domain.common;

public enum ResultCode {
	SUCCESS("000") // 성공
	, FAILED("100") // 알수 없는 실패
	, NO_DATA("200") //데이터 가 없습니다. 
	, INVALID_INPUT_FIELD("300") // 입력 필드 오류.	
	, NOT_NULL_FIELD("301") // 값이 비어 있음.	
	, EXCEED_RANGE("302") // 범위 초과.
	, DUPLICATED_VALUE("303") // 중복된 값 존재
	, NOT_FOUND_RESOURCE("303") //리소스를 찾을 수 없음.
	, EXCEED_MAX_COUNT("400") // 최대 전송 갯수 .	초과
	, INVALID_TOKEN("500")    // 인증실패.		
	, NOT_MATCH_PWD("501")	// 인증실패.
	, BLOCKED_LOGIN("502")	// 로그인 금지.
	, USED_ELSEWHERE("600"); // 다른 곳에서 사용중입니다.

	private final String code;

	private ResultCode(final String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return code.toString();
	}
}