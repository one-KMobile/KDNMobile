package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class JGUSubInfo implements Parcelable {
	public int IDX;
	public String FNCT_LC_NO;
	public String FNCT_LC_DTLS;
	public String EQP_NO;
	public String EQP_NM;
	public String UPTLVL_UPLMT;
	public String UPTLVL_LWLT;
	public String UPTLVL_INTRCP;
	public String MNG_01;
	public String MNG_02;
	public String SD;

	public String TOWER_IDX;
	public String CONT_NUM;
	public String SN;
	public String TTM_LOAD;

	public JGUSubInfo() {
	}

	private JGUSubInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		IDX = source.readInt();
		EQP_NM  = source.readString();
		UPTLVL_UPLMT = source.readString();
		UPTLVL_LWLT = source.readString();
		UPTLVL_INTRCP = source.readString();
		MNG_01 = source.readString();
		MNG_02 = source.readString();
		SD = source.readString();

		TOWER_IDX  = source.readString();
		CONT_NUM  = source.readString();
		SN  = source.readString();
		TTM_LOAD  = source.readString();
		FNCT_LC_DTLS  = source.readString();
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
		arg0.writeString(FNCT_LC_NO);
		arg0.writeString(FNCT_LC_DTLS);
		arg0.writeString(EQP_NO);
		arg0.writeString(EQP_NM);
		arg0.writeString(UPTLVL_UPLMT);
		arg0.writeString(UPTLVL_LWLT);
		arg0.writeString(UPTLVL_INTRCP);
		arg0.writeString(MNG_01);
		arg0.writeString(MNG_02);
		arg0.writeString(SD);

		arg0.writeString(TOWER_IDX);
		arg0.writeString(CONT_NUM);
		arg0.writeString(SN);
		arg0.writeString(TTM_LOAD);

	}

	public  static final Creator<JGUSubInfo> CREATOR
	= new Creator<JGUSubInfo>() {

		@Override
		public JGUSubInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new JGUSubInfo(source);
		}

		@Override
		public JGUSubInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new JGUSubInfo[size];
		}
	};

	public int getIDX() {
		return IDX;
	}

	public void setIDX(int iDX) {
		IDX = iDX;
	}

	public String getEQP_NM() {
		return EQP_NM;
	}

	public void setEQP_NM(String EQP_NM) {
		EQP_NM = EQP_NM;
	}

	public String getUPTLVL_UPLMT() {
		return UPTLVL_UPLMT;
	}

	public void setUPTLVL_UPLMT(String UPTLVL_UPLMT) {
		EQP_NM = UPTLVL_UPLMT;
	}

	public String getUPTLVL_LWLT() {
		return UPTLVL_LWLT;
	}

	public void setUPTLVL_LWLT(String UPTLVL_LWLT) {
		UPTLVL_LWLT = UPTLVL_LWLT;
	}

	public String getUPTLVL_INTRCP() {
		return UPTLVL_INTRCP;
	}

	public void setUPTLVL_INTRCP(String UPTLVL_INTRCP) {
		UPTLVL_INTRCP = UPTLVL_INTRCP;
	}

	public String getMNG_01() {
		return MNG_01;
	}

	public void setMNG_01(String MNG_01) {
		MNG_01 = MNG_01;
	}

	public String getMNG_02() {
		return MNG_02;
	}

	public void setMNG_02(String MNG_02) {
		MNG_02 = MNG_02;
	}

	public String getSD() {
		return SD;
	}

	public void setSD(String SD) {
		SD = SD;
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

	public String getFNCT_LC_NO() {
		return FNCT_LC_NO;
	}

	public void setFNCT_LC_NO(String fNCT_LC_NO) {
		FNCT_LC_NO = fNCT_LC_NO;
	}


	public static class COLS {
		public static final String IDX = "IDX";
		public static final String FNCT_LC_NO = "FNCT_LC_NO";
		public static final String FNCT_LC_DTLS = "FNCT_LC_DTLS";
		public static final String EQP_NO = "EQP_NO";
		public static final String EQP_NM = "EQP_NM";
		public static final String UPTLVL_UPLMT = "UPTLVL_UPLMT";
		public static final String UPTLVL_LWLT = "UPTLVL_LWLT";
		public static final String UPTLVL_INTRCP = "UPTLVL_INTRCP";
		public static final String MNG_01 = "MNG_01";
		public static final String MNG_02 = "MNG_02";
		public static final String SD = "SD";

		public static final String TOWER_IDX = "TOWER_IDX";
		public static final String CONT_NUM = "CONT_NUM";
		public static final String SN = "SN";
		public static final String TTM_LOAD = "TTM_LOAD";
	}
}
