package com.kdn.mtps.mobile.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.BRSubInfo;

public class InputBRSubInfoDao extends BaseDao{
	static InputBRSubInfoDao inputBRSubInfoDao;

	private InputBRSubInfoDao(Context ctx) {
		tableName = "INPUT_BR_SUBINFO";
		this.ctx = ctx;
	}

	public static InputBRSubInfoDao getInstance(Context ctx) {
		if (inputBRSubInfoDao == null)
			inputBRSubInfoDao = new InputBRSubInfoDao(ctx);
		return inputBRSubInfoDao;
	}
	
	public void Append(BRSubInfo row) {
		Append(row, -1);
	}
	
	public void Append(BRSubInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(BRSubInfo.COLS.IDX, idx);
		}
		
		updateRow.put(BRSubInfo.COLS.TOWER_IDX, row.TOWER_IDX);
		updateRow.put(BRSubInfo.COLS.INSR_EQP_NO, row.INSR_EQP_NO);
		updateRow.put(BRSubInfo.COLS.INSBTY_LFT, row.INSBTY_LFT);
		updateRow.put(BRSubInfo.COLS.INSBTY_RIT, row.INSBTY_RIT);
		updateRow.put(BRSubInfo.COLS.TY_SECD_NM, row.TY_SECD_NM);
		updateRow.put(BRSubInfo.COLS.TY_SECD, row.TY_SECD);
		updateRow.put(BRSubInfo.COLS.CL_NM, row.CL_NM);
		updateRow.put(BRSubInfo.COLS.CL_NO, row.CL_NO);
		updateRow.put(BRSubInfo.COLS.INS_CNT, row.INS_CNT);
		updateRow.put(BRSubInfo.COLS.EQP_NO, row.EQP_NO);
		updateRow.put(BRSubInfo.COLS.PHS_SECD_NM, row.PHS_SECD_NM);
		updateRow.put(BRSubInfo.COLS.PHS_SECD, row.PHS_SECD);
			
		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}
	
	public ArrayList<BRSubInfo> selectList(String eqpNo) {
		
		ArrayList<BRSubInfo> brList = new ArrayList<BRSubInfo>();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where EQP_NO = '%s' ORDER BY CL_NM ASC,  TY_SECD_NM DESC,  PHS_SECD_NM ASC";
			String sql = String.format(fmt, tableName, eqpNo);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(BRSubInfo.COLS.IDX));
				String TOWER_IDX = cursor.getString(cursor.getColumnIndex(BRSubInfo.COLS.TOWER_IDX));
				String INSR_EQP_NO = cursor.getString(cursor.getColumnIndex(BRSubInfo.COLS.INSR_EQP_NO));
				String INSBTY_LFT = cursor.getString(cursor.getColumnIndex(BRSubInfo.COLS.INSBTY_LFT));
				String INSBTY_RIT = cursor.getString(cursor.getColumnIndex(BRSubInfo.COLS.INSBTY_RIT));
				String TY_SECD_NM = cursor.getString(cursor.getColumnIndex(BRSubInfo.COLS.TY_SECD_NM));
				String TY_SECD = cursor.getString(cursor.getColumnIndex(BRSubInfo.COLS.TY_SECD));
				String CL_NM = cursor.getString(cursor.getColumnIndex(BRSubInfo.COLS.CL_NM));
				String CL_NO = cursor.getString(cursor.getColumnIndex(BRSubInfo.COLS.CL_NO));
				String INS_CNT = cursor.getString(cursor.getColumnIndex(BRSubInfo.COLS.INS_CNT));
				String EQP_NO = cursor.getString(cursor.getColumnIndex(BRSubInfo.COLS.EQP_NO));
				String PHS_SECD_NM = cursor.getString(cursor.getColumnIndex(BRSubInfo.COLS.PHS_SECD_NM));
				String PHS_SECD = cursor.getString(cursor.getColumnIndex(BRSubInfo.COLS.PHS_SECD));
				
				BRSubInfo info = new BRSubInfo();
				
				info.IDX = idx;
				info.TOWER_IDX = TOWER_IDX;
				info.INSR_EQP_NO = INSR_EQP_NO;
				info.INSBTY_LFT = INSBTY_LFT;
				info.INSBTY_RIT = INSBTY_RIT;
				info.TY_SECD_NM = TY_SECD_NM;
				info.TY_SECD = TY_SECD;
				info.CL_NM = CL_NM;
				info.CL_NO = CL_NO;
				info.INS_CNT = INS_CNT;
				info.EQP_NO = EQP_NO;
				info.PHS_SECD_NM = PHS_SECD_NM;
				info.PHS_SECD = PHS_SECD;
				
				brList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return brList;
	}

	public boolean existBR(String mIdx) {
		
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
