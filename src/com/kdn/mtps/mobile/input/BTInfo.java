package com.kdn.mtps.mobile.input;

import android.os.Parcel;
import android.os.Parcelable;

public class BTInfo implements Parcelable {
	public int idx;
	public String no;
	public String result;
	public String type;
	public String detail;
	public String result2;
	public String proceed;
	public String weather;
	public String worker_cnt;
	public String claim_content;
	public String check_result;
	public String etc;
	
	public BTInfo() {
		
	}
	
	private BTInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		idx = source.readInt();
		no = source.readString();
		result = source.readString();
		type = source.readString();
		detail = source.readString();
		result2 = source.readString();
		proceed = source.readString();
		weather = source.readString();
		worker_cnt = source.readString();
		claim_content = source.readString();
		check_result = source.readString();
		etc = source.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(idx);
		arg0.writeString(no);
		arg0.writeString(result);
		arg0.writeString(type);
		arg0.writeString(detail);
		arg0.writeString(result2);
		arg0.writeString(proceed);
		arg0.writeString(weather);
		arg0.writeString(worker_cnt);
		arg0.writeString(claim_content);
		arg0.writeString(check_result);
		arg0.writeString(etc);
	}

	public  static final Parcelable.Creator<BTInfo> CREATOR
	= new Parcelable.Creator<BTInfo>() {

		@Override
		public BTInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new BTInfo(source);
		}

		@Override
		public BTInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new BTInfo[size];
		}
	};

	
	
	public Integer getIdx() {
		return idx;
	}

	public void setIdx(Integer idx) {
		this.idx = idx;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getResult2() {
		return result2;
	}

	public void setResult2(String result2) {
		this.result2 = result2;
	}

	public String getProceed() {
		return proceed;
	}

	public void setProceed(String proceed) {
		this.proceed = proceed;
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

	public String getWorker_cnt() {
		return worker_cnt;
	}

	public void setWorker_cnt(String worker_cnt) {
		this.worker_cnt = worker_cnt;
	}

	public String getClaim_content() {
		return claim_content;
	}

	public void setClaim_content(String claim_content) {
		this.claim_content = claim_content;
	}

	public String getCheck_result() {
		return check_result;
	}

	public void setCheck_result(String check_result) {
		this.check_result = check_result;
	}
	
	
}
