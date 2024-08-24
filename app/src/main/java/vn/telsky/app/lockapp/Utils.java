package vn.telsky.app.lockapp;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

public class Utils {
    UsageStatsManager usageStatsManager;
    private final String EXTRA_LAST_APP = "EXTRA_LAST_APP";
    private final String LOCKED_APPS = "LOCKED_APPS";
    private final Context context;

    public Utils(Context context) {
        this.context = context;
    }
    public String getLauncherTopApp() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.RunningTaskInfo> taskInfoList = manager.getRunningTasks(1);
            if (null != taskInfoList && !taskInfoList.isEmpty()) {
                return taskInfoList.get(0).topActivity.getPackageName();
            }
        } else {
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            String result = "";
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);

            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.getPackageName();
                }
            }
            if (!TextUtils.isEmpty(result))
                Log.d("RESULT", result);
            return result;
        }
        return "";
    }

    public boolean isLockApp(String appRunning) {
        return (appRunning.equals("com.android.chrome")
                || appRunning.equals("com.sec.android.app.sbrowse")
                || appRunning.equals("com.sec.android.app.sbrowser.lite")
                || appRunning.equals("com.sec.android.app.samsungapps")
                || appRunning.equals("com.android.browser")
                || appRunning.equals("com.android.deskclock")
                || appRunning.equals("com.sec.android.app.clockpackage")
                || appRunning.equals("com.android.calculator2")
                || appRunning.equals("com.sec.android.app.popupcalculator"));
    }
}
