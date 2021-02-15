package com.autonomous.pm.model.Dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResCmdHst {

	private Long idV;		// 차량ID
	
	private String dts;			// 차량번호
	
	private String cmd;		// 출발/도착시간
								// Actual Date and Time of Departure(Arrival)
								// 실제 출발(도착) 시간
								// terminal에 따라 의미가 다름
								// T1: 입국장 도착시간
								// T2: 출국장 출발시간

	private String mthd;	// 항공편명
								// Flight Identifier (FLT, fltId). 운항편명
	
	private String rqMsg;	// 상태
								// 운항 상태 (Flight Remark, rem1)
	private String sendStr; // ARR - Arrived(도착)
								// CNL - Cancelled(취소)
								// DEL - Delayed(지연)
								// DIV - Diversion(회항)
								// LND - Landed(착륙)
								// DEP - Departured(출발)
	
	
	private String mId;		// 게이트번호
								// Gate Departure(Arrival) number
	
	private String ackStr;			// 날짜
								// 운행시간(TRIP출발보고시간)
	
	private Date cDt;	// 생성일시

	private Date mDt;	// 수정일시
}