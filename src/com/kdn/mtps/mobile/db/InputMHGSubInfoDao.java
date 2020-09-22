package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.MHGSubInfo;
import com.kdn.mtps.mobile.util.Logg;

import java.util.ArrayList;

public class InputMHGSubInfoDao extends BaseDao{
	static InputMHGSubInfoDao inputMHGSubInfoDao;

	private InputMHGSubInfoDao(Context ctx) {
		tableName = "INPUT_MH_G_SUBINFO";
		this.ctx = ctx;
	}

	public static InputMHGSubInfoDao getInstance(Context ctx) {
		if (inputMHGSubInfoDao == null)
			inputMHGSubInfoDao = new InputMHGSubInfoDao(ctx);
		return inputMHGSubInfoDao;
	}
	
	public void Append(MHGSubInfo row) {
		Append(row, -1);
	}
	
	public void Append(MHGSubInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(MHGSubInfo.COLS.IDX, idx);
		}
		
		updateRow.put(MHGSubInfo.COLS.TOWER_IDX, row.TOWER_IDX);
		updateRow.put(MHGSubInfo.COLS.FNCT_LC_DTLS, row.FNCT_LC_DTLS);
		updateRow.put(MHGSubInfo.COLS.EQP_NM, row.EQP_NM);
		updateRow.put(MHGSubInfo.COLS.FNCT_LC_NO, row.FNCT_LC_NO);
		updateRow.put(MHGSubInfo.COLS.EQP_NO, row.EQP_NO);
			
		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}
	
	public ArrayList<MHGSubInfo> selectList(String eqpNo) {
		
		ArrayList<MHGSubInfo> jsList = new ArrayList<MHGSubInfo>();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select idx, TOWER_IDX, '1' groder, '개방후' G_GUBUN , FNCT_LC_DTLS,EQP_NM,FNCT_LC_NO,EQP_NO from %s where EQP_NO = '%s' "
					+ "union ALL"
					+ " select idx, TOWER_IDX, '2' groder, '환기후' G_GUBUN , FNCT_LC_DTLS,EQP_NM,FNCT_LC_NO,EQP_NO from %s where EQP_NO = '%s' "
					+ "union ALL"
					+ " select idx, TOWER_IDX, '3' groder, '작업중' G_GUBUN , FNCT_LC_DTLS,EQP_NM,FNCT_LC_NO,EQP_NO from %s where EQP_NO = '%s' "
					+ " ORDER BY idx, groder, FNCT_LC_DTLS ";
			String sql = String.format(fmt, tableName, eqpNo, tableName, eqpNo, tableName, eqpNo);
			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(MHGSubInfo.COLS.IDX));
				String TOWER_IDX = cursor.getString(cursor.getColumnIndex(MHGSubInfo.COLS.TOWER_IDX));
				String G_GUBUN = cursor.getString(cursor.getColumnIndex(MHGSubInfo.COLS.G_GUBUN));
				String FNCT_LC_DTLS = cursor.getString(cursor.getColumnIndex(MHGSubInfo.COLS.FNCT_LC_DTLS));
				String EQP_NM = cursor.getString(cursor.getColumnIndex(MHGSubInfo.COLS.EQP_NM));
				String FNCT_LC_NO = cursor.getString(cursor.getColumnIndex(MHGSubInfo.COLS.FNCT_LC_NO));
				String EQP_NO = cursor.getString(cursor.getColumnIndex(MHGSubInfo.COLS.EQP_NO));
				MHGSubInfo info = new MHGSubInfo();
				
				info.IDX = idx;
				info.TOWER_IDX = TOWER_IDX;
				info.G_GUBUN = G_GUBUN;
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
