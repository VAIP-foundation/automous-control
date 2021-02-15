package com.autonomous.pm.model.Dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class ResAdminUsrGrp {

	private Long idUgrp; // 사용자그룹ID [ID 존재시 해당 사용자그룹에 update / 미 존재시 신규SEQ로 insert]
	private String grpNm; // 그룹이름
	private Integer athCd; // 권한코드
	
	@ApiModelProperty(dataType = "java.lang.Integer")
	private Byte dFlg; // 삭제여부 [0=정상,1=삭제]
	
	public Byte getdFlg() {
		return dFlg;
	}
	public void setdFlg(Byte dFlg) {
		this.dFlg = dFlg;
	}
	public Long getIdUgrp() {
		return idUgrp;
	}
	public String getGrpNm() {
		return grpNm;
	}
	public Integer getAthCd() {
		return athCd;
	}
	
}