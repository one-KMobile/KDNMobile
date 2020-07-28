package com.kdn.mtps.mobile.input;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.db.CompanyListDao;
import com.kdn.mtps.mobile.util.UIUtil;

public class CompanyListBaseAdapter extends BaseAdapter{

	private LayoutInflater inflater = null;
	private List<String> mData = new ArrayList<String>();
	private Context mContext;
	private EditText editCompany;
	private AlertDialog mDialog;
	
	public CompanyListBaseAdapter(Context context, EditText editText, AlertDialog dialog) {
		mContext = context;
		editCompany = editText;
		mDialog = dialog;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData.clear();
	}
	
	public List<String> getList() {
		return mData;
	}
	
	public void setListClear() {
		mData.clear();
	}
	
	public void setList(List<String> list) {
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
			convertView = inflater.inflate(R.layout.input_br_make_company_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tvCompany = (TextView)convertView.findViewById(R.id.tvCompany);
			viewHolder.ivDelete = (ImageView)convertView.findViewById(R.id.ivDelete);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
        }
		
		if (position == 0) {
			viewHolder.ivDelete.setVisibility(View.GONE);
		}
		
		String info = mData.get(position);
//		viewHolder.tvName.setText(info.FNCT_LC_DTLS);
		viewHolder.tvCompany.setText(info);
		convertView.setTag(viewHolder);
		viewHolder.ivDelete.setTag(info);
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewHolder viewHolder = (ViewHolder)v.getTag();
				String strCompany = viewHolder.tvCompany.getText().toString();
				
				mDialog.dismiss();
				UIUtil.hideKeyboard(editCompany);
				
				if ("직접입력".equals(strCompany)) {
					return;
				}
				
				editCompany.setText(strCompany);
				
			}
		});
		
		viewHolder.ivDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String strCompany = (String)v.getTag();
				
				CompanyListDao.getInstance(mContext).Delete(strCompany);
				mData = CompanyListDao.getInstance(mContext).selectCompanyList();
				
				if (mData.isEmpty())
					mDialog.dismiss();
				else
					notifyDataSetChanged();
				
			}
		});
		
//		viewHolder.btnLocation.setTag(info);
//		viewHolder.btnLocation.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				FacilityInfo info = (FacilityInfo)v.getTag();
//				
//				if (info.LATITUDE == null || "".equals(info.LATITUDE) || info.LONGITUDE == null || "".equals(info.LONGITUDE)) {
//					ToastUtil.show(mContext, "좌표 정보가 없습니다.");
//					return;
//				}
//				
//				Intent intent = new Intent(mContext, FacilityMapActivity.class);
//				intent.putExtra("facility", info);
//				AppUtil.startActivity(mContext, intent);
//			}
//		});
		
//		viewHolder.facilityInfo = info;
//		convertView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ViewHolder viewHolder = (ViewHolder)v.getTag();
//				
//				Intent intent = new Intent(mContext, FacilityDetailActivity.class);
//				intent.putExtra("facility", viewHolder.facilityInfo);
//				AppUtil.startActivity(mContext, intent);
//			}
//		});
		
		return convertView;
	}

	public class ViewHolder {
		public TextView tvCompany = null;
		public ImageView ivDelete = null;
	}
}