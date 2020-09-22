package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class JBInfo implements Parcelable {
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
	public String t_no = "";
	public String t1_no = "";
	public String t1_c = "";
	public String t2_no = "";
	public String t2_c = "";
	public String t3_no = "";
	public String t3_c = "";
	public String inspect_no = "";

	public String getCircuit_no() {
		return circuit_no;
	}

	public void setCircuit_no(String circuit_no) {
		this.circuit_no = circuit_no;
	}

	public JBInfo() {
	}

	private JBInfo(Parcel source) {
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
		t_no = source.readString();
		t1_no = source.readString();
		t1_c = source.readString();
		t2_no = source.readString();
		t2_c = source.readString();
		t3_no = source.readString();
		t3_c = source.readString();
		inspect_no = source.readString();

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
		arg0.writeString(t_no);
		arg0.writeString(t1_no);
		arg0.writeString(t1_c);
		arg0.writeString(t2_no);
		arg0.writeString(t2_c);
		arg0.writeString(t3_no);
		arg0.writeString(t3_c);
		arg0.writeString(inspect_no);

	}

	public  static final Creator<JBInfo> CREATOR
	= new Creator<JBInfo>() {

		@Override
		public JBInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new JBInfo(source);
		}

		@Override
		public JBInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new JBInfo[size];
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
		public static final String T_NO = "T_NO";
		public static final String T1_NO = "T1_NO";
		public static final String T1_C = "T1_C";
		public static final String T2_NO = "T2_NO";
		public static final String T2_C = "T2_C";
		public static final String T3_NO = "T3_NO";
		public static final String T3_C = "T3_C";
		public static final String INSPECT_NO = "INSPECT_NO";
		
	}
}
