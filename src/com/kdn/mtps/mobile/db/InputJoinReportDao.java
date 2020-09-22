package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.BTInfo;
import com.kdn.mtps.mobile.input.JoinReportInfo;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.util.Logg;

import java.util.ArrayList;

public class InputJoinReportDao extends BaseDao{
	static InputJoinReportDao inputJoinReportDao;

	private InputJoinReportDao(Context ctx) {
		tableName = "INPUT_JOIN_REPORT";
		this.ctx = ctx;
	}

	public static InputJoinReportDao getInstance(Context ctx) {
		if (inputJoinReportDao == null)
			inputJoinReportDao = new InputJoinReportDao(ctx);
		return inputJoinReportDao;
	}
	
	public void Append(JoinReportInfo row) {
		Append(row, -1);
	}
	
	public void Append(JoinReportInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(JoinReportInfo.COLS.IDX, idx);
		}

		//updateRow.put(JoinReportInfo.COLS.IDX, row.idx);
		updateRow.put(JoinReportInfo.COLS.NAME, row.name);
		updateRow.put(JoinReportInfo.COLS.LENGTH, row.length);
		updateRow.put(JoinReportInfo.COLS.LOCATION, row.location);
		updateRow.put(JoinReportInfo.COLS.WORK_NM, row.work_nm);
		updateRow.put(JoinReportInfo.COLS.JOIN_DATE, row.join_date);
		updateRow.put(JoinReportInfo.COLS.REQUEST_DATE, row.request_date);
		updateRow.put(JoinReportInfo.COLS.JOIN_S_DATE, row.join_s_date);
		updateRow.put(JoinReportInfo.COLS.JOIN_S_TIME, row.join_s_time);
		updateRow.put(JoinReportInfo.COLS.JOIN_E_DATE, row.join_e_date);
		updateRow.put(JoinReportInfo.COLS.JOIN_E_TIME, row.join_e_time);
		updateRow.put(JoinReportInfo.COLS.REQUEST_JOINER_CO, row.request_joiner_co);
		updateRow.put(JoinReportInfo.COLS.REQUEST_JOINER, row.request_joiner);
		updateRow.put(JoinReportInfo.COLS.JOINER_DEPT, row.joiner_dept);
		updateRow.put(JoinReportInfo.COLS.JOINER, row.joiner);
		updateRow.put(JoinReportInfo.COLS.JOIN_REASON, row.join_reason);
		updateRow.put(JoinReportInfo.COLS.ETC, row.etc);

		db.replace(tableName, null, updateRow);

//		dbCheck();
	}
	
	public void Delete(String mIdx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE idx = %s";
		String sql = String.format(fmt, tableName, mIdx);
		db.execSQL(sql);
	}

	public JoinReportInfo selectJoinReport(String mIdx) {

		JoinReportInfo info = new JoinReportInfo();

		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where idx = '%s'";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(0);
				String weather = cursor.getString(1);
				String name = cursor.getString(2);
				String length = cursor.getString(3);
				String location = cursor.getString(4);
				String work_nm = cursor.getString(5);
				String join_date = cursor.getString(6);
				String request_date = cursor.getString(7);
				String join_s_date = cursor.getString(8);
				String join_s_time = cursor.getString(9);
				String join_e_date = cursor.getString(10);
				String join_e_time = cursor.getString(11);
				String request_joiner_co = cursor.getString(12);
				String request_joiner = cursor.getString(13);
				String joiner_dept = cursor.getString(14);
				String joiner = cursor.getString(15);
				String join_reason = cursor.getString(16);
				String etc = cursor.getString(17);

				info.idx = idx;
				info.name = name;
				info.length = length;
				info.location = location;
				info.work_nm = work_nm;
				info.join_date = join_date;
				info.request_date = request_date;
				info.join_s_date = join_s_date;
				info.join_s_time = join_s_time;
				info.join_e_date = join_e_date;
				info.join_e_time = join_e_time;
				info.request_joiner_co = request_joiner_co;
				info.request_joiner = request_joiner;
				info.joiner_dept = joiner_dept;
				info.joiner = joiner;
				info.join_reason = join_reason;
				info.etc = etc;

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return info;
	}

	public ArrayList<JoinReportInfo> selectJoinReport(String mName, String mWorkNm, String sDate, String eDate) {

		ArrayList<JoinReportInfo> joinReportList = new ArrayList<JoinReportInfo>();

		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			//String fmt = "select * from %s where name like '%'||trim('%s')||'%' and WORK_NM like '%'||trim('%s')||'%' and JOIN_DATE between '%s' and '%s'";
			String fmt = "select * from INPUT_JOIN_REPORT "
					+ " WHERE JOIN_DATE between '" + sDate + "' and '"+eDate+"'";

			if (mName != null && !"".equals(mName))
				fmt += " and name = '" + mName + "' ";

			if (mWorkNm != null && !"".equals(mWorkNm))
				fmt += " and mWorkNm = '" + mWorkNm + "' ";

			Logg.d("query : " + fmt);

			//String sql = String.format(fmt, tableName, mName.trim(), mWorkNm.trim(), sDate, eDate);

			cursor = db.rawQuery(fmt, null);
			//cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(0);
				String weather = cursor.getString(1);
				String name = cursor.getString(2);
				String length = cursor.getString(3);
				String location = cursor.getString(4);
				String work_nm = cursor.getString(5);
				String join_date = cursor.getString(6);
				String request_date = cursor.getString(7);
				String join_s_date = cursor.getString(8);
				String join_s_time = cursor.getString(9);
				String join_e_date = cursor.getString(10);
				String join_e_time = cursor.getString(11);
				String request_joiner_co = cursor.getString(12);
				String request_joiner = cursor.getString(13);
				String joiner_dept = cursor.getString(14);
				String joiner = cursor.getString(15);
				String join_reason = cursor.getString(16);
				String etc = cursor.getString(17);

				JoinReportInfo info = new JoinReportInfo();
				info.idx = idx;
				info.weather = weather;
				info.name = name;
				info.length = length;
				info.location = location;
				info.work_nm = work_nm;
				info.join_date = join_date;
				info.request_date = request_date;
				info.join_s_date = join_s_date;
				info.join_s_time = join_s_time;
				info.join_e_date = join_e_date;
				info.join_e_time = join_e_time;
				info.request_joiner_co = request_joiner_co;
				info.request_joiner = request_joiner;
				info.joiner_dept = joiner_dept;
				info.joiner = joiner;
				info.join_reason = join_reason;
				info.etc = etc;
				joinReportList.add(info);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return joinReportList;
	}

	public ArrayList<JoinReportInfo> selectJoinReportList(String mIdx) {
		
		ArrayList<JoinReportInfo> btList = new ArrayList<JoinReportInfo>();
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s where master_idx = %s";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {

				int idx = cursor.getInt(0);
				String name = cursor.getString(1);
				String length = cursor.getString(2);
				String location = cursor.getString(3);
				String work_nm = cursor.getString(4);
				String join_date = cursor.getString(5);
				String request_date = cursor.getString(6);
				String join_s_date = cursor.getString(7);
				String join_s_time = cursor.getString(8);
				String join_e_date = cursor.getString(9);
				String join_e_time = cursor.getString(10);
				String request_joiner_co = cursor.getString(11);
				String request_joiner = cursor.getString(12);
				String joiner_dept = cursor.getString(13);
				String joiner = cursor.getString(14);
				String join_reason = cursor.getString(15);
				String etc = cursor.getString(16);

				JoinReportInfo info = new JoinReportInfo();

				info.idx = idx;
				info.name = name;
				info.length = length;
				info.location = location;
				info.work_nm = work_nm;
				info.join_date = join_date;
				info.request_date = request_date;
				info.join_s_date = join_s_date;
				info.join_s_time = join_s_time;
				info.join_e_date = join_e_date;
				info.join_e_time = join_e_time;
				info.request_joiner_co = request_joiner_co;
				info.request_joiner = request_joiner;
				info.joiner_dept = joiner_dept;
				info.joiner = joiner;
				info.join_reason = join_reason;
				info.etc = etc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return btList;
	}
	
	public boolean existJoinReport(String mIdx) {
		
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

	public void dbCheck() {
		Log.d("Test", "############ DB value check #############");
		
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s";
			String sql = String.format(fmt, tableName);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(0);
				String name = cursor.getString(1);
				String length = cursor.getString(2);
				String location = cursor.getString(3);
				String work_nm = cursor.getString(4);
				String join_date = cursor.getString(5);
				String request_date = cursor.getString(6);
				String join_s_date = cursor.getString(7);
				String join_s_time = cursor.getString(8);
				String join_e_date = cursor.getString(9);
				String join_e_time = cursor.getString(10);
				String request_joiner_co = cursor.getString(11);
				String request_joiner = cursor.getString(12);
				String joiner_dept = cursor.getString(13);
				String joiner = cursor.getString(14);
				String join_reason = cursor.getString(15);
				String etc = cursor.getString(16);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}
}
