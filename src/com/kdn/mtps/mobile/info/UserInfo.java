package com.kdn.mtps.mobile.info;

import android.content.Context;

import com.kdn.mtps.mobile.KDNApplication;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.net.api.bean.LoginData;
import com.kdn.mtps.mobile.net.api.bean.LoginData.SessionData;
import com.kdn.mtps.mobile.util.Shared;

public class UserInfo {
	static SessionData sessionData;// 로그인 후 Hong 서버로부터 받는 데이터

	public static void clearData() {
		sessionData = null;
	}

	public static SessionData getSessionData(Context ctx) {
			sessionData = new SessionData();
			
			sessionData.user_name = Shared.getString(ctx, ConstSP.USER_NAME);
			sessionData.user_id = Shared.getString(ctx, ConstSP.USER_ID);
			sessionData.contract = Shared.getString(ctx, ConstSP.CONTRACT);
			sessionData.fst_bizplc_cd = Shared.getString(ctx, ConstSP.FST_BIZPLC_CD);
			sessionData.fst_bizplc_cd_nm = Shared.getString(ctx, ConstSP.FST_BIZPLC_CD_NM);
			sessionData.scd_bizplc_cd = Shared.getString(ctx, ConstSP.SCD_BIZPLC_CD);
			sessionData.scd_bizplc_cd_nm = Shared.getString(ctx, ConstSP.SCD_BIZPLC_CD_NM);
			sessionData.appVersion = Shared.getString(ctx, ConstSP.APP_SERVER_VERSION);
			sessionData.appUrl = Shared.getString(ctx, ConstSP.APP_URL);
		
		return sessionData;
	}
	
	public static void setLoginData(Context ctx, LoginData loginData) {
		UserInfo.sessionData = loginData.session;
		
		Shared.set(ctx, ConstSP.USER_NAME, loginData.session.user_name);
		Shared.set(ctx, ConstSP.USER_ID, loginData.session.user_id);
		
		String contract = Shared.getString(ctx, ConstSP.CONTRACT);
		if (contract == null || "".equals(contract)) {
			Shared.set(ctx, ConstSP.CONTRACT, loginData.session.contract);
		}
		
		Shared.set(ctx, ConstSP.FST_BIZPLC_CD, loginData.session.fst_bizplc_cd);
		Shared.set(ctx, ConstSP.FST_BIZPLC_CD_NM, loginData.session.fst_bizplc_cd_nm);
		Shared.set(ctx, ConstSP.SCD_BIZPLC_CD, loginData.session.scd_bizplc_cd);
		Shared.set(ctx, ConstSP.SCD_BIZPLC_CD_NM, loginData.session.scd_bizplc_cd_nm);
		Shared.set(ctx, ConstSP.APP_SERVER_VERSION, loginData.session.appVersion);
		Shared.set(ctx, ConstSP.APP_URL, loginData.session.appUrl);
		
	}

}
