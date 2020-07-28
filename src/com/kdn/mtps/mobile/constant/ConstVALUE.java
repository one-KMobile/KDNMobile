package com.kdn.mtps.mobile.constant;

import android.os.Environment;


public class ConstVALUE {
	public static final String APP_CLIENT_VERSION = "1.0";
	public static final boolean DEBUG = true;

	public static final int LISTVIEW_LIMIT = 10;
	
	public static final String MAP_API_KEY = "fb6550e48a84d0bebdebfe34ad96db74";
	
	public static final int DELETE_DAY = 7;
	
	public static final int MODE_MAPVIEW_DETAIL = 100;
	public static final int MODE_MAPVIEW_ALL = 200;
	
	public static final int CALL_TYPE_DEFAULT = 300;
	public static final int CALL_TYPE_RESOURCE = CALL_TYPE_DEFAULT + 1;
	public static final int CALL_TYPE_DATASYNC = CALL_TYPE_DEFAULT + 2;
	public static final int CALL_TYPE_SEND_INSPECT_RESULT = CALL_TYPE_DEFAULT + 3;
	
	/** Input Activity Type **/
	public static final String ACTIVITY_INPUT_TYPE = "input_type";
	public static final int ACTIVITY_INPUT_TYPE_DEFAULT = 10100;
	public static final int ACTIVITY_INPUT_TYPE_ADD = ACTIVITY_INPUT_TYPE_DEFAULT + 1;
	public static final int ACTIVITY_INPUT_TYPE_EDIT = ACTIVITY_INPUT_TYPE_DEFAULT + 2;
	
	public static final int REQUEST_CODE_INSPECT_SEARCH = 1000; 
	
	public static final String CODE_TYPE_INS_TYPE = "INS_TYPE";
	public static final String CODE_TYPE_NOTICE = "NOTICETYPE";
	public static final String CODE_TYPE_WEATHER = "WETHR_SECD";
	public static final String CODE_TYPE_HK_TYPE = "SRCELCT_KND";
	public static final String CODE_TYPE_HK_CATEGORY = "AIRLAMP_CS";
	public static final String CODE_TYPE_TRACKS = "TRACKS";
	public static final String CODE_TYPE_EQPS = "EQPS";
	public static final String CODE_TYPE_GOOD_SECD = "GOOD_SECD";
	public static final String CODE_TYPE_FLIGHT_LMP = "FLIGHT_LMP_KND";
	
	
	/** Inspect Type No**/
	public static final String CODE_NO_INSPECT_BT = "001";
	public static final String CODE_NO_INSPECT_JS = "027";
	public static final String CODE_NO_INSPECT_JJ = "028";
	public static final String CODE_NO_INSPECT_KB = "021";
	public static final String CODE_NO_INSPECT_HK = "025";
	public static final String CODE_NO_INSPECT_HJ = "024";
	public static final String CODE_NO_INSPECT_BR = "029";
	public static final String CODE_NO_INSPECT_YB = "003";
	public static final String CODE_NO_INSPECT_JP = "026";
	
	/** 파일경로를 URI로 변경할때 앞에 붙일 prefix */
	public static final String MEDIA_FILEPATH_PREFIX = "file://";
	
	/** api 서버 **/
	//public static String PREFIX_API_SERVER = "http://kdn.testbed.kr:8989/";
	public static String PREFIX_API_SERVER = "http://172.30.1.35:8080/kdnweb/";
//	public static String PREFIX_API_SERVER = "http://192.168.0.12:8080/";
	public static final String NOTICE_LIST_URL = PREFIX_API_SERVER + "mobileWeb/noticeList.do";
	public static final String NOTICE_DETAIL_URL = NOTICE_LIST_URL + "?board_idx=";
//	public static final String PREFIX_API_SERVER_HD = "http://192.168.0.12:8080/"; // 현도	
//	public static final String PREFIX_API_SERVER = "http://192.168.43.62:8080/";

	public static final String PATH_SDCARD_KDN_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/KDN";
	public static final String PATH_SDCARD_PICTURE = PATH_SDCARD_KDN_ROOT + "/photo";
	
	
}
