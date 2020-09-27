package com.kdn.mtps.mobile.db;

import com.kdn.mtps.mobile.input.BRInfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "kdnmobile.db";
	public static final int DB_VERSION = 3; 

	static DBHelper instance;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public static SQLiteDatabase getInstance(Context ctx) {
		if (instance == null)
			instance = new DBHelper(ctx);
		return instance.getWritableDatabase();
	}
	
	public static SQLiteDatabase getReadableInstance(Context ctx)
	{
		if(instance == null)
			instance = new DBHelper(ctx);
		return instance.getReadableDatabase();
	}

			
	private static final String MYLOCATION_LOG = "CREATE TABLE MYLOCATION_LOG (" 
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "REG_DATE TIMESTAMP,"
			+ "LATITUDE VARCHAR2(30),"
			+ "LONGITUDE VARCHAR2(30));";

	private static final String INPUT_BT = "CREATE TABLE INPUT_BT ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "MASTER_IDX VARCHAR2(20),"
			+ "WEATHER VARCHAR2(10)," 
			+ "WORKER_CNT VARCHAR2(10),"
			+ "CLAIM_CONTENT VARCHAR2(10)," 
			+ "CHECK_RESULT CHAR(100)," 
			+ "ETC VARCHAR2(100));";
	
	private static final String INPUT_JS = "CREATE TABLE INPUT_JS ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "MASTER_IDX VARCHAR2(20),"
			+ "WEATHER VARCHAR2(10),"
			+ "AREA_TEMP VARCHAR2(10),"
			+ "CIRCUIT_NO VARCHAR2(30),"
			+ "CIRCUIT_NAME VARCHAR2(50),"
			+ "CURRENT_LOAD VARCHAR2(10),"
			+ "CONDUCTOR_CNT VARCHAR2(10),"
			+ "LOCATION VARCHAR2(10),"
			+ "C1_JS VARCHAR2(10),"
			+ "C1_JSJ VARCHAR2(10),"
			+ "C1_YB_RESULT VARCHAR2(10),"
			+ "C1_POWER_NO VARCHAR2(10),"
			+ "C2_JS VARCHAR2(10),"
			+ "C2_JSJ VARCHAR2(10),"
			+ "C2_YB_RESULT VARCHAR2(10),"
			+ "C2_POWER_NO VARCHAR2(10),"
			+ "C3_JS VARCHAR2(10),"
			+ "C3_JSJ VARCHAR2(10),"
			+ "C3_YB_RESULT VARCHAR2(10),"
			+ "C3_POWER_NO VARCHAR2(10))";
	
	private static final String INPUT_JP = "CREATE TABLE INPUT_JP ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "MASTER_IDX VARCHAR2(20),"
			+ "WEATHER VARCHAR2(10),"
			+ "AREA_TEMP VARCHAR2(10),"
			+ "CIRCUIT_NO VARCHAR2(30),"
			+ "CIRCUIT_NAME VARCHAR2(50),"
			+ "CURRENT_LOAD VARCHAR2(10),"
			+ "CONDUCTOR_CNT VARCHAR2(10),"
			+ "LOCATION VARCHAR2(10),"
			+ "C1_JS VARCHAR2(10),"
			+ "C1_JSJ VARCHAR2(10),"
			+ "C1_YB_RESULT VARCHAR2(10),"
			+ "C1_POWER_NO VARCHAR2(10),"
			+ "C2_JS VARCHAR2(10),"
			+ "C2_JSJ VARCHAR2(10),"
			+ "C2_YB_RESULT VARCHAR2(10),"
			+ "C2_POWER_NO VARCHAR2(10),"
			+ "C3_JS VARCHAR2(10),"
			+ "C3_JSJ VARCHAR2(10),"
			+ "C3_YB_RESULT VARCHAR2(10),"
			+ "C3_POWER_NO VARCHAR2(10))";
	
	private static final String INPUT_JJ = "CREATE TABLE INPUT_JJ ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "MASTER_IDX VARCHAR2(20),"
			+ "WEATHER VARCHAR2(10)," 
			+ "GROUND VARCHAR2(10),"
			+ "SPAN_LENGTH VARCHAR2(10)," 
			+ "CHECK_RESULT VARCHAR2(10),"
			+ "REMARKS VARCHAR2(100),"
			+ "COUNT_1 VARCHAR2(10),"
			+ "TERMINAL1_1 VARCHAR2(10),"
			+ "TERMINAL1_2 VARCHAR2(10),"
			+ "TERMINAL1_3 VARCHAR2(10),"
			+ "TERMINAL1_5 VARCHAR2(10),"
			+ "TERMINAL1_10 VARCHAR2(10));";
	
	private static final String INPUT_KB = "CREATE TABLE INPUT_KB ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "MASTER_IDX VARCHAR2(20),"
			+ "WEATHER VARCHAR2(10)," 
			+ "ITEM_FAC_1 VARCHAR2(10),"
			+ "ITEM_FAC_2 VARCHAR2(10),"
			+ "ITEM_FAC_3 VARCHAR2(10),"
			+ "ITEM_FAC_4 VARCHAR2(10),"
			+ "ITEM_FAC_5 VARCHAR2(10),"
			+ "ITEM_FAC_6 VARCHAR2(10),"
			+ "ITEM_FAC_7 VARCHAR2(10),"
			+ "ITEM_FAC_8 VARCHAR2(10),"
			+ "ITEM_FAC_9 VARCHAR2(10),"
			+ "ITEM_FAC_10 VARCHAR2(10),"
			+ "ITEM_FAC_11 VARCHAR2(10),"
			+ "ITEM_FAC_12 VARCHAR2(10),"
			+ "ITEM_SETT_1 VARCHAR2(10),"
			+ "ITEM_SETT_2 VARCHAR2(10),"
			+ "ITEM_SETT_3 VARCHAR2(10),"
			+ "ITEM_SETT_4 VARCHAR2(10),"
			+ "ITEM_ETC_1 VARCHAR2(10),"
			+ "ITEM_ETC_2 VARCHAR2(10),"
			+ "ITEM_ETC_3 VARCHAR2(10))";
			
	
	private static final String INPUT_HK = "CREATE TABLE INPUT_HK ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "MASTER_IDX VARCHAR2(20),"
			+ "WEATHER VARCHAR2(10)," 
			+ "LIGHT_TYPE VARCHAR2(10),"
			+ "POWER VARCHAR2(10),"
			+ "LIGHT_NO VARCHAR2(10),"
			+ "LIGHT_CNT VARCHAR2(10),"
			+ "LIGHT_STATE VARCHAR2(10),"
			+ "YB_RESULT VARCHAR2(10))";
	
	private static final String INPUT_HJ = "CREATE TABLE INPUT_HJ ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "MASTER_IDX VARCHAR2(20),"
			+ "WEATHER VARCHAR2(10)," 
			+ "LIGHT_TYPE VARCHAR2(10)," 
			+ "POWER VARCHAR2(10)," 
			+ "LIGHT_NO VARCHAR2(10),"
			+ "CONTROL VARCHAR2(10),"
			+ "SUN_BATTERY VARCHAR2(10),"
			+ "STORAGE_BATTERY VARCHAR2(10),"
			+ "LIGHT_ITEM VARCHAR2(10),"
			+ "CABLE VARCHAR2(10),"
			+ "YB_RESULT VARCHAR2(10),"
			+ "REMARKS VARCHAR2(10))";
	
	private static final String INPUT_BR = "CREATE TABLE INPUT_BR ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "MASTER_IDX VARCHAR2(20),"
			+ "WEATHER VARCHAR2(10),"
			+ "CIRCUIT_NO VARCHAR2(50),"
			+ "CIRCUIT_NAME VARCHAR2(50),"
			+ "EJ_CNT VARCHAR2(10),"
			+ "BR_CNT VARCHAR2(10),"
			+ "MAKE_DATE VARCHAR2(20),"
			+ "MAKE_COMPANY VARCHAR2(50),"
			+ "YB_RESULT VARCHAR2(10),"
			+ "TY_SECD VARCHAR2(10),"
			+ "TY_SECD_NM VARCHAR2(10),"
			+ "PHS_SECD VARCHAR2(10),"
			+ "PHS_SECD_NM VARCHAR2(10),"
			+ "INSBTY_LFT VARCHAR2(10),"
			+ "INSBTY_RIT VARCHAR2(10),"
			+ "INSR_EQP_NO VARCHAR2(20))";
	
	private static final String INPUT_YB = "CREATE TABLE INPUT_YB ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "MASTER_IDX VARCHAR2(20),"
			+ "RGT_SN VARCHAR2(20)," 
			+ "BEGIN_EQP_NO VARCHAR2(20),"
			+ "BEGIN_EQP_NM VARCHAR2(20)," 
			+ "END_EQP_NO VARCHAR2(20)," 
			+ "END_EQP_NM VARCHAR2(20),"
			+ "CWRK_NM VARCHAR2(100),"
			+ "TINS_RESULT_SECD VARCHAR2(10),"
			+ "TINS_RESULT VARCHAR2(10))";
	
	
	private static final String MANAGE_CODE = "CREATE TABLE MANAGE_CODE ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "CODE_TYPE VARCHAR2(30),"
			+ "CODE_KEY VARCHAR2(20),"
			+ "CODE_VALUE VARCHAR2(20));"; 
	
	private static final String INSPECT_RESULT_MASTER = "CREATE TABLE INSPECT_RESULT_MASTER ("
			+ "MASTER_IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "TRACKS_NAME VARCHAR2(50),"
			+ "TOWER_NO  VARCHAR2(20),"
			+ "INS_TYPE  VARCHAR2(10),"
			+ "INS_SCH_DATE VARCHAR2(20),"
			+ "LATITUDE  VARCHAR2(20),"
			+ "LONGITUDE  VARCHAR2(20),"
			+ "INS_PLAN_NO  VARCHAR2(50),"
			+ "COMPLETE_YN  VARCHAR2(5),"
			+ "NFC_TAG_LATITUDE  VARCHAR2(30) DEFAULT '',"
			+ "NFC_TAG_LONGITUDE  VARCHAR2(30) DEFAULT '',"
			+ "EQP_NO  VARCHAR2(20),"
			+ "EQP_NM VARCHAR2(50),"
			+ "INS_TYPE_NM VARCHAR2(20),"
			+ "COMPLETE_YN_INS_TYPE VARCHAR2(5) DEFAULT '',"
			+ "SEND_YN_INS_TYPE VARCHAR2(5) DEFAULT '',"
			+ "NFC_TAG_ID VARCHAR2(30) DEFAULT '',"
			+ "NFC_TAG_YN CHAR(1) DEFAULT 'N',"
			+ "INS_INPUT_DATE TIMESTAMP,"
			+ "UNITY_INS_NO VARCHAR2(30),"
			+ "INS_SN VARCHAR2(10),"
			+ "ADDRESS VARCHAR2(100),"
			+ "FNCT_LC_NO VARCHAR2(30));";
	
	
	private static final String TOWER_LIST = "CREATE TABLE TOWER_LIST ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "TOWER_IDX VARCHAR2(20),"
			+ "FNCT_LC_NO VARCHAR2(30),"
			+ "FNCT_LC_DTLS VARCHAR2(50),"
			+ "FNCT_LC_TY VARCHAR2(5),"
			+ "EQP_TY_CD_NM  VARCHAR2(20),"
			+ "EQP_NO  VARCHAR2(20),"
			+ "EQP_NM  VARCHAR2(20),"
			+ "LATITUDE  VARCHAR2(20),"
			+ "LONGITUDE  VARCHAR2(20),"
			+ "CONT_NUM  VARCHAR2(10),"
			+ "ADDRESS  VARCHAR2(100));";
	
	
	private static final String CAMERA_LOG = "CREATE TABLE CAMERA_LOG ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "MASTER_IDX VARCHAR2(20),"
			+ "IMAGE_PATH VARCHAR2(50),"
			+ "INS_TYPE VARCHAR2(10),"
			+ "SUBJECT VARCHAR2(100) DEFAULT '',"
			+ "CONTENT VARCHAR2(100) DEFAULT '');";

	/*정기순시*/
	private static final String INPUT_JG = "CREATE TABLE INPUT_JG ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "MASTER_IDX VARCHAR2(20),"
			+ "WEATHER VARCHAR2(10),"
			+ "AREA_TEMP VARCHAR2(10),"
			+ "CLAIM_CONTENT VARCHAR2(10),"
			+ "T_GUBUN VARCHAR2(10),"
			+ "EQP_NM VARCHAR2(50),"
			+ "UPTLVL_UPLMT VARCHAR2(10),"
			+ "UPTLVL_LWLT VARCHAR2(10),"
			+ "UPTLVL_INTRCP VARCHAR2(10),"
			+ "MNG_01 VARCHAR2(20),"
			+ "MNG_02 VARCHAR2(20),"
			+ "SD VARCHAR2(20),"
			+ "T1_C1 VARCHAR2(10),"
			+ "T1_C2 VARCHAR2(10),"
			+ "T1_C3 VARCHAR2(10),"
			+ "T1_C4 VARCHAR2(10),"
			+ "T2_C1 VARCHAR2(10),"
			+ "T2_C2 VARCHAR2(10),"
			+ "T2_C3 VARCHAR2(10))";

	/*정기순시 유압리스트*/
	private static final String INPUT_JG_U_SUBINFO = "CREATE TABLE INPUT_JG_U_SUBINFO ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "FNCT_LC_NO VARCHAR2(50),"
			+ "FNCT_LC_DTLS VARCHAR2(50),"
			+ "EQP_NO VARCHAR2(20),"
			+ "EQP_NM VARCHAR2(50),"
			+ "UPTLVL_UPLMT VARCHAR2(10),"
			+ "UPTLVL_LWLT VARCHAR2(10),"
			+ "UPTLVL_INTRCP VARCHAR2(10),"
			+ "MNG_01 VARCHAR2(20),"
			+ "MNG_02 VARCHAR2(20),"
			+ "SD VARCHAR2(20),"
			+ "TOWER_IDX VARCHAR2(20),"
			+ "CONT_NUM VARCHAR2(20),"
			+ "SN VARCHAR2(20),"
			+ "TTM_LOAD VARCHAR2(10))";

	/*정기순시 피뢰기리스트*/
	private static final String INPUT_JG_P_SUBINFO = "CREATE TABLE INPUT_JG_P_SUBINFO ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "TOWER_IDX VARCHAR2(20),"
			+ "CONT_NUM VARCHAR2(20),"
			+ "SN VARCHAR2(20),"
			+ "TTM_LOAD VARCHAR2(10),"
			+ "FNCT_LC_DTLS VARCHAR2(50),"
			+ "EQP_NM VARCHAR2(50),"
			+ "FNCT_LC_NO VARCHAR2(50),"
			+ "EQP_NO VARCHAR2(20))";

	/*경보회로점검*/
	private static final String INPUT_GH = "CREATE TABLE INPUT_GH ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "MASTER_IDX VARCHAR2(20),"
			+ "WEATHER VARCHAR2(10),"
			+ "AREA_TEMP VARCHAR2(10),"
			+ "CIRCUIT_NO VARCHAR2(30),"
			+ "CIRCUIT_NAME VARCHAR2(50),"
			+ "CURRENT_LOAD VARCHAR2(10),"
			+ "CONDUCTOR_CNT VARCHAR2(10),"
			+ "LOCATION VARCHAR2(10),"
			+ "C1 VARCHAR2(10),"
			+ "C2 VARCHAR2(10),"
			+ "C3 VARCHAR2(10),"
			+ "C4 VARCHAR2(10),"
			+ "C5 VARCHAR2(10),"
			+ "C6 VARCHAR2(10),"
			+ "C7 VARCHAR2(10),"
			+ "C8 VARCHAR2(10),"
			+ "C9 VARCHAR2(10))";

	/*경보회로점검 _점검리스트정보*/
	private static final String INPUT_GH_SUBINFO = "CREATE TABLE INPUT_GH_SUBINFO ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "TOWER_IDX VARCHAR2(20),"
			+ "CONT_NUM VARCHAR2(20),"
			+ "SN VARCHAR2(20),"
			+ "TTM_LOAD VARCHAR2(10),"
			+ "FNCT_LC_DTLS VARCHAR2(50),"
			+ "EQP_NM VARCHAR2(50),"
			+ "FNCT_LC_NO VARCHAR2(50),"
			+ "EQP_NO VARCHAR2(20),"
			+ "POWER_NO_C1 VARCHAR2(20),"
			+ "POWER_NO_C2 VARCHAR2(20),"
			+ "POWER_NO_C3 VARCHAR2(20))";

	/*피뢰기점검*/
	private static final String INPUT_PR = "CREATE TABLE INPUT_PR ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "MASTER_IDX VARCHAR2(20),"
			+ "WEATHER VARCHAR2(10),"
			+ "AREA_TEMP VARCHAR2(10),"
			+ "CIRCUIT_NO VARCHAR2(30),"
			+ "CIRCUIT_NAME VARCHAR2(50),"
			+ "CURRENT_LOAD VARCHAR2(10),"
			+ "CONDUCTOR_CNT VARCHAR2(10),"
			+ "LOCATION VARCHAR2(10),"
			+ "CLAIM_CONTENT VARCHAR2(10),"
			+ "T_GUBUN VARCHAR2(10),"
			+ "T1_C1 VARCHAR2(10),"
			+ "T1_C2 VARCHAR2(10),"
			+ "T1_C3 VARCHAR2(10),"
			+ "T1_C4 VARCHAR2(10),"
			+ "T2_C1 VARCHAR2(10),"
			+ "T2_C2 VARCHAR2(10),"
			+ "T2_C3 VARCHAR2(10),"
			+ "YB_RESULT VARCHAR(10))";

	/*피뢰기점검 피뢰기점검리스트*/
	private static final String INPUT_PR_P_SUBINFO = "CREATE TABLE INPUT_PR_P_SUBINFO ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "TOWER_IDX VARCHAR2(20),"
			+ "CONT_NUM VARCHAR2(20),"
			+ "SN VARCHAR2(20),"
			+ "TTM_LOAD VARCHAR2(10),"
			+ "FNCT_LC_DTLS VARCHAR2(50),"
			+ "EQP_NM VARCHAR2(50),"
			+ "FNCT_LC_NO VARCHAR2(50),"
			+ "EQP_NO VARCHAR2(20))";

	/*피뢰기점검 케이블헤드리스트*/
	private static final String INPUT_PR_K_SUBINFO = "CREATE TABLE INPUT_PR_K_SUBINFO ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "TOWER_IDX VARCHAR2(20),"
			+ "CONT_NUM VARCHAR2(20),"
			+ "SN VARCHAR2(20),"
			+ "TTM_LOAD VARCHAR2(10),"
			+ "FNCT_LC_DTLS VARCHAR2(50),"
			+ "EQP_NM VARCHAR2(50),"
			+ "FNCT_LC_NO VARCHAR2(50),"
			+ "EQP_NO VARCHAR2(20))";


	/*맨홀점검*/
	private static final String INPUT_MH = "CREATE TABLE INPUT_MH ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "MASTER_IDX VARCHAR2(20),"
			+ "WEATHER VARCHAR2(10),"
			+ "AREA_TEMP VARCHAR2(10),"
			+ "CIRCUIT_NO VARCHAR2(30),"
			+ "CIRCUIT_NAME VARCHAR2(50),"
			+ "CURRENT_LOAD VARCHAR2(10),"
			+ "CONDUCTOR_CNT VARCHAR2(10),"
			+ "LOCATION VARCHAR2(10),"
			+ "CLAIM_CONTENT VARCHAR2(10),"
			+ "T_GUBUN VARCHAR2(10),"
			+ "T1_C1 VARCHAR2(10),"
			+ "T1_C2 VARCHAR2(10),"
			+ "T1_C3 VARCHAR2(10),"
			+ "T1_C4 VARCHAR2(10),"
			+ "T1_C5 VARCHAR2(10),"
			+ "T1_C6 VARCHAR2(10),"
			+ "T1_C7 VARCHAR2(10),"
			+ "T1_C8 VARCHAR2(10),"
			+ "T2_C1 VARCHAR2(10),"
			+ "T2_C2 VARCHAR2(10),"
			+ "T2_C3 VARCHAR2(10),"
			+ "T2_C4 VARCHAR2(10),"
			+ "T2_C5 VARCHAR2(10),"
			+ "T2_C6 VARCHAR2(10),"
			+ "T2_C7 VARCHAR2(10),"
			+ "T2_C8 VARCHAR2(10),"
			+ "G_GUBUN VARCHAR2(10),"
			+ "T3_C1 VARCHAR2(10),"
			+ "T3_C2 VARCHAR2(10),"
			+ "T3_C3 VARCHAR2(10),"
			+ "T3_C4 VARCHAR2(10),"
			+ "T3_C5 VARCHAR2(10),"
			+ "T3_C6 VARCHAR2(10),"
			+ "T3_C7 VARCHAR2(10),"
			+ "T3_C8 VARCHAR2(10),"
			+ "T3_C9 VARCHAR2(10),"
			+ "T3_C10 VARCHAR2(10))";

	/*맨홀점검 이상유무리스트*/
	private static final String INPUT_MH_I_SUBINFO = "CREATE TABLE INPUT_MH_I_SUBINFO ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "TOWER_IDX VARCHAR2(20),"
			+ "FNCT_LC_DTLS VARCHAR2(50),"
			+ "EQP_NM VARCHAR2(50),"
			+ "FNCT_LC_NO VARCHAR2(50),"
			+ "EQP_NO VARCHAR2(20))";

	/*맨홀점검 발생유무리스트*/
	private static final String INPUT_MH_B_SUBINFO = "CREATE TABLE INPUT_MH_B_SUBINFO ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "TOWER_IDX VARCHAR2(20),"
			+ "FNCT_LC_DTLS VARCHAR2(50),"
			+ "EQP_NM VARCHAR2(50),"
			+ "FNCT_LC_NO VARCHAR2(50),"
			+ "EQP_NO VARCHAR2(20))";

	/*맨홀점검 가스탐지기록리스트*/
	private static final String INPUT_MH_G_SUBINFO = "CREATE TABLE INPUT_MH_G_SUBINFO ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "TOWER_IDX VARCHAR2(20),"
			+ "CONT_NUM VARCHAR2(20),"
			+ "SN VARCHAR2(20),"
			+ "TTM_LOAD VARCHAR2(10),"
			+ "FNCT_LC_DTLS VARCHAR2(50),"
			+ "EQP_NM VARCHAR2(50),"
			+ "FNCT_LC_NO VARCHAR2(50),"
			+ "EQP_NO VARCHAR2(20),"
			+ "G_GUBUN VARCHAR2(10))";

	/*전력구소방시설_보통점검 */
	private static final String INPUT_JB = "CREATE TABLE INPUT_JB ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "MASTER_IDX VARCHAR2(20),"
			+ "WEATHER VARCHAR2(10),"
			+ "AREA_TEMP VARCHAR2(10),"
			+ "CIRCUIT_NAME VARCHAR2(50),"
			+ "CURRENT_LOAD VARCHAR2(10),"
			+ "CLAIM_CONTENT VARCHAR2(10),"
			+ "T_GUBUN VARCHAR2(10),"
			+ "T1_NO VARCHAR2(10),"
			+ "T1_C VARCHAR2(10),"
			+ "T2_NO VARCHAR2(10),"
			+ "T2_C VARCHAR2(10),"
			+ "T3_NO VARCHAR2(10),"
			+ "T3_C VARCHAR2(10))";

	private static final String INPUT_JS_SUBINFO = "CREATE TABLE INPUT_JS_SUBINFO ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "TOWER_IDX VARCHAR2(20),"
			+ "CONT_NUM VARCHAR2(20),"
			+ "SN VARCHAR2(20),"
			+ "TTM_LOAD VARCHAR2(10),"
			+ "FNCT_LC_DTLS VARCHAR2(50),"
			+ "EQP_NM VARCHAR2(50)," 
			+ "FNCT_LC_NO VARCHAR2(50),"
			+ "EQP_NO VARCHAR2(20),"
			+ "POWER_NO_C1 VARCHAR2(20),"
			+ "POWER_NO_C2 VARCHAR2(20),"
			+ "POWER_NO_C3 VARCHAR2(20))";
	
	private static final String INPUT_HK_SUBINFO = "CREATE TABLE INPUT_HK_SUBINFO ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "TOWER_IDX VARCHAR2(20),"
			+ "SRCELCT_KND VARCHAR2(20),"
			+ "SRCELCT_KND_NM VARCHAR2(20),"
			+ "EQP_NO VARCHAR2(20),"
			+ "FLIGHT_LMP_NO VARCHAR2(10),"
			+ "FLIGHT_LMP_KND VARCHAR2(20),"
			+ "FLIGHT_LMP_KND_NM VARCHAR2(20))";
	
	private static final String INPUT_BR_SUBINFO = "CREATE TABLE INPUT_BR_SUBINFO ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "TOWER_IDX VARCHAR2(20),"
			+ "INSR_EQP_NO VARCHAR2(20),"
			+ "INSBTY_LFT VARCHAR2(5),"
			+ "INSBTY_RIT VARCHAR2(5),"
			+ "TY_SECD_NM VARCHAR2(20),"
			+ "TY_SECD VARCHAR2(20)," 
			+ "CL_NM VARCHAR2(50),"
			+ "CL_NO VARCHAR2(50),"
			+ "INS_CNT VARCHAR2(20),"
			+ "EQP_NO VARCHAR2(20),"
			+ "PHS_SECD VARCHAR2(20),"
			+ "PHS_SECD_NM VARCHAR2(20))";

	private static final String INPUT_JOIN_REPORT = "CREATE TABLE INPUT_JOIN_REPORT ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "WEATHER VARCHAR2(20),"
			+ "NAME VARCHAR2(20),"
			+ "LENGTH VARCHAR2(20),"
			+ "LOCATION VARCHAR2(20),"
			+ "WORK_NM VARCHAR2(50),"
			+ "JOIN_DATE VARCHAR2(20),"
			+ "REQUEST_DATE VARCHAR2(20),"
			+ "JOIN_S_DATE VARCHAR2(20),"
			+ "JOIN_S_TIME VARCHAR2(20),"
			+ "JOIN_E_DATE VARCHAR2(20),"
			+ "JOIN_E_TIME VARCHAR2(20),"
			+ "REQUEST_JOINER_CO VARCHAR2(20),"
			+ "REQUEST_JOINER VARCHAR2(20),"
			+ "JOINER_DEPT VARCHAR2(20),"
			+ "JOINER VARCHAR2(20),"
			+ "JOIN_REASON VARCHAR2(20),"
			+ "ETC VARCHAR2(20))";

	private static final String COMPANY_LIST = "CREATE TABLE COMPANY_LIST ("
			+ "IDX INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "COMPANY_NAME VARCHAR2(50))";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(MYLOCATION_LOG);
			db.execSQL(INPUT_BT);
			db.execSQL(INPUT_JS);
			db.execSQL(INPUT_JP);
			db.execSQL(INPUT_JS_SUBINFO);
			db.execSQL(INPUT_JJ);
			db.execSQL(INPUT_KB);
			db.execSQL(INPUT_HK);
			db.execSQL(INPUT_HJ);
			db.execSQL(INPUT_HK_SUBINFO);
			db.execSQL(INPUT_BR);
			db.execSQL(INPUT_BR_SUBINFO);
			db.execSQL(INPUT_YB);
			db.execSQL(MANAGE_CODE);
			db.execSQL(INSPECT_RESULT_MASTER);
			db.execSQL(CAMERA_LOG);
			db.execSQL(TOWER_LIST);
			db.execSQL(COMPANY_LIST);

			db.execSQL(INPUT_JG);
			db.execSQL(INPUT_JG_U_SUBINFO);
			db.execSQL(INPUT_JG_P_SUBINFO);
			db.execSQL(INPUT_GH);
			db.execSQL(INPUT_GH_SUBINFO);
			db.execSQL(INPUT_PR);
			db.execSQL(INPUT_PR_P_SUBINFO);
			db.execSQL(INPUT_PR_K_SUBINFO);
			db.execSQL(INPUT_MH);
			db.execSQL(INPUT_MH_I_SUBINFO);
			db.execSQL(INPUT_MH_B_SUBINFO);
			db.execSQL(INPUT_MH_G_SUBINFO);
			db.execSQL(INPUT_JB);

			db.execSQL(INPUT_JOIN_REPORT);
			
			
		} catch (SQLiteException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			db.execSQL("DROP TABLE IF EXISTS KDN_MYLOCATION;");
		} catch (SQLiteException ex) {
			ex.printStackTrace();
		}
		onCreate(db);
	}

}
