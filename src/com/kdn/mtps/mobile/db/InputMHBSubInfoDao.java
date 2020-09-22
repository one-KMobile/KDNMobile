package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.MHBSubInfo;

import java.util.ArrayList;

public class InputMHBSubInfoDao extends BaseDao{
	static InputMHBSubInfoDao inputMHBSubInfoDao;

	private InputMHBSubInfoDao(Context ctx) {
		tableName = "INPUT_MH_B_SUBINFO";
		this.ctx = ctx;
	}

	public static InputMHBSubInfoDao getInstance(Context ctx) {
		if (inputMHBSubInfoDao == null)
			inputMHBSubInfoDao = new InputMHBSubInfoDao(ctx);
		return inputMHBSubInfoDao;
	}
	
	public void Append(MHBSubInfo row) {
		Append(row, -1);
	}
	
	public void Append(MHBSubInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(MHBSubInfo.COLS.IDX, idx);
		}
		
		updateRow.put(MHBSubInfo.COLS.TOWER_IDX, row.TOWER_IDX);
		updateRow.put(MHBSubInfo.COLS.FNCT_LC_DTLS, row.FNCT_LC_DTLS);
		updateRow.put(MHBSubInfo.COLS.EQP_NM, row.EQP_NM);
		updateRow.put(MHBSubInfo.COLS.FNCT_LC_NO, row.FNCT_LC_NO);
		updateRow.put(MHBSubInfo.COLS.EQP_NO, row.EQP_NO);
			
		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}
	
	public ArrayList<MHBSubInfo> selectList(String eqpNo) {
		
		ArrayList<MHBSubInfo> jsList = new ArrayList<MHBSubInfo>();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where EQP_NO = '%s' ORDER BY FNCT_LC_DTLS";
			String sql = String.format(fmt, tableName, eqpNo);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(MHBSubInfo.COLS.IDX));
				String TOWER_IDX = cursor.getString(cursor.getColumnIndex(MHBSubInfo.COLS.TOWER_IDX));
				String FNCT_LC_DTLS = cursor.getString(cursor.getColumnIndex(MHBSubInfo.COLS.FNCT_LC_DTLS));
				String EQP_NM = cursor.getString(cursor.getColumnIndex(MHBSubInfo.COLS.EQP_NM));
				String FNCT_LC_NO = cursor.getString(cursor.getColumnIndex(MHBSubInfo.COLS.FNCT_LC_NO));
				String EQP_NO = cursor.getString(cursor.getColumnIndex(MHBSubInfo.COLS.EQP_NO));
				
				MHBSubInfo info = new MHBSubInfo();
				
				info.IDX = idx;
				info.TOWER_IDX = TOWER_IDX;
				info.FNCT_LC_DTLS = FNCT_LC_DTLS;
				info.EQP_NM = EQP_NM;
				info.FNCT_LC_NO = FNCT_LC_NO;
				info.EQP_NO = EQP_NO;
				
				jsList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return jsList;
	}

	public boolean existPRP(String mIdx) {
		
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
