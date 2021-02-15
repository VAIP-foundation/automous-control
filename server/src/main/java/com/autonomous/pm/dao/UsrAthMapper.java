package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.domain.common.SearchParam;
import com.autonomous.pm.model.Do.TUsrAth;
import com.autonomous.pm.model.Dto.ResAdminUsrAth;

public interface UsrAthMapper {
	
	List<ResAdminUsrAth> getAdminUsrAthLs(String athNm);
	int postAdminUsrAthLs(TUsrAth tUsrAth);

}