package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.domain.common.SearchParam;
import com.autonomous.pm.model.Do.TUsrGrp;
import com.autonomous.pm.model.Dto.ReqAdminUsrGrp;
import com.autonomous.pm.model.Dto.ResAdminUsrGrp;

public interface UsrGrpMapper {
	
	TUsrGrp findById(Long idUgrp);
	List<ResAdminUsrGrp> getAdminUsrGrpLs(String grpNm);
	List<ResAdminUsrGrp> getAdminUsrGrpLsByAthCd(Integer authCd);
	
	int postAdminUsrGrpLs(TUsrGrp tUsrGrp);
	

}