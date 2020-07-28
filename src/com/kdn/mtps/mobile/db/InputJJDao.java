package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.JJInfo;

public class InputJJDao extends BaseDao{
	static InputJJDao inputJJDao;

	private InputJJDao(Context ctx) {
		tableName = "INPUT_JJ";
		this.ctx = ctx;
	}

	public static InputJJDao getInstance(Context ctx) {
		if (inputJJDao == null)
			inputJJDao = new InputJJDao(ctx);
		return inputJJDao;
	}
	
	public void Append(JJInfo row) {
		Append(row, -1);
	}
	
	public void Append(JJInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(JJInfo.COLS.IDX, idx);
		}
		
		updateRow.put(JJInfo.COLS.MASTER_IDX, row.master_idx);
		updateRow.put(JJInfo.COLS.WEATHER, row.weather);
		updateRow.put(JJInfo.COLS.GROUND, row.ground);
		updateRow.put(JJInfo.COLS.SPAN_LENGTH, row.span_length);
		updateRow.put(JJInfo.COLS.CHECK_RESULT, row.check_result);
		updateRow.put(JJInfo.COLS.REMARKS, row.remarks);
		updateRow.put(JJInfo.COLS.COUNT_1, row.count_1);
		updateRow.put(JJInfo.COLS.TERMINAL1_1, row.terminal1_1);
		updateRow.put(JJInfo.COLS.TERMINAL1_2, row.terminal1_2);
		updateRow.put(JJInfo.COLS.TERMINAL1_3, row.terminal1_3);
		updateRow.put(JJInfo.COLS.TERMINAL1_5, row.terminal1_5);
		updateRow.put(JJInfo.COLS.TERMINAL1_10, row.terminal1_10);

		db.replace(tableName, null, updateRow);

		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}
	
	public JJInfo selectJJ(String mIdx) {
		
		JJInfo info = new JJInfo();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(JJInfo.COLS.IDX));
				String master_idx = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.WEATHER));
				String ground = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.GROUND));
				String span_length = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.SPAN_LENGTH));
				String check_result = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.CHECK_RESULT));
				String remarks = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.REMARKS));
				String count_1 = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.COUNT_1));
				String terminal1_1 = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.TERMINAL1_1));
				String terminal1_2 = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.TERMINAL1_2));
				String terminal1_3 = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.TERMINAL1_3));
				String terminal1_5 = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.TERMINAL1_5));
				String terminal1_10 = cursor.getString(cursor.getColumnIndex(JJInfo.COLS.TERMINAL1_10));
				
				
				info.idx = idx;
				info.master_idx = master_idx;
				info.weather = wether;
				info.ground = ground;
				info.span_length = span_length;
				info.check_result = check_result;
				info.remarks = remarks;
				info.count_1 = count_1;
				info.terminal1_1 = terminal1_1;
				info.terminal1_2 = terminal1_2;
				info.terminal1_3 = terminal1_3;
				info.terminal1_5 = terminal1_5;
				info.terminal1_10 = terminal1_10;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return info;
	}

	public boolean existJJ(String mIdx) {
		
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
