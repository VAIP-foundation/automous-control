package com.autonomous.pm.service.restful;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonomous.pm.dao.UsrAthMapper;
import com.autonomous.pm.dao.UsrGrpMapper;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Dto.ReqAdminUsrAth;
import com.autonomous.pm.model.Dto.ResAdminUsrAth;
import com.autonomous.pm.model.Dto.ResAdminUsrGrp;
import com.autonomous.pm.util.JwtUtil;

@Service
public class UsrAthServiceImpl implements UsrAthService {

	public static final Logger logger = LoggerFactory.getLogger(UsrAthServiceImpl.class);

	@Autowired
	UsrAthMapper usrAthMapper;
	
	@Autowired
	UsrGrpMapper usrGrpMapper;
	
	@Autowired
	LoginHstServiceImpl loginHstService;

	@Override
	public List<ResAdminUsrAth> getAdminUsrAthLs(String athNm) {
		return usrAthMapper.getAdminUsrAthLs(athNm);
	}

	@Override
	public int postAdminUsrAthLs(List<ReqAdminUsrAth> reqParam) {
		
		for (ReqAdminUsrAth adminUsrAth : reqParam) {
			if ( adminUsrAth.getdFlg() != null && adminUsrAth.getdFlg() == 1 ) {
				Integer authCd = adminUsrAth.getAthCd();
				List<ResAdminUsrGrp> usrGrpList = usrGrpMapper.getAdminUsrGrpLsByAthCd(authCd);
				if ( usrGrpList.size() > 0 ) {
					return -1;
				}
			}
		}
		
		for (ReqAdminUsrAth adminUsrAth : reqParam) {
			usrAthMapper.postAdminUsrAthLs(adminUsrAth.toEntity());
		}
		
		return 1;
	}


	/**
	 */
	@Override
	public void checkJwtSession() {
		MemDB.JWT.selectAll().stream().forEach(jwtSession->{
			String token = jwtSession.getToken();
			if ( !JwtUtil.verify(token) ) {
				MemDB.JWT.delete(token);
				
				if ( jwtSession.isAccessToken() ) {
					loginHstService.postAdminLogoutHstByToken(token, "3");	// User-AccessToken logout
				} else {
					loginHstService.postAdminLogoutHstByToken(token, "4");	// User-RefreshToken logout
				}
			}
		});
	}
	
	@Override
	public void removeJwtSessionByUsername(String username) {
		if ( username != null ) {
			
			MemDB.JWT.selectAll().stream()
				.filter(jwtSession->{
					return username.equals(jwtSession.getUserDetails().getUsername());
				})
				.forEach(jwtSession->{
					MemDB.JWT.delete(jwtSession.getToken());
				});
		}
	}
	
	@Override
	public void removeJwtSessionByAccessToken(String accessToken) {
		if ( accessToken != null ) {
			
			MemDB.JWT.selectAll().stream()
				.filter(jwtSession->{
					return accessToken.equals(jwtSession.getAccessToken());
				})
				.forEach(jwtSession->{
					MemDB.JWT.delete(jwtSession.getToken());
				});
		}
	}
	
	@Override
	public void addJwtSession() {
	}
	

}
