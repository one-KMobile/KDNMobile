package com.kdn.mtps.mobile.db.bean;


public class MyLocationLog {
	public int _id;
	public String latitude;
	public String longitude;
	public long reg_date;
	
	public static class COLS {
		public static final String _id = "_id";
		public static final String REG_DATE = "REG_DATE";
		public static final String LATITUDE = "LATITUDE";
		public static final String LONGITUDE = "LONGITUDE";
	}
}
