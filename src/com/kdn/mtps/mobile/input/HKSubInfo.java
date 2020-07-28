package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class HKSubInfo implements Parcelable {
	public int IDX;
	public String TOWER_IDX;
	public String SRCELCT_KND;
	public String SRCELCT_KND_NM;
	public String EQP_NO;
	public String FLIGHT_LMP_NO;
	public String FLIGHT_LMP_KND;
	public String FLIGHT_LMP_KND_NM;

	public HKSubInfo() {
	}
	
	private HKSubInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		IDX = source.readInt();
		TOWER_IDX = source.readString();
		SRCELCT_KND = source.readString();
		SRCELCT_KND_NM = source.readString();
		EQP_NO = source.readString();
		FLIGHT_LMP_NO = source.readString();
		FLIGHT_LMP_KND = source.readString();
		FLIGHT_LMP_KND_NM = source.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(IDX);
		
		arg0.writeString(TOWER_IDX);
		arg0.writeString(SRCELCT_KND);
		arg0.writeString(SRCELCT_KND_NM);
		arg0.writeString(EQP_NO);
		arg0.writeString(FLIGHT_LMP_NO);
		arg0.writeString(FLIGHT_LMP_KND);
		arg0.writeString(FLIGHT_LMP_KND_NM);		
	}

	public  static final Parcelable.Creator<HKSubInfo> CREATOR
	= new Parcelable.Creator<HKSubInfo>() {

		@Override
		public HKSubInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new HKSubInfo(source);
		}

		@Override
		public HKSubInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new HKSubInfo[size];
		}
	};

	public int getIDX() {
		return IDX;
	}

	public void setIDX(int iDX) {
		IDX = iDX;
	}

	public String getEQP_NO() {
		return EQP_NO;
	}

	public void setEQP_NO(String eQP_NO) {
		EQP_NO = eQP_NO;
	}

	public String getTOWER_IDX() {
		return TOWER_IDX;
	}

	public void setTOWER_IDX(String tOWER_IDX) {
		TOWER_IDX = tOWER_IDX;
	}

	public String getSRCELCT_KND() {
		return SRCELCT_KND;
	}

	public void setSRCELCT_KND(String sRCELCT_KND) {
		SRCELCT_KND = sRCELCT_KND;
	}

	public String getSRCELCT_KND_NM() {
		return SRCELCT_KND_NM;
	}

	public void setSRCELCT_KND_NM(String sRCELCT_KND_NM) {
		SRCELCT_KND_NM = sRCELCT_KND_NM;
	}

	public String getFLIGHT_LMP_NO() {
		return FLIGHT_LMP_NO;
	}

	public void setFLIGHT_LMP_NO(String fLIGHT_LMP_NO) {
		FLIGHT_LMP_NO = fLIGHT_LMP_NO;
	}

	public String getFLIGHT_LMP_KND() {
		return FLIGHT_LMP_KND;
	}

	public void setFLIGHT_LMP_KND(String fLIGHT_LMP_KND) {
		FLIGHT_LMP_KND = fLIGHT_LMP_KND;
	}

	public String getFLIGHT_LMP_KND_NM() {
		return FLIGHT_LMP_KND_NM;
	}

	public void setFLIGHT_LMP_KND_NM(String fLIGHT_LMP_KND_NM) {
		FLIGHT_LMP_KND_NM = fLIGHT_LMP_KND_NM;
	}

	public static class COLS {
		public static final String IDX = "IDX";
		public static final String TOWER_IDX = "TOWER_IDX";
		public static final String SRCELCT_KND = "SRCELCT_KND";
		public static final String SRCELCT_KND_NM = "SRCELCT_KND_NM";
		public static final String EQP_NO = "EQP_NO";
		public static final String FLIGHT_LMP_NO = "FLIGHT_LMP_NO";
		public static final String FLIGHT_LMP_KND = "FLIGHT_LMP_KND";
		public static final String FLIGHT_LMP_KND_NM = "FLIGHT_LMP_KND_NM";
	}
}
