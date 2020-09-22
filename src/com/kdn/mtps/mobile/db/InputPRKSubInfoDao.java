package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.PRKSubInfo;

import java.util.ArrayList;

public class InputPRKSubInfoDao extends BaseDao{
	static InputPRKSubInfoDao inputPRKSubInfoDao;

	private InputPRKSubInfoDao(Context ctx) {
		tableName = "INPUT_PR_K_SUBINFO";
		this.ctx = ctx;
	}

	public static InputPRKSubInfoDao getInstance(Context ctx) {
		if (inputPRKSubInfoDao == null)
			inputPRKSubInfoDao = new InputPRKSubInfoDao(ctx);
		return inputPRKSubInfoDao;
	}
	
	public void Append(PRKSubInfo row) {
		Append(row, -1);
	}
	
	public void Append(PRKSubInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(PRKSubInfo.COLS.IDX, idx);
		}

		updateRow.put(PRKSubInfo.COLS.TOWER_IDX, row.TOWER_IDX);
		updateRow.put(PRKSubInfo.COLS.CONT_NUM, row.CONT_NUM);
		updateRow.put(PRKSubInfo.COLS.SN, row.SN);
		updateRow.put(PRKSubInfo.COLS.TTM_LOAD, row.TTM_LOAD);
		updateRow.put(PRKSubInfo.COLS.FNCT_LC_DTLS, row.FNCT_LC_DTLS);
		updateRow.put(PRKSubInfo.COLS.EQP_NM, row.EQP_NM);
		updateRow.put(PRKSubInfo.COLS.FNCT_LC_NO, row.FNCT_LC_NO);
		updateRow.put(PRKSubInfo.COLS.EQP_NO, row.EQP_NO);
			
		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}
	
	public ArrayList<PRKSubInfo> selectList(String eqpNo) {
		
		ArrayList<PRKSubInfo> jsList = new ArrayList<PRKSubInfo>();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where EQP_NO = '%s' ORDER BY FNCT_LC_DTLS, CONT_NUM, SN";
			String sql = String.format(fmt, tableName, eqpNo);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(PRKSubInfo.COLS.IDX));
				String TOWER_IDX = cursor.getString(cursor.getColumnIndex(PRKSubInfo.COLS.TOWER_IDX));
				String CONT_NUM = cursor.getString(cursor.getColumnIndex(PRKSubInfo.COLS.CONT_NUM));
				String SN = cursor.getString(cursor.getColumnIndex(PRKSubInfo.COLS.SN));
				String TTM_LOAD = cursor.getString(cursor.getColumnIndex(PRKSubInfo.COLS.TTM_LOAD));
				String FNCT_LC_DTLS = cursor.getString(cursor.getColumnIndex(PRKSubInfo.COLS.FNCT_LC_DTLS));
				String EQP_NM = cursor.getString(cursor.getColumnIndex(PRKSubInfo.COLS.EQP_NM));
				String FNCT_LC_NO = cursor.getString(cursor.getColumnIndex(PRKSubInfo.COLS.FNCT_LC_NO));
				String EQP_NO = cursor.getString(cursor.getColumnIndex(PRKSubInfo.COLS.EQP_NO));
				
				PRKSubInfo info = new PRKSubInfo();
				
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

	public boolean existPRK(String mIdx) {
		
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
