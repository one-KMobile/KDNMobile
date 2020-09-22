package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.JGInfo;
import com.kdn.mtps.mobile.input.JGUSubInfo;

import java.util.ArrayList;

public class InputJGDao extends BaseDao{
	static InputJGDao InputJGDao;

	private InputJGDao(Context ctx) {
		tableName = "INPUT_JG";
		this.ctx = ctx;
	}

	public static InputJGDao getInstance(Context ctx) {
		if (InputJGDao == null)
			InputJGDao = new InputJGDao(ctx);
		return InputJGDao;
	}
	
	public void Append(JGInfo row) {
		Append(row, -1);
	}
	
	public void Append(JGInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(JGInfo.COLS.IDX, idx);
		}
		
		updateRow.put(JGInfo.COLS.MASTER_IDX, row.master_idx);
		updateRow.put(JGInfo.COLS.WEATHER, row.weather);
		updateRow.put(JGInfo.COLS.AREA_TEMP, row.area_temp);
		updateRow.put(JGInfo.COLS.CLAIM_CONTENT, row.claim_content);
		updateRow.put(JGInfo.COLS.T_GUBUN, row.t_gubun);
		updateRow.put(JGUSubInfo.COLS.EQP_NM, row.eqp_nm);
		updateRow.put(JGUSubInfo.COLS.UPTLVL_UPLMT, row.uptlvl_uplmt);
		updateRow.put(JGUSubInfo.COLS.UPTLVL_LWLT, row.uptlvl_lwlt);
		updateRow.put(JGUSubInfo.COLS.UPTLVL_INTRCP, row.uptlvl_intrcp);
		updateRow.put(JGUSubInfo.COLS.MNG_01, row.mng_01);
		updateRow.put(JGUSubInfo.COLS.MNG_02, row.mng_02);
		updateRow.put(JGUSubInfo.COLS.SD, row.sd);
		updateRow.put(JGInfo.COLS.T1_C1, row.t1_c1);
		updateRow.put(JGInfo.COLS.T1_C2, row.t1_c2);
		updateRow.put(JGInfo.COLS.T1_C3, row.t1_c3);
		updateRow.put(JGInfo.COLS.T1_C4, row.t1_c4);
		updateRow.put(JGInfo.COLS.T2_C1, row.t2_c1);
		updateRow.put(JGInfo.COLS.T2_C2, row.t2_c2);
		updateRow.put(JGInfo.COLS.T2_C3, row.t2_c3);

		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}
	
	public void DeleteIdx(int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE idx = %s";
		String sql = String.format(fmt, tableName, idx);
		db.execSQL(sql);
	}

	public ArrayList<JGInfo> selectJGU(String mIdx) {

		ArrayList<JGInfo> jguList = new ArrayList<JGInfo>();

		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s and t_gubun = '1'";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(JGInfo.COLS.IDX));
				String master_idx = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.WEATHER));
				String area_temp = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.AREA_TEMP));
				String claim_content = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.CLAIM_CONTENT));

				String eqp_nm = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.EQP_NM));
				String uptlvl_uplmt = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.UPTLVL_UPLMT));
				String uptlvl_lwlt = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.UPTLVL_LWLT));
				String uptlvl_intrcp = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.UPTLVL_INTRCP));
				String mng_01 = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.MNG_01));
				String mng_02 = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.MNG_02));
				String sd = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.SD));
				String t1_c1 = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.T1_C1));
				String t1_c2 = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.T1_C2));
				String t1_c3 = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.T1_C3));
				String t1_c4 = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.T1_C4));

				JGInfo info = new JGInfo();

				info.idx = idx;
				info.master_idx = master_idx;
				info.weather = wether;
				info.area_temp = area_temp;
				info.claim_content = claim_content;

				info.eqp_nm = eqp_nm;
				info.uptlvl_uplmt = uptlvl_uplmt;
				info.uptlvl_lwlt = uptlvl_lwlt;
				info.uptlvl_intrcp = uptlvl_intrcp;
				info.mng_01 = mng_01;
				info.mng_02 = mng_02;
				info.sd = sd;
				info.t1_c1= t1_c1;
				info.t1_c2= t1_c2;
				info.t1_c3= t1_c3;
				info.t1_c4= t1_c4;

				jguList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return jguList;
	}

	public ArrayList<JGInfo> selectJGP(String mIdx) {

		ArrayList<JGInfo> jsList = new ArrayList<JGInfo>();

		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s and t_gubun = '2'";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(JGInfo.COLS.IDX));
				String master_idx = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.WEATHER));
				String area_temp = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.AREA_TEMP));

				String fnct_lc_no = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.FNCT_LC_NO));
				String fnct_lc_dtls = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.FNCT_LC_DTLS));
				String eqp_no = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.EQP_NO));
				String eqp_nm = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.EQP_NM));
				String t2_c1 = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.T2_C1));
				String t2_c2 = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.T2_C2));
				String t2_c3 = cursor.getString(cursor.getColumnIndex(JGInfo.COLS.T2_C3));

				JGInfo info = new JGInfo();

				info.idx = idx;
				info.master_idx = master_idx;
				info.weather = wether;
				info.area_temp = area_temp;

				info.fnct_lc_no = fnct_lc_no;
				info.fnct_lc_dtls = fnct_lc_dtls;
				info.eqp_no = eqp_no;
				info.eqp_nm = eqp_nm;
				info.t2_c1= t2_c1;
				info.t2_c2= t2_c2;
				info.t2_c3= t2_c3;

				jsList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return jsList;
	}

	public boolean existJG(String mIdx) {
		
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

	public void dbCheck() {
		Log.d("Test", "############ DB value check #############");
		
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s";
			String sql = String.format(fmt, tableName);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(0);
				String master_idx = cursor.getString(1);
				String wether = cursor.getString(2);
				String worker_cnt = cursor.getString(3);
				String claim_content = cursor.getString(4);
				String check_result = cursor.getString(5);
				String etc = cursor.getString(6);
//				int ins_result_code = cursor.getInt(2);
//				int check_result_code = cursor.getInt(3);
//				int eqp_type_code = cursor.getInt(4);
//				int detail_item_code = cursor.getInt(5);
//				String spt_mgt_yn = cursor.getString(6);
				
				//Log.d("Test", "idx : " + idx);
				//Log.d("Test", "master_idx : " + master_idx);
				//Log.d("Test", "wether : " + wether);
				//Log.d("Test", "worker_cnt : " + worker_cnt);
				//Log.d("Test", "claim_content : " + claim_content);
				//Log.d("Test", "check_result : " + check_result);
				//Log.d("Test", "etc : " + etc);
//				Log.d("Test", "ins_result_code : " + ins_result_code);
//				Log.d("Test", "check_result_code : " + check_result_code);
//				Log.d("Test", "eqp_type_code : " + eqp_type_code);
//				Log.d("Test", "detail_item_code : " + detail_item_code);
//				Log.d("Test", "spt_mgt_yn : " + spt_mgt_yn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}
}
