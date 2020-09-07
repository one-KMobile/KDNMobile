package com.kdn.mtps.mobile.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.db.bean.InputBTLog;
import com.kdn.mtps.mobile.db.bean.InspectResultMasterLog;
import com.kdn.mtps.mobile.db.bean.ManageCodeLog;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.info.InspectOutput;
import com.kdn.mtps.mobile.info.UserInfo;
import com.kdn.mtps.mobile.input.BRInfo;
import com.kdn.mtps.mobile.input.BTInfo;
import com.kdn.mtps.mobile.input.HJInfo;
import com.kdn.mtps.mobile.input.HKInfo;
import com.kdn.mtps.mobile.input.JJInfo;
import com.kdn.mtps.mobile.input.JSInfo;
import com.kdn.mtps.mobile.input.KBInfo;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.util.Logg;

public class InspectResultMasterDao extends BaseDao{
	static InspectResultMasterDao inspectResultMasterDao;

	private InspectResultMasterDao(Context ctx) {
		tableName = "INSPECT_RESULT_MASTER";
		this.ctx = ctx;
	}

	public static InspectResultMasterDao getInstance(Context ctx) {
		if (inspectResultMasterDao == null)
			inspectResultMasterDao = new InspectResultMasterDao(ctx);
		return inspectResultMasterDao;
	}
	
	public void Append(InspectInfo row) {
		Append(row, -1);
	}
	
	public void Append(InspectInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(InspectInfo.COLS.MASTER_IDX, idx);
		}
		
		updateRow.put(InspectInfo.COLS.TRACKS_NAME, row.tracksName);
		updateRow.put(InspectInfo.COLS.TOWER_NO, row.towerNo);
		updateRow.put(InspectInfo.COLS.INS_TYPE, row.type);
		updateRow.put(InspectInfo.COLS.INS_SCH_DATE, row.date);
		updateRow.put(InspectInfo.COLS.LATITUDE, row.latitude);
		updateRow.put(InspectInfo.COLS.LONGITUDE, row.longitude);
		updateRow.put(InspectInfo.COLS.EQP_NM, row.eqpNm);
		updateRow.put(InspectInfo.COLS.EQP_NO, row.eqpNo);
		updateRow.put(InspectInfo.COLS.INS_TYPE_NM, row.typeName);
		updateRow.put(InspectInfo.COLS.UNITY_INS_NO, row.unity_ins_no);
		updateRow.put(InspectInfo.COLS.INS_PLAN_NO, row.ins_plan_no);
		updateRow.put(InspectInfo.COLS.INS_SN, row.ins_sn);
		updateRow.put(InspectInfo.COLS.FNCT_LC_NO, row.fnct_lc_no);
		updateRow.put(InspectInfo.COLS.ADDRESS, row.address);
		
		db.replace(tableName, null, updateRow);

	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
		
	}
	
	public void updateComplete(String mIdx, String completeYN, String inspectCode) {
		if ("N".equals(completeYN))
			inspectCode = "";
		
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "UPDATE %s SET COMPLETE_YN_INS_TYPE = '%s', ";
		
		if ("N".equalsIgnoreCase(completeYN))
			fmt += "COMPLETE_YN = '%s' WHERE master_idx = %s";
		else{
			fmt += "COMPLETE_YN = '%s', INS_INPUT_DATE = (datetime('now','localtime')) WHERE master_idx = %s";
//			fmt += "COMPLETE_YN = '%s', INS_INPUT_DATE = date('now','-9 day') WHERE master_idx = %s";
		}
		
		String sql = String.format(fmt, tableName, inspectCode, completeYN, mIdx);
		db.execSQL(sql);
		
//		dbCheck("select * from "+tableName+" where master_idx = '" + mIdx +"'");
	}
	
	public void deleteOldData() {
		
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE from %s where INS_INPUT_DATE < date('now','-" + ConstVALUE.DELETE_DAY +" day')";
		
		String sql = String.format(fmt, tableName);
		db.execSQL(sql);
		
	}
	
	public void updateSendComplete(String mIdx, String inspectCode) {
		
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "UPDATE %s SET SEND_YN_INS_TYPE = '%s' WHERE master_idx = %s";
		
		String sql = String.format(fmt, tableName, inspectCode, mIdx);
		db.execSQL(sql);
		
//		dbCheck("select * from "+tableName+" where master_idx = '" + mIdx +"'");
	}
	
	public void updateNfcTagLocation(String mIdx, String nfcTag, String latitude, String longitude, boolean isTag) {
		Logg.d("nfctag : " + latitude + " / " + longitude);
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		
		String fmt;
		if (isTag) {
			fmt = "UPDATE %s SET NFC_TAG_YN='Y', NFC_TAG_ID = '%s', NFC_TAG_LATITUDE = '%s', NFC_TAG_LONGITUDE = '%s' WHERE master_idx = %s ";
			String sql = String.format(fmt, tableName, nfcTag, latitude, longitude, mIdx);
			db.execSQL(sql);
		} else {
			fmt = "UPDATE %s SET NFC_TAG_LATITUDE = '%s', NFC_TAG_LONGITUDE = '%s' WHERE master_idx = %s and NFC_TAG_YN = 'N'";
			String sql = String.format(fmt, tableName, latitude, longitude, mIdx);
			db.execSQL(sql);
		}
		
		
		String fmt2 = "select * from %s WHERE master_idx = %s ";
		String sql2 = String.format(fmt2, tableName, mIdx);
		dbCheck(sql2);
	}
	
	public boolean existIns(String tracksName, String eqpName, String insType, String insDate) {
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s where TRACKS_NAME = '%s' and TOWER_NO = '%s' and INS_TYPE = '%s' and INS_SCH_DATE = '%s' ";
			String sql = String.format(fmt, tableName, tracksName, eqpName, insType, insDate);

			cursor = db.rawQuery(sql, null);

			if (cursor.moveToNext())
				return true;

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close();
			}
			
		return false;
	}
	
	public InspectInfo selectIns(String insPlanNo, String tracksName, String eqpName, String insType, String insDate) {
		InspectInfo info = new InspectInfo();
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s where INS_PLAN_NO = '%s' and TRACKS_NAME = '%s' and TOWER_NO = '%s' and INS_TYPE = '%s' and INS_SCH_DATE = '%s' ";
			String sql = String.format(fmt, tableName, insPlanNo, tracksName, eqpName, insType, insDate);

			cursor = db.rawQuery(sql, null);

			if (cursor.moveToNext()) {
				String master_idx = cursor.getString(0);
				info.master_idx = master_idx;
				return info;
			}
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				close();
			}
			
			
		return null;
	}
	
		
//	public InspectInfo selectIns(String tracksName, String eqpName, String insDate) {
//		InspectInfo info = null;
//		
//		try {
//			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
//			String fmt = "select * from %s where TRACKS_NAME = '%s' and TOWER_NO = '%s' and INS_SCH_DATE = '%s' ";
//			String sql = String.format(fmt, tableName, tracksName, eqpName, insDate);
//
//			cursor = db.rawQuery(sql, null);
//
//			while (cursor.moveToNext()) {
//				String master_idx = cursor.getString(0);
//				String tracks_name = cursor.getString(1);
//				String tower_no= cursor.getString(2);
//				String ins_type = cursor.getString(3);
//				String ins_sch_date = cursor.getString(4);
//				String latitude = cursor.getString(5);
//				String longitude = cursor.getString(6);
//				String complete_yn = cursor.getString(7);
//				String eqpNm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NM));
//				String eqpNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NO));
//				
//				info = new InspectInfo();
//				info.master_idx = master_idx;
//				info.tracksName = tracks_name;
//				info.towerNo = tower_no;
//				info.type = ins_type;
//				info.date = ins_sch_date;
//				info.latitude = latitude;
//				info.longitude = longitude;
//				info.eqpNm = eqpNm;
//				info.eqpNo = eqpNo;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			close();
//		}
//
//		return info;
//	}

	public ArrayList<InspectInfo> selecInsList(String tracksName, String insType, String towerNo, String schDate) {
		
		ArrayList<InspectInfo> insList = new ArrayList<InspectInfo>();
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

//			String fmt = "select distinct EQP_NO, EQP_NM, REPLACE(REPLACE(REPLACE(EQP_NM, TRACKS_NAME, ''),'호 철탑',''),'호 철','') as tower_idx "
//			          + "from %s where TRACKS_NAME = '%s' ORDER BY CAST(tower_idx AS INTEGER)";
			
			String insPlanNo = UserInfo.getSessionData(ctx).contract;
			
			String fmt = "SELECT *,"
		                    + "GROUP_CONCAT(INS_TYPE ) AS SUM_INS_TYPE "
		                    + ", GROUP_CONCAT(COMPLETE_YN_INS_TYPE ) AS SUM_COMPLETE_INS_TYPE "
		                    + ", GROUP_CONCAT(SEND_YN_INS_TYPE ) AS SUM_SEND_INS_TYPE "
		                    + ", REPLACE(REPLACE(REPLACE(EQP_NM, TRACKS_NAME, ''),'호 철탑',''),'호 철','') as tower_idx"
		                    +  " FROM INSPECT_RESULT_MASTER  "
		                    + " WHERE TRACKS_NAME = '" + tracksName + "'";
//		                    + " WHERE INS_PLAN_NO = '" + insPlanNo + "' and TRACKS_NAME = '" + tracksName + "'";

			if (insType != null && !"".equals(insType))
				fmt += " and INS_TYPE = '" + insType + "' ";
				
			if (towerNo != null && !"".equals(towerNo))
				fmt += " and TOWER_NO LIKE '%" + towerNo + "%' ";
				
			if (schDate != null && !"".equals(schDate) && !"전체".equals(schDate))
				fmt += " and INS_SCH_DATE = '" + schDate + "' ";
			
			fmt += " GROUP BY TOWER_NO, INS_SCH_DATE ORDER BY CAST(tower_idx AS INTEGER)";
			
			Logg.d("query : " + fmt);
//			String sql = String.format(fmt, tableName, tracksName);

			cursor = db.rawQuery(fmt, null);

			while (cursor.moveToNext()) {
				String master_idx = cursor.getString(0);
				String tracks_name = cursor.getString(1);
				String tower_no= cursor.getString(2);
				String ins_type = cursor.getString(3);
				String ins_type_nm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE_NM));
				String ins_sch_date = cursor.getString(4);
				String latitude = cursor.getString(5);
				String longitude = cursor.getString(6);
				String complete_yn = cursor.getString(7);
				String eqpNm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NM));
				String eqpNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NO));
				String sum_ins_type = cursor.getString(cursor.getColumnIndex("SUM_INS_TYPE"));
				String sum_complete_ins_type = cursor.getString(cursor.getColumnIndex("SUM_COMPLETE_INS_TYPE"));
				String sum_send_ins_type = cursor.getString(cursor.getColumnIndex("SUM_SEND_INS_TYPE"));
				String address = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.ADDRESS));
				
				InspectInfo info = new InspectInfo();
				info.master_idx = master_idx;
				info.tracksName = tracks_name;
				info.towerNo = tower_no;
				info.type = ins_type;
				info.typeName = ins_type_nm;
				info.date = ins_sch_date;
				info.latitude = latitude;
				info.longitude = longitude;
				info.eqpNm = eqpNm;
				info.eqpNo = eqpNo;
				info.address = address;
				
				String[] types = sum_ins_type.split(",");
				for (String type : types) {
					/*if (ConstVALUE.CODE_NO_INSPECT_BT.equals(type))
						info.has_bt = "Y";
					else*/
					if (ConstVALUE.CODE_NO_INSPECT_JS.equals(type))
						info.has_js = "Y";
					else if (ConstVALUE.CODE_NO_INSPECT_JJ.equals(type))
						info.has_jj = "Y";
					else if (ConstVALUE.CODE_NO_INSPECT_KB.equals(type))
						info.has_kb = "Y";
					else if (ConstVALUE.CODE_NO_INSPECT_HK.equals(type))
						info.has_hk = "Y";
					else if (ConstVALUE.CODE_NO_INSPECT_HJ.equals(type))
						info.has_hj = "Y";
					else if (ConstVALUE.CODE_NO_INSPECT_BR.equals(type))
						info.has_br = "Y";
					else if (ConstVALUE.CODE_NO_INSPECT_YB.equals(type))
						info.has_yb = "Y";
					else if (ConstVALUE.CODE_NO_INSPECT_JP.equals(type))
						info.has_jp = "Y";

					else if (ConstVALUE.CODE_NO_INSPECT_JG.equals(type))
						info.has_jg = "Y";
					else if (ConstVALUE.CODE_NO_INSPECT_MH.equals(type))
						info.has_mh = "Y";
					else if (ConstVALUE.CODE_NO_INSPECT_GH.equals(type))
						info.has_gh = "Y";
					else if (ConstVALUE.CODE_NO_INSPECT_PR.equals(type))
						info.has_pr = "Y";
					else if (ConstVALUE.CODE_NO_INSPECT_JB.equals(type))
						info.has_jb = "Y";
				}
				
				if (sum_complete_ins_type != null) {
					String[] complete_types = sum_complete_ins_type.split(",");
					for (String type : complete_types) {
						/*if (ConstVALUE.CODE_NO_INSPECT_BT.equals(type))
							info.complete_yn_bt = "Y";
						else*/
						if (ConstVALUE.CODE_NO_INSPECT_JS.equals(type))
							info.complete_yn_js = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_JJ.equals(type))
							info.complete_yn_jj = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_KB.equals(type))
							info.complete_yn_kb = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_HK.equals(type))
							info.complete_yn_hk = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_HJ.equals(type))
							info.complete_yn_hj = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_BR.equals(type))
							info.complete_yn_br = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_JP.equals(type))
							info.complete_yn_jp = "Y";

						else if (ConstVALUE.CODE_NO_INSPECT_JG.equals(type))
							info.complete_yn_jg = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_YB.equals(type))
							info.complete_yn_yb = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_MH.equals(type))
							info.complete_yn_mh = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_GH.equals(type))
							info.complete_yn_gh = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_PR.equals(type))
							info.complete_yn_pr = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_JB.equals(type))
							info.complete_yn_jb = "Y";
					}
				}
				
				if (sum_send_ins_type != null) {
					String[] send_types = sum_send_ins_type.split(",");
					for (String type : send_types) {
						/*if (ConstVALUE.CODE_NO_INSPECT_BT.equals(type))
							info.send_yn_bt = "Y";
						else*/
						if (ConstVALUE.CODE_NO_INSPECT_JS.equals(type))
							info.send_yn_js = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_JJ.equals(type))
							info.send_yn_jj = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_KB.equals(type))
							info.send_yn_kb = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_HK.equals(type))
							info.send_yn_hk = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_HJ.equals(type))
							info.send_yn_hj = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_BR.equals(type))
							info.send_yn_br = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_JP.equals(type))
							info.send_yn_jp = "Y";

						else if (ConstVALUE.CODE_NO_INSPECT_JG.equals(type))
							info.send_yn_jg = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_YB.equals(type))
							info.send_yn_yb = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_MH.equals(type))
							info.send_yn_mh = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_GH.equals(type))
							info.send_yn_gh = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_PR.equals(type))
							info.send_yn_pr = "Y";
						else if (ConstVALUE.CODE_NO_INSPECT_JB.equals(type))
							info.send_yn_jb = "Y";
					}
				}
				
				insList.add(info);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return insList;
	}
	
	public ArrayList<InspectInfo> selecInsCompleteList() {
		
		ArrayList<InspectInfo> insList = new ArrayList<InspectInfo>();
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s where COMPLETE_YN = 'Y' ";
			String sql = String.format(fmt, tableName);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				String master_idx = cursor.getString(0);
				String tracks_name = cursor.getString(1);
				String tower_no = cursor.getString(2);
				String ins_type = cursor.getString(3);
				String ins_sch_date = cursor.getString(4);
				String latitude = cursor.getString(5);
				String longitude = cursor.getString(6);
				String complete_yn = cursor.getString(7);
				String eqpNm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NM));
				String eqpNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NO));
				
				InspectInfo info = new InspectInfo();
				info.master_idx = master_idx;
				info.tracksName = tracks_name;
				info.towerNo = tower_no;
				info.type = ins_type;
				info.date = ins_sch_date;
				info.latitude = latitude;
				info.longitude = longitude;
				info.eqpNm = eqpNm;
				info.eqpNo = eqpNo;
				
				insList.add(info);
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "tracks_name : " + tracks_name);
				Log.d("Test", "ins_type : " + ins_type);
				Log.d("Test", "tower_no : " + tower_no);
				Log.d("Test", "ins_sch_date : " + ins_sch_date);
				Log.d("Test", "latitude : " + latitude);
				Log.d("Test", "longitude : " + longitude);
				Log.d("Test", "complete_yn : " + complete_yn);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return insList;
	}
	
	public ArrayList<InspectOutput> selectAllOutputList(Context ctx) {
		ArrayList<InspectOutput> outputList = new ArrayList<InspectOutput>();
		
		outputList.addAll(selectBTOutputList(ctx));
		outputList.addAll(selectJSOutputList(ctx));
		outputList.addAll(selectJJOutputList(ctx));
		outputList.addAll(selectHKOutputList(ctx));
		outputList.addAll(selectKBOutputList(ctx));
		outputList.addAll(selectHJOutputList(ctx));
		outputList.addAll(selectBROutputList(ctx));
		outputList.addAll(selectJPOutputList(ctx));;
		
		return outputList;
	}
	
	public ArrayList<InspectOutput> selectBTOutputList(Context ctx) {
		
		ArrayList<InspectOutput> outputList = new ArrayList<InspectOutput>();
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "SELECT * FROM INSPECT_RESULT_MASTER MA, INPUT_BT BT WHERE MA.MASTER_IDX = BT.MASTER_IDX AND MA.COMPLETE_YN='Y'"; 
			String sql = String.format(fmt, tableName);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				InspectOutput output = new InspectOutput();
				
				String master_idx = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.MASTER_IDX));
				String tracks_name = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TRACKS_NAME));
				String tower_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TOWER_NO));
				String ins_type = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE));
				String ins_type_nm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE_NM));
				String ins_sch_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SCH_DATE));
				String latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LATITUDE));
				String longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LONGITUDE));
				String complete_yn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.COMPLETE_YN));
				String nfc_tag_latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LATITUDE));
				String nfc_tag_longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LONGITUDE));
				String eqpNm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NM));
				String eqpNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NO));
				String unityInsNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.UNITY_INS_NO));
				String nfcTagId = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_ID));
				String nfcTagYn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_YN));
				String ins_input_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_INPUT_DATE));
				String ins_plan_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_PLAN_NO));
				String ins_sn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SN));
				String fnct_lc_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.FNCT_LC_NO));
				String address = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.ADDRESS));
				
				InspectInfo info = new InspectInfo();
				info.master_idx = master_idx;
				info.tracksName = tracks_name;
				info.towerNo = tower_no;
				info.type = ins_type;
				info.typeName = ins_type_nm;
				info.date = ins_sch_date;
				info.latitude = latitude;
				info.longitude = longitude;
				info.nfcTagLatitude = nfc_tag_latitude;
				info.nfcTagLongitude = nfc_tag_longitude;
				info.status = complete_yn;
				info.eqpNm = eqpNm;
				info.eqpNo = eqpNo;
				info.unity_ins_no = unityInsNo;
				info.nfc_tag_id = nfcTagId;
				info.nfc_tag_yn = nfcTagYn;
				info.ins_input_date = ins_input_date;
				info.ins_plan_no = ins_plan_no;
				info.ins_sn = ins_sn;
				info.fnct_lc_no = fnct_lc_no;
				info.address = address;
				
				output.inspectInfo = info;
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "tracks_name : " + tracks_name);
				Log.d("Test", "ins_type : " + ins_type);
				Log.d("Test", "tower_no : " + tower_no);
				Log.d("Test", "ins_sch_date : " + ins_sch_date);
				Log.d("Test", "latitude : " + latitude);
				Log.d("Test", "longitude : " + longitude);
				Log.d("Test", "nfcTagLatitude : " + nfc_tag_latitude);
				Log.d("Test", "nfcTagLongitude : " + nfc_tag_longitude);
				Log.d("Test", "complete_yn : " + complete_yn);
				
				int idx = cursor.getInt(cursor.getColumnIndex(InputBTLog.COLS.IDX));
				int mIdx = cursor.getInt(cursor.getColumnIndex(InputBTLog.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(InputBTLog.COLS.WEATHER));
				String worker_cnt = cursor.getString(cursor.getColumnIndex(InputBTLog.COLS.WORKER_CNT));
				String claim_content = cursor.getString(cursor.getColumnIndex(InputBTLog.COLS.CLAIM_CONTENT));
				String check_result = cursor.getString(cursor.getColumnIndex(InputBTLog.COLS.CHECK_RESULT));
				String etc = cursor.getString(cursor.getColumnIndex(InputBTLog.COLS.ETC));
				
				output.btInfo = new BTInfo();
				output.btInfo.weather = wether;
				output.btInfo.worker_cnt = worker_cnt;
				output.btInfo.claim_content = claim_content;
				output.btInfo.check_result = check_result;
				output.btInfo.etc = etc;
				
				Log.d("Test", "======================BTINFO====================");
				
				Log.d("Test", "mIdx : " + mIdx);
				Log.d("Test", "wether : " + wether);
				Log.d("Test", "worker_cnt : " + worker_cnt);
				Log.d("Test", "claim_content : " + claim_content);
				Log.d("Test", "check_result : " + check_result);
				Log.d("Test", "etc : " + etc);
				
				outputList.add(output);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return outputList;
	}

	public ArrayList<InspectOutput> selectJSOutputList(Context ctx) {
		
		ArrayList<InspectOutput> outputList = new ArrayList<InspectOutput>();
		
		InspectOutput output = new InspectOutput();
		
		String b4_master_idx = null;
		
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String insPlanNo = UserInfo.getSessionData(ctx).contract;
			String fmt = "SELECT * FROM INSPECT_RESULT_MASTER MA, INPUT_JS JS WHERE MA.MASTER_IDX = JS.MASTER_IDX AND MA.COMPLETE_YN='Y' ORDER BY MA.MASTER_IDX"; 
			String sql = String.format(fmt, tableName);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				
				String master_idx = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.MASTER_IDX));
				String tracks_name = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TRACKS_NAME));
				String tower_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TOWER_NO));
				String ins_type = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE));
				String ins_type_nm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE_NM));
				String ins_sch_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SCH_DATE));
				String latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LATITUDE));
				String longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LONGITUDE));
				String complete_yn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.COMPLETE_YN));
				String nfc_tag_latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LATITUDE));
				String nfc_tag_longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LONGITUDE));
				String eqpNm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NM));
				String eqpNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NO));
				String nfcTagId = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_ID));
				String nfcTagYn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_YN));
				String ins_input_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_INPUT_DATE));
				String unityInsNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.UNITY_INS_NO));
				String ins_plan_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_PLAN_NO));
				String ins_sn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SN));
				String fnct_lc_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.FNCT_LC_NO));
				String address = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.ADDRESS));
				
				InspectInfo info = new InspectInfo();
				info.master_idx = master_idx;
				info.tracksName = tracks_name;
				info.towerNo = tower_no;
				info.type = ins_type;
				info.typeName = ins_type_nm;
				info.date = ins_sch_date;
				info.latitude = latitude;
				info.longitude = longitude;
				info.nfcTagLatitude = nfc_tag_latitude;
				info.nfcTagLongitude = nfc_tag_longitude;
				info.status = complete_yn;
				info.eqpNm = eqpNm;
				info.eqpNo = eqpNo;
				info.unity_ins_no = unityInsNo;
				info.nfc_tag_id = nfcTagId;
				info.nfc_tag_yn = nfcTagYn;
				info.ins_input_date = ins_input_date;
				info.ins_plan_no = ins_plan_no;
				info.ins_sn = ins_sn;
				info.fnct_lc_no = fnct_lc_no;
				info.address = address;
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "tracks_name : " + tracks_name);
				Log.d("Test", "ins_type : " + ins_type);
				Log.d("Test", "tower_no : " + tower_no);
				Log.d("Test", "ins_sch_date : " + ins_sch_date);
				Log.d("Test", "latitude : " + latitude);
				Log.d("Test", "longitude : " + longitude);
				Log.d("Test", "nfcTagLatitude : " + nfc_tag_latitude);
				Log.d("Test", "nfcTagLongitude : " + nfc_tag_longitude);
				Log.d("Test", "complete_yn : " + complete_yn);
				
				int idx = cursor.getInt(cursor.getColumnIndex(JSInfo.COLS.IDX));
				String mIdx = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.WEATHER));
				String circuit_no = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.CIRCUIT_NO));
				String circuit_name = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.CIRCUIT_NAME));
				String area_temp = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.AREA_TEMP));
				String current_load = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.CURRENT_LOAD));
				String conductor_cnt = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.CONDUCTOR_CNT));
				String location = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.LOCATION));
				String c1_js = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C1_JS));
				String c1_jsj = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C1_JSJ));
				String c1_yb_result = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C1_YB_RESULT));
				String c1_power_no = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C1_POWER_NO));
				String c2_js = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C2_JS));
				String c2_jsj = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C2_JSJ));
				String c2_yb_result = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C2_YB_RESULT));
				String c2_power_no = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C2_POWER_NO));
				String c3_js = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C3_JS));
				String c3_jsj = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C3_JSJ));
				String c3_yb_result = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C3_YB_RESULT));
				String c3_power_no = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C3_POWER_NO));
				
				JSInfo jsInfo = new JSInfo();
				jsInfo.idx = idx;
				jsInfo.master_idx = mIdx;
				jsInfo.weather = wether;
				jsInfo.area_temp = area_temp;
				jsInfo.circuit_no = circuit_no;
				jsInfo.circuit_name = circuit_name;
				jsInfo.current_load = current_load;
				jsInfo.conductor_cnt = conductor_cnt;
				jsInfo.location = location;
				jsInfo.c1_js= c1_js;
				jsInfo.c1_jsj = c1_jsj;
				jsInfo.c1_yb_result = c1_yb_result;
				jsInfo.c1_power_no = c1_power_no;
				jsInfo.c2_js = c2_js;
				jsInfo.c2_jsj = c2_jsj;
				jsInfo.c2_yb_result = c2_yb_result;
				jsInfo.c2_power_no = c2_power_no;
				jsInfo.c3_js = c3_js;
				jsInfo.c3_jsj = c3_jsj;
				jsInfo.c3_yb_result = c3_yb_result;
				jsInfo.c3_power_no = c3_power_no;
				
				if (b4_master_idx == null) {
					output.jsInfoList = new ArrayList<JSInfo>();
					output.jsInfoList.add(jsInfo);
					output.inspectInfo = info;
				} else {
					if (b4_master_idx.equals(master_idx)) {
						output.jsInfoList.add(jsInfo);
					} else {
						outputList.add(output);
						output = new InspectOutput();
						output.jsInfoList = new ArrayList<JSInfo>();
						output.jsInfoList.add(jsInfo);
						output.inspectInfo = info;
					}
				}
					
				b4_master_idx = master_idx;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		if (output.jsInfoList != null)
			outputList.add(output);

		return outputList;
	}
	
	public ArrayList<InspectOutput> selectJPOutputList(Context ctx) {
		
		ArrayList<InspectOutput> outputList = new ArrayList<InspectOutput>();
		
		InspectOutput output = new InspectOutput();
		
		String b4_master_idx = null;
		
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String insPlanNo = UserInfo.getSessionData(ctx).contract;
			String fmt = "SELECT * FROM INSPECT_RESULT_MASTER MA, INPUT_JP JP WHERE MA.MASTER_IDX = JP.MASTER_IDX AND MA.COMPLETE_YN='Y' ORDER BY MA.MASTER_IDX"; 
			String sql = String.format(fmt, tableName);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				
				String master_idx = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.MASTER_IDX));
				String tracks_name = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TRACKS_NAME));
				String tower_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TOWER_NO));
				String ins_type = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE));
				String ins_type_nm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE_NM));
				String ins_sch_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SCH_DATE));
				String latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LATITUDE));
				String longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LONGITUDE));
				String complete_yn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.COMPLETE_YN));
				String nfc_tag_latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LATITUDE));
				String nfc_tag_longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LONGITUDE));
				String eqpNm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NM));
				String eqpNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NO));
				String nfcTagId = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_ID));
				String nfcTagYn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_YN));
				String ins_input_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_INPUT_DATE));
				String unityInsNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.UNITY_INS_NO));
				String ins_plan_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_PLAN_NO));
				String ins_sn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SN));
				String fnct_lc_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.FNCT_LC_NO));
				String address = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.ADDRESS));
				
				InspectInfo info = new InspectInfo();
				info.master_idx = master_idx;
				info.tracksName = tracks_name;
				info.towerNo = tower_no;
				info.type = ins_type;
				info.typeName = ins_type_nm;
				info.date = ins_sch_date;
				info.latitude = latitude;
				info.longitude = longitude;
				info.nfcTagLatitude = nfc_tag_latitude;
				info.nfcTagLongitude = nfc_tag_longitude;
				info.status = complete_yn;
				info.eqpNm = eqpNm;
				info.eqpNo = eqpNo;
				info.unity_ins_no = unityInsNo;
				info.nfc_tag_id = nfcTagId;
				info.nfc_tag_yn = nfcTagYn;
				info.ins_input_date = ins_input_date;
				info.ins_plan_no = ins_plan_no;
				info.ins_sn = ins_sn;
				info.fnct_lc_no = fnct_lc_no;
				info.address = address;
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "tracks_name : " + tracks_name);
				Log.d("Test", "ins_type : " + ins_type);
				Log.d("Test", "tower_no : " + tower_no);
				Log.d("Test", "ins_sch_date : " + ins_sch_date);
				Log.d("Test", "latitude : " + latitude);
				Log.d("Test", "longitude : " + longitude);
				Log.d("Test", "nfcTagLatitude : " + nfc_tag_latitude);
				Log.d("Test", "nfcTagLongitude : " + nfc_tag_longitude);
				Log.d("Test", "complete_yn : " + complete_yn);
				
				int idx = cursor.getInt(cursor.getColumnIndex(JSInfo.COLS.IDX));
				String mIdx = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.WEATHER));
				String circuit_no = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.CIRCUIT_NO));
				String circuit_name = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.CIRCUIT_NAME));
				String area_temp = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.AREA_TEMP));
				String current_load = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.CURRENT_LOAD));
				String conductor_cnt = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.CONDUCTOR_CNT));
				String location = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.LOCATION));
				String c1_js = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C1_JS));
				String c1_jsj = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C1_JSJ));
				String c1_yb_result = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C1_YB_RESULT));
				String c1_power_no = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C1_POWER_NO));
				String c2_js = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C2_JS));
				String c2_jsj = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C2_JSJ));
				String c2_yb_result = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C2_YB_RESULT));
				String c2_power_no = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C2_POWER_NO));
				String c3_js = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C3_JS));
				String c3_jsj = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C3_JSJ));
				String c3_yb_result = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C3_YB_RESULT));
				String c3_power_no = cursor.getString(cursor.getColumnIndex(JSInfo.COLS.C3_POWER_NO));
				
				JSInfo jsInfo = new JSInfo();
				jsInfo.idx = idx;
				jsInfo.master_idx = mIdx;
				jsInfo.weather = wether;
				jsInfo.area_temp = area_temp;
				jsInfo.circuit_no = circuit_no;
				jsInfo.circuit_name = circuit_name;
				jsInfo.current_load = current_load;
				jsInfo.conductor_cnt = conductor_cnt;
				jsInfo.location = location;
				jsInfo.c1_js= c1_js;
				jsInfo.c1_jsj = c1_jsj;
				jsInfo.c1_yb_result = c1_yb_result;
				jsInfo.c1_power_no = c1_power_no;
				jsInfo.c2_js = c2_js;
				jsInfo.c2_jsj = c2_jsj;
				jsInfo.c2_yb_result = c2_yb_result;
				jsInfo.c2_power_no = c2_power_no;
				jsInfo.c3_js = c3_js;
				jsInfo.c3_jsj = c3_jsj;
				jsInfo.c3_yb_result = c3_yb_result;
				jsInfo.c3_power_no = c3_power_no;
				
				if (b4_master_idx == null) {
					output.jpInfoList = new ArrayList<JSInfo>();
					output.jpInfoList.add(jsInfo);
					output.inspectInfo = info;
				} else {
					if (b4_master_idx.equals(master_idx)) {
						output.jpInfoList.add(jsInfo);
					} else {
						outputList.add(output);
						output = new InspectOutput();
						output.jpInfoList = new ArrayList<JSInfo>();
						output.jpInfoList.add(jsInfo);
						output.inspectInfo = info;
					}
				}
					
				b4_master_idx = master_idx;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		if (output.jpInfoList != null)
			outputList.add(output);

		return outputList;
	}
	
	public ArrayList<InspectOutput> selectJJOutputList(Context ctx) {
		
		ArrayList<InspectOutput> outputList = new ArrayList<InspectOutput>();
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String insPlanNo = UserInfo.getSessionData(ctx).contract;
			String fmt = "SELECT * FROM INSPECT_RESULT_MASTER MA, INPUT_JJ JJ WHERE MA.MASTER_IDX = JJ.MASTER_IDX AND MA.COMPLETE_YN='Y'"; 
			String sql = String.format(fmt, tableName);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				InspectOutput output = new InspectOutput();
				
				String master_idx = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.MASTER_IDX));
				String tracks_name = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TRACKS_NAME));
				String tower_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TOWER_NO));
				String ins_type = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE));
				String ins_type_nm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE_NM));
				String ins_sch_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SCH_DATE));
				String latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LATITUDE));
				String longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LONGITUDE));
				String complete_yn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.COMPLETE_YN));
				String nfc_tag_latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LATITUDE));
				String nfc_tag_longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LONGITUDE));
				String eqpNm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NM));
				String eqpNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NO));
				String unityInsNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.UNITY_INS_NO));
				String nfcTagId = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_ID));
				String nfcTagYn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_YN));
				String ins_input_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_INPUT_DATE));
				String ins_plan_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_PLAN_NO));
				String ins_sn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SN));
				String fnct_lc_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.FNCT_LC_NO));
				String address = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.ADDRESS));
				
				InspectInfo info = new InspectInfo();
				info.master_idx = master_idx;
				info.tracksName = tracks_name;
				info.towerNo = tower_no;
				info.type = ins_type;
				info.typeName = ins_type_nm;
				info.date = ins_sch_date;
				info.latitude = latitude;
				info.longitude = longitude;
				info.nfcTagLatitude = nfc_tag_latitude;
				info.nfcTagLongitude = nfc_tag_longitude;
				info.status = complete_yn;
				info.eqpNm = eqpNm;
				info.eqpNo = eqpNo;
				info.unity_ins_no = unityInsNo;
				info.nfc_tag_id = nfcTagId;
				info.nfc_tag_yn = nfcTagYn;
				info.ins_input_date = ins_input_date;
				info.ins_plan_no = ins_plan_no;
				info.ins_sn = ins_sn;
				info.fnct_lc_no = fnct_lc_no;
				info.address = address;
				
				output.inspectInfo = info;
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "tracks_name : " + tracks_name);
				Log.d("Test", "ins_type : " + ins_type);
				Log.d("Test", "tower_no : " + tower_no);
				Log.d("Test", "ins_sch_date : " + ins_sch_date);
				Log.d("Test", "latitude : " + latitude);
				Log.d("Test", "longitude : " + longitude);
				Log.d("Test", "nfcTagLatitude : " + nfc_tag_latitude);
				Log.d("Test", "nfcTagLongitude : " + nfc_tag_longitude);
				Log.d("Test", "complete_yn : " + complete_yn);
				
				int idx = cursor.getInt(cursor.getColumnIndex(JJInfo.COLS.IDX));
				String mIdx = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.WEATHER));
				String ground = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.GROUND));
				String span_length = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.SPAN_LENGTH));
				String check_result = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.CHECK_RESULT));
				String remarks = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.REMARKS));
				String terminal1_1 = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.TERMINAL1_1));
				String terminal1_2 = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.TERMINAL1_2));
				String terminal1_3 = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.TERMINAL1_3));
				String terminal1_5 = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.TERMINAL1_5));
				String terminal1_10 = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.TERMINAL1_10));
				String count_1 = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.COUNT_1));
				
				output.jjInfo = new JJInfo();
				output.jjInfo.weather = wether;
				output.jjInfo.ground = ground;;
				output.jjInfo.span_length = span_length;
				output.jjInfo.check_result = check_result;
				output.jjInfo.remarks = remarks;
				output.jjInfo.terminal1_1 = terminal1_1;
				output.jjInfo.terminal1_2 = terminal1_2;
				output.jjInfo.terminal1_3 = terminal1_3;
				output.jjInfo.terminal1_5 = terminal1_5;
				output.jjInfo.terminal1_10 = terminal1_10;
				output.jjInfo.count_1 = count_1;
				
				outputList.add(output);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return outputList;
	}

	public ArrayList<InspectOutput> selectHKOutputList(Context ctx) {
		
		ArrayList<InspectOutput> outputList = new ArrayList<InspectOutput>();
		
		InspectOutput output = new InspectOutput();
		
		String b4_master_idx = null;
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String insPlanNo = UserInfo.getSessionData(ctx).contract;
			String fmt = "SELECT * FROM INSPECT_RESULT_MASTER MA, INPUT_HK HK WHERE MA.MASTER_IDX = HK.MASTER_IDX AND MA.COMPLETE_YN='Y' ORDER BY MA.MASTER_IDX"; 
			String sql = String.format(fmt, tableName);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				String master_idx = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.MASTER_IDX));
				String tracks_name = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TRACKS_NAME));
				String tower_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TOWER_NO));
				String ins_type = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE));
				String ins_type_nm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE_NM));
				String ins_sch_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SCH_DATE));
				String latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LATITUDE));
				String longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LONGITUDE));
				String complete_yn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.COMPLETE_YN));
				String nfc_tag_latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LATITUDE));
				String nfc_tag_longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LONGITUDE));
				String eqpNm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NM));
				String eqpNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NO));
				String nfcTagId = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_ID));
				String nfcTagYn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_YN));
				String ins_input_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_INPUT_DATE));
				String unityInsNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.UNITY_INS_NO));
				String ins_plan_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_PLAN_NO));
				String ins_sn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SN));
				String fnct_lc_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.FNCT_LC_NO));
				String address = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.ADDRESS));
				
				InspectInfo info = new InspectInfo();
				info.master_idx = master_idx;
				info.tracksName = tracks_name;
				info.towerNo = tower_no;
				info.type = ins_type;
				info.typeName = ins_type_nm;
				info.date = ins_sch_date;
				info.latitude = latitude;
				info.longitude = longitude;
				info.nfcTagLatitude = nfc_tag_latitude;
				info.nfcTagLongitude = nfc_tag_longitude;
				info.status = complete_yn;
				info.eqpNm = eqpNm;
				info.eqpNo = eqpNo;
				info.unity_ins_no = unityInsNo;
				info.nfc_tag_id = nfcTagId;
				info.nfc_tag_yn = nfcTagYn;
				info.ins_input_date = ins_input_date;
				info.ins_plan_no = ins_plan_no;
				info.ins_sn = ins_sn;
				info.fnct_lc_no = fnct_lc_no;
				info.address = address;
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "tracks_name : " + tracks_name);
				Log.d("Test", "ins_type : " + ins_type);
				Log.d("Test", "tower_no : " + tower_no);
				Log.d("Test", "ins_sch_date : " + ins_sch_date);
				Log.d("Test", "latitude : " + latitude);
				Log.d("Test", "longitude : " + longitude);
				Log.d("Test", "nfcTagLatitude : " + nfc_tag_latitude);
				Log.d("Test", "nfcTagLongitude : " + nfc_tag_longitude);
				Log.d("Test", "complete_yn : " + complete_yn);
				
				int idx = cursor.getInt(cursor.getColumnIndex(JSInfo.COLS.IDX));
				String weather = cursor.getString(cursor.getColumnIndex(HKInfo.COLS.WEATHER));
				String lightType = cursor.getString(cursor.getColumnIndex(HKInfo.COLS.LIGHT_TYPE));
				String light_no = cursor.getString(cursor.getColumnIndex(HKInfo.COLS.LIGHT_NO));
				String power = cursor.getString(cursor.getColumnIndex(HKInfo.COLS.POWER));
				String light_cnt = cursor.getString(cursor.getColumnIndex(HKInfo.COLS.LIGHT_CNT));
				String light_state = cursor.getString(cursor.getColumnIndex(HKInfo.COLS.LIGHT_STATE));
				String yb_result = cursor.getString(cursor.getColumnIndex(HKInfo.COLS.YB_RESULT));
				
				HKInfo hkInfo = new HKInfo();
				hkInfo.idx = idx;
				hkInfo.weather = weather;
				hkInfo.lightType = lightType;
				hkInfo.light_no = light_no;
				hkInfo.power = power;
				hkInfo.light_cnt = light_cnt;
				hkInfo.light_state = light_state;
				hkInfo.yb_result = yb_result;
				
				if (b4_master_idx == null) {
					output.hkInfoList = new ArrayList<HKInfo>();
					output.hkInfoList.add(hkInfo);
					output.inspectInfo = info;
				} else {
					if (b4_master_idx.equals(master_idx)) {
						output.hkInfoList.add(hkInfo);
					} else {
						outputList.add(output);
						output = new InspectOutput();
						output.hkInfoList = new ArrayList<HKInfo>();
						output.hkInfoList.add(hkInfo);
						output.inspectInfo = info;
					}
				}
					
				b4_master_idx = master_idx;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		if (output.hkInfoList != null)
			outputList.add(output);
		
		return outputList;
	}
	
	public ArrayList<InspectOutput> selectKBOutputList(Context ctx) {
		
		ArrayList<InspectOutput> outputList = new ArrayList<InspectOutput>();
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String insPlanNo = UserInfo.getSessionData(ctx).contract;
			String fmt = "SELECT * FROM INSPECT_RESULT_MASTER MA, INPUT_KB KB WHERE MA.MASTER_IDX = KB.MASTER_IDX AND MA.COMPLETE_YN='Y'"; 
			String sql = String.format(fmt, tableName);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				InspectOutput output = new InspectOutput();
				
				String master_idx = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.MASTER_IDX));
				String tracks_name = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TRACKS_NAME));
				String tower_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TOWER_NO));
				String ins_type = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE));
				String ins_type_nm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE_NM));
				String ins_sch_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SCH_DATE));
				String latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LATITUDE));
				String longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LONGITUDE));
				String complete_yn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.COMPLETE_YN));
				String nfc_tag_latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LATITUDE));
				String nfc_tag_longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LONGITUDE));
				String eqpNm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NM));
				String eqpNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NO));
				String unityInsNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.UNITY_INS_NO));
				String nfcTagId = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_ID));
				String nfcTagYn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_YN));
				String ins_input_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_INPUT_DATE));
				String ins_plan_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_PLAN_NO));
				String ins_sn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SN));
				String fnct_lc_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.FNCT_LC_NO));
				String address = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.ADDRESS));
				
				InspectInfo info = new InspectInfo();
				info.master_idx = master_idx;
				info.tracksName = tracks_name;
				info.towerNo = tower_no;
				info.type = ins_type;
				info.typeName = ins_type_nm;
				info.date = ins_sch_date;
				info.latitude = latitude;
				info.longitude = longitude;
				info.nfcTagLatitude = nfc_tag_latitude;
				info.nfcTagLongitude = nfc_tag_longitude;
				info.status = complete_yn;
				info.eqpNm = eqpNm;
				info.eqpNo = eqpNo;
				info.unity_ins_no = unityInsNo;
				info.nfc_tag_id = nfcTagId;
				info.nfc_tag_yn = nfcTagYn;
				info.ins_input_date = ins_input_date;
				info.ins_plan_no = ins_plan_no;
				info.ins_sn = ins_sn;
				info.fnct_lc_no = fnct_lc_no;
				info.address = address;
				
				output.inspectInfo = info;
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "tracks_name : " + tracks_name);
				Log.d("Test", "ins_type : " + ins_type);
				Log.d("Test", "tower_no : " + tower_no);
				Log.d("Test", "ins_sch_date : " + ins_sch_date);
				Log.d("Test", "latitude : " + latitude);
				Log.d("Test", "longitude : " + longitude);
				Log.d("Test", "nfcTagLatitude : " + nfc_tag_latitude);
				Log.d("Test", "nfcTagLongitude : " + nfc_tag_longitude);
				Log.d("Test", "complete_yn : " + complete_yn);
				
				String weather = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.WEATHER));
				String item_fac_1 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_FAC_1));
				String item_fac_2 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_FAC_2));
				String item_fac_3 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_FAC_3));
				String item_fac_4 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_FAC_4));
				String item_fac_5 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_FAC_5));
				String item_fac_6 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_FAC_6));
				String item_fac_7 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_FAC_7));
				String item_fac_8 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_FAC_8));
				String item_fac_9 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_FAC_9));
				String item_fac_10 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_FAC_10));
				String item_fac_11 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_FAC_11));
				String item_fac_12 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_FAC_12));
				String item_sett_1 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_SETT_1));
				String item_sett_2 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_SETT_2));
				String item_sett_3 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_SETT_3));
				String item_sett_4 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_SETT_4));
				String item_etc_1 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_ETC_1));
				String item_etc_2 = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.ITEM_ETC_2));
				
				output.kbInfo = new KBInfo();
				output.kbInfo.weather = weather;
				output.kbInfo.item_fac_1 = item_fac_1;
				output.kbInfo.item_fac_2 = item_fac_2;
				output.kbInfo.item_fac_3 = item_fac_3;
				output.kbInfo.item_fac_4 = item_fac_4;
				output.kbInfo.item_fac_5 = item_fac_5;
				output.kbInfo.item_fac_6 = item_fac_6;
				output.kbInfo.item_fac_7 = item_fac_7;
				output.kbInfo.item_fac_8 = item_fac_8;
				output.kbInfo.item_fac_9 = item_fac_9;
				output.kbInfo.item_fac_10 = item_fac_10;
				output.kbInfo.item_fac_11 = item_fac_11;
				output.kbInfo.item_fac_12 = item_fac_12;
				output.kbInfo.item_sett_1 = item_sett_1;
				output.kbInfo.item_sett_2 = item_sett_2;
				output.kbInfo.item_sett_3 = item_sett_3;
				output.kbInfo.item_sett_4 = item_sett_4;
				output.kbInfo.item_etc_1 = item_etc_1;
				output.kbInfo.item_etc_2 = item_etc_2;
				
				outputList.add(output);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return outputList;
	}
	
	public ArrayList<InspectOutput> selectHJOutputList(Context ctx) {
		
		ArrayList<InspectOutput> outputList = new ArrayList<InspectOutput>();
		
		InspectOutput output = new InspectOutput();
		
		String b4_master_idx = null;
		
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String insPlanNo = UserInfo.getSessionData(ctx).contract;
			String fmt = "SELECT * FROM INSPECT_RESULT_MASTER MA, INPUT_HJ HJ WHERE MA.MASTER_IDX = HJ.MASTER_IDX AND MA.COMPLETE_YN='Y' ORDER BY MA.MASTER_IDX"; 
			String sql = String.format(fmt, tableName);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				
				String master_idx = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.MASTER_IDX));
				String tracks_name = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TRACKS_NAME));
				String tower_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TOWER_NO));
				String ins_type = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE));
				String ins_type_nm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE_NM));
				String ins_sch_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SCH_DATE));
				String latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LATITUDE));
				String longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LONGITUDE));
				String complete_yn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.COMPLETE_YN));
				String nfc_tag_latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LATITUDE));
				String nfc_tag_longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LONGITUDE));
				String eqpNm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NM));
				String eqpNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NO));
				String unityInsNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.UNITY_INS_NO));
				String nfcTagId = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_ID));
				String nfcTagYn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_YN));
				String ins_input_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_INPUT_DATE));
				String ins_plan_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_PLAN_NO));
				String ins_sn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SN));
				String fnct_lc_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.FNCT_LC_NO));
				String address = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.ADDRESS));
				
				InspectInfo info = new InspectInfo();
				info.master_idx = master_idx;
				info.tracksName = tracks_name;
				info.towerNo = tower_no;
				info.type = ins_type;
				info.typeName = ins_type_nm;
				info.date = ins_sch_date;
				info.latitude = latitude;
				info.longitude = longitude;
				info.nfcTagLatitude = nfc_tag_latitude;
				info.nfcTagLongitude = nfc_tag_longitude;
				info.status = complete_yn;
				info.eqpNm = eqpNm;
				info.eqpNo = eqpNo;
				info.nfc_tag_id = nfcTagId;
				info.nfc_tag_yn = nfcTagYn;
				info.ins_input_date = ins_input_date;
				info.unity_ins_no = unityInsNo;
				info.ins_plan_no = ins_plan_no;
				info.ins_sn = ins_sn;
				info.fnct_lc_no = fnct_lc_no;
				info.address = address;
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "tracks_name : " + tracks_name);
				Log.d("Test", "ins_type : " + ins_type);
				Log.d("Test", "tower_no : " + tower_no);
				Log.d("Test", "ins_sch_date : " + ins_sch_date);
				Log.d("Test", "latitude : " + latitude);
				Log.d("Test", "longitude : " + longitude);
				Log.d("Test", "nfcTagLatitude : " + nfc_tag_latitude);
				Log.d("Test", "nfcTagLongitude : " + nfc_tag_longitude);
				Log.d("Test", "complete_yn : " + complete_yn);
				
				int idx = cursor.getInt(cursor.getColumnIndex(JSInfo.COLS.IDX));
				String weather = cursor.getString(cursor.getColumnIndex(HJInfo.COLS.WEATHER));
				String wether = cursor.getString(cursor.getColumnIndex(HJInfo.COLS.WEATHER));
				String light_type = cursor.getString(cursor.getColumnIndex(HJInfo.COLS.LIGHT_TYPE));
				String power = cursor.getString(cursor.getColumnIndex(HJInfo.COLS.POWER));
				String light_no = cursor.getString(cursor.getColumnIndex(HJInfo.COLS.LIGHT_NO));
				String control = cursor.getString(cursor.getColumnIndex(HJInfo.COLS.CONTROL));
				String sun_battery = cursor.getString(cursor.getColumnIndex(HJInfo.COLS.SUN_BATTERY));
				String storage_battery = cursor.getString(cursor.getColumnIndex(HJInfo.COLS.STORAGE_BATTERY));
				String light_item = cursor.getString(cursor.getColumnIndex(HJInfo.COLS.LIGHT_ITEM));
				String cable = cursor.getString(cursor.getColumnIndex(HJInfo.COLS.CABLE));
				String yb_result = cursor.getString(cursor.getColumnIndex(HJInfo.COLS.YB_RESULT));
				String remarks = cursor.getString(cursor.getColumnIndex(HJInfo.COLS.REMARKS));
				
				HJInfo hjInfo = new HJInfo();
				hjInfo.idx = idx;
				hjInfo.weather = weather;
				hjInfo.light_type = light_type;
				hjInfo.power = power;
				hjInfo.light_no = light_no;
				hjInfo.control = control;
				hjInfo.sun_battery = sun_battery;
				hjInfo.storage_battery = storage_battery;
				hjInfo.light_item = light_item;
				hjInfo.cable = cable;
				hjInfo.yb_result = yb_result;
				hjInfo.remarks = remarks;
				
				if (b4_master_idx == null) {
					output.hjInfoList = new ArrayList<HJInfo>();
					output.hjInfoList.add(hjInfo);
					output.inspectInfo = info;
				} else {
					if (b4_master_idx.equals(master_idx)) {
						output.hjInfoList.add(hjInfo);
					} else {
						outputList.add(output);
						output = new InspectOutput();
						output.hjInfoList = new ArrayList<HJInfo>();
						output.hjInfoList.add(hjInfo);
						output.inspectInfo = info;
					}
				}
					
				b4_master_idx = master_idx;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		if (output.hjInfoList != null)
		outputList.add(output);
		
		return outputList;
	}
	
	public ArrayList<InspectOutput> selectBROutputList(Context ctx) {
		
		ArrayList<InspectOutput> outputList = new ArrayList<InspectOutput>();
		
		InspectOutput output = new InspectOutput();
		
		String b4_master_idx = null;
		
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String insPlanNo = UserInfo.getSessionData(ctx).contract;
			String fmt = "SELECT * FROM INSPECT_RESULT_MASTER MA, INPUT_BR BR WHERE MA.MASTER_IDX = BR.MASTER_IDX AND MA.COMPLETE_YN='Y' ORDER BY MA.MASTER_IDX"; 
			String sql = String.format(fmt, tableName);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				
				String master_idx = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.MASTER_IDX));
				String tracks_name = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TRACKS_NAME));
				String tower_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TOWER_NO));
				String ins_type = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE));
				String ins_type_nm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE_NM));
				String ins_sch_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SCH_DATE));
				String latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LATITUDE));
				String longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LONGITUDE));
				String complete_yn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.COMPLETE_YN));
				String nfc_tag_latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LATITUDE));
				String nfc_tag_longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LONGITUDE));
				String eqpNm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NM));
				String eqpNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NO));
				String nfcTagId = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_ID));
				String nfcTagYn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_YN));
				String ins_input_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_INPUT_DATE));
				String unityInsNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.UNITY_INS_NO));
				String ins_plan_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_PLAN_NO));
				String ins_sn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SN));
				String fnct_lc_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.FNCT_LC_NO));
				String address = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.ADDRESS));
				
				InspectInfo info = new InspectInfo();
				info.master_idx = master_idx;
				info.tracksName = tracks_name;
				info.towerNo = tower_no;
				info.type = ins_type;
				info.typeName = ins_type_nm;
				info.date = ins_sch_date;
				info.latitude = latitude;
				info.longitude = longitude;
				info.nfcTagLatitude = nfc_tag_latitude;
				info.nfcTagLongitude = nfc_tag_longitude;
				info.status = complete_yn;
				info.eqpNm = eqpNm;
				info.eqpNo = eqpNo;
				info.unity_ins_no = unityInsNo;
				info.nfc_tag_id = nfcTagId;
				info.nfc_tag_yn = nfcTagYn;
				info.ins_input_date = ins_input_date;
				info.ins_plan_no = ins_plan_no;
				info.ins_sn = ins_sn;
				info.fnct_lc_no = fnct_lc_no;
				info.address = address;
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "tracks_name : " + tracks_name);
				Log.d("Test", "ins_type : " + ins_type);
				Log.d("Test", "tower_no : " + tower_no);
				Log.d("Test", "ins_sch_date : " + ins_sch_date);
				Log.d("Test", "latitude : " + latitude);
				Log.d("Test", "longitude : " + longitude);
				Log.d("Test", "nfcTagLatitude : " + nfc_tag_latitude);
				Log.d("Test", "nfcTagLongitude : " + nfc_tag_longitude);
				Log.d("Test", "complete_yn : " + complete_yn);
				
				int idx = cursor.getInt(cursor.getColumnIndex(BRInfo.COLS.IDX));
				String weather = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.WEATHER));
				String circuit_no = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.CIRCUIT_NO));
				String circuit_name = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.CIRCUIT_NAME));
				String ej_cnt = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.EJ_CNT));
				String br_cnt = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.BR_CNT));
				String make_date = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.MAKE_DATE));
				String make_company = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.MAKE_COMPANY));
				String yb_result = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.YB_RESULT));
				String ty_secd = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.TY_SECD));
				String ty_secd_nm = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.TY_SECD_NM));
				String phs_secd = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.PHS_SECD));
				String insbty_lft = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.INSBTY_LFT));
				String insbty_rit = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.INSBTY_RIT));
				String insr_eqp_no = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.INSR_EQP_NO));
				
				BRInfo brInfo = new BRInfo();
				
				brInfo.idx = idx;
				brInfo.weather = weather;
				brInfo.circuit_no = circuit_no;
				brInfo.circuit_name = circuit_name;
				brInfo.ej_cnt = ej_cnt;
				brInfo.br_cnt = br_cnt;
				brInfo.make_date = make_date;
				brInfo.make_company = make_company;
				brInfo.yb_result = yb_result;
				brInfo.ty_secd = ty_secd;
				brInfo.ty_secd_nm = ty_secd_nm;
				brInfo.phs_secd = phs_secd;
				brInfo.insbty_lft = insbty_lft;
				brInfo.insbty_rit = insbty_rit;
				brInfo.insr_eqp_no = insr_eqp_no;
				
				if (b4_master_idx == null) {
					output.brInfoList = new ArrayList<BRInfo>();
					output.brInfoList.add(brInfo);
				} else {
					if (b4_master_idx.equals(master_idx)) {
						output.brInfoList.add(brInfo);
						output.inspectInfo = info;
					} else {
						outputList.add(output);
						output = new InspectOutput();
						output.brInfoList = new ArrayList<BRInfo>();
						output.brInfoList.add(brInfo);
						output.inspectInfo = info;
					}
				}
					
				b4_master_idx = master_idx;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		if (output.brInfoList != null)
			outputList.add(output);
		
		return outputList;
	}

	public ArrayList<InspectInfo> selectTowerInsList(String eqp_No) {
		
		ArrayList<InspectInfo> insList = new ArrayList<InspectInfo>();
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s where EQP_NO = '%s' ";
			String sql = String.format(fmt, tableName, eqp_No);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				String master_idx = cursor.getString(0);
				String tracks_name = cursor.getString(1);
				String tower_no= cursor.getString(2);
				String ins_type = cursor.getString(3);
				String ins_sch_date = cursor.getString(4);
				String latitude = cursor.getString(5);
				String longitude = cursor.getString(6);
				String complete_yn = cursor.getString(9);
				String eqpNm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NM));
				String eqpNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NO));
				String ins_type_nm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE_NM));
				String send_yn_ins_type = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.SEND_YN_INS_TYPE));
				
				InspectInfo info = new InspectInfo();
				info.master_idx = master_idx;
				info.tracksName = tracks_name;
				info.towerNo = tower_no;
				info.type = ins_type;
				info.typeName = ins_type_nm;
				info.date = ins_sch_date;
				info.latitude = latitude;
				info.longitude = longitude;
				info.status = complete_yn;
				info.eqpNm = eqpNm;
				info.eqpNo = eqpNo;
				
				if (send_yn_ins_type != null && !"".equals(send_yn_ins_type)){
					/*if (ConstVALUE.CODE_NO_INSPECT_BT.equals(info.type))
						info.send_yn_bt = send_yn_ins_type;
					else*/
					if (ConstVALUE.CODE_NO_INSPECT_JS.equals(info.type))
						info.send_yn_js = send_yn_ins_type;
					else if (ConstVALUE.CODE_NO_INSPECT_JP.equals(info.type))
						info.send_yn_jp = send_yn_ins_type;
					else if (ConstVALUE.CODE_NO_INSPECT_JJ.equals(info.type))
						info.send_yn_jj = send_yn_ins_type;
					else if (ConstVALUE.CODE_NO_INSPECT_HK.equals(info.type))
						info.send_yn_hk = send_yn_ins_type;
					else if (ConstVALUE.CODE_NO_INSPECT_HJ.equals(info.type))
						info.send_yn_hj = send_yn_ins_type;
					else if (ConstVALUE.CODE_NO_INSPECT_KB.equals(info.type))
						info.send_yn_kb = send_yn_ins_type;
					else if (ConstVALUE.CODE_NO_INSPECT_BR.equals(info.type))
						info.send_yn_br = send_yn_ins_type;
				}
				
				insList.add(info);
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "tracks_name : " + tracks_name);
				Log.d("Test", "ins_type : " + ins_type);
				Log.d("Test", "tower_no : " + tower_no);
				Log.d("Test", "ins_sch_date : " + ins_sch_date);
				Log.d("Test", "latitude : " + latitude);
				Log.d("Test", "longitude : " + longitude);
				Log.d("Test", "complete_yn : " + complete_yn);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return insList;
	}


	public ArrayList<InspectInfo> selectTowerInsList(String tracksName, String towerNo) {
		
		ArrayList<InspectInfo> insList = new ArrayList<InspectInfo>();
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s where TRACKS_NAME = '%s' and TOWER_NO = '%s' ";
			String sql = String.format(fmt, tableName, tracksName, towerNo);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				String master_idx = cursor.getString(0);
				String tracks_name = cursor.getString(1);
				String tower_no= cursor.getString(2);
				String ins_type = cursor.getString(3);
				String ins_sch_date = cursor.getString(4);
				String latitude = cursor.getString(5);
				String longitude = cursor.getString(6);
				String complete_yn = cursor.getString(9);
				String eqpNm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NM));
				String eqpNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NO));
				
				InspectInfo info = new InspectInfo();
				info.master_idx = master_idx;
				info.tracksName = tracks_name;
				info.towerNo = tower_no;
				info.type = ins_type;
				info.date = ins_sch_date;
				info.latitude = latitude;
				info.longitude = longitude;
				info.status = complete_yn;
				info.eqpNm = eqpNm;
				info.eqpNo = eqpNo;
				
				insList.add(info);
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "tracks_name : " + tracks_name);
				Log.d("Test", "ins_type : " + ins_type);
				Log.d("Test", "tower_no : " + tower_no);
				Log.d("Test", "ins_sch_date : " + ins_sch_date);
				Log.d("Test", "latitude : " + latitude);
				Log.d("Test", "longitude : " + longitude);
				Log.d("Test", "complete_yn : " + complete_yn);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return insList;
	}
	
	public ArrayList<InspectResultMasterLog> selecTracksNameList() {
		
		ArrayList<InspectResultMasterLog> insList = new ArrayList<InspectResultMasterLog>();
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select tracks_name from %s ";
			String sql = String.format(fmt, tableName);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				String tracks_name = cursor.getString(0);
				
				InspectResultMasterLog info = new InspectResultMasterLog();
				info.tracks_name = tracks_name;
				
				insList.add(info);
				
				Log.d("Test", "tracks_name : " + tracks_name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return insList;
	}
	
	public boolean existBT(String mIdx) {
		
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s where master_idx = %s";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			if (cursor.moveToNext())
				return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return false;
	}

	public void selectEqpNames(Context context, String tracksName) {
		ArrayList<ManageCodeLog> codeList = new ArrayList<ManageCodeLog>();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select distinct EQP_NO, EQP_NM, REPLACE(REPLACE(REPLACE(EQP_NM, TRACKS_NAME, ''),'호 철탑',''),'호 철','') as tower_idx "
					          + "from %s where TRACKS_NAME = '%s' ORDER BY CAST(tower_idx AS INTEGER)";
			String sql = String.format(fmt, tableName, tracksName);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				String eqp_no = cursor.getString(0);
				String eqp_nm = cursor.getString(1);
				
				ManageCodeLog log = new ManageCodeLog();
				log.code_key = eqp_no;
				log.code_value = eqp_nm;
				codeList.add(log);
				
				Logg.d("eqp_nm : " + eqp_no + " / " + eqp_nm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		CodeInfo.getInstance(context).setCodeData(ConstVALUE.CODE_TYPE_EQPS, codeList);
	}
		
	public void dbCheck() {
		String fmt = "select * from %s";
		String sql = String.format(fmt, tableName);
		
		dbCheck(sql);
	}
	
	public void dbCheck(String sql) {
		Log.d("Test", "############ DB value check #############");
		
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {

				String master_idx = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.MASTER_IDX));
				String tracks_name = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TRACKS_NAME));
				String tower_no = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.TOWER_NO));
				String ins_type = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_TYPE));
				String ins_sch_date = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.INS_SCH_DATE));
				String latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LATITUDE));
				String longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.LONGITUDE));
				String complete_yn = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.COMPLETE_YN));
				String nfc_tag_latitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LATITUDE));
				String nfc_tag_longitude = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.NFC_TAG_LONGITUDE));
				String eqpNm = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NM));
				String eqpNo = cursor.getString(cursor.getColumnIndex(InspectInfo.COLS.EQP_NO));
				
				
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "tracks_name" + tracks_name);
				Log.d("Test", "tower_no" + tower_no);
				Log.d("Test", "ins_type" + ins_type);
				Log.d("Test", "ins_sch_date" + ins_sch_date);
				Log.d("Test", "latitude" + latitude);
				Log.d("Test", "longitude" + longitude);
				Log.d("Test", "complete_yn" + complete_yn);
				Log.d("Test", "nfc_tag_latitude : " + nfc_tag_latitude);
				Log.d("Test", "nfc_tag_longitude : " + nfc_tag_longitude);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
}
