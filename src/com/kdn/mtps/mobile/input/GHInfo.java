package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class GHInfo implements Parcelable {
	public int idx;
	public String master_idx;
	public String weather;
	public String area_temp;

	public String circuit_no = "";
	public String circuit_name = "";
	public String current_load = "";
	public String conductor_cnt = "";
	public String location = "";
	public String c1 = "";
	public String c2 = "";
	public String c3 = "";
	public String c4 = "";
	public String c5 = "";
	public String c6 = "";
	public String c7 = "";
	public String c8 = "";
	public String c9 = "";

	public String c1_js = "";
	public String c1_jsj = "";
	public String c1_yb_result = "";
	public String c1_power_no = "";
	public String c2_js = "";
	public String c2_jsj = "";
	public String c2_yb_result = "";
	public String c2_power_no = "";
	public String c3_js = "";
	public String c3_jsj = "";
	public String c3_yb_result = "";
	public String c3_power_no = "";
	public String claim_content;

	public String getCircuit_no() {
		return circuit_no;
	}

	public void setCircuit_no(String circuit_no) {
		this.circuit_no = circuit_no;
	}

	public String getC1_power_no() {
		return c1_power_no;
	}

	public void setC1_power_no(String c1_power_no) {
		this.c1_power_no = c1_power_no;
	}

	public String getC2_power_no() {
		return c2_power_no;
	}

	public void setC2_power_no(String c2_power_no) {
		this.c2_power_no = c2_power_no;
	}

	public String getC3_power_no() {
		return c3_power_no;
	}

	public void setC3_power_no(String c3_power_no) {
		this.c3_power_no = c3_power_no;
	}


	public GHInfo() {
	}

	private GHInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		idx = source.readInt();
		master_idx = source.readString();
		weather = source.readString();
		area_temp = source.readString();

		circuit_name = source.readString();
		current_load = source.readString();
		conductor_cnt = source.readString();
		location = source.readString();
		c1 = source.readString();
		c2 = source.readString();
		c3 = source.readString();
		c4 = source.readString();
		c5 = source.readString();
		c6 = source.readString();
		c7 = source.readString();
		c8 = source.readString();
		c9 = source.readString();
		c1_js = source.readString();
		c1_jsj = source.readString();
		c1_yb_result = source.readString();
		c2_js = source.readString();
		c2_jsj = source.readString();
		c2_yb_result = source.readString();
		c3_js = source.readString();
		c3_jsj = source.readString();
		c3_yb_result = source.readString();
		claim_content = source.readString();


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
		arg0.writeString(c1);
		arg0.writeString(c2);
		arg0.writeString(c3);
		arg0.writeString(c4);
		arg0.writeString(c5);
		arg0.writeString(c6);
		arg0.writeString(c7);
		arg0.writeString(c8);
		arg0.writeString(c9);
		arg0.writeString(c1_js);
		arg0.writeString(c1_jsj);
		arg0.writeString(c1_yb_result);
		arg0.writeString(c2_js);
		arg0.writeString(c2_jsj);
		arg0.writeString(c2_yb_result);
		arg0.writeString(c3_js);
		arg0.writeString(c3_jsj);
		arg0.writeString(c3_yb_result);
		arg0.writeString(claim_content);


	}

	public  static final Creator<GHInfo> CREATOR
	= new Creator<GHInfo>() {

		@Override
		public GHInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new GHInfo(source);
		}

		@Override
		public GHInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new GHInfo[size];
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

	public String getC1() {
		return c1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}

	public String getC2() {
		return c2;
	}

	public void setC2(String c2) {
		this.c2 = c2;
	}

	public String getC3() {
		return c3;
	}

	public void setC3(String c3) {
		this.c3 = c3;
	}

	public String getC4() {
		return c4;
	}

	public void setC4(String c4) {
		this.c4 = c4;
	}

	public String getC5() {
		return c5;
	}

	public void setC5(String c5) {
		this.c5 = c5;
	}

	public String getC6() {
		return c6;
	}

	public void setC6(String c6) {
		this.c6 = c6;
	}

	public String getC7() {	return c7; }

	public void setC7(String c7) {
		this.c7 = c7;
	}

	public String getC8() {
		return c8;
	}

	public void setC8(String c8) {
		this.c8 = c8;
	}

	public String getC9() {
		return c9;
	}

	public void setC9(String c9) {
		this.c9 = c9;
	}

	public String getC1_js() {
		return c1_js;
	}

	public void setC1_js(String c1_js) {
		this.c1_js = c1_js;
	}

	public String getC1_jsj() {
		return c1_jsj;
	}

	public void setC1_jsj(String c1_jsj) {
		this.c1_jsj = c1_jsj;
	}

	public String getC2_js() {
		return c2_js;
	}

	public void setC2_js(String c2_js) {
		this.c2_js = c2_js;
	}

	public String getC2_jsj() {
		return c2_jsj;
	}

	public void setC2_jsj(String c2_jsj) {
		this.c2_jsj = c2_jsj;
	}

	public String getC3_js() {
		return c3_js;
	}

	public void setC3_js(String c3_js) {
		this.c3_js = c3_js;
	}

	public String getC3_jsj() {
		return c3_jsj;
	}

	public void setC3_jsj(String c3_jsj) {
		this.c3_jsj = c3_jsj;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCircuit_name() {
		return circuit_name;
	}

	public void setCircuit_name(String circuit_name) {
		this.circuit_name = circuit_name;
	}

	public String getC1_yb_result() {
		return c1_yb_result;
	}

	public void setC1_yb_result(String c1_yb_result) {
		this.c1_yb_result = c1_yb_result;
	}

	public String getC2_yb_result() {
		return c2_yb_result;
	}

	public void setC2_yb_result(String c2_yb_result) {
		this.c2_yb_result = c2_yb_result;
	}

	public String getC3_yb_result() {
		return c3_yb_result;
	}

	public void setC3_yb_result(String c3_yb_result) {
		this.c3_yb_result = c3_yb_result;
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
		public static final String C1_JS = "C1_JS";
		public static final String C1_JSJ = "C1_JSJ";
		public static final String C1_YB_RESULT = "C1_YB_RESULT";
		public static final String C1_POWER_NO = "C1_POWER_NO";
		public static final String C2_JS = "C2_JS";
		public static final String C2_JSJ = "C2_JSJ";
		public static final String C2_YB_RESULT = "C2_YB_RESULT";
		public static final String C2_POWER_NO = "C2_POWER_NO";
		public static final String C3_JS = "C3_JS";
		public static final String C3_JSJ = "C3_JSJ";
		public static final String C3_YB_RESULT = "C3_YB_RESULT";
		public static final String C3_POWER_NO = "C3_POWER_NO";
		public static final String CLAIM_CONTENT = "CLAIM_CONTENT";
		
	}
}
