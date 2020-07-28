package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class KBInfo implements Parcelable {
	public int idx;
	public String master_idx;
	public String weather;
	public String item_fac_1;
	public String item_fac_2;
	public String item_fac_3;
	public String item_fac_4;
	public String item_fac_5;
	public String item_fac_6;
	public String item_fac_7;
	public String item_fac_8;
	public String item_fac_9;
	public String item_fac_10;
	public String item_fac_11;
	public String item_fac_12;
	public String item_sett_1;
	public String item_sett_2;
	public String item_sett_3;
	public String item_sett_4;
	public String item_etc_1;
	public String item_etc_2;
	
	public KBInfo() {
	}
	
	private KBInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		idx = source.readInt();
		master_idx = source.readString();
		weather = source.readString();
		item_fac_1 = source.readString();
		item_fac_2 = source.readString();
		item_fac_3 = source.readString();
		item_fac_4 = source.readString();
		item_fac_5 = source.readString();
		item_fac_6 = source.readString();
		item_fac_7 = source.readString();
		item_fac_8 = source.readString();
		item_fac_9 = source.readString();
		item_fac_10 = source.readString();
		item_fac_11 = source.readString();
		item_fac_12 = source.readString();
		item_sett_1 = source.readString();
		item_sett_2 = source.readString();
		item_sett_3 = source.readString();
		item_sett_4 = source.readString();
		item_etc_1 = source.readString();
		item_etc_2 = source.readString();
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
		arg0.writeString(item_fac_1);
		arg0.writeString(item_fac_2);
		arg0.writeString(item_fac_3);
		arg0.writeString(item_fac_4);
		arg0.writeString(item_fac_5);
		arg0.writeString(item_fac_6);
		arg0.writeString(item_fac_7);
		arg0.writeString(item_fac_8);
		arg0.writeString(item_fac_9);
		arg0.writeString(item_fac_10);
		arg0.writeString(item_fac_11);
		arg0.writeString(item_fac_12);
		arg0.writeString(item_sett_1);
		arg0.writeString(item_sett_2);
		arg0.writeString(item_sett_3);
		arg0.writeString(item_sett_4);
		arg0.writeString(item_etc_1);
		arg0.writeString(item_etc_2);
		
	}

	public  static final Parcelable.Creator<KBInfo> CREATOR
	= new Parcelable.Creator<KBInfo>() {

		@Override
		public KBInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new KBInfo(source);
		}

		@Override
		public KBInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new KBInfo[size];
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
	
	public String getItem_fac_1() {
		return item_fac_1;
	}

	public void setItem_fac_1(String item_fac_1) {
		this.item_fac_1 = item_fac_1;
	}

	public String getItem_fac_2() {
		return item_fac_2;
	}

	public void setItem_fac_2(String item_fac_2) {
		this.item_fac_2 = item_fac_2;
	}

	public String getItem_fac_3() {
		return item_fac_3;
	}

	public void setItem_fac_3(String item_fac_3) {
		this.item_fac_3 = item_fac_3;
	}

	public String getItem_fac_4() {
		return item_fac_4;
	}

	public void setItem_fac_4(String item_fac_4) {
		this.item_fac_4 = item_fac_4;
	}

	public String getItem_fac_5() {
		return item_fac_5;
	}

	public void setItem_fac_5(String item_fac_5) {
		this.item_fac_5 = item_fac_5;
	}

	public String getItem_fac_6() {
		return item_fac_6;
	}

	public void setItem_fac_6(String item_fac_6) {
		this.item_fac_6 = item_fac_6;
	}

	public String getItem_fac_7() {
		return item_fac_7;
	}

	public void setItem_fac_7(String item_fac_7) {
		this.item_fac_7 = item_fac_7;
	}

	public String getItem_fac_8() {
		return item_fac_8;
	}

	public void setItem_fac_8(String item_fac_8) {
		this.item_fac_8 = item_fac_8;
	}

	public String getItem_fac_9() {
		return item_fac_9;
	}

	public void setItem_fac_9(String item_fac_9) {
		this.item_fac_9 = item_fac_9;
	}

	public String getItem_fac_10() {
		return item_fac_10;
	}

	public void setItem_fac_10(String item_fac_10) {
		this.item_fac_10 = item_fac_10;
	}

	public String getItem_fac_11() {
		return item_fac_11;
	}

	public void setItem_fac_11(String item_fac_11) {
		this.item_fac_11 = item_fac_11;
	}

	public String getItem_fac_12() {
		return item_fac_12;
	}

	public void setItem_fac_12(String item_fac_12) {
		this.item_fac_12 = item_fac_12;
	}

	public String getItem_sett_1() {
		return item_sett_1;
	}

	public void setItem_sett_1(String item_sett_1) {
		this.item_sett_1 = item_sett_1;
	}

	public String getItem_sett_2() {
		return item_sett_2;
	}

	public void setItem_sett_2(String item_sett_2) {
		this.item_sett_2 = item_sett_2;
	}

	public String getItem_sett_3() {
		return item_sett_3;
	}

	public void setItem_sett_3(String item_sett_3) {
		this.item_sett_3 = item_sett_3;
	}

	public String getItem_sett_4() {
		return item_sett_4;
	}

	public void setItem_sett_4(String item_sett_4) {
		this.item_sett_4 = item_sett_4;
	}

	public String getItem_etc_1() {
		return item_etc_1;
	}

	public void setItem_etc_1(String item_etc_1) {
		this.item_etc_1 = item_etc_1;
	}

	public String getItem_etc_2() {
		return item_etc_2;
	}

	public void setItem_etc_2(String item_etc_2) {
		this.item_etc_2 = item_etc_2;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}


	public static class COLS {
		public static final String IDX = "IDX";
		public static final String MASTER_IDX = "MASTER_IDX";
		public static final String WEATHER = "WEATHER";
		public static final String ITEM_FAC_1 = "ITEM_FAC_1";
		public static final String ITEM_FAC_2 = "ITEM_FAC_2";
		public static final String ITEM_FAC_3 = "ITEM_FAC_3";
		public static final String ITEM_FAC_4 = "ITEM_FAC_4";
		public static final String ITEM_FAC_5 = "ITEM_FAC_5";
		public static final String ITEM_FAC_6 = "ITEM_FAC_6";
		public static final String ITEM_FAC_7 = "ITEM_FAC_7";
		public static final String ITEM_FAC_8 = "ITEM_FAC_8";
		public static final String ITEM_FAC_9 = "ITEM_FAC_9";
		public static final String ITEM_FAC_10 = "ITEM_FAC_10";
		public static final String ITEM_FAC_11 = "ITEM_FAC_11";
		public static final String ITEM_FAC_12 = "ITEM_FAC_12";
		public static final String ITEM_SETT_1 = "ITEM_SETT_1";
		public static final String ITEM_SETT_2 = "ITEM_SETT_2";
		public static final String ITEM_SETT_3 = "ITEM_SETT_3";
		public static final String ITEM_SETT_4 = "ITEM_SETT_4";
		public static final String ITEM_ETC_1 = "ITEM_ETC_1";
		public static final String ITEM_ETC_2 = "ITEM_ETC_2";
	}
}
