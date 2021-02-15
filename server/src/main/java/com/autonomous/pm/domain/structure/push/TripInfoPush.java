package com.autonomous.pm.domain.structure.push;

import org.springframework.beans.BeanUtils;

import com.autonomous.pm.domain.structure.Gps;
import com.autonomous.pm.domain.structure.Poi;
import com.autonomous.pm.domain.structure.TripInfo;
import com.autonomous.pm.domain.structure.mem.DrivingInfoMem;
import com.autonomous.pm.domain.structure.mem.EventMem;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.util.GpsUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TripInfoPush extends TripInfo {	// UI PUSH를 위한 구조체. 일부 항목을 추가하였다.
	
	private Long idV;	// 차량ID
	private String vrn;	// 차량번호
	private Poi from;	// 출발지 정보
	
	public TripInfoPush() {
	}
	
	public TripInfoPush(Long idV, String vrn, TripInfo tripInfo) {
		BeanUtils.copyProperties(tripInfo, this);
		this.setIdV(idV);
		this.setVrn(vrn);
	}
	
}