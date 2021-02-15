package com.autonomous.pm.model.Dto;

import java.util.Date;

import com.autonomous.pm.domain.structure.Call;
import com.autonomous.pm.domain.structure.FlightInfo;
import com.autonomous.pm.memcache.MemDB;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@AllArgsConstructor
@Builder
@ToString
public class CmdHst {
	
	public CmdHst() {
	}
		
    private Long idV;		// 차량ID. CALL 이 전달된 차량 ID
    private String dts;		// Group Of POI  ID
    private String cmd;	// 배차ID,차량에 내리는 CALL ID값. 예:20190531T199999, YMD + 'T1' + subseq
    private String mthd;	// 배차ID,차량에 내리는 CALL ID값. 예:20190531T199999, YMD + 'T1' + subseq
    private String rqMsg;	// 배차ID,차량에 내리는 CALL ID값. 예:20190531T199999, YMD + 'T1' + subseq
    private String sendStr;	// 배차ID,차량에 내리는 CALL ID값. 예:20190531T199999, YMD + 'T1' + subseq
    private String mId;	// 배차ID,차량에 내리는 CALL ID값. 예:20190531T199999, YMD + 'T1' + subseq
    private String ackStr;	// 배차ID,차량에 내리는 CALL ID값. 예:20190531T199999, YMD + 'T1' + subseq
    private Date cDt;		// 생성일시
    private Date mDt;		// 수정일시
    
    
    public ResCmdHst toResCmdHst() {
    	ResCmdHst cmdHst = new ResCmdHst();	// 차량ID
    	cmdHst.setIdV(this.idV);
//    	if ( this.idV!= null) {
//    		trip.setVrn(MemDB.VHCL.select(idV).getVrn());	// 차량번호
//    	}
//    	trip.setActlDts(null);		// 출발/도착시간
//
//    	trip.setFlghtnum(null);		// 항공편명
//    	trip.setFlghtStts(null);	// 상태
//    	trip.setFlghtSttsNm(null);
//    	if ( this.getToPoi() != null ) {
//    		trip.setGateNo(MemDB.POI.select(this.getToPoi()).getPoiNm());		// 게이트번호
//    	}
//    	trip.setDts(null);			// 날짜
//    	trip.setTripStts(null);		// 운행상태
//    	trip.setRepeatActiveCnt(repeatActiveCnt);	// 반복운행수행횟수
//    	trip.setRepeatReserveCnt(this.getRpt());	// 반복운행지정횟수
		return cmdHst;
    }
    
	public Long getIdV() {
		return idV;
	}
	public void setIdV(Long idV) {
		this.idV = idV;
	}
	public String getDts() {
		return dts;
	}
	public void setDts(String dts) {
		this.dts = dts;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getMthd() {
		return mthd;
	}
	public void setMthd(String mthd) {
		this.mthd = mthd;
	}
	public String getRqMsg() {
		return rqMsg;
	}
	public void setRqMsg(String rqMsg) {
		this.rqMsg = rqMsg;
	}
	public String getSendStr() {
		return sendStr;
	}
	public void setSendStr(String sendStr) {
		this.sendStr = sendStr;
	}
	public String getMid() {
		return mId;
	}
	public void setMid(String mId) {
		this.mId = mId;
	}
	public String getAckStr() {
		return ackStr;
	}
	public void setAckStr(String ackStr) {
		this.ackStr = ackStr;
	}
	public Date getcDt() {
		return cDt;
	}
	public void setcDt(Date cDt) {
		this.cDt = cDt;
	}
	public Date getmDt() {
		return mDt;
	}
	public void setmDt(Date mDt) {
		this.mDt = mDt;
	}
}