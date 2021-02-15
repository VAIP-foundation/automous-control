package com.autonomous.pm.model.Dto;

import com.autonomous.pm.domain.structure.Poi;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@ToString
public class ResAdminPoi {

	private Long idPoi;		// POI ID
	private String poiCd;	// POI 코드
	private String poiNm;	// POI 명
	private Integer poiTy;		// POI 타입	[1:Gate,2:Info,3:검역소,4:충전소]
	private Double lng;		// 경도
	private Double lat;		// 위도
	private Integer ord;		// 순서
	private String[] gateNo; // 게이트 번호
	private Long idGop;		// 권역 ID
	private String gopCd;		// 권역 CODE
	private String gopNm;		// 권역 명
	private String term;		// 터미널
	
	@ApiModelProperty(dataType = "java.lang.Integer")
	private Byte dFlg;		// 삭제여부
	
	public Poi toPoi() {
		Poi poi = new Poi();
		poi.setTerm(this.getTerm());	// T1, T2	terminal code.
		poi.setGrp(this.getGopCd());	// T1-A, T1-B, T2-C	권역CODE.(group)
		poi.setGrpnm(this.getGopNm());	// 권역 name [1터미널입국장, 2터미널출국장]
		poi.setPoicd(this.getPoiCd());	// POI Code. 모든 POI 마다 rule에 따라 할당해야 한다. Gate: {terminal} + {g} + 'G' + {FIMS gate number} Info: {terminal} + {g} + 'I' + {info number}
		poi.setPoinm(this.getPoiNm());	// Poi Name. [Gate102,검역소1,...]
		poi.setTy(this.getPoiTy());	// POI type:1=Gate,2=Info,3=검역소,4=충전소
		poi.setLat(this.getLat());	// 위도(WGS84)
		poi.setLng(this.getLng());	// 경도(WGS84)
		return poi;
	}

	public Long getIdPoi() {
		return idPoi;
	}

	public void setIdPoi(Long idPoi) {
		this.idPoi = idPoi;
	}

	public String getPoiCd() {
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

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Integer getOrd() {
		return ord;
	}

	public void setOrd(Integer ord) {
		this.ord = ord;
	}

	public String[] getGateNo() {
//		return gateNo;
		return gateNo.clone();
//		String[] ret = null;
//		if (this.gateNo != null) {
//			ret = new String[gateNo.length];
//			for (int i = 0; i < gateNo.length; i++) {
//				ret[i] = this.gateNo[i].clone();
//			}
//		}
//		return ret;

	}

	public void setGateNo(String[] gateNo) {
//		this.gateNo = gateNo;
		if ( gateNo == null) {
			this.gateNo = null;
		} else {
			this.gateNo = new String[gateNo.length];
			for (int i = 0; i < gateNo.length; ++i) {
				this.gateNo[i] = gateNo[i];
			}
		}
	}

	public Long getIdGop() {
		return idGop;
	}

	public void setIdGop(Long idGop) {
		this.idGop = idGop;
	}

	public String getGopCd() {
		return gopCd;
	}

	public void setGopCd(String gopCd) {
		this.gopCd = gopCd;
	}

	public String getGopNm() {
		return gopNm;
	}

	public void setGopNm(String gopNm) {
		this.gopNm = gopNm;
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

	
}