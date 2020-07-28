package com.kdn.mtps.mobile;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.service.KDNService;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.Shared;

public class SplashActivity extends BaseActivity{
	
	ImageView ivSplash;
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			AppUtil.startActivity(SplashActivity.this, new Intent(SplashActivity.this, LoginActivity.class));
			finish();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_splash);
		
		ivSplash = (ImageView)findViewById(R.id.ivSplash);
		
		String strCompany = Shared.getString(this, ConstSP.SETTING_COMPANY);
		
		// 설정-업체설정 에서 설정한 업체 이미지가 적용된다. 기본은 kepco임.
		if (strCompany != null) {
			if ("한전KDN".equals(strCompany))
				ivSplash.setBackgroundResource(R.drawable.landing_kdn);
			else if ("한전KPS".equals(strCompany))
				ivSplash.setBackgroundResource(R.drawable.landing_kps);
		}
		
		boolean isRunService = AppUtil.checkRunningService(this, getPackageName() + ".service.KDNService");
		if ( ! isRunService ) {
			Logg.d("start Service");
			startService(new Intent(this, KDNService.class));
		}
		String[] tgtDir = {ConstVALUE.PATH_SDCARD_KDN_ROOT, ConstVALUE.PATH_SDCARD_PICTURE};
		for (String dir : tgtDir) {
			File file = new File(dir);
			if (!file.exists())
				file.mkdirs();
		}
		
		// 1초 후에 로그인 화면으로 이동 
		handler.sendEmptyMessageDelayed(0, 1000);
	}
	
}
	
