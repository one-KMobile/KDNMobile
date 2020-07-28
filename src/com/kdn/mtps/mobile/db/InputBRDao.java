package com.kdn.mtps.mobile.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.BRInfo;

public class InputBRDao extends BaseDao{
	static InputBRDao inputBRDao;

	private InputBRDao(Context ctx) {
		tableName = "INPUT_BR";
		this.ctx = ctx;
	}

	public static InputBRDao getInstance(Context ctx) {
		if (inputBRDao == null)
			inputBRDao = new InputBRDao(ctx);
		return inputBRDao;
	}
	
	public void Append(BRInfo row) {
		Append(row, -1);
	}
	
	public void Append(BRInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(BRInfo.COLS.IDX, idx);
		}
		
		updateRow.put(BRInfo.COLS.MASTER_IDX, row.masterIdx);
		updateRow.put(BRInfo.COLS.WEATHER, row.weather);
		updateRow.put(BRInfo.COLS.CIRCUIT_NO, row.circuit_no);
		updateRow.put(BRInfo.COLS.CIRCUIT_NAME, row.circuit_name);
		updateRow.put(BRInfo.COLS.EJ_CNT, row.ej_cnt);
		updateRow.put(BRInfo.COLS.BR_CNT, row.br_cnt);
		updateRow.put(BRInfo.COLS.MAKE_DATE, row.make_date); 
		updateRow.put(BRInfo.COLS.MAKE_COMPANY, row.make_company);
		updateRow.put(BRInfo.COLS.YB_RESULT, row.yb_result);
		updateRow.put(BRInfo.COLS.TY_SECD, row.ty_secd);
		updateRow.put(BRInfo.COLS.TY_SECD_NM, row.ty_secd_nm);
		updateRow.put(BRInfo.COLS.PHS_SECD, row.phs_secd);
		updateRow.put(BRInfo.COLS.PHS_SECD_NM, row.phs_secd_nm);
		updateRow.put(BRInfo.COLS.INSBTY_LFT, row.insbty_lft);
		updateRow.put(BRInfo.COLS.INSBTY_RIT, row.insbty_rit);
		updateRow.put(BRInfo.COLS.INSR_EQP_NO, row.insr_eqp_no);
		
		
		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}
	
	public void DeleteIdx(int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE idx = %s";
		String sql = String.format(fmt, tableName, idx);
		db.execSQL(sql);
	}
	
	public ArrayList<BRInfo> selectBR(String mIdx) {
		
		ArrayList<BRInfo> brList = new ArrayList<BRInfo>();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				
				int idx = cursor.getInt(cursor.getColumnIndex(BRInfo.COLS.IDX));
				int master_idx = cursor.getInt(cursor.getColumnIndex(BRInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.WEATHER));
				String circuit_no = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.CIRCUIT_NO));
				String circuit_name = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.CIRCUIT_NAME));
				String ej_cnt = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.EJ_CNT));
				String br_cnt = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.BR_CNT));
				String make_date = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.MAKE_DATE));
				String make_company = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.MAKE_COMPANY));
				String yb_result = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.YB_RESULT));
				String ty_secd = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.TY_SECD));
				String ty_secd_nm = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.TY_SECD_NM));
				String phs_secd = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.PHS_SECD));
				String phs_secd_nm = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.PHS_SECD_NM));
				String insbty_lft = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.INSBTY_LFT));
				String insbty_rit = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.INSBTY_RIT));
				String insr_eqp_no = cursor.getString(cursor.getColumnIndex(BRInfo.COLS.INSR_EQP_NO));
				
				BRInfo info = new BRInfo();
				
				info.idx = idx;
				info.weather = wether;
				info.circuit_no = circuit_no;
				info.circuit_name = circuit_name;
				info.ej_cnt = ej_cnt;
				info.br_cnt = br_cnt;
				info.make_date = make_date;
				info.make_company = make_company;
				info.yb_result = yb_result;
				info.ty_secd = ty_secd;
				info.ty_secd_nm = ty_secd_nm;
				info.phs_secd = phs_secd;
				info.phs_secd_nm = phs_secd_nm;
				info.insbty_lft = insbty_lft;
				info.insbty_rit = insbty_rit;
				info.insr_eqp_no = insr_eqp_no;
				
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
				int master_idx = cursor.getInt(1);
				String wether = cursor.getString(2);
				String startDate = cursor.getString(3);
				String endDate = cursor.getString(4);
				String lightType = cursor.getString(5);
				String category = cursor.getString(6);
				String controlCnt = cursor.getString(7);
				String lightCnt = cursor.getString(8);
				String lightStateTop = cursor.getString(9);
				String lightStateMiddle = cursor.getString(10);
				String lightStateBottom = cursor.getString(11);
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "idx : " + idx);
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "wether : " + wether);
				Log.d("Test", "startDate : " + startDate);
				Log.d("Test", "endDate : " + endDate);
				Log.d("Test", "lightType : " + lightType);
				Log.d("Test", "category : " + category);
				Log.d("Test", "controlCnt : " + controlCnt);
				Log.d("Test", "lightCnt : " + lightCnt);
				Log.d("Test", "lightStateTop : " + lightStateTop);
				Log.d("Test", "lightStateMiddle : " + lightStateMiddle);
				Log.d("Test", "lightStateBottom : " + lightStateBottom);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}
}
