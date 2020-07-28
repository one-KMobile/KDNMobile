package com.kdn.mtps.mobile.db;

import java.text.SimpleDateFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.db.bean.MyLocationLog;

public class MyLocationLogDao extends BaseDao{
	static MyLocationLogDao myLocationLogDao;

	private MyLocationLogDao(Context ctx) {
		tableName = "MYLOCATION_LOG";
		this.ctx = ctx;
	}

	public static MyLocationLogDao getInstance(Context ctx) {
		if (myLocationLogDao == null)
			myLocationLogDao = new MyLocationLogDao(ctx);
		return myLocationLogDao;
	}
	
	public void Append(MyLocationLog row) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();

		updateRow.put(MyLocationLog.COLS.REG_DATE, row.reg_date);
		updateRow.put(MyLocationLog.COLS.LATITUDE, row.latitude);
		updateRow.put(MyLocationLog.COLS.LONGITUDE, row.longitude);

		db.replace(tableName, null, updateRow);

		dbCheck();
	}
	
	public MyLocationLog getLastData() {
		MyLocationLog log = new MyLocationLog();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s order by _id desc";
			String sql = String.format(fmt, tableName);

			cursor = db.rawQuery(sql, null);

			Log.d("Test", "======================START====================");
			
			if (cursor.moveToNext()) {
				int _id = cursor.getInt(0);
				long reg_date = cursor.getLong(1);
				String latitude = cursor.getString(2);
				String  longitude = cursor.getString(3);
			
				log.reg_date = reg_date;
				Log.d("Test", "last data : " + _id + " / " + reg_date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			close();
		}
		
		return log;
	}
	
	public void dbCheck() {
		Log.d("Test", "############ DB value check #############");
		
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s";
			String sql = String.format(fmt, tableName);

			cursor = db.rawQuery(sql, null);

			Log.d("Test", "======================START====================");
			
			while (cursor.moveToNext()) {
				int _id = cursor.getInt(0);
				long reg_date = cursor.getLong(1);
				String latitude = cursor.getString(2);
				String  longitude = cursor.getString(3);
				
				String strDate = new SimpleDateFormat ("yyyy.MM.dd HH:mm").format(reg_date);
				
				Log.d("Test", "dbCheck : " + _id + " / " + strDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			close();
		}

	}
}
