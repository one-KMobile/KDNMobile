package com.kdn.mtps.mobile.camera;

import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.kdn.mtps.mobile.net.http.HttpUtil;
import com.kdn.mtps.mobile.util.FileUtil;
import com.kdn.mtps.mobile.util.Logg;
import com.kdn.mtps.mobile.util.thread.ATask;

public class ImageCacher {
	/** 로컬파일을 로딩하여 Cache */
	public static final int TYPE_LOAD_FILE = 0;
	/** 웹파일을 다운로드하여 Cache */
	public static final int TYPE_LOAD_WEB = 1;
	/** 웹파일을 다운로드하여 Cache & 파일로 저장하여 파일 Cache */
	public static final int TYPE_LOAD_WEB_FILE = 2;
	
	static final int HARD_CACHE_CAPACITY = 500;
	static ImageCacher ic;
	
	Context mContext;
	
	public ImageCacher(Context context) {
    	mContext = context;
    }
	
	public static ImageCacher getInstance(Context context) {
    	if (ic == null)
    		ic = new ImageCacher(context);
    	return ic;
    }
	    
    // Cache, Queue
	ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2);
    HashMap<String, Bitmap> sHardBitmapCache = new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
		private static final long serialVersionUID = 1L;
		@Override
        protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) {
            if (size() > HARD_CACHE_CAPACITY) {
                sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
                return true;
            } else
                return false;
        }
    };
    // 이미지를 웹에서 받아오는 경우 taskMap에 저장했다가 필요한 경우 취소처리해줌.
    Map<String, AndroidHttpClient> httpClientMap = new HashMap<String, AndroidHttpClient>();
    
    public boolean containCache(String url) {
    	boolean res = false;
    	synchronized (sHardBitmapCache) {
    		res = sHardBitmapCache.containsKey(url);
    	}
    	if (!res)
    		res = sSoftBitmapCache.containsKey(url);
    	return res;
    }
    void addBitmapToCache(String url, Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (sHardBitmapCache) {
                sHardBitmapCache.put(url, bitmap);
            }
        }
    }
    Bitmap getBitmapFromCache(String url) {
        synchronized (sHardBitmapCache) {
            final Bitmap bitmap = sHardBitmapCache.get(url);
            if (bitmap != null) {
                sHardBitmapCache.remove(url);
                sHardBitmapCache.put(url, bitmap);
                return bitmap;
            }
        }
        SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(url);
        if (bitmapReference != null) {
            final Bitmap bitmap = bitmapReference.get();
            if (bitmap != null) {
                return bitmap;
            } else {
                sSoftBitmapCache.remove(url);
            }
        }
        return null;
    }
    public void cacheReload(String filePath, int thumbW, int thumbH) {
    	Bitmap bitmap = FileUtil.loadBitmap(mContext, filePath, thumbW, thumbH);
    	addBitmapToCache(filePath, bitmap);
    }
    public Bitmap getBitmap(String filePath) {
    	Bitmap bitmap = getBitmapFromCache(filePath);
    	if (bitmap == null) {
    		bitmap = FileUtil.loadBitmap(filePath);
        	addBitmapToCache(filePath, bitmap);
    	}
    	return bitmap;
    }
    
    public void removeCache(String url) {
    	sHardBitmapCache.remove(url);
    	sSoftBitmapCache.remove(url);
    }
    public void clearCache() {
        sHardBitmapCache.clear();
        sSoftBitmapCache.clear();
        ic = null;
    }
    
    /**
     * Cache 저장없이, File 원본 이미지를 로딩
     * @param loadType
     * @param url
     * @param imageView
     */
    public AsyncTask<Void, Void, Bitmap> loadFileSrc(final String filePath, final ImageView imageView) {
    	return loadFileSrc(filePath, imageView, null);
    }
    public AsyncTask<Void, Void, Bitmap> loadFileSrc(final String filePath, final ImageView imageView, final ProgressBar pb) {
    	return ATask.executeResult(new ATask.OnTaskBitmap() {
			public void onPre() {
				if (pb != null)
					pb.setVisibility(View.VISIBLE);
			}
			public Bitmap onBG() {
				return FileUtil.loadBitmap(filePath);
			}
			public void onPost(Bitmap bm) {
				if (pb != null)
					pb.setVisibility(View.GONE);
				if (bm != null) {
//		    		recycle(imageView);
		    		SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bm);
		    		imageView.setAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
		    		imageView.setImageBitmap(softBitmap.get());
		    	}
			}
		});
    }
    /**
     * Cache 저장없이, Web 원본 이미지를 로딩
     * @param url
     * @param imageView
     */
    public AsyncTask<Void, Void, Bitmap> loadWebSrc(final String url, final ImageView imageView) {
    	return loadWebSrc(url, imageView, null, null);
    }
    public AsyncTask<Void, Void, Bitmap> loadWebSrc(final String url, final ImageView imageView, final ProgressBar pb) {
    	return loadWebSrc(url, imageView, pb, null);
    }
    public AsyncTask<Void, Void, Bitmap> loadWebSrc(final String url, final ImageView imageView, final ProgressBar pb, final String saveFilePath) {
    	return ATask.executeResult(new ATask.OnTaskBitmap() {
			public void onPre() {
				if (pb != null)
					pb.setVisibility(View.VISIBLE);
			}
			public Bitmap onBG() {
				Bitmap bitmap = HttpUtil.getUrlBitmap(url);
				if (bitmap != null && saveFilePath != null)
					FileUtil.saveBitmap(bitmap, saveFilePath);
				return bitmap; 
			}
			public void onPost(Bitmap bm) {
				if (pb != null)
					pb.setVisibility(View.GONE);
				if (bm != null) {
//		    		recycle(imageView);
		    		SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bm);
		    		imageView.setAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
		    		imageView.setImageBitmap(softBitmap.get());
		    	}
			}
		});
    }
    /**
     * Imageview의 bitmap 객체를 recycle 처리하여 메모리 누수를 방지함.
     * @param img
     */
    void recycle(ImageView img) {
		Drawable d = img.getDrawable();
	    if (d != null && d instanceof BitmapDrawable) {
	        Bitmap b = ((BitmapDrawable)d).getBitmap();
	        if (b != null)
	        	b.recycle();
	        d.setCallback(null);
	    } // 현재로서는 BitmapDrawable 이외의 drawable 들에 대한 직접적인 메모리 해제는 불가능하다.
	}
    
   /**
    * Cache 저장하고, 썸네일 이미지를 로딩
    * @param loadType
    * @param url
    * @param imageView
    * @param thumbW
    * @param thumbH
    * @param defaultImg - 기본이미지(안넣을꺼면 -1)
    */
    public void loadThumb(int loadType, String url, ImageView imageView, int thumbW, int thumbH, int defaultImg) {
    	loadThumb(loadType, url, imageView, thumbW, thumbH, defaultImg, true);
    }
    public void loadThumb(int loadType, String url, ImageView imageView, int thumbW, int thumbH, int defaultImg, boolean isRounded) {
    	if (url == null)
    		imageView.setImageResource(defaultImg);
    	else
    		loadThumb(loadType, url, imageView, thumbW, thumbH, null, null, defaultImg, isRounded);
    }
    /**
     * Cache 저장하고, 썸네일 이미지를 로딩
     * @param loadType
     * @param url
     * @param imageView
     * @param thumbW
     * @param thumbH
     * @param pb - progressbar 객체
     * @param defaultImg - 기본이미지(안넣을꺼면 -1)
     */
    public void loadThumb(int loadType, String url, ImageView imageView, int thumbW, int thumbH, ProgressBar pb, int defaultImg, boolean isRounded) {
    	loadThumb(loadType, url, imageView, thumbW, thumbH, pb, null, defaultImg, isRounded);
    }
    public void loadThumb(int loadType, String url, ImageView imageView, int thumbW, int thumbH, ProgressBar pb, int defaultImg) {
    	loadThumb(loadType, url, imageView, thumbW, thumbH, pb, null, defaultImg, true);
    }
    /**
     * Cache 저장하고, 썸네일 이미지를 로딩
     * @param loadType
     * @param url
     * @param imageView
     * @param thumbW
     * @param thumbH
     * @param pb - progressbar 객체
     * @param localUrl - Local에 File로 저장할 경로
     * @param defaultImg - 기본이미지(안넣을꺼면 -1)
     */
    public void loadThumb(int loadType, String url, ImageView imageView, int thumbW, int thumbH, ProgressBar pb, String localUrl, int defaultImg) {
    	loadThumb(loadType, url, imageView, thumbW, thumbH, pb, localUrl, defaultImg, true);
    }
    public void loadThumb(int loadType, String url, ImageView imageView, int thumbW, int thumbH, ProgressBar pb, String localUrl, int defaultImg, boolean isRounded) {
        Bitmap bitmap = null;
        if (/*thumbW == -1 || */(bitmap = getBitmapFromCache(url)) == null) {
        	forceLoad(loadType, url, imageView, thumbW, thumbH, pb, localUrl, defaultImg, isRounded);
        } else {
        	cancelPotentialLoad(loadType, url, imageView);
        	if (bitmap != null) {
        		imageView.setImageBitmap(isRounded ? getRoundedBitmap(bitmap) : bitmap);
                if (pb != null)
                	pb.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
        	}
        }
    }
    
    void forceLoad(int loadType, String url, ImageView imageView, int thumbW, int thumbH, int defaultImg) {
    	forceLoad(loadType, url, imageView, thumbW, thumbH, null, null, defaultImg, true);
    }
    void forceLoad(int loadType, String url, ImageView imageView, int thumbW, int thumbH, ProgressBar pb, String localUrl, int defaultImg, boolean isRounded) {
        if (url == null) {
            imageView.setImageDrawable(null);
            return;
        }
        if (cancelPotentialLoad(loadType, url, imageView)) {
        	BitmapDownloaderTask task = new BitmapDownloaderTask(loadType, url, imageView, thumbW, thumbH, pb, localUrl, isRounded);
    		DownloadedDrawable downloadedDrawable = 
    			defaultImg == -1 ? 
    				new DownloadedDrawable(task, imageView.getContext()) : new DownloadedDrawable(task, imageView.getContext(), defaultImg);
            imageView.setImageDrawable(downloadedDrawable);
            //imageView.setMinimumHeight(156);
            task.execute();
        }
    }
    boolean cancelPotentialLoad(int loadType, String url, ImageView imageView) {
    	BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }
    public void cancelTaskWebAll() {
    	Logg.d("cancelTaskWebAll");
//    	Iterator<String> it = taskWebMap.keySet().iterator();
//		while (it.hasNext()) {
//			BitmapDownloaderTask bitmapDownloaderTask = taskWebMap.get(it.next());
//			bitmapDownloaderTask.cancel(true);
//			Logg.d("cancel:"+bitmapDownloaderTask.url);
//		}
//		taskWebMap.clear();
    }
    BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }
    
    class BitmapDownloaderTask extends AsyncTask<Void, Void, Bitmap> {
        WeakReference<ImageView> imageViewReference;
        ProgressBar pb;
        
        String url;
    	String localUrl;
    	int loadType;
    	int thumbW;
    	int thumbH;
    	
        boolean isThumb;
        boolean isRounded;

        public BitmapDownloaderTask(int loadType, String url, ImageView imageView, int thumbW, int thumbH, ProgressBar pb, String localUrl, boolean isRounded) {
        	imageViewReference = new WeakReference<ImageView>(imageView);
        	this.pb = pb;
        	this.url = url;
        	this.localUrl = localUrl;
        	this.loadType = loadType;
        	this.thumbW = thumbW;
        	this.thumbH = thumbH;
        	this.isRounded = isRounded;
        }        	

        protected Bitmap doInBackground(Void... params) {
        	if (TYPE_LOAD_FILE == loadType)
        		return FileUtil.loadBitmap(mContext, url, thumbW, thumbH);
        	else if (TYPE_LOAD_WEB == loadType)
        		return HttpUtil.downloadBitmap(url, thumbW, thumbH);
        	else if (TYPE_LOAD_WEB_FILE == loadType) {
        		if (new File(localUrl).exists()) {// 로컬파일이 있을때
            		return FileUtil.loadBitmap(mContext, url, thumbW, thumbH);
        		} else {// 로컬파일이 없으면 다운받음.
        			return HttpUtil.downloadBitmap(url, thumbW, thumbH);
        		}
        	} else
        		return null;
        }
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }
            // TODO : 무조건 라운딩 이미지 저장
//            if (bitmap != null)
//            	bitmap = getRoundedCornerBitmap(bitmap);
            addBitmapToCache(url, bitmap);
            if (imageViewReference != null && bitmap != null) {
                ImageView imageView = imageViewReference.get();
                BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
                if (this == bitmapDownloaderTask) {
                    imageView.setImageBitmap(isRounded ? getRoundedBitmap(bitmap) : bitmap);
                    if (pb != null)
                    	pb.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    class DownloadedDrawable extends BitmapDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;
        @SuppressWarnings("deprecation")
        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask, Context context) {
        	super();
            bitmapDownloaderTaskReference = new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
        }
        @SuppressWarnings("deprecation")
		public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask, Context context, int defaultImg) {
        	super(getRoundedBitmap(BitmapFactory.decodeResource(context.getResources(), defaultImg)));
            bitmapDownloaderTaskReference = new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
        }
        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }
    }
    
    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 7;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
