package com.kdn.mtps.mobile.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.facility.FacilityInfo;
import com.kdn.mtps.mobile.facility.FacilityMapActivity;
import com.kdn.mtps.mobile.inspect.InspectInfo;
import com.kdn.mtps.mobile.util.AppUtil;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.overlay.NMapPOIitem;

public class NMapCalloutCustomOverlayView extends NMapCalloutOverlayView {

	private View mCalloutView;
	private TextView mCalloutText;
	private View mRightArrow;
	private TextView tvDetailInfo;
	private Context mContext;

	public NMapCalloutCustomOverlayView(Context context, NMapOverlay itemOverlay, NMapOverlayItem item, Rect itemBounds, int viewMode) {
		super(context, itemOverlay, item, itemBounds);

		mContext = context;
		
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater)getContext().getSystemService(infService);
		li.inflate(R.layout.callout_overlay_view, this, true);

		mCalloutView = findViewById(R.id.callout_overlay);
		mCalloutText = (TextView)mCalloutView.findViewById(R.id.callout_text);
		mRightArrow = findViewById(R.id.callout_rightArrow);
		tvDetailInfo = (TextView)findViewById(R.id.tvDetailInfo);
		
		

		if (item instanceof NMapPOIitem) {
			if (((NMapPOIitem)item).hasRightAccessory() == false) {
				mRightArrow.setVisibility(View.GONE);
			}
		}
		
		if (viewMode == ConstVALUE.MODE_MAPVIEW_ALL) {
			mCalloutText.setText(item.getTitle());
			Object object = ((NMapPOIitem)item).getTag();
			tvDetailInfo.setText("상세정보");
			tvDetailInfo.setTag(((NMapPOIitem)item).getTag());
			tvDetailInfo.setOnClickListener(goDetailClickListener);
			mCalloutText.setOnClickListener(goDetailClickListener);
			mCalloutText.setTag(((NMapPOIitem)item).getTag());
			mCalloutView.setOnClickListener(goDetailClickListener);
			mCalloutView.setTag(((NMapPOIitem)item).getTag());
		} else if (viewMode == ConstVALUE.MODE_MAPVIEW_DETAIL) {
			mCalloutText.setVisibility(View.GONE);
			Object object = ((NMapPOIitem)item).getTag();
			
			FacilityInfo facInfo = null;
			InspectInfo insInfo = null;
			
			if (object instanceof FacilityInfo) {
				FacilityInfo info = (FacilityInfo) ((NMapPOIitem)item).getTag();
				tvDetailInfo.setText(info.ADDRESS);
			} else {
				InspectInfo info = (InspectInfo) ((NMapPOIitem)item).getTag();
				tvDetailInfo.setText(info.address);
			}
		} else {
			tvDetailInfo.setVisibility(View.GONE);
		}
	}

	private final OnClickListener callOutClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if (mOnClickListener != null) {
				mOnClickListener.onClick(null, mItemOverlay, mOverlayItem);
			}
		}
	};
	
	private final OnClickListener goDetailClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			Object object = view.getTag();
			
			FacilityInfo facInfo = null;
			InspectInfo insInfo = null;
			
			if (object instanceof FacilityInfo) {
				facInfo = (FacilityInfo) view.getTag();
				if (facInfo == null)
					return;
			} else {
				facInfo = new FacilityInfo();
				insInfo = (InspectInfo) view.getTag();
				
				if (insInfo == null)
					return;
				facInfo.FNCT_LC_DTLS = insInfo.tracksName;
				facInfo.LATITUDE = insInfo.latitude;
				facInfo.LONGITUDE = insInfo.longitude;
				facInfo.EQP_NM = insInfo.eqpNm;
				facInfo.ADDRESS = insInfo.address;
//				return;
			}
			
			if (facInfo == null && insInfo == null)
				return;
			
			Intent intent = new Intent(mContext, FacilityMapActivity.class);
			intent.putExtra("facility", facInfo);
			AppUtil.startActivity(mContext, intent);
		}
	};

}
