package com.autonomous.pm.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonomous.pm.dao.RouteHstMapper;
import com.autonomous.pm.dao.SnsrRawYymmMapper;
import com.autonomous.pm.domain.structure.Location;
import com.autonomous.pm.domain.structure.mem.RouteInfoMem;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Dto.RouteHst;
import com.autonomous.pm.util.GpsUtil;

@Service
public class RouteInfoServiceImpl implements RouteInfoService {

	public static final Logger logger = LoggerFactory.getLogger(RouteInfoServiceImpl.class);

	@Autowired
	RouteHstMapper routeHstMapper;

	@Override
	public void insertInfo(RouteInfoMem info) {
		MemDB.RTI.insertSafety(info.getIdV(), info);
	}

	@Override
	public void insertInfo(List<RouteInfoMem> infos) {
		
		
		// DB저장
		List<RouteHst> entityList = new ArrayList<RouteHst>();
		for ( RouteInfoMem wri : infos ) {
			entityList.add(wri.toEntity());
			// 데이터 MemDB 저장
			MemDB.RTI.insertSafety(wri.getIdV(), wri);
			
			
			// 시간계산
			Location[] points = wri.getData().getPoints();
			Double distance = GpsUtil.distanceByLocations(points);
			Double sec = GpsUtil.kilometersToSeconds(distance);
		}
		
		// DB저장(multi-insert)
		routeHstMapper.insertList(entityList);
		
	}
	

}
