package com.kdn.mtps.mobile.net.api.bean;

import java.util.List;

public class TracksList {
	
	public List<TracksInfo> tracksList;
	public String code;			
	
	public static class TracksInfo {
		public String FNCT_LC_NO ;
		public String FNCT_LC_DTLS;
	}
}
