package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.PRInfo;

import java.util.ArrayList;

public class InputPRDao extends BaseDao{
	static InputPRDao InputPRDao;

	private InputPRDao(Context ctx) {
		tableName = "INPUT_PR";
		this.ctx = ctx;
	}

	public static InputPRDao getInstance(Context ctx) {
		if (InputPRDao == null)
			InputPRDao = new InputPRDao(ctx);
		return InputPRDao;
	}
	
	public void Append(PRInfo row) {
		Append(row, -1);
	}
	
	public void Append(PRInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(PRInfo.COLS.IDX, idx);
		}
		
		updateRow.put(PRInfo.COLS.MASTER_IDX, row.master_idx);
		updateRow.put(PRInfo.COLS.WEATHER, row.weather);
		updateRow.put(PRInfo.COLS.AREA_TEMP, row.area_temp);
		updateRow.put(PRInfo.COLS.CIRCUIT_NO, row.circuit_no);
		updateRow.put(PRInfo.COLS.CIRCUIT_NAME, row.circuit_name);
		updateRow.put(PRInfo.COLS.CURRENT_LOAD, row.current_load);
		updateRow.put(PRInfo.COLS.CONDUCTOR_CNT, row.conductor_cnt);
		updateRow.put(PRInfo.COLS.LOCATION, row.location);
		updateRow.put(PRInfo.COLS.CLAIM_CONTENT, row.claim_content);
		updateRow.put(PRInfo.COLS.T_GUBUN, row.t_gubun);
		updateRow.put(PRInfo.COLS.T1_C1, row.t1_c1);
		updateRow.put(PRInfo.COLS.T1_C2, row.t1_c2);
		updateRow.put(PRInfo.COLS.T1_C3, row.t1_c3);
		updateRow.put(PRInfo.COLS.T1_C4, row.t1_c4);
		updateRow.put(PRInfo.COLS.T2_C1, row.t2_c1);
		updateRow.put(PRInfo.COLS.T2_C2, row.t2_c2);
		updateRow.put(PRInfo.COLS.T2_C3, row.t2_c3);
		updateRow.put(PRInfo.COLS.YB_RESULT, row.yb_result);

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

	public ArrayList<PRInfo> selectPRP(String mIdx) {

		ArrayList<PRInfo> jsList = new ArrayList<PRInfo>();

		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s and t_gubun = '1'";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(PRInfo.COLS.IDX));
				String master_idx = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.WEATHER));
				String area_temp = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.AREA_TEMP));

				String circuit_no = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.CIRCUIT_NO));
				String circuit_name = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.CIRCUIT_NAME));
				String current_load = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.CURRENT_LOAD));
				String conductor_cnt = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.CONDUCTOR_CNT));
				String location = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.LOCATION));
				String claim_content = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.CLAIM_CONTENT));
				String t1_c1 = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.T1_C1));
				String t1_c2 = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.T1_C2));
				String t1_c3 = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.T1_C3));
				String t1_c4 = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.T1_C4));
				String yb_result = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.YB_RESULT));

				PRInfo info = new PRInfo();

				info.idx = idx;
				info.master_idx = master_idx;
				info.weather = wether;
				info.area_temp = area_temp;

				info.circuit_no = circuit_no;
				info.circuit_name = circuit_name;
				info.current_load = current_load;
				info.conductor_cnt = conductor_cnt;
				info.claim_content = claim_content;
				info.location = location;
				info.t1_c1= t1_c1;
				info.t1_c2= t1_c2;
				info.t1_c3= t1_c3;
				info.t1_c4= t1_c4;
				info.yb_result= yb_result;

				jsList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return jsList;
	}

	public ArrayList<PRInfo> selectPRK(String mIdx) {

		ArrayList<PRInfo> jsList = new ArrayList<PRInfo>();

		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s and t_gubun = '2'";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(PRInfo.COLS.IDX));
				String master_idx = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.WEATHER));
				String area_temp = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.AREA_TEMP));

				String circuit_no = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.CIRCUIT_NO));
				String circuit_name = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.CIRCUIT_NAME));
				String current_load = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.CURRENT_LOAD));
				String conductor_cnt = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.CONDUCTOR_CNT));
				String location = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.LOCATION));
				String t2_c1 = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.T2_C1));
				String t2_c2 = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.T2_C2));
				String t2_c3 = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.T2_C3));
				String yb_result = cursor.getString(cursor.getColumnIndex(PRInfo.COLS.YB_RESULT));

				PRInfo info = new PRInfo();

				info.idx = idx;
				info.master_idx = master_idx;
				info.weather = wether;
				info.area_temp = area_temp;

				info.circuit_no = circuit_no;
				info.circuit_name = circuit_name;
				info.current_load = current_load;
				info.conductor_cnt = conductor_cnt;
				info.location = location;
				info.t2_c1= t2_c1;
				info.t2_c2= t2_c2;
				info.t2_c3= t2_c3;
				info.yb_result= yb_result;

				jsList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return jsList;
	}

	public boolean existPR(String mIdx) {
		
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
