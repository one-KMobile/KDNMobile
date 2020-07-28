package com.kdn.mtps.mobile.camera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.db.CameraLogDao;
import com.kdn.mtps.mobile.db.bean.CameraLog;
import com.kdn.mtps.mobile.util.ImageUtil;

public class PhotoListAdapter extends BaseAdapter{

	private LayoutInflater inflater = null;
	private ArrayList<CameraLog> mData = new ArrayList<CameraLog>();
	private Context mContext;
	private int selectedInspectResultNo=0;
	
	public PhotoListAdapter(Context context) {
		mContext = context;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData.clear();
	}
	
	public ArrayList<CameraLog> getList() {
		return mData;
	}
	
	public void setListClear() {
		mData.clear();
	}
	
	public void setList(ArrayList<CameraLog> list) {
		mData.clear();
		
		mData.addAll(list);
		
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_camera_item_, null);
			viewHolder = new ViewHolder();
			viewHolder.ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
			viewHolder.btnSelect = (Button)convertView.findViewById(R.id.btnSelect);
			viewHolder.editSubject = (EditText)convertView.findViewById(R.id.editSubject);
			viewHolder.editContent = (EditText)convertView.findViewById(R.id.editContent);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
        }
		
		final CameraLog info = mData.get(position);
		
		viewHolder.info = info;
		if (info.isChecked)
			viewHolder.btnSelect.setBackgroundResource(R.drawable.photo_check);
		else
			viewHolder.btnSelect.setBackgroundResource(R.drawable.photo_check_box);
		
		Uri uri = Uri.fromFile(new File(info.img_path));
		Bitmap bitmap = ImageUtil.getBitmap(uri.getPath(), 4);
		if (bitmap != null)
			viewHolder.ivPhoto.setImageBitmap(bitmap);
		
		viewHolder.btnSelect.setTag(viewHolder);
		
		viewHolder.btnSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewHolder viewHolder = (ViewHolder)v.getTag();
				viewHolder.info.isChecked = !viewHolder.info.isChecked;
				
				if (info.isChecked)
					((Button)v).setBackgroundResource(R.drawable.photo_check);
				else
					((Button)v).setBackgroundResource(R.drawable.photo_check_box);
				
			}
		});

		if (info.subject == null || "".equals(info.subject)) {
			String strDate = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
			viewHolder.editSubject.setText(strDate);
		} else {
			viewHolder.editSubject.setText(info.subject);
		}
		
		viewHolder.editSubject.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				info.subject = s.toString();
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
		});
		
		viewHolder.editContent.setText(info.content);
		viewHolder.editContent.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				info.content = s.toString();
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
		});
		
		return convertView;
	}

	public class ViewHolder {
		Button btnSelect;
		ImageView ivPhoto;
		EditText editSubject;
		EditText editContent;
        
		public CameraLog info = null;
	}
	
	public void saveData() {
		CameraLogDao cameraLogDao = CameraLogDao.getInstance(mContext);
		
		for (CameraLog log : mData) {
			cameraLogDao.Append(log, log.idx);
		}
	}
}