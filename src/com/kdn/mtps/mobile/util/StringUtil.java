package com.kdn.mtps.mobile.util;


public class StringUtil {
	public static String printDate(String date) {
		if (date == null)
			return "";
		
		if (date.length() < 6)
			return "";
		
		return date.substring(0,4) + "." + date.substring(4,6);
	}
	
	public static String removeDot(String date) {
		return date.replace(".", "");
	}
	
	public static int getIndex(String[] strArray, String value) {
		return getIndex(strArray, value, 0);
	}
	
	public static int getIndex(String[] strArray, String value, int defaultValue) {
		if (value == null || "".equals(value))
			return 0;
		
		for (int idx=0; idx<strArray.length; idx++) {
			if (value.equals(strArray[idx]))
				return idx;
		}
		
		return 0;
	}
}
