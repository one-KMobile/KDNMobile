package com.kdn.mtps.mobile.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class NetUtil {
	static final int STATE_NONE = 0;
	static final int STATE_WIFI_CONNECTED = 1;
	static final int STATE_MOBILE_CONNECTED = 2;
	static int state = STATE_NONE;

	public static boolean isDisconnect(Context context) {
		boolean isDisconnect = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo niWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo niMobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (state == STATE_NONE) {
				if (niWifi.getState() == State.CONNECTED) {
					state = STATE_WIFI_CONNECTED;
				} else if (niMobile.getState() == State.CONNECTED) {
					state = STATE_MOBILE_CONNECTED;
				} else {
					isDisconnect = true;
				}
			} else if (state == STATE_WIFI_CONNECTED) {
				if (niWifi.getState() == State.DISCONNECTED || niWifi.getState() == State.DISCONNECTING) {
					state = STATE_NONE;
					isDisconnect = true;
					Logg.d("Wi-Fi 끊어졌다!");
				}
			} else if (state == STATE_MOBILE_CONNECTED) {
				if (niMobile.getState() == State.DISCONNECTED || niMobile.getState() == State.DISCONNECTING) {
					state = STATE_NONE;
					isDisconnect = true;
					Logg.d("3G/4G 끊어졌다!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isDisconnect;
	}

	public static boolean isNetAvailable(Context context) {
		NetworkInfo activeNetInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		return activeNetInfo != null && activeNetInfo.isAvailable();
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo == null)
			return false;

		NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		// 둘 중 하나의 네트워크에만 연결됐을 때
		if ((mobile != null && mobile.isConnected()) != (wifi != null && wifi.isConnected()) && State.CONNECTED == activeNetInfo.getState())
			return true;
		return false;
	}

	public static int getNetworkStatus(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo == null)
			return -1;

		NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if ((mobile != null && mobile.isConnected()) && (wifi != null && wifi.isConnected()))
			return -2;
		else if ((mobile != null && mobile.isConnected()) && (wifi != null && !wifi.isConnected()))
			return ConnectivityManager.TYPE_MOBILE;
		else if ((mobile != null && mobile.isConnected()) != (wifi != null && wifi.isConnected()))
			return ConnectivityManager.TYPE_WIFI;

		return -1;
	}
}
