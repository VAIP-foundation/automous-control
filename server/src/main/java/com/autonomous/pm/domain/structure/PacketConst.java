package com.autonomous.pm.domain.structure;

import com.autonomous.pm.util.MyUtil;

public class PacketConst {

	public static final byte DEVICE_TYPE_SERVER = (byte) 0xCE; 		
	public static final byte DEVICE_TYPE_V2X_SERVER = (byte) 0xCF;  
	public static final byte DEVICE_TYPE_ADMIN_DEVICE = (byte) 0xD1;
	public static final byte DEVICE_TYPE_OBU_DEVICE = (byte) 0xD2; 	
	public static final byte DEVICE_TYPE_RSU = (byte) 0xD3; 		
	public static final byte DEVICE_TYPE_TSIGNAL = (byte) 0xD4; 	

	public static final String STR_DEVICE_ID = "010203";

	public static final byte REGISTRATION_STATUS_OK = (byte) 0x01;
	public static final byte REGISTRATION_STATUS_FAIL = (byte) 0x02;/

	public static final int T_TYPE_V2X_OBD = 11;
	public static final int T_TYPE_V2X_CLIENT = 12;
	public static final int T_TYPE_HD_MAP = 14;
	public static final int T_TYPE_IOT = 15;
	public static final int T_TYPE_TRE = 21;
	public static final int T_TYPE_PEDESTRIAN = 31;

	public static final int ESC_COMMAND_STOP = 1;
	public static final int ESC_COMMAND_RESUME = 2;

	/**
	 */
	/**
	 */
	public static final int MISSIONCODE_DEDUCT = 0;
	/**
	 */
	public static final int MISSIONCODE_CALL = 1;
	/**
	 */
	public static final int MISSIONCODE_PARKING = 2;
	/**
	 */
	public static final int MISSIONCODE_PEDES_EVENT = 3;
	public static final int MISSIONCODE_ETC = 10;

	/**
	 */
	/**
	 */
	public static final int MISSIONSTAT_DEDUCT = 0;
	/**
	 */
	public static final int MISSIONSTAT_START = 1;
	/**
	 */
	public static final int MISSIONSTAT_FIN = 2;
	/**
	 */
	public static final int MISSIONSTAT_SCORE = 3;
	/**
	 */
	public static final int MISSIONSTAT_FAIL = 4;

	/**
	 */
	/**
	 */
	public static final int DEDUCTCAUSE_SIGNAL_VIOLATION = 1;
	/**
	 */
	public static final int DEDUCTCAUSE_OVER_SPEED = 2;
	/**
	 */
	public static final int DEDUCTCAUSE_WRONG_WAY = 3;

	/**
	 */
	public static final int MPTYPE_MISSION = 1;
	public static final int MPTYPE_PENALTY = 2;

	/**
	 */
	public static final int ESC_STATUS_RCVD_CMD = 1;
	/**
	 */
	public static final int ESC_STATUS_COMMAND_REQ = 2;
	/**
	 */
	public static final int ESC_STATUS_COMMAND_RES = 3;
	/**
	 */
	public static final int ESC_STATUS_STOPPING = 4;
	/**
	 */
	public static final int ESC_STATUS_STOP = 5;

	/**
	 */
	public static final int ESS_TYPE_ADMIN = 1;
	/**
	 */
	public static final int ESS_TYPE_RC = 2;
	/**
	 */
	public static final int ESS_TYPE_CTRL = 3;

	/**
	 */
	public static final int ESS_STATUS_STOPPING = 1;
	/**
	 */
	public static final int ESS_STATUS_STOP = 2;
	/**
	 */
	public static final int ESS_STATUS_RC = 3;

	/**
	 */
	/**
	 */
	public static final int ESC_SRC_UI = 1;
	/**
	 */
	public static final int ESC_SRC_ADMIN_TERMINAL = 2;
	/**
	 */
	public static final int ESC_SRC_RC = 3;

	/**
	 */
	public static final int DRVSTAT_STOP = 1;
	/**
	 */
	public static final int DRVSTAT_DRIVING = 2;

	/**
	 */
	/**
	 */
	public static final int ENG_ON = 1;
	/**
	 */
	public static final int ENG_OFF = 0;

	/**
	 */
	/**
	 */
	public static final int ESTOP_ESC_CMD = 1;
	/**
	 */
	public static final int ESTOP_ESC_SND = 2;
	/**
	 */
	public static final int ESTOP_ESC_RES = 3;
	/**
	 */
	public static final int ESTOP_STOPPING = 4;
	/**
	 */
	public static final int ESTOP_STOP = 5;

	/**
	 * 
	 * @param dType
	 * @return
	 */

	/**
	 */
	/**
	 */
	public static final int BOARD_GETIN = 1;
	/**
	 */
	public static final int BOARD_GETOUT = 2;

	public static String getDeviceTypeName(int dType) {
		String name = "";
		
		return name;
	}

	public static String nameOfMission(String mcode) {
		return nameOfMission(Integer.parseInt(mcode));
	}

	public static String nameOfMission(int mcode) {
		switch (mcode) {

		}
		return "Unknown";
	}

	public static String nameOfMissionStatus(int code) {
		String ret = "Unknown";


		return ret;
	}

	public static String nameOfEstopCmd(int cmd) {
		switch (cmd) {
		case PacketConst.ESC_COMMAND_STOP: // 1
			return "ESTOP_STOP";
		case PacketConst.ESC_COMMAND_RESUME: // 2
			return "ESTOP_RESUME";
		}
		return "Unknown";
	}

	/**
	 * 
	 * @param stat
	 * @return
	 */
	public static String nameOfEstopStat(int stat) {

		return "Unknown";
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static String nameOfEstopCmder(int code) {

		return "Unknown";
	}

	/**
	 */
	public static String nameOfEssStatus(int code) {
		return "Unknown";
	}


	/**
	 */
	public static String nameOfBoardStatus(int code) {
	}
	
	/**
	 */
	public static String nameOfCause(int code) {
		String ret = "Unknown";
		switch(code) {
		}
		return ret;
	}

}
