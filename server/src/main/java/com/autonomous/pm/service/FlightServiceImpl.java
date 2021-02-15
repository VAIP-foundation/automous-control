package com.autonomous.pm.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.autonomous.pm.dao.IisAfsQueueMapper;
import com.autonomous.pm.domain.structure.FlightInfo;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.CallPlanP2;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.model.Dto.IisAfsQueue;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.util.MyUtil;

@Service
public class FlightServiceImpl implements FlightService {

	public static final Logger logger = LoggerFactory.getLogger(FlightServiceImpl.class);
	
	@Value("${pm.flight.sch.start.Min:-10}")
	Integer pmFlightSchStartMin;
	@Value("${pm.flight.sch.end.Min:180}")
	Integer pmFlightSchEndMin;
	
	@Autowired
	CallServiceImpl callServiceImpl;
	
	@Autowired
	IisAfsQueueMapper iisAfsQueueMapper;
	
	
	/**
	 */
	public List<FlightInfo> getFlightSchLs(String vrn){
		List<FlightInfo> fiList = new ArrayList<FlightInfo>();
		
		VVhcl vhcl =  MemDB.VHCL.selectAll().stream()
				.filter(v->v.getVrn().equals(vrn))
				.findFirst().orElse(null);
		
		if ( vhcl != null ) {
			String term = vhcl.getTerm();
	        List<IisAfsQueue> queueList = iisAfsQueueMapper.selectFlightSchLs(pmFlightSchStartMin, pmFlightSchEndMin, term);
	        
	        queueList = callServiceImpl.filterQueueByGateNo(queueList, term);	
	        
	        queueList.stream()
				.forEach(q->{
					fiList.add(q.toFlightInfo());
				});
        }
		
        return fiList;
	}
	
}
