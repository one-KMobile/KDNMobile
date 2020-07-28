package com.kdn.mtps.mobile.net.api.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeList {
	
//	public static Map<String, String> CODE_INPUT_BT_WEATHER = new HashMap<String, String>();
	
	public List<CodeDef> apiCodeInfoList;
	public String code;			
	
	public static class CodeDef {
		public String CODE_IDX;
		public String GROUP_CODE_ID;
		public String CODE_ID;
		public String CODE_NAME;
	}

}
