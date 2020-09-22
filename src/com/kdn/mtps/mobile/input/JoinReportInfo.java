package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class JoinReportInfo implements Parcelable {
	public int idx;
	public String name;
	public String length;
	public String location;
	public String work_nm;
	public String join_date;
	public String request_date;
	public String join_s_date;
	public String join_s_time;
	public String join_e_date;
	public String join_e_time;
	public String request_joiner_co;
	public String request_joiner;
	public String joiner_dept;
	public String joiner;
	public String join_reason;
	public String etc;
	public String weather;

	public JoinReportInfo() {

	}

	private JoinReportInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		idx = source.readInt();
		name = source.readString();
		length = source.readString();
		location = source.readString();
		work_nm = source.readString();
		join_date = source.readString();
		request_date = source.readString();
		join_s_date = source.readString();
		join_s_time = source.readString();
		join_e_date = source.readString();
		join_e_time = source.readString();
		request_joiner_co = source.readString();
		request_joiner = source.readString();
		joiner_dept = source.readString();
		joiner = source.readString();
		join_reason = source.readString();
		etc = source.readString();
		weather = source.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(idx);
		arg0.writeString(name);
		arg0.writeString(length);
		arg0.writeString(location);
		arg0.writeString(work_nm);
		arg0.writeString(join_date);
		arg0.writeString(request_date);
		arg0.writeString(join_s_date);
		arg0.writeString(join_s_time);
		arg0.writeString(join_e_date);
		arg0.writeString(join_e_time);
		arg0.writeString(request_joiner_co);
		arg0.writeString(request_joiner);
		arg0.writeString(joiner_dept);
		arg0.writeString(joiner);
		arg0.writeString(join_reason);
		arg0.writeString(etc);
		arg0.writeString(weather);
	}

	public  static final Creator<JoinReportInfo> CREATOR
	= new Creator<JoinReportInfo>() {

		@Override
		public JoinReportInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new JoinReportInfo(source);
		}

		@Override
		public JoinReportInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new JoinReportInfo[size];
		}
	};

	
	
	public Integer getIdx() {
		return idx;
	}

	public void setIdx(Integer idx) {
		this.idx = idx;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getWork_nm() {
		return work_nm;
	}

	public void setWork_nm(String work_nm) {
		this.work_nm = work_nm;
	}

	public String getJoin_date() {
		return join_date;
	}

	public void seJjoin_date(String join_date) {
		this.join_date = join_date;
	}

	public String getRequest_date() {
		return request_date;
	}

	public void setRequest_date(String request_date) {
		this.request_date = request_date;
	}

	public String getJoin_s_date() {
		return join_s_date;
	}

	public void setJoin_s_date(String join_s_date) {
		this.join_s_date = join_s_date;
	}

	public String getJoin_s_time() {
		return join_s_time;
	}

	public void setJoin_s_time(String join_s_time) {
		this.join_s_time = join_s_time;
	}

	public String getJoin_e_date() {
		return join_e_date;
	}

	public void setJoin_e_date(String join_e_date) {
		this.join_e_date = join_e_date;
	}

	public String getJoin_e_time() {
		return join_e_time;
	}

	public void setJoin_e_time(String join_e_time) {
		this.join_e_time = join_e_time;
	}

	public String getRequest_joiner_co() {
		return request_joiner_co;
	}

	public void setRequest_joiner_co(String request_joiner_co) {
		this.request_joiner_co = request_joiner_co;
	}

	public String getRequest_joiner() {
		return request_joiner;
	}

	public void setRequest_joiner(String request_joiner) {
		this.request_joiner = request_joiner;
	}
	public String getJoiner_dept() {
		return joiner_dept;
	}

	public void setJoiner_dept(String joiner_dept) {
		this.joiner_dept = joiner_dept;
	}
	public String getJoiner() {
		return joiner;
	}

	public void setJoiner(String joiner) {
		this.joiner = joiner;
	}
	public String getJoin_reason() {
		return join_reason;
	}

	public void setJoin_reason(String join_reason) {
		this.join_reason = join_reason;
	}

	public String getEtc() {
		return etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}


	public static class COLS {
		public static final String IDX = "IDX";
		public static final String NAME = "NAME";
		public static final String LENGTH = "LENGTH";
		public static final String LOCATION = "LOCATION";
		public static final String WORK_NM = "WORK_NM";
		public static final String JOIN_DATE = "JOIN_DATE";
		public static final String REQUEST_DATE = "REQUEST_DATE";
		public static final String JOIN_S_DATE = "JOIN_S_DATE";
		public static final String JOIN_S_TIME = "JOIN_S_TIME";
		public static final String JOIN_E_DATE = "JOIN_E_DATE";
		public static final String JOIN_E_TIME = "JOIN_E_TIME";
		public static final String REQUEST_JOINER_CO = "REQUEST_JOINER_CO";
		public static final String REQUEST_JOINER = "REQUEST_JOINER";
		public static final String JOINER_DEPT = "JOINER_DEPT";
		public static final String JOINER = "JOINER";
		public static final String JOIN_REASON = "JOIN_REASON";
		public static final String ETC = "ETC";
	}
}
