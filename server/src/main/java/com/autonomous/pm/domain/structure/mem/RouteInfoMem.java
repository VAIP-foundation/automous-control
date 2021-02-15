package com.autonomous.pm.domain.structure.mem;

import java.io.IOException;

import com.autonomous.pm.domain.structure.Location;
import com.autonomous.pm.domain.structure.RouteInfo;
import com.autonomous.pm.model.Dto.RouteHst;
import com.autonomous.pm.util.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class RouteInfoMem extends CommonMem {
	
	public RouteInfoMem(Long idV, RouteInfo info) {
		this.setIdV(idV);
		this.setData(info);
	}
	
	public RouteInfoMem() {
	}
	
	@Override
	public RouteInfo getData() {
		return (RouteInfo) data;
	}
	
	/**
	 */
	public void setDataByEntity(RouteHst raw) {
		RouteInfo info = new RouteInfo();
		
		info.setTs(DateUtil.convertStringToUtcDate(raw.getDts(), "YYYYMMDDhhmiss"));
		log.debug("raw>>"+raw);
		Location from = new Location(raw.getFromLat(), raw.getFromLng());
		info.setFrom(from);
		
		Location to = new Location(raw.getToLat(), raw.getToLng());
		info.setTo(to);
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			Location[] points = mapper.readValue(raw.getPoints(), new TypeReference<Location[]>(){});
			info.setPoints(points) ;
		} catch (IOException e) {
			log.error(e.toString());
		}
		
		this.setIdV(raw.getIdV());
		this.setData(info);
	}
	
	public RouteHst toEntity() {
		RouteHst entity = new RouteHst();
		RouteInfo info = this.getData();
		
		entity.setIdRoute(null);
		entity.setIdV(this.getIdV());
		
		entity.setFromLat(info.getFrom().getLat());
		entity.setFromLng(info.getFrom().getLng());
		entity.setToLat(info.getTo().getLat());
		entity.setToLng(info.getTo().getLng());
		
		if( info.getPoints() != null ) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				String points;
				points = objectMapper.writeValueAsString(info.getPoints());
				entity.setPoints(points);
			} catch (JsonProcessingException e) {
				log.error(e.toString());
			}
		}
		entity.setDts(DateUtil.getUtcDatetimeByFormat(info.getTs(), "YYYYMMDDhhmiss"));
		entity.setcDt(null);
		
		return entity;
	}

	@Override
	public String toString() {
		return "WrapRouteInfo [" + (idV != null ? "idV=" + idV + ", " : "") + "dirty=" + dirty + ", "
				+ (data != null ? "data=" + getData() : "") + "]";
	}
	
	
}