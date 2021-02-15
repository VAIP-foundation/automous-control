package com.autonomous.pm.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.autonomous.pm.dao.SnsrRawYymmMapper;
import com.autonomous.pm.dao.VhclSttsMapper;
import com.autonomous.pm.domain.structure.Battery;
import com.autonomous.pm.domain.structure.SensorInfo;
import com.autonomous.pm.domain.structure.mem.SensorInfoMem;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.TSnsrRawYymm;
import com.autonomous.pm.model.Dto.VhclStts;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SensorInfoServiceImpl implements SensorInfoService {

	public static final Logger logger = LoggerFactory.getLogger(SensorInfoServiceImpl.class);

	@Autowired
	SnsrRawYymmMapper snsrRawYymmMapper;
	@Autowired
	VhclSttsMapper vhclSttsMapper;

	@Value("${vhcl.driving.able.sec:7200}")	// 운행가능 시간. 미설정시 2시간
	private Integer drivingAbleSec;

	@Value("${vhcl.driving.able.bat.rem:0.2}")	// 운행가능 배터리잔량. 미설정시 0.2
	private Float drivingAbleBatRem;

	@Value("${vhcl.driving.able.bat.volt:68}")	// 운행가능 전압. 미설정시 68V
	private Integer drivingAbleBatVolt;
	
	@Override
	public void insertInfo(SensorInfoMem info) {
		MemDB.SNSR.insertSafety(info.getIdV(), info);
		
	}

	@Override
	public void insertInfo(List<SensorInfoMem> infos) {
		ArrayList<Long> idVs = new ArrayList<Long>();
		
		// DB저장
		List<TSnsrRawYymm> entityList = new ArrayList<TSnsrRawYymm>();
		for ( SensorInfoMem wsi : infos ) {
			entityList.add(wsi.toEntity());
			Long idV = wsi.getIdV();
			// 데이터 MemDB 저장
			MemDB.SNSR.insertSafety(idV, wsi);
			if ( !idVs.contains(idV) ) {
				idVs.add(idV);
			}
//			MemDB.SNSR.insertSafety(idV, infos.get(infos.size()-1));
		}
		// DB저장(multi-insert)
		snsrRawYymmMapper.insertList(entityList);
		
		// 마지막 데이터 T_VHCL_STTS 에 업데이트
		for( Long idV : idVs ) {
			TSnsrRawYymm lastSnsrRawYymm = snsrRawYymmMapper.selectLastByIdV(idV);
			VhclStts vhclStts = new VhclStts();
			vhclStts.setIdV(idV);
			vhclStts.setIsSnsr(lastSnsrRawYymm.getIdSnsr());
			vhclSttsMapper.updateByIdvSelective(vhclStts);
		}

	}
	
	
	// 차량의 마지막 운행가능시간이 설정된 시간보다 적으면 true를 반환한다.
	@Override
	public boolean isBatLow(Long idV) {
		SensorInfoMem sim = MemDB.SNSR.select(idV);
		if ( sim != null && sim.getData() != null) {
			SensorInfo lastSensorData = sim.getData();
			Integer dr_s_abl = lastSensorData.getDr_s_abl();	// 운행가능시간.초단위(분단위갱신)
			
			// 1. 운행가능시간이 기준보다낮으면 배터리 부족으로 판단
			if ( dr_s_abl == null ) {
				return true;
			} else if ( dr_s_abl < drivingAbleSec ) {
				return true;
			}
			
			if ( lastSensorData.getBat() != null) {
				// 2. 운행가능 배터리 잔량이 기준보다낮으면 배터리 부족으로 판단
				Battery lastBatteryData = lastSensorData.getBat();
				BigDecimal batteryRemain = lastBatteryData.getRem();
				if ( batteryRemain == null ) {
					return true;
				} else if ( batteryRemain.floatValue() < drivingAbleBatRem ) {
					return true;
				}
				
				// 3. 운행가능 배터리 전압이 기준보다낮으면 배터리 부족으로 판단
				BigDecimal batteryVoltage = lastBatteryData.getVolt();
				if ( batteryVoltage == null ) {
					return true;
				} else if ( batteryVoltage.intValue() < drivingAbleBatVolt ) {
					return true;
				}
			}

			return false;
		} else {
			log.error("VRN has not SensorInfo! idV={}", idV);
			return false;
		}
	}

}
