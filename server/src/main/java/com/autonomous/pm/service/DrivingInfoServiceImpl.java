package com.autonomous.pm.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonomous.pm.dao.DrvRawYymmMapper;
import com.autonomous.pm.dao.VhclSttsMapper;
import com.autonomous.pm.domain.structure.DrivingInfo;
import com.autonomous.pm.domain.structure.Event;
import com.autonomous.pm.domain.structure.mem.DrivingInfoMem;
import com.autonomous.pm.domain.structure.mem.EventMem;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.model.Dto.DrvRawYymm;
import com.autonomous.pm.model.Dto.VhclStts;

@Service
public class DrivingInfoServiceImpl implements DrivingInfoService {

	public static final Logger logger = LoggerFactory.getLogger(DrivingInfoServiceImpl.class);

	@Autowired
	DrvRawYymmMapper drvRawYymmMapper;
	@Autowired
	VhclSttsMapper vhclSttsMapper;
	
	@Override
	public void insertInfo(DrivingInfoMem info) {
		// DB 저장
		Long idV = info.getIdV();
		MemDB.DRV.insertSafety(idV, info);
	}

	@Override
	public void insertInfo(List<DrivingInfoMem> infos) {
		ArrayList<Long> idVs = new ArrayList<Long>();
		
		List<DrvRawYymm> entityList = new ArrayList<DrvRawYymm>();
		for ( DrivingInfoMem wdi : infos ) {
			entityList.add(wdi.toEntity());
			// 데이터 MemDB 저장
			Long idV = wdi.getIdV();
			MemDB.DRV.insertSafety(idV, wdi);
			if ( !idVs.contains(idV) ) {
				idVs.add(idV);
			}
		}
		// DB저장(multi-insert)
		drvRawYymmMapper.insertList(entityList);
		
		// 마지막 데이터 T_VHCL_STTS 에 업데이트
		for( Long idV : idVs ) {
			DrivingInfo drivingInfo = MemDB.DRV.select(idV).getData();
			DrvRawYymm lastDrvRawYymm = drvRawYymmMapper.selectLastByIdV(idV);
			VhclStts vhclStts = new VhclStts();
			vhclStts.setIdV(idV);
			vhclStts.setIdDr(lastDrvRawYymm.getIdDr());
			vhclStts.setSpd(drivingInfo.getSpeed());
			vhclStts.setDrvStts(drivingInfo.getStts());
			if ( drivingInfo.getGps() != null ) {
				vhclStts.setLat(drivingInfo.getGps().getLat());
				vhclStts.setLng(drivingInfo.getGps().getLng());
			}
			
			// 네트워크 연결 끊김일 경우 현재 상태에 코드값 20 설정
			EventMem memEvt = MemDB.EVT.select(idV);
			if (memEvt != null) {
				Event evt = memEvt.getData();
				if ( evt != null ) {
	            	// 네트워크연결끊김이 마지막일 경우 상태 추가
					if ( Event.EventCode.UNREACHABLE.toString().equals(evt.getEvtcd()) ) {
	            		vhclStts.setDrvStts(20);
	            		
	            	// 충전중일때는 충전중 추가
	            	} else if ( Event.EventCode.CHARGE_START.toString().equals(evt.getEvtcd()) ) {
	            		vhclStts.setDrvStts(3);
	            		vhclStts.setSpd(0f);
	            		vhclStts.setDestPoi(null);
	            		vhclStts.setFromPoi(null);
	            		VVhcl vhcl = MemDB.VHCL.select(idV);
	    				if (vhcl!= null) {
	    					vhcl.setDrvStts(3);	// 충전중
	    					vhcl.setIdDr(vhclStts.getIdDr());
	    					vhcl.setSpd(0f);
	    					vhcl.setDestPoi(null);
	    					vhcl.setFromPoi(null);
	    					MemDB.VHCL.insertSafety(vhcl.getIdV(), vhcl);	// 상태값 다시 저장
	    				}
	            	}
				}
			}
			vhclSttsMapper.updateByIdvSelective(vhclStts);
		}
		
	}
	

}
