package com.autonomous.pm.service.restful;

import java.util.List;

import com.autonomous.pm.domain.common.SearchParam;
import com.autonomous.pm.model.Dto.ReqAdminUsrGrp;
import com.autonomous.pm.model.Dto.ResAdminUsrGrp;

public interface UsrGrpService {
	
	List<ResAdminUsrGrp> getAdminUsrGrpLs(String grpNm);
	int postAdminUsrGrpLs(List<ReqAdminUsrGrp> reqParam);
	
	
}
