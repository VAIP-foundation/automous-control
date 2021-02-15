package com.autonomous.pm.domain.structure;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FlightInfo {		// 비행편 정보
	
	private String flgtnum;		// flight number. 항공편명.
	private String term;		// terminal code. T1, T2
	private String gate;		// gate number. 게이트번호
	private Date deptm;			// departure time. 출발시각
	private Date arrtm;			// arrival time. 도착시각
	private String stts;		// status. 운항상태. ['ARR'=도착.Arrived 'CNL'=취소.Cancelled 'DEL'=지연.Delayed]
	private String ty;			// 입출국장 구분 ['ARV'=입국장 'DPT'=출국장"]
	private String aln;			// airline. 항공사
	private String destairport;	// destination airport. 목적지_공항
	
	// add 20200720. kdh.
	private Date schdeptm;		// schedule departure time. 출발 예정시각.
	private Date scharrtm;		// schedule arrival time. 도착 예정시각.
	private String gatestts;	// gate status. 게이트 상태. 출국장에서만 사용.
								// GTO - Gate Open, GTC - Gate Closed, BOR - Boarding, FIN - Final Call
	
}
