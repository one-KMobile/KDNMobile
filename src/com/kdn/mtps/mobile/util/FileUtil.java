package com.kdn.mtps.mobile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import com.kdn.mtps.mobile.net.http.HttpUtil;
import com.kdn.mtps.mobile.util.thread.ATask;

public class FileUtil {
	private static final String TAG_NAME = "FileUtil";
	
	public static String getFileExtension(File f) {
		int idx = f.getName().lastIndexOf(".");
		if (idx == -1)
			return "";
		else
			return f.getName().substring(idx + 1);
	}

	public static String remExt(String fileName) {
		if (fileName == null)
			return null;
		int idx = fileName.lastIndexOf(".");
		if (idx == -1)
			return fileName;
		else
			return fileName.substring(0, idx);
	}

	public static String stringCheck(String str) {
		StringBuilder strbuilder = new StringBuilder();
		int size = str.length();
		for (int i = 0; i < size; i++) {
			char curChar = str.charAt(i);
			if (curChar == '\\' || curChar == '/' || curChar == ':' || curChar == '*' || curChar == '?' || curChar == '"' || curChar == '<'
					|| curChar == '>' || curChar == '|') {
				strbuilder.append('_');
			} else
				strbuilder.append(curChar);
		}
		return strbuilder.toString();
	}

	public static String getUniqueFilename(File folder, String filename, String ext) {
		if (folder == null || filename == null)
			return null;
		String curFileName;
		File curFile;
		if (filename.length() > 20) {
			filename = filename.substring(0, 19);
		}
		filename = stringCheck(filename);
		int i = 1;
		do {
			curFileName = String.format("%s_%02d.%s", filename, i++, ext);
			curFile = new File(folder, curFileName);
		} while (curFile.exists());
		return curFileName;
	}

	public static byte[] readBytedata(String aFilename) {
		byte[] imgBuffer = null;
		FileInputStream fileInputStream = null;
		try {
			File file = new File(aFilename);
			fileInputStream = new FileInputStream(file);
			int byteSize = (int) file.length();
			imgBuffer = new byte[byteSize];
			if (fileInputStream.read(imgBuffer) == -1) {
				Logg.d("failed to read image");
			}
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return imgBuffer;
	}

	public static boolean writeBytedata(String aFilename, byte[] imgBuffer) {
		FileOutputStream fileOutputStream = null;
		boolean result = true;
		try {
			File file = new File(aFilename);
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(imgBuffer);
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			result = false;
		} catch (IOException e2) {
			e2.printStackTrace();
			result = false;
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					result = false;
				}
			}
		}
		return result;
	}

	public interface OnOK {
		public void onOk();
	}

	public static void copyAssets(boolean isThread, final Context context, int pdMsg, final String srcAssets, final String tgtDir, final OnOK onOk) {
		// Logg.d("srcAssets:" + srcAssets + ",tgtDir:" + tgtDir);

		File targetDir = new File(tgtDir);
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}

		if (isThread) {
			try
			{
				ATask.executeVoidProgress(context, pdMsg, false, new ATask.OnTaskProgress() {
					public void onPre() {
					}

					public void onBG() {
						try {
							AssetManager assetManager = context.getAssets();
							copyAssetsFileOrDir(assetManager, srcAssets, tgtDir);
							if (onOk != null)
								onOk.onOk();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					public void onPost() {
					}

					public void onCancel() {
					}
				});
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		} else {
			try {
				AssetManager assetManager = context.getAssets();
				copyAssetsFileOrDir(assetManager, srcAssets, tgtDir);
				if (onOk != null)
					onOk.onOk();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static void copyAssetsFileOrDir(AssetManager assetManager, String srcAssets, String tgtDir) throws Exception {
		String[] files = assetManager.list(srcAssets);
		if (files.length == 0) {
			copyAssetsFile(assetManager, srcAssets, tgtDir);
		} else {
			File dir = new File(tgtDir);
			if (!dir.exists())
				dir.mkdir();
			for (int i = 0; i < files.length; ++i) {
				copyAssetsFileOrDir(assetManager, srcAssets + "/" + files[i], tgtDir + "/" + files[i]);
			}
		}
	}

	static void copyAssetsFile(AssetManager assetManager, String srcAssets, String tgtDir) throws Exception {
		Logg.d("srcAssets:" + srcAssets + ",tgtDir:" + tgtDir);
		InputStream in = null;
		OutputStream out = null;
		in = assetManager.open(srcAssets);
		out = new FileOutputStream(tgtDir);
		copyFile(in, out);
		in.close();
		in = null;
		out.flush();
		out.close();
		out = null;
	}

	/**
	 * sdcard에서 file 간 복사
	 * 
	 * @param srcFile
	 * @param tgtDir
	 * @return
	 */

	static void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[in.available()];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	public static void saveBitmap(Bitmap bm, String filePath) {
		try {
			File file = new File(filePath);
			file.getParentFile().mkdirs();
			FileOutputStream fos = new FileOutputStream(file);
			bm.compress(CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Bitmap loadBitmap(String filePath) {
		Bitmap bm = null;
		try {
			bm = BitmapFactory.decodeFile(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bm;
	}

	public static Bitmap loadBitmap(Context context, String filePath, int w, int h) {
		Bitmap bm = null;

		try {
			if (w == -1) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				Bitmap src = BitmapFactory.decodeFile(filePath, options);
				bm = Bitmap.createScaledBitmap(src, w, h, true);
			} else
				bm = HttpUtil.getArtworkQuick(context, filePath, w, h);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bm;
	}

	public boolean saveBitmapPNG(String strFileName, Bitmap bitmap) {
		if (strFileName == null || bitmap == null)
			return false;

		boolean bSuccess1 = false;
		boolean bSuccess2;
		boolean bSuccess3;
		File saveFile = new File(strFileName);

		if (saveFile.exists()) {
			if (!saveFile.delete())
				return false;
		}

		try {
			bSuccess1 = saveFile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		OutputStream out = null;
		try {
			out = new FileOutputStream(saveFile);
			bSuccess2 = bitmap.compress(CompressFormat.PNG, 100, out);
		} catch (Exception e) {
			e.printStackTrace();
			bSuccess2 = false;
		}
		try {
			if (out != null) {
				out.flush();
				out.close();
				bSuccess3 = true;
			} else
				bSuccess3 = false;
		} catch (IOException e) {
			e.printStackTrace();
			bSuccess3 = false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return (bSuccess1 && bSuccess2 && bSuccess3);
	}

	public static String getNewWriteFilePath(String dirPath, String ext, String id) {
		File mFolder = new File(dirPath);
		if (!(mFolder.exists()))
			mFolder.mkdirs();
		String savePath = mFolder.getPath() + '/' + id + "." + ext;// FileUtil.getUniqueFilename(mFolder,
		// "image",
		// ext);
		File saveFile = new File(savePath);
		if (saveFile.exists()) {
			saveFile.delete();
		}
		return savePath;
	}

	public static String getNewFilePath(String dirPath, String ext, String id) {
		File mFolder = new File(dirPath);
		if (!(mFolder.exists()))
			mFolder.mkdirs();

		String savePath = mFolder.getPath() + '/' + id + "." + ext;
		String movePath = mFolder.getPath() + '/' + FileUtil.getUniqueFilename(mFolder, id, ext);

		File saveFile = new File(savePath);
		if (saveFile.exists()) {
			saveFile.renameTo(new File(movePath));
		}
		return savePath;
	}

}
