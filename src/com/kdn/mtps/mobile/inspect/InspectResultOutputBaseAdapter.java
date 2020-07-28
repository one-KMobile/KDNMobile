package com.kdn.mtps.mobile.inspect;

import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.db.InputBRDao;
import com.kdn.mtps.mobile.db.InputBTDao;
import com.kdn.mtps.mobile.db.InputHJDao;
import com.kdn.mtps.mobile.db.InputHKDao;
import com.kdn.mtps.mobile.db.InputJJDao;
import com.kdn.mtps.mobile.db.InputJPDao;
import com.kdn.mtps.mobile.db.InputJSDao;
import com.kdn.mtps.mobile.db.InputKBDao;
import com.kdn.mtps.mobile.db.InspectResultMasterDao;
import com.kdn.mtps.mobile.info.CodeInfo;
import com.kdn.mtps.mobile.input.InputBRActivity;
import com.kdn.mtps.mobile.input.InputBTActivity;
import com.kdn.mtps.mobile.input.InputHJActivity;
import com.kdn.mtps.mobile.input.InputHKActivity;
import com.kdn.mtps.mobile.input.InputJJActivity;
import com.kdn.mtps.mobile.input.InputJPActivity;
import com.kdn.mtps.mobile.input.InputJSActivity;
import com.kdn.mtps.mobile.input.InputKBActivity;
import com.kdn.mtps.mobile.util.AlertUtil;
import com.kdn.mtps.mobile.util.StringUtil;
import com.kdn.mtps.mobile.util.ToastUtil;

public class InspectResultOutputBaseAdapter extends BaseAdapter{

	private LayoutInflater inflater = null;
	private ArrayList<InspectInfo> mData = new ArrayList<InspectInfo>();
	private Context mContext;
	
	public InspectResultOutputBaseAdapter(Context context) {
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
			
			convertView = inflater.inflate(R.layout.listview_inspect_output_item_, null);
			
			viewHolder = new ViewHolder();
			viewHolder.tvTowerNo = (TextView)convertView.findViewById(R.id.tvTowerNo);
			viewHolder.tvDate = (TextView)convertView.findViewById(R.id.tvDate);
			viewHolder.tvType = (TextView)convertView.findViewById(R.id.tvType);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
        }
		
		InspectInfo info = mData.get(position);
//		viewHolder.tvTowerNo.setText(info.towerNo);
		viewHolder.tvTowerNo.setText(info.eqpNm); // updatee
		viewHolder.tvDate.setText(StringUtil.printDate(info.date));
		
		String insType = CodeInfo.getInstance(mContext).getValue(ConstVALUE.CODE_TYPE_INS_TYPE, info.type);
		viewHolder.tvType.setText(insType == null ? info.type : insType);
		
		viewHolder.inspectInfo = info;
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewHolder viewHolder = (ViewHolder)v.getTag();
				
				
			if (viewHolder.inspectInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_BT)) {
				goInputActivity(InputBTActivity.class, viewHolder.inspectInfo);
			} else if (viewHolder.inspectInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_HK)) {
				goInputActivity(InputHKActivity.class, viewHolder.inspectInfo);
			}  else if (viewHolder.inspectInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_JS)) {
				goInputActivity(InputJSActivity.class, viewHolder.inspectInfo);
			}  else if (viewHolder.inspectInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_JJ)) {
				goInputActivity(InputJJActivity.class, viewHolder.inspectInfo);
			}  else if (viewHolder.inspectInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_KB)) {
				goInputActivity(InputKBActivity.class, viewHolder.inspectInfo);
			}  else if (viewHolder.inspectInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_HJ)) {
				goInputActivity(InputHJActivity.class, viewHolder.inspectInfo);
			}  else if (viewHolder.inspectInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_BR)) {
				goInputActivity(InputBRActivity.class, viewHolder.inspectInfo);
			}  else if (viewHolder.inspectInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_JP)) {
				goInputActivity(InputJPActivity.class, viewHolder.inspectInfo);
			} else {
				ToastUtil.show(mContext, "선택한 설비는 입력 불가능합니다.");
			}
				
//				Intent intent = new Intent(mContext, InspectResultDetailActivity.class);
//				intent.putExtra("inspect", viewHolder.inspectInfo);d
//				intent.putExtra(ConstVALUE.DISPLAY_MENU_TYPE, menuType);
//				intent.putExtra("WRITE_COMPLETE", true);
//				AppUtil.startActivity(mContext, intent);
			}
		});
		
		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(final View v) {
				AlertUtil.showNoTitleAlertOK(mContext, "삭제하시겠습니까?", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ViewHolder viewHolder = (ViewHolder)v.getTag();
						
						InspectResultMasterDao insRetDao = InspectResultMasterDao.getInstance(mContext);
						insRetDao.updateComplete(viewHolder.inspectInfo.master_idx, "N", viewHolder.inspectInfo.type);
						
						if (viewHolder.inspectInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_BT)) {
							InputBTDao.getInstance(mContext).Delete(viewHolder.inspectInfo.master_idx);
						} else if (viewHolder.inspectInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_HK)) {
							InputHKDao.getInstance(mContext).Delete(viewHolder.inspectInfo.master_idx);
						}  else if (viewHolder.inspectInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_JS)) {
							InputJSDao.getInstance(mContext).Delete(viewHolder.inspectInfo.master_idx);
						}  else if (viewHolder.inspectInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_JJ)) {
							InputJJDao.getInstance(mContext).Delete(viewHolder.inspectInfo.master_idx);
						}  else if (viewHolder.inspectInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_KB)) {
							InputKBDao.getInstance(mContext).Delete(viewHolder.inspectInfo.master_idx);
						}  else if (viewHolder.inspectInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_HJ)) {
							InputHJDao.getInstance(mContext).Delete(viewHolder.inspectInfo.master_idx);
						}  else if (viewHolder.inspectInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_BR)) {
							InputBRDao.getInstance(mContext).Delete(viewHolder.inspectInfo.master_idx);
						}  else if (viewHolder.inspectInfo.type.equals(ConstVALUE.CODE_NO_INSPECT_JP)) {
							InputJPDao.getInstance(mContext).Delete(viewHolder.inspectInfo.master_idx);
						} else {
							ToastUtil.show(mContext, "선택한 설비는 삭제 불가능합니다.");
						}
						
						notifyDataSetChanged();
					}
				}, null);
				return false;
			}
		});
		
		return convertView;
	}

	public class ViewHolder {
		public TextView tvTracksName = null;
		public TextView tvTowerNo = null;
		public TextView tvDate = null;
		public TextView tvType = null;
		public InspectInfo inspectInfo = null;
	}
	
	public void goInputActivity(Class<?> cls, InspectInfo info) {
		Intent intent = new Intent(mContext, cls);
		intent.putExtra("inspect", info);
		mContext.startActivity(intent);
	}
}