package com.kdn.mtps.mobile.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;

import com.kdn.mtps.mobile.LoginActivity;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.db.MyLocationLogDao;
import com.kdn.mtps.mobile.db.bean.MyLocationLog;
import com.kdn.mtps.mobile.net.api.ApiManager;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.thread.ATask;

public class KDNService extends Service {
	
	public final int TAG_KDNSERVICE_DEFAULT = 1000;
	public final int TAG_REMOVE_LOCATION_LISTENER = TAG_KDNSERVICE_DEFAULT + 1;
	public final int TAG_INSERT_LOCATION = TAG_KDNSERVICE_DEFAULT + 2;
	
	// gps 정보 입력 호출 시간
	public final int TAG_INSERT_TIME = (1000 * 60) * 1; //분
	
	// gps  정보 가져오기 취소 시간
	public final int TAG_CANCEL_TIME = 1000 * 30; //초
	
	public boolean getGps = false;
	
	Context mContext;
	Timer mTimer;
	LocationManager locationManager;
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Logg.d(mContext, "handleMessage");
			
			switch (msg.what) {
			case TAG_REMOVE_LOCATION_LISTENER:
			{
				Logg.d(mContext, "TAG_REMOVE_LOCATION_LISTENER");
				if (locationManager != null)
	            	locationManager.removeUpdates(listener);

				// 최초에 gps로 위치정보를 호출했는데 위치정보를 가져오지 못했다면 network로 위치정보 가져옴
				if (!getGps && LocationManager.GPS_PROVIDER.equals(msg.obj) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
					requestLocation(LocationManager.NETWORK_PROVIDER);
				
				break;
			}
			case TAG_INSERT_LOCATION:
			{
				Logg.d(mContext, "TAG_INSERT_LOCATION=========");
				
	            MyLocationLog log = new MyLocationLog();
				
//	            log.reg_date = new SimpleDateFormat ("yyyy.MM.dd HH:mm").format(new Date());
	            log.reg_date = new Date().getTime();
	            log.latitude = "";
	            log.longitude = "";
	            
				MyLocationLogDao myLocationLogDao = MyLocationLogDao.getInstance(mContext);
				
				MyLocationLog locationLog = myLocationLogDao.getLastData();
				Logg.d("regdate : " + locationLog.reg_date);
				
				Date d1 = new Date(new Date().getTime());
				Date d2 = new Date(locationLog.reg_date);
				
				long diffTime = d1.getTime() - d2.getTime();
				
				Logg.d("regdiff : " + diffTime);
				if (1000*60*15 <= diffTime) {
					myLocationLogDao.Append(log);
					requestLocation();
				}
				
				break;
			}
			}
		}
	};
	
	private class MyTask extends TimerTask {
        public void run() {
        	try{
				mHandler.sendEmptyMessage(TAG_INSERT_LOCATION);
			}catch(Exception ex){
				ex.printStackTrace();
			}
        }
      }
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Logg.d(this, "onStartCommand");
		
		mContext = this;
		
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		} else {
			mTimer = new Timer();
			MyTask task=new MyTask();
			mTimer.schedule(task, 0, TAG_INSERT_TIME);
		}
		
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Logg.d(mContext, "onDestroy");
		
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		
		startService(new Intent(this, KDNService.class));
	}
	
	LocationListener listener = new LocationListener() {
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			case LocationProvider.OUT_OF_SERVICE:
				Logg.d(mContext, "상태: 서비스 범위 밖입니다.");
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Logg.d(mContext, "상태: 일시적으로 서비스 사용 불가능");
				break;
			case LocationProvider.AVAILABLE:
				Logg.d(mContext, "상태: 서비스 이용 가능");
				break;
				
			}
		}
		
		public void onProviderEnabled(String provider) {
			Logg.d(mContext, "상태: 서비스 사용가능");
		}
		
		public void onProviderDisabled(String provider) {
			Logg.d(mContext, "상태: 서비스 사용불가");
		}
		
		@Override
		public void onLocationChanged(Location location) {
			getGps = true;
			
			String currentLocation = String.format("위도:%f  경도:%f  고도:%f", location.getLatitude(), location.getLongitude(), location.getAltitude());
			Logg.d(mContext, "currentLocation : " + currentLocation);
            Calendar cal = new GregorianCalendar();
            String now = String.format("%d년 %d월 %d일 %d시 %d분 %d초", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, 
					cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
					cal.get(Calendar.SECOND));
            Logg.d(mContext, "갱신 시간 : " + now);
            
            String loginId = Shared.getString(mContext, ConstSP.LOING_ID_KEY);
            if (loginId != null && loginId.length() > 0)
            	insertLocation(loginId, location.getLatitude()+"", location.getLongitude()+"");
			
            if (locationManager != null) {
            	locationManager.removeUpdates(listener);
            }
		}
	};
	
	public void requestLocation() {
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		String provider = locationManager.getBestProvider(new Criteria(), true);
		if (provider == null || "passive".equals(provider)) {
//			Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//			goToSettings.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
//			startActivity(goToSettings);
			return;
		}
		
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
	
	public void insertLocation(final String loginId, final String latitude, final String longitude) {
		try {
			
			final String device_token = Shared.getString(KDNService.this, ConstSP.GCM_REGID_KEY);
			
			ATask.executeVoid(new ATask.OnTask() {

				@Override
				public void onPre() {
					
				}

				@Override
				public void onBG() {
					ApiManager.saveTracking(loginId, latitude, longitude, device_token);
				}

				@Override
				public void onPost() {
					
				}
			});
		} catch (Exception e) {
			
		}
	}
}
