package com.kdn.mtps.mobile.facility;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kdn.mtps.mobile.BaseActivity;
import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.ToastUtil;
import com.kdn.mtps.mobile.util.UIUtil;

public class FacilitySearchBaseAdapter extends BaseAdapter{

	private LayoutInflater inflater = null;
	private List<FacilityInfo> mData = new ArrayList<FacilityInfo>();
	private Context mContext;
	
	public FacilitySearchBaseAdapter(Context context) {
		mContext = context;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData.clear();
	}
	
	public List<FacilityInfo> getList() {
		return mData;
	}
	
	public void setListClear() {
		mData.clear();
	}
	
	public void setList(List<FacilityInfo> list) {
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
			convertView = inflater.inflate(R.layout.listview_facility_item_, null);
			viewHolder = new ViewHolder();
			viewHolder.viewGroup = (RelativeLayout)convertView.findViewById(R.id.viewGroup);
//			viewHolder.tvName = (TextView)convertView.findViewById(R.id.tvName);
			viewHolder.tvEqpName = (TextView)convertView.findViewById(R.id.tvEqpName);
			viewHolder.tvEqpType = (TextView)convertView.findViewById(R.id.tvEqpType);
			viewHolder.tvCircuitCnt = (TextView)convertView.findViewById(R.id.tvCircuitCnt);
			viewHolder.btnLocation = (Button)convertView.findViewById(R.id.btnLocation);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
        }
		
		UIUtil.setFont(mContext, (ViewGroup)viewHolder.viewGroup);
		
		FacilityInfo info = mData.get(position);
//		viewHolder.tvName.setText(info.FNCT_LC_DTLS);
		viewHolder.tvEqpType.setText(info.EQP_TY_CD_NM);
		viewHolder.tvCircuitCnt.setText(info.CONT_NUM);
		viewHolder.tvEqpName.setText(info.EQP_NM);
		
		viewHolder.btnLocation.setTag(info);
		viewHolder.btnLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FacilityInfo info = (FacilityInfo)v.getTag();
				
				if (info.LATITUDE == null || "".equals(info.LATITUDE) || info.LONGITUDE == null || "".equals(info.LONGITUDE)) {
					ToastUtil.show(mContext, "좌표 정보가 없습니다.");
					return;
				}
				
				Intent intent = new Intent(mContext, FacilityMapActivity.class);
				intent.putExtra("facility", info);
				AppUtil.startActivity(mContext, intent);
			}
		});
		
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
//		public TextView tvName = null;
//		public TextView tvEqpType = null;
		public RelativeLayout viewGroup = null;
		public TextView tvEqpName = null;
		public TextView tvEqpType = null;
		public TextView tvCircuitCnt = null;
		public Button btnLocation = null;
		public FacilityInfo facilityInfo = null;
	}
	
}