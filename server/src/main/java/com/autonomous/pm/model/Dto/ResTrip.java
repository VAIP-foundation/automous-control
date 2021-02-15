package com.autonomous.pm.model.Dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResTrip {

	private Long idV;			
	private String vrn;			
	private Date actlDts;		
	private String flghtnum;	
	private String flghtStts;	
	private String flghtSttsNm; 
	private String gateNo;		
	private Date dts;			
	private Integer tripStts;	
	@JsonIgnore
	private Integer callStts;	
	@JsonIgnore
	private Integer callRslt;	
	private String pri;			
	@JsonIgnore
	private Long callIdV;		
	@JsonIgnore
	private Integer finRslt;	
	@JsonIgnore
	private Integer rsn;
	
	private Integer repeatActiveCnt;	
	private Integer repeatReserveCnt;	
	
	private Long idCall
}