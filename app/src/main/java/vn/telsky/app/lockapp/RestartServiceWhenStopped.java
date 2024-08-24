package vn.telsky.app.lockapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class RestartServiceWhenStopped extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Khởi động dịch vụ khi thiết bị khởi động
            Intent serviceIntent = new Intent(context, MyAccessibilityService.class);
            context.startForegroundService(serviceIntent); // Sử dụng startForegroundService() cho Android O và cao hơn
        }
    }
}
