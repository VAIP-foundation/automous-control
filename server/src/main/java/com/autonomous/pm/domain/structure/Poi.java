package com.autonomous.pm.domain.structure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Poi { // Poi 정보
	
	private String term;	// T1, T2	terminal code.
	private String grp;	// T1-A, T1-B, T2-C	권역CODE.(group)
	private String grpnm;	// 권역 name [1터미널입국장, 2터미널출국장]
	private String poicd;	// POI Code. 모든 POI 마다 rule에 따라 할당해야 한다. Gate: {terminal} + {g} + 'G' + {FIMS gate number} Info: {terminal} + {g} + 'I' + {info number}

	private String poinm;	// Poi Name. [Gate102,검역소1,...]
	private Integer ty;	// POI type:1=Gate,2=Info,3=검역소,4=충전소
//	private String ty;	// POI type:1=Gate,2=Info,3=검역소,4=충전소
	private Double lat;	// 위도(WGS84)
	private Double lng;	// 경도(WGS84)
	
}
