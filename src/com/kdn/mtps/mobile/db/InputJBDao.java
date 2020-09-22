package com.kdn.mtps.mobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kdn.mtps.mobile.input.JBInfo;
import com.kdn.mtps.mobile.util.Logg;

import java.util.ArrayList;

public class InputJBDao extends BaseDao{
	static InputJBDao InputJBDao;

	private InputJBDao(Context ctx) {
		tableName = "INPUT_JB";
		this.ctx = ctx;
	}

	public static InputJBDao getInstance(Context ctx) {
		if (InputJBDao == null)
			InputJBDao = new InputJBDao(ctx);
		return InputJBDao;
	}
	
	public void Append(JBInfo row) {
		Append(row, -1);
	}
	
	public void Append(JBInfo row, int idx) {
		SQLiteDatabase db = DBHelper.getInstance(ctx);
		ContentValues updateRow = new ContentValues();
		
		if (idx != -1) {
			updateRow.put(JBInfo.COLS.IDX, idx);
		}
		
		updateRow.put(JBInfo.COLS.MASTER_IDX, row.master_idx);
		updateRow.put(JBInfo.COLS.WEATHER, row.weather);
		updateRow.put(JBInfo.COLS.AREA_TEMP, row.area_temp);
		updateRow.put(JBInfo.COLS.CIRCUIT_NAME, row.circuit_name);
		updateRow.put(JBInfo.COLS.CLAIM_CONTENT, row.claim_content);
		updateRow.put(JBInfo.COLS.T_GUBUN, row.t_gubun);
		updateRow.put(JBInfo.COLS.T1_NO, row.t1_no);
		updateRow.put(JBInfo.COLS.T1_C, row.t1_c);
		updateRow.put(JBInfo.COLS.T2_NO, row.t2_no);
		updateRow.put(JBInfo.COLS.T2_C, row.t2_c);
		updateRow.put(JBInfo.COLS.T3_NO, row.t3_no);
		updateRow.put(JBInfo.COLS.T3_C, row.t3_c);

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

	public ArrayList<JBInfo> selectJBS(String mIdx) {

		ArrayList<JBInfo> jsList = new ArrayList<JBInfo>();

		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s and t_gubun = '1'";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(JBInfo.COLS.IDX));
				String master_idx = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.WEATHER));
				String area_temp = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.AREA_TEMP));

				String circuit_name = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.CIRCUIT_NAME));
				String claim_content = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.CLAIM_CONTENT));
				String t1_no = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.T1_NO));
				String t1_c = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.T1_C));

				JBInfo info = new JBInfo();

				info.idx = idx;
				info.master_idx = master_idx;
				info.weather = wether;
				info.area_temp = area_temp;

				info.circuit_name = circuit_name;
				info.claim_content = claim_content;
				info.t1_no= t1_no;
				info.t1_c= t1_c;

				jsList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return jsList;
	}

	public ArrayList<JBInfo> selectJBJ(String mIdx) {

		ArrayList<JBInfo> jsList = new ArrayList<JBInfo>();

		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s and t_gubun = '2'";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(JBInfo.COLS.IDX));
				String master_idx = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.WEATHER));
				String area_temp = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.AREA_TEMP));

				String circuit_name = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.CIRCUIT_NAME));
				String claim_content = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.CLAIM_CONTENT));
				String t2_no = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.T2_NO));
				String t2_c = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.T2_C));

				JBInfo info = new JBInfo();

				info.idx = idx;
				info.master_idx = master_idx;
				info.weather = wether;
				info.area_temp = area_temp;

				info.circuit_name = circuit_name;
				info.claim_content = claim_content;
				info.t2_no= t2_no;
				info.t2_c= t2_c;

				jsList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return jsList;
	}

	public ArrayList<JBInfo> selectJBY(String mIdx) {

		ArrayList<JBInfo> jsList = new ArrayList<JBInfo>();

		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt = "select * from %s where master_idx = %s and t_gubun = '3'";
			String sql = String.format(fmt, tableName, mIdx);

			cursor = db.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				int idx = cursor.getInt(cursor.getColumnIndex(JBInfo.COLS.IDX));
				String master_idx = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.MASTER_IDX));
				String wether = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.WEATHER));
				String area_temp = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.AREA_TEMP));

				String circuit_name = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.CIRCUIT_NAME));
				String claim_content = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.CLAIM_CONTENT));
				String t3_no = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.T3_NO));
				String t3_c = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.T3_C));

				JBInfo info = new JBInfo();

				info.idx = idx;
				info.master_idx = master_idx;
				info.weather = wether;
				info.area_temp = area_temp;

				info.circuit_name = circuit_name;
				info.claim_content = claim_content;
				info.t3_no= t3_no;
				info.t3_c= t3_c;

				jsList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return jsList;
	}

	public ArrayList<JBInfo> selectInspectNoList(String gubun) {

		ArrayList<JBInfo> jsList = new ArrayList<JBInfo>();
		try {
			SQLiteDatabase db = DBHelper.getReadableInstance(ctx);
			String fmt =  "select gubun, T_NO, INSPECT_NO from ( "
					+ "	select 1 gubun, 1 T_NO, '점검표에검사기일이 기재되어 있는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 1 gubun, 2 T_NO, '소화기 본체에는 검정인이 탈락되지 않았는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 1 gubun, 3 T_NO, '사용방법 및 적응화재 표시는 되어 있는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 1 gubun, 4 T_NO, '용기본체의 도장이 벗겨진 부분이 부식되고 있지 않은가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 1 gubun, 5 T_NO, '설치장소에 소화기표시는 되어 있는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 1 gubun, 6 T_NO, '밸브 및 패킹이 노후되거나 탈락되지 않았는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 1 gubun, 7 T_NO, '노즐등에 이물질이 끼어 있지는 않는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 1 gubun, 8 T_NO, '소화약제용기 지시압력치는 적당한가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 1 gubun, 9 T_NO, '수신부의 설치장소 및 음량장치의 음량은 적정한가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 1 gubun, 10 T_NO, '가스누설 시험시 감지기의 작동하며 연료특성에 따른 적절한 설치위치인가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 1 gubun, 11 T_NO, '방출구에서 소화약제 방출 시 장애는 없는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 1 gubun, 12 T_NO, '가스차단밸브는 견고하며 정상적으로 계폐되는가' INSPECT_NO "
					+ "	union all "
					+ "	 select 2 gubun, 1 T_NO, '수신기가 있는 장소에는 경계구역 알람고를 비치하였는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 2 gubun, 2 T_NO, '수신기 부근에 조작상 지장을 주는 장애물은 없는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 2 gubun, 3 T_NO, '수신기 조작부 스위치는 정상위치에 있는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 2 gubun, 4 T_NO, '감지기는 유효하게 화재발생을 감지할 수 있도록 설치되어 있는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 2 gubun, 5 T_NO, '연기감지기는 출입구 부분이나 흡입구가 있는 실내의 그 부분에 설치되어 있는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 2 gubun, 6 T_NO, '발신기의 상단에 표시등은 점등되어 있는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 2 gubun, 7 T_NO, '비상전원이 방전되고 있지 않았는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 3 gubun, 1 T_NO, '소방펌프차량은 쉽게 접근할 수 있는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 3 gubun, 2 T_NO, '사용에 지장이 없는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 3 gubun, 3 T_NO, '송수구에는 나사식 보호용 덮개가 부착되어 있는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 3 gubun, 4 T_NO, '가압소수장치는 이상없으며 전원은 단절되어 있지 않은가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 3 gubun, 5 T_NO, '방수용 기구함 속에는 15m 호오스 5개 이상, 노즐 2개이상이 수납되어 있는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 3 gubun, 6 T_NO, '방수용 기구함에 표시된 방수기구함표지는 이상없는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 3 gubun, 7 T_NO, '살수헤드의 살수에 지장이 있는 장애물은 없는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 3 gubun, 8 T_NO, '송수구에 소방펌프차가 쉽게 접근할 수 있으며 연결살수설비용 송수구표지는 이상이 없는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 3 gubun, 9 T_NO, '하나의 송수구역의 부착헤드는 개발형 또는 폐쇄형 헤드의 어느 것이든 하나의 종류로 되어 있는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 3 gubun, 10 T_NO, '송수구역표시 계통도가 설치 되었는가' INSPECT_NO "
					+ "	union all  "
					+ "	 select 3 gubun, 11 T_NO, '살수헤드가 파손, 탈락된 부분은 없는가' INSPECT_NO "
					+ " ) where gubun = "+gubun+" order by t_no";

			cursor = db.rawQuery(fmt, null);
			while (cursor.moveToNext()) {
				String inspect_no = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.INSPECT_NO));
				String t_no = cursor.getString(cursor.getColumnIndex(JBInfo.COLS.T_NO));
				JBInfo info = new JBInfo();

				info.inspect_no = inspect_no;
				info.t_no = t_no;

				jsList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return jsList;
	}

	public boolean existJB(String mIdx) {
		
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
				
				//Log.d("Test", "idx : " + idx);
				//Log.d("Test", "master_idx : " + master_idx);
				//Log.d("Test", "wether : " + wether);
				//Log.d("Test", "worker_cnt : " + worker_cnt);
				//Log.d("Test", "claim_content : " + claim_content);
				//Log.d("Test", "check_result : " + check_result);
				//Log.d("Test", "etc : " + etc);
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
