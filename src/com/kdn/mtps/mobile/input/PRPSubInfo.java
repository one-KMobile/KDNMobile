package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class PRPSubInfo implements Parcelable {
	public int IDX;
	public String TOWER_IDX;
	public String CONT_NUM;
	public String SN;
	public String TTM_LOAD;
	public String FNCT_LC_DTLS;
	public String EQP_NM;
	public String FNCT_LC_NO;
	public String EQP_NO;

	public PRPSubInfo() {
	}

	private PRPSubInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		IDX = source.readInt();
		TOWER_IDX  = source.readString();
		CONT_NUM  = source.readString();
		SN  = source.readString();
		TTM_LOAD  = source.readString();
		FNCT_LC_DTLS  = source.readString();
		EQP_NM  = source.readString();
		FNCT_LC_NO  = source.readString();
		EQP_NO  = source.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(IDX);

		arg0.writeString(TOWER_IDX);
		arg0.writeString(CONT_NUM);
		arg0.writeString(SN);
		arg0.writeString(TTM_LOAD);
		arg0.writeString(FNCT_LC_DTLS);
		arg0.writeString(EQP_NM);
		arg0.writeString(FNCT_LC_NO);
		arg0.writeString(EQP_NO);

	}

	public  static final Creator<PRPSubInfo> CREATOR
	= new Creator<PRPSubInfo>() {

		@Override
		public PRPSubInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new PRPSubInfo(source);
		}

		@Override
		public PRPSubInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new PRPSubInfo[size];
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

	public String getCONT_NUM() {
		return CONT_NUM;
	}

	public void setCONT_NUM(String cONT_NUM) {
		CONT_NUM = cONT_NUM;
	}

	public String getSN() {
		return SN;
	}

	public void setSN(String sN) {
		SN = sN;
	}

	public String getTTM_LOAD() {
		return TTM_LOAD;
	}

	public void setTTM_LOAD(String tTM_LOAD) {
		TTM_LOAD = tTM_LOAD;
	}

	public String getFNCT_LC_DTLS() {
		return FNCT_LC_DTLS;
	}

	public void setFNCT_LC_DTLS(String fNCT_LC_DTLS) {
		FNCT_LC_DTLS = fNCT_LC_DTLS;
	}

	public String getEQP_NM() {
		return EQP_NM;
	}

	public void setEQP_NM(String eQP_NM) {
		EQP_NM = eQP_NM;
	}

	public String getFNCT_LC_NO() {
		return FNCT_LC_NO;
	}

	public void setFNCT_LC_NO(String fNCT_LC_NO) {
		FNCT_LC_NO = fNCT_LC_NO;
	}


	public static class COLS {
		public static final String IDX = "IDX";
		public static final String TOWER_IDX = "TOWER_IDX";
		public static final String CONT_NUM = "CONT_NUM";
		public static final String SN = "SN";
		public static final String TTM_LOAD = "TTM_LOAD";
		public static final String FNCT_LC_DTLS = "FNCT_LC_DTLS";
		public static final String EQP_NM = "EQP_NM";
		public static final String FNCT_LC_NO = "FNCT_LC_NO";
		public static final String EQP_NO = "EQP_NO";
	}
}
