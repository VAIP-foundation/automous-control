package com.autonomous.pm.service.restful;

import java.util.List;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import com.autonomous.pm.model.Do.TVhcl;
import com.autonomous.pm.model.Dto.ReqAdminVhcl;
import com.autonomous.pm.model.Dto.ResAdminVhcl;

public interface VhclService {
	
	List<TVhcl> selectAll();
	
	List<String> getAllVrns();
	List<ResAdminVhcl> getAdminVhclLs(String term);
	int postAdminVhclLs(List<ReqAdminVhcl> reqParam, BindingResult bindingResult) throws BindException;
	List<Long> checkDuplicateByVrn(List<ReqAdminVhcl> reqParam);

	boolean checkVrnPwd(String vrn, String pwd);

}
