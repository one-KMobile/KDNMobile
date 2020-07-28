package com.kdn.mtps.mobile.net.http;

import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.ParcelFileDescriptor;

import com.kdn.mtps.mobile.constant.ConstVALUE;
import com.kdn.mtps.mobile.util.Logg;

public class HttpUtil {
	public static final int TIMEOUT = 10 * 1000;
	public static HttpClient client = new DefaultHttpClient();
	public static ProgressDialog pd;
	
	public static String connectMultipartPostG(String httpUrl,
			CustomMultiPartEntity multipartEntity) {

		BufferedReader in = null;

		try {
			HttpClient client = new DefaultHttpClient();

			client.getParams().setParameter("http.protocol.expect-continue",
					false);
//			client.getParams().setParameter("http.connection.timeout", 30000);
//			client.getParams().setParameter("http.socket.timeout", 30000);

			HttpPost request = new HttpPost(httpUrl);

			if (multipartEntity != null) {
				request.setEntity(multipartEntity);
			}

			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));

			String result = "";
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null)
				result += (!"".equals(result) ? NL : "") + line;

			Logg.d(httpUrl + " result:" + result);

			return result.trim();
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().toLowerCase().contains("time")
					&& e.getMessage().toLowerCase().contains("out")) {
				return "timeout";
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public static String connectMultipartPost(String httpUrl,
			HttpEntity multipartEntity) {
		BufferedReader in = null;
		try {
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter("http.protocol.expect-continue",
					false);
			client.getParams().setParameter("http.connection.timeout", TIMEOUT);
			client.getParams().setParameter("http.socket.timeout", TIMEOUT);

			HttpPost request = new HttpPost(httpUrl);

			if (multipartEntity != null) {
				request.setEntity(multipartEntity);
			}

			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));

			String result = "";
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null)
				result += (!"".equals(result) ? NL : "") + line;

			Logg.d(httpUrl + " result:" + result);

			return result.trim();
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().toLowerCase().contains("time")
					&& e.getMessage().toLowerCase().contains("out")) {
				return "timeout";
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}
	
	public static String connectPost(String httpUrl,
			List<NameValuePair> postParameters) {
		BufferedReader in = null;
		try {
//			HttpClient client = new DefaultHttpClient();
			if (client == null)
				client = new DefaultHttpClient();
			client.getParams().setParameter("http.protocol.expect-continue",
					false);
			client.getParams().setParameter("http.connection.timeout", TIMEOUT);
			client.getParams().setParameter("http.socket.timeout", TIMEOUT);

			HttpConnectionParams.setConnectionTimeout(client.getParams(), TIMEOUT);
			
			HttpPost request = new HttpPost(httpUrl);
			
			Logg.d("httpUrl : " + httpUrl);
			if (postParameters != null) {
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						postParameters);
				formEntity.setContentEncoding("UTF-8");
				request.setEntity(formEntity);
			}

			HttpResponse response = client.execute(request);
			
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent(), "UTF-8"));

			StringBuilder result = new StringBuilder();
			String line = "";
			while ((line = in.readLine()) != null)
				result.append(line);

			Logg.d("result : " + result.toString());
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			dismissDialog();
		}

		return null;
	}
	
	public static String connectPost(String httpUrl,
			List<NameValuePair> postParameters, int timeout) {
		BufferedReader in = null;
		try {
//			HttpClient client = new DefaultHttpClient();
			if (client == null)
				client = new DefaultHttpClient();
			client.getParams().setParameter("http.protocol.expect-continue",
					false);
			client.getParams().setParameter("http.connection.timeout", timeout);
			client.getParams().setParameter("http.socket.timeout", timeout);

			HttpConnectionParams.setConnectionTimeout(client.getParams(), timeout);
			
			HttpPost request = new HttpPost(httpUrl);
			Logg.d("httpUrl : " + httpUrl);
			if (postParameters != null) {
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						postParameters);
				request.setEntity(formEntity);
			}

			HttpResponse response = client.execute(request);
			
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent(), "UTF-8"));

			StringBuilder result = new StringBuilder();
			String line = "";
			while ((line = in.readLine()) != null)
				result.append(line);

			Logg.d("result : " + result.toString());
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			dismissDialog();
		}

		return null;
	}
	
	public static String connectPostProgress(Context context, String httpUrl,
			List<NameValuePair> postParameters, boolean cancelable, int resId) {
		BufferedReader in = null;
		try {
			
			showDialog(context, cancelable, resId);
//			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter("http.protocol.expect-continue",
					false);
			client.getParams().setParameter("http.connection.timeout", TIMEOUT);
			client.getParams().setParameter("http.socket.timeout", TIMEOUT);

			HttpConnectionParams.setConnectionTimeout(client.getParams(), TIMEOUT);
			
			HttpPost request = new HttpPost(httpUrl);
			Logg.d("httpUrl : " + httpUrl);
			if (postParameters != null) {
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						postParameters);
				request.setEntity(formEntity);
			}

			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent(), "UTF-8"));

			StringBuilder result = new StringBuilder();
			String line = "";
			while ((line = in.readLine()) != null)
				result.append(line);

			Logg.d("result : " + result.toString());
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public static String connectGet(String httpUrl) {
		return connectGet(httpUrl, null);
	}

	public static String connectGet(String httpUrl, List<NameValuePair> params) {
		BufferedReader in = null;
		try {
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter("http.protocol.expect-continue",
					false);
			client.getParams().setParameter("http.connection.timeout", TIMEOUT);
			client.getParams().setParameter("http.socket.timeout", TIMEOUT);

			HttpConnectionParams.setConnectionTimeout(client.getParams(), TIMEOUT);
			
			if (params != null) {
				httpUrl += "?" + URLEncodedUtils.format(params, "utf-8");
			}

			HttpGet request = new HttpGet(httpUrl);
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));

			StringBuilder result = new StringBuilder();
			String line = "";
			while ((line = in.readLine()) != null)
				result.append(line);

			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public static Drawable getUrlDrawable(String imgurl) {
		Drawable d;
		try {
			InputStream is = (InputStream) new URL(imgurl).getContent();
			d = Drawable.createFromStream(is, "src name");
			is.close();
			return d;
		} catch (Exception e) {
			return null;
		}
	}

	// Inner classes
	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	public static Bitmap downloadBitmap(String url, int w, int h) {
		return downloadBitmap(AndroidHttpClient.newInstance("Android"), url, w,
				h);
	}

	public static Bitmap downloadBitmap(HttpClient client, String url, int w,
			int h) {
		HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Logg.d("Error " + statusCode + " while retrieving bitmap from "
						+ url);
				return null;
			}

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					if (w == -1)
						return BitmapFactory
								.decodeStream(new FlushedInputStream(
										inputStream));
					else
						return getArtworkQuick(new FlushedInputStream(
								inputStream), w, h);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (IOException e) {
			getRequest.abort();
			Logg.d("I/O error while retrieving bitmap from " + url);
		} catch (IllegalStateException e) {
			getRequest.abort();
			Logg.d("Incorrect URL: " + url);
		} catch (Exception e) {
			getRequest.abort();
			Logg.d("Error while retrieving bitmap from " + url);
		} finally {
			if ((client instanceof AndroidHttpClient)) {
				((AndroidHttpClient) client).close();
			}
		}
		return null;
	}

	public static Bitmap getUrlBitmap(String imgurl) {
		return getUrlBitmap(imgurl, -1);
	}

	public static Bitmap getUrlBitmap(String imgurl, int size) {
		Bitmap imgBitmap = null;
		try {
			URL url = new URL(imgurl);
			URLConnection conn = url.openConnection();
			conn.connect();
			// int nSize = conn.getContentLength();
			FlushedInputStream fis = new FlushedInputStream(
					conn.getInputStream());// ,
											// nSize);
			if (size > -1)
				imgBitmap = getArtworkQuick(fis, size, size);
			else
				imgBitmap = BitmapFactory.decodeStream(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imgBitmap;
	}

	public static Bitmap getArtworkQuick(Context context, String filePath,
			int w, int h) {
		BitmapFactory.Options sBitmapOptionsCache = new BitmapFactory.Options();

		w -= 1;
		ContentResolver res = context.getContentResolver();
		ParcelFileDescriptor fd = null;
		try {
			fd = res.openFileDescriptor(
					Uri.parse(ConstVALUE.MEDIA_FILEPATH_PREFIX + filePath),
					"r");
			int sampleSize = 1;

			sBitmapOptionsCache.inJustDecodeBounds = true;

			BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null,
					sBitmapOptionsCache);

			int nextWidth = sBitmapOptionsCache.outWidth >> 1;
			int nextHeight = sBitmapOptionsCache.outHeight >> 1;
			while (nextWidth > w && nextHeight > h) {
				sampleSize <<= 1;
				nextWidth >>= 1;
				nextHeight >>= 1;
			}

			// Logg.d("sampleSize:" + sampleSize);
			sBitmapOptionsCache.inSampleSize = sampleSize;
			sBitmapOptionsCache.inJustDecodeBounds = false;
			Bitmap b = BitmapFactory.decodeFileDescriptor(
					fd.getFileDescriptor(), null, sBitmapOptionsCache);

			if (b != null) {
				if (sBitmapOptionsCache.outWidth != w
						|| sBitmapOptionsCache.outHeight != h) {
					Bitmap tmp = Bitmap.createScaledBitmap(b, w, h, true);
					if (tmp != b)
						b.recycle();
					b = tmp;
				}
			}

			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap getArtworkQuick(InputStream is, int w, int h) {
		BitmapFactory.Options sBitmapOptionsCache = new BitmapFactory.Options();

		// NOTE: There is in fact a 1 pixel border on the right side in the
		// ImageView
		// used to display this drawable. Take it into account now, so we don't
		// have to
		// scale later.
		w -= 1;
		// ContentResolver res = context.getContentResolver();
		// Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
		// if (uri != null) {
		// ParcelFileDescriptor fd = null;
		try {
			// fd = res.openFileDescriptor(uri, "r");
			int sampleSize = 2;

			// Compute the closest power-of-two scale factor
			// and pass that to sBitmapOptionsCache.inSampleSize, which will
			// result in faster decoding and better quality
			sBitmapOptionsCache.inJustDecodeBounds = true;

			// BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null,
			// sBitmapOptionsCache);

			int nextWidth = sBitmapOptionsCache.outWidth >> 1;
			int nextHeight = sBitmapOptionsCache.outHeight >> 1;
			while (nextWidth > w && nextHeight > h) {
				sampleSize <<= 1;
				nextWidth >>= 1;
				nextHeight >>= 1;
			}

			sBitmapOptionsCache.inSampleSize = sampleSize;
			sBitmapOptionsCache.inJustDecodeBounds = false;
			// Bitmap b =
			// BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null,
			// sBitmapOptionsCache);
			Bitmap b = BitmapFactory
					.decodeStream(is, null, sBitmapOptionsCache);

			if (b != null) {
				// finally rescale to exactly the size we need
				if (sBitmapOptionsCache.outWidth != w
						|| sBitmapOptionsCache.outHeight != h) {
					Bitmap tmp = Bitmap.createScaledBitmap(b, w, h, true);
					// Bitmap.createScaledBitmap() can return the same bitmap
					if (tmp != b)
						b.recycle();
					b = tmp;
				}
			}

			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// finally {
		// try {
		// if (fd != null)
		// fd.close();
		// } catch (IOException e) {
		// }
		// }
		// }
		return null;
	}
	
	public static void showDialog(Context context, boolean cancelable, int resId) {
		pd = new ProgressDialog(context);
		pd.setMessage(context.getString(resId));
		pd.setCancelable(cancelable);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
	}
	
	public static void dismissDialog() {
		if (pd != null)
			pd.dismiss();
		
		pd = null;
	}
	
	public static void cancel() {
		if (client != null)
			client.getConnectionManager().shutdown();
		
		client = null;
	}

}
