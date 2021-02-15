package com.autonomous.pm.domain.structure.mem;

import java.io.IOException;

import com.autonomous.pm.domain.structure.Battery;
import com.autonomous.pm.domain.structure.Charging;
import com.autonomous.pm.domain.structure.Obs;
import com.autonomous.pm.domain.structure.SensorInfo;
import com.autonomous.pm.model.Do.TSnsrRawYymm;
import com.autonomous.pm.util.DateUtil;
import com.autonomous.pm.util.MyUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class SensorInfoMem extends CommonMem {
	
	public SensorInfoMem(Long idV, SensorInfo info) {
		this.setIdV(idV);
		this.setData(info);
	}
	
	public SensorInfoMem() {
	}
	
	@Override
	public SensorInfo getData() {
		return (SensorInfo) data;
	}
	
	/**
	 * @param raw
	 */
	public void setDataByEntity(TSnsrRawYymm raw) {
		
		SensorInfo info = new SensorInfo();
		
		Battery bat = new Battery();
		bat.setVolt(raw.getBatV());
		bat.setAmp(raw.getBatA());
		bat.setCap(raw.getBatMaxA());
		bat.setRem(raw.getBatCurA());
		info.setBat(bat);
		
		Charging chg = new Charging();
		chg.setChg_tm(raw.getChgElapsS());
		chg.setRem_tm(raw.getChgFinS());
		
		info.setChg(chg);
		
		info.setDr_s_abl(raw.getDrAblS());
		info.setDr_s_chg(raw.getDrChgS());
		
		info.setLoad(MyUtil.byteToBooleans(raw.getCargo()));
		info.setSeat(MyUtil.byteToBooleans(raw.getSeat()));
		
		try {
			String payload = raw.getObs();
			if ( payload != null ) {
				ObjectMapper mapper = new ObjectMapper();
				Obs[] obses = mapper.readValue(raw.getObs(), new TypeReference<Obs[]>(){});
				info.setObs(obses);
			}
		} catch (IOException e) {
			log.error(e.toString());
		}
		info.setTs(DateUtil.convertStringToUtcDate(raw.getDts(), "YYYYMMDDhhmiss"));
		
		
		this.setIdV(raw.getIdV());
		this.setData(info);

	}
	

	public TSnsrRawYymm toEntity() {
		TSnsrRawYymm entity = new TSnsrRawYymm();
		SensorInfo info = this.getData();

		entity.setIdSnsr(null);
		entity.setIdV(this.getIdV());
		entity.setDts(DateUtil.getUtcDatetimeByFormat(info.getTs(), "YYYYMMDDhhmiss"));

		// 충전 중 여부. 충전 중일때만 전송한다.1=충전중,0=NO충전중
		if (info.getChg() != null) {
			entity.setChgStts((byte) 1);
			// charge elapse time. 충전 시간(sec)-(분단위갱신)
			entity.setChgElapsS(info.getChg().getChg_tm());
			entity.setChgFinS(info.getChg().getRem_tm());
		} else {
			entity.setChgStts((byte) 0);
		}

		if (info.getBat() != null) {
			entity.setBatCurA(info.getBat().getRem());
			entity.setBatMaxA(info.getBat().getCap());
			entity.setBatV(info.getBat().getVolt());
			entity.setBatA(info.getBat().getAmp());
		}
		entity.setDrAblS(info.getDr_s_abl());
		entity.setDrChgS(info.getDr_s_chg());
		if (info.getSeat() != null) {
			entity.setSeat(info.getSeatToByte());
		}
		if (info.getLoad() != null) {
			entity.setCargo(info.getLoadToByte());
		}
		entity.setcDt(null);

		if (info.getObs() != null) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				String obsStr;
				obsStr = objectMapper.writeValueAsString(info.getObs());
				entity.setObs(obsStr);
			} catch (JsonProcessingException e) {
				log.error(e.toString());
			}
		}

		return entity;
	}

	@Override
	public String toString() {
		return "SensorInfoMem [" + (idV != null ? "idV=" + idV + ", " : "") + "dirty=" + dirty + ", "
				+ (data != null ? "data=" + getData() : "") + "]";
	}
	
	
}