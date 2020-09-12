package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.GHInfo;

import java.util.ArrayList;

public class InputGHDao extends BaseDao{
	static InputGHDao InputJGDao;

	private InputGHDao(Context ctx) {
		tableName = "INPUT_GH";
		this.ctx = ctx;
	}

	public static InputGHDao getInstance(Context ctx) {
		if (InputJGDao == null)
			InputJGDao = new InputGHDao(ctx);
		return InputJGDao;
	}
	
	public void Append(GHInfo row) {
		Append(row, -1);
	}
	
	public void Append(GHInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(GHInfo.COLS.IDX, idx);
		}
		
		updateRow.put(GHInfo.COLS.MASTER_IDX, row.master_idx);
		updateRow.put(GHInfo.COLS.WEATHER, row.weather);
		updateRow.put(GHInfo.COLS.AREA_TEMP, row.area_temp);
		updateRow.put(GHInfo.COLS.CIRCUIT_NO, row.circuit_no);
		updateRow.put(GHInfo.COLS.CIRCUIT_NAME, row.circuit_name);
		updateRow.put(GHInfo.COLS.CURRENT_LOAD, row.current_load);
		updateRow.put(GHInfo.COLS.CONDUCTOR_CNT, row.conductor_cnt);
		updateRow.put(GHInfo.COLS.LOCATION, row.location);
		updateRow.put(GHInfo.COLS.C1, row.c1);
		updateRow.put(GHInfo.COLS.C2, row.c2);
		updateRow.put(GHInfo.COLS.C3, row.c3);
		updateRow.put(GHInfo.COLS.C4, row.c4);
		updateRow.put(GHInfo.COLS.C5, row.c5);
		updateRow.put(GHInfo.COLS.C6, row.c6);
		updateRow.put(GHInfo.COLS.C7, row.c7);
		updateRow.put(GHInfo.COLS.C8, row.c8);
		updateRow.put(GHInfo.COLS.C9, row.c9);

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
	
	public ArrayList<GHInfo> selectGH(String mIdx) {
		
		ArrayList<GHInfo> jsList = new ArrayList<GHInfo>();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(GHInfo.COLS.IDX));
				String master_idx = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.WEATHER));
				String area_temp = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.AREA_TEMP));
				
				String circuit_no = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.CIRCUIT_NO));
				String circuit_name = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.CIRCUIT_NAME));
				String current_load = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.CURRENT_LOAD));
				String conductor_cnt = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.CONDUCTOR_CNT));
				String location = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.LOCATION));
				String c1 = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.C1));
				String c2 = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.C2));
				String c3 = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.C3));
				String c4 = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.C4));
				String c5 = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.C5));
				String c6 = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.C6));
				String c7 = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.C7));
				String c8 = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.C8));
				String c9 = cursor.getString(cursor.getColumnIndex(GHInfo.COLS.C9));
				
				GHInfo info = new GHInfo();
				
				info.idx = idx;
				info.master_idx = master_idx;
				info.weather = wether;
				info.area_temp = area_temp;
				
				info.circuit_no = circuit_no;
				info.circuit_name = circuit_name;
				info.current_load = current_load;
				info.conductor_cnt = conductor_cnt;
				info.location = location;
				info.c1= c1;
				info.c2 = c2;
				info.c3 = c3;
				info.c4 = c4;
				info.c5 = c5;
				info.c6 = c6;
				info.c7 = c7;
				info.c8 = c8;
				info.c9 = c9;
				
				jsList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return jsList;
	}

	public boolean existGH(String mIdx) {
		
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
				
				Log.d("Test", "idx : " + idx);
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "wether : " + wether);
				Log.d("Test", "worker_cnt : " + worker_cnt);
				Log.d("Test", "claim_content : " + claim_content);
				Log.d("Test", "check_result : " + check_result);
				Log.d("Test", "etc : " + etc);
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
