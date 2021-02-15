package com.autonomous.pm.domain.structure.push;

import org.springframework.beans.BeanUtils;

import com.autonomous.pm.domain.structure.DrivingInfo;
import com.autonomous.pm.domain.structure.Event;
import com.autonomous.pm.domain.structure.mem.EventMem;
import com.autonomous.pm.memcache.MemDB;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DrivingInfoPush extends DrivingInfo {	// UI PUSH를 위한 구조체. 일부 항목을 추가하였다.
	public enum EventCode {
		SUCCESS("1000")				// 1000 정상
//		CRASH("1101"),					// 1101 충돌
//		TROUBLE("1201"),				// 1201 고장보고
//		CHARGE_START("1202"),			// 1202 충전 시작
//		CHARGE_FIN("1203"),				// 1203 충전 완료
//		STOP_START_USER("1301"),		// 1301 수동정지(by승객).
//		STOP_FIN_USER("1302"),			// 1302 수동정지-재기동(by승객)
//		CHNAGE_GATE_DEP_ORIGIN("1303"),	// 1303 출국장 게이트변경: 기존 목적지 선택
//		CHNAGE_GATE_DEP_OTHER("1304"),	// 1304 출국장 게이트변경: 변경 목적지 선택
//		CHNAGE_GATE_DEP_CANCEL("1305")	// 1305 출국장 게이트변경: CALL 취소, 하차 선택
		;
		
		private final String code;
		private EventCode(final String code) {
			this.code = code;
		}
		public String toString() {
			return code.toString();
		}
	}
	
	Long idV;	// 차량ID
	String vrn;	// 차량번호
	String evtcd;	// 이벤트코드
	
	public DrivingInfoPush() {
	}
	
	public DrivingInfoPush(Long idV, String vrn, DrivingInfo drivingInfo) {
		BeanUtils.copyProperties(drivingInfo, this);
		
		this.setIdV(idV);
		this.setVrn(vrn);
		
	}
	
	/**
	 * @param defaultSpeed
	 */
	public void calcEvtCd(Float defaultSpeed) {
		if ( this.getSpeed() >= defaultSpeed ) {
			this.setEvtcd(EventCode.SUCCESS.toString());
			EventMem memEvt = MemDB.EVT.select(idV);
			if (memEvt != null) {
				Event evt = memEvt.getData();
				if ( evt != null ) {
	            	evt.setEvtcd(EventCode.SUCCESS.toString());
	            	evt.setFailcd(null);
	            	memEvt.setData(evt);
	            	MemDB.EVT.insertSafety(idV, memEvt);	// 마지막 event 값 갱신.
				}
			}
			
		} else {
			EventMem memEvt = MemDB.EVT.select(idV);
			if (memEvt != null) {
				Event evt = memEvt.getData();
				if ( evt != null ) {
	            	this.setEvtcd(evt.getEvtcd());
				}
			}
		}
	}
	
	/**
	 */
	public void calcEvtCd() {
		EventMem memEvt = MemDB.EVT.select(idV);
		if (memEvt != null) {
			Event evt = memEvt.getData();
			if ( evt != null ) {
            	this.setEvtcd(evt.getEvtcd());// 현재 이벤트코드추가
            	
            	if ( Event.EventCode.UNREACHABLE.toString().equals(evt.getEvtcd()) ) {
            		this.setStts(20);
            	}
			}
		}
	}
	
}