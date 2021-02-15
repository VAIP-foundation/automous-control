package com.autonomous.pm.service.restful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonomous.pm.dao.EvtHstMapper;
import com.autonomous.pm.dao.PmAssgnMapper;
import com.autonomous.pm.dao.PoiMapper;
import com.autonomous.pm.dao.StatMapper;
import com.autonomous.pm.dao.TrpRawMapper;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.model.Dto.ReqFromToDay;
import com.autonomous.pm.model.Dto.ReqFromToMonth;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.model.Dto.ResCnflctCnt;
import com.autonomous.pm.model.Dto.ResDrKmCnt;
import com.autonomous.pm.model.Dto.ResDrTmsCnt;
import com.autonomous.pm.model.Dto.ResFailCnt;
import com.autonomous.pm.model.Dto.ResMStopCnt;
import com.autonomous.pm.model.Dto.ResMeStopCnt;
import com.autonomous.pm.model.Dto.ResPeoplCnt;
import com.autonomous.pm.model.Dto.ResRStopCnt;
import com.autonomous.pm.model.Dto.ResStatSheet;
import com.autonomous.pm.model.Dto.ResTotal;
import com.autonomous.pm.model.Dto.ResTripCnt;
import com.autonomous.pm.model.Dto.RestRawCall;
import com.autonomous.pm.model.Dto.RestRawEvent;
import com.autonomous.pm.model.Dto.RestRawTrip;
import com.autonomous.pm.model.Dto.RestStTrip;
import com.autonomous.pm.util.GpsUtil;

@Service
public class StatServiceImpl implements StatService {

	public static final Logger logger = LoggerFactory.getLogger(StatServiceImpl.class);

	@Autowired
	StatMapper statMapper;
	@Autowired
	PoiMapper poiMapper;
	@Autowired
	EvtHstMapper evtHstMapper;
	@Autowired
	TrpRawMapper trpRawMapper;
	@Autowired
	PmAssgnMapper pmAssgnMapper;
	
	

	public List<ResDrKmCnt> getStatDistDailyLs(ReqFromToDay reqParam) {
		return statMapper.getStatDistDailyLs(reqParam);
	}

	public List<ResDrKmCnt> getStatDistMonthlyLs(ReqFromToMonth reqParam) {
		return statMapper.getStatDistMonthlyLs(reqParam);
	}

	public List<ResDrTmsCnt> getStatDrvtmDailyLs(ReqFromToDay reqParam) {
		return statMapper.getStatDrvtmDailyLs(reqParam);
	}

	public List<ResDrTmsCnt> getStatDrvtmMonthlyLs(ReqFromToMonth reqParam) {
		return statMapper.getStatDrvtmMonthlyLs(reqParam);
	}

	public List<ResPeoplCnt> getStatUsersDailyLs(ReqFromToDay reqParam) {
		return statMapper.getStatUsersDailyLs(reqParam);
	}

	public List<ResPeoplCnt> getStatUsersMonthlyLs(ReqFromToMonth reqParam) {
		return statMapper.getStatUsersMonthlyLs(reqParam);
	}

	public List<ResTripCnt> getStatTripcntDailyLs(ReqFromToDay reqParam) {
		return statMapper.getStatTripcntDailyLs(reqParam);
	}

	public List<ResTripCnt> getStatTripcntMonthlyLs(ReqFromToMonth reqParam) {
		return statMapper.getStatTripcntMonthlyLs(reqParam);
	}

	public List<ResRStopCnt> getStatRstopcntDailyLs(ReqFromToDay reqParam) {
		return statMapper.getStatRstopcntDailyLs(reqParam);
	}

	public List<ResRStopCnt> getStatRstopcntMonthlyLs(ReqFromToMonth reqParam) {
		return statMapper.getStatRstopcntMonthlyLs(reqParam);
	}

	
	
	public List<ResMStopCnt> getStatMStopCntDailyLs(ReqFromToDay reqParam) {
		return statMapper.getStatMStopcntDailyLs(reqParam);
	}

	public List<ResMStopCnt> getStatMStopCntMonthlyLs(ReqFromToMonth reqParam) {
		return statMapper.getStatMStopcntMonthlyLs(reqParam);
	}

	public List<ResMeStopCnt> getStatMeStopCntDailyLs(ReqFromToDay reqParam) {
		return statMapper.getStatMeStopcntDailyLs(reqParam);
	}

	public List<ResMeStopCnt> getStatMeStopCntMonthlyLs(ReqFromToMonth reqParam) {
		return statMapper.getStatMeStopcntMonthlyLs(reqParam);
	}
	
	
	public List<ResCnflctCnt> getStatCrshDailyLs(ReqFromToDay reqParam) {
		return statMapper.getStatCrshDailyLs(reqParam);
	}

	public List<ResCnflctCnt> getStatCrshMonthlyLs(ReqFromToMonth reqParam) {
		return statMapper.getStatCrshMonthlyLs(reqParam);
	}

	public List<ResFailCnt> getStatFailDailyLs(ReqFromToDay reqParam) {
		return statMapper.getStatFailDailyLs(reqParam);
	}

	public List<ResFailCnt> getStatFailMonthlyLs(ReqFromToMonth reqParam) {
		return statMapper.getStatFailMonthlyLs(reqParam);
	}

	public List<ResTotal> getStatTotalDailyLs(ReqFromToDay reqParam) {
		return statMapper.getStatTotalDailyLs(reqParam);
	}

	public List<ResTotal> getStatTotalMonthlyLs(ReqFromToMonth reqParam) {
		return statMapper.getStatTotalMonthlyLs(reqParam);
	}

	
	
	
	


	@Override
	public List<ResStatSheet> getStatExcelDailyLs(ReqFromToDay reqParam) {
		List<ResStatSheet> result = new ArrayList<ResStatSheet>();
		
		// 1. 전체 데이터 조회
		List<RestStTrip> totalData = getStatExcelTotalDailyLs(reqParam);
		ResStatSheet sheet1 = new ResStatSheet();
		sheet1.setSheet("total");
		sheet1.setData(totalData);
		result.add(sheet1);
		
		// 2. 합계 데이터 조회
		List<RestStTrip> sumData = getStatExcelTotalSumDailyLs(reqParam);
		ResStatSheet sheet2 = new ResStatSheet();
		sheet2.setSheet("sum");
		sheet2.setData(sumData);
		result.add(sheet2);
		
		// 3. 전체 데이터 내 POI 출발/도착 횟수 데이터를 기반으로 sheet-data 생성 
		ResStatSheet sheet3 = getStatExcelPoiLs(totalData, "T1");
		result.add(sheet3);
		ResStatSheet sheet4 = getStatExcelPoiLs(totalData, "T2");
		result.add(sheet4);
		
		return result;
	}
	@Override
	public List<ResStatSheet> getStatExcelMonthlyLs(ReqFromToMonth reqParam) {
		List<ResStatSheet> result = new ArrayList<ResStatSheet>();
		
		// 1. 전체 데이터 조회
		List<RestStTrip> totalData = getStatExcelTotalMonthlyLs(reqParam);
		ResStatSheet sheet1 = new ResStatSheet();
		sheet1.setSheet("total");
		sheet1.setList(null);
		sheet1.setData(totalData);
		result.add(sheet1);
		
		// 2. 합계 데이터 조회
		List<RestStTrip> sumData = getStatExcelTotalSumMonthlyLs(reqParam);
		ResStatSheet sheet2 = new ResStatSheet();
		sheet2.setSheet("sum");
		sheet2.setData(sumData);
		result.add(sheet2);
		
		// 3. 전체 데이터 내 POI 출발/도착 횟수 데이터를 기반으로 sheet-data 생성 
		ResStatSheet sheet3 = getStatExcelPoiLs(totalData, "T1");
		result.add(sheet3);
		ResStatSheet sheet4 = getStatExcelPoiLs(totalData, "T2");
		result.add(sheet4);
		
		return result;
	}
	

	@Override
	public List<RestStTrip> getStatExcelTotalDailyLs(ReqFromToDay reqParam) {
		List<RestStTrip> result = statMapper.getStatExcelTotalDailyLs(reqParam);
		return result;
	}
	@Override
	public List<RestStTrip> getStatExcelTotalMonthlyLs(ReqFromToMonth reqParam) {
		List<RestStTrip> result = statMapper.getStatExcelTotalMonthlyLs(reqParam);
		return result;
	}
	
	@Override
	public List<RestStTrip> getStatExcelTotalSumDailyLs(ReqFromToDay reqParam) {
		List<RestStTrip> result = statMapper.getStatExcelTotalSumDailyLs(reqParam);
		return result;
	}
	@Override
	public List<RestStTrip> getStatExcelTotalSumMonthlyLs(ReqFromToMonth reqParam) {
		List<RestStTrip> result = statMapper.getStatExcelTotalSumMonthlyLs(reqParam);
		return result;
	}
	
	
	@Override
	public ResStatSheet getStatExcelPoiLs(List<RestStTrip> totalData, String term) {
		ResStatSheet sheet2 = new ResStatSheet();
		
		List<HashMap<String,Object>> sheet2Datas = new ArrayList<HashMap<String,Object>>();
		
		// 1. POI 도착 횟수를 위해 POI정보 조회
		List<ResAdminPoi> poiList = poiMapper.getAllPoiLs();
		List<String> poiCdList = new ArrayList<String>();
		poiList.stream()
			.filter(p->p.getTerm().equals(term))
			.forEach(p->{
//				poiCdList.add(p.getPoiCd()+"_on");
//				poiCdList.add(p.getPoiCd()+"_off");
				poiCdList.add(p.getPoiNm()+"_on");
				poiCdList.add(p.getPoiNm()+"_off");
			});
		
		
		// 2. 전체 데이터 내 POI 도착 횟수 데이터를 기반으로 sheet-data 생성 
		totalData.stream()
			.filter(p->p.getTerm().equals(term))
			.forEach(t->{
				HashMap<String,Object> data = new HashMap<String,Object>();
				data.put("term", t.getTerm());
				data.put("idV", t.getIdV());
				data.put("vrn", t.getVrn());
				data.put("dts", t.getDts());
				poiList.stream()
					.filter(p->p.getTerm().equals(term))
					.forEach(p->{
						data.put(p.getPoiNm()+"_on", 0);
						data.put(p.getPoiNm()+"_off", 0);
					});
	
				String fromPoiCnt = t.getFromPoiCnt();
				if ( fromPoiCnt != null ) {
					String[] fromPoiCntArr = fromPoiCnt.split(",");
					for ( int i=0;i<fromPoiCntArr.length;i++) {
						String poiCd = fromPoiCntArr[i].split("=")[0];
						ResAdminPoi poi = poiList.stream().filter(p->p.getPoiCd().equals(poiCd)).findFirst().orElse(null);
						if ( poi != null ) {
							String poiNm = poi.getPoiNm();
							String poiCnt = fromPoiCntArr[i].split("=")[1];
							data.put(poiNm+"_on", Integer.valueOf(poiCnt));
						}
					}
				}
	
				String destPoiCnt = t.getDestPoiCnt();
				if ( destPoiCnt != null ) {
					String[] destPoiCntArr = destPoiCnt.split(",");
					for ( int i=0;i<destPoiCntArr.length;i++) {
						String poiCd = destPoiCntArr[i].split("=")[0];
						ResAdminPoi poi = poiList.stream().filter(p->p.getPoiCd().equals(poiCd)).findFirst().orElse(null);
						if ( poi != null ) {
							String poiNm = poi.getPoiNm();
							String poiCnt = destPoiCntArr[i].split("=")[1];
							data.put(poiNm+"_off", Integer.valueOf(poiCnt));
						}
					}
				}
				
				sheet2Datas.add(data);
			});

		
		sheet2.setSheet("POI_"+term);
		sheet2.setList(poiCdList);
		sheet2.setData(sheet2Datas);
		
		return sheet2;
	}
	

	@Override
	public List<ResStatSheet> getStatRawEventLs(ReqFromToDay reqParam) {
		List<ResStatSheet> result = new ArrayList<ResStatSheet>();
		
		// 1. 전체 데이터 조회
		List<RestRawEvent> rawData = evtHstMapper.selectRawList(reqParam);
		rawData.stream().forEach(e->{
			VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->v.getIdV()==e.getIdV()).findFirst().orElse(null);
			Double lng = e.getLng();
			Double lat = e.getLat();
			
			if ( vhcl != null ) {
				Long idGop = vhcl.getIdGop();
				if ( lat != null && lng != null ) {
					ResAdminPoi rap = GpsUtil.findClosestPoi(idGop, lat, lng);
					String poiCd = rap.getPoiCd();
					String poiNm = rap.getPoiNm();
					e.setPoiCd(poiCd);
					e.setPoiNm(poiNm);
				}
			}
		});
		
		ResStatSheet sheet1 = new ResStatSheet();
		sheet1.setSheet("event");
		sheet1.setList(null);
		sheet1.setData(rawData);
		result.add(sheet1);
		
		return result;
	}
	
	@Override
	public List<ResStatSheet> getStatRawTripLs(ReqFromToDay reqParam) {
		
		List<ResStatSheet> result = new ArrayList<ResStatSheet>();
		// 1. 전체 데이터 조회
		List<RestRawTrip> rawData = trpRawMapper.selectRawList(reqParam);
		
		ResStatSheet sheet1 = new ResStatSheet();
		sheet1.setSheet("trip");
		sheet1.setList(null);
		sheet1.setData(rawData);
		result.add(sheet1);
		
		return result;
	}
	
	@Override
	public List<ResStatSheet> getStatRawCallLs(ReqFromToDay reqParam) {
		List<ResStatSheet> result = new ArrayList<ResStatSheet>();
		// 1. 전체 데이터 조회
		List<RestRawCall> rawData = pmAssgnMapper.selectRawList(reqParam);
		
		ResStatSheet sheet1 = new ResStatSheet();
		sheet1.setSheet("call");
		sheet1.setList(null);
		sheet1.setData(rawData);
		result.add(sheet1);
		
		return result;
	}
	
	
	
	
	
}