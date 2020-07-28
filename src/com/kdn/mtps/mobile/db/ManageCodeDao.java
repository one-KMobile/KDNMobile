package com.kdn.mtps.mobile.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.db.bean.ManageCodeLog;
import com.kdn.mtps.mobile.info.CodeInfo;

public class ManageCodeDao extends BaseDao{
	static ManageCodeDao manageCodeDao;

	private ManageCodeDao(Context ctx) {
		tableName = "MANAGE_CODE";
		this.ctx = ctx;
	}

	public static ManageCodeDao getInstance(Context ctx) {
		if (manageCodeDao == null)
			manageCodeDao = new ManageCodeDao(ctx);
		return manageCodeDao;
	}
	
	public void Append(ManageCodeLog row) {
		Append(row, -1);
	}
	
	public void Append(ManageCodeLog row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(ManageCodeLog.COLS.IDX, idx);
		}
		
		updateRow.put(ManageCodeLog.COLS.CODE_TYPE, row.code_type);
		updateRow.put(ManageCodeLog.COLS.CODE_KEY, row.code_key);
		updateRow.put(ManageCodeLog.COLS.CODE_VALUE, row.code_value);

		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Append(ArrayList<ManageCodeLog> rows) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		for (ManageCodeLog row : rows) {
			updateRow.put(ManageCodeLog.COLS.CODE_TYPE, row.code_type);
			updateRow.put(ManageCodeLog.COLS.CODE_KEY, row.code_key);
			updateRow.put(ManageCodeLog.COLS.CODE_VALUE, row.code_value);

			db.replace(tableName, null, updateRow);
		}
		dbCheck();
	}
	
	public ArrayList<ManageCodeLog> selectCodeItems(String mType) {
		
		ArrayList<ManageCodeLog> codeList = new ArrayList<ManageCodeLog>();
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s where code_type = '%s' ";
			String sql = String.format(fmt, tableName, mType);

			cursor = db.rawQuery(sql, null);

			Log.d("Test", "selectCodeItems : " + mType);
			
			while (cursor.moveToNext()) {
				int idx = cursor.getInt(0);
				String code_type = cursor.getString(1);
				String code_key = cursor.getString(2);
				String code_value = cursor.getString(3);
				
				ManageCodeLog info = new ManageCodeLog();
				info.idx = idx;
				info.code_type = code_type;
				info.code_key = code_key;
				info.code_value = code_value;
				
				codeList.add(info);
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "code_type : " + code_type);
				Log.d("Test", "code_key : " + code_key);
				Log.d("Test", "code_value : " + code_value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		CodeInfo.getInstance(ctx).setCodeData(mType, codeList);
		return codeList;
	}

	public ArrayList<ManageCodeLog> selectCodeList(String mType) {
		
		ArrayList<ManageCodeLog> codeList = new ArrayList<ManageCodeLog>();
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s where code_type = '%s' ";
			String sql = String.format(fmt, tableName, mType);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(0);
				String code_type = cursor.getString(1);
				String code_key = cursor.getString(2);
				String code_value = cursor.getString(3);
				
				ManageCodeLog info = new ManageCodeLog();
				info.idx = idx;
				info.code_type = code_type;
				info.code_key = code_key;
				info.code_value = code_value;
				
				codeList.add(info);
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "code_type : " + code_type);
				Log.d("Test", "code_key : " + code_key);
				Log.d("Test", "code_value : " + code_value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return codeList;
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
				String code_type = cursor.getString(1);
				String code_key = cursor.getString(2);
				String code_value = cursor.getString(3);
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "idx : " + idx);
				Log.d("Test", "code_type : " + code_type);
				Log.d("Test", "code_key : " + code_key);
				Log.d("Test", "code_value : " + code_value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}
}
