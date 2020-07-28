package com.kdn.mtps.mobile.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.db.bean.CameraLog;
import com.kdn.mtps.mobile.input.BTInfo;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.notice.NoticeInfo;

public class CameraLogDao extends BaseDao{
	static CameraLogDao cameraLogDao;

	private CameraLogDao(Context ctx) {
		tableName = "CAMERA_LOG";
		this.ctx = ctx;
	}

	public static CameraLogDao getInstance(Context ctx) {
		if (cameraLogDao == null)
			cameraLogDao = new CameraLogDao(ctx);
		return cameraLogDao;
	}
	
	public void Append(CameraLog row) {
		Append(row, -1);
	}
	
	public void Append(CameraLog row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(NoticeInfo.COLS.IDX, idx);
		}
		
		updateRow.put(CameraLog.COLS.MASTER_IDX, row.master_idx);
		updateRow.put(CameraLog.COLS.IMAGE_PATH, row.img_path);
		updateRow.put(CameraLog.COLS.SUBJECT, row.subject);
		updateRow.put(CameraLog.COLS.CONTENT, row.content);
		updateRow.put(CameraLog.COLS.INS_TYPE, row.ins_type);
		
		db.replace(tableName, null, updateRow);

		dbCheck();
	}
	
	public ArrayList<CameraLog> selectPickList(String mIdx, String insType) {
		// 예방순시는 스케줄 정보를 받지 않기 때문에 master_idx 가 없음.
		// 따라서 예방순시 master_idx값은 선로코드 값이 저장된다.
		// 예방순시 때문에 master_idx값이 유니크하지 않기 때문에 ins_type(순시종류) 필드를 조건으로 추가함.
		ArrayList<CameraLog> pickList = new ArrayList<CameraLog>();
		try {
			
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);

			String fmt = "select * from %s where master_idx = '%s' and ins_type = '%s' ";
			String sql = String.format(fmt, tableName, mIdx, insType);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(CameraLog.COLS.IDX));
				String master_idx = cursor.getString(cursor.getColumnIndex(CameraLog.COLS.MASTER_IDX));
				String img_path = cursor.getString(cursor.getColumnIndex(CameraLog.COLS.IMAGE_PATH));
				String subject = cursor.getString(cursor.getColumnIndex(CameraLog.COLS.SUBJECT));
				String content = cursor.getString(cursor.getColumnIndex(CameraLog.COLS.CONTENT));
				String ins_type = cursor.getString(cursor.getColumnIndex(CameraLog.COLS.INS_TYPE));
				
				CameraLog info = new CameraLog();
				info.idx = idx;
				info.master_idx = master_idx;
				info.img_path = img_path;
				info.isChecked = false;
				info.subject = subject;
				info.content = content;
				info.ins_type = ins_type;
				
				pickList.add(info);
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "idx : " + idx);
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "img_path : " + img_path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return pickList;
	}
	
	public void delete(int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		String fmt = "DELETE FROM %s WHERE idx = %s ";
		String sql = String.format(fmt, tableName, idx);
		db.execSQL(sql);
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
				String img_path = cursor.getString(2);
				
				Log.d("Test", "======================START====================");
				Log.d("Test", "idx : " + idx);
				Log.d("Test", "master_idx : " + master_idx);
				Log.d("Test", "img_path : " + img_path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}
}
