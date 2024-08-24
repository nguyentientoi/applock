package vn.telsky.app.lockapp;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class MyAccessibilityService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String packageName = event.getPackageName().toString();
          //  Toast.makeText(this, "packageName: " + packageName, Toast.LENGTH_SHORT).show();
            // Kiểm tra và đóng ứng dụng nếu cần
            if (isLockApp(packageName)) {
                performGlobalAction(GLOBAL_ACTION_HOME); // Chuyển về màn hình chính
                // Có thể thêm code để dừng ứng dụng nếu cần
            }
        }
    }

    @Override
    public void onInterrupt() {
    }

    public boolean isLockApp(String appRunning) {
        return (appRunning.equals("com.android.chrome")
                || appRunning.equals("com.sec.android.app.sbrowse")
                || appRunning.equals("com.sec.android.app.sbrowser.lite")
                || appRunning.equals("com.sec.android.app.samsungapps")
                || appRunning.equals("com.android.browser")
                || appRunning.equals("com.android.vending"));
    }
}
