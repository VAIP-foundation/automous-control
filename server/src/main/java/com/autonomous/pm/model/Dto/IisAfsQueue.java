package com.autonomous.pm.model.Dto;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.autonomous.pm.domain.structure.FlightInfo;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.VPoiGate;
import com.autonomous.pm.util.DateUtil;
import com.autonomous.pm.util.MyUtil;

public class IisAfsQueue {



	/**
	 * 
	 * @return
	 */
	public FlightInfo toFlightInfo() {
		FlightInfo fi = new FlightInfo();

		String gateNo = this.getGateNo();
		VPoiGate gate = MemDB.GATE.selectAll().stream().filter(g -> g.getGateNo().equals(gateNo)).findFirst()
				.orElse(null);
		String term = "";
    	if ( gate != null ) {
			term = gate.getTerm();
    	} else {
    		term = MyUtil.getTerminalByType(this.gettTy());
    	}

		return fi;
	}

	/**
	 * 
	 * @return
	 */
	public ResTrip toResTrip() {
		ResTrip resTrip = new ResTrip();
		resTrip.setActlDts(this.getLastDateAndTime());
		resTrip.setFlghtnum(this.getFltId());
		resTrip.setFlghtStts(this.getRem1());
		resTrip.setFlghtSttsNm(this.getRem1());
		return resTrip;
	}

	public Long getIdQueue() {
		return idQueue;
	}

	public void setIdQueue(Long idQueue) {
		this.idQueue = idQueue;
	}

	public String gettTy() {
		return tTy;
	}

	public void settTy(String tTy) {
		this.tTy = tTy;
	}

	public Byte getIsDirty() {
		return isDirty;
	}

	public void setIsDirty(Byte isDirty) {
		this.isDirty = isDirty;
	}

	public String getUnitsystemId() {
		return unitsystemId;
	}

	public void setUnitsystemId(String unitsystemId) {
		this.unitsystemId = unitsystemId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public String getTmStmp() {
		return tmStmp;
	}

	public void setTmStmp(String tmStmp) {
		this.tmStmp = tmStmp;
	}

	public String getAfsId() {
		return afsId;
	}

	public void setAfsId(String afsId) {
		this.afsId = afsId;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getFltCarr() {
		return fltCarr;
	}

	public void setFltCarr(String fltCarr) {
		this.fltCarr = fltCarr;
	}

	public String getFltNo() {
		return fltNo;
	}

	public void setFltNo(String fltNo) {
		this.fltNo = fltNo;
	}

	public String getFltSufxNo() {
		return fltSufxNo;
	}

	public void setFltSufxNo(String fltSufxNo) {
		this.fltSufxNo = fltSufxNo;
	}

	public String getFltId() {
		return fltId;
	}

	public void setFltId(String fltId) {
		this.fltId = fltId;
	}

	public String getSchdDate() {
		return schdDate;
	}

	public void setSchdDate(String schdDate) {
		this.schdDate = schdDate;
	}

	public String getSchdTime() {
		return schdTime;
	}

	public void setSchdTime(String schdTime) {
		this.schdTime = schdTime;
	}

	public String getEstmDate() {
		return estmDate;
	}

	public void setEstmDate(String estmDate) {
		this.estmDate = estmDate;
	}

	public String getEstmTime() {
		return estmTime;
	}

	public void setEstmTime(String estmTime) {
		this.estmTime = estmTime;
	}

	public String getLastKnownDate() {
		return lastKnownDate;
	}

	public void setLastKnownDate(String lastKnownDate) {
		this.lastKnownDate = lastKnownDate;
	}

	public String getLastKnownTime() {
		return lastKnownTime;
	}

	public void setLastKnownTime(String lastKnownTime) {
		this.lastKnownTime = lastKnownTime;
	}

	public String getBlockDate() {
		return blockDate;
	}

	public void setBlockDate(String blockDate) {
		this.blockDate = blockDate;
	}

	public String getBlockTime() {
		return blockTime;
	}

	public void setBlockTime(String blockTime) {
		this.blockTime = blockTime;
	}

	public String getActlDate() {
		return actlDate;
	}

	public void setActlDate(String actlDate) {
		this.actlDate = actlDate;
	}

	public String getActlTime() {
		return actlTime;
	}

	public void setActlTime(String actlTime) {
		this.actlTime = actlTime;
	}

	public String getGateNo() {
		return gateNo;
	}

	public void setGateNo(String gateNo) {
		this.gateNo = gateNo;
	}

	public String getPrevGateNo() {
		return prevGateNo;
	}

	public void setPrevGateNo(String prevGateNo) {
		this.prevGateNo = prevGateNo;
	}

	public Date getcDt() {
		return cDt;
	}

	public void setcDt(Date cDt) {
		this.cDt = cDt;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public Date getLastDateAndTime() {
		return lastDateAndTime;
	}

	public void setLastDateAndTime(Date lastDateAndTime) {
		this.lastDateAndTime = lastDateAndTime;
	}

	public String getCdsStat() {
		return cdsStat;
	}

	public void setCdsStat(String cdsStat) {
		this.cdsStat = cdsStat;
	}

	public String getCanclInd() {
		return canclInd;
	}

	public void setCanclInd(String canclInd) {
		this.canclInd = canclInd;
	}

	public String getRem1() {
		return rem1;
	}

	public void setRem1(String rem1) {
		this.rem1 = rem1;
	}

	public String getRem2() {
		return rem2;
	}

	public void setRem2(String rem2) {
		this.rem2 = rem2;
	}

	public String getRem3() {
		return rem3;
	}

	public void setRem3(String rem3) {
		this.rem3 = rem3;
	}

	public String getRem5() {
		return rem5;
	}

	public void setRem5(String rem5) {
		this.rem5 = rem5;
	}

	public String getArpt() {
		return arpt;
	}

	public void setArpt(String arpt) {
		this.arpt = arpt;
	}

	public Date getSchdDateAndTime() {
		return schdDateAndTime;
	}

	public void setSchdDateAndTime(Date schdDateAndTime) {
		this.schdDateAndTime = schdDateAndTime;
	}

	@Override
	public String toString() {
		return "IisAfsQueue [idQueue=" + idQueue + ", tTy=" + tTy + ", isDirty=" + isDirty + ", unitsystemId="
				+ unitsystemId + ", messageId=" + messageId + ", dateAndTime=" + dateAndTime + ", tmStmp=" + tmStmp
				+ ", afsId=" + afsId + ", msgType=" + msgType + ", fltCarr=" + fltCarr + ", fltNo=" + fltNo
				+ ", fltSufxNo=" + fltSufxNo + ", fltId=" + fltId + ", schdDate=" + schdDate + ", schdTime=" + schdTime
				+ ", schdDateAndTime=" + schdDateAndTime + ", estmDate=" + estmDate + ", estmTime=" + estmTime
				+ ", lastKnownDate=" + lastKnownDate + ", lastKnownTime=" + lastKnownTime + ", blockDate=" + blockDate
				+ ", blockTime=" + blockTime + ", actlDate=" + actlDate + ", actlTime=" + actlTime + ", lastDate="
				+ lastDate + ", lastTime=" + lastTime + ", lastDateAndTime=" + lastDateAndTime + ", gateNo=" + gateNo
				+ ", prevGateNo=" + prevGateNo + ", arpt=" + arpt + ", cdsStat=" + cdsStat + ", canclInd=" + canclInd
				+ ", rem1=" + rem1 + ", rem2=" + rem2 + ", rem3=" + rem3 + ", rem5=" + rem5 + ", cDt=" + cDt + "]";
	}

}