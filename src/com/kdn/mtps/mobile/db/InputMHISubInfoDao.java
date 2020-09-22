package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.MHISubInfo;

import java.util.ArrayList;

public class InputMHISubInfoDao extends BaseDao{
	static InputMHISubInfoDao inputMHISubInfoDao;

	private InputMHISubInfoDao(Context ctx) {
		tableName = "INPUT_MH_I_SUBINFO";
		this.ctx = ctx;
	}

	public static InputMHISubInfoDao getInstance(Context ctx) {
		if (inputMHISubInfoDao == null)
			inputMHISubInfoDao = new InputMHISubInfoDao(ctx);
		return inputMHISubInfoDao;
	}
	
	public void Append(MHISubInfo row) {
		Append(row, -1);
	}
	
	public void Append(MHISubInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(MHISubInfo.COLS.IDX, idx);
		}
		
		updateRow.put(MHISubInfo.COLS.TOWER_IDX, row.TOWER_IDX);
		updateRow.put(MHISubInfo.COLS.FNCT_LC_DTLS, row.FNCT_LC_DTLS);
		updateRow.put(MHISubInfo.COLS.EQP_NM, row.EQP_NM);
		updateRow.put(MHISubInfo.COLS.FNCT_LC_NO, row.FNCT_LC_NO);
		updateRow.put(MHISubInfo.COLS.EQP_NO, row.EQP_NO);
			
		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}
	
	public ArrayList<MHISubInfo> selectList(String eqpNo) {
		
		ArrayList<MHISubInfo> jsList = new ArrayList<MHISubInfo>();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where EQP_NO = '%s' ORDER BY FNCT_LC_DTLS";
			String sql = String.format(fmt, tableName, eqpNo);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(MHISubInfo.COLS.IDX));
				String TOWER_IDX = cursor.getString(cursor.getColumnIndex(MHISubInfo.COLS.TOWER_IDX));
				String FNCT_LC_DTLS = cursor.getString(cursor.getColumnIndex(MHISubInfo.COLS.FNCT_LC_DTLS));
				String EQP_NM = cursor.getString(cursor.getColumnIndex(MHISubInfo.COLS.EQP_NM));
				String FNCT_LC_NO = cursor.getString(cursor.getColumnIndex(MHISubInfo.COLS.FNCT_LC_NO));
				String EQP_NO = cursor.getString(cursor.getColumnIndex(MHISubInfo.COLS.EQP_NO));
				
				MHISubInfo info = new MHISubInfo();
				
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
