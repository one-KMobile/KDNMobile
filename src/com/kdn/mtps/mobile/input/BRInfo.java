package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class BRInfo implements Parcelable {
	public int idx;
	public String masterIdx = "";
	public String weather = "";
	public String circuit_no = "";
	public String circuit_name = "";
	public String ej_cnt = "";
	public String br_cnt = "";
	public String make_date = "";
	public String make_company = "";
	public String yb_result = "";
	public String ty_secd = "";
	public String ty_secd_nm = "";
	public String phs_secd = "";
	public String phs_secd_nm = "";
	public String insbty_lft = "";
	public String insbty_rit = "";
	public String insr_eqp_no = "";

	public BRInfo() {
		
	}
	
	private BRInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		idx = source.readInt();
		masterIdx = source.readString();
		weather = source.readString();
		circuit_no = source.readString();
		circuit_name = source.readString();
		ej_cnt = source.readString();
		br_cnt = source.readString();
		make_date = source.readString();
		make_company = source.readString();
		yb_result = source.readString();
		ty_secd = source.readString();
		ty_secd_nm = source.readString();
		phs_secd = source.readString();
		phs_secd_nm = source.readString();
		insbty_lft = source.readString();
		insbty_rit = source.readString();
		insr_eqp_no = source.readString();
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
		arg0.writeString(circuit_no);
		arg0.writeString(circuit_name);
		arg0.writeString(ej_cnt);
		arg0.writeString(br_cnt);
		arg0.writeString(make_date);
		arg0.writeString(make_company);
		arg0.writeString(yb_result);
		arg0.writeString(ty_secd);
		arg0.writeString(ty_secd_nm);
		arg0.writeString(phs_secd);
		arg0.writeString(phs_secd_nm);
		arg0.writeString(insbty_lft);
		arg0.writeString(insbty_rit);
		arg0.writeString(insr_eqp_no);
	}

	public  static final Parcelable.Creator<BRInfo> CREATOR
	= new Parcelable.Creator<BRInfo>() {

		@Override
		public BRInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new BRInfo(source);
		}

		@Override
		public BRInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new BRInfo[size];
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
	
	public String getCircuit_name() {
		return circuit_name;
	}

	public void setCircuit_name(String circuit_name) {
		this.circuit_name = circuit_name;
	}

	public String getEj_cnt() {
		return ej_cnt;
	}

	public void setEj_cnt(String ej_cnt) {
		this.ej_cnt = ej_cnt;
	}

	public String getBr_cnt() {
		return br_cnt;
	}

	public void setBr_cnt(String br_cnt) {
		this.br_cnt = br_cnt;
	}

	public String getMake_date() {
		return make_date;
	}

	public void setMake_date(String make_date) {
		this.make_date = make_date;
	}

	public String getMake_company() {
		return make_company;
	}

	public void setMake_company(String make_company) {
		this.make_company = make_company;
	}

	public String getYb_result() {
		return yb_result;
	}

	public void setYb_result(String yb_result) {
		this.yb_result = yb_result;
	}
	
	public String getCircuit_no() {
		return circuit_no;
	}

	public void setCircuit_no(String circuit_no) {
		this.circuit_no = circuit_no;
	}

	public String getTy_secd() {
		return ty_secd;
	}

	public void setTy_secd(String ty_secd) {
		this.ty_secd = ty_secd;
	}

	public String getPhs_secd() {
		return phs_secd;
	}

	public void setPhs_secd(String phs_secd) {
		this.phs_secd = phs_secd;
	}

	public String getPhs_secd_nm() {
		return phs_secd_nm;
	}

	public void setPhs_secd_nm(String phs_secd_nm) {
		this.phs_secd_nm = phs_secd_nm;
	}

	public String getInsbty_lft() {
		return insbty_lft;
	}

	public void setInsbty_lft(String insbty_lft) {
		this.insbty_lft = insbty_lft;
	}

	public String getInsbty_rit() {
		return insbty_rit;
	}

	public void setInsbty_rit(String insbty_rit) {
		this.insbty_rit = insbty_rit;
	}

	public String getInsr_eqp_no() {
		return insr_eqp_no;
	}

	public void setInsr_eqp_no(String insr_eqp_no) {
		this.insr_eqp_no = insr_eqp_no;
	}

	public String getTy_secd_nm() {
		return ty_secd_nm;
	}

	public void setTy_secd_nm(String ty_secd_nm) {
		this.ty_secd_nm = ty_secd_nm;
	}

	public static class COLS {
		public static final String IDX = "IDX";
		public static final String MASTER_IDX = "MASTER_IDX";
		public static final String WEATHER = "WEATHER";
		public static final String CIRCUIT_NO = "CIRCUIT_NO";
		public static final String CIRCUIT_NAME = "CIRCUIT_NAME";
		public static final String EJ_CNT = "EJ_CNT";
		public static final String BR_CNT = "BR_CNT";
		public static final String MAKE_DATE = "MAKE_DATE";
		public static final String MAKE_COMPANY = "MAKE_COMPANY";
		public static final String YB_RESULT = "YB_RESULT";
		public static final String TY_SECD = "TY_SECD";
		public static final String TY_SECD_NM = "TY_SECD_NM";
		public static final String PHS_SECD = "PHS_SECD";
		public static final String PHS_SECD_NM = "PHS_SECD_NM";
		public static final String INSBTY_LFT = "INSBTY_LFT";
		public static final String INSBTY_RIT = "INSBTY_RIT";
		public static final String INSR_EQP_NO = "INSR_EQP_NO";

	}
	
}
