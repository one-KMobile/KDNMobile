package com.kdn.mtps.mobile.net.api.bean;

import java.util.List;

public class PatrolmansList {
	
	public List<PatrolMansInfo> patrolmans;
	public String code;			
	
	public static class PatrolMansInfo {
		public String USER_ID ;
		public String USER_NAME;
	}
}
