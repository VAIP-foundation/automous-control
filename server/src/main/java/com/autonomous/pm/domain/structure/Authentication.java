package com.autonomous.pm.domain.structure;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Authentication {	// 로그인 인증 응답 정보
	
	
	private String token;		// /v1/authen/{vrn} 의 응답 메시지.
								// JWT에 의해 암호화 된 key. 단말은 acckey 를 매 HTTP attribute 필드(x-akey:)에 추가하여 전송하고
								// 서버는 매번 이를 복호화 하여 접속자의 ID를 식별한다.
	
	// private int valid_time;		// String 형태의 종료일자로 변경. 2003256. kdh.
//	@JsonProperty("expire-time")
	private String exprtm;		// pm-as-key key 값의 유효시간. 분 단위.
	private String term;		// 터미널정보 [T1, T2]
	private String hall;		// 입출국장정보 [ARV, DPT]
	
}
