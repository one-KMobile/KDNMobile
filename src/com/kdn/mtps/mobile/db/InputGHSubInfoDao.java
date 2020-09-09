package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.GHSubInfo;

import java.util.ArrayList;

public class InputGHSubInfoDao extends BaseDao{
	static InputGHSubInfoDao inputGHSubInfoDao;

	private InputGHSubInfoDao(Context ctx) {
		tableName = "INPUT_GH_SUBINFO";
		this.ctx = ctx;
	}

	public static InputGHSubInfoDao getInstance(Context ctx) {
		if (inputGHSubInfoDao == null)
			inputGHSubInfoDao = new InputGHSubInfoDao(ctx);
		return inputGHSubInfoDao;
	}
	
	public void Append(GHSubInfo row) {
		Append(row, -1);
	}
	
	public void Append(GHSubInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(GHSubInfo.COLS.IDX, idx);
		}
		
		updateRow.put(GHSubInfo.COLS.TOWER_IDX, row.TOWER_IDX);
		updateRow.put(GHSubInfo.COLS.CONT_NUM, row.CONT_NUM);
		updateRow.put(GHSubInfo.COLS.SN, row.SN);
		updateRow.put(GHSubInfo.COLS.TTM_LOAD, row.TTM_LOAD);
		updateRow.put(GHSubInfo.COLS.FNCT_LC_DTLS, row.FNCT_LC_DTLS);
		updateRow.put(GHSubInfo.COLS.EQP_NM, row.EQP_NM);
		updateRow.put(GHSubInfo.COLS.FNCT_LC_NO, row.FNCT_LC_NO);
		updateRow.put(GHSubInfo.COLS.EQP_NO, row.EQP_NO);
		updateRow.put(GHSubInfo.COLS.POWER_NO_C1, row.POWER_NO_C1);
		updateRow.put(GHSubInfo.COLS.POWER_NO_C2, row.POWER_NO_C2);
		updateRow.put(GHSubInfo.COLS.POWER_NO_C3, row.POWER_NO_C3);
			
		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}
	
	public ArrayList<GHSubInfo> selectList(String eqpNo) {
		
		ArrayList<GHSubInfo> jsList = new ArrayList<GHSubInfo>();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where EQP_NO = '%s' ORDER BY FNCT_LC_DTLS, CONT_NUM, SN";
			String sql = String.format(fmt, tableName, eqpNo);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(GHSubInfo.COLS.IDX));
				String TOWER_IDX = cursor.getString(cursor.getColumnIndex(GHSubInfo.COLS.TOWER_IDX));
				String CONT_NUM = cursor.getString(cursor.getColumnIndex(GHSubInfo.COLS.CONT_NUM));
				String SN = cursor.getString(cursor.getColumnIndex(GHSubInfo.COLS.SN));
				String TTM_LOAD = cursor.getString(cursor.getColumnIndex(GHSubInfo.COLS.TTM_LOAD));
				String FNCT_LC_DTLS = cursor.getString(cursor.getColumnIndex(GHSubInfo.COLS.FNCT_LC_DTLS));
				String EQP_NM = cursor.getString(cursor.getColumnIndex(GHSubInfo.COLS.EQP_NM));
				String FNCT_LC_NO = cursor.getString(cursor.getColumnIndex(GHSubInfo.COLS.FNCT_LC_NO));
				String EQP_NO = cursor.getString(cursor.getColumnIndex(GHSubInfo.COLS.EQP_NO));
				String POWER_NO_C1 = cursor.getString(cursor.getColumnIndex(GHSubInfo.COLS.POWER_NO_C1));
				String POWER_NO_C2 = cursor.getString(cursor.getColumnIndex(GHSubInfo.COLS.POWER_NO_C2));
				String POWER_NO_C3 = cursor.getString(cursor.getColumnIndex(GHSubInfo.COLS.POWER_NO_C3));
				
				GHSubInfo info = new GHSubInfo();
				
				info.IDX = idx;
				info.TOWER_IDX = TOWER_IDX;
				info.CONT_NUM = CONT_NUM;
				info.SN = SN;
				info.TTM_LOAD = TTM_LOAD;
				info.FNCT_LC_DTLS = FNCT_LC_DTLS;
				info.EQP_NM = EQP_NM;
				info.FNCT_LC_NO = FNCT_LC_NO;
				info.EQP_NO = EQP_NO;
				info.POWER_NO_C1 = POWER_NO_C1;
				info.POWER_NO_C2 = POWER_NO_C2;
				info.POWER_NO_C3 = POWER_NO_C3;
				
				jsList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return jsList;
	}

	public boolean existGH(String mIdx) {
		
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
