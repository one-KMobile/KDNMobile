package com.kdn.mtps.mobile.net.http;

import android.content.Context;
import android.os.Handler;
import android.widget.ProgressBar;

import com.kdn.mtps.mobile.util.Logg;


public class FileTransfer {
	Handler handler;
	Context context;
	
	HttpUploader uploadClient;
	HttpDownloader downloadClient;
	
	public FileTransfer(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}
	
	public String uploadFile(String serverHost, String serverPath, int port, String filePath) {
		uploadClient = HttpUploader.sendFile(serverHost, serverPath, port, filePath, handler);
		return uploadClient.upload();
	}
	
	public String downloadFile(String downloadUrl, String saveDir) {
		downloadClient = new HttpDownloader(downloadUrl, saveDir, handler);
		return downloadClient.download();
	}
	
	public void disconnect() {
		if (uploadClient != null) {
			uploadClient.disconnect();
			uploadClient = null;
		}
		if (downloadClient != null) {
			downloadClient.disconnect();
			downloadClient = null;
		}
	}
	
	public void setProgressBar(ProgressBar pbar) {
		if (uploadClient != null) {
			uploadClient.setPB(pbar);
		} else if (downloadClient != null) {
			downloadClient.setPB(pbar);
		}
	}

	public int getPercent() {
		int percent = 0;
		if (uploadClient != null) {
			percent = uploadClient.getPercent();
		} else if (downloadClient != null) {
			percent = downloadClient.getPercent();
		}
		if (percent%10 == 0)// 10% 단위만 로그찍음.
			Logg.d(context, "percent:"+percent);
		return percent;
	}
}
