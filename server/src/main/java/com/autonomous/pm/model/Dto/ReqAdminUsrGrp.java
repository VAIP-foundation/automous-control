package com.autonomous.pm.model.Dto;

import javax.validation.constraints.NotNull;

import com.autonomous.pm.model.Do.TUsrGrp;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@ToString
public class ReqAdminUsrGrp {

	private Long idUgrp; // 사용자그룹ID [ID 존재시 해당 사용자그룹에 update / 미 존재시 신규SEQ로 insert]
	
	@NotNull(message = "그룹이름(grpNm) 은 필수 입력 값입니다.")
	private String grpNm; // 그룹이름
	
	@NotNull(message = "권한코드(authCd) 은 필수 입력 값입니다.")
	private Integer athCd; // 권한코드
	
	@NotNull(message = "삭제여부(dFlg) 은 필수 입력 값입니다.")
	@ApiModelProperty(dataType = "java.lang.Integer")
	private Byte dFlg; // 삭제여부 [0=정상,1=삭제]
	
	public TUsrGrp toEntity() {
		TUsrGrp t = new TUsrGrp();
		t.setIdUgrp(this.idUgrp);
		t.setGrpNm(this.grpNm);
		t.setAthCd(this.athCd);
		t.setdFlg(this.dFlg);
		return t;
	}

	public Long getIdUgrp() {
		return idUgrp;
	}

	public void setIdUgrp(Long idUgrp) {
		this.idUgrp = idUgrp;
	}

	public String getGrpNm() {
		return grpNm;
	}

	public void setGrpNm(String grpNm) {
		this.grpNm = grpNm;
	}

	public Integer getAthCd() {
		return athCd;
	}

	public void setAthCd(Integer athCd) {
		this.athCd = athCd;
	}

	public Byte getdFlg() {
		return dFlg;
	}

	public void setdFlg(Byte dFlg) {
		this.dFlg = dFlg;
	}
}