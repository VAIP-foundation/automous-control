package com.autonomous.pm.model.Dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ReqAdminRate {
	@Size(min = 12, max = 12, message = "조회시작일자(fromDate)는 yyyymmddHHmi 형식의 12자리입니다.")
	@NotNull(message = "조회시작일자(fromDate)는 필수 입력 값입니다.")
	private String fromDate;

	@Size(min = 12, max = 12, message = "조회종료일자(toDate)는 yyyymmddHHmi 형식의 12자리입니다.")
	@NotNull(message = "조회종료일자(toDate)는 필수 입력 값입니다.")
	private String toDate;

//	@Pattern(regexp = "^(T1|T2)$",message = "T1,T2 중 하나의 값만 입력해 주세요.")
//	@NotNull(message = "터미널명(term) 은 필수 입력 값입니다.")
	private String term; // 터미널명
	
}