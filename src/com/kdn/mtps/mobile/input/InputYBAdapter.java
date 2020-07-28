package com.kdn.mtps.mobile.input;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.constant.ConstSP;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.util.Shared;
import com.kdn.mtps.mobile.util.StringUtil;

public class InputYBAdapter extends BaseAdapter{

	private LayoutInflater inflater = null;
	private ArrayList<YBInfo> mData = new ArrayList<YBInfo>();
	private Context mContext;
	
	final String strInspectResultItems[] = CodeInfo.getInstance(mContext).getNames(ConstVALUE.CODE_TYPE_GOOD_SECD);
	
	public InputYBAdapter(Context context) {
		mContext = context;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData.clear();
	}
	
	public ArrayList<YBInfo> getList() {
		return mData;
	}
	
	public void setListClear() {
		mData.clear();
	}
	
	public void setList(List<YBInfo> preventingInspection) {
		mData.clear();
		
		mData.addAll(preventingInspection);
		
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
			
			convertView = inflater.inflate(R.layout.listview_input_yb_item_, null);
			
			viewHolder = new ViewHolder();
//			viewHolder.layoutHeader = (LinearLayout)convertView.findViewById(R.id.layout_header);
			viewHolder.tvNo = (TextView)convertView.findViewById(R.id.tvNo);
			viewHolder.tvTowerNo1 = (TextView)convertView.findViewById(R.id.tvTowerNo1);
			viewHolder.tvTowerNo2 = (TextView)convertView.findViewById(R.id.tvTowerNo2);
			viewHolder.tvPlaceName = (TextView)convertView.findViewById(R.id.tvPlaceName);
			viewHolder.btnCheckResult = (Button)convertView.findViewById(R.id.btnCheckResult);
			viewHolder.editContent = (EditText)convertView.findViewById(R.id.editContent);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
        }
		
		YBInfo info = mData.get(position);
		viewHolder.ybInfo = info;
		
		if (position != 0)
			viewHolder.layoutHeader.setVisibility(View.GONE);
			
		viewHolder.tvNo.setText((position+1)+"");
		viewHolder.tvTowerNo1.setText(info.BEGIN_EQP_NM);
		viewHolder.tvTowerNo2.setText(info.END_EQP_NM);
		viewHolder.tvPlaceName.setText(info.CWRK_NM);
		
		String strResultSecd = CodeInfo.getInstance(mContext).getValue(ConstVALUE.CODE_TYPE_GOOD_SECD, info.TINS_RESULT_SECD);
		viewHolder.btnCheckResult.setText(strResultSecd);
		viewHolder.btnCheckResult.setId(StringUtil.getIndex(strInspectResultItems, strResultSecd));
		
		viewHolder.editContent.setText(info.TINS_RESULT);
		
		viewHolder.btnCheckResult.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showInspectResultDialog((Button)v);
			}
		});
		
		return convertView;
	}

	public class ViewHolder {
		LinearLayout layoutHeader;
		TextView tvNo;
		TextView tvTowerNo1;
		TextView tvTowerNo2;
        TextView tvPlaceName;
        Button btnCheckResult;
        EditText editContent;
        
		public YBInfo ybInfo = null;
	}
	
	public void showInspectResultDialog(final Button btn) {
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		dialog.setTitle(mContext.getString(R.string.input_bt_weather));
		dialog.setSingleChoiceItems(strInspectResultItems, btn.getId(), new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				btn.setId(which);
				btn.setText(strInspectResultItems[which]);
                dialog.dismiss();
			}
		}).show();
		
	}
}