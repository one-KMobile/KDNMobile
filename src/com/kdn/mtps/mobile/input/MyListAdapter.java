package com.kdn.mtps.mobile.input;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.inspect.InspectResultSearchActivity;

public class MyListAdapter extends BaseAdapter{
	Context mContext;
//	String[] strNames;
	ArrayList<String> mItems = new ArrayList<String>();
	int mSelected;
	AlertDialog mDialog;
	
    public MyListAdapter(Context context, AlertDialog dialog){
    	mContext = context;
    	mDialog = dialog;
    }

    public void setNames(String[] names) {
//    	strNames = names;
    }
    
    public void setList(int selected, ArrayList<String> list) {
    	mSelected = selected;
    	mItems.clear();
    	mItems = list;
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder myViewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_list_item, null);
            myViewHolder = new ViewHolder();
            myViewHolder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            myViewHolder.tvContent.setText(mItems.get(position));
            myViewHolder.rbCheck = (RadioButton) convertView.findViewById(R.id.rbCheck);

            if (mSelected == position)
            	myViewHolder.rbCheck.setChecked(true);
            
            convertView.setTag(myViewHolder);
            
        }
        else{
            myViewHolder = (ViewHolder) convertView.getTag();
        }

        myViewHolder.position = position;

        convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ViewHolder myViewHolder = (ViewHolder) v.getTag();
				((InspectResultSearchActivity)mContext).setNameNo(myViewHolder.position);
				mDialog.dismiss();
			}
		});
        
//        myViewHolder.txt_icon_name.setText(imageName[position]);
        return convertView;
    }

    class ViewHolder {
    	TextView tvContent;
    	RadioButton rbCheck;
    	int position;
    }
}


