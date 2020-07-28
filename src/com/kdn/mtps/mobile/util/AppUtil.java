package com.kdn.mtps.mobile.util;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.kdn.mtps.mobile.constant.ConstVALUE;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

public class AppUtil {
	public static void startActivity(Activity act, Intent intent) {
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		act.startActivity(intent);
	}
	
	public static void startActivity(Context act, Intent intent) {
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		act.startActivity(intent);
	}
	
	public static boolean checkRunningService(Context paramContext,
			String paramString) {
		List info = ((ActivityManager) paramContext
				.getSystemService("activity")).getRunningServices(200);
		for (Iterator localIterator = info.iterator(); localIterator.hasNext();) {
			if (((ActivityManager.RunningServiceInfo) localIterator.next()).service
					.getClassName().equalsIgnoreCase(paramString))
				return true;
		}
		return false;
	}
	
	public static void clearApplicationData(Context context) {
		
		File file = new File(ConstVALUE.PATH_SDCARD_KDN_ROOT);
		deleteDir(file);
		
		MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, null,
				new MediaScannerConnection.OnScanCompletedListener() {
					public void onScanCompleted(String path, Uri uri) {
					}
				});
		
	    File cache = context.getCacheDir();
	    File appDir = new File(cache.getParent());
	    if (appDir.exists()) {
	        String[] children = appDir.list();
	        for (String s : children) {
	            if (!s.equals("lib")) {
	                deleteDir(new File(appDir, s));
	                Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
	            }
	        }
	    }
	}

	public static boolean deleteDir(File dir) {
	    if (dir != null && dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }

	    return dir.delete();
	}
}
