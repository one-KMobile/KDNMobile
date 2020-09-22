package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class MHInfo implements Parcelable {
	public int idx;
	public String master_idx;
	public String weather;
	public String area_temp;

	public String circuit_no = "";
	public String circuit_name = "";
	public String current_load = "";
	public String conductor_cnt = "";
	public String location = "";
	public String claim_content;
	public String t_gubun = "";
	public String t1_c1 = "";
	public String t1_c2 = "";
	public String t1_c3 = "";
	public String t1_c4 = "";
	public String t1_c5 = "";
	public String t1_c6 = "";
	public String t1_c7 = "";
	public String t1_c8 = "";
	public String t2_c1 = "";
	public String t2_c2 = "";
	public String t2_c3 = "";
	public String t2_c4 = "";
	public String t2_c5 = "";
	public String t2_c6 = "";
	public String t2_c7 = "";
	public String t2_c8 = "";
	public String g_gubun = "";
	public String t3_c1 = "";
	public String t3_c2 = "";
	public String t3_c3 = "";
	public String t3_c4 = "";
	public String t3_c5 = "";
	public String t3_c6 = "";
	public String t3_c7 = "";
	public String t3_c8 = "";
	public String t3_c9 = "";
	public String t3_c10 = "";

	public String getCircuit_no() {
		return circuit_no;
	}

	public void setCircuit_no(String circuit_no) {
		this.circuit_no = circuit_no;
	}

	public MHInfo() {
	}

	private MHInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		idx = source.readInt();
		master_idx = source.readString();
		weather = source.readString();
		area_temp = source.readString();

		circuit_name = source.readString();
		current_load = source.readString();
		conductor_cnt = source.readString();
		location = source.readString();
		claim_content = source.readString();
		t_gubun = source.readString();
		t1_c1 = source.readString();
		t1_c2 = source.readString();
		t1_c3 = source.readString();
		t1_c4 = source.readString();
		t1_c5 = source.readString();
		t1_c6 = source.readString();
		t1_c7 = source.readString();
		t1_c8 = source.readString();
		t2_c1 = source.readString();
		t2_c2 = source.readString();
		t2_c3 = source.readString();
		t2_c4 = source.readString();
		t2_c5 = source.readString();
		t2_c6 = source.readString();
		t2_c7 = source.readString();
		t2_c8 = source.readString();
		g_gubun = source.readString();
		t3_c1 = source.readString();
		t3_c2 = source.readString();
		t3_c3 = source.readString();
		t3_c4 = source.readString();
		t3_c5 = source.readString();
		t3_c6 = source.readString();
		t3_c7 = source.readString();
		t3_c8 = source.readString();
		t3_c9 = source.readString();
		t3_c10 = source.readString();

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
		arg0.writeString(circuit_name);
		arg0.writeString(current_load);
		arg0.writeString(conductor_cnt);
		arg0.writeString(location);
		arg0.writeString(claim_content);
		arg0.writeString(t_gubun);
		arg0.writeString(t1_c1);
		arg0.writeString(t1_c2);
		arg0.writeString(t1_c3);
		arg0.writeString(t1_c4);
		arg0.writeString(t1_c5);
		arg0.writeString(t1_c6);
		arg0.writeString(t1_c7);
		arg0.writeString(t1_c8);
		arg0.writeString(t2_c1);
		arg0.writeString(t2_c2);
		arg0.writeString(t2_c3);
		arg0.writeString(t2_c4);
		arg0.writeString(t2_c5);
		arg0.writeString(t2_c6);
		arg0.writeString(t2_c7);
		arg0.writeString(t2_c8);
		arg0.writeString(g_gubun);
		arg0.writeString(t3_c1);
		arg0.writeString(t3_c2);
		arg0.writeString(t3_c3);
		arg0.writeString(t3_c4);
		arg0.writeString(t3_c5);
		arg0.writeString(t3_c6);
		arg0.writeString(t3_c7);
		arg0.writeString(t3_c8);
		arg0.writeString(t3_c9);
		arg0.writeString(t3_c10);

	}

	public  static final Creator<MHInfo> CREATOR
	= new Creator<MHInfo>() {

		@Override
		public MHInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new MHInfo(source);
		}

		@Override
		public MHInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new MHInfo[size];
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
	
	public String getCurrent_load() {
		return current_load;
	}

	public void setCurrent_load(String current_load) {
		this.current_load = current_load;
	}

	public String getConductor_cnt() {
		return conductor_cnt;
	}

	public void setConductor_cnt(String conductor_cnt) {
		this.conductor_cnt = conductor_cnt;
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
		
		public static final String CIRCUIT_NO = "CIRCUIT_NO";
		public static final String CIRCUIT_NAME = "CIRCUIT_NAME";
		public static final String CURRENT_LOAD = "CURRENT_LOAD";
		public static final String CONDUCTOR_CNT = "CONDUCTOR_CNT";
		public static final String LOCATION = "LOCATION";
		public static final String CLAIM_CONTENT = "CLAIM_CONTENT";
		public static final String T_GUBUN = "T_GUBUN";
		public static final String T1_C1 = "T1_C1";
		public static final String T1_C2 = "T1_C2";
		public static final String T1_C3 = "T1_C3";
		public static final String T1_C4 = "T1_C4";
		public static final String T1_C5 = "T1_C5";
		public static final String T1_C6 = "T1_C6";
		public static final String T1_C7 = "T1_C7";
		public static final String T1_C8 = "T1_C8";
		public static final String T2_C1 = "T2_C1";
		public static final String T2_C2 = "T2_C2";
		public static final String T2_C3 = "T2_C3";
		public static final String T2_C4 = "T2_C4";
		public static final String T2_C5 = "T2_C5";
		public static final String T2_C6 = "T2_C6";
		public static final String T2_C7 = "T2_C7";
		public static final String T2_C8 = "T2_C8";
		public static final String G_GUBUN = "G_GUBUN";
		public static final String T3_C1 = "T3_C1";
		public static final String T3_C2 = "T3_C2";
		public static final String T3_C3 = "T3_C3";
		public static final String T3_C4 = "T3_C4";
		public static final String T3_C5 = "T3_C5";
		public static final String T3_C6 = "T3_C6";
		public static final String T3_C7 = "T3_C7";
		public static final String T3_C8 = "T3_C8";
		public static final String T3_C9 = "T3_C9";
		public static final String T3_C10 = "T3_C10";
		
	}
}
