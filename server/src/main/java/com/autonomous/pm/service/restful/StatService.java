package com.autonomous.pm.service.restful;

import java.util.HashMap;
import java.util.List;

import com.autonomous.pm.domain.common.SearchParam;
import com.autonomous.pm.model.Do.VEvtHst;
import com.autonomous.pm.model.Dto.ReqFromToDay;
import com.autonomous.pm.model.Dto.ReqFromToMonth;
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
import com.autonomous.pm.model.Dto.ResTripCnt;
import com.autonomous.pm.model.Dto.RestStTrip;

public interface StatService {
	
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

	List<ResMeStopCnt> getStatMeStopCntDailyLs(ReqFromToDay reqParam);
	List<ResMeStopCnt> getStatMeStopCntMonthlyLs(ReqFromToMonth reqParam);
	List<ResMStopCnt> getStatMStopCntDailyLs(ReqFromToDay reqParam);
	List<ResMStopCnt> getStatMStopCntMonthlyLs(ReqFromToMonth reqParam);
	
	
	List<ResCnflctCnt> getStatCrshDailyLs(ReqFromToDay reqParam);
	List<ResCnflctCnt> getStatCrshMonthlyLs(ReqFromToMonth reqParam);

	List<ResFailCnt> getStatFailDailyLs(ReqFromToDay reqParam);
	List<ResFailCnt> getStatFailMonthlyLs(ReqFromToMonth reqParam);


	List<ResStatSheet> getStatExcelDailyLs(ReqFromToDay reqParam);
	List<ResStatSheet> getStatExcelMonthlyLs(ReqFromToMonth reqParam);
	
	List<RestStTrip> getStatExcelTotalDailyLs(ReqFromToDay reqParam);
	List<RestStTrip> getStatExcelTotalMonthlyLs(ReqFromToMonth reqParam);

	List<RestStTrip> getStatExcelTotalSumDailyLs(ReqFromToDay reqParam);
	List<RestStTrip> getStatExcelTotalSumMonthlyLs(ReqFromToMonth reqParam);
	
	
	ResStatSheet getStatExcelPoiLs(List<RestStTrip> totalData, String term);
	
	List<ResStatSheet> getStatRawEventLs(ReqFromToDay reqParam);
	List<ResStatSheet> getStatRawTripLs(ReqFromToDay reqParam);
	List<ResStatSheet> getStatRawCallLs(ReqFromToDay reqParam);
	
}
