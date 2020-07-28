package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class HJInfo implements Parcelable {
	public int idx;
	public String masterIdx = "";
	public String weather = "";
	public String light_type = "";
	public String power = "";
	public String light_no = "";
	public String control = "";
	public String sun_battery = "";
	public String storage_battery = "";
	public String light_item = "";
	public String cable = "";
	public String yb_result = "";
	public String remarks = "";
	
	
	public HJInfo() {
		
	}
	
	private HJInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		idx = source.readInt();
		masterIdx = source.readString();
		weather = source.readString();
		light_type = source.readString();
		power = source.readString();
		light_no = source.readString();
		control = source.readString();
		sun_battery = source.readString();
		storage_battery = source.readString();
		light_item = source.readString();
		cable = source.readString();
		yb_result = source.readString();
		remarks = source.readString();
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
		arg0.writeString(light_type);
		arg0.writeString(power);
		arg0.writeString(light_no);
		arg0.writeString(control);
		arg0.writeString(sun_battery);
		arg0.writeString(storage_battery);
		arg0.writeString(light_item);
		arg0.writeString(cable);
		arg0.writeString(yb_result);
		arg0.writeString(remarks);
	}

	public  static final Parcelable.Creator<HJInfo> CREATOR
	= new Parcelable.Creator<HJInfo>() {

		@Override
		public HJInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new HJInfo(source);
		}

		@Override
		public HJInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new HJInfo[size];
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

	public String getLight_type() {
		return light_type;
	}

	public void setLight_type(String light_type) {
		this.light_type = light_type;
	}

	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public String getSun_battery() {
		return sun_battery;
	}

	public void setSun_battery(String sun_battery) {
		this.sun_battery = sun_battery;
	}

	public String getStorage_battery() {
		return storage_battery;
	}

	public void setStorage_battery(String storage_battery) {
		this.storage_battery = storage_battery;
	}

	public String getLight_item() {
		return light_item;
	}

	public void setLight_item(String light_item) {
		this.light_item = light_item;
	}

	public String getCable() {
		return cable;
	}

	public void setCable(String cable) {
		this.cable = cable;
	}

	public String getYb_result() {
		return yb_result;
	}

	public void setYb_result(String yb_result) {
		this.yb_result = yb_result;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getLight_no() {
		return light_no;
	}

	public void setLight_no(String light_no) {
		this.light_no = light_no;
	}


	public static class COLS {
		public static final String IDX = "IDX";
		public static final String MASTER_IDX = "MASTER_IDX";
		public static final String WEATHER = "WEATHER";
		public static final String LIGHT_TYPE = "LIGHT_TYPE";
		public static final String POWER = "POWER";
		public static final String LIGHT_NO = "LIGHT_NO";
		public static final String CONTROL = "CONTROL";
		public static final String SUN_BATTERY = "SUN_BATTERY";
		public static final String STORAGE_BATTERY = "STORAGE_BATTERY";
		public static final String LIGHT_ITEM = "LIGHT_ITEM";
		public static final String CABLE = "CABLE";
		public static final String YB_RESULT = "YB_RESULT";
		public static final String REMARKS = "REMARKS";
	}
	
}
