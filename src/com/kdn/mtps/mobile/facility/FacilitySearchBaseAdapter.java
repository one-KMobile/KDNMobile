package com.kdn.mtps.mobile.facility;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.input.InputJoinReportAddActivity;
import com.kdn.mtps.mobile.facility.FacilityInfo;
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
			viewHolder.tvIdx = (TextView)convertView.findViewById(R.id.tvIdx);
			viewHolder.tvFnctLcDtls = (TextView)convertView.findViewById(R.id.tvFnctLcDtls);
			viewHolder.tvEqpTyCdNm = (TextView)convertView.findViewById(R.id.tvEqpTyCdNm);
			viewHolder.tvEqpNm = (TextView)convertView.findViewById(R.id.tvEqpNm);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
        }

		FacilityInfo info = mData.get(position);
//		viewHolder.tvName.setText(info.FNCT_LC_DTLS);
		viewHolder.tvIdx.setText(String.valueOf(info.IDX));
		viewHolder.tvFnctLcDtls.setText(info.FNCT_LC_DTLS);
		viewHolder.tvEqpTyCdNm.setText(info.EQP_TY_CD_NM);
		viewHolder.tvEqpNm.setText(info.EQP_NM);

		viewHolder.facilityInfo = info;
		
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

	public void click(View v) {
		FacilitySearchBaseAdapter.ViewHolder viewHolder = (FacilitySearchBaseAdapter.ViewHolder)v.getTag();
		Intent it = new Intent(mContext, InputJoinReportAddActivity.class);
		it.putExtra("facilityInfo", viewHolder.facilityInfo);
		it.putExtra("eqpTyCdNm", viewHolder.facilityInfo.EQP_TY_CD_NM);
		it.putExtra("idx", String.valueOf(viewHolder.facilityInfo.IDX));
		((Activity)mContext).startActivityForResult(it, ConstVALUE.REQUEST_CODE_INSPECT_SEARCH);
	}

	public class ViewHolder {
		public TextView tvIdx = null;
		public TextView tvFnctLcDtls = null;
		public TextView tvEqpTyCdNm = null;
		public TextView tvEqpNm = null;
		public FacilityInfo facilityInfo = null;
	}
	
}