package com.kdn.mtps.mobile.util.thread;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.CountDownTimer;

import com.kdn.mtps.mobile.net.http.HttpUtil;
import com.kdn.mtps.mobile.util.Logg;

public class ATask {

	public interface OnTask {
		public void onPre();

		public void onBG();

		public void onPost();
	}

	public interface OnPublishProgress {
		public void onPublish(int progress);
	}

	public interface OnTaskPublishProgress {
		public void onPre();

		public void onBG(OnPublishProgress onPublish);

		public void onProgress(int progress);

		public void onPost();
	}

	public interface OnTaskProgress {
		public void onPre();

		public void onBG();

		public void onPost();

		public void onCancel();
	}

	public interface OnTaskResultProgress {
		public void onPre();

		public boolean onBG();

		public void onPost(boolean result);

		public void onCancel();
	}

	public interface OnTaskResultProgressTimer {
		public void onPre();

		public boolean onBG();

		public void onPost(boolean result);

		public void onCancel();

		public void onFinish();
	}

	public static AsyncTask<Void, Void, Void> executeVoid(final OnTask onTask) {
		AsyncTask<Void, Void, Void> aTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected void onPreExecute() {
				onTask.onPre();
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					onTask.onBG();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) 
			{
				onTask.onPost();
			};
		}.execute();
		return aTask;
	}

	public static AsyncTask<Void, Integer, Void> executeVoidPublishProgress(
			final OnTaskPublishProgress onTask) {
		AsyncTask<Void, Integer, Void> aTask = new AsyncTask<Void, Integer, Void>() {
			@Override
			protected void onPreExecute() {
				onTask.onPre();
			}

			@Override
			protected Void doInBackground(Void... params) {
				OnPublishProgress onPublish = new OnPublishProgress() {
					@Override
					public void onPublish(int progress) {
						publishProgress(progress);
					}
				};
				onTask.onBG(onPublish);
				return null;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				onTask.onProgress(values[0]);
			}

			@Override
			protected void onPostExecute(Void result) {
				onTask.onPost();
			};
		}.execute();
		return aTask;
	}

	public static AsyncTask<Void, Void, Void> executeVoidProgress(
			Context context, int progressMsg, boolean cancelable,
			final OnTaskProgress onTask) {
		final ProgressDialog pd = new ProgressDialog(context);
		pd.setMessage(context.getString(progressMsg));
		pd.setCancelable(cancelable);
		pd.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				Logg.d("onCancel 000");
				HttpUtil.cancel();
				onTask.onCancel();
			}
		});

		AsyncTask<Void, Void, Void> aTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected void onPreExecute() {
				pd.show();
				onTask.onPre();
			}

			@Override
			protected Void doInBackground(Void... params) {
				onTask.onBG();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				pd.dismiss();
				onTask.onPost();
			};
		}.execute();
		return aTask;
	}

	public static AsyncTask<Void, Void, Boolean> executeResultProgress(
			Context context, int progressMsg, boolean cancelable,
			final OnTaskResultProgress onTask) {
		final ProgressDialog pd = new ProgressDialog(context);
		pd.setMessage(context.getString(progressMsg));
		pd.setCancelable(cancelable);
		pd.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				onTask.onCancel();
			}
		});

		AsyncTask<Void, Void, Boolean> aTask = new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				pd.show();
				onTask.onPre();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				return onTask.onBG();
			}

			@Override
			protected void onPostExecute(Boolean result) {
				pd.dismiss();
				onTask.onPost(result);
			};
		}.execute();
		return aTask;
	}

	public static AsyncTask<Void, Void, Boolean> executeResultProgressTimer(
			Context context, int progressMsg, boolean cancelable,
			final int timeout, final OnTaskResultProgressTimer onTask) {
		final ProgressDialog pd = new ProgressDialog(context);
		pd.setMessage(context.getString(progressMsg));
		pd.setCancelable(cancelable);

		final AsyncTask<Void, Void, Boolean> aTask = new AsyncTask<Void, Void, Boolean>() {
			CountDownTimer cdt = null;

			@Override
			protected void onPreExecute() {
				pd.show();
				onTask.onPre();

				final AsyncTask<Void, Void, Boolean> aTask = this;
				cdt = new CountDownTimer(timeout * 1000, 1000) {
					@Override
					public void onTick(long millisUntilFinished) {
					}

					@Override
					public void onFinish() {
						aTask.cancel(true);
						pd.dismiss();
						onTask.onFinish();
					}
				}.start();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				return onTask.onBG();
			}

			@Override
			protected void onPostExecute(Boolean result) {
				cdt.cancel();
				pd.dismiss();
				onTask.onPost(result);
			};
		}.execute();

		pd.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				onTask.onCancel();
				aTask.cancel(true);
				pd.dismiss();
			}
		});

		return aTask;
	}

	public interface OnTaskResult {
		public void onPre();

		public boolean onBG();

		public void onPost(boolean result);
	}

	public static AsyncTask<Void, Void, Boolean> executeResult(
			final OnTaskResult onTask) {
		AsyncTask<Void, Void, Boolean> aTask = new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				onTask.onPre();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				return onTask.onBG();
			}

			@Override
			protected void onPostExecute(Boolean result) {
				onTask.onPost(result);
			};
		}.execute();
		return aTask;
	}

	@SuppressWarnings("rawtypes")
	public interface OnTaskList {
		public void onPre();

		public List onBG();

		public void onPost(List result);

		public void onCancel();
	}

	@SuppressWarnings("rawtypes")
	public static AsyncTask<Void, Void, List> executeList(
			final OnTaskList onTask) {

		AsyncTask<Void, Void, List> aTask = new AsyncTask<Void, Void, List>() {
			@Override
			protected void onPreExecute() {
				onTask.onPre();
			}

			@Override
			protected List doInBackground(Void... params) {
				return onTask.onBG();
			}

			@Override
			protected void onPostExecute(List result) {
				try {
					onTask.onPost(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.execute();
		return aTask;
	}

	@SuppressWarnings("rawtypes")
	public static AsyncTask<Void, Void, List> executeListProgress(
			Context context, int progressMsg, boolean cancelable,
			final OnTaskList onTask) {
		final ProgressDialog pd = new ProgressDialog(context);
		pd.setMessage(context.getString(progressMsg));
		pd.setCancelable(cancelable);
		pd.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				onTask.onCancel();
			}
		});

		AsyncTask<Void, Void, List> aTask = new AsyncTask<Void, Void, List>() {
			@Override
			protected void onPreExecute() {
				if (pd != null)
					pd.show();
				onTask.onPre();
			}

			@Override
			protected List doInBackground(Void... params) {
				return onTask.onBG();
			}

			@Override
			protected void onPostExecute(List result) {
				try {
					if (pd != null)
						pd.dismiss();
					onTask.onPost(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.execute();
		return aTask;
	}

	public interface OnTaskBitmap {
		public void onPre();

		public Bitmap onBG();

		public void onPost(Bitmap bm);
	}

	public static AsyncTask<Void, Void, Bitmap> executeResult(
			final OnTaskBitmap onTask) {
		AsyncTask<Void, Void, Bitmap> aTask = new AsyncTask<Void, Void, Bitmap>() {
			@Override
			protected void onPreExecute() {
				onTask.onPre();
			}

			@Override
			protected Bitmap doInBackground(Void... params) {
				return onTask.onBG();
			}

			@Override
			protected void onPostExecute(Bitmap bm) {
				onTask.onPost(bm);
			};
		}.execute();
		return aTask;
	}
}
