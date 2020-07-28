package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.YBInfo;

public class InputYBDao extends BaseDao{
	static InputYBDao inputYBDao;

	private InputYBDao(Context ctx) {
		tableName = "INPUT_YB";
		this.ctx = ctx;
	}

	public static InputYBDao getInstance(Context ctx) {
		if (inputYBDao == null)
			inputYBDao = new InputYBDao(ctx);
		return inputYBDao;
	}
	
	public void Append(YBInfo row) {
		Append(row, -1);
	}
	
	public void Append(YBInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(YBInfo.COLS.IDX, idx);
		}
		
		updateRow.put(YBInfo.COLS.MASTER_IDX, row.masterIdx);
		updateRow.put(YBInfo.COLS.WEATHER, row.weather);
//		updateRow.put(YBInfo.COLS.EQP_NO_FRONT, row.eqp_no_front);
//		updateRow.put(YBInfo.COLS.EQP_NO_BACK, row.eqp_no_back);
//		updateRow.put(YBInfo.COLS.SITE_NAME, row.site_name);
//		updateRow.put(YBInfo.COLS.INSPECT_RESULT, row.inspect_result);
//		updateRow.put(YBInfo.COLS.CHECK_CONTENT, row.check_content);
//		updateRow.put(YBInfo.COLS.REMARKS, row.remarks);
		
		db.replace(tableName, null, updateRow);

		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}
	
	public YBInfo selectYB(String mIdx) {
		
		YBInfo info = new YBInfo();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				
				int idx = cursor.getInt(cursor.getColumnIndex(YBInfo.COLS.IDX));
				int master_idx = cursor.getInt(cursor.getColumnIndex(YBInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(YBInfo.COLS.WEATHER));
//				String eqp_no_front = cursor.getString(cursor.getColumnIndex(YBInfo.COLS.EQP_NO_FRONT));
//				String eqp_no_back = cursor.getString(cursor.getColumnIndex(YBInfo.COLS.EQP_NO_BACK));
//				String site_name = cursor.getString(cursor.getColumnIndex(YBInfo.COLS.SITE_NAME));
//				String inspect_result = cursor.getString(cursor.getColumnIndex(YBInfo.COLS.INSPECT_RESULT));
//				String check_content = cursor.getString(cursor.getColumnIndex(YBInfo.COLS.CHECK_CONTENT));
//				String remarks = cursor.getString(cursor.getColumnIndex(YBInfo.COLS.REMARKS));
//				
//				info.idx = idx;
//				info.weather = wether;
//				info.eqp_no_front = eqp_no_front;
//				info.eqp_no_back = eqp_no_back;
//				info.site_name = site_name;
//				info.inspect_result = inspect_result;
//				info.check_content = check_content;
//				info.remarks = remarks;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return info;
	}

	public boolean existYB(String mIdx) {
		
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
				int master_idx = cursor.getInt(1);
				String wether = cursor.getString(2);
				String startDate = cursor.getString(3);
				String endDate = cursor.getString(4);
				String lightType = cursor.getString(5);
				String category = cursor.getString(6);
				String controlCnt = cursor.getString(7);
				String lightCnt = cursor.getString(8);
				String lightStateTop = cursor.getString(9);
				String lightStateMiddle = cursor.getString(10);
				String lightStateBottom = cursor.getString(11);
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "idx : " + idx);
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "wether : " + wether);
				Log.d("Test", "startDate : " + startDate);
				Log.d("Test", "endDate : " + endDate);
				Log.d("Test", "lightType : " + lightType);
				Log.d("Test", "category : " + category);
				Log.d("Test", "controlCnt : " + controlCnt);
				Log.d("Test", "lightCnt : " + lightCnt);
				Log.d("Test", "lightStateTop : " + lightStateTop);
				Log.d("Test", "lightStateMiddle : " + lightStateMiddle);
				Log.d("Test", "lightStateBottom : " + lightStateBottom);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}
}
