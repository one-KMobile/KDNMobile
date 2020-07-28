package com.kdn.mtps.mobile.net.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import com.kdn.mtps.mobile.util.Logg;

/**
 * Upload a file to server chunk by chunk, using socket connection instead of
 * standard android library because of infamous pre froyo android bug 3164
 * http://code.google.com/p/android/issues/detail?id=3164#c6
 * 
 * Class could be redefined in order to implement nice features, like notify of
 * upload progress and other goodies
 * 
 * 
 * -- Original credits: keianhzo
 * http://groups.google.com/group/android-developers/msg/4ddd2e502f195e3a
 * 
 * -- How to use it:
 * http://stackoverflow.com/questions/2105364/android-i-want-to
 * -show-file-upload-progress-to-the-user HttpMultipartClient
 * httpMultipartClient = new HttpMultipartClient("v2.bluppr.nl",
 * "/webservice/placeorder", 80); FileInputStream fis = new FileInputStream(path
 * + fileName); httpMultipartClient.addFile(fileName, fis, fis.available());
 * httpMultipartClient.setRequestMethod("POST"); httpMultipartClient.send();
 * 
 * 
 * -- Server side code: <?php $target_path = "uploads/"; $target_path =
 * $target_path . basename( $_FILES['uploadedfile']['name']);
 * if(move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $target_path)) {
 * echo "The file ". basename( $_FILES['uploadedfile']['name'])." has been
 * uploaded " .$_POST["order"]. " post"; } else{ echo
 * "There was an error uploading the file, please try again!"; } ?>
 * 
 * @author Alfredo "Rainbowbreeze" Morresi
 */

public class HttpUploader {
	
	public static HttpUploader sendFile(String serverHost, String serverPath, int port, String filePath, Handler uiHandler) {
		HttpUploader httpMultipartClient = null;
		try {
			httpMultipartClient = new HttpUploader(serverHost, serverPath, port);
			FileInputStream fis = new FileInputStream(filePath);
			httpMultipartClient.setUIHandler(uiHandler);
			httpMultipartClient.addFile(new File(filePath).getName(), fis, new File(filePath).length());
//			httpMultipartClient.addField("test1", "testvalue1");
//			httpMultipartClient.addField("test2", "testvalue2");
			httpMultipartClient.setRequestMethod("POST");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return httpMultipartClient;
	}
	
	public class Parameter {

		private String name;
		private String value;

		public Parameter(String n, String v) {
			name = n;
			value = v;
		}

		public void setName(String n) {
			name = n;
		}

		public void setValue(String v) {
			value = v;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getName() + ":" + getValue();
		}
	}

	// ---------- Private fields
	private static final String TAG = "ROK";

	private static final int CONNECTION_TIMEOUT = 10000;
	private static final String END = "\r\n";
	private final String boundary = new Integer(new Random().nextInt(Integer.MAX_VALUE)).toString();
	private final String lastBoundary = END + "--" + boundary + "--" + END;

	private Socket socket;
	private String host;
	private int port;
	private String path;
	private String method;
	private List<Parameter> headers;
	private List<Parameter> cookies;
	private List<Parameter> fields;
	private String fileName;
	private InputStream fileStream;
	PrintStream outStream;
	private long fileSize;
	private int responseCode;
	private String responseMessage;
	private String responseBody;
	private List<Parameter> responseHeaders;

	private StringBuilder headersBuffer;
	private StringBuilder bodyBuffer;
	private long length = 0;
	
	boolean isUpload = true;

	// ---------- Constructors
	public HttpUploader(String h, String pth, int p) throws IllegalArgumentException {
		if (h == null || h.length() == 0)
			throw new IllegalArgumentException("Invalid host name: host=" + h);
		else {
			host = h;
			path = pth;
			port = p;
			socket = new Socket();
			headers = new ArrayList<Parameter>();
			cookies = new ArrayList<Parameter>();
			fields = new ArrayList<Parameter>();
			responseHeaders = new ArrayList<Parameter>();
			responseBody = null;
		}
	}

	// ---------- Public properties
	public void setRequestMethod(String m) {
		method = m;
	}

	public void setPath(String p) {
		path = p;
	}

	public String getRequestMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public List<Parameter> getResponseHeaders() {
		return responseHeaders;
	}

	public String getResponseBody() {
		return responseBody;
	}

	// ---------- Public methods
	public void disconnect() {
		try {
			socket.close();
			isUpload = false;
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addHeader(String name, String value) throws IllegalArgumentException {
		if (name == null || value == null)
			throw new IllegalArgumentException("Header invalid: name=" + name + ", value=" + value);
		else {
			headers.add(new Parameter(name, value));
			if (Log.isLoggable(TAG, Log.DEBUG))
				Log.d(TAG, "Adding header [" + name + ": " + value + "]");
		}
	}

	public void addCookie(String name, String value) throws IllegalArgumentException {
		if (name == null || value == null)
			throw new IllegalArgumentException("Cookie invalid: name=" + name + ", value=" + value);
		else {
			cookies.add(new Parameter(name, value));
			if (Log.isLoggable(TAG, Log.DEBUG))
				Log.d(TAG, "Adding cookie [" + name + ": " + value + "]");
		}
	}

	public void addField(String name, String value) throws IllegalArgumentException {
		if (name == null || value == null)
			throw new IllegalArgumentException("Field invalid: name=" + name + ", value=" + value);
		else {
			fields.add(new Parameter(name, value));
			if (Log.isLoggable(TAG, Log.DEBUG))
				Log.d(TAG, "Adding field [" + name + ": " + value + "]");
		}
	}

	public void addFile(String fn, InputStream is, long fs) {
		if (is == null)
			throw new IllegalArgumentException("Invalid null input stream");
		else {
			fileName = fn;
			fileStream = is;
			fileSize = fs;
			//Logg.d("fileSize:"+fileSize);
			if (Log.isLoggable(TAG, Log.DEBUG))
				Log.d(TAG, "Adding file [filename: " + fileName + "]");
		}
	}

	long bytesSent = 0;
	ProgressBar pbar;
	Handler uiHandler;
	void setUIHandler(Handler uiHandler) {
		this.uiHandler = uiHandler;
	}
	public void setPB(ProgressBar pbar) {
		this.pbar = pbar;
	}
	public int getPercent() {
		if (fileSize <= 0)
			return 0;
		return (int)(((double)bytesSent/(double)fileSize)*100);
	}
	public String upload() {
		prepare();

		BufferedReader reader = null;
		try {
			// We send the Http Request
			socket.connect(new InetSocketAddress(host, port));
			socket.setSoTimeout(CONNECTION_TIMEOUT);
			
			outStream = new PrintStream(socket.getOutputStream());
			outStream.print(headersBuffer);
			outStream.print(bodyBuffer);
			bytesSent += headersBuffer.length() + headersBuffer.length();

			byte[] bytes = new byte[1024 * 90];
			int size;
			while ((size = fileStream.read(bytes)) > 0) {
				bytesSent += size;
				outStream.write(bytes, 0, size);
				outStream.flush();
				
				//Logg.d("file send:"+bytesSent);
				if (uiHandler != null && pbar != null) {
					//uiHandler.sendEmptyMessage(0);
					uiHandler.post(new Runnable() {
						@Override
						public void run() {
							pbar.setProgress(getPercent());
						}
					});
				}
//				if (!isUpload)
//					break;
			}
			fileStream.close();

			outStream.print(lastBoundary);
			outStream.flush();

			// We read the response from the server
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String line = "";
			boolean headersEnd = false;
			StringBuffer payload = new StringBuffer();
			boolean bodyEnd = false;
			while ((line = reader.readLine()) != null) {
				//Logg.d("line:"+line);
				if (!headersEnd && line.length() == 0)
					headersEnd = true;
				else if (!bodyEnd && line.length() == 0) {
					bodyEnd = true;
				} else if (headersEnd && !bodyEnd) {
					payload.append(line.trim());
					break;
				}
			}
			//{"result":"0" , "filename" :"c_13546013631354601380469850.png" }
			responseBody = payload.toString();
			
			JSONObject jObj = new JSONObject(responseBody);
			String filename = null;
			if (jObj != null)
				filename = jObj.optString("filename", null);
			return filename;
			
//			String line = reader.readLine();
//			Logg.d("line:"+line);
			
//			String[] responseLine = line.trim().split(" ", 3);
//			responseCode = new Integer(responseLine[1]);
//			responseMessage = responseLine[2];
//
//			boolean headersEnd = false;
//			while ((line = reader.readLine()) != null && !headersEnd) {
//				if (line.length() == 0)
//					headersEnd = true;
//				else {
//					String[] headerLine = line.trim().split(":", 2);
//					responseHeaders.add(new Parameter(headerLine[0], headerLine[1]));
//				}
//			}
//
//			StringBuffer payload = new StringBuffer();
//			boolean bodyEnd = false;
//			while ((line = reader.readLine()) != null && !bodyEnd) {
//				if (line.length() == 0)
//					bodyEnd = true;
//				else
//					payload.append(line.trim());
//			}
//
//			responseBody = payload.toString();
//			Logg.d("responseBody:"+responseBody);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				//fileStream.close();
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

	// ---------- Private methods
	private void prepare() {
		preHeaders();
		prepareBody();
		postHeaders();
	}

	private void preHeaders() {
		if (Log.isLoggable(TAG, Log.DEBUG))
			Log.d(TAG, "Pre headers");

		headersBuffer = new StringBuilder();
		headersBuffer.append(method + " " + path + " HTTP/1.1" + END);
		headersBuffer.append("User-Agent: FileSocialClient 1.0" + END);
		headersBuffer.append("Host: " + host + END);
		headersBuffer.append("Content-Type: multipart/form-data; boundary=" + boundary + END);

		if (!headers.isEmpty()) {
			for (Iterator<Parameter> it = headers.iterator(); it.hasNext();) {
				Parameter param = it.next();
				headersBuffer.append(param.getName());
				headersBuffer.append(": ");
				headersBuffer.append(param.getValue());
				headersBuffer.append(END);

				if (Log.isLoggable(TAG, Log.DEBUG))
					Log.d(TAG, "Header added: " + param);
			}
		}

		if (!cookies.isEmpty()) {
			headersBuffer.append("Cookie: ");
			for (Iterator<Parameter> it = cookies.iterator(); it.hasNext();) {
				Parameter param = it.next();

				headersBuffer.append(param.getName());
				headersBuffer.append("=");
				headersBuffer.append(param.getValue());

				if (it.hasNext())
					headersBuffer.append("; ");

				if (Log.isLoggable(TAG, Log.DEBUG))
					Log.d(TAG, "Cookie added: " + param);
			}
			headersBuffer.append(END);
		}

		headersBuffer.append("Content-Length: ");
	}

	private void postHeaders() {
		if (Log.isLoggable(TAG, Log.DEBUG))
			Log.d(TAG, "Post headers");

		length = fileSize + lastBoundary.length() + bodyBuffer.length();
		headersBuffer.append(length);
		headersBuffer.append(END + END);
	}

	private void prepareBody() {
		if (Log.isLoggable(TAG, Log.DEBUG))
			Log.d(TAG, "Preparing body");

		bodyBuffer = new StringBuilder();

		if (!fields.isEmpty()) {
			for (Parameter param : fields) {
				bodyBuffer.append("--");
				bodyBuffer.append(boundary);
				bodyBuffer.append(END);
				bodyBuffer.append("Content-Disposition: form-data; name=\"");
				bodyBuffer.append(param.getName());
				bodyBuffer.append("\"");
				bodyBuffer.append(END);
				bodyBuffer.append(END);
				bodyBuffer.append(param.getValue());
				bodyBuffer.append(END);

				if (Log.isLoggable(TAG, Log.DEBUG))
					Log.d(TAG, "Field added: " + param);
			}
		}

		if (fileStream != null) {
			bodyBuffer.append("--");
			bodyBuffer.append(boundary);
			bodyBuffer.append(END);
			bodyBuffer.append("Content-Disposition: form-data; name=\"");
			bodyBuffer.append("file");
			bodyBuffer.append("\"; filename=\"");
			bodyBuffer.append(fileName);
			bodyBuffer.append("\"");
			bodyBuffer.append(END);
			bodyBuffer.append(END);
		}
	}

}
