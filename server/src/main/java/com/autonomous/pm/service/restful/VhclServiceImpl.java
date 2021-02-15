package com.autonomous.pm.service.restful;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.autonomous.pm.dao.DrvRawYymmMapper;
import com.autonomous.pm.dao.SnsrRawYymmMapper;
import com.autonomous.pm.dao.TrpRawMapper;
import com.autonomous.pm.dao.UsrAthMapper;
import com.autonomous.pm.dao.VhclMapper;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.TSnsrRawYymm;
import com.autonomous.pm.model.Do.TTrpRaw;
import com.autonomous.pm.model.Do.TUsr;
import com.autonomous.pm.model.Do.TVhcl;
import com.autonomous.pm.model.Dto.DrvRawYymm;
import com.autonomous.pm.model.Dto.ReqAdminPoi;
import com.autonomous.pm.model.Dto.ReqAdminVhcl;
import com.autonomous.pm.model.Dto.ResAdminVhcl;
import com.autonomous.pm.model.Dto.Vhcl;
import com.autonomous.pm.model.Dto.VhclStts;
import com.autonomous.pm.service.VhclSttsServiceImpl;
import com.autonomous.pm.util.MyUtil;
import com.autonomous.pm.util.StringUtil;
import com.autonomous.pm.util.ValidationUtil;

@Service
public class VhclServiceImpl implements VhclService {

	public static final Logger logger = LoggerFactory.getLogger(VhclServiceImpl.class);

	@Autowired
	VhclSttsServiceImpl vhclSttsService;
	@Autowired
	VhclMapper vhclMapper;
	@Autowired
	DrvRawYymmMapper drvRawYymmMapper;
	@Autowired
	TrpRawMapper trpRawMapper;
	@Autowired
	SnsrRawYymmMapper snsrRawYymmMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<TVhcl> selectAll() {
		return vhclMapper.selectAll();
	}

	public List<String> getVrnsByTerm(String term) {
		List<String> vrns = new ArrayList<String>();
		MemDB.VHCL.selectAll().stream()
			.filter(v->v.getTerm().equals(term))
			.forEach(v->{
				vrns.add(v.getVrn());
			});
		return vrns;
	}

	public List<String> getAllVrns() {
		List<String> vrns = new ArrayList<String>();
		selectAll().stream().forEach(v -> {
			vrns.add(v.getVrn());
		});
		return vrns;
	}

	public List<ResAdminVhcl> getAdminVhclLs(String term) {
		return vhclMapper.getAdminVhclLs(term);
	}

	/**
	 * @throws BindException 
	 */
	@Override
	public int postAdminVhclLs(List<ReqAdminVhcl> reqParam, BindingResult bindingResult) throws BindException {
		for (ReqAdminVhcl adminVhcl : reqParam) {
			
			Long idV = adminVhcl.getIdV();
			String pwd = adminVhcl.getPwd();
			
			if (idV == null && StringUtil.isEmpty(pwd)) {
				bindingResult.addError(error);
				throw new BindException(bindingResult);

			} else if (idV != null && StringUtil.isEmpty(pwd)) {
				TVhcl current = vhclMapper.findById(idV);
				pwd = current.getPwd();
				adminVhcl.setPwd(pwd);
			}
			

			if (pwd != null && !pwd.contains("{sha256}") && !pwd.contains("{bcrypt}") && !pwd.contains("{pbkdf2}") && !pwd.contains("{noop}")) {
				
				if ( ValidationUtil.isNotPasswordValidate(pwd) ){
					String errTxt = ValidationUtil.passwordValidator(pwd);
					FieldError error = new FieldError("reqAdminUsrList", "pwd", errTxt);
					bindingResult.addError(error);
					throw new BindException(bindingResult);
				} else {
					String encPassword = passwordEncoder.encode(pwd);
					adminVhcl.setPwd(encPassword);
				}
			}

			Long idGop = vhclMapper.getIdGop(adminVhcl.getTerm());
			TVhcl vhcl = adminVhcl.toEntity();
			vhcl.setIdGop(idGop);
			vhclMapper.postAdminVhclLs(vhcl);
			
			if ( vhcl.getIdV() == null ) {
				TVhcl newVhcl = vhclMapper.findByVrn(vhcl.getVrn());	// 차량번호로 idV를 찾는다
				VhclStts vhclStts = new VhclStts();
				vhclStts.setIdV(newVhcl.getIdV());
				vhclSttsService.insert(vhclStts);
			}
		}
		loadAll(); // MemDB.VHCL 초기화
		return 1;
	}

	@Override
	public List<Long> checkDuplicateByVrn(List<ReqAdminVhcl> reqParam) {
		List<Long> duplicatedIdV = new ArrayList<Long>();
		for (ReqAdminVhcl adminVhcl : reqParam) {
			List<TVhcl> vhclList = vhclMapper.checkDuplicateByVrn(adminVhcl.toEntity());
			for (TVhcl vhcl : vhclList) {
				duplicatedIdV.add(vhcl.getIdV());
			}
		}

		return duplicatedIdV;
	}
	
	@Override
	public boolean checkVrnPwd(String vrn, String pwd) {
		TVhcl vhcl = vhclMapper.findByVrn(vrn);
		if ( vhcl == null ) {
			return false;
		}
		String savedPwd = vhcl.getPwd();
		return passwordEncoder.matches(pwd, savedPwd);
	}

	/**
	 * 
	 * @return
	 */
	public void loadAll() {

		List<Vhcl> vhcls = vhclMapper.selectAllView();
		vhcls.stream().forEach(v -> {
			Long idDr = v.getIdDr();
			Long idSnsr = v.getIdSnsr();
			Long idTrip = v.getIdTrip();

			DrvRawYymm drvRawYymm = drvRawYymmMapper.selectById(idDr);
			TSnsrRawYymm snsrRawYymm = snsrRawYymmMapper.selectById(idSnsr);
			TTrpRaw trpRaw = trpRawMapper.selectById(idTrip);
			v.setDrvRaw(drvRawYymm);
			v.setSnsrRawYymm(snsrRawYymm);
			v.setTrpRaw(trpRaw);

		});

		// MemDB clear & insert
		MemDB.VHCL.clearAll();
		vhcls.stream().forEach(v -> {
			MemDB.VHCL.insertSafety(v.getIdV(), v);
		});
	}

}
