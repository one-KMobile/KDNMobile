package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class BRSubInfo implements Parcelable {
	public int IDX;
	public String TOWER_IDX;
	public String INSR_EQP_NO;
	public String PHS_SECD_NM;
	public String PHS_SECD;
	public String INSBTY_LFT;
	public String INSBTY_RIT;
	public String TY_SECD_NM;
	public String TY_SECD;
	public String CL_NM;
	public String CL_NO;
	public String INS_CNT;
	public String EQP_NO;
	
	
	public BRSubInfo() {
	}
	
	private BRSubInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		IDX = source.readInt();
		TOWER_IDX  = source.readString();
		INSR_EQP_NO  = source.readString();
		PHS_SECD_NM  = source.readString();
		PHS_SECD = source.readString();
		INSBTY_LFT  = source.readString();
		INSBTY_RIT  = source.readString();
		TY_SECD_NM  = source.readString();
		TY_SECD  = source.readString();
		CL_NM  = source.readString();
		CL_NO  = source.readString();
		INS_CNT  = source.readString();
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
		arg0.writeString(INSR_EQP_NO);
		arg0.writeString(PHS_SECD_NM);
		arg0.writeString(PHS_SECD);
		arg0.writeString(INSBTY_LFT);
		arg0.writeString(INSBTY_RIT);
		arg0.writeString(TY_SECD_NM);
		arg0.writeString(TY_SECD);
		arg0.writeString(CL_NM);
		arg0.writeString(CL_NO);
		arg0.writeString(INS_CNT);
		arg0.writeString(EQP_NO);
	}

	public  static final Parcelable.Creator<BRSubInfo> CREATOR
	= new Parcelable.Creator<BRSubInfo>() {

		@Override
		public BRSubInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new BRSubInfo(source);
		}

		@Override
		public BRSubInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new BRSubInfo[size];
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

	public String getINSR_EQP_NO() {
		return INSR_EQP_NO;
	}

	public void setINSR_EQP_NO(String iNSR_EQP_NO) {
		INSR_EQP_NO = iNSR_EQP_NO;
	}

	public String getPHS_SECD_NM() {
		return PHS_SECD_NM;
	}

	public void setPHS_SECD_NM(String pHS_SECD_NM) {
		PHS_SECD_NM = pHS_SECD_NM;
	}

	public String getINSBTY_LFT() {
		return INSBTY_LFT;
	}

	public void setINSBTY_LFT(String iNSBTY_LFT) {
		INSBTY_LFT = iNSBTY_LFT;
	}

	public String getINSBTY_RIT() {
		return INSBTY_RIT;
	}

	public void setINSBTY_RIT(String iNSBTY_RIT) {
		INSBTY_RIT = iNSBTY_RIT;
	}

	public String getTY_SECD_NM() {
		return TY_SECD_NM;
	}

	public void setTY_SECD_NM(String tY_SECD_NM) {
		TY_SECD_NM = tY_SECD_NM;
	}

	public String getTY_SECD() {
		return TY_SECD;
	}

	public void setTY_SECD(String tY_SECD) {
		TY_SECD = tY_SECD;
	}

	public String getCL_NM() {
		return CL_NM;
	}

	public void setCL_NM(String cL_NM) {
		CL_NM = cL_NM;
	}

	public String getCL_NO() {
		return CL_NO;
	}

	public void setCL_NO(String cL_NO) {
		CL_NO = cL_NO;
	}

	public String getINS_CNT() {
		return INS_CNT;
	}

	public void setINS_CNT(String iNS_CNT) {
		INS_CNT = iNS_CNT;
	}

	public String getPHS_SECD() {
		return PHS_SECD;
	}

	public void setPHS_SECD(String pHS_SECD) {
		PHS_SECD = pHS_SECD;
	}

	public static class COLS {
		public static final String IDX = "IDX";
		public static final String TOWER_IDX = "TOWER_IDX";
		public static final String INSR_EQP_NO = "INSR_EQP_NO";
		public static final String INSBTY_LFT = "INSBTY_LFT";
		public static final String INSBTY_RIT = "INSBTY_RIT";
		public static final String TY_SECD_NM = "TY_SECD_NM";
		public static final String TY_SECD = "TY_SECD";
		public static final String CL_NM = "CL_NM";
		public static final String CL_NO = "CL_NO";
		public static final String INS_CNT = "INS_CNT";
		public static final String EQP_NO = "EQP_NO";
		public static final String PHS_SECD_NM = "PHS_SECD_NM";
		public static final String PHS_SECD = "PHS_SECD";
	}
}
