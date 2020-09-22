package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.JGUSubInfo;

import java.util.ArrayList;

public class InputJGUSubInfoDao extends BaseDao{
	static InputJGUSubInfoDao inputJGUSubInfoDao;

	private InputJGUSubInfoDao(Context ctx) {
		tableName = "INPUT_JG_U_SUBINFO";
		this.ctx = ctx;
	}

	public static InputJGUSubInfoDao getInstance(Context ctx) {
		if (inputJGUSubInfoDao == null)
			inputJGUSubInfoDao = new InputJGUSubInfoDao(ctx);
		return inputJGUSubInfoDao;
	}
	
	public void Append(JGUSubInfo row) {
		Append(row, -1);
	}
	
	public void Append(JGUSubInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(JGUSubInfo.COLS.IDX, idx);
		}

		updateRow.put(JGUSubInfo.COLS.FNCT_LC_NO, row.FNCT_LC_NO);
		updateRow.put(JGUSubInfo.COLS.FNCT_LC_DTLS, row.FNCT_LC_DTLS);
		updateRow.put(JGUSubInfo.COLS.EQP_NO, row.EQP_NO);
		updateRow.put(JGUSubInfo.COLS.EQP_NM, row.EQP_NM);
		updateRow.put(JGUSubInfo.COLS.UPTLVL_UPLMT, row.UPTLVL_UPLMT);
		updateRow.put(JGUSubInfo.COLS.UPTLVL_LWLT, row.UPTLVL_LWLT);
		updateRow.put(JGUSubInfo.COLS.UPTLVL_INTRCP, row.UPTLVL_INTRCP);
		updateRow.put(JGUSubInfo.COLS.MNG_01, row.MNG_01);
		updateRow.put(JGUSubInfo.COLS.MNG_02, row.MNG_02);
		updateRow.put(JGUSubInfo.COLS.SD, row.SD);

		updateRow.put(JGUSubInfo.COLS.TOWER_IDX, row.TOWER_IDX);
		updateRow.put(JGUSubInfo.COLS.CONT_NUM, row.CONT_NUM);
		updateRow.put(JGUSubInfo.COLS.SN, row.SN);
		updateRow.put(JGUSubInfo.COLS.TTM_LOAD, row.TTM_LOAD);
			
		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}
	
	public ArrayList<JGUSubInfo> selectList(String eqpNo) {
		
		ArrayList<JGUSubInfo> jsList = new ArrayList<JGUSubInfo>();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where EQP_NO = '%s' ORDER BY idx";
			String sql = String.format(fmt, tableName, eqpNo);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(JGUSubInfo.COLS.IDX));
				String FNCT_LC_NO = cursor.getString(cursor.getColumnIndex(JGUSubInfo.COLS.FNCT_LC_NO));
				String FNCT_LC_DTLS = cursor.getString(cursor.getColumnIndex(JGUSubInfo.COLS.FNCT_LC_DTLS));
				String EQP_NO = cursor.getString(cursor.getColumnIndex(JGUSubInfo.COLS.EQP_NO));
				String EQP_NM = cursor.getString(cursor.getColumnIndex(JGUSubInfo.COLS.EQP_NM));
				String UPTLVL_UPLMT = cursor.getString(cursor.getColumnIndex(JGUSubInfo.COLS.UPTLVL_UPLMT));
				String UPTLVL_LWLT = cursor.getString(cursor.getColumnIndex(JGUSubInfo.COLS.UPTLVL_LWLT));
				String UPTLVL_INTRCP = cursor.getString(cursor.getColumnIndex(JGUSubInfo.COLS.UPTLVL_INTRCP));
				String MNG_01 = cursor.getString(cursor.getColumnIndex(JGUSubInfo.COLS.MNG_01));
				String MNG_02 = cursor.getString(cursor.getColumnIndex(JGUSubInfo.COLS.MNG_02));
				String SD = cursor.getString(cursor.getColumnIndex(JGUSubInfo.COLS.SD));

				String TOWER_IDX = cursor.getString(cursor.getColumnIndex(JGUSubInfo.COLS.TOWER_IDX));
				String CONT_NUM = cursor.getString(cursor.getColumnIndex(JGUSubInfo.COLS.CONT_NUM));
				String SN = cursor.getString(cursor.getColumnIndex(JGUSubInfo.COLS.SN));
				String TTM_LOAD = cursor.getString(cursor.getColumnIndex(JGUSubInfo.COLS.TTM_LOAD));
				
				JGUSubInfo info = new JGUSubInfo();
				
				info.IDX = idx;
				info.FNCT_LC_NO = FNCT_LC_NO;
				info.FNCT_LC_DTLS = FNCT_LC_DTLS;
				info.EQP_NO = EQP_NO;
				info.EQP_NM = EQP_NM;
				info.UPTLVL_UPLMT = UPTLVL_UPLMT;
				info.UPTLVL_LWLT = UPTLVL_LWLT;
				info.UPTLVL_INTRCP = UPTLVL_INTRCP;
				info.MNG_01 = MNG_01;
				info.MNG_02 = MNG_02;
				info.SD = SD;

				info.TOWER_IDX = TOWER_IDX;
				info.CONT_NUM = CONT_NUM;
				info.SN = SN;
				info.TTM_LOAD = TTM_LOAD;
				
				jsList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return jsList;
	}

	public boolean existJGU(String mIdx) {
		
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
