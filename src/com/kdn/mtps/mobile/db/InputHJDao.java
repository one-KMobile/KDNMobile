package com.kdn.mtps.mobile.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.HJInfo;
import com.kdn.mtps.mobile.input.JSInfo;

public class InputHJDao extends BaseDao{
	static InputHJDao inputHJDao;

	private InputHJDao(Context ctx) {
		tableName = "INPUT_HJ";
		this.ctx = ctx;
	}

	public static InputHJDao getInstance(Context ctx) {
		if (inputHJDao == null)
			inputHJDao = new InputHJDao(ctx);
		return inputHJDao;
	}
	
	public void Append(HJInfo row) {
		Append(row, -1);
	}
	
	public void Append(HJInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(HJInfo.COLS.IDX, idx);
		}
		
		updateRow.put(HJInfo.COLS.MASTER_IDX, row.masterIdx);
		updateRow.put(HJInfo.COLS.WEATHER, row.weather);
		updateRow.put(HJInfo.COLS.LIGHT_TYPE, row.light_type);
		updateRow.put(HJInfo.COLS.POWER, row.power);
		updateRow.put(HJInfo.COLS.LIGHT_NO, row.light_no);
		updateRow.put(HJInfo.COLS.CONTROL, row.control);
		updateRow.put(HJInfo.COLS.SUN_BATTERY, row.sun_battery);
		updateRow.put(HJInfo.COLS.STORAGE_BATTERY, row.storage_battery);
		updateRow.put(HJInfo.COLS.LIGHT_ITEM, row.light_item);
		updateRow.put(HJInfo.COLS.CABLE, row.cable);
		updateRow.put(HJInfo.COLS.YB_RESULT, row.yb_result);
		updateRow.put(HJInfo.COLS.REMARKS, row.remarks);
		
		
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
	
	public ArrayList<HJInfo> selectHJ(String mIdx) {
		
		ArrayList<HJInfo> hjList = new ArrayList<HJInfo>();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				
				int idx = cursor.getInt(cursor.getColumnIndex(HJInfo.COLS.IDX));
				int master_idx = cursor.getInt(cursor.getColumnIndex(HJInfo.COLS.MASTER_IDX));
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
				
				HJInfo info = new HJInfo();
				
				info.idx = idx;
				info.weather = wether;
				info.light_type = light_type;
				info.power = power;
				info.light_no = light_no;
				info.control = control;
				info.sun_battery = sun_battery;
				info.storage_battery = storage_battery;
				info.light_item = light_item;
				info.cable = cable;
				info.yb_result = yb_result;
				info.remarks = remarks;
				
				hjList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return hjList;
	}

	public boolean existHJ(String mIdx) {
		
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
