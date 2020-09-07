package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class JGInfo2 implements Parcelable {
	public int idx;
	public String master_idx;
	public String weather;
	public String area_temp;

	public String circuit_no = "";
	public String circuit_name = "";
	public String current_load = "";
	public String c1 = "";
	public String c2 = "";
	public String c3 = "";

	public String getCircuit_no() {
		return circuit_no;
	}

	public void setCircuit_no(String circuit_no) {
		this.circuit_no = circuit_no;
	}

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


	public JGInfo2() {
	}

	private JGInfo2(Parcel source) {
		// TODO Auto-generated constructor stub
		idx = source.readInt();
		master_idx = source.readString();
		weather = source.readString();
		area_temp = source.readString();

		circuit_name = source.readString();
		current_load = source.readString();
		c1 = source.readString();
		c2 = source.readString();
		c3 = source.readString();


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
		arg0.writeString(c1);
		arg0.writeString(c2);
		arg0.writeString(c3);


	}

	public  static final Creator<JGInfo2> CREATOR
	= new Creator<JGInfo2>() {

		@Override
		public JGInfo2 createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new JGInfo2(source);
		}

		@Override
		public JGInfo2[] newArray(int size) {
			// TODO Auto-generated method stub
			return new JGInfo2[size];
		}
	};

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
