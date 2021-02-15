package com.autonomous.pm.model.Dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.autonomous.pm.model.Do.TVhcl;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.ToString;

@ToString
@ApiModel
public class ReqAdminVhcl {

	private Long idV;		// 차량ID [존재시 해당 차량ID에 update, 미 존재시 신규SEQ로 insert]
	
    @NotNull(message = "차량번호(vrn) 은 필수 입력 값입니다.")
	private String vrn;		// 차량번호
    
    @NotNull(message = "회차장소(vBase) 은 필수 입력 값입니다.")
	private Long vBase;	// 회차장소
 
//    @Size(min=6, max=6, message ="차량색상(vclr) 의 Length는 6입니다." )
//    @Pattern(regexp = "^#[A-Fa-f0-9+]{6}$",message = "차량색상(vclr) 은 6자리 RGB규격으로 입력해 주세요. [ex:#ff00ee]")
    @NotNull(message = "차량색상(vclr) 은 필수 입력 값입니다.")
	private String vclr;	// 차량색상 [Vehicle Color, 차량색상 RGB값]
    
    @Pattern(regexp = "^(T1|T2)$",message = "T1,T2 중 하나의 값만 입력해 주세요.")
    @NotNull(message = "터미널(term) 은 필수 입력 값입니다.")
	private String term;	// 터미널 [T1, T2]
    
    @ApiModelProperty(dataType = "java.lang.Integer")
    private Byte dFlg;		// 삭제여부 [0=정상,1=삭제]
    
    private String pwd; // 로그인암호
	
	public TVhcl toEntity() {
		TVhcl t = new TVhcl();
		t.setIdV(this.idV);
		t.setVrn(this.vrn);
		t.setvBase(this.vBase);
		t.setVclr(this.vclr);
		t.setdFlg(this.dFlg);
		t.setPwd(this.pwd);
		return t;
	}

	public Long getIdV() {
		return idV;
	}

	public void setIdV(Long idV) {
		this.idV = idV;
	}

	public String getVrn() {
		return vrn;
	}

	public void setVrn(String vrn) {
		this.vrn = vrn.trim();
//		this.vrn = vrn;
	}

	public Long getvBase() {
		return vBase;
	}

	public void setvBase(Long vBase) {
		this.vBase = vBase;
	}

	public String getVclr() {
		return vclr;
	}

	public void setVclr(String vclr) {
		this.vclr = vclr;
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

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}