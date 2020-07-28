package com.kdn.mtps.mobile.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.db.bean.InputBTLog;
import com.kdn.mtps.mobile.input.HKInfo;

public class InputHKDao extends BaseDao{
	static InputHKDao inputHKDao;

	private InputHKDao(Context ctx) {
		tableName = "INPUT_HK";
		this.ctx = ctx;
	}

	public static InputHKDao getInstance(Context ctx) {
		if (inputHKDao == null)
			inputHKDao = new InputHKDao(ctx);
		return inputHKDao;
	}
	
	public void Append(HKInfo row) {
		Append(row, -1);
	}
	
	public void Append(HKInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(InputBTLog.COLS.IDX, idx);
		}
		
		updateRow.put(HKInfo.COLS.MASTER_IDX, row.masterIdx);
		updateRow.put(HKInfo.COLS.WEATHER, row.weather);
		updateRow.put(HKInfo.COLS.LIGHT_TYPE, row.lightType);
		updateRow.put(HKInfo.COLS.LIGHT_NO, row.light_no);
		updateRow.put(HKInfo.COLS.POWER, row.power);
		updateRow.put(HKInfo.COLS.LIGHT_CNT, row.light_cnt);
		updateRow.put(HKInfo.COLS.LIGHT_STATE, row.light_state);
		updateRow.put(HKInfo.COLS.YB_RESULT, row.yb_result);
		
		db.replace(tableName, null, updateRow);

		dbCheck();
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
	
	public ArrayList<HKInfo> selectHK(String mIdx) {
		
		ArrayList<HKInfo> hkList = new ArrayList<HKInfo>();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(HKInfo.COLS.IDX));
				String master_idx = cursor.getString(cursor.getColumnIndex(HKInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(HKInfo.COLS.WEATHER));
				String lightType = cursor.getString(cursor.getColumnIndex(HKInfo.COLS.LIGHT_TYPE));
				String light_no = cursor.getString(cursor.getColumnIndex(HKInfo.COLS.LIGHT_NO));
				String power = cursor.getString(cursor.getColumnIndex(HKInfo.COLS.POWER));
				String light_cnt = cursor.getString(cursor.getColumnIndex(HKInfo.COLS.LIGHT_CNT));
				String light_state = cursor.getString(cursor.getColumnIndex(HKInfo.COLS.LIGHT_STATE));
				String yb_result = cursor.getString(cursor.getColumnIndex(HKInfo.COLS.YB_RESULT));
				
				HKInfo info = new HKInfo();
				
				info.idx = idx;
				info.masterIdx = master_idx;
				info.weather = wether;
				info.lightType = lightType;
				info.light_no = light_no;
				info.power = power;
				info.light_cnt = light_cnt;
				info.light_state = light_state;
				info.yb_result = yb_result;
				
				hkList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return hkList;
	}

	public boolean existHK(String mIdx) {
		
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}
}
