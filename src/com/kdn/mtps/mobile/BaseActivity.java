package com.kdn.mtps.mobile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// 폰트적용하는 클래스
// 현재 액티비티를 상속받는 액티비티는 정적View에 대해서 모두 아래에서 설정한 글꼴이 적용된다.
public class BaseActivity extends Activity {
	private static Typeface mTypeface;

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		
		setFont();
    }

	public void setFont() {
		if (mTypeface == null)
            mTypeface = Typeface.createFromAsset(getAssets(), "font.ttf.mp3"); // 폰트파일 용량이 크면 확장자를 mp3로 해야 오류없음 

        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        setGlobalFont(root);
	}
	
	void setGlobalFont(ViewGroup root) {
	    for (int i = 0; i < root.getChildCount(); i++) {
	        View child = root.getChildAt(i);
	        if (child instanceof TextView)
	            ((TextView)child).setTypeface(mTypeface);
	        else if (child instanceof ViewGroup)
	            setGlobalFont((ViewGroup)child);
	    }
	}
	
	public static Typeface getTypeface(Context ctx) {
		if (mTypeface == null)
            mTypeface = Typeface.createFromAsset(ctx.getAssets(), "font.ttf.mp3");
		
		return mTypeface;
	}

}
