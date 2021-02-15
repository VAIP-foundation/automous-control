package com.autonomous.pm.model.Dto;

import javax.validation.constraints.NotNull;
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
public class ReqFromToDay {
	@Size(min = 8, max = 8, message = "조회시작일자(fromDate)는 yyyymmdd 형식의 8자리입니다.")
	@NotNull(message = "조회시작일자(fromDate)는 필수 입력 값입니다.")
	private String fromDate;

	@Size(min = 8, max = 8, message = "조회종료일자(toDate)는 yyyymmdd 형식의 8자리입니다.")
	@NotNull(message = "조회종료일자(toDate)는 필수 입력 값입니다.")
	private String toDate;

}