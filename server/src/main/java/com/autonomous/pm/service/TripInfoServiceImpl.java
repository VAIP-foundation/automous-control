package com.autonomous.pm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonomous.pm.dao.TrpRawMapper;
import com.autonomous.pm.domain.structure.TripInfo;
import com.autonomous.pm.domain.structure.mem.TripInfoMem;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.TTrpRaw;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.model.Dto.PmAssgn;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TripInfoServiceImpl implements TripInfoService {

	public static final Logger logger = LoggerFactory.getLogger(TripInfoServiceImpl.class);

	@Autowired
	VhclSttsServiceImpl vhclSttsService;
	@Autowired
	CallServiceImpl callService;
	@Autowired
	TrpRawMapper trpRawMapper;
	

	@Override
	public void insertInfo(TripInfoMem info) {
		MemDB.TRP.insertSafety(info.getIdV(), info);
	}

	@Override
	public void insertInfo(TTrpRaw info) {
		trpRawMapper.insert(info);
	}

	@Override
	public void insertInfo(List<TripInfoMem> infos) {
		ArrayList<Long> idVs = new ArrayList<Long>();
		
		for ( TripInfoMem wti : infos ) {
			// 데이터 MemDB.TRP 저장
			Long idV = wti.getIdV();
			MemDB.TRP.insertSafety(idV, wti);
			if ( !idVs.contains(idV) ) {
				idVs.add(idV);
			}
			
			TTrpRaw trpRaw = wti.toEntity();
			if (wti.getData().getTy() == 1) {	// START
				trpRawMapper.updateToUnfinished(idV);	
				trpRawMapper.insertAtStart(trpRaw);		
				log.debug("trpRaw.getIdTrip()"+trpRaw.getIdTrip());
			} else if (wti.getData().getTy() == 2) { // FIN
				trpRaw.setIdTrip(trpRawMapper.getTripId(trpRaw));
				trpRawMapper.updateAtFin(trpRaw);		
			} else {
				logger.error("TripInfo don't have type [1 or 2]");
			}
			
			callService.updatePmAssgnByTrip(trpRaw);	
			vhclSttsService.update(idV, wti.getData());	
		}
		
	}

	/**
	 * @param idV
	 */
	public void updateFinishOnBye(Long idV) {

		TripInfoMem tim = MemDB.TRP.select(idV);
		TripInfo ti = tim.getData();
		if ( ti.getTy() == 1) {		
			ti.setTy(2);			
			ti.setTs(new Date());	
			ti.setResult(1); 		
			ti.setReason(100);		
			ti.setDist(null);		
			tim.setData(ti);
			
			TTrpRaw trpRaw = tim.toEntity();
			trpRaw.setIdTrip(trpRawMapper.getTripId(trpRaw));
			trpRawMapper.updateAtFin(trpRaw);			

			MemDB.TRP.insertSafety(idV, tim);			
			
			callService.updatePmAssgnByTrip(trpRaw);	
			vhclSttsService.update(idV, tim.getData());	
		}
		
	}
	public void updateFinishOnBye(String vrn) {
		VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->v.getVrn().equals(vrn)).findFirst().orElse(null);
		if ( vhcl != null) {
			updateFinishOnBye(vhcl.getIdV());
		}
	}

	/**
	 * @param idV
	 */
	public void updateStartOnContinueJoin(Long idV) {

		TripInfoMem tim = MemDB.TRP.select(idV);
		TripInfo ti = tim.getData();
		if ( ti!=null && ti.getTy() == 2 && ti.getReason() == 100 ) {	
			ti.setTy(1);			
			ti.setTs(new Date());	
			ti.setResult(null); 	
			ti.setReason(null);		
			ti.setDist(null);		
			tim.setData(ti);
			MemDB.TRP.insertSafety(idV, tim);		// 다시 저장
			
			TTrpRaw trpRaw = new TTrpRaw();
			trpRaw.setIdV(idV);
			trpRaw.setIdTrip(trpRawMapper.getTripIdOnFin(trpRaw));
			trpRaw.setFinRslt(null);	
			trpRaw.setRsn(null);		
			trpRaw.setArvDt(null);		
		trpRawMapper.rollbackAtStart(trpRaw);		
			
			callService.rollbackAtStart(trpRaw);		
			vhclSttsService.update(idV, tim.getData());	
		}
		
	}
	public void updateStartOnContinueJoin(String vrn) {
		VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->v.getVrn().equals(vrn)).findFirst().orElse(null);
		if ( vhcl != null) {
			updateStartOnContinueJoin(vhcl.getIdV());
		}
	}
	

}
