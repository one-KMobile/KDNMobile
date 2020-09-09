package com.kdn.mtps.mobile.datasync;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.db.DBHelper;
import com.kdn.mtps.mobile.db.InputBRDao;
import com.kdn.mtps.mobile.db.InputBRSubInfoDao;
import com.kdn.mtps.mobile.db.InputBTDao;
import com.kdn.mtps.mobile.db.InputGHSubInfoDao;
import com.kdn.mtps.mobile.db.InputHJDao;
import com.kdn.mtps.mobile.db.InputHKDao;
import com.kdn.mtps.mobile.db.InputHKSubInfoDao;
import com.kdn.mtps.mobile.db.InputJGUSubInfoDao;
import com.kdn.mtps.mobile.db.InputJJDao;
import com.kdn.mtps.mobile.db.InputJPDao;
import com.kdn.mtps.mobile.db.InputJSDao;
import com.kdn.mtps.mobile.db.InputJSSubInfoDao;
import com.kdn.mtps.mobile.db.InputKBDao;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.db.ManageCodeDao;
import com.kdn.mtps.mobile.db.TowerListDao;
import com.kdn.mtps.mobile.db.bean.ManageCodeLog;
import com.kdn.mtps.mobile.facility.FacilityInfo;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.info.InspectOutput;
import com.kdn.mtps.mobile.info.UserInfo;
import com.kdn.mtps.mobile.input.BRInfo;
import com.kdn.mtps.mobile.input.BRSubInfo;
import com.kdn.mtps.mobile.input.BTInfo;
import com.kdn.mtps.mobile.input.GHSubInfo;
import com.kdn.mtps.mobile.input.HJInfo;
import com.kdn.mtps.mobile.input.HKInfo;
import com.kdn.mtps.mobile.input.HKSubInfo;
import com.kdn.mtps.mobile.input.JGUSubInfo;
import com.kdn.mtps.mobile.input.JJInfo;
import com.kdn.mtps.mobile.input.JSInfo;
import com.kdn.mtps.mobile.input.JSSubInfo;
import com.kdn.mtps.mobile.input.KBInfo;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.net.api.ApiManager;
import com.kdn.mtps.mobile.net.api.bean.BRSubInfoList;
import com.kdn.mtps.mobile.net.api.bean.BaseResult;
import com.kdn.mtps.mobile.net.api.bean.CodeList;
import com.kdn.mtps.mobile.net.api.bean.CodeList.CodeDef;
import com.kdn.mtps.mobile.net.api.bean.FacilityList;
import com.kdn.mtps.mobile.net.api.bean.HKSubInfoList;
import com.kdn.mtps.mobile.net.api.bean.JSSubInfoList;
import com.kdn.mtps.mobile.net.api.bean.JGUSubInfoList;
import com.kdn.mtps.mobile.net.api.bean.GHSubInfoList;
import com.kdn.mtps.mobile.net.api.bean.NoticeList;
import com.kdn.mtps.mobile.net.api.bean.ScheduleList;
import com.kdn.mtps.mobile.net.api.bean.ScheduleList.ScheduleInfo;
import com.kdn.mtps.mobile.net.api.bean.TracksList;
import com.kdn.mtps.mobile.net.api.bean.TracksList.TracksInfo;
import com.kdn.mtps.mobile.net.http.HttpUtil;
import com.kdn.mtps.mobile.util.AlertUtil;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.ToastUtil;
import com.kdn.mtps.mobile.util.thread.ATask;
import com.kdn.mtps.mobile.util.thread.ATask.OnPublishProgress;

public class DataSync {
	
	public final int TAG_DATASYNC_DEFAULT = 1000;
	public final int TAG_CALL_API_TRACKS_LIST = TAG_DATASYNC_DEFAULT + 1;
	public final int TAG_CALL_API_CODE_LIST = TAG_DATASYNC_DEFAULT + 2;
	public final int TAG_END_SYNC = TAG_DATASYNC_DEFAULT + 3;
	public final int TAG_CALL_API_SCHDULE = TAG_DATASYNC_DEFAULT + 4;
	public final int TAG_CALL_API_TOWER_LIST = TAG_DATASYNC_DEFAULT + 5;
	//public final int TAG_CALL_API_JSINFO_SUBLIST = TAG_DATASYNC_DEFAULT + 6;
	//public final int TAG_CALL_API_BRINFO_SUBLIST = TAG_DATASYNC_DEFAULT + 7;
	//public final int TAG_CALL_API_HKINFO_SUBLIST = TAG_DATASYNC_DEFAULT + 8;

	public final int TAG_CALL_API_JGUINFO_SUBLIST = TAG_DATASYNC_DEFAULT + 6;
	public final int TAG_CALL_API_GHINFO_SUBLIST = TAG_DATASYNC_DEFAULT + 7;
	
	
	public static Context ctx;
	TracksList tracksList;
	CodeList codeList;
	NoticeList noticeList;
	ProgressDialog pd;
	boolean isComplete;
	boolean isCancel;
	
	static DataSync dataSync;
	protected OnSyncCompletedListener onSyncCompletedListener;
	
	ScheduleList scheduleList;
	FacilityList resultTowerList;
	JSSubInfoList jsSubInfoList;
	BRSubInfoList brSubInfoList;
	HKSubInfoList hkSubInfoList;

	JGUSubInfoList jguSubInfoList;
	GHSubInfoList ghSubInfoList;
	
	int callType;
	boolean isLogin;

	public void setOnSyncResult(OnSyncCompletedListener onSyncCompletedListener) {
		this.onSyncCompletedListener = onSyncCompletedListener; 
	}
	
	public interface OnSyncCompletedListener {
		public void onSyncCompleted(int callType, boolean isComplete);
	}
	
	private DataSync() {
	}
	
	public static DataSync getInstance(Context c) {
		ctx = c;
		if (dataSync == null)
			dataSync = new DataSync();
		return dataSync;
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Logg.d(ctx, "handleMessage");
			
			switch (msg.what) {
			case TAG_CALL_API_CODE_LIST: // 2
			{
				pd.setProgress((int) ((1 / (float) 7) * 100));
				apiCodeInfoList();
				break;
			}
			case TAG_CALL_API_TRACKS_LIST: // 3
			{
				pd.setProgress((int) ((2 / (float) 7) * 100));
				apiTracksList();
				break;
			}
			/*case TAG_CALL_API_JSINFO_SUBLIST: //
				pd.setProgress((int) ((3 / (float) 7) * 100));
				getJSInfoSubList();
				break;
			case TAG_CALL_API_BRINFO_SUBLIST: //
				pd.setProgress((int) ((4 / (float) 7) * 100));
				getBRInfoSubList();
				break;
			case TAG_CALL_API_HKINFO_SUBLIST: //
				pd.setProgress((int) ((5 / (float) 7) * 100));
				getHKInfoSubList();
				break;*/
			case TAG_CALL_API_SCHDULE: // 1
			{
				pd.setProgress((int) ((0 / (float) 7) * 100));
				getScheduleList();
				break;
			}
			case TAG_CALL_API_TOWER_LIST: // 6
				pd.setProgress((int) ((5 / (float) 7) * 100));
				getTowerList();
				break;
			case TAG_CALL_API_JGUINFO_SUBLIST: // 4
				pd.setProgress((int) ((3 / (float) 7) * 100));
				getJGUInfoSubList();
				break;
			case TAG_CALL_API_GHINFO_SUBLIST: // 5
				pd.setProgress((int) ((4 / (float) 7) * 100));
				getGHInfoSubList();
				break;
			case TAG_END_SYNC:
			{
				dismissDialog();
				
				if (onSyncCompletedListener != null)
					onSyncCompletedListener.onSyncCompleted(callType, isComplete);

				
				if (!isComplete) {
					if (callType == ConstVALUE.CALL_TYPE_DATASYNC)
						AlertUtil.showNoTitleAlertOK(ctx, "데이터 동기화가 실패하였습니다.");
//						ToastUtil.show(ctx, "데이터 동기화가 실패하였습니다.");
					
				}
				
				break;
			}
			}
		}
	};
	
	public void startSync(boolean isLogin) {
		this.isLogin = isLogin;
		isCancel = false;
		
		callType = ConstVALUE.CALL_TYPE_DATASYNC; 
		
		showDialog("데이터 동기화중입니다..");
		
		mHandler.sendEmptyMessage(TAG_CALL_API_SCHDULE);
	}
	
	public void startSendInspectResult(ArrayList<InspectOutput> outputList) {
		isCancel = false;
		
		callType = ConstVALUE.CALL_TYPE_SEND_INSPECT_RESULT;
		
		showDialog("데이터 전송중입니다..");
		
		apiSendInspectResult(outputList);
		
	}
	
	public void cancelSync() {
		isCancel = true;
		isComplete = false;
		HttpUtil.cancel();
		dismissDialog();
		
		try {
			SQLiteDatabase db = DBHelper.getInstance(ctx);
			db.endTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mHandler.sendEmptyMessage(TAG_END_SYNC);
	}
	
	public void apiTracksList() {
		ATask.executeVoid(new ATask.OnTask() {
			public void onPre() {
			}

			public void onBG() {
				tracksList = ApiManager.tracksList();
				
				isComplete = false;
				if (tracksList != null && tracksList.tracksList != null && ApiManager.RESULT_OK.equals(tracksList.code)) {
					
					ArrayList<ManageCodeLog> logs = new ArrayList<ManageCodeLog>();
					
					for (int i=0; i<tracksList.tracksList.size(); i++) {
						TracksInfo info = tracksList.tracksList.get(i);
						
						ManageCodeLog log = new ManageCodeLog();
						log.code_type = ConstVALUE.CODE_TYPE_TRACKS;
						log.code_key= info.FNCT_LC_NO;
						log.code_value=info.FNCT_LC_DTLS;
						logs.add(log);
					}
						
					ManageCodeDao manageCodeDao = ManageCodeDao.getInstance(ctx);
					manageCodeDao.Append(logs);
					
					CodeInfo.getInstance(ctx).setCodeResource();
					
					isComplete = true;
				}
			}

			@Override
			public void onPost() {
				
				if (isCancel)
					return;
				
				if (isComplete) {
					mHandler.sendEmptyMessage(TAG_CALL_API_JGUINFO_SUBLIST);
					//mHandler.sendEmptyMessage(TAG_CALL_API_TOWER_LIST);
				} else {
					mHandler.sendEmptyMessage(TAG_END_SYNC);
				}
			}
			
		});
	}
	
	public void apiCodeInfoList() {
		
		ATask.executeVoid(new ATask.OnTask() {
			public void onPre() {
			}

			public void onBG() {
				Logg.d("apiCodeInfoList");
				codeList = ApiManager.codeInfoList();
				
				isComplete = false;
				if (codeList != null && codeList.apiCodeInfoList != null && ApiManager.RESULT_OK.equals(codeList.code)) {
					ArrayList<ManageCodeLog> logs = new ArrayList<ManageCodeLog>();

					SQLiteDatabase db = DBHelper.getInstance(ctx);
					try {
						db.beginTransaction();
						ManageCodeDao.getInstance(ctx).DeleteAll();
						
						for (int i=0; i<codeList.apiCodeInfoList.size(); i++) {
							CodeDef def = codeList.apiCodeInfoList.get(i);
							Logg.d("def : " + def.GROUP_CODE_ID + " / " + def.CODE_ID + " / " + def.CODE_NAME);
							
							ManageCodeLog log = new ManageCodeLog();
							log.code_type = def.GROUP_CODE_ID;
							log.code_key= def.CODE_ID;
							log.code_value=def.CODE_NAME;
							logs.add(log);
							
							Logg.d("def 22: " + log.code_key);
						}
						
						ManageCodeDao manageCodeDao = ManageCodeDao.getInstance(ctx);
						manageCodeDao.Append(logs);
					
						db.setTransactionSuccessful();
					} catch ( Exception e) {
						e.printStackTrace();
					} finally {
						Logg.d("endTransaction");
						db.endTransaction();
					}

					isComplete = true;
				}
			}

			@Override
			public void onPost() {
				
				if (isCancel)
					return;
				
				if (isComplete) {
					mHandler.sendEmptyMessage(TAG_CALL_API_TRACKS_LIST);
				} else {
					mHandler.sendEmptyMessage(TAG_END_SYNC);
				}
				
			}
		});
	}
	
	public void showDialog(String msg) {
		if (pd != null)
			pd.dismiss();
		
		pd = new ProgressDialog(ctx);
		pd.setMessage(msg);
		pd.setCancelable(true);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				cancelSync();
			}
		});
		
		pd.setCanceledOnTouchOutside(false);
		pd.show();
	}
	
	public void dismissDialog() {
		if (pd != null)
			pd.dismiss();
		
		pd = null;
	}
	
	public void setTracksResource() {
		ManageCodeDao manageCodeDao = ManageCodeDao.getInstance(ctx);
		manageCodeDao.selectCodeItems(ConstVALUE.CODE_TYPE_TRACKS);
	}
	
	public void getScheduleList() {
		final boolean isFirst = Shared.getBoolean(ctx, ConstSP.FIRST_START_APP, true);
		// isLogin 이 false이면 수동
		final boolean getFirst = isFirst || !isLogin; 
		final String insPlanNo = UserInfo.getSessionData(ctx).contract;
		
		ATask.executeVoidPublishProgress( new ATask.OnTaskPublishProgress() {
			
			@Override
			public void onProgress(int progress) {
			}
			
			@Override
			public void onPre() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onBG(OnPublishProgress onPublish) {
				scheduleList = ApiManager.scheduleList(getFirst, insPlanNo);
				
				if (scheduleList != null && scheduleList.schedule != null && scheduleList.schedule.size() > 0) {
					
					List<ScheduleInfo> schList = scheduleList.schedule;
					
					InspectResultMasterDao inspectResultMasterDao = InspectResultMasterDao.getInstance(ctx);
					
					SQLiteDatabase db = DBHelper.getInstance(ctx);
					try {
						db.beginTransaction();
						
						for (int i=0; i<schList.size(); i++) {
							ScheduleInfo schInfo = schList.get(i);
							
							// temp
	//						if ( i==0 || i == 10) {
	//							schInfo.INS_TY_CD = "026";
	//							schInfo.NM = "점퍼접속개소";
	//						}
							
							if (ConstVALUE.CODE_NO_INSPECT_HK.equals(schInfo.INS_TY_CD) || ConstVALUE.CODE_NO_INSPECT_HJ.equals(schInfo.INS_TY_CD)) {
								if (schInfo.INS_TY_CD_COUNT == 0)
									continue;
							}
							
							InspectInfo insInfo = new InspectInfo();
							insInfo.tracksName = schInfo.FNCT_LC_DTLS;
							insInfo.towerNo = schInfo.TOWER_IDX;
							insInfo.type = schInfo.INS_TY_CD;
							insInfo.typeName = schInfo.NM;
							insInfo.date = schInfo.CYCLE_YM;
							insInfo.latitude = schInfo.LATITUDE;
							insInfo.longitude = schInfo.LONGITUDE;
							insInfo.eqpNm = schInfo.EQP_NM;
							insInfo.eqpNo = schInfo.EQP_NO;
							insInfo.unity_ins_no = schInfo.UNITY_INS_NO;
							insInfo.ins_plan_no = schInfo.INS_PLAN_NO;
							insInfo.ins_sn = schInfo.INS_SN;
							insInfo.fnct_lc_no = schInfo.FNCT_LC_NO;
							insInfo.address = schInfo.ADDRESS;
							
							Logg.d("sch info  : " + schInfo.FNCT_LC_DTLS + " / " + schInfo.TOWER_IDX + " / " + schInfo.CYCLE_YM + " / " + schInfo.INS_TY_CD + " / " + schInfo.INS_PLAN_NO);
							
	//						boolean exist = inspectResultMasterDao.existIns(insInfo.tracksName, insInfo.towerNo, insInfo.type, insInfo.date);
							InspectInfo info = inspectResultMasterDao.selectIns(insInfo.ins_plan_no, insInfo.tracksName, insInfo.towerNo, insInfo.type, insInfo.date);
							
							Logg.d("sch exist : " + info);
							if (info == null) {
								inspectResultMasterDao.Append(insInfo);
							} else {
	//							insInfo.master_idx = info.master_idx;
	//							inspectResultMasterDao.Append(insInfo);
							}
						}
						
						if (!isFirst)
							Shared.set(ctx, ConstSP.CONTRACT, scheduleList.planno);
						
						db.setTransactionSuccessful();
					} catch (Exception e){
						e.printStackTrace();
					} finally {
						db.endTransaction();
					}
				}
				
			}
				
			@Override
			public void onPost() {
				if (isCancel)
					return;
				
				isComplete = false;
				if (scheduleList != null && ApiManager.RESULT_OK.equals(scheduleList.code) ) {
					isComplete = true;
					if (isLogin && (scheduleList.schedule == null || scheduleList.schedule.isEmpty())) {
						CodeInfo.getInstance(ctx).setCodeResource();
						mHandler.sendEmptyMessage(TAG_END_SYNC);
					} else {
						mHandler.sendEmptyMessage(TAG_CALL_API_CODE_LIST);
					}
					
				} else {
					mHandler.sendEmptyMessage(TAG_END_SYNC);
				}
			}
			
		});
	}
	
	public void getTowerList() {

		ATask.executeVoidPublishProgress(new ATask.OnTaskPublishProgress() {
			public void onPre() {
			}
	
			@Override
			public void onBG(OnPublishProgress onPublish) {
				resultTowerList = ApiManager.facilityList("", "");
				isComplete = false;
				
				if (resultTowerList != null && resultTowerList.searchedTransTower != null && ApiManager.RESULT_OK.equals(resultTowerList.code)) {
					
					SQLiteDatabase db = DBHelper.getInstance(ctx);
					try {
						db.beginTransaction();
						
						TowerListDao.getInstance(ctx).DeleteAll();
						
						List<FacilityInfo> facList = resultTowerList.searchedTransTower;
						
						for (int i=0; i<facList.size(); i++) {
							FacilityInfo facInfo = facList.get(i);
							
							TowerListDao towerListDao = TowerListDao.getInstance(ctx);
							boolean exist = towerListDao.existTower(facInfo);
							
							if (!exist) {
								towerListDao.Append(facInfo);
							}
						}
						
						db.setTransactionSuccessful();
					} catch (Exception e){
						e.printStackTrace();
					} finally {
						db.endTransaction();
					}
					
					isComplete = true;
				}
			}
	
			@Override
			public void onPost() {

				String strDate = new SimpleDateFormat("yyyy-MM-dd a hh:mm").format(System.currentTimeMillis());
				Shared.set(ctx, ConstSP.SYNC_DATE, strDate);
				
				pd.setProgress((int) ((7 / (float) 7) * 100));
				mHandler.sendEmptyMessage(TAG_END_SYNC);
				
			}

			@Override
			public void onProgress(int progress) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void getJSInfoSubList() {
		
		ATask.executeVoidPublishProgress(new ATask.OnTaskPublishProgress() {
			public void onPre() {
			}
	
			@Override
			public void onBG(OnPublishProgress onPublish) {
				jsSubInfoList = ApiManager.jsInfoSubList();
				
				isComplete = false;
				
				if (jsSubInfoList != null && jsSubInfoList.result != null && ApiManager.RESULT_OK.equals(jsSubInfoList.code)) {
					
					SQLiteDatabase db = DBHelper.getInstance(ctx);
					try {
						db.beginTransaction();
						InputJSSubInfoDao.getInstance(ctx).DeleteAll();
						
						List<JSSubInfo> subList = jsSubInfoList.result;
						for (int i=0; i<subList.size(); i++) {
							JSSubInfo subInfo = subList.get(i);
							InputJSSubInfoDao inputJSSubInfoDao = InputJSSubInfoDao.getInstance(ctx);
							inputJSSubInfoDao.Append(subInfo);
						}
						
						db.setTransactionSuccessful();
					} catch (Exception e){
						e.printStackTrace();
					} finally {
						db.endTransaction();
					}
					
					isComplete = true;
				}
			}
	
			@Override
			public void onPost() {
				if (isCancel)
					return;
				
				if (isComplete) {
					//mHandler.sendEmptyMessage(TAG_CALL_API_BRINFO_SUBLIST);
					mHandler.sendEmptyMessage(TAG_CALL_API_TOWER_LIST);
				} else {
					mHandler.sendEmptyMessage(TAG_END_SYNC);
				}
			}

			@Override
			public void onProgress(int progress) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void getBRInfoSubList() {
		
		ATask.executeVoidPublishProgress(new ATask.OnTaskPublishProgress() {
			public void onPre() {
			}
	
			@Override
			public void onBG(OnPublishProgress onPublish) {
				brSubInfoList = ApiManager.brInfoSubList();
				
				isComplete = false;
				
				if (brSubInfoList != null && brSubInfoList.result != null && ApiManager.RESULT_OK.equals(brSubInfoList.code)) {
					
					SQLiteDatabase db = DBHelper.getInstance(ctx);
					try {
						db.beginTransaction();
						InputBRSubInfoDao.getInstance(ctx).DeleteAll();
						
						List<BRSubInfo> subList = brSubInfoList.result;
						for (int i=0; i<subList.size(); i++) {
							BRSubInfo subInfo = subList.get(i);
							InputBRSubInfoDao inputBRSubInfoDao = InputBRSubInfoDao.getInstance(ctx);
							inputBRSubInfoDao.Append(subInfo);
						}
						
						db.setTransactionSuccessful();
					} catch (Exception e){
						e.printStackTrace();
					} finally {
						db.endTransaction();
					}
					
					isComplete = true;
				}
			}
	
			@Override
			public void onPost() {
				if (isCancel)
					return;
				
				if (isComplete) {
					//mHandler.sendEmptyMessage(TAG_CALL_API_HKINFO_SUBLIST);
				} else {
					mHandler.sendEmptyMessage(TAG_END_SYNC);
				}
			}

			@Override
			public void onProgress(int progress) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void getHKInfoSubList() {
		
		ATask.executeVoidPublishProgress(new ATask.OnTaskPublishProgress() {
			public void onPre() {
			}
	
			@Override
			public void onBG(OnPublishProgress onPublish) {
				hkSubInfoList = ApiManager.hkInfoSubList();
				
				isComplete = false;
				
				if (hkSubInfoList != null && hkSubInfoList.result != null && ApiManager.RESULT_OK.equals(hkSubInfoList.code)) {
					
					SQLiteDatabase db = DBHelper.getInstance(ctx);
					try {
						db.beginTransaction();
						InputHKSubInfoDao.getInstance(ctx).DeleteAll();
						
						List<HKSubInfo> subList = hkSubInfoList.result;
						for (int i=0; i<subList.size(); i++) {
							HKSubInfo subInfo = subList.get(i);
							InputHKSubInfoDao inputHKSubInfoDao = InputHKSubInfoDao.getInstance(ctx);
							inputHKSubInfoDao.Append(subInfo);
						}
						
						db.setTransactionSuccessful();
					} catch (Exception e){
						e.printStackTrace();
					} finally {
						db.endTransaction();
					}
					
					isComplete = true;
				}
			}
	
			@Override
			public void onPost() {
				String strDate = new SimpleDateFormat("yyyy-MM-dd a hh:mm").format(System.currentTimeMillis());
				Shared.set(ctx, ConstSP.SYNC_DATE, strDate);
				
				pd.setProgress((int) ((7 / (float) 7) * 100));
				mHandler.sendEmptyMessage(TAG_END_SYNC);
			}

			@Override
			public void onProgress(int progress) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	/*정기순시_유압리스트*/
	public void getJGUInfoSubList() {

		ATask.executeVoidPublishProgress(new ATask.OnTaskPublishProgress() {
			public void onPre() {
			}

			@Override
			public void onBG(OnPublishProgress onPublish) {
				jguSubInfoList = ApiManager.jguInfoSubList();

				isComplete = false;

				if (jguSubInfoList != null && jguSubInfoList.result != null && ApiManager.RESULT_OK.equals(jguSubInfoList.code)) {

					SQLiteDatabase db = DBHelper.getInstance(ctx);
					try {
						db.beginTransaction();
						InputJGUSubInfoDao.getInstance(ctx).DeleteAll();

						List<JGUSubInfo> subList = jguSubInfoList.result;
						for (int i=0; i<subList.size(); i++) {
							JGUSubInfo subInfo = subList.get(i);
							InputJGUSubInfoDao inputJGUSubInfoDao = InputJGUSubInfoDao.getInstance(ctx);
							inputJGUSubInfoDao.Append(subInfo);
						}

						db.setTransactionSuccessful();
					} catch (Exception e){
						e.printStackTrace();
					} finally {
						db.endTransaction();
					}

					isComplete = true;
				}
			}

			@Override
			public void onPost() {
				if (isCancel)
					return;

				if (isComplete) {
					//mHandler.sendEmptyMessage(TAG_CALL_API_BRINFO_SUBLIST);
					mHandler.sendEmptyMessage(TAG_CALL_API_GHINFO_SUBLIST);
				} else {
					mHandler.sendEmptyMessage(TAG_END_SYNC);
				}
			}

			@Override
			public void onProgress(int progress) {
				// TODO Auto-generated method stub

			}

		});
	}
	/*경보회로점검_점검리스트*/
	public void getGHInfoSubList() {

		ATask.executeVoidPublishProgress(new ATask.OnTaskPublishProgress() {
			public void onPre() {
			}

			@Override
			public void onBG(OnPublishProgress onPublish) {
				ghSubInfoList = ApiManager.ghInfoSubList();

				isComplete = false;

				if (ghSubInfoList != null && ghSubInfoList.result != null && ApiManager.RESULT_OK.equals(ghSubInfoList.code)) {

					SQLiteDatabase db = DBHelper.getInstance(ctx);
					try {
						db.beginTransaction();
						InputGHSubInfoDao.getInstance(ctx).DeleteAll();

						List<GHSubInfo> subList = ghSubInfoList.result;
						for (int i=0; i<subList.size(); i++) {
							GHSubInfo subInfo = subList.get(i);
							InputGHSubInfoDao inputGHSubInfoDao = InputGHSubInfoDao.getInstance(ctx);
							inputGHSubInfoDao.Append(subInfo);
						}

						db.setTransactionSuccessful();
					} catch (Exception e){
						e.printStackTrace();
					} finally {
						db.endTransaction();
					}

					isComplete = true;
				}
			}

			@Override
			public void onPost() {
				if (isCancel)
					return;

				if (isComplete) {
					//mHandler.sendEmptyMessage(TAG_CALL_API_BRINFO_SUBLIST);
					mHandler.sendEmptyMessage(TAG_CALL_API_TOWER_LIST);
				} else {
					mHandler.sendEmptyMessage(TAG_END_SYNC);
				}
			}

			@Override
			public void onProgress(int progress) {
				// TODO Auto-generated method stub

			}

		});
	}

	public void apiSendInspectResult(final ArrayList<InspectOutput> outputList) {
		ATask.executeVoidPublishProgress(new ATask.OnTaskPublishProgress() {
			public void onPre() {
			}
	
			@Override
			public void onBG(OnPublishProgress onPublish) {
				InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(ctx);
				SQLiteDatabase db = DBHelper.getInstance(ctx);
				
				try {
					for(int i=0; i<outputList.size(); i++) {
						
						pd.setProgress((int) ((i / (float) outputList.size()) * 100));
						
						InspectInfo inspectInfo = outputList.get(i).inspectInfo;
						
			        	// 보통순시 데이터
			        	BTInfo btInfo = outputList.get(i).btInfo;
			        	if (btInfo != null) {
			        		BaseResult baseResult = ApiManager.apiInputBT(ctx, inspectInfo, btInfo);
			        		
			        		if (baseResult != null && ApiManager.RESULT_OK.equals(baseResult.code)) {
//					        	InputBTDao inputBTDao = InputBTDao.getInstance(ctx);
//								inputBTDao.Delete(inspectInfo.master_idx);
//								insRetDao.updateComplete(inspectInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_BT);
			        			insRetDao.updateSendComplete(inspectInfo.master_idx, inspectInfo.type);
			        		}
			        		continue;
			        	}
			        	
			        	KBInfo kbInfo = outputList.get(i).kbInfo;
			        	if (kbInfo != null) {
			        		BaseResult baseResult = ApiManager.apiInputKB(ctx, inspectInfo, kbInfo);
			        		
			        		if (baseResult != null && ApiManager.RESULT_OK.equals(baseResult.code)) {
//					        	InputKBDao inputKBDao = InputKBDao.getInstance(ctx);
//								inputKBDao.Delete(inspectInfo.master_idx);
//								insRetDao.updateComplete(inspectInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_KB);
			        			insRetDao.updateSendComplete(inspectInfo.master_idx, inspectInfo.type);
			        		}
			        		continue;
			        	}
			        	
			        	JJInfo jjInfo = outputList.get(i).jjInfo;
			        	if (jjInfo != null) {
			        		BaseResult baseResult = ApiManager.apiInputJJ(ctx, inspectInfo, jjInfo);
			        		if (baseResult != null && ApiManager.RESULT_OK.equals(baseResult.code)) {
//					        	InputJJDao inputJJDao = InputJJDao.getInstance(ctx);
//								inputJJDao.Delete(inspectInfo.master_idx);
//								insRetDao.updateComplete(inspectInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_JJ);
			        			insRetDao.updateSendComplete(inspectInfo.master_idx, inspectInfo.type);
			        		}
			        		continue;
			        	}
			        	
			        	ArrayList<JSInfo> jsInfoList = outputList.get(i).jsInfoList;
			        	if (jsInfoList != null && !jsInfoList.isEmpty()) {
			        		boolean isSuccess = true;
			        		
			        		try {
				        		db.beginTransaction();
				        		for (int idx=0; idx<jsInfoList.size(); idx++) {
				        			BaseResult baseResult = ApiManager.apiInputJS(ctx, inspectInfo, jsInfoList.get(idx));
					        		
				        			if (baseResult != null && ApiManager.RESULT_OK.equals(baseResult.code)) {
//							        	InputJSDao.getInstance(ctx).DeleteIdx(jsInfoList.get(idx).idx);
					        		} else {
					        			isSuccess = false;
					        		}
				        		}
				        		
				        		if (isSuccess) {
//			        				insRetDao.updateComplete(inspectInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_JS);
				        			insRetDao.updateSendComplete(inspectInfo.master_idx, inspectInfo.type);
			        				db.setTransactionSuccessful();
				        		}
			        		} catch (Exception e) {
			        			e.printStackTrace();
			        			throw new Exception();
			        		} finally {
			        			db.endTransaction();
			        		}
			        		
			        		continue;
			        	}
			        	
			        	ArrayList<JSInfo> jpInfoList = outputList.get(i).jpInfoList;
			        	if (jpInfoList != null && !jpInfoList.isEmpty()) {
			        		boolean isSuccess = true;
			        		
			        		try {
				        		db.beginTransaction();
				        		for (int idx=0; idx<jpInfoList.size(); idx++) {
				        			BaseResult baseResult = ApiManager.apiInputJP(ctx, inspectInfo, jpInfoList.get(idx));
					        		
				        			if (baseResult != null && ApiManager.RESULT_OK.equals(baseResult.code)) {
//							        	InputJPDao.getInstance(ctx).DeleteIdx(jpInfoList.get(idx).idx);
					        		} else {
					        			isSuccess = false;
					        		}
				        		}
				        		
				        		if (isSuccess) {
//			        				insRetDao.updateComplete(inspectInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_JP);
				        			insRetDao.updateSendComplete(inspectInfo.master_idx, inspectInfo.type);
			        				db.setTransactionSuccessful();
				        		}
			        		} catch (Exception e) {
			        			e.printStackTrace();
			        		} finally {
			        			db.endTransaction();
			        		}
			        		
			        		continue;
			        	}
			        	
			        	ArrayList<HKInfo> hkInfoList = outputList.get(i).hkInfoList;
			        	if (hkInfoList != null && !hkInfoList.isEmpty()) {
			        		boolean isSuccess = true;
			        		
			        		try {
				        		db.beginTransaction();
				        		for (int idx=0; idx<hkInfoList.size(); idx++) {
				        			BaseResult baseResult = ApiManager.apiInputHK(ctx, inspectInfo, hkInfoList.get(idx));
					        		
				        			if (baseResult != null && ApiManager.RESULT_OK.equals(baseResult.code)) {
//				        				InputHKDao.getInstance(ctx).DeleteIdx(hkInfoList.get(idx).idx);
					        		} else {
					        			isSuccess = false;
					        		}
				        		}
				        		
				        		if (isSuccess) {
//			        				insRetDao.updateComplete(inspectInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_HK);
				        			insRetDao.updateSendComplete(inspectInfo.master_idx, inspectInfo.type);
			        				db.setTransactionSuccessful();
				        		}
			        		} catch (Exception e) {
			        			e.printStackTrace();
			        		} finally {
			        			db.endTransaction();
			        		}
			        		
			        		continue;
			        	}
			        	
			        	ArrayList<HJInfo> hjInfoList = outputList.get(i).hjInfoList;
			        	if (hjInfoList != null && !hjInfoList.isEmpty()) {
			        		boolean isSuccess = true;
			        		
			        		try {
			        			db.beginTransaction();
				        		for (int idx=0; idx<hjInfoList.size(); idx++) {
				        			BaseResult baseResult = ApiManager.apiInputHJ(ctx, inspectInfo, hjInfoList.get(idx));
					        		
				        			if (baseResult != null && ApiManager.RESULT_OK.equals(baseResult.code)) {
//				        				InputHJDao.getInstance(ctx).DeleteIdx(hjInfoList.get(idx).idx);
					        		} else {
					        			isSuccess = false;
					        		}
				        		}
				        		
				        		if (isSuccess) {
//			        				insRetDao.updateComplete(inspectInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_HJ);
				        			insRetDao.updateSendComplete(inspectInfo.master_idx, inspectInfo.type);
			        				db.setTransactionSuccessful();
				        		}
			        		} catch (Exception e) {
			        			e.printStackTrace();
			        		} finally {
			        			db.endTransaction();
			        		}
			        		
			        		continue;
			        	}
			        	
			        	ArrayList<BRInfo> brInfoList = outputList.get(i).brInfoList;
			        	if (brInfoList != null && !brInfoList.isEmpty()) {
			        		boolean isSuccess = true;
			        		
			        		try {
				        		db.beginTransaction();
				        		for (int idx=0; idx<brInfoList.size(); idx++) {
				        			BaseResult baseResult = ApiManager.apiInputBR(ctx, inspectInfo, brInfoList.get(idx));
					        		
				        			if (baseResult != null && ApiManager.RESULT_OK.equals(baseResult.code)) {
//				        				InputBRDao.getInstance(ctx).DeleteIdx(brInfoList.get(idx).idx);
					        		} else {
					        			isSuccess = false;
					        		}
				        		}
				        		
				        		if (isSuccess) {
//			        				insRetDao.updateComplete(inspectInfo.master_idx, "N", ConstVALUE.CODE_NO_INSPECT_BR);
				        			insRetDao.updateSendComplete(inspectInfo.master_idx, inspectInfo.type);
			        				db.setTransactionSuccessful();
				        		}
			        		} catch (Exception e) {
			        			e.printStackTrace();
			        		} finally {
			        			db.endTransaction();
			        		}
			        		
			        		continue;
			        	}
					}
					isComplete = true;
				} catch ( Exception e) {
					e.printStackTrace();
					isComplete = false;
				}
			}
	
			@Override
			public void onPost() {
//				isComplete = false;
//				if (resultTowerList != null && ApiManager.RESULT_OK.equals(resultTowerList.code))
				
				mHandler.sendEmptyMessage(TAG_END_SYNC);
			}

			@Override
			public void onProgress(int progress) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
}
