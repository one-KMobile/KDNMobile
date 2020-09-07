package com.kdn.mtps.mobile.inspect;

import android.os.Parcel;
import android.os.Parcelable;

public class InspectInfo implements Parcelable {
	public String master_idx = "";
	public String tracksName = "";
	public String towerNo = "";
	public String date = "";
	public String type = "";
	public String typeName = "";
	public String p1st = "";
	public String p2nd = "";
	public String latitude = "";
	public String longitude = "";
	public String nfcTagLatitude = "";
	public String nfcTagLongitude = "";
	public String status = "";
	public String eqpNm = "";
	public String eqpNo = "";
	public String has_bt = "";
	public String has_js = "";
	public String has_jj = "";
	public String has_kb = "";
	public String has_hk = "";
	public String has_hj = "";
	public String has_br = "";
	public String has_jp = "";
	//public String has_yb = "";
	public String complete_yn_bt = "";
	public String complete_yn_js = "";
	public String complete_yn_jj = "";
	public String complete_yn_kb = "";
	public String complete_yn_hk = "";
	public String complete_yn_hj = "";
	public String complete_yn_br = "";
	public String complete_yn_jp = "";
	public String send_yn_bt = "";
	public String send_yn_js = "";
	public String send_yn_jj = "";
	public String send_yn_kb = "";
	public String send_yn_hk = "";
	public String send_yn_hj = "";
	public String send_yn_br = "";
	public String send_yn_jp = "";
	public String unity_ins_no = "";
	public String nfc_tag_id = "";
	public String nfc_tag_yn = "";
	public String ins_input_date = "";
	public String ins_plan_no = "";
	public String ins_sn = "";
	public String fnct_lc_no = "";
	public String address = "";

/*지중순시 추가*/
	public String has_jg = "";
	public String has_yb = "";
	public String has_mh = "";
	public String has_gh = "";
	public String has_pr = "";
	public String has_jb = "";
	public String complete_yn_jg = "";
	public String complete_yn_yb = "";
	public String complete_yn_mh = "";
	public String complete_yn_gh = "";
	public String complete_yn_pr = "";
	public String complete_yn_jb = "";
	public String send_yn_jg = "";
	public String send_yn_yb = "";
	public String send_yn_mh = "";
	public String send_yn_gh = "";
	public String send_yn_pr = "";
	public String send_yn_jb = "";
	
	public InspectInfo() {
		
	}
	
	private InspectInfo(Parcel source) {
		// TODO Auto-generated constructor stub
		master_idx = source.readString();
		tracksName = source.readString();
		towerNo = source.readString();
		date = source.readString();
		type = source.readString();
		typeName = source.readString();
		p1st = source.readString();
		p2nd = source.readString();
		latitude = source.readString();
		longitude = source.readString();
		nfcTagLatitude = source.readString();
		nfcTagLongitude = source.readString();
		status = source.readString();
		eqpNm = source.readString();
		eqpNo = source.readString();
		has_bt = source.readString();
		has_js = source.readString();
		has_jj = source.readString();
		has_kb = source.readString();
		has_hk = source.readString();
		complete_yn_bt = source.readString();
		complete_yn_js = source.readString();
		complete_yn_jp = source.readString();
		complete_yn_jj = source.readString();
		complete_yn_kb = source.readString();
		complete_yn_hk = source.readString();
		complete_yn_hj = source.readString();
		complete_yn_br = source.readString();
		send_yn_bt = source.readString();
		send_yn_js = source.readString();
		send_yn_jp = source.readString();
		send_yn_jj = source.readString();
		send_yn_kb = source.readString();
		send_yn_hk = source.readString();
		send_yn_hj = source.readString();
		send_yn_br = source.readString();
		unity_ins_no = source.readString();
		nfc_tag_id = source.readString();
		nfc_tag_yn = source.readString();
		ins_input_date = source.readString();
		ins_plan_no = source.readString();
		ins_sn = source.readString();
		fnct_lc_no = source.readString();
		address = source.readString();

		has_jg = source.readString();
		has_yb = source.readString();
		has_mh = source.readString();
		has_gh = source.readString();
		has_pr = source.readString();
		has_jb = source.readString();
		complete_yn_jg = source.readString();
		complete_yn_yb = source.readString();
		complete_yn_mh = source.readString();
		complete_yn_gh = source.readString();
		complete_yn_pr = source.readString();
		complete_yn_jb = source.readString();
		send_yn_jg = source.readString();
		send_yn_yb = source.readString();
		send_yn_mh = source.readString();
		send_yn_gh = source.readString();
		send_yn_pr = source.readString();
		send_yn_jb = source.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(master_idx);
		arg0.writeString(tracksName);
		arg0.writeString(towerNo);
		arg0.writeString(date);
		arg0.writeString(type);
		arg0.writeString(typeName);
		arg0.writeString(p1st);
		arg0.writeString(p2nd);
		arg0.writeString(latitude);
		arg0.writeString(longitude);
		arg0.writeString(nfcTagLatitude);
		arg0.writeString(nfcTagLongitude);
		arg0.writeString(status);
		arg0.writeString(eqpNm);
		arg0.writeString(eqpNo);
		arg0.writeString(has_bt);
		arg0.writeString(has_js);
		arg0.writeString(has_jj);
		arg0.writeString(has_kb);
		arg0.writeString(has_hk);
		arg0.writeString(complete_yn_bt);
		arg0.writeString(complete_yn_js);
		arg0.writeString(complete_yn_jp);
		arg0.writeString(complete_yn_jj);
		arg0.writeString(complete_yn_kb);
		arg0.writeString(complete_yn_hk);
		arg0.writeString(complete_yn_hj);
		arg0.writeString(complete_yn_br);
		arg0.writeString(send_yn_bt);
		arg0.writeString(send_yn_js);
		arg0.writeString(send_yn_jp);
		arg0.writeString(send_yn_jj);
		arg0.writeString(send_yn_kb);
		arg0.writeString(send_yn_hk);
		arg0.writeString(send_yn_hj);
		arg0.writeString(send_yn_br);
		arg0.writeString(unity_ins_no);
		arg0.writeString(nfc_tag_id);
		arg0.writeString(nfc_tag_yn);
		arg0.writeString(ins_input_date);
		arg0.writeString(ins_plan_no);
		arg0.writeString(ins_sn);
		arg0.writeString(fnct_lc_no);
		arg0.writeString(address);

		arg0.writeString(has_jg);
		arg0.writeString(has_yb);
		arg0.writeString(has_mh);
		arg0.writeString(has_gh);
		arg0.writeString(has_pr);
		arg0.writeString(has_jb);
		arg0.writeString(complete_yn_jg);
		arg0.writeString(complete_yn_yb);
		arg0.writeString(complete_yn_mh);
		arg0.writeString(complete_yn_gh);
		arg0.writeString(complete_yn_pr);
		arg0.writeString(complete_yn_jb);
		arg0.writeString(send_yn_jg);
		arg0.writeString(send_yn_yb);
		arg0.writeString(send_yn_mh);
		arg0.writeString(send_yn_gh);
		arg0.writeString(send_yn_pr);
		arg0.writeString(send_yn_jb);
		
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public  static final Parcelable.Creator<InspectInfo> CREATOR
	= new Parcelable.Creator<InspectInfo>() {

		@Override
		public InspectInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new InspectInfo(source);
		}

		@Override
		public InspectInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new InspectInfo[size];
		}
	};

	
	public String getMaster_idx() {
		return master_idx;
	}

	public void setMaster_idx(String master_idx) {
		this.master_idx = master_idx;
	}

	public String getTracksName() {
		return tracksName;
	}

	public String getTowerNo() {
		return towerNo;
	}

	public void setTowerNo(String towerNo) {
		this.towerNo = towerNo;
	}

	public void setTracksName(String tracksName) {
		this.tracksName = tracksName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getP1st() {
		return p1st;
	}

	public void setP1st(String p1st) {
		this.p1st = p1st;
	}

	public String getP2nd() {
		return p2nd;
	}

	public void setP2nd(String p2nd) {
		this.p2nd = p2nd;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	

	public String getNfcTagLatitude() {
		return nfcTagLatitude;
	}

	public void setNfcTagLatitude(String nfcTagLatitude) {
		this.nfcTagLatitude = nfcTagLatitude;
	}

	public String getNfcTagLongitude() {
		return nfcTagLongitude;
	}

	public void setNfcTagLongitude(String nfcTagLongitude) {
		this.nfcTagLongitude = nfcTagLongitude;
	}

	public String getEqpNm() {
		return eqpNm;
	}

	public void setEqpNm(String eqpNm) {
		this.eqpNm = eqpNm;
	}

	public String getEqpNo() {
		return eqpNo;
	}

	public void setEqpNo(String eqpNo) {
		this.eqpNo = eqpNo;
	}


	public String getHas_bt() {
		return has_bt;
	}

	public void setHas_bt(String has_bt) {
		this.has_bt = has_bt;
	}

	public String getComplete_yn_jp() {
		return complete_yn_jp;
	}

	public void setComplete_yn_jp(String complete_yn_jp) {
		this.complete_yn_jp = complete_yn_jp;
	}

	public String getHas_js() {
		return has_js;
	}

	public void setHas_js(String has_js) {
		this.has_js = has_js;
	}

	public String getHas_jj() {
		return has_jj;
	}

	public void setHas_jj(String has_jj) {
		this.has_jj = has_jj;
	}

	public String getHas_kb() {
		return has_kb;
	}

	public void setHas_kb(String has_kb) {
		this.has_kb = has_kb;
	}

	public String getHas_hk() {
		return has_hk;
	}

	public void setHas_hk(String has_hk) {
		this.has_hk = has_hk;
	}

	public String getComplete_yn_bt() {
		return complete_yn_bt;
	}

	public void setComplete_yn_bt(String complete_yn_bt) {
		this.complete_yn_bt = complete_yn_bt;
	}

	public String getComplete_yn_js() {
		return complete_yn_js;
	}

	public void setComplete_yn_js(String complete_yn_js) {
		this.complete_yn_js = complete_yn_js;
	}

	public String getComplete_yn_jj() {
		return complete_yn_jj;
	}

	public void setComplete_yn_jj(String complete_yn_jj) {
		this.complete_yn_jj = complete_yn_jj;
	}

	public String getComplete_yn_kb() {
		return complete_yn_kb;
	}

	public void setComplete_yn_kb(String complete_yn_kb) {
		this.complete_yn_kb = complete_yn_kb;
	}

	public String getComplete_yn_hk() {
		return complete_yn_hk;
	}

	public void setComplete_yn_hk(String complete_yn_hk) {
		this.complete_yn_hk = complete_yn_hk;
	}

	public String getUnity_ins_no() {
		return unity_ins_no;
	}

	public void setUnity_ins_no(String unity_ins_no) {
		this.unity_ins_no = unity_ins_no;
	}

	public String getHas_hj() {
		return has_hj;
	}

	public void setHas_hj(String has_hj) {
		this.has_hj = has_hj;
	}

	public String getHas_br() {
		return has_br;
	}

	public void setHas_br(String has_br) {
		this.has_br = has_br;
	}

	public String getHas_yb() {
		return has_yb;
	}

	public void setHas_yb(String has_yb) {
		this.has_yb = has_yb;
	}

	public String getComplete_yn_hj() {
		return complete_yn_hj;
	}

	public void setComplete_yn_hj(String complete_yn_hj) {
		this.complete_yn_hj = complete_yn_hj;
	}

	public String getComplete_yn_br() {
		return complete_yn_br;
	}

	public void setComplete_yn_br(String complete_yn_br) {
		this.complete_yn_br = complete_yn_br;
	}
	
	public String getNfc_tag_id() {
		return nfc_tag_id;
	}

	public void setNfc_tag_id(String nfc_tag_id) {
		this.nfc_tag_id = nfc_tag_id;
	}

	public String getNfc_tag_yn() {
		return nfc_tag_yn;
	}

	public void setNfc_tag_yn(String nfc_tag_yn) {
		this.nfc_tag_yn = nfc_tag_yn;
	}
	
	public String getIns_input_date() {
		return ins_input_date;
	}

	public void setIns_input_date(String ins_input_date) {
		this.ins_input_date = ins_input_date;
	}

	public String getIns_plan_no() {
		return ins_plan_no;
	}

	public void setIns_plan_no(String ins_plan_no) {
		this.ins_plan_no = ins_plan_no;
	}

	public String getIns_sn() {
		return ins_sn;
	}

	public void setIns_sn(String ins_sn) {
		this.ins_sn = ins_sn;
	}

	public String getFnct_lc_no() {
		return fnct_lc_no;
	}

	public void setFnct_lc_no(String fnct_lc_no) {
		this.fnct_lc_no = fnct_lc_no;
	}

	public String getHas_jp() {
		return has_jp;
	}

	public void setHas_jp(String has_jp) {
		this.has_jp = has_jp;
	}


	public String getSend_yn_bt() {
		return send_yn_bt;
	}

	public void setSend_yn_bt(String send_yn_bt) {
		this.send_yn_bt = send_yn_bt;
	}

	public String getSend_yn_js() {
		return send_yn_js;
	}

	public void setSend_yn_js(String send_yn_js) {
		this.send_yn_js = send_yn_js;
	}

	public String getSend_yn_jj() {
		return send_yn_jj;
	}

	public void setSend_yn_jj(String send_yn_jj) {
		this.send_yn_jj = send_yn_jj;
	}

	public String getSend_yn_kb() {
		return send_yn_kb;
	}

	public void setSend_yn_kb(String send_yn_kb) {
		this.send_yn_kb = send_yn_kb;
	}

	public String getSend_yn_hk() {
		return send_yn_hk;
	}

	public void setSend_yn_hk(String send_yn_hk) {
		this.send_yn_hk = send_yn_hk;
	}

	public String getSend_yn_hj() {
		return send_yn_hj;
	}

	public void setSend_yn_hj(String send_yn_hj) {
		this.send_yn_hj = send_yn_hj;
	}

	public String getSend_yn_br() {
		return send_yn_br;
	}

	public void setSend_yn_br(String send_yn_br) {
		this.send_yn_br = send_yn_br;
	}

	public String getSend_yn_jp() {
		return send_yn_jp;
	}

	public void setSend_yn_jp(String send_yn_jp) {
		this.send_yn_jp = send_yn_jp;
	}


	public static class COLS {
		public static final String MASTER_IDX = "MASTER_IDX";
		public static final String SCHEDULE_ID = "SCHEDULE_ID";
		public static final String MASTER_NO = "MASTER_NO";
		public static final String NFC_ID = "NFC_ID";
		public static final String INS_DATE = "INS_DATE";
		public static final String INS_TYPE_CODE = "INS_TYPE_CODE";
		public static final String WORK_TYPE = "WORK_TYPE";
		public static final String CHECK_INS_COMP = "CHECK_INS_COMP";
		public static final String CHECK_INS_NAME_A = "CHECK_INS_NAME_A";
		public static final String CHECK_INS_NAME_B = "CHECK_INS_NAME_B";
		public static final String START_TIME = "START_TIME";
		public static final String START_CHECKER = "START_CHECKER";
		public static final String END_TIME = "END_TIME";
		public static final String END_CHECKER = "END_CHECKER";
		public static final String WEATHER_CODE = "WEATHER_CODE";
		public static final String REMARKS = "REMARKS";
		public static final String ADD_FILE = "ADD_FILE";
		public static final String REPORT_DATE = "REPORT_DATE";
		public static final String CHECK_INS_CO_COUNT = "CHECK_INS_CO_COUNT";
		public static final String REPORT_NO = "REPORT_NO";
		public static final String LATITUDE = "LATITUDE";
		public static final String LONGITUDE = "LONGITUDE";
		public static final String REG_DATE = "REG_DATE";
		public static final String REG_ID = "REG_ID";
		public static final String UPD_DATE = "UPD_DATE";
		public static final String UPD_ID = "UPD_ID";
		
		public static final String TRACKS_NAME = "TRACKS_NAME";
		public static final String TOWER_NO = "TOWER_NO";
		public static final String INS_TYPE = "INS_TYPE";
		public static final String INS_TYPE_NM = "INS_TYPE_NM";
		public static final String INS_SCH_DATE = "INS_SCH_DATE";
		public static final String BPLACE1 = "BPLACE1";
		public static final String BPLACE2 = "BPLACE2";
		public static final String COMPLETE_YN = "COMPLETE_YN";
		public static final String NFC_TAG_LATITUDE = "NFC_TAG_LATITUDE";
		public static final String NFC_TAG_LONGITUDE = "NFC_TAG_LONGITUDE";
		public static final String EQP_NM = "EQP_NM";
		public static final String EQP_NO = "EQP_NO";
		
		public static final String COMPLETE_YN_INS_TYPE = "COMPLETE_YN_INS_TYPE";
		public static final String SEND_YN_INS_TYPE = "SEND_YN_INS_TYPE";
		public static final String UNITY_INS_NO = "UNITY_INS_NO";
		public static final String NFC_TAG_ID = "NFC_TAG_ID";
		public static final String NFC_TAG_YN = "NFC_TAG_YN";
		public static final String INS_INPUT_DATE = "INS_INPUT_DATE";

		public static final String INS_PLAN_NO = "INS_PLAN_NO";
		public static final String INS_SN = "INS_SN";
		public static final String FNCT_LC_NO = "FNCT_LC_NO";
		public static final String ADDRESS = "ADDRESS";
		
	}
}
