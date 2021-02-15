package com.autonomous.pm.service.restful;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.autonomous.pm.auth.UsrDetailsImpl;
import com.autonomous.pm.model.Do.TLoginHst;
import com.autonomous.pm.model.Dto.ReqAdminLoginHst;
import com.autonomous.pm.model.Dto.ResAdminLoginHst;

public interface LoginHstService {

	void insertLoginHst(HttpServletRequest request, UsrDetailsImpl userDetails);
	void insertLogoutHst(HttpServletRequest request, UsrDetailsImpl userDetails);
	List<ResAdminLoginHst> getAdminLoginHstLs(ReqAdminLoginHst reqParam);
	int postAdminLoginHstLs(TLoginHst reqParam);
	void postAdminLogoutHstByToken(String token, String loginTy);
	
}