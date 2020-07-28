package com.kdn.mtps.mobile.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.facility.FacilityInfo;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.util.Logg;

public class TowerListDao extends BaseDao{
	static TowerListDao towerListDao;

	private TowerListDao(Context ctx) {
		tableName = "TOWER_LIST";
		this.ctx = ctx;
	}

	public static TowerListDao getInstance(Context ctx) {
		if (towerListDao == null)
			towerListDao = new TowerListDao(ctx);
		return towerListDao;
	}
	
	public void Append(FacilityInfo row) {
		Append(row, -1);
	}
	
	public void Append(FacilityInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(FacilityInfo.COLS.IDX, idx);
		}
		
		updateRow.put(FacilityInfo.COLS.TOWER_IDX, row.TOWER_IDX);
		updateRow.put(FacilityInfo.COLS.FNCT_LC_DTLS, row.FNCT_LC_DTLS);
		updateRow.put(FacilityInfo.COLS.EQP_TY_CD_NM, row.EQP_TY_CD_NM);
		updateRow.put(FacilityInfo.COLS.EQP_NO, row.EQP_NO);
		updateRow.put(FacilityInfo.COLS.EQP_NM, row.EQP_NM);
		updateRow.put(FacilityInfo.COLS.LATITUDE, row.LATITUDE);
		updateRow.put(FacilityInfo.COLS.LONGITUDE, row.LONGITUDE);
		updateRow.put(FacilityInfo.COLS.ADDRESS, row.ADDRESS);
		updateRow.put(FacilityInfo.COLS.CONT_NUM, row.CONT_NUM);

		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Delete(String idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE idx = %s";
		String sql = String.format(fmt, tableName, idx);
		db.execSQL(sql);
	}
	
	public boolean existTower(FacilityInfo info) {
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where EQP_NO = '%s' and TOWER_IDX = '%s' ";
			String sql = String.format(fmt, tableName, info.EQP_NO, info.TOWER_IDX);

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
	
	public FacilityInfo selectInfo(String mIdx) {
		
		FacilityInfo info = new FacilityInfo();
		
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where idx = %s";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(FacilityInfo.COLS.IDX));
				String FNCT_LC_DTLS = cursor.getString(cursor.getColumnIndex(FacilityInfo.COLS.FNCT_LC_DTLS));
				String EQP_TY_CD_NM = cursor.getString(cursor.getColumnIndex(FacilityInfo.COLS.EQP_TY_CD_NM));
				String EQP_NM = cursor.getString(cursor.getColumnIndex(FacilityInfo.COLS.EQP_NM));
				String LATITUDE = cursor.getString(cursor.getColumnIndex(FacilityInfo.COLS.LATITUDE));
				String LONGITUDE = cursor.getString(cursor.getColumnIndex(FacilityInfo.COLS.LONGITUDE));
				String ADDRESS = cursor.getString(cursor.getColumnIndex(FacilityInfo.COLS.ADDRESS));
				String CONT_NUM = cursor.getString(cursor.getColumnIndex(FacilityInfo.COLS.CONT_NUM));
				
				info.IDX = idx;
				info.FNCT_LC_DTLS = FNCT_LC_DTLS;
				info.EQP_TY_CD_NM = EQP_TY_CD_NM;
				info.EQP_NM = EQP_NM;
				info.LATITUDE = LATITUDE;
				info.LONGITUDE = LONGITUDE;
				info.ADDRESS = ADDRESS;
				info.CONT_NUM = CONT_NUM;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return info;
	}

	public boolean existInfo(String mIdx) {
		
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s where idx = %s";
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

	public ArrayList<FacilityInfo> selectFacList(String tracksCode, String eqpNm) {
		
		ArrayList<FacilityInfo> facList = new ArrayList<FacilityInfo>();
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from " + tableName + " where FNCT_LC_DTLS = '" + tracksCode + "' ";
			
			if (eqpNm != null && !"".equals(eqpNm))
				fmt += " and TOWER_IDX LIKE '%" + eqpNm + "%' ";
				
			Logg.d("query : " + fmt);

			cursor = db.rawQuery(fmt, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(FacilityInfo.COLS.IDX));
				String TOWER_IDX = cursor.getString(cursor.getColumnIndex(FacilityInfo.COLS.TOWER_IDX));
				String FNCT_LC_DTLS= cursor.getString(cursor.getColumnIndex(FacilityInfo.COLS.FNCT_LC_DTLS));
				String EQP_TY_CD_NM = cursor.getString(cursor.getColumnIndex(FacilityInfo.COLS.EQP_TY_CD_NM));
				String EQP_NO = cursor.getString(cursor.getColumnIndex(FacilityInfo.COLS.EQP_NO));
				String EQP_NM = cursor.getString(cursor.getColumnIndex(FacilityInfo.COLS.EQP_NM));
				String LATITUDE = cursor.getString(cursor.getColumnIndex(FacilityInfo.COLS.LATITUDE));
				String LONGITUDE = cursor.getString(cursor.getColumnIndex(FacilityInfo.COLS.LONGITUDE));
				String ADDRESS = cursor.getString(cursor.getColumnIndex(FacilityInfo.COLS.ADDRESS));
				String CONT_NUM = cursor.getString(cursor.getColumnIndex(FacilityInfo.COLS.CONT_NUM));
				
				FacilityInfo info = new FacilityInfo();
				info.IDX = idx;
				info.TOWER_IDX = TOWER_IDX;
				info.FNCT_LC_DTLS = FNCT_LC_DTLS;
				info.EQP_TY_CD_NM = EQP_TY_CD_NM;
				info.EQP_TY_CD_NM = EQP_TY_CD_NM;
				info.EQP_NO = EQP_NO;
				info.EQP_NM = EQP_NM;
				info.LATITUDE = LATITUDE;
				info.LONGITUDE = LONGITUDE;
				info.ADDRESS = ADDRESS;
				info.CONT_NUM = CONT_NUM;
				
				facList.add(info);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return facList;
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
				String master_idx = cursor.getString(1);
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
