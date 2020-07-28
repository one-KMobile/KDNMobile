package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class YBInfo implements Parcelable {
	public int idx;
	public String masterIdx;
	public String weather;
	public String RGT_SN;
	public String BEGIN_EQP_NO;
	public String BEGIN_EQP_NM;
	public String END_EQP_NO;
	public String END_EQP_NM;
	public String CWRK_NM;
	public String TINS_RESULT_SECD;
	public String TINS_RESULT;
	
	
	public YBInfo() {
		
	}
	
	private YBInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		idx = source.readInt();
		masterIdx = source.readString();
		weather = source.readString();
		RGT_SN = source.readString();
		BEGIN_EQP_NO = source.readString();
		BEGIN_EQP_NM = source.readString();
		END_EQP_NO = source.readString();
		END_EQP_NM = source.readString();
		CWRK_NM = source.readString();
		TINS_RESULT_SECD = source.readString();
		TINS_RESULT = source.readString();
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
		arg0.writeString(RGT_SN);
		arg0.writeString(BEGIN_EQP_NO);
		arg0.writeString(BEGIN_EQP_NM);
		arg0.writeString(END_EQP_NO);
		arg0.writeString(END_EQP_NM);
		arg0.writeString(CWRK_NM);
	}

	public  static final Parcelable.Creator<YBInfo> CREATOR
	= new Parcelable.Creator<YBInfo>() {

		@Override
		public YBInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new YBInfo(source);
		}

		@Override
		public YBInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new YBInfo[size];
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
		public static final String RGT_SN = "RGT_SN";
		public static final String BEGIN_EQP_NO = "BEGIN_EQP_NO";
		public static final String BEGIN_EQP_NM = "BEGIN_EQP_NM";
		public static final String END_EQP_NO = "END_EQP_NO";
		public static final String END_EQP_NM = "END_EQP_NM";
		public static final String CWRK_NM = "CWRK_NM";
		public static final String TINS_RESULT_SECD = "TINS_RESULT_SECD";
		public static final String TINS_RESULT = "TINS_RESULT";

	}
	
}
