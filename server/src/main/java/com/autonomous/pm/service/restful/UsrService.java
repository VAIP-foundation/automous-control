package com.autonomous.pm.service.restful;

import java.util.List;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import com.autonomous.pm.model.Dto.ReqAdminUsr;
import com.autonomous.pm.model.Dto.ResAdminUsr;

public interface UsrService {

	List<ResAdminUsr> getAdminUsrLs(Integer idUgrp, String usrNm);
	void postAdminUsrLs(List<ReqAdminUsr> reqParam, BindingResult bindingResult) throws BindException;
	
}