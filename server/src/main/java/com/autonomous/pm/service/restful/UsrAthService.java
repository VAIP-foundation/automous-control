package com.autonomous.pm.service.restful;

import java.util.List;

import com.autonomous.pm.domain.common.SearchParam;
import com.autonomous.pm.model.Dto.ReqAdminUsrAth;
import com.autonomous.pm.model.Dto.ResAdminUsrAth;

public interface UsrAthService {
	
	List<ResAdminUsrAth> getAdminUsrAthLs(String athNm);
	int postAdminUsrAthLs(List<ReqAdminUsrAth> reqParam);
	void addJwtSession();
	void checkJwtSession();
	void removeJwtSessionByUsername(String username);
	void removeJwtSessionByAccessToken(String accessToken);
	
}
