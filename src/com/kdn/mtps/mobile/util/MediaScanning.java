package com.kdn.mtps.mobile.util;

import java.io.File;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

public class MediaScanning implements MediaScannerConnectionClient{
	private MediaScannerConnection mConnection;

	private File mTargetFile;
	public MediaScanning(Context mContext, File targetFile) {
		
		this.mTargetFile = targetFile;
		mConnection = new MediaScannerConnection(mContext, this);
		mConnection.connect();
	}

	@Override
	public void onMediaScannerConnected() {
		mConnection.scanFile(mTargetFile.getAbsolutePath(), null);
	}
	
	@Override
	public void onScanCompleted(String path, Uri uri) {
		mConnection.disconnect();
	}
}
