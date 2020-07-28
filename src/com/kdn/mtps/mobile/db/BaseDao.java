package com.kdn.mtps.mobile.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kdn.mtps.mobile.util.Logg;

public class BaseDao {
	protected String tableName = "tableName";
	protected Context ctx;

	Cursor cursor;

	public void GetAll() {
		String strSQL;
		strSQL = String.format("select * from %s", tableName);

		try {
			SQLiteDatabase db = DBHelper.getInstance(ctx);
			cursor = db.rawQuery(strSQL, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void close() {
		if (cursor != null)
			cursor.close();
	}

	public boolean isEOF() {
		if (cursor == null)
			return true;
		return !cursor.moveToFirst();
	}

	public boolean isAfterLast() {
		return cursor.isAfterLast();
	}

	public boolean isBeforeFirst() {
		return cursor.isBeforeFirst();
	}

	public boolean isClosed() {
		return cursor.isClosed();
	}

	public boolean isFirst() {
		return cursor.isFirst();
	}

	public boolean isLast() {
		return cursor.isLast();
	}

	public boolean isNull(int i) {
		return cursor.isNull(i);
	}

	public boolean move(int i) {
		return cursor.move(i);
	}

	public boolean moveToFirst() {
		return cursor.moveToFirst();
	}

	public boolean moveToLast() {
		return cursor.moveToLast();
	}

	public boolean moveToNext() {
		return cursor.moveToNext();
	}

	public boolean moveToPosition(int i) {
		return cursor.moveToPosition(i);
	}

	public boolean moveToPrevious() {
		return cursor.moveToPrevious();
	}
	
	public int getCount() {
		return cursor.getCount();
	}

	public Object Bind() {
		return null;
	}

	public Cursor getCursor() {
		return cursor;
	}
	
	public void DeleteAll() {
		Logg.d("tableName : " + tableName);
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		db.delete(tableName, null, null);
	}
}
