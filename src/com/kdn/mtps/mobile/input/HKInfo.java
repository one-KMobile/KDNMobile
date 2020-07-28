package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class HKInfo implements Parcelable {
	public int idx;
	public String masterIdx = "";
	public String weather = "";
	public String lightType = "";
	public String light_no = "";
	public String power = "";
	public String light_cnt = "";
	public String light_state = "";
	public String yb_result = "";
	
	public HKInfo() {
		
	}
	
	private HKInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		idx = source.readInt();
		masterIdx = source.readString();
		weather = source.readString();
		lightType = source.readString();
		light_no = source.readString();
		power = source.readString();
		light_cnt = source.readString();
		light_state = source.readString();
		yb_result = source.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(idx);
		arg0.writeString(masterIdx);
		arg0.writeString(weather);
		arg0.writeString(lightType);
		arg0.writeString(light_no);
		arg0.writeString(power);
		arg0.writeString(light_cnt);
		arg0.writeString(light_state);
		arg0.writeString(yb_result);
	}

	public  static final Parcelable.Creator<HKInfo> CREATOR
	= new Parcelable.Creator<HKInfo>() {

		@Override
		public HKInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new HKInfo(source);
		}

		@Override
		public HKInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new HKInfo[size];
		}
	};

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}
	
	public String getMasterIdx() {
		return masterIdx;
	}

	public void setMasterIdx(String masterIdx) {
		this.masterIdx = masterIdx;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}


	public static class COLS {
		public static final String IDX = "IDX";
		public static final String MASTER_IDX = "MASTER_IDX";
		public static final String WEATHER = "WEATHER";
		public static final String LIGHT_TYPE = "LIGHT_TYPE";
		public static final String LIGHT_NO = "LIGHT_NO";
		public static final String POWER = "POWER";
		public static final String LIGHT_CNT = "LIGHT_CNT";
		public static final String LIGHT_STATE = "LIGHT_STATE";
		public static final String YB_RESULT = "YB_RESULT";
		
	}
	
}
