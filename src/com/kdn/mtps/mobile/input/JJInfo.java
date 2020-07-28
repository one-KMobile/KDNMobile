package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class JJInfo implements Parcelable {
	public int idx;
	public String master_idx;
	public String weather;
	public String ground;
	public String span_length;
	public String check_result;
	public String remarks;
	public String terminal1_1;
	public String terminal1_2;
	public String terminal1_3;
	public String terminal1_5;
	public String terminal1_10;
	public String count_1;
	
	public JJInfo() {
	}
	
	private JJInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		idx = source.readInt();
		master_idx = source.readString();
		weather = source.readString();
		ground = source.readString();
		span_length = source.readString();
		check_result = source.readString();
		remarks = source.readString();
		terminal1_1 = source.readString();
		terminal1_2 = source.readString();
		terminal1_3 = source.readString();
		terminal1_5 = source.readString();
		terminal1_10 = source.readString();
		count_1 = source.readString();
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
		arg0.writeString(ground);
		arg0.writeString(span_length);
		arg0.writeString(check_result);
		arg0.writeString(remarks);
		arg0.writeString(terminal1_1);
		arg0.writeString(terminal1_2);
		arg0.writeString(terminal1_3);
		arg0.writeString(terminal1_5);
		arg0.writeString(terminal1_10);
		arg0.writeString(count_1);
	}

	public  static final Parcelable.Creator<JJInfo> CREATOR
	= new Parcelable.Creator<JJInfo>() {

		@Override
		public JJInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new JJInfo(source);
		}

		@Override
		public JJInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new JJInfo[size];
		}
	};

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
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

	public String getGround() {
		return ground;
	}

	public void setGround(String ground) {
		this.ground = ground;
	}

	public String getSpan_length() {
		return span_length;
	}

	public void setSpan_length(String span_length) {
		this.span_length = span_length;
	}

	public String getCheck_result() {
		return check_result;
	}

	public void setCheck_result(String check_result) {
		this.check_result = check_result;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTerminal1_1() {
		return terminal1_1;
	}

	public void setTerminal1_1(String terminal1_1) {
		this.terminal1_1 = terminal1_1;
	}

	public String getTerminal1_2() {
		return terminal1_2;
	}

	public void setTerminal1_2(String terminal1_2) {
		this.terminal1_2 = terminal1_2;
	}

	public String getTerminal1_3() {
		return terminal1_3;
	}

	public void setTerminal1_3(String terminal1_3) {
		this.terminal1_3 = terminal1_3;
	}

	public String getTerminal1_5() {
		return terminal1_5;
	}

	public void setTerminal1_5(String terminal1_5) {
		this.terminal1_5 = terminal1_5;
	}

	public String getTerminal1_10() {
		return terminal1_10;
	}

	public void setTerminal1_10(String terminal1_10) {
		this.terminal1_10 = terminal1_10;
	}

	
	public String getCount_1() {
		return count_1;
	}

	public void setCount_1(String count_1) {
		this.count_1 = count_1;
	}


	public static class COLS {
		public static final String IDX = "IDX";
		public static final String MASTER_IDX = "MASTER_IDX";
		public static final String WEATHER = "WEATHER";
		public static final String GROUND = "GROUND";
		public static final String SPAN_LENGTH = "SPAN_LENGTH";
		public static final String CHECK_RESULT = "CHECK_RESULT";
		public static final String REMARKS = "REMARKS";
		public static final String TERMINAL1_1 = "TERMINAL1_1";
		public static final String TERMINAL1_2 = "TERMINAL1_2";
		public static final String TERMINAL1_3 = "TERMINAL1_3";
		public static final String TERMINAL1_5 = "TERMINAL1_5";
		public static final String TERMINAL1_10 = "TERMINAL1_10";
		public static final String COUNT_1 = "COUNT_1";
		
	}
}
