package com.autonomous.pm.model.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResAdminGop {

	private int idGop;		// 권역ID [Group Of POI  ID,SEQ]
	private String gopCd;	// 권역CODE [권역CODE.PM차량과 공유한다.ex: T1WI,T2EO]
	private String gopNm;	// 권역명 [권역명.PM차량과 공유한다.ex:T1 West 입국장]
	private String term;	// 터미널 [T1=터미널1,T2=터미널2]
	private int gty;		// 입출국구분 [Group Type. 입출국구분: 1030001=출발,1030002=도착]
	
}