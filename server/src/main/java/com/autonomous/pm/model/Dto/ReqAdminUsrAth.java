package com.autonomous.pm.model.Dto;

import javax.validation.constraints.NotNull;

import com.autonomous.pm.model.Do.TUsrAth;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@ToString
public class ReqAdminUsrAth {

//	@NotNull(message = "권한코드(authCd) 은 필수 입력 값입니다.")
	private Integer athCd; // 권한코드
	
	private Integer athLvl;	// 권한레벨
	
	private String athNm;	// 권한명
	private String dscrpt;	// 권한에 대한 설명
	
	private Byte dFlg; // 삭제여부 [0=정상,1=삭제]
	
	public TUsrAth toEntity() {
		TUsrAth t = new TUsrAth();
		t.setAthCd(this.athCd);
		t.setAthLvl(this.athLvl);
		t.setAthNm(this.athNm);
		t.setDscrpt(this.dscrpt);
		t.setdFlg(this.dFlg);
		return t;
	}

	public Integer getAthCd() {
		return athCd;
	}

	public void setAthCd(Integer athCd) {
		this.athCd = athCd;
	}

	public Integer getAthLvl() {
		return athLvl;
	}

	public void setAthLvl(Integer athLvl) {
		this.athLvl = athLvl;
	}

	public String getAthNm() {
		return athNm;
	}

	public void setAthNm(String athNm) {
		this.athNm = athNm;
	}

	public String getDscrpt() {
		return dscrpt;
	}

	public void setDscrpt(String dscrpt) {
		this.dscrpt = dscrpt;
	}

	public Byte getdFlg() {
		return dFlg;
	}

	public void setdFlg(Byte dFlg) {
		this.dFlg = dFlg;
	}

	
}