package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class JGSubInfo2 implements Parcelable {
	public int IDX;
	public String TOWER_IDX;
	public String FNCT_LC_DTLS;
	public String FNCT_LC_NO;

	public JGSubInfo2() {
	}

	private JGSubInfo2(Parcel source) {
		// TODO Auto-generated constructor stub
		IDX = source.readInt();
		TOWER_IDX  = source.readString();
		FNCT_LC_DTLS  = source.readString();
		FNCT_LC_NO  = source.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(IDX);

		arg0.writeString(TOWER_IDX);
		arg0.writeString(FNCT_LC_DTLS);
		arg0.writeString(FNCT_LC_NO);

	}

	public  static final Creator<JGSubInfo2> CREATOR
	= new Creator<JGSubInfo2>() {

		@Override
		public JGSubInfo2 createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new JGSubInfo2(source);
		}

		@Override
		public JGSubInfo2[] newArray(int size) {
			// TODO Auto-generated method stub
			return new JGSubInfo2[size];
		}
	};

	public int getIDX() {
		return IDX;
	}

	public void setIDX(int iDX) {
		IDX = iDX;
	}

	public String getTOWER_IDX() {
		return TOWER_IDX;
	}

	public void setTOWER_IDX(String tOWER_IDX) {
		TOWER_IDX = tOWER_IDX;
	}

	public String getFNCT_LC_DTLS() {
		return FNCT_LC_DTLS;
	}

	public void setFNCT_LC_DTLS(String fNCT_LC_DTLS) {
		FNCT_LC_DTLS = fNCT_LC_DTLS;
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
		public static final String POWER_NO_C1 = "POWER_NO_C1";
		public static final String POWER_NO_C2 = "POWER_NO_C2";
		public static final String POWER_NO_C3 = "POWER_NO_C3";
	}
}
