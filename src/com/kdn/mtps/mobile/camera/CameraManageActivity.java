package com.kdn.mtps.mobile.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.TitleManager;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.db.CameraLogDao;
import com.kdn.mtps.mobile.db.bean.CameraLog;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.util.AlertUtil;
import com.kdn.mtps.mobile.util.ImageUtil;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.MediaScanning;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.StringUtil;
import com.kdn.mtps.mobile.util.ToastUtil;

public class CameraManageActivity extends BaseActivity implements TitleManager, OnClickListener{

	public static final int REQ_PICTURE_PICK = 1;
	
	LinearLayout llParent;
	LinearLayout linearNodate;
	
	TextView tvName;
	TextView tvDate;
	TextView tvEqpNm;
	TextView tvType;
	
	Button btnShoot;
	Button btnDelete;
	
	TextView tvTitle;
//	GridView gridPhoto;
	ListView listPhoto;
	TextView tvNoData;
	InspectInfo mInfo;
	Uri fileuri;
	PhotoListAdapter adapter;
	ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_manage);
		
		setTitle();
		
		setInit();
		
		setData();
	}

	public void setInit() {
		llParent = (LinearLayout)findViewById(R.id.llParent);
		tvName = (TextView)findViewById(R.id.tvName);
		tvDate = (TextView)	findViewById(R.id.tvDate);
		tvEqpNm = (TextView)	findViewById(R.id.tvEqpNm);
		tvType = (TextView)findViewById(R.id.tvType);
		
		btnShoot = (Button)findViewById(R.id.btnShoot);
		btnDelete = (Button)findViewById(R.id.btnDelete);
		tvTitle = (TextView)findViewById(R.id.tvTitle);
		listPhoto = (ListView)findViewById(R.id.listPhoto);
		
		btnShoot.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		adapter = new PhotoListAdapter(this);
		
		tvNoData = (TextView)findViewById(R.id.tvNoData);
		linearNodate = (LinearLayout)findViewById(R.id.linearNodate);
		
		listPhoto.setAdapter(adapter);
	}
	
	public void setData() {
		Intent intent = getIntent();
		mInfo = intent.getParcelableExtra("inspect");
		
		if (ConstVALUE.CODE_NO_INSPECT_YB.equals(mInfo.type))
			llParent.setVisibility(View.GONE);
		
		tvName.setText(mInfo.tracksName);
		tvDate.setText(StringUtil.printDate(mInfo.date));
		tvEqpNm.setText(mInfo.eqpNm);
		
		String insType = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, mInfo.type);
		tvType.setText(insType);
		
		String type = CodeInfo.getInstance(this).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, mInfo.type);
		tvTitle.setText(type + " 사진 목록");
		
		CameraLogDao cameraLogDao = CameraLogDao.getInstance(this);
		ArrayList<CameraLog> list = cameraLogDao.selectPickList(mInfo.master_idx, mInfo.type);
		
		adapter.setList(list);
		
		setTvNoData();
	}
	
	@Override
	public void setTitle() {
		Button btnHeaderTitle = (Button)findViewById(R.id.btnHeaderTitle);
		Button btnHeaderLeft = (Button)findViewById(R.id.btnHeaderLeft);
		Button btnHeaderRight = (Button)findViewById(R.id.btnHeaderRight);
		
		btnHeaderTitle.setText("사진 촬영 및 관리");
		btnHeaderRight.setVisibility(View.GONE);
		
		
		btnHeaderLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.saveData();
				finish();
			}
		});
	}
	
	public void picturePick() {

		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		
		String tgtDir = ConstVALUE.PATH_SDCARD_PICTURE;
		String folderName = mInfo.date + "_" + mInfo.eqpNo + "_" + mInfo.type; 
		
		// 촬영하면 sdcard 에 KDN/순시일_지지물코드_순시종류/이미지파일명 으로 저장함
		
		String fullPath = tgtDir + "/" + folderName;
		File dir = new File(fullPath);// 타켓 폴더 생성
		if (!dir.exists())
			dir.mkdirs();
				
		
		fileuri = Uri.fromFile(new File(fullPath, System.currentTimeMillis() / 1000 + ".png"));
		
		Shared.set(CameraManageActivity.this, "fileUri", fileuri.getPath());
//		intent.setData(fileuri);
//		fileuri = Uri.fromFile(new File(tgtDir, "c_" + System.currentTimeMillis() / 1000 + ".png"));
//		fileuri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "c_" + System.currentTimeMillis() / 1000 + ".png"));

		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileuri);
		intent.putExtra(android.provider.MediaStore.EXTRA_SCREEN_ORIENTATION, 0);
		startActivityForResult(intent, REQ_PICTURE_PICK);
		
//		pd = new ProgressDialog(this);
//		pd.setCancelable(false);
//		pd.setCanceledOnTouchOutside(false);
//		pd.setMessage("이미지 저장중입니다..");
//		pd.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_PICTURE_PICK) {
			
			if (pd != null)
				pd.dismiss();
			
//			fileuri = data.getData();
			
//			if ( fileuri == null)
//				return;
			
			String filePath = Shared.getString(CameraManageActivity.this, "fileUri");
					
//			String filePath = fileuri.getPath();
			
			Logg.d("end picture");
			Logg.d("path : " + filePath);
//			String filePath = data.getData().getPath();
			File file = new File(filePath);
			if (file == null || !file.exists())
				return;
			
			Bitmap bitmap = ImageUtil.getBitmap(filePath, 8);
			
			int orientation = ImageUtil.getImageOrientation(filePath);
			if (orientation != 0)
				bitmap = ImageUtil.getRotatedBitmap(bitmap, orientation);
			
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(new File(filePath));
				bitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			new MediaScanning(CameraManageActivity.this, file);
			
//			Uri contentUri = Uri.fromFile(file);
//			Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//			mediaScanIntent.setData(contentUri);
//			sendBroadcast(mediaScanIntent);
			
//			MediaScannerConnection.scanFile(this, new String[]{filePath}, null,
//					new MediaScannerConnection.OnScanCompletedListener() {
//						public void onScanCompleted(String path, Uri uri) {
//						}
//					});
			
			// 촬영한 사진정보를 DB에 저장한다.
			CameraLog log = new CameraLog();
			log.master_idx = mInfo.master_idx;
			log.img_path = filePath;
			log.ins_type = mInfo.type;
			CameraLogDao cameraLogDao = CameraLogDao.getInstance(this);
			cameraLogDao.Append(log);
			
			ArrayList<CameraLog> list = cameraLogDao.selectPickList(mInfo.master_idx, mInfo.type);
			adapter.setList(list);
			setTvNoData();
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnShoot:
			picturePick();
			break;
		case R.id.btnDelete:
			final List<CameraLog> list = adapter.getList();
			
			int deleteCnt = 0;
			for (CameraLog info : list) {
				if (info.isChecked)
					deleteCnt++;
			}
			
			if (deleteCnt == 0) {
				ToastUtil.show(CameraManageActivity.this, "삭제할 항목을 선택해 주세요.");
				return;
			}
			
			AlertUtil.showNoTitleAlertOK(this, deleteCnt + " 개 항목을 삭제하시겠습니까?", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					CameraLogDao cameraLogDao = CameraLogDao.getInstance(CameraManageActivity.this);
					for (CameraLog info : list) {
						if (info.isChecked) {
							Logg.d("exist file 11111111111");
							File file = new File(info.img_path);
							Logg.d("exist file 22222222222");
							Logg.d("exist file : " + file.exists());
							if (file != null && file.exists())
								file.delete();
							Logg.d("exist file 33333333333333");
							cameraLogDao.delete(info.idx);
							
							new MediaScanning(CameraManageActivity.this, file);
							
//							Uri contentUri = Uri.fromFile(file);
//							Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//							mediaScanIntent.setData(contentUri);
//							sendBroadcast(mediaScanIntent);
						}
					}
					
					ArrayList<CameraLog> list = cameraLogDao.selectPickList(mInfo.master_idx, mInfo.type);
					String folderName = null;
					
					String tgtDir = ConstVALUE.PATH_SDCARD_PICTURE;
					folderName = mInfo.date + "_" + mInfo.eqpNo + "_" + mInfo.type; 
					String fullPath = tgtDir + "/" + folderName;
					
					if (list.isEmpty()) {
						
						File dir = new File(fullPath);
						if (dir.exists())
							dir.delete();
						
					} else {
						new MediaScanning(CameraManageActivity.this, new File(fullPath));
					}
					
					
//					Uri contentUri = Uri.fromFile(new File(fullPath));
//					Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//					mediaScanIntent.setData(contentUri);
//					sendBroadcast(mediaScanIntent);
					
					adapter.setList(list);
					setTvNoData();
				}
			}, null);
			
			break;
		}
	}

	public void setTvNoData() {
		if (adapter.getCount() <= 0 )
			linearNodate.setVisibility(View.VISIBLE);
		else
			linearNodate.setVisibility(View.GONE);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		adapter.saveData();
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		adapter.saveData();
	}
	
}
