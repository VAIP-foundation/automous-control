package com.autonomous.pm.service.restful;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonomous.pm.dao.PoiGateMapMapper;
import com.autonomous.pm.dao.PoiMapper;
import com.autonomous.pm.domain.structure.Poi;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.TPoiGateMap;
import com.autonomous.pm.model.Do.TSnsrRawYymm;
import com.autonomous.pm.model.Do.TTrpRaw;
import com.autonomous.pm.model.Do.VPoiGate;
import com.autonomous.pm.model.Dto.DrvRawYymm;
import com.autonomous.pm.model.Dto.ReqAdminPoi;
import com.autonomous.pm.model.Dto.ResAdminGop;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.model.Dto.Vhcl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PoiServiceImpl implements PoiService {

	public static final Logger logger = LoggerFactory.getLogger(PoiServiceImpl.class);

	@Autowired
	PoiMapper poiMapper;
	
	@Autowired
	PoiGateMapMapper poiGateMapMapper;
	
	@Override
	public List<ResAdminPoi> getAdminPoiLs(String term) {
		
		List<ResAdminPoi> list = poiMapper.getAdminPoiLs(term);
		
		for(ResAdminPoi row : list) {
			Long idPoi = row.getIdPoi();
			List<String> gateList = poiGateMapMapper.getGateByIdPoi(idPoi);
			String[] gateStr = new String[gateList.size()];
			row.setGateNo(gateList.toArray(gateStr));
		}
		return list;
		
	}

	@Override
	public int postAdminPoiLs(List<ReqAdminPoi> reqParam) {
		for (ReqAdminPoi adminPoi : reqParam) {
			Long idPoi = adminPoi.getIdPoi();
			if ( idPoi == null ) { // idPoi가 없는 경우 insert
				poiMapper.insertAdminPoiLs(adminPoi);
				idPoi = adminPoi.getIdPoi();
			} else { // idPoi가 있는 경우 update
				Long result = poiMapper.updateAdminPoiLs(adminPoi);
				if(result == 0) {
					poiMapper.insertAdminPoiLs(adminPoi);
					idPoi = adminPoi.getIdPoi();
				} else {
					poiMapper.updateGateMap(idPoi);
				}
			}
			String[] gateNo = adminPoi.getGateNo();
			if ( gateNo != null ) {
				for(int i = 0; i < gateNo.length; i++) {
					adminPoi.setIdPoi(idPoi);
					adminPoi.setGate(gateNo[i]);
					poiMapper.insertGateMap(adminPoi);
				}
			}
		}
		loadAll(); // MemDB.POI 초기화
		return 1;
	}

	@Override
	public List<ResAdminGop> getAdminGopLs(String term) {
		return poiMapper.getAdminGopLs(term);
	}
	
	/**
	 * @param gateNo
	 * @return ResAdminPoi
	 */
	@Override
	public ResAdminPoi getPoiByGateNo(String gateNo) {
		if ( gateNo == null ) return null;
		
		List<VPoiGate> gateList = MemDB.GATE.selectAll();
		VPoiGate gate = gateList.stream().filter(g->gateNo.equals(g.getGateNo())).findFirst().orElse(null);
		if ( gate != null && gate.getIdPoi() != null) {
			return MemDB.POI.select(gate.getIdPoi());
		} else {
			return null;
		}
	}
	

	/**
	 * @param lng
	 * @param lat
	 * @return ResAdminPoi
	 */
	@Override
	public ResAdminPoi getPoiByClosestGPS(Long lng, Long lat) {
		return null;	// TODO:
	}
	
	
	/**
	 * @return
	 */
	public void loadAll(){
		List<ResAdminPoi> pois = poiMapper.getAllPoiLs();
		
		// MemDB clear & insert
		MemDB.POI.clearAll();
		pois.stream().forEach(p->{
			MemDB.POI.insertSafety(p.getIdPoi(), p);
		});
	}
	
}
