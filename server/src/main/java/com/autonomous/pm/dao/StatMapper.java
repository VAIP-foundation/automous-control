package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.model.Do.TVhcl;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.model.Dto.ReqAdminVhcl;
import com.autonomous.pm.model.Dto.ReqFromToDay;
import com.autonomous.pm.model.Dto.ReqFromToMonth;
import com.autonomous.pm.model.Dto.ResAdminVhcl;
import com.autonomous.pm.model.Dto.ResCnflctCnt;
import com.autonomous.pm.model.Dto.ResDrKmCnt;
import com.autonomous.pm.model.Dto.ResDrTmsCnt;
import com.autonomous.pm.model.Dto.ResFailCnt;
import com.autonomous.pm.model.Dto.ResMStopCnt;
import com.autonomous.pm.model.Dto.ResMeStopCnt;
import com.autonomous.pm.model.Dto.ResPeoplCnt;
import com.autonomous.pm.model.Dto.ResRStopCnt;
import com.autonomous.pm.model.Dto.ResStatSheet;
import com.autonomous.pm.model.Dto.ResStatTotal;
import com.autonomous.pm.model.Dto.ResStatTrip;
import com.autonomous.pm.model.Dto.ResTotal;
import com.autonomous.pm.model.Dto.ResTripCnt;
import com.autonomous.pm.model.Dto.RestStTrip;

public interface StatMapper {
	
	List<ResDrKmCnt> getStatDistDailyLs(ReqFromToDay reqParam);
	List<ResDrKmCnt> getStatDistMonthlyLs(ReqFromToMonth reqParam);

	List<ResDrTmsCnt> getStatDrvtmDailyLs(ReqFromToDay reqParam);
	List<ResDrTmsCnt> getStatDrvtmMonthlyLs(ReqFromToMonth reqParam);

	List<ResPeoplCnt> getStatUsersDailyLs(ReqFromToDay reqParam);
	List<ResPeoplCnt> getStatUsersMonthlyLs(ReqFromToMonth reqParam);

	List<ResTripCnt> getStatTripcntDailyLs(ReqFromToDay reqParam);
	List<ResTripCnt> getStatTripcntMonthlyLs(ReqFromToMonth reqParam);

	List<ResRStopCnt> getStatRstopcntDailyLs(ReqFromToDay reqParam);
	List<ResRStopCnt> getStatRstopcntMonthlyLs(ReqFromToMonth reqParam);
	
	List<ResMStopCnt> getStatMStopcntDailyLs(ReqFromToDay reqParam);
	List<ResMStopCnt> getStatMStopcntMonthlyLs(ReqFromToMonth reqParam);
	
	List<ResMeStopCnt> getStatMeStopcntDailyLs(ReqFromToDay reqParam);
	List<ResMeStopCnt> getStatMeStopcntMonthlyLs(ReqFromToMonth reqParam);

	List<ResCnflctCnt> getStatCrshDailyLs(ReqFromToDay reqParam);
	List<ResCnflctCnt> getStatCrshMonthlyLs(ReqFromToMonth reqParam);

	List<ResFailCnt> getStatFailDailyLs(ReqFromToDay reqParam);
	List<ResFailCnt> getStatFailMonthlyLs(ReqFromToMonth reqParam);
	
	List<ResTotal> getStatTotalDailyLs(ReqFromToDay reqParam);
	List<ResTotal> getStatTotalMonthlyLs(ReqFromToMonth reqParam);
	

	List<RestStTrip> getStatExcelTotalDailyLs(ReqFromToDay reqParam);
	List<RestStTrip> getStatExcelTotalMonthlyLs(ReqFromToMonth reqParam);
	
	List<RestStTrip> getStatExcelTotalSumDailyLs(ReqFromToDay reqParam);
	List<RestStTrip> getStatExcelTotalSumMonthlyLs(ReqFromToMonth reqParam);
	
}