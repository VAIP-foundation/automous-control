package com.autonomous.pm.model;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.autonomous.pm.domain.structure.Call;
import com.autonomous.pm.domain.structure.Poi;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Dto.PmAssgn;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.service.SensorInfoServiceImpl;
import com.autonomous.pm.util.Guid;
import com.autonomous.pm.util.MyUtil;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CallPlanP0 extends PmAssgn {
	
	private Integer leftRpt;// 남은 반복횟수. pri=P1 인경우에만 사용
	private String term;	// T1, T2	terminal code.

	public CallPlanP0() {
	}
	
	public CallPlanP0(PmAssgn pa) {
		Long idV = pa.getIdV();
		this.setIdCall(pa.getIdCall());		// 내부적으로만 사용되며 외부에 노출하지 않는 ID,SEQ,AI
		this.setIdV(idV);			// 차량ID. CALL 이 전달된 차량 ID
		this.setIdGop(pa.getIdGop());		// Group Of POI  ID
		this.setCallId(pa.getCallId());		// 배차ID,차량에 내리는 CALL ID값. 예:20190531T199999, YMD + 'T1' + subseq
		this.setCsd(pa.getCsd());			// CALL SCHEDULE Date. YYYYMMDD format
		this.setSubSeq(pa.getSubSeq());		// 하루단위 seq 값.
		this.setPri(pa.getPri());			// P1
		this.setRpt(pa.getRpt());			// 반복실행횟수
		this.setRptAct(pa.getRptAct());		// 반복수행횟수
		this.setFlc(pa.getFlc());			// Flight Carrier (FLC). KE,OZ,...
		this.setFln(pa.getFln());			// Flight Number (FLN)
		this.setFrmPoi(pa.getFrmPoi());		// ID_POI
		this.setToPoi(pa.getToPoi());		// ID_POI
		this.setCallStts(pa.getCallStts());	// CALL상태.0=생성,1=전송,2=완료
		this.setCallRslt(pa.getCallRslt());	// CALL 수행 결과의 저장.0=수행중.1=정상수행완료.2=비정상완료
		this.setIdTrip(pa.getIdTrip());		// 수행한 TRIP 결과
		this.setCallDt(pa.getCallDt());		// CALL을 명령/응답 한 일시
		this.setCallMthd(pa.getCallMthd());	// CALL을 전송한 방법. G=GET(PULL),P=PUSH
		this.setIdQueue(pa.getIdQueue());	// 참조할 항공스케쥴정보.AFS(Active Flight Schedule) Depature/Arrival ID.SEQ,AI
		this.setCusr(pa.getCusr());			// Creation User.SYS=system,ID_USR
		this.setcDt(pa.getcDt());			// 생성일시
		this.setmDt(pa.getmDt());			// 수정일시
		
		if (idV != null) {
			String term = MemDB.VHCL.select(idV).getTerm();
			this.setTerm(term);
		} else if (pa.getIdGop() != null ) {
			String term = MemDB.GRP_POI.select(pa.getIdGop()).getTerm();
			this.setTerm(term);
		}
	}
	
	public Call toCall() {
		Call call = new Call();
		
		String uuid = Guid.genUUID(false);
		String sub = null;
		
		Long idV = this.getIdV();
		
		call.setMid(null);
		call.setCallid(uuid);
		call.setPrio(this.getPri());
		
		Long toIdPoi = this.getToPoi();
		Poi toPoi = MemDB.POI.select(toIdPoi).toPoi();
		
		call.setSub(sub);
		call.setTo(toPoi);
		call.setFi(null);	// 비행스케쥴정보는 P2 에만 적용가능
		
		return call;
	}

	public Integer getLeftRpt() {
		return leftRpt;
	}
	public void setLeftRpt(Integer leftRpt) {
		this.leftRpt = leftRpt;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}

	@Override
	public String toString() {
	    return "CallPlanP1 [idCall="+this.getIdCall()+", idV="+this.getIdV()+", idGop="+this.getIdGop()+"leftRpt ="+this.getLeftRpt()+", term =" + this.getTerm()
    		+", callId="+this.getCallId()+", csd="+this.getCsd()
	    	+", subSeq="+this.getSubSeq()+", pri="+this.getPri()+", rpt="+this.getRpt()+", flc="+this.getFlc()+", fln="+this.getFln()+", frmPoi="+this.getFrmPoi()
	    	+", toPoi="+this.getToPoi()+", callStts="+this.getCallStts()+", callRslt="+this.getCallRslt()+", idTrip="+this.getIdTrip()+", callDt="+this.getCallDt()
	    	+", callMthd="+this.getCallMthd()+", idQueue="+this.getIdQueue()+", cusr="+this.getCusr()+", cDt="+this.getcDt()+", mDt="+this.getmDt()+"]";
	}
	

}