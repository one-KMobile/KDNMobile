package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.JGSubInfo2;

import java.util.ArrayList;

public class InputJGSubInfo2Dao extends BaseDao{
	static InputJGSubInfo2Dao inputJGSubInfo2Dao;

	private InputJGSubInfo2Dao(Context ctx) {
		tableName = "INPUT_JS_SUBINFO";
		this.ctx = ctx;
	}

	public static InputJGSubInfo2Dao getInstance(Context ctx) {
		if (inputJGSubInfo2Dao == null)
			inputJGSubInfo2Dao = new InputJGSubInfo2Dao(ctx);
		return inputJGSubInfo2Dao;
	}
	
	public void Append(JGSubInfo2 row) {
		Append(row, -1);
	}
	
	public void Append(JGSubInfo2 row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(JGSubInfo2.COLS.IDX, idx);
		}
		
		updateRow.put(JGSubInfo2.COLS.TOWER_IDX, row.TOWER_IDX);
		updateRow.put(JGSubInfo2.COLS.FNCT_LC_DTLS, row.FNCT_LC_DTLS);
		updateRow.put(JGSubInfo2.COLS.FNCT_LC_NO, row.FNCT_LC_NO);
			
		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}
	
	public ArrayList<JGSubInfo2> selectList(String eqpNo) {
		
		ArrayList<JGSubInfo2> jsList = new ArrayList<JGSubInfo2>();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where EQP_NO = '%s' ORDER BY FNCT_LC_DTLS, CONT_NUM, SN";
			String sql = String.format(fmt, tableName, eqpNo);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(JGSubInfo2.COLS.IDX));
				String TOWER_IDX = cursor.getString(cursor.getColumnIndex(JGSubInfo2.COLS.TOWER_IDX));
				String CONT_NUM = cursor.getString(cursor.getColumnIndex(JGSubInfo2.COLS.CONT_NUM));
				String SN = cursor.getString(cursor.getColumnIndex(JGSubInfo2.COLS.SN));
				String TTM_LOAD = cursor.getString(cursor.getColumnIndex(JGSubInfo2.COLS.TTM_LOAD));
				String FNCT_LC_DTLS = cursor.getString(cursor.getColumnIndex(JGSubInfo2.COLS.FNCT_LC_DTLS));
				String EQP_NM = cursor.getString(cursor.getColumnIndex(JGSubInfo2.COLS.EQP_NM));
				String FNCT_LC_NO = cursor.getString(cursor.getColumnIndex(JGSubInfo2.COLS.FNCT_LC_NO));
				String EQP_NO = cursor.getString(cursor.getColumnIndex(JGSubInfo2.COLS.EQP_NO));
				String POWER_NO_C1 = cursor.getString(cursor.getColumnIndex(JGSubInfo2.COLS.POWER_NO_C1));
				String POWER_NO_C2 = cursor.getString(cursor.getColumnIndex(JGSubInfo2.COLS.POWER_NO_C2));
				String POWER_NO_C3 = cursor.getString(cursor.getColumnIndex(JGSubInfo2.COLS.POWER_NO_C3));
				
				JGSubInfo2 info = new JGSubInfo2();
				
				info.IDX = idx;
				info.TOWER_IDX = TOWER_IDX;
				info.FNCT_LC_DTLS = FNCT_LC_DTLS;
				info.FNCT_LC_NO = FNCT_LC_NO;
				
				jsList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return jsList;
	}

	public boolean existJS(String mIdx) {
		
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
