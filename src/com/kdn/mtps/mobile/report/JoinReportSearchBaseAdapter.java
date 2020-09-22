package com.kdn.mtps.mobile.report;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.input.InputJoinReportAddActivity;
import com.kdn.mtps.mobile.input.JoinReportInfo;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.StringUtil;
import com.kdn.mtps.mobile.util.UIUtil;

import java.util.ArrayList;

public class JoinReportSearchBaseAdapter extends BaseAdapter{

	private LayoutInflater inflater = null;
	private ArrayList<JoinReportInfo> mData = new ArrayList<JoinReportInfo>();
	private Context mContext;
	public static final int NOT_SELECTED = -1;
    private int selectedPos = -1;

	public JoinReportSearchBaseAdapter(Context context) {
		mContext = context;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData.clear();
	}
	
	public ArrayList<JoinReportInfo> getList() {
		return mData;
	}
	
	public void setListClear() {
		mData.clear();
	}
	
	public void setList(ArrayList<JoinReportInfo> list) {
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
			convertView = inflater.inflate(R.layout.listview_join_report_item_, null);
			
			viewHolder = new ViewHolder();
			viewHolder.tvIdx = (TextView)convertView.findViewById(R.id.tvIdx);
			viewHolder.tvName = (TextView)convertView.findViewById(R.id.tvName);
			viewHolder.tvJoinDate = (TextView)convertView.findViewById(R.id.tvJoinDate);
			viewHolder.tvWorkNm = (TextView)convertView.findViewById(R.id.tvWorkNm);
			viewHolder.tvJoiner = (TextView)convertView.findViewById(R.id.tvJoiner);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
        }
		
		//UIUtil.setFont(mContext, (ViewGroup)viewHolder.viewGroup);
		
		Logg.d("position : " + position + " / " + selectedPos);
		if (position == selectedPos) {
            //your color for selected item
			convertView.setBackgroundColor(Color.parseColor("#ff0000"));
        } else {
            //your color for non-selected item
        	convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

		
		JoinReportInfo info = mData.get(position);
//		viewHolder.tvTowerNo.setText(info.towerNo);
		int newIdx = info.idx + 1;
		String strJoinDate = info.join_date.substring(0,4) + "." + info.join_date.substring(4,6) + "." + info.join_date.substring(6,8);
		viewHolder.tvIdx.setText(String.valueOf(newIdx));
		viewHolder.tvName.setText(info.name);
		viewHolder.tvJoinDate.setText(strJoinDate);
		viewHolder.tvWorkNm.setText(info.work_nm);
		viewHolder.tvJoiner.setText(info.joiner);
		
//		String insType = CodeInfo.getInstance(mContext).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, info.type);
//		viewHolder.tvType.setText(insType == null ? info.type : insType);
		
		//addInsTypeView(info, viewHolder);
		
		viewHolder.JoinReportInfo = info;
		viewHolder.position = position;
		
//		convertView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ViewHolder viewHolder = (ViewHolder)v.getTag();
//				Intent it = new Intent(mContext, InspectResultDetailActivity.class);
//				it.putExtra("inspect", viewHolder.JoinReportInfo);
//				it.putExtra("currentDate", viewHolder.JoinReportInfo.date);
//				AppUtil.startActivity(mContext, it);
//			
//			}
//		});
		
		
		return convertView;
	}
	
	public void click(View v) {
		ViewHolder viewHolder = (ViewHolder)v.getTag();
		Intent it = new Intent(mContext, InputJoinReportAddActivity.class);
		it.putExtra("joinReportInfo", viewHolder.JoinReportInfo);
		it.putExtra("joinDate", viewHolder.JoinReportInfo.join_date);
		it.putExtra("idx", String.valueOf(viewHolder.JoinReportInfo.idx));
		((Activity)mContext).startActivityForResult(it, ConstVALUE.REQUEST_CODE_INSPECT_SEARCH);
	}

	public class ViewHolder {
		public RelativeLayout viewGroup = null;
		public TextView tvTracksName = null;
		public TextView tvIdx = null;
		public TextView tvName = null;
		public TextView tvWorkNm = null;
		public TextView tvJoinDate = null;
		public TextView tvJoiner = null;
		public JoinReportInfo JoinReportInfo = null;
		public int position;
	}
	
	public void goInputActivity(Class<?> cls, JoinReportInfo info) {
		Intent intent = new Intent(mContext, cls);
		intent.putExtra("inspect", info);
		mContext.startActivity(intent);
	}
	
}