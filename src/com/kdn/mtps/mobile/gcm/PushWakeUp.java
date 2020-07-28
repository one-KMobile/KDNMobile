package com.kdn.mtps.mobile.gcm;

import android.content.Context;
import android.os.PowerManager;

public class PushWakeUp {
	private static final String TAG = "Talk";
	private static PowerManager.WakeLock mWakeLock;

	public static void wakeLock(Context context) {
		if (mWakeLock != null) 
			mWakeLock.release();

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        mWakeLock = pm.newWakeLock(
        		PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "WakeLock");
        mWakeLock.acquire();

	}

	public static void powerOff(Context context) {
		
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		pm.goToSleep(System.currentTimeMillis() + 100); 
	}

	public static void releaseWakeLock() {
		if (mWakeLock != null) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}
}
