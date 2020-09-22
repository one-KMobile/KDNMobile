package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.PRPSubInfo;

import java.util.ArrayList;

public class InputPRPSubInfoDao extends BaseDao{
	static InputPRPSubInfoDao inputPRPSubInfoDao;

	private InputPRPSubInfoDao(Context ctx) {
		tableName = "INPUT_PR_P_SUBINFO";
		this.ctx = ctx;
	}

	public static InputPRPSubInfoDao getInstance(Context ctx) {
		if (inputPRPSubInfoDao == null)
			inputPRPSubInfoDao = new InputPRPSubInfoDao(ctx);
		return inputPRPSubInfoDao;
	}
	
	public void Append(PRPSubInfo row) {
		Append(row, -1);
	}
	
	public void Append(PRPSubInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(PRPSubInfo.COLS.IDX, idx);
		}
		
		updateRow.put(PRPSubInfo.COLS.TOWER_IDX, row.TOWER_IDX);
		updateRow.put(PRPSubInfo.COLS.CONT_NUM, row.CONT_NUM);
		updateRow.put(PRPSubInfo.COLS.SN, row.SN);
		updateRow.put(PRPSubInfo.COLS.TTM_LOAD, row.TTM_LOAD);
		updateRow.put(PRPSubInfo.COLS.FNCT_LC_DTLS, row.FNCT_LC_DTLS);
		updateRow.put(PRPSubInfo.COLS.EQP_NM, row.EQP_NM);
		updateRow.put(PRPSubInfo.COLS.FNCT_LC_NO, row.FNCT_LC_NO);
		updateRow.put(PRPSubInfo.COLS.EQP_NO, row.EQP_NO);
			
		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}
	
	public ArrayList<PRPSubInfo> selectList(String eqpNo) {
		
		ArrayList<PRPSubInfo> jsList = new ArrayList<PRPSubInfo>();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where EQP_NO = '%s' ORDER BY FNCT_LC_DTLS, CONT_NUM, SN";
			String sql = String.format(fmt, tableName, eqpNo);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(PRPSubInfo.COLS.IDX));
				String TOWER_IDX = cursor.getString(cursor.getColumnIndex(PRPSubInfo.COLS.TOWER_IDX));
				String CONT_NUM = cursor.getString(cursor.getColumnIndex(PRPSubInfo.COLS.CONT_NUM));
				String SN = cursor.getString(cursor.getColumnIndex(PRPSubInfo.COLS.SN));
				String TTM_LOAD = cursor.getString(cursor.getColumnIndex(PRPSubInfo.COLS.TTM_LOAD));
				String FNCT_LC_DTLS = cursor.getString(cursor.getColumnIndex(PRPSubInfo.COLS.FNCT_LC_DTLS));
				String EQP_NM = cursor.getString(cursor.getColumnIndex(PRPSubInfo.COLS.EQP_NM));
				String FNCT_LC_NO = cursor.getString(cursor.getColumnIndex(PRPSubInfo.COLS.FNCT_LC_NO));
				String EQP_NO = cursor.getString(cursor.getColumnIndex(PRPSubInfo.COLS.EQP_NO));
				
				PRPSubInfo info = new PRPSubInfo();
				
				info.IDX = idx;
				info.TOWER_IDX = TOWER_IDX;
				info.CONT_NUM = CONT_NUM;
				info.SN = SN;
				info.TTM_LOAD = TTM_LOAD;
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
