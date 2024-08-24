package vn.telsky.app.lockapp;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ActivityResultLauncher<Intent> requestPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // Xử lý khi người dùng quay lại từ màn hình cài đặt
                Toast.makeText(this, "Cấp quyền thành công!", Toast.LENGTH_SHORT).show();
            }
        });

        /*
        if (!isPermissionGranted(Settings.ACTION_USAGE_ACCESS_SETTINGS)) {
            // Mở màn hình Usage Access Settings
            Intent usageAccessIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            requestPermissionLauncher.launch(usageAccessIntent);
        }

         */

        /*
        if (!isPermissionGranted(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)) {
            ComponentName adminComponent = new ComponentName(this, ReceiverApplock.class);
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Cần quyền này để quản lý thiết bị");
            requestPermissionLauncher.launch(intent);
       }
         */

       // if (!isAccessibilityServiceEnabled(this)) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            requestPermissionLauncher.launch(intent);
       // }


        BackgroundManager.getInstance().init(this).startService();
    }

    private boolean isAccessibilityServiceEnabled(Context context) {
        // Xác định tên dịch vụ theo định dạng "packageName/ServiceName"
        String serviceName = MyAccessibilityService.class.getName();
        String colonSplitter = ":";
        String enabledServices = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        for (String componentName : enabledServices.split(colonSplitter)) {
            if (componentName.contains(serviceName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            }
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
