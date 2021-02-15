package com.autonomous.pm.domain.structure.push;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;

import com.autonomous.pm.domain.structure.Battery;
import com.autonomous.pm.domain.structure.Charging;
import com.autonomous.pm.domain.structure.Obs;
import com.autonomous.pm.domain.structure.SensorInfo;
import com.autonomous.pm.util.MyUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
//public class SensorInfoPush extends SensorInfo {	// UI PUSH를 위한 구조체. 일부 항목을 추가하였다.
public class SensorInfoPush {	// UI PUSH를 위한 구조체. 일부 항목을 추가하였다.
	
	private Long idV;	// 차량ID
	private String vrn;	// 차량번호
	private BatteryPush bat;	// 배터리
	
	private Date ts;		// 현재시각(UTC).yyyy-mm-ddThh:mi:ssZ ["2011-08-12T20:17:46.384Z"]
	
	private Charging chg;	// 충전 중일때만 전송하고 그외에는 null
//	private Battery bat;	// 배터리 상태
	private Integer dr_s_abl;	// 운행가능시간.초단위(분단위갱신)
	private Integer dr_s_chg;	// 충전 후 경과 시간.초단위(분단위갱신) '완충 후 운행 시간'
	private Boolean[] seat;	// 좌석단위 탑승 여부 표현. 크기가 4개이 boolean array 로 표현하여 각 좌석이 occupied 인지 표기. true=착석. false=빈좌석. [1,3 번 좌석에 착석. [true,false,true,false] ]
	private Boolean[] load;	// 수하물탑재여부 true=화물. false=빈. [예:1 번 slot에 화물 [true,false] ]
	private Obs[] obs;		// 매 실시간으로 인지되는 장애물 정보. 장애물 정보의 해지는 정의하지 않으며. 전송하지 않으면 없어진것으로 간주.
	
	@JsonIgnore
	public byte getSeatToByte() {
		return MyUtil.booleansToByte(this.seat);
	}
	@JsonIgnore
	public byte getLoadToByte() {
		return MyUtil.booleansToByte(this.load);
	}
	
	public SensorInfoPush() {
	}
	
	public SensorInfoPush(Long idV, String vrn, SensorInfo sensorInfo, Integer vhclDrivingAbleSec) {
		this.setIdV(idV);
		this.setVrn(vrn);
		
		BatteryPush bp = new BatteryPush(sensorInfo.getBat());
		
//		log.debug("===================================");
//		log.debug("==getDr_s_abl() " + sensorInfo.getDr_s_abl() + "==");
//		log.debug("==vhclDrivingAbleSec " + vhclDrivingAbleSec + "==");
//		log.debug("===================================");
		if ( sensorInfo.getDr_s_abl() == null ) {
			bp.setRecharge(true);
		} else if ( sensorInfo.getDr_s_abl() < vhclDrivingAbleSec ) {	// 운행 가능시간이 기준시간보다 작으면 배터리충전필요여부:true
			bp.setRecharge(true);
		} else {
			bp.setRecharge(false);
		}
		this.setBat(bp);
		BeanUtils.copyProperties(sensorInfo, this);
	}
}