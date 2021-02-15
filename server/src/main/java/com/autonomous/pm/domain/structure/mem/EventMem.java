package com.autonomous.pm.domain.structure.mem;

import com.autonomous.pm.domain.structure.Event;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.TEvtHst;
import com.autonomous.pm.model.Do.VEvtHst;
import com.autonomous.pm.util.DateUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventMem extends CommonMem {
	
	public EventMem(Long idV, Event info) {
		this.setIdV(idV);
		this.setData(info);
	}
	
	public EventMem() {
	}
	
	@Override
	public Event getData() {
		return (Event) data;
	}
	
	/**
	 * @param raw
	 */
	public void setDataByEntity(TEvtHst raw) {
		Event info = new Event();

		info.setTs(DateUtil.convertStringToUtcDate(raw.getDts(), "YYYYMMDDhhmiss"));
		info.setEvtcd(raw.getEvtCd());
		info.setFailcd(raw.getFailCd());
		
		this.setIdV(raw.getIdV());
		this.setData(info);
	}
	
	public TEvtHst toEntity() {
		TEvtHst entity = new TEvtHst();
		Event info = this.getData();
		entity.setSeq(null);
		entity.setIdV(this.getIdV());
		entity.setDts(DateUtil.getUtcDatetimeByFormat(info.getTs(), "YYYYMMDDhhmiss"));
		entity.setEvtCd(info.getEvtcd());
		entity.setFailCd(info.getFailcd());
		entity.setcDt(null);
		
		return entity;
	}
	
	@Override
	public String toString() {
		return "EventMem [" + (idV != null ? "idV=" + idV + ", " : "") + "dirty=" + dirty + ", "
				+ (data != null ? "data=" + getData() : "") + "]";
	}
	
	
}