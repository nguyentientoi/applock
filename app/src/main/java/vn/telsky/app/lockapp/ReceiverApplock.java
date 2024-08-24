package vn.telsky.app.lockapp;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ReceiverApplock extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils utils = new Utils(context);
        String appRunning = utils.getLauncherTopApp();
     //   Toast.makeText(context, "App package: " + appRunning, Toast.LENGTH_SHORT).show();
        if (utils.isLockApp(appRunning)) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(startMain);
            activityManager.killBackgroundProcesses(appRunning);

            Intent i = new Intent(context, ScreenBlocker.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra("broadcast_receiver", "broadcast_receiver");
            context.startActivity(i);
        }
    }
}
