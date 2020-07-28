package com.kdn.mtps.mobile.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.info.GData;
import com.kdn.mtps.mobile.notice.NoticeInfo;
import com.kdn.mtps.mobile.util.Logg;

public class NoticeListDao extends BaseDao{
	static NoticeListDao noticeListDao;

	private NoticeListDao(Context ctx) {
		tableName = "NOTICE_LIST";
		this.ctx = ctx;
	}

	public boolean exist(String idx) {
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s where BOARD_IDX = %s";
			String sql = String.format(fmt, tableName, idx);

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
	
	public boolean existNew() {
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s where READ_YN = 'N'";
			String sql = String.format(fmt, tableName);

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

	public static NoticeListDao getInstance(Context ctx) {
		if (noticeListDao == null)
			noticeListDao = new NoticeListDao(ctx);
		return noticeListDao;
	}
	
	public void Append(NoticeInfo row) {
		Append(row, -1);
	}
	
	public void Append(NoticeInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(NoticeInfo.COLS.IDX, idx);
		}
		
		updateRow.put(NoticeInfo.COLS.BOARD_IDX, row.BOARD_IDX);
		updateRow.put(NoticeInfo.COLS.BOARD_TITLE, row.BOARD_TITLE);
		updateRow.put(NoticeInfo.COLS.BOARD_CONT, row.BOARD_CONT);
		updateRow.put(NoticeInfo.COLS.SEND_NAME, row.SEND_NAME);
		updateRow.put(NoticeInfo.COLS.SEND_DATE, row.SEND_DATE);
		updateRow.put(NoticeInfo.COLS.CATEGORY_NAME, row.CATEGORY_NAME);
		updateRow.put(NoticeInfo.COLS.TOP_YN, row.TOP_YN);
		updateRow.put(NoticeInfo.COLS.USE_YN, row.USE_YN);
		
		db.replace(tableName, null, updateRow);

		dbCheck();
	}
	
	public void updateRead(int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "UPDATE %s SET READ_YN = 'Y' WHERE IDX = %s";
		String sql = String.format(fmt, tableName, idx);
		db.execSQL(sql);
	}
	
	public ArrayList<NoticeInfo> selectNoticeItems() {
		
		ArrayList<NoticeInfo> noticeList = new ArrayList<NoticeInfo>();
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from '%s' ";
			String sql = String.format(fmt, tableName);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(0);
				String board_idx = cursor.getString(1);
				String board_title = cursor.getString(2);
				String board_cont = cursor.getString(3);
				String send_name = cursor.getString(4);
				String send_date = cursor.getString(5);
				String category_name = cursor.getString(6);
				String top_yn = cursor.getString(7);
				String use_yn = cursor.getString(8);
				String read_yn = cursor.getString(9);
				
				NoticeInfo info = new NoticeInfo();
				info.IDX = idx;
				info.BOARD_IDX = board_idx;
				info.BOARD_TITLE = board_title;
				info.BOARD_CONT = board_cont;
				info.SEND_NAME = send_name;
				info.SEND_DATE = send_date;
				info.CATEGORY_NAME = category_name;
				info.TOP_YN = top_yn;
				info.USE_YN = use_yn;
				info.READ_YN = read_yn;
				
				noticeList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		GData.setNoticeList(noticeList);
		
		return noticeList;
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
				String notice_idx = cursor.getString(1);
				String read_yn = cursor.getString(2);
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "idx : " + idx);
				Log.d("Test", "notice_idx : " + notice_idx);
				Log.d("Test", "read_yn : " + read_yn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}
}
