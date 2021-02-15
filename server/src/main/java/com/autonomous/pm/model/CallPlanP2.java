package com.autonomous.pm.model;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autonomous.pm.domain.structure.Call;
import com.autonomous.pm.domain.structure.FlightInfo;
import com.autonomous.pm.domain.structure.Gps;
import com.autonomous.pm.domain.structure.Poi;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.VPoiGate;
import com.autonomous.pm.model.Dto.IisAfsQueue;
import com.autonomous.pm.model.Dto.PmAssgn;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.service.restful.PoiServiceImpl;
import com.autonomous.pm.util.Guid;
import com.autonomous.pm.util.MyUtil;

import lombok.Builder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Slf4j
@Component
public class CallPlanP2 extends IisAfsQueue {
	
	
	@Autowired
	PoiServiceImpl poiService;
	
	private String pri;		
	private String term;	
	private Long frmPoi;	
	private Long toPoi;		

	public CallPlanP2() {
	}
	
	/**
	 * @param queue
	 */
	@Builder
	public CallPlanP2(IisAfsQueue queue) {
//		poiService = new PoiServiceImpl();
		this.poiService = new PoiServiceImpl();
		
		this.setPri("p2");	
		
		ResAdminPoi toPoi = poiService.getPoiByGateNo(queue.getGateNo());	
		if ( toPoi != null ) {
			this.setToPoi(toPoi.getIdPoi());
			String term = MemDB.POI.select(toPoi.getIdPoi()).getTerm();
			this.setTerm(term);	
		}
		
	    this.setIdQueue(queue.getIdQueue());
	    this.settTy(queue.gettTy());
	    this.setIsDirty(queue.getIsDirty());
	    this.setUnitsystemId(queue.getUnitsystemId());
	    this.setMessageId(queue.getMessageId());
	    this.setDateAndTime(queue.getDateAndTime());
	    this.setTmStmp(queue.getTmStmp());
	    this.setAfsId(queue.getAfsId());
	    this.setMsgType(queue.getMsgType());
	    this.setFltCarr(queue.getFltCarr());
	    this.setFltNo(queue.getFltNo());
	    this.setFltSufxNo(queue.getFltSufxNo());
	    this.setFltId(queue.getFltId());
	    this.setSchdDate(queue.getSchdDate());
	    this.setSchdTime(queue.getSchdTime());
	    this.setEstmDate(queue.getEstmDate());
	    this.setEstmTime(queue.getEstmTime());
	    this.setLastKnownDate(queue.getLastKnownDate());
	    this.setLastKnownTime(queue.getLastKnownTime());
	    this.setBlockDate(queue.getBlockDate());
	    this.setBlockTime(queue.getBlockTime());
	    this.setActlDate(queue.getActlDate());
	    this.setActlTime(queue.getActlTime());
	    this.setGateNo(queue.getGateNo());
	    this.setPrevGateNo(queue.getPrevGateNo());
	    this.setLastDate(queue.getLastDate());
	    this.setLastTime(queue.getLastTime());
	    this.setLastDateAndTime(queue.getLastDateAndTime());
	    
	    this.setArpt(queue.getArpt());
	    this.setCdsStat(queue.getCdsStat());
	    this.setCanclInd(queue.getCanclInd());
	    this.setRem1(queue.getRem1());
	    this.setRem2(queue.getRem2());
	    this.setRem3(queue.getRem3());
	    this.setRem5(queue.getRem5());
	    
	    this.setcDt(queue.getcDt());
	}
	
	/**
	 * @param idV
	 * @return
	 */
	public Call toCall() {
		Call call = new Call();
		
		String uuid = Guid.genUUID(false);
		String sub = null;
		Poi toPoi = new Poi();
		
		FlightInfo fi = this.getFlightInfo();
		
		call.setMid(null);
		call.setCallid(uuid);
		call.setPrio(this.getPri());
		
		Long toIdPoi = this.getToPoi();
		if ( toIdPoi != null ) {
			toPoi = MemDB.POI.select(toIdPoi).toPoi();
		}
		
		call.setSub(sub);
		call.setTo(toPoi);
		call.setFi(fi);
		
		return call;
	}
	
	/**
	 * @return
	 */
	private FlightInfo getFlightInfo() {
		FlightInfo fi = new FlightInfo();
    	
		String gateNo = this.getGateNo();
    	VPoiGate gate = MemDB.GATE.selectAll().stream().filter(g->g.getGateNo().equals(gateNo)).findFirst().orElse(null);
    	String term = "";
    	if ( gate != null ) {
    		term = gate.getTerm();
    	} else {
    		term = MyUtil.getTerminalByType(this.gettTy());
    		if ( term == null ) {
    			term = this.getTerm();
    		}
    	}
		
		fi.setFlgtnum(this.getFltId());	
		fi.setTerm(term);				
		fi.setGate(this.getGateNo());	
		if (MyUtil.isT1(term)) {
			fi.setScharrtm(this.getSchdDateAndTime());
			fi.setArrtm(this.getLastDateAndTime());	
		} else if (MyUtil.isT2(term)) {
			fi.setSchdeptm(this.getSchdDateAndTime());	
			fi.setGatestts(this.getRem2());				
			fi.setDeptm(this.getLastDateAndTime());		
			fi.setDestairport(this.getArpt());			
			fi.setArrtm(this.getLastDateAndTime());	
		}
		fi.setStts(this.getRem1());		
		fi.setTy(this.gettTy());		
		fi.setAln(this.getFltCarr());	
	    return fi;
	}
	
	
	public String getPri() {
		return pri;
	}
	public void setPri(String pri) {
		this.pri = pri;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
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
	

	public void setFrmPoi(Long lng, Long lat) {
		ResAdminPoi poi = poiService.getPoiByClosestGPS(lng, lat);
		this.frmPoi = poi != null ? poi.getIdPoi() : null;
	}
	
	
	@Override
	public String toString() {
		return "CallPlanP2 [pri="+this.getPri()+", term="+this.getTerm()+", frmPoi="+this.getFrmPoi()+", toPoi="+this.getToPoi()
			+", idQueue="+this.getIdQueue()+", tTy="+this.gettTy()+", isDirty="+this.getIsDirty()+", unitsystemId="+this.getUnitsystemId()
			+", messageId="+this.getMessageId()+", dateAndTime="+this.getDateAndTime()+", tmStmp="+this.getTmStmp()+", afsId="+this.getAfsId()
			+", msgType="+this.getMsgType()+", fltCarr="+this.getFltCarr()+", fltNo="+this.getFltNo()+", fltSufxNo="+this.getFltSufxNo()
			+", fltId="+this.getFltId()+", schdDate="+this.getSchdDate()+", schdTime="+this.getSchdTime()+", estmDate="+this.getEstmDate()
			+", estmTime="+this.getEstmTime()+", lastKnownDate="+this.getLastKnownDate()+", lastKnownTime="+this.getLastKnownTime()
			+", blockDate="+this.getBlockDate()+", blockTime="+this.getBlockTime()+", actlDate="+this.getActlDate()+", actlTime="+this.getActlTime()
			+", lastDate="+this.getLastDate()+", lastTime="+this.getLastTime()+", lastDateAndTime="+this.getLastDateAndTime()
			+", gateNo="+this.getGateNo()+", prevGateNo="+this.getPrevGateNo()+", arpt="+this.getArpt()+", cdsStat="+this.getCdsStat()
			+", canclInd="+this.getCanclInd()+", rem1="+this.getRem1()+", rem2="+this.getRem2()+", rem3="+this.getRem3()+", rem5="+this.getRem5()
			+", cDt="+this.getcDt()+"]";
		
	}

}