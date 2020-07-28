package com.kdn.mtps.mobile.db.bean;


public class CameraLog {
	public int idx;
	public String master_idx;
	public String img_path;
	public boolean isChecked;
	public String subject="";
	public String content="";
	public String ins_type="";
	
	public static class COLS {
		public static final String IDX = "IDX";
		public static final String MASTER_IDX = "MASTER_IDX";
		public static final String IMAGE_PATH = "IMAGE_PATH";
		public static final String SUBJECT = "SUBJECT";
		public static final String CONTENT = "CONTENT";
		public static final String INS_TYPE = "INS_TYPE";
	}
}
