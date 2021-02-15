package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.model.Do.TVhcl;
import com.autonomous.pm.model.Dto.ResAdminVhcl;
import com.autonomous.pm.model.Dto.Vhcl;

public interface VhclMapper {
	
	TVhcl findById(Long idV);
	TVhcl findByVrn(String vrn);
	
	List<TVhcl> selectAll();
	
	List<Vhcl> selectAllView();
	
	List<ResAdminVhcl> getAdminVhclLs(String term);
	
	Long getIdGop(String term);

	int postAdminVhclLs(TVhcl reqParam);
	
	List<TVhcl> checkDuplicateByVrn(TVhcl vrn);

	
}