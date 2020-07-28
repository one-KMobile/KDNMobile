package com.kdn.mtps.mobile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kdn.mtps.mobile.service.KDNService;

public class KDNReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		String strAction = intent.getAction();
		
		if (strAction.equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED) ) {
			context.startService(new Intent(context, KDNService.class));
		}
	}

}
