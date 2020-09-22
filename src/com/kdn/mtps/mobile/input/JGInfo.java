package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class JGInfo implements Parcelable {
	public int idx;
	public String master_idx;
	public String weather;
	public String area_temp;

	public String fnct_lc_no = "";
	public String fnct_lc_dtls = "";
	public String eqp_no = "";
	public String eqp_nm = "";
	public String uptlvl_uplmt = "";
	public String uptlvl_lwlt = "";
	public String uptlvl_intrcp = "";
	public String mng_01 = "";
	public String mng_02 = "";
	public String sd = "";
	public String claim_content;
	public String t_gubun = "";
	public String t1_c1 = "";
	public String t1_c2 = "";
	public String t1_c3 = "";
	public String t1_c4 = "";
	public String t2_c1 = "";
	public String t2_c2 = "";
	public String t2_c3 = "";

	public String getfnct_lc_no() {
		return fnct_lc_no;
	}

	public void setfnct_lc_no(String fnct_lc_no) {
		this.fnct_lc_no = fnct_lc_no;
	}

	public JGInfo() {
	}

	private JGInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		idx = source.readInt();
		master_idx = source.readString();
		weather = source.readString();
		area_temp = source.readString();
		claim_content = source.readString();
		t_gubun = source.readString();

		fnct_lc_no = source.readString();
		fnct_lc_dtls = source.readString();
		eqp_no = source.readString();
		eqp_nm = source.readString();
		uptlvl_uplmt = source.readString();
		uptlvl_lwlt = source.readString();
		uptlvl_intrcp = source.readString();
		mng_01 = source.readString();
		mng_02 = source.readString();
		sd = source.readString();

		t1_c1 = source.readString();
		t1_c2 = source.readString();
		t1_c3 = source.readString();
		t1_c4 = source.readString();
		t2_c1 = source.readString();
		t2_c2 = source.readString();
		t2_c3 = source.readString();


	}

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(idx);
		arg0.writeString(master_idx);
		arg0.writeString(weather);
		arg0.writeString(area_temp);
		arg0.writeString(claim_content);
		arg0.writeString(t_gubun);
		arg0.writeString(fnct_lc_dtls);
		arg0.writeString(eqp_no);
		arg0.writeString(eqp_nm);
		arg0.writeString(uptlvl_uplmt);
		arg0.writeString(uptlvl_lwlt);
		arg0.writeString(uptlvl_intrcp);
		arg0.writeString(mng_01);
		arg0.writeString(mng_02);
		arg0.writeString(sd);
		arg0.writeString(t1_c1);
		arg0.writeString(t1_c2);
		arg0.writeString(t1_c3);
		arg0.writeString(t1_c4);
		arg0.writeString(t2_c1);
		arg0.writeString(t2_c2);
		arg0.writeString(t2_c3);


	}

	public  static final Creator<JGInfo> CREATOR
	= new Creator<JGInfo>() {

		@Override
		public JGInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new JGInfo(source);
		}

		@Override
		public JGInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new JGInfo[size];
		}
	};

	
	
	public Integer getIdx() {
		return idx;
	}

	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	
	public String getMaster_idx() {
		return master_idx;
	}

	public void setMaster_idx(String master_idx) {
		this.master_idx = master_idx;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getArea_temp() {
		return area_temp;
	}

	public void setArea_temp(String area_temp) {
		this.area_temp = area_temp;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}
	
	public String getEqp_no() {
		return eqp_no;
	}

	public void setEqp_no(String eqp_no) {
		this.eqp_no = eqp_no;
	}

	public String getEqp_nm() {
		return eqp_nm;
	}

	public void setEqp_nm(String eqp_nm) {
		this.eqp_nm = eqp_nm;
	}

	public String getT1_C1() {
		return t1_c1;
	}

	public void setT1_C1(String t1_c1) {
		this.t1_c1 = t1_c1;
	}

	public String getT1_C2() {
		return t1_c2;
	}

	public void setT1_C2(String t1_c2) {
		this.t1_c2 = t1_c2;
	}

	public String getT1_C3() {
		return t1_c3;
	}

	public void setT1_C3(String t1_c3) {
		this.t1_c3 = t1_c3;
	}

	public String getT1_C4() {
		return t1_c4;
	}

	public void setT1_C4(String t1_c4) {
		this.t1_c4 = t1_c4;
	}

	public String getT2_C1() {
		return t2_c1;
	}

	public void setT2_C1(String t2_c1) {
		this.t2_c1 = t2_c1;
	}

	public String getT2_C2() {
		return t2_c2;
	}

	public void setT2_C2(String t2_c2) {
		this.t2_c2 = t2_c2;
	}

	public String getT2_C3() {
		return t2_c3;
	}

	public void setT2_C3(String t2_c3) {
		this.t2_c3 = t2_c3;
	}

	public String getClaim_content() {
		return claim_content;
	}

	public void setClaim_content(String claim_content) {
		this.claim_content = claim_content;
	}


	public static class COLS {
		public static final String IDX = "IDX";
		public static final String MASTER_IDX = "MASTER_IDX";
		public static final String WEATHER = "WEATHER";
		public static final String AREA_TEMP = "AREA_TEMP";
		public static final String CLAIM_CONTENT = "CLAIM_CONTENT";
		public static final String T_GUBUN = "T_GUBUN";
		
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
		public static final String T1_C1 = "T1_C1";
		public static final String T1_C2 = "T1_C2";
		public static final String T1_C3 = "T1_C3";
		public static final String T1_C4 = "T1_C4";
		public static final String T2_C1 = "T2_C1";
		public static final String T2_C2 = "T2_C2";
		public static final String T2_C3 = "T2_C3";
		
	}
}
