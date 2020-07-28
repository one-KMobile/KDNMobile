package com.kdn.mtps.mobile.net.api.bean;

import java.util.List;

public class TowerList {
	
	public List<TowerInfo> tracksList;
	public String code;			
	
	public static class TowerInfo {
		public String TRANS_TOWER_IDX ;
		public String TRANS_TOWER_TYPE_CODE;
	}
	
}
