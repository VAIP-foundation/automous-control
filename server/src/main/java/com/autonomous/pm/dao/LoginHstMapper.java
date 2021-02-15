package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.model.Do.TLoginHst;
import com.autonomous.pm.model.Dto.ReqAdminLoginHst;
import com.autonomous.pm.model.Dto.ResAdminLoginHst;

public interface LoginHstMapper {
	
	List<ResAdminLoginHst> getAdminLoginHstLs(ReqAdminLoginHst reqParam);
	int postAdminLoginHstLs(TLoginHst reqParam);

}