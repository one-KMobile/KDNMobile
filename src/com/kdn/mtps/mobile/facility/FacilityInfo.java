package com.kdn.mtps.mobile.facility;

import android.os.Parcel;
import android.os.Parcelable;

public class FacilityInfo implements Parcelable {
	
	public int IDX;
	public String TOWER_IDX;
	public String FNCT_LC_NO;
	public String FNCT_LC_DTLS;
	public String FNCT_LC_TY;
	public String EQP_TY_CD_NM;
	public String EQP_NO;
	public String EQP_NM;
	public String LATITUDE;
	public String LONGITUDE;
	public String ADDRESS;
	public String CONT_NUM;
	
	public FacilityInfo() {
		
	}
	
	private FacilityInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		IDX = source.readInt();
		TOWER_IDX = source.readString();
		FNCT_LC_NO = source.readString();
		FNCT_LC_DTLS = source.readString();
		FNCT_LC_TY = source.readString();
		EQP_TY_CD_NM = source.readString();
		EQP_NO = source.readString();
		EQP_NM = source.readString();
		LATITUDE = source.readString();
		LONGITUDE = source.readString();
		ADDRESS = source.readString();
		CONT_NUM = source.readString();
		
//		arrLocation = new ArrayList<String>();
//		source.readList(arrLocation, null);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(IDX);
		arg0.writeString(TOWER_IDX);
		arg0.writeString(FNCT_LC_NO);
		arg0.writeString(FNCT_LC_DTLS);
		arg0.writeString(FNCT_LC_TY);
		arg0.writeString(EQP_TY_CD_NM);
		arg0.writeString(EQP_NO);
		arg0.writeString(EQP_NM);
		arg0.writeString(LATITUDE);
		arg0.writeString(LONGITUDE);
		arg0.writeString(ADDRESS);
		arg0.writeString(CONT_NUM);
		
		
	}

	public  static final Parcelable.Creator<FacilityInfo> CREATOR
	= new Parcelable.Creator<FacilityInfo>() {

		@Override
		public FacilityInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new FacilityInfo(source);
		}

		@Override
		public FacilityInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new FacilityInfo[size];
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

	public String getEQP_TY_CD_NM() {
		return EQP_TY_CD_NM;
	}

	public void setEQP_TY_CD_NM(String eQP_TY_CD_NM) {
		EQP_TY_CD_NM = eQP_TY_CD_NM;
	}

	public String getEQP_NO() {
		return EQP_NO;
	}

	public void setEQP_NO(String eQP_NO) {
		EQP_NO = eQP_NO;
	}

	public String getEQP_NM() {
		return EQP_NM;
	}

	public void setEQP_NM(String eQP_NM) {
		EQP_NM = eQP_NM;
	}

	public String getLATITUDE() {
		return LATITUDE;
	}

	public void setLATITUDE(String lATITUDE) {
		LATITUDE = lATITUDE;
	}

	public String getLONGITUDE() {
		return LONGITUDE;
	}

	public void setLONGITUDE(String lONGITUDE) {
		LONGITUDE = lONGITUDE;
	}

	public String getADDRESS() {
		return ADDRESS;
	}

	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}

	public String getCONT_NUM() {
		return CONT_NUM;
	}

	public void setCONT_NUM(String cONT_NUM) {
		CONT_NUM = cONT_NUM;
	}


	public static class COLS {
		public static final String IDX = "IDX";
		public static final String TOWER_IDX = "TOWER_IDX";
		public static final String FNCT_LC_NO = "FNCT_LC_NO";
		public static final String FNCT_LC_DTLS = "FNCT_LC_DTLS";
		public static final String FNCT_LC_TY = "FNCT_LC_TY";
		public static final String EQP_TY_CD_NM = "EQP_TY_CD_NM";
		public static final String EQP_NO = "EQP_NO";
		public static final String EQP_NM = "EQP_NM";
		public static final String LATITUDE = "LATITUDE";
		public static final String LONGITUDE = "LONGITUDE";
		public static final String ADDRESS = "ADDRESS";
		public static final String CONT_NUM = "CONT_NUM";
		
	}
}
