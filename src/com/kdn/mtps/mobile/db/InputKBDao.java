package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.KBInfo;

public class InputKBDao extends BaseDao{
	static InputKBDao inputKBDao;

	private InputKBDao(Context ctx) {
		tableName = "INPUT_KB";
		this.ctx = ctx;
	}

	public static InputKBDao getInstance(Context ctx) {
		if (inputKBDao == null)
			inputKBDao = new InputKBDao(ctx);
		return inputKBDao;
	}
	
	public void Append(KBInfo row) {
		Append(row, -1);
	}
	
	public void Append(KBInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(KBInfo.COLS.IDX, idx);
		}
		
		updateRow.put(KBInfo.COLS.MASTER_IDX, row.master_idx);
		updateRow.put(KBInfo.COLS.WEATHER, row.weather);
		updateRow.put(KBInfo.COLS.ITEM_FAC_1, row.item_fac_1);
		updateRow.put(KBInfo.COLS.ITEM_FAC_2, row.item_fac_2);
		updateRow.put(KBInfo.COLS.ITEM_FAC_3, row.item_fac_3);
		updateRow.put(KBInfo.COLS.ITEM_FAC_4, row.item_fac_4);
		updateRow.put(KBInfo.COLS.ITEM_FAC_5, row.item_fac_5);
		updateRow.put(KBInfo.COLS.ITEM_FAC_6, row.item_fac_6);
		updateRow.put(KBInfo.COLS.ITEM_FAC_7, row.item_fac_7);
		updateRow.put(KBInfo.COLS.ITEM_FAC_8, row.item_fac_8);
		updateRow.put(KBInfo.COLS.ITEM_FAC_9, row.item_fac_9);
		updateRow.put(KBInfo.COLS.ITEM_FAC_10, row.item_fac_10);
		updateRow.put(KBInfo.COLS.ITEM_FAC_11, row.item_fac_11);
		updateRow.put(KBInfo.COLS.ITEM_FAC_12, row.item_fac_12);
		updateRow.put(KBInfo.COLS.ITEM_SETT_1, row.item_sett_1);
		updateRow.put(KBInfo.COLS.ITEM_SETT_2, row.item_sett_2);
		updateRow.put(KBInfo.COLS.ITEM_SETT_3, row.item_sett_3);
		updateRow.put(KBInfo.COLS.ITEM_SETT_4, row.item_sett_4);
		updateRow.put(KBInfo.COLS.ITEM_ETC_1, row.item_etc_1);
		updateRow.put(KBInfo.COLS.ITEM_ETC_2, row.item_etc_2);
		

		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}
	
	public KBInfo selectKB(String mIdx) {
		
		KBInfo info = new KBInfo();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(KBInfo.COLS.IDX));
				String master_idx = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(KBInfo.COLS.WEATHER));
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
				
				info.idx = idx;
				info.master_idx = master_idx;
				info.weather = wether;
				info.item_fac_1 = item_fac_1;
				info.item_fac_2 = item_fac_2;
				info.item_fac_3 = item_fac_3;
				info.item_fac_4 = item_fac_4;
				info.item_fac_5 = item_fac_5;
				info.item_fac_6 = item_fac_6;
				info.item_fac_7 = item_fac_7;
				info.item_fac_8 = item_fac_8;
				info.item_fac_9 = item_fac_9;
				info.item_fac_10 = item_fac_10;
				info.item_fac_11 = item_fac_11;
				info.item_fac_12 = item_fac_12;
				info.item_sett_1 = item_sett_1;
				info.item_sett_2 = item_sett_2;
				info.item_sett_3 = item_sett_3;
				info.item_sett_4 = item_sett_4;
				info.item_etc_1 = item_etc_1;
				info.item_etc_2 = item_etc_2;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return info;
	}

	public boolean existKB(String mIdx) {
		
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
//				int detail_item_fac_code = cursor.getInt(5);
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
//				Log.d("Test", "detail_item_fac_code : " + detail_item_fac_code);
//				Log.d("Test", "spt_mgt_yn : " + spt_mgt_yn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}
}
