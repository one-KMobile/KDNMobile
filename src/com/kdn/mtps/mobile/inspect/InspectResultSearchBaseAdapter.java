package com.kdn.mtps.mobile.inspect;

import java.util.ArrayList;

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
import com.kdn.mtps.mobile.util.AppUtil;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.StringUtil;
import com.kdn.mtps.mobile.util.UIUtil;

public class InspectResultSearchBaseAdapter extends BaseAdapter{

	private LayoutInflater inflater = null;
	private ArrayList<InspectInfo> mData = new ArrayList<InspectInfo>();
	private Context mContext;
	public static final int NOT_SELECTED = -1;
    private int selectedPos = -1;
    
	public InspectResultSearchBaseAdapter(Context context) {
		mContext = context;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData.clear();
	}
	
	public ArrayList<InspectInfo> getList() {
		return mData;
	}
	
	public void setListClear() {
		mData.clear();
	}
	
	public void setList(ArrayList<InspectInfo> list) {
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
			convertView = inflater.inflate(R.layout.listview_inspect_result_item_, null);
			
			viewHolder = new ViewHolder();
			viewHolder.viewGroup = (RelativeLayout)convertView.findViewById(R.id.viewGroup);
//			viewHolder.tvTracksName = (TextView)convertView.findViewById(R.id.tvTracksName);
			viewHolder.tvTowerNo = (TextView)convertView.findViewById(R.id.tvTowerNo);
			viewHolder.tvDate = (TextView)convertView.findViewById(R.id.tvDate);
			viewHolder.linearInsType = (LinearLayout)convertView.findViewById(R.id.linearInsType);
			viewHolder.linearInsType2 = (LinearLayout)convertView.findViewById(R.id.linearInsType2);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
        }
		
		UIUtil.setFont(mContext, (ViewGroup)viewHolder.viewGroup);
		
		Logg.d("position : " + position + " / " + selectedPos);
		if (position == selectedPos) {
            //your color for selected item
			convertView.setBackgroundColor(Color.parseColor("#ff0000"));
        } else {
            //your color for non-selected item
        	convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

		
		InspectInfo info = mData.get(position);
//		viewHolder.tvTowerNo.setText(info.towerNo);
		viewHolder.tvTowerNo.setText(info.eqpNm); // updatee
		viewHolder.tvDate.setText(StringUtil.printDate(info.date));
		
//		String insType = CodeInfo.getInstance(mContext).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, info.type);
//		viewHolder.tvType.setText(insType == null ? info.type : insType);
		
		addInsTypeView(info, viewHolder);
		
		viewHolder.inspectInfo = info;
		viewHolder.position = position;
		
//		convertView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ViewHolder viewHolder = (ViewHolder)v.getTag();
//				Intent it = new Intent(mContext, InspectResultDetailActivity.class);
//				it.putExtra("inspect", viewHolder.inspectInfo);
//				it.putExtra("currentDate", viewHolder.inspectInfo.date);
//				AppUtil.startActivity(mContext, it);
//			
//			}
//		});
		
		
		return convertView;
	}
	
	public void click(View v) {
		ViewHolder viewHolder = (ViewHolder)v.getTag();
		Intent it = new Intent(mContext, InspectResultDetailActivity.class);
		it.putExtra("inspect", viewHolder.inspectInfo);
		it.putExtra("currentDate", viewHolder.inspectInfo.date);
		((Activity)mContext).startActivityForResult(it, ConstVALUE.REQUEST_CODE_INSPECT_SEARCH);
	}

	public class ViewHolder {
		public RelativeLayout viewGroup = null;
		public TextView tvTracksName = null;
		public TextView tvTowerNo = null;
		public TextView tvDate = null;
		public LinearLayout linearInsType = null;
		public LinearLayout linearInsType2 = null;
//		public TextView tvType = null;
		public InspectInfo inspectInfo = null;
		public int position;
	}
	
	public void goInputActivity(Class<?> cls, InspectInfo info) {
		Intent intent = new Intent(mContext, cls);
		intent.putExtra("inspect", info);
		mContext.startActivity(intent);
	}
	
	public void addInsTypeView(InspectInfo info, ViewHolder viewHolder) {
		viewHolder.linearInsType.removeAllViews();
		viewHolder.linearInsType2.removeAllViews();
		
		String[] hasInput = {info.has_bt, info.has_jg, info.has_yb, info.has_mh, info.has_gh, info.has_pr, info.has_jb};
		for (int idx=0; idx<hasInput.length; idx++) {
			String has = hasInput[idx];
			
			if ("Y".equalsIgnoreCase(has)) {
				TextView tv = new TextView(mContext);
				if (idx == 0) {
					if ("Y".equalsIgnoreCase(info.send_yn_bt))
						tv.setBackgroundResource(R.drawable.btn_input_bt_s);
					else if ("Y".equalsIgnoreCase(info.complete_yn_bt))
						tv.setBackgroundResource(R.drawable.btn_input_bt_y);
					else
						tv.setBackgroundResource(R.drawable.btn_input_bt_n);
				} else if (idx == 1) {
					if ("Y".equalsIgnoreCase(info.send_yn_jg))
						tv.setBackgroundResource(R.drawable.btn_input_jg_s);
					else if ("Y".equalsIgnoreCase(info.complete_yn_js))
						tv.setBackgroundResource(R.drawable.btn_input_jg_y);
					else
						tv.setBackgroundResource(R.drawable.btn_input_jg_n);
				} else if (idx == 2) {
					if ("Y".equalsIgnoreCase(info.send_yn_yb))
						tv.setBackgroundResource(R.drawable.btn_input_yb_s);
					else if ("Y".equalsIgnoreCase(info.complete_yn_jj))
						tv.setBackgroundResource(R.drawable.btn_input_yb_y);
					else
						tv.setBackgroundResource(R.drawable.btn_input_yb_n);
				} else if (idx == 3) {
					if ("Y".equalsIgnoreCase(info.send_yn_mh))
						tv.setBackgroundResource(R.drawable.btn_input_mh_s);
					else if ("Y".equalsIgnoreCase(info.complete_yn_kb))
						tv.setBackgroundResource(R.drawable.btn_input_mh_y);
					else
						tv.setBackgroundResource(R.drawable.btn_input_mh_n);
				} else if (idx == 4) {
					if ("Y".equalsIgnoreCase(info.send_yn_gh))
						tv.setBackgroundResource(R.drawable.btn_input_gh_s);
					else if ("Y".equalsIgnoreCase(info.complete_yn_hk))
						tv.setBackgroundResource(R.drawable.btn_input_gh_y);
					else
						tv.setBackgroundResource(R.drawable.btn_input_gh_n);
				} else if (idx == 5) {
					if ("Y".equalsIgnoreCase(info.send_yn_pr))
						tv.setBackgroundResource(R.drawable.btn_input_pr_s);
					else if ("Y".equalsIgnoreCase(info.complete_yn_hj))
						tv.setBackgroundResource(R.drawable.btn_input_pr_y);
					else
						tv.setBackgroundResource(R.drawable.btn_input_pr_n);
				} else if (idx == 6) {
					if ("Y".equalsIgnoreCase(info.send_yn_jb))
						tv.setBackgroundResource(R.drawable.btn_input_jb_s);
					else if ("Y".equalsIgnoreCase(info.complete_yn_br))
						tv.setBackgroundResource(R.drawable.btn_input_jb_y);
					else
						tv.setBackgroundResource(R.drawable.btn_input_jb_n);
				}
				
				addTextView(tv, viewHolder);
			}
		}
		
	}
	
	public void addTextView(TextView tv, ViewHolder viewHolder) {
//		TextView tv = new TextView(mContext);
//		tv.setPadding(0, 0, 20, 0);

		if (viewHolder.linearInsType.getChildCount() >= 4)
			viewHolder.linearInsType2.addView(tv);
		else
			viewHolder.linearInsType.addView(tv);
		
	}
	
}