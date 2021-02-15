package com.autonomous.pm.model.Dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResEvent {

	private Date dts;		// 시간
	private int idV;		// 차량ID(차량코드)
	private String vrn;		// 차량번호
	private Date adtda;		// 배차시간
	private String evtCd;	// 상태코드
	private String evtNm;	// 상태명
	private String failCd;	// 고장코드
	private String failNm;	// 고장명
	
}