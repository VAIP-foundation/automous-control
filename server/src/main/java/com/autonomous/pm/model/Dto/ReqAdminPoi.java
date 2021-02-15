package com.autonomous.pm.model.Dto;

import java.util.Arrays;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import com.autonomous.pm.domain.structure.Gps;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.TGrpPoi;
import com.autonomous.pm.model.Do.TPoi;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class ReqAdminPoi {

	private Long idPoi; // 사용자ID [ID 존재시 해당 Poi에 update / 미 존재시 신규SEQ로 insert]
	
//	@NotNull(message = "POI 코드(poiCd) 는 필수 입력 값입니다.")
	@JsonIgnore
	private String poiCd; // POI 타입

	@NotNull(message = "POI 명(poiNm) 는 필수 입력 값입니다.")
	private String poiNm; // POI 명

	@NotNull(message = "POI 타입(poiTy) 는 필수 입력 값입니다.")
	private Integer poiTy; // POI 타입
	
	@NotNull(message = "위도(lat) 는 필수 입력 값입니다.")
	private Double lat; // 위도

	@NotNull(message = "경도(lng) 은 필수 입력 값입니다.")
	private Double lng; // 경도
	
	@NotNull(message = "순서(ord) 은 필수 입력 값입니다.")
	private Integer ord; // 순서

//	@NotNull(message = "권역 ID(idGop) 은 필수 입력 값입니다.")
	@JsonIgnore
	private Long idGop; // 권역 ID
	
	@NotNull(message = "터미널명(term) 은 필수 입력 값입니다.")
	private String term; // 터미널명

	@NotNull(message = "삭제여부(dFlg) 는 필수 입력 값입니다.")
	@ApiModelProperty(dataType = "java.lang.Integer")
	private Byte dFlg; // 삭제여부
	
	private String[] gateNo; // 게이트번호 배열
	
	private String gate;

	public TPoi toEntity() {
		TPoi t = new TPoi();
		t.setIdPoi(this.idPoi);
		t.setPoiCd(getPoiCd());	// ex: T14001
		t.setPoiNm(this.poiNm);
		t.setPoiTy(this.poiTy);
		t.setLngLat(new byte[] {
			Byte.valueOf(this.lng.toString()),
			Byte.valueOf(this.lat.toString())
		});
		t.setOrd(this.ord);
		t.setdFlg(this.dFlg);
		
		// 터미널을 기준으로 GRP_POI의 idGop를 찾아온다.
		TGrpPoi gpoi = MemDB.GRP_POI.selectAll().stream().filter(g->g.getTerm().equals(getTerm())).findFirst().orElse(null);
		if ( gpoi != null ) {
			t.setIdGop(gpoi.getIdGop());
			
		}
		
		return t;
	}

	public Long getIdPoi() {
		return idPoi;
	}

	public void setIdPoi(Long idPoi) {
		this.idPoi = idPoi;
	}

	public String getPoiCd() {
		if ( StringUtils.isEmpty(poiCd) ) {
			// poiCd = {terminal}{poiTy}{ord:3}
			poiCd = "";
			poiCd += this.term;	// T1, T2
			poiCd += this.poiTy.toString();	// 1, 2, 3. ..
			poiCd += String.format("%3s", this.ord).replace(' ', '0');	// 001, 002, ..
		}
		return poiCd;
	}

	public void setPoiCd(String poiCd) {
		this.poiCd = poiCd;
	}

	public String getPoiNm() {
		return poiNm;
	}

	public void setPoiNm(String poiNm) {
		this.poiNm = poiNm;
	}

	public Integer getPoiTy() {
		return poiTy;
	}

	public void setPoiTy(Integer poiTy) {
		this.poiTy = poiTy;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Integer getOrd() {
		return ord;
	}

	public void setOrd(Integer ord) {
		this.ord = ord;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Byte getdFlg() {
		return dFlg;
	}

	public void setdFlg(Byte dFlg) {
		this.dFlg = dFlg;
	}

	public String[] getGateNo() {
		return gateNo;
	}

	public void setGateNo(String[] gateNo) {
		this.gateNo = gateNo;
	}

	public Long getIdGop() {
		if ( this.idGop == null ) {
			TGrpPoi gpoi = MemDB.GRP_POI.selectAll().stream().filter(g->g.getTerm().equals(getTerm())).findFirst().orElse(null);
			if ( gpoi != null ) {
				setIdGop(gpoi.getIdGop());
			}
		}
		return this.idGop;
	}

	public void setIdGop(Long idGop) {
		this.idGop = idGop;
	}
	
	public void setGate(String gate) {
		this.gate = gate;
	}
	
	@Override
	public String toString() {
		return "ReqAdminPoi [" + (idPoi != null ? "idPoi=" + idPoi + ", " : "") + (poiCd != null ? "poiCd=" + poiCd + ", " : "")
				+ (poiNm != null ? "poiNm=" + poiNm + ", " : "") + (poiTy != null ? "poiTy=" + poiTy + ", " : "")
				+ (lat != null ? "lat=" + lat + ", " : "") + (lng != null ? "lng=" + lng + ", " : "") + (ord != null ? "ord=" + ord + ", " : "")
				+ (idGop != null ? "idGop=" + idGop + ", " : "") + (term != null ? "term=" + term + ", " : "") + "dFlg=" + dFlg + ", "
				+ (gateNo != null ? "gateNo=" + Arrays.toString(gateNo) + ", " : "")
				+ (gate != null ? "gate=" + gate + ", " : "") + "]";
	}
	
}