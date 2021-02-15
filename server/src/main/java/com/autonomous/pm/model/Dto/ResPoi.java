package com.autonomous.pm.model.Dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ResPoi
{
	
	private Integer idPoi;		// POI ID
	private String poiCd;	// POI 코드
	private String poiNm;	// POI 명
	private Integer poiTy;		// POI Type
	private Double lng;		// 경도
	private Double lat;		// 위도
	private Integer ord;		// 순서
	private String idGop;	// 권역
	
}