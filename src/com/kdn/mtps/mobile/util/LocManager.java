package com.kdn.mtps.mobile.util;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;

public class LocManager {
	public final int TAG_KDNSERVICE_DEFAULT = 1000;
	public final int TAG_REMOVE_LOCATION_LISTENER = TAG_KDNSERVICE_DEFAULT + 1;
	public final int TAG_INSERT_LOCATION = TAG_KDNSERVICE_DEFAULT + 2;
	// gps  정보 가져오기 취소 시간
	public final int TAG_CANCEL_TIME = 1000 * 5; //초
		
	static LocManager locManager;
	public static Context ctx;
	LocationManager locationManager;
	public boolean getGps = false;
	LocationListener listener;
	
	public LocManager() {
	}
	
	public static LocManager getInstance(Context c) {
		ctx = c;
		if (locManager == null)
			locManager = new LocManager();
		return locManager;
	}
	
	public void setListener(LocationListener locListener) {
		listener = locListener;
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Logg.d(ctx, "handleMessage");
			
			switch (msg.what) {
			case TAG_REMOVE_LOCATION_LISTENER:
			{
				Logg.d(ctx, "TAG_REMOVE_LOCATION_LISTENER");
				if (locationManager != null)
					locationManager.removeUpdates(listener);

				// 최초에 gps로 위치정보를 호출했는데 위치정보를 가져오지 못했다면 network로 위치정보 가져옴
				if (!getGps && LocationManager.GPS_PROVIDER.equals(msg.obj) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
					requestLocation(LocationManager.NETWORK_PROVIDER);
				
				break;
			}
			case TAG_INSERT_LOCATION:
			{
				Logg.d(ctx, "TAG_INSERT_LOCATION");
				requestLocation();
				break;
			}
			}
		}
	};
	
	public void requestLocation() {
		
		locationManager = (LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);
		
		String provider = locationManager.getBestProvider(new Criteria(), true);
		if (provider == null || "passive".equals(provider))
			return;
		
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			requestLocation(LocationManager.GPS_PROVIDER);
		else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			requestLocation(LocationManager.NETWORK_PROVIDER);
	}
	
	public void requestLocation(String provider) {
        Logg.d("provider : " + provider);
        
        getGps = false;
        locationManager.requestLocationUpdates(provider, 0,0, listener);
        
        Message msg = new Message();
        msg.what = TAG_REMOVE_LOCATION_LISTENER;
        msg.obj = provider;
        mHandler.sendMessageDelayed(msg, TAG_CANCEL_TIME);
	}
	
	public void startGetLocation(LocationListener locListener) {
		listener = locListener;
		mHandler.sendEmptyMessage(TAG_INSERT_LOCATION);
	}
	
	public void setGetGps(boolean getGps) {
		this.getGps = getGps;
	}
}
