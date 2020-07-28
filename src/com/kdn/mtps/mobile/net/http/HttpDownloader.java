package com.kdn.mtps.mobile.net.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

import android.net.Uri;
import android.os.Handler;
import android.widget.ProgressBar;

import com.kdn.mtps.mobile.util.Logg;


public class HttpDownloader {
	static final int DOWNLOAD_DONE = 0;
    static final int DEFAULT_TIMEOUT = 10000;
    
    Handler uiHandler;
    
    InputStream input = null;
    
	String downloadUrl;
	String saveFilePath;
	long lenghtOfFile = 0;
	long bytesDownload = 0;
	
	boolean isDownload = true;
	
	public HttpDownloader(String downloadUrl, String saveDir, Handler uiHandler) {
		this(downloadUrl, saveDir, null, uiHandler);
	}
	public HttpDownloader(String downloadUrl, String saveDir, String saveFilename) {
		this(downloadUrl, saveDir, saveFilename, null);
	}
	public HttpDownloader(String downloadUrl, String saveDir, String saveFilename, Handler uiHandler) {
		this.downloadUrl = downloadUrl;
		File mFolder = new File(saveDir);
		if (!(mFolder.exists()))
			mFolder.mkdirs();
		this.saveFilePath = saveDir + (saveFilename == null ? Uri.parse(downloadUrl).getLastPathSegment() : saveFilename);
		this.uiHandler = uiHandler;
	}
	
	ProgressBar pbar;
	public void setPB(ProgressBar pbar) {
		this.pbar = pbar;
	}
	public String download() {
		String res = null;
		RandomAccessFile output = null;
		try {
	        long fileSize, remains = 0;
	
	        File file = new File(saveFilePath);
	        if (!file.exists()) {
	            file.createNewFile();
	        }
	        output = new RandomAccessFile(file.getAbsolutePath(), "rw"); 
	        fileSize = output.length(); 
	        output.seek(fileSize);
	        
	        bytesDownload = fileSize;// 이어받기라면 기존 파일의 다운로드 사이즈 입력
	        
	        URL url = new URL(downloadUrl);
	        URLConnection conn = url.openConnection(); 
	        conn.setRequestProperty("Range", "bytes=" + String.valueOf(fileSize) + '-'); 
	        conn.connect();
	        conn.setConnectTimeout(DEFAULT_TIMEOUT); 
	        conn.setReadTimeout(DEFAULT_TIMEOUT); 
	        remains = conn.getContentLength(); 
	        lenghtOfFile = remains + fileSize;
	        
	        if (remains <= 0 && fileSize == 0)
	        	return null;
	         
	        if ((remains <= DOWNLOAD_DONE) || (remains == fileSize)) { 
	        	res = saveFilePath;
	            return saveFilePath;
	        }
	        input = conn.getInputStream(); 
	        	
	        byte data[] = new byte[1024*90]; 
	        int count = 0; 
	         
	        if (fileSize < lenghtOfFile) { 
	            while((count = input.read(data)) != -1) { 
	                output.write(data, 0, count); 
	                bytesDownload += count;
	                
	                if (uiHandler != null && pbar != null) {
						//uiHandler.sendEmptyMessage(0);
	                	uiHandler.post(new Runnable() {
							@Override
							public void run() {
								pbar.setProgress(getPercent());
							}
						});
	                }
	                if (!isDownload)
	                	break;
	            }
	        }
	        
	        if (bytesDownload == lenghtOfFile) {
	        	res = saveFilePath;
	        	return saveFilePath;
	        } else
	        	return null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (output != null)
					output.close();
				if (input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (res == null) {
				boolean result = new File(saveFilePath).delete();
			}
		}
		return null;
	}
	
	public void disconnect() {
		try {
			isDownload = false;
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getPercent() {
		if (lenghtOfFile <= 0)
			return 0;
		return (int)(((double)bytesDownload/(double)lenghtOfFile)*100);
	}
}
