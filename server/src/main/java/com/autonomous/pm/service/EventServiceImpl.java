package com.autonomous.pm.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonomous.pm.dao.DrvRawYymmMapper;
import com.autonomous.pm.dao.EvtHstMapper;
import com.autonomous.pm.domain.structure.mem.DrivingInfoMem;
import com.autonomous.pm.domain.structure.mem.EventMem;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.TEvtHst;
import com.autonomous.pm.model.Dto.DrvRawYymm;

@Service
public class EventServiceImpl implements EventService {

	public static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

	@Autowired
	EvtHstMapper evtHstMapper;

	@Override
	public void insertInfo(EventMem info) {
		// DB 저장
		MemDB.EVT.insertSafety(info.getIdV(), info);
	}

	@Override
	public void insertInfo(List<EventMem> infos) {
		
		// DB저장
		List<TEvtHst> entityList = new ArrayList<TEvtHst>();
		for ( EventMem wdi : infos ) {
			entityList.add(wdi.toEntity());
			// 데이터 MemDB 저장
			MemDB.EVT.insertSafety(wdi.getIdV(), wdi);
		}
		// DB저장(multi-insert)
		evtHstMapper.insertList(entityList);
		
	}
	

}
