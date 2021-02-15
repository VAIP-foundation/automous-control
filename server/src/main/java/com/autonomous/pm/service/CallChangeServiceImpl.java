package com.autonomous.pm.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.autonomous.pm.domain.structure.Call;
import com.autonomous.pm.domain.structure.CallChange;
import com.autonomous.pm.domain.structure.FlightInfo;
import com.autonomous.pm.domain.structure.Poi;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Dto.IisAfsQueue;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.service.restful.PoiServiceImpl;
import com.autonomous.pm.util.Guid;
import com.autonomous.pm.vhcl.websocket.WebSocketPmServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CallChangeServiceImpl implements CallChangeService {

	@Autowired
	PoiServiceImpl poiService;
	@Autowired
	WebSocketPmServiceImpl wsPmService;
	
	
	public static final Logger logger = LoggerFactory.getLogger(CallChangeServiceImpl.class);
	
	
	// 가져온 데이터 중 현재 운행중인 차량이 있으면 CallChange 전송을 한다.
	@Override
	public void checkCallChange(List<IisAfsQueue> queueList) {
		queueList.stream()
		.filter(q-> "DPT".equals(q.gettTy()) )					// 출국장에서 이동중인 차량에만 적용하여야 함.
		.filter(q-> "U".equals(q.getMsgType()) )				// 변경된 정보에만 적용
		.filter(q-> StringUtils.isNotEmpty(q.getGateNo()) )		// 게이트번호가 존재해야 함.
		.filter(q-> StringUtils.isNotEmpty(q.getPrevGateNo()) )	// 이전 게이트번호가 존재해야 함.
		.forEach(q->{
			String prevGateNo = q.getPrevGateNo();
			String gateNo = q.getGateNo();
			ResAdminPoi adminPoiPrev = poiService.getPoiByGateNo(prevGateNo);	// 이전 게이트번호로 Poi 정보 추출
			ResAdminPoi adminPoiNew = poiService.getPoiByGateNo(gateNo);		// 변경 게이트번호로 Poi 정보 추출
			
			// 이전 GateNo가 시스템 관리 대상이 아닌 GateNo일 경우
			if ( adminPoiPrev == null ) {
				// Do Nothing: 해당 POI로 운행중인 차량이 없는 것으로 간주함
				
			// 변경 전 Gate의 POI와 변경 후 Gate의 POI가 같을 경우
			} else if ( adminPoiPrev == adminPoiNew ) {
				// Do Nothing: 별도의 알림 없음
				
			// IIS의 이전 GateNo가 시스템 관리대상이고, 목적지가 변경되었을 경우
			} else {
				
				Poi poiPrev = adminPoiPrev.toPoi();
				Long poiIdPrev = adminPoiPrev.getIdPoi();
				
				// 현재 이동중인 차량의 목적지가 이전게이트번호와 같은지 확인
				MemDB.VHCL.selectAll().stream()
					.filter(v->v.getDestPoi() != null)	// 운행중인 차량만 대상
					.forEach(v->{
						log.info("poiIdPrev:{}, q.getFltId:{}", poiIdPrev, q.getFltId());
						log.info("VRN:{}, DestPoi:{}, DestFlightnum:{}", v.getVrn(), v.getDestPoi(), v.getDestFlightnum());
						
						// 차량의 목적지와 항공편이 같으면 해당 차량에 P0 CALL 전송!!
						if ( v.getDestPoi() == poiIdPrev && q.getFltId().equals(v.getDestFlightnum())  ) { 
							CallChange callChange = new CallChange();
							
							// 변경된 GateNo가 서비스영역 밖인 경우. 혹은 서비스영역 안이지만 권역이 다른 경우 -> info로 보냄
							if ( adminPoiNew == null || (adminPoiNew != null && !adminPoiNew.getTerm().equals(v.getTerm()) ) ) {
								
								// 기본 Poi로 갈지 묻는 CallChange생성
								ResAdminPoi poiNew = MemDB.POI.selectAll().stream()
									.filter(p->p.getTerm().equals(v.getTerm()))
									.filter(p->p.getPoiTy() == 2)	// 2:Info
									.findFirst()
									.orElse(null);
								
								if ( poiNew != null ) {
									// 항공편 생성
									FlightInfo fi = q.toFlightInfo();
									callChange = createCallChange(poiPrev, poiNew.toPoi(), fi);
								} else {
									log.error("This Terminal has not Info-Poi. term={}", v.getTerm());
								}
								
							// 변경된 GateNo가 서비스영역 안이고 권역도 같을 경우
							} else {
								Poi poiNew = adminPoiNew.toPoi();
								// 변경된 Poi로 갈지 묻는 CallCahnge 생성 
								if ( poiNew != null ) {
									// 항공편 생성
									FlightInfo fi = q.toFlightInfo();
									callChange = createCallChange(poiPrev, poiNew, fi);
								} else {
									log.error("This Terminal has not Info-Poi. term={}", v.getTerm());
								}
								
							}
							
							// CallChange 전송
							wsPmService.callChange(v.getIdV(), callChange);
							log.info(
								"CallChange vrn={}, prev->{}/{}, next->{}/{}, flightInfo->{}"
								, v.getVrn()
								, poiPrev.getPoicd(), poiPrev.getPoinm()
								, callChange.getNewdst().getPoicd(), callChange.getNewdst().getPoinm()
								, callChange.getFi()
							);
						}
					});
					
				
			}
			
		});
	}
	
	/**
	 * @param poiPrev	
	 * @param poiNew	
	 * @param fi		
	 * @return
	 */
	private CallChange createCallChange(Poi poiPrev, Poi poiNew, FlightInfo fi) {
		CallChange cc = new CallChange();
		
		Call call = new Call();
		call.setPrio("p0");	
		call.setMid(null);
		call.setSub(null);
		call.setFi(null);
		call.setCallid(null);
		
		String termPrev = poiPrev.getTerm();
		String termNew = poiNew.getTerm();
		if ( !termPrev.equals(termNew) ) {	
			ResAdminPoi poiInfo = MemDB.POI.selectAll().stream()
				.filter(p->termPrev.equals(p.getTerm()))
				.filter(p->p.getPoiTy()==2)	
				.findFirst().orElse(null);
			call.setTo(poiInfo.toPoi());
			
			poiNew.setTerm(null);
			poiNew.setGrp(null);
			poiNew.setGrpnm(null);
			poiNew.setLat(null);
			poiNew.setLng(null);
			
		} else {	
			call.setTo(poiNew);
		}
		
		cc.setMid(Guid.genUUID());
		if ( fi != null ) {
			if( "".equals(fi.getStts()) ) {
				fi.setStts(null);
			}
		}
		cc.setFi(fi);
		cc.setCall(call);
		
		cc.setNewdst(poiNew);
		
		return cc;
	}

}
