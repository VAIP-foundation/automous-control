package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.model.Do.TTrpRaw;
import com.autonomous.pm.model.Dto.ReqFromToDay;
import com.autonomous.pm.model.Dto.ResTrip;
import com.autonomous.pm.model.Dto.RestRawTrip;

public interface TrpRawMapper {

	List<TTrpRaw> selectLastAll();
	
	TTrpRaw selectById(Long idTrip);
	TTrpRaw selectLastByIdV(Long idV);
	
	
	Long getTripId(TTrpRaw data);
	Long getTripIdOnFin(TTrpRaw data);
	
	int insertAtStart(TTrpRaw data);
	int updateAtFin(TTrpRaw data);
	int rollbackAtStart(TTrpRaw data);
	int updateToUnfinished(Long idV);
	
	int insert(TTrpRaw data);
	int insertList(List<TTrpRaw> datas);
	
	// 항공+PM 운행정보 조회
//	List<ResTrip> selectTrpLs(String term, Integer trpUiStartMin, Integer trpUiEndMin);
	List<ResTrip> selectTrpLs(String term);
	
	List<RestRawTrip> selectRawList(ReqFromToDay reqParam);
}