package src.com.rcp.day;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
	public MyReceiver() {
	
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		 if ("android.alarm.demo.action".equals(intent.getAction())) {
//			 mNotificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			 
	           Log.e("kk", "闹铃时间到了");
	        }
	
	
	
	}
}
