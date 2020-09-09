package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class GHSubInfo implements Parcelable {
	public int IDX;
	public String TOWER_IDX;
	public String CONT_NUM;
	public String SN;
	public String TTM_LOAD;
	public String FNCT_LC_DTLS;
	public String EQP_NM;
	public String FNCT_LC_NO;
	public String EQP_NO;
	public String POWER_NO_C1;
	public String POWER_NO_C2;
	public String POWER_NO_C3;

	public GHSubInfo() {
	}

	private GHSubInfo(Parcel source) {
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
		POWER_NO_C1  = source.readString();
		POWER_NO_C2  = source.readString();
		POWER_NO_C3  = source.readString();
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
		arg0.writeString(POWER_NO_C1);
		arg0.writeString(POWER_NO_C2);
		arg0.writeString(POWER_NO_C3);

	}

	public  static final Creator<GHSubInfo> CREATOR
	= new Creator<GHSubInfo>() {

		@Override
		public GHSubInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new GHSubInfo(source);
		}

		@Override
		public GHSubInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new GHSubInfo[size];
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

	public String getPOWER_NO_C1() {
		return POWER_NO_C1;
	}

	public void setPOWER_NO_C1(String pOWER_NO_C1) {
		POWER_NO_C1 = pOWER_NO_C1;
	}

	public String getPOWER_NO_C2() {
		return POWER_NO_C2;
	}

	public void setPOWER_NO_C2(String pOWER_NO_C2) {
		POWER_NO_C2 = pOWER_NO_C2;
	}

	public String getPOWER_NO_C3() {
		return POWER_NO_C3;
	}

	public void setPOWER_NO_C3(String pOWER_NO_C3) {
		POWER_NO_C3 = pOWER_NO_C3;
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
		public static final String POWER_NO_C1 = "POWER_NO_C1";
		public static final String POWER_NO_C2 = "POWER_NO_C2";
		public static final String POWER_NO_C3 = "POWER_NO_C3";
	}
}
