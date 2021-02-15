package com.autonomous.pm.model.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReqRate {
//	@Size(min = 8, max = 8, message = "조회시작일자(fromDate)는 yyyymmdd 형식의 8자리입니다.")
//	@NotNull(message = "조회시작일자(fromDate)는 필수 입력 값입니다.")
	private Integer star;	//  별점

}