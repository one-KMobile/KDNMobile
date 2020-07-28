package com.kdn.mtps.mobile.db.bean;


public class InputBTLog {
	public int idx;
	public String master_idx;
	public String weather;
	public String worker_cnt;
	public String claim_content;
	public String check_result;
	public String etc;
//	public String ins_result_code;
//	public String check_result_code;
//	public String eqp_type_code;
//	public String detail_item_code;
	
	
	public static class COLS {
		public static final String IDX = "IDX";
		public static final String MASTER_IDX = "MASTER_IDX";
		public static final String WEATHER = "WEATHER";
		public static final String WORKER_CNT = "WORKER_CNT";
		public static final String CLAIM_CONTENT = "CLAIM_CONTENT";
		public static final String CHECK_RESULT = "CHECK_RESULT";
		public static final String ETC = "ETC";
//		public static final String INS_RESULT_CODE = "INS_RESULT_CODE";
//		public static final String CHECK_RESULT_CODE = "CHECK_RESULT_CODE";
//		public static final String EQP_TYPE_CODE = "EQP_TYPE_CODE";
//		public static final String DETAIL_ITEM_CODE = "DETAIL_ITEM_CODE";
//		public static final String SPT_MGT_YN = "SPT_MGT_YN";
	}
}
