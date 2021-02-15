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
public class PmAssgn {
	public enum CallTy {
		STOP("1"),		// 1 정지
		RESUME("2"),	// 2 재운행
		VBASE("3"),		// 3 회차
		RESERVE("4");	// 4 긴급배정
		
		private final String code;
		private CallTy(final String code) {
			this.code = code;
		}
		public String toString() {
			return code.toString();
		}
	}
	public enum CallStts {
		CREATE(0),		// 0 생성
		SEND(1),		// 1 전송
		COMPLETE(2);	// 2 완료
		
		private final Integer code;
		private CallStts(final Integer code) {
			this.code = code;
		}
		public String toString() {
			return String.valueOf(code);
		}
		public Integer toInteger() {
			return code;
		}
	}
	public enum CallRslt {
		ONGOING(0),		// 0=수행중
		COMPLETE(1),	// 1=정상수행완료
		ERROR(2);		// 2=비정상완료
		
		private final Integer code;
		private CallRslt(final Integer code) {
			this.code = code;
		}
		public String toString() {
			return String.valueOf(code);
		}
		public Integer toInteger() {
			return code;
		}
	}
	
	
	public PmAssgn() {
	}
		
    private Long idCall;	// 내부적으로만 사용되며 외부에 노출하지 않는 ID,SEQ,AI
    private Long idV;		// 차량ID. CALL 이 전달된 차량 ID
    private Long idGop;		// Group Of POI  ID
    private String callTy;	// Call 종류. 1:회차
    private String callId;	// 배차ID,차량에 내리는 CALL ID값. 예:20190531T199999, YMD + 'T1' + subseq
    private String csd;		// CALL SCHEDULE Date. YYYYMMDD format
    private Integer subSeq;	// 하루단위 seq 값.
    private String pri;		// P0,P1,P2
    private Integer rpt;	// 반복횟수 (P1 인경우에만 사용)
    private Integer rptAct;	// 수행횟수
    private String flc;		// Flight Carrier (FLC). KE,OZ,...
    private String fln;		// Flight Number (FLN)
    private Long frmPoi;	// ID_POI
    private Long toPoi;		// ID_POI
    private Integer callStts;// CALL상태.0=생성,1=전송,2=완료
    private Integer callRslt;// CALL 수행 결과의 저장.0=수행중.1=정상수행완료.2=비정상완료
    private Long idTrip;	// 수행한 TRIP 결과
    private Date callDt;	// CALL을 명령/응답 한 일시
    private String callMthd;// CALL을 전송한 방법. G=GET(PULL),P=PUSH
    private Long idQueue;	// 참조할 항공스케쥴정보.AFS(Active Flight Schedule) Depature/Arrival ID.SEQ,AI
    private String cusr;	// Creation User.SYS=system,ID_USR
    private Date cDt;		// 생성일시
    private Date mDt;		// 수정일시
    
    
    public ResTrip toResTrip() {
    	Integer repeatActiveCnt = this.getRptAct() == null ? 0 : this.getRptAct();
    	ResTrip trip = new ResTrip();	// 차량ID
    	trip.setIdV(this.idV);
    	if ( this.idV!= null) {
    		trip.setVrn(MemDB.VHCL.select(idV).getVrn());	// 차량번호
    	}
    	trip.setActlDts(null);		// 출발/도착시간

    	trip.setFlghtnum(null);		// 항공편명
    	trip.setFlghtStts(null);	// 상태
    	trip.setFlghtSttsNm(null);
    	if ( this.getToPoi() != null ) {
    		trip.setGateNo(MemDB.POI.select(this.getToPoi()).getPoiNm());		// 게이트번호
    	}
    	trip.setDts(null);			// 날짜
    	trip.setTripStts(null);		// 운행상태
    	trip.setRepeatActiveCnt(repeatActiveCnt);	// 반복운행수행횟수
    	trip.setRepeatReserveCnt(this.getRpt());	// 반복운행지정횟수
    	trip.setIdCall(this.getIdCall());			// 배차ID
    	trip.setPri(this.getPri());					// 배차순위
		return trip;
    }
    
    
	public Long getIdCall() {
		return idCall;
	}
	public void setIdCall(Long idCall) {
		this.idCall = idCall;
	}
	public Long getIdV() {
		return idV;
	}
	public void setIdV(Long idV) {
		this.idV = idV;
	}
	public Long getIdGop() {
		return idGop;
	}
	public void setIdGop(Long idGop) {
		this.idGop = idGop;
	}
	public String getCallId() {
		return callId;
	}
	public void setCallId(String callId) {
		this.callId = callId;
	}
	public String getCsd() {
		return csd;
	}
	public void setCsd(String csd) {
		this.csd = csd;
	}
	public Integer getSubSeq() {
		return subSeq;
	}
	public void setSubSeq(Integer subSeq) {
		this.subSeq = subSeq;
	}
	public String getPri() {
		return pri;
	}
	public void setPri(String pri) {
		this.pri = pri;
	}
	public Integer getRpt() {
		return rpt;
	}
	public void setRpt(Integer rpt) {
		this.rpt = rpt;
	}
	public String getFlc() {
		return flc;
	}
	public void setFlc(String flc) {
		this.flc = flc;
	}
	public String getFln() {
		return fln;
	}
	public void setFln(String fln) {
		this.fln = fln;
	}
	public Long getFrmPoi() {
		return frmPoi;
	}
	public void setFrmPoi(Long frmPoi) {
		this.frmPoi = frmPoi;
	}
	public Long getToPoi() {
		return toPoi;
	}
	public void setToPoi(Long toPoi) {
		this.toPoi = toPoi;
	}
	public Integer getCallStts() {
		return callStts;
	}
	public void setCallStts(Integer callStts) {
		this.callStts = callStts;
	}
	public Integer getCallRslt() {
		return callRslt;
	}
	public void setCallRslt(Integer callRslt) {
		this.callRslt = callRslt;
	}
	public Long getIdTrip() {
		return idTrip;
	}
	public void setIdTrip(Long idTrip) {
		this.idTrip = idTrip;
	}
	public Date getCallDt() {
		return callDt;
	}
	public void setCallDt(Date callDt) {
		this.callDt = callDt;
	}
	public String getCallMthd() {
		return callMthd;
	}
	public void setCallMthd(String callMthd) {
		this.callMthd = callMthd;
	}
	public Long getIdQueue() {
		return idQueue;
	}
	public void setIdQueue(Long idQueue) {
		this.idQueue = idQueue;
	}
	public String getCusr() {
		return cusr;
	}
	public void setCusr(String cusr) {
		this.cusr = cusr;
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
	public Integer getRptAct() {
		return rptAct;
	}
	public void setRptAct(Integer rptAct) {
		this.rptAct = rptAct;
	}
	public String getCallTy() {
		return callTy;
	}
	public void setCallTy(String callTy) {
		this.callTy = callTy;
	}

}