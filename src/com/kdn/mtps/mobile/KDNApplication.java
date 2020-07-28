package com.kdn.mtps.mobile;

import android.app.Application;

import com.google.android.gcm.GCMRegistrar;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.Shared;

public class KDNApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		registGCM();
	}
	
	void registGCM() {
		try {
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			
			String regId = GCMRegistrar.getRegistrationId(this);
			Logg.d("Application-regId:"+regId);
			if ("".equals(regId))
				GCMRegistrar.register(this, GCMIntentService.SEND_ID);
			else {
				if (Shared.getString(this, ConstSP.GCM_REGID_KEY, null) == null)
					Shared.set(this, ConstSP.GCM_REGID_KEY, regId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    }
}
