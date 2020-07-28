package com.kdn.mtps.mobile.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.kdn.mtps.mobile.R;
import com.kdn.mtps.mobile.db.bean.CameraLog;
import com.kdn.mtps.mobile.util.ImageUtil;
import com.kdn.mtps.mobile.util.Logg;

public class PhotoGridAdapter extends BaseAdapter{

	private LayoutInflater inflater = null;
	private List<CameraLog> mData = new ArrayList<CameraLog>();
	private Context mContext;
	
	public PhotoGridAdapter(Context context) {
		mContext = context;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData.clear();
	}
	
	public List<CameraLog> getList() {
		return mData;
	}
	
	public void setListClear() {
		mData.clear();
	}
	
	public void setList(List<CameraLog> list) {
		mData.clear();
		mData.addAll(list);
		
		notifyDataSetChanged();
	}
	
	public void addList(CameraLog log) {
		mData.add(log);
		
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
		
		Logg.d("info convertView : " + convertView);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.camera_photo_item, null);
			viewHolder = new ViewHolder();
			viewHolder.ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
			viewHolder.cboxSelect = (CheckBox)convertView.findViewById(R.id.cboxSelect);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
        }
		
		CameraLog info = mData.get(position);
		viewHolder.info = info;
		viewHolder.cboxSelect.setChecked(info.isChecked);
//		viewHolder.ivPhoto.
//		ImageCacher.getInstance(mContext).loadFileSrc(info.img_path, viewHolder.ivPhoto);

		Logg.d("info : " + info.idx + " / " + position);
		
		Uri uri = Uri.fromFile(new File(info.img_path));
//		try {
//			Bitmap bitmap = getThumbnail(uri);
			Bitmap bitmap = ImageUtil.getBitmap(uri.getPath(), 4);
			if (bitmap != null)
				viewHolder.ivPhoto.setImageBitmap(bitmap);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewHolder viewHolder = (ViewHolder)v.getTag();
				CheckBox cboxSelect = (CheckBox)v.findViewById(R.id.cboxSelect);
				viewHolder.info.isChecked = !cboxSelect.isChecked();
				cboxSelect.setChecked(!cboxSelect.isChecked());
				
			}
		});
		
		return convertView;
	}

	public class ViewHolder {
		public ImageView ivPhoto = null;
		public CheckBox cboxSelect = null;
		CameraLog info = null;
//		public FacilityInfo facilityInfo = null;
	}
	
	public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException{
		int THUMBNAIL_SIZE = 100;
        InputStream input = mContext.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither=true;//optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        input = mContext.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        
        int ret = ImageUtil.getImageOrientation(uri.getPath());
		
		if (ret != 0)
			bitmap = ImageUtil.getRotatedBitmap(bitmap, ret);
		
        input.close();
        return bitmap;
    }

    private int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        Logg.d("k : " + k);
        if(k==0) return 1;
        else return k;
    }
    
}