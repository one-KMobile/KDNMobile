package com.kdn.mtps.mobile.util;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.kdn.mtps.mobile.constant.ConstVALUE;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

public class AnsemUtil {
	// Ansem 패키지명
		public static final String SERVICE_PKG_NAME = "com.kdn.ansempro23";	

		// Ansem SharedPreference 키값
		private final static String PREFERENCEKEY_NAME = "KdnMdmUserSecurity";	

		// 유저아이디 키
		private final static String PREFERENCEKEY_USERID = "mdm_preferencekey_userid";

		// 패스워드 키
		private final static String PREFERENCEKEY_PASSWORD = "mdm_preferencekey_password";

		// VPN 접속상태 키
		private final static String PREFERENCEKEY_NETWORK_STATE = "mdm_preferencekey_network_state";	

		// VPN 상태값
		private final static int VPNRESULT_DISCONNECTED = 0;		// 연결되어 있지 않은 상태
		private final static int VPNRESULT_CONNECTED = 1;			// 연결된 상태
		private final static int VPNRESULT_CONNECTING = 2;			// 연결중인 상태
		
		public final static int REQUEST_ANSEM_CODE = 11;		
		
	public static boolean isConnected(Context ctx) {
		
		SharedPreferences pref = ctx.getSharedPreferences(PREFERENCEKEY_NAME, Activity.MODE_PRIVATE);
		if (pref == null) { return false; }
		String userid = pref.getString(PREFERENCEKEY_USERID, "");
		String passwd = pref.getString(PREFERENCEKEY_PASSWORD, "");
		int state = pref.getInt(PREFERENCEKEY_NETWORK_STATE, 0);
		Logg.d("ansem userId : " + userid + " / " + passwd + " / " + state);
		
		if (NetUtil.isDisconnect(ctx))
			return true;
		
		// temp
		if (1==1) return true;
		
		if (state == VPNRESULT_DISCONNECTED) {
			return false;
		} else {
			return true;
		}
	}
	
	public static void showAnsemLogin(Context ctx) {
		ComponentName compName = 
				new ComponentName(SERVICE_PKG_NAME, SERVICE_PKG_NAME+".MdmClient");
				Intent i = new Intent(Intent.ACTION_MAIN);
				i.addCategory(Intent.CATEGORY_LAUNCHER);
				i.setComponent(compName);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				startActivity(i);
				((Activity) ctx).startActivityForResult(i, REQUEST_ANSEM_CODE);
	}
}
