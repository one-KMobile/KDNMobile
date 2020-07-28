package com.kdn.mtps.mobile.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.HKSubInfo;

public class InputHKSubInfoDao extends BaseDao{
	static InputHKSubInfoDao inputHKSubInfoDao;

	private InputHKSubInfoDao(Context ctx) {
		tableName = "INPUT_HK_SUBINFO";
		this.ctx = ctx;
	}

	public static InputHKSubInfoDao getInstance(Context ctx) {
		if (inputHKSubInfoDao == null)
			inputHKSubInfoDao = new InputHKSubInfoDao(ctx);
		return inputHKSubInfoDao;
	}
	
	public void Append(HKSubInfo row) {
		Append(row, -1);
	}
	
	public void Append(HKSubInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(HKSubInfo.COLS.IDX, idx);
		}
		
		updateRow.put(HKSubInfo.COLS.TOWER_IDX, row.TOWER_IDX);
		updateRow.put(HKSubInfo.COLS.SRCELCT_KND, row.SRCELCT_KND);
		updateRow.put(HKSubInfo.COLS.SRCELCT_KND_NM, row.SRCELCT_KND_NM);
		updateRow.put(HKSubInfo.COLS.EQP_NO, row.EQP_NO);
		updateRow.put(HKSubInfo.COLS.FLIGHT_LMP_NO, row.FLIGHT_LMP_NO);
		updateRow.put(HKSubInfo.COLS.FLIGHT_LMP_KND, row.FLIGHT_LMP_KND);
		updateRow.put(HKSubInfo.COLS.FLIGHT_LMP_KND_NM, row.FLIGHT_LMP_KND_NM);
			
		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}
	
	public ArrayList<HKSubInfo> selectList(String eqpNo) {
		
		ArrayList<HKSubInfo> hkList = new ArrayList<HKSubInfo>();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where EQP_NO = '%s' ORDER BY FLIGHT_LMP_KND, FLIGHT_LMP_NO";
			String sql = String.format(fmt, tableName, eqpNo);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(HKSubInfo.COLS.IDX));
				String TOWER_IDX = cursor.getString(cursor.getColumnIndex(HKSubInfo.COLS.TOWER_IDX));
				String SRCELCT_KND = cursor.getString(cursor.getColumnIndex(HKSubInfo.COLS.SRCELCT_KND));
				String SRCELCT_KND_NM = cursor.getString(cursor.getColumnIndex(HKSubInfo.COLS.SRCELCT_KND_NM));
				String EQP_NO = cursor.getString(cursor.getColumnIndex(HKSubInfo.COLS.EQP_NO));
				String FLIGHT_LMP_NO = cursor.getString(cursor.getColumnIndex(HKSubInfo.COLS.FLIGHT_LMP_NO));
				String FLIGHT_LMP_KND = cursor.getString(cursor.getColumnIndex(HKSubInfo.COLS.FLIGHT_LMP_KND));
				String FLIGHT_LMP_KND_NM = cursor.getString(cursor.getColumnIndex(HKSubInfo.COLS.FLIGHT_LMP_KND_NM));
				
				HKSubInfo info = new HKSubInfo();
				
				info.IDX = idx;
				info.TOWER_IDX = TOWER_IDX;
				info.SRCELCT_KND = SRCELCT_KND;
				info.SRCELCT_KND_NM = SRCELCT_KND_NM;
				info.EQP_NO = EQP_NO;
				info.FLIGHT_LMP_NO = FLIGHT_LMP_NO;
				info.FLIGHT_LMP_KND = FLIGHT_LMP_KND;
				info.FLIGHT_LMP_KND_NM = FLIGHT_LMP_KND_NM;
				
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
				int idx = cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}
}
