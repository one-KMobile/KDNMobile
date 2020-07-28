package com.kdn.mtps.mobile.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.YBInfo;

public class CompanyListDao extends BaseDao{
	static CompanyListDao companyListDao;

	private CompanyListDao(Context ctx) {
		tableName = "COMPANY_LIST";
		this.ctx = ctx;
	}

	public static CompanyListDao getInstance(Context ctx) {
		if (companyListDao == null)
			companyListDao = new CompanyListDao(ctx);
		return companyListDao;
	}
	
	public void Append(String companyName) {
		Append(companyName, -1);
	}
	
	public void Append(String companyName, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(YBInfo.COLS.IDX, idx);
		}
		
		updateRow.put("COMPANY_NAME", companyName);
		
		db.replace(tableName, null, updateRow);

		dbCheck();
	}
	
	public void Delete(String companyName) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE COMPANY_NAME = '%s'";
		String sql = String.format(fmt, tableName, companyName);
		db.execSQL(sql);
	}
	
	public ArrayList<String> selectCompanyList() {
		
		ArrayList<String> list = new ArrayList<String>();
		
		list.add("직접입력");
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s ";
			String sql = String.format(fmt, tableName);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				
				String companyList = cursor.getString(cursor.getColumnIndex("COMPANY_NAME"));
				list.add(companyList);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return list;
	}

	public boolean exist(String companyName) {
		
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s where COMPANY_NAME = %s";
			String sql = String.format(fmt, tableName, companyName);

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
