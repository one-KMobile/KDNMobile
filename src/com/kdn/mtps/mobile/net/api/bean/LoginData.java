package com.kdn.mtps.mobile.net.api.bean;

public class LoginData {
	
	public SessionData session;
	public String code;			
	
	public static class SessionData {
		public String user_pwd;
		public String user_email;	
		public String comp_name;
		public String fst_bizplc_cd_nm;			
		public String fst_bizplc_cd;	
		public String user_name;		
		public String biz_no;		
		public String user_hp;	
		public String scd_bizplc_cd_nm;		
		public String scd_bizplc_cd;		
		public String session_key;	
		public String user_id;	
		public String mac_address;
		public String contract;
		public String appVersion;
		public String appUrl;
	}
}
