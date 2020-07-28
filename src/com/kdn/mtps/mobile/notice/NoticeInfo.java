package com.kdn.mtps.mobile.notice;

import java.util.ArrayList;

import com.kdn.mtps.mobile.facility.FacilityInfo;

import android.os.Parcel;
import android.os.Parcelable;

public class NoticeInfo implements Parcelable{
	public String SEND_DATE;
	public String SEND_NAME;	
	public String TOP_YN;
	public String USE_YN;			
	public String CATEGORY_NAME;	
	public String BOARD_IDX;		
	public String BOARD_TITLE;	
	public String BOARD_CONT;
	public int IDX;
	public String READ_YN;
	
	public NoticeInfo() {
		
	}
	
	private NoticeInfo(Parcel source) {
		SEND_DATE = source.readString();
		SEND_NAME = source.readString();
		TOP_YN = source.readString();
		USE_YN = source.readString();
		CATEGORY_NAME = source.readString();
		BOARD_IDX = source.readString();
		BOARD_TITLE = source.readString();
		BOARD_CONT = source.readString();
		IDX = source.readInt();
		READ_YN = source.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(SEND_DATE);
		arg0.writeString(SEND_NAME);
		arg0.writeString(TOP_YN);
		arg0.writeString(USE_YN);
		arg0.writeString(CATEGORY_NAME);
		arg0.writeString(BOARD_IDX);
		arg0.writeString(BOARD_TITLE);
		arg0.writeString(BOARD_CONT);
		arg0.writeInt(IDX);
		arg0.writeString(READ_YN);
		
	}
	
	public  static final Parcelable.Creator<NoticeInfo> CREATOR
	= new Parcelable.Creator<NoticeInfo>() {

		@Override
		public NoticeInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new NoticeInfo(source);
		}

		@Override
		public NoticeInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new NoticeInfo[size];
		}
	};

	public String getSEND_DATE() {
		return SEND_DATE;
	}

	public void setSEND_DATE(String sEND_DATE) {
		SEND_DATE = sEND_DATE;
	}

	public String getSEND_NAME() {
		return SEND_NAME;
	}

	public void setSEND_NAME(String sEND_NAME) {
		SEND_NAME = sEND_NAME;
	}

	public String getTOP_YN() {
		return TOP_YN;
	}

	public void setTOP_YN(String tOP_YN) {
		TOP_YN = tOP_YN;
	}

	public String getUSE_YN() {
		return USE_YN;
	}

	public void setUSE_YN(String uSE_YN) {
		USE_YN = uSE_YN;
	}

	public String getCATEGORY_NAME() {
		return CATEGORY_NAME;
	}

	public void setCATEGORY_NAME(String cATEGORY_NAME) {
		CATEGORY_NAME = cATEGORY_NAME;
	}

	public String getBOARD_IDX() {
		return BOARD_IDX;
	}

	public void setBOARD_IDX(String bOARD_IDX) {
		BOARD_IDX = bOARD_IDX;
	}

	public String getBOARD_TITLE() {
		return BOARD_TITLE;
	}

	public void setBOARD_TITLE(String bOARD_TITLE) {
		BOARD_TITLE = bOARD_TITLE;
	}

	public String getBOARD_CONT() {
		return BOARD_CONT;
	}

	public void setBOARD_CONT(String bOARD_CONT) {
		BOARD_CONT = bOARD_CONT;
	}


	public int getIDX() {
		return IDX;
	}

	public void setIDX(int iDX) {
		IDX = iDX;
	}

	public String getREAD_YN() {
		return READ_YN;
	}

	public void setREAD_YN(String rEAD_YN) {
		READ_YN = rEAD_YN;
	}


	public static class COLS {
		public static final String IDX = "IDX";
		public static final String BOARD_IDX = "BOARD_IDX";
		public static final String BOARD_TITLE = "BOARD_TITLE";
		public static final String BOARD_CONT = "BOARD_CONT";
		public static final String SEND_NAME = "SEND_NAME";
		public static final String SEND_DATE = "SEND_DATE";
		public static final String CATEGORY_NAME = "CATEGORY_NAME";
		public static final String TOP_YN = "TOP_YN";
		public static final String USE_YN = "USE_YN";
		public static final String READ_YN = "READ_YN";
	}
}
