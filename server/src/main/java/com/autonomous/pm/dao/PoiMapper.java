package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.domain.common.SearchParam;
import com.autonomous.pm.model.Do.TPoi;
import com.autonomous.pm.model.Do.TUsrGrp;
import com.autonomous.pm.model.Dto.ReqAdminPoi;
import com.autonomous.pm.model.Dto.ReqAdminUsrGrp;
import com.autonomous.pm.model.Dto.ResAdminGop;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.model.Dto.ResAdminUsrGrp;

public interface PoiMapper {

	List<ResAdminPoi> getAllPoiLs();
	
	List<ResAdminPoi> getAdminPoiLs(String term);
	
	Long insertAdminPoiLs(ReqAdminPoi reqAdminPoi);
	
	Long updateAdminPoiLs(ReqAdminPoi reqAdminPoi);
	
	int insertGateMap(ReqAdminPoi reqAdminPoi);
	
	int updateGateMap(Long idPoi);
	
	List<ResAdminGop> getAdminGopLs(String term);

}