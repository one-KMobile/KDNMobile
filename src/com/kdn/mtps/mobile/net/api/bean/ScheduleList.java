package com.kdn.mtps.mobile.net.api.bean;

import java.util.List;

import com.kdn.mtps.mobile.inspect.InspectInfo;

public class ScheduleList {
	
//	public static Map<String, String> CODE_INPUT_BT_WEATHER = new HashMap<String, String>();
	
	public List<ScheduleInfo> schedule;
	public String code;
	public String planno;
	
	public static class ScheduleInfo {
		public String NM;
		public String FNCT_LC_NO;
		public String FNCT_LC_DTLS;
		public String CYCLE_YM;
		public String INS_TY_CD ;
		public int INS_TY_CD_COUNT ;
		public String INS_PLAN_NO;
		public String TOWER_IDX;
		public String FST_BIZPLC_CD_NM;
		public String SCD_BIZPLC_CD_NM;
		public String LATITUDE;
		public String LONGITUDE;
		public String EQP_NM;
		public String EQP_NO;
		public String UNITY_INS_NO;
		public String INS_SN;
		public String ADDRESS;
	}
	
}
