package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.MHInfo;
import com.kdn.mtps.mobile.util.Logg;

import java.util.ArrayList;

public class InputMHDao extends BaseDao{
	static InputMHDao InputMHDao;

	private InputMHDao(Context ctx) {
		tableName = "INPUT_MH";
		this.ctx = ctx;
	}

	public static InputMHDao getInstance(Context ctx) {
		if (InputMHDao == null)
			InputMHDao = new InputMHDao(ctx);
		return InputMHDao;
	}
	
	public void Append(MHInfo row) {
		Append(row, -1);
	}
	
	public void Append(MHInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(MHInfo.COLS.IDX, idx);
		}
		
		updateRow.put(MHInfo.COLS.MASTER_IDX, row.master_idx);
		updateRow.put(MHInfo.COLS.WEATHER, row.weather);
		updateRow.put(MHInfo.COLS.AREA_TEMP, row.area_temp);
		updateRow.put(MHInfo.COLS.CIRCUIT_NO, row.circuit_no);
		updateRow.put(MHInfo.COLS.CIRCUIT_NAME, row.circuit_name);
		updateRow.put(MHInfo.COLS.CURRENT_LOAD, row.current_load);
		updateRow.put(MHInfo.COLS.CONDUCTOR_CNT, row.conductor_cnt);
		updateRow.put(MHInfo.COLS.LOCATION, row.location);
		updateRow.put(MHInfo.COLS.CLAIM_CONTENT, row.claim_content);
		updateRow.put(MHInfo.COLS.T_GUBUN, row.t_gubun);
		updateRow.put(MHInfo.COLS.T1_C1, row.t1_c1);
		updateRow.put(MHInfo.COLS.T1_C2, row.t1_c2);
		updateRow.put(MHInfo.COLS.T1_C3, row.t1_c3);
		updateRow.put(MHInfo.COLS.T1_C4, row.t1_c4);
		updateRow.put(MHInfo.COLS.T1_C5, row.t1_c5);
		updateRow.put(MHInfo.COLS.T1_C6, row.t1_c6);
		updateRow.put(MHInfo.COLS.T1_C7, row.t1_c7);
		updateRow.put(MHInfo.COLS.T1_C8, row.t1_c8);
		updateRow.put(MHInfo.COLS.T2_C1, row.t2_c1);
		updateRow.put(MHInfo.COLS.T2_C2, row.t2_c2);
		updateRow.put(MHInfo.COLS.T2_C3, row.t2_c3);
		updateRow.put(MHInfo.COLS.T2_C4, row.t2_c4);
		updateRow.put(MHInfo.COLS.T2_C5, row.t2_c5);
		updateRow.put(MHInfo.COLS.T2_C6, row.t2_c6);
		updateRow.put(MHInfo.COLS.T2_C7, row.t2_c7);
		updateRow.put(MHInfo.COLS.T2_C8, row.t2_c8);
		updateRow.put(MHInfo.COLS.G_GUBUN, row.g_gubun);
		updateRow.put(MHInfo.COLS.T3_C1, row.t3_c1);
		updateRow.put(MHInfo.COLS.T3_C2, row.t3_c2);
		updateRow.put(MHInfo.COLS.T3_C3, row.t3_c3);
		updateRow.put(MHInfo.COLS.T3_C4, row.t3_c4);
		updateRow.put(MHInfo.COLS.T3_C5, row.t3_c5);
		updateRow.put(MHInfo.COLS.T3_C6, row.t3_c6);
		updateRow.put(MHInfo.COLS.T3_C7, row.t3_c7);
		updateRow.put(MHInfo.COLS.T3_C8, row.t3_c8);
		updateRow.put(MHInfo.COLS.T3_C9, row.t3_c9);
		updateRow.put(MHInfo.COLS.T3_C10, row.t3_c10);

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

	public ArrayList<MHInfo> selectMHI(String mIdx) {

		ArrayList<MHInfo> jsList = new ArrayList<MHInfo>();

		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s and t_gubun = '1'";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(MHInfo.COLS.IDX));
				String master_idx = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.WEATHER));
				String area_temp = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.AREA_TEMP));

				String circuit_no = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.CIRCUIT_NO));
				String circuit_name = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.CIRCUIT_NAME));
				String current_load = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.CURRENT_LOAD));
				String conductor_cnt = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.CONDUCTOR_CNT));
				String location = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.LOCATION));
				String claim_content = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.CLAIM_CONTENT));
				String t1_c1 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T1_C1));
				String t1_c2 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T1_C2));
				String t1_c3 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T1_C3));
				String t1_c4 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T1_C4));
				String t1_c5 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T1_C5));
				String t1_c6 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T1_C6));
				String t1_c7 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T1_C7));
				String t1_c8 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T1_C8));

				MHInfo info = new MHInfo();

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
				info.t1_c5= t1_c5;
				info.t1_c6= t1_c6;
				info.t1_c7= t1_c7;
				info.t1_c8= t1_c8;

				jsList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return jsList;
	}

	public ArrayList<MHInfo> selectMHB(String mIdx) {

		ArrayList<MHInfo> jsList = new ArrayList<MHInfo>();

		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s and t_gubun = '2'";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(MHInfo.COLS.IDX));
				String master_idx = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.WEATHER));
				String area_temp = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.AREA_TEMP));

				String circuit_no = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.CIRCUIT_NO));
				String circuit_name = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.CIRCUIT_NAME));
				String current_load = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.CURRENT_LOAD));
				String conductor_cnt = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.CONDUCTOR_CNT));
				String location = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.LOCATION));
				String claim_content = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.CLAIM_CONTENT));
				String t2_c1 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T2_C1));
				String t2_c2 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T2_C2));
				String t2_c3 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T2_C3));
				String t2_c4 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T2_C4));
				String t2_c5 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T2_C5));
				String t2_c6 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T2_C6));
				String t2_c7 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T2_C7));
				String t2_c8 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T2_C8));

				MHInfo info = new MHInfo();

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
				info.t2_c1= t2_c1;
				info.t2_c2= t2_c2;
				info.t2_c3= t2_c3;
				info.t2_c4= t2_c4;
				info.t2_c5= t2_c5;
				info.t2_c6= t2_c6;
				info.t2_c7= t2_c7;
				info.t2_c8= t2_c8;

				jsList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return jsList;
	}

	public ArrayList<MHInfo> selectMHG(String mIdx) {

		ArrayList<MHInfo> jsList = new ArrayList<MHInfo>();

		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s and t_gubun = '3'";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(MHInfo.COLS.IDX));
				String master_idx = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.WEATHER));
				String area_temp = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.AREA_TEMP));

				String circuit_no = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.CIRCUIT_NO));
				String circuit_name = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.CIRCUIT_NAME));
				String current_load = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.CURRENT_LOAD));
				String conductor_cnt = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.CONDUCTOR_CNT));
				String location = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.LOCATION));
				String claim_content = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.CLAIM_CONTENT));
				String t3_c1 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T3_C1));
				String t3_c2 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T3_C2));
				String t3_c3 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T3_C3));
				String t3_c4 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T3_C4));
				String t3_c5 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T3_C5));
				String t3_c6 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T3_C6));
				String t3_c7 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T3_C7));
				String t3_c8 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T3_C8));
				String t3_c9 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T3_C9));
				String t3_c10 = cursor.getString(cursor.getColumnIndex(MHInfo.COLS.T3_C10));

				MHInfo info = new MHInfo();

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
				info.t3_c1= t3_c1;
				info.t3_c2= t3_c2;
				info.t3_c3= t3_c3;
				info.t3_c4= t3_c4;
				info.t3_c5= t3_c5;
				info.t3_c6= t3_c6;
				info.t3_c7= t3_c7;
				info.t3_c8= t3_c8;
				info.t3_c9= t3_c9;
				info.t3_c10= t3_c10;

				jsList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return jsList;
	}

	public boolean existMH(String mIdx) {
		
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
