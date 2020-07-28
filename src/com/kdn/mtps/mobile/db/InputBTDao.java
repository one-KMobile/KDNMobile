package com.kdn.mtps.mobile.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.db.bean.InputBTLog;
import com.kdn.mtps.mobile.input.BTInfo;

public class InputBTDao extends BaseDao{
	static InputBTDao inputBTDao;

	private InputBTDao(Context ctx) {
		tableName = "INPUT_BT";
		this.ctx = ctx;
	}

	public static InputBTDao getInstance(Context ctx) {
		if (inputBTDao == null)
			inputBTDao = new InputBTDao(ctx);
		return inputBTDao;
	}
	
	public void Append(InputBTLog row) {
		Append(row, -1);
	}
	
	public void Append(InputBTLog row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(InputBTLog.COLS.IDX, idx);
		}
		
		updateRow.put(InputBTLog.COLS.MASTER_IDX, row.master_idx);
		updateRow.put(InputBTLog.COLS.WEATHER, row.weather);
		updateRow.put(InputBTLog.COLS.WORKER_CNT, row.worker_cnt);
		updateRow.put(InputBTLog.COLS.CLAIM_CONTENT, row.claim_content);
		updateRow.put(InputBTLog.COLS.CHECK_RESULT, row.check_result);
		updateRow.put(InputBTLog.COLS.ETC, row.etc);
//		updateRow.put(InputBTLog.COLS.INS_RESULT_CODE, row.ins_result_code);
//		updateRow.put(InputBTLog.COLS.CHECK_RESULT_CODE, row.check_result_code);
//		updateRow.put(InputBTLog.COLS.EQP_TYPE_CODE, row.eqp_type_code);
//		updateRow.put(InputBTLog.COLS.DETAIL_ITEM_CODE, row.detail_item_code);
//		updateRow.put(InputBTLog.COLS.SPT_MGT_YN, row.spt_mgt_yn);

		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE master_idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}
	
	public BTInfo selectBT(String mIdx) {
		
		BTInfo info = new BTInfo();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(0);
				int master_idx = cursor.getInt(1);
				String wether = cursor.getString(2);
				String worker_cnt = cursor.getString(3);
				String claim_content = cursor.getString(4);
				String check_result = cursor.getString(5);
				String etc = cursor.getString(6);
				
				info.idx = idx;
				info.weather = wether;
				info.worker_cnt = worker_cnt;
				info.claim_content = claim_content;
				info.check_result = check_result;
				info.etc = etc;
				
//				info.result = String.valueOf(ins_result_code);
//				info.result2 = String.valueOf(check_result_code);
//				info.type = String.valueOf(eqp_type_code);
//				info.detail = String.valueOf(detail_item_code);
//				info.proceed = spt_mgt_yn;
				
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "idx : " + idx);
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "wether : " + wether);
				Log.d("Test", "worker_cnt : " + worker_cnt);
				Log.d("Test", "claim_content : " + claim_content);
				Log.d("Test", "check_result : " + check_result);
				Log.d("Test", "etc : " + etc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return info;
	}

	public ArrayList<BTInfo> selectBTList(String mIdx) {
		
		ArrayList<BTInfo> btList = new ArrayList<BTInfo>();
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s where master_idx = %s";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(0);
				int master_idx = cursor.getInt(1);
				int ins_result_code = cursor.getInt(2);
				int check_result_code = cursor.getInt(3);
				int eqp_type_code = cursor.getInt(4);
				int detail_item_code = cursor.getInt(5);
				String spt_mgt_yn = cursor.getString(6);
				
				BTInfo info = new BTInfo();
				info.idx = idx;
				info.result = String.valueOf(ins_result_code);
				info.result2 = String.valueOf(check_result_code);
				info.type = String.valueOf(eqp_type_code);
				info.detail = String.valueOf(detail_item_code);
				info.proceed = spt_mgt_yn;
				
				btList.add(info);
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "idx : " + idx);
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "ins_result_code : " + ins_result_code);
				Log.d("Test", "check_result_code : " + check_result_code);
				Log.d("Test", "eqp_type_code : " + eqp_type_code);
				Log.d("Test", "detail_item_code : " + detail_item_code);
				Log.d("Test", "spt_mgt_yn : " + spt_mgt_yn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return btList;
	}
	
	public boolean existBT(String mIdx) {
		
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
				String worker_cnt = cursor.getString(3);
				String claim_content = cursor.getString(4);
				String check_result = cursor.getString(5);
				String etc = cursor.getString(6);
//				int ins_result_code = cursor.getInt(2);
//				int check_result_code = cursor.getInt(3);
//				int eqp_type_code = cursor.getInt(4);
//				int detail_item_code = cursor.getInt(5);
//				String spt_mgt_yn = cursor.getString(6);
				
				Log.d("Test", "idx : " + idx);
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "wether : " + wether);
				Log.d("Test", "worker_cnt : " + worker_cnt);
				Log.d("Test", "claim_content : " + claim_content);
				Log.d("Test", "check_result : " + check_result);
				Log.d("Test", "etc : " + etc);
//				Log.d("Test", "ins_result_code : " + ins_result_code);
//				Log.d("Test", "check_result_code : " + check_result_code);
//				Log.d("Test", "eqp_type_code : " + eqp_type_code);
//				Log.d("Test", "detail_item_code : " + detail_item_code);
//				Log.d("Test", "spt_mgt_yn : " + spt_mgt_yn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}
}
