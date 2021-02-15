package com.autonomous.pm.domain.common;

public enum ReasonCode {
//	SUCCESS("000") // 성공
//	, FAILED("100") // 알수 없는 실패
//	, NO_DATA("200") //데이터 가 없습니다. 
//	, INVALID_INPUT_FIELD("300") // 입력 필드 오류.	
//	, NOT_NULL_FIELD("301") // 값이 비어 있음.	
//	, EXCEED_RANGE("302") // 범위 초과.
//	, ALREADY_MONTHEND_CLOSING("303") // 이미 월마감 되었음.
//	, NOT_FOUND_RESOURCE("303") //리소스를 찾을 수 없음.
//	, EXCEED_MAX_COUNT("400") // 최대 전송 갯수 .	초과
//	, INVALID_TOKEN("500")    //인증실패.		
//	, NOT_MATCH_PWD("501"); //인증실패.
	SUCCESS("200")	// 정상
	, UNKNOWN_MESSAGE("400")	// 알 수 없는 메시지
	, INTERNAL_SERVER_ERROR("500")	// Server 내부 오류
	, NOT_MATCH_PWD("1100")	// 인증실패(vrn,password 오류 포함)
	, INVALID_TOKEN("1101");	// pm-as-key 유효하지 않은 토큰
	
	private final String code;

	private ReasonCode(final String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return code.toString();
	}
}