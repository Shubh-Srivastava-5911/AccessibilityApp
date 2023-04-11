package com.example.messagerecorder;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

public class MyService extends AccessibilityService {

    public static MyService ms;
    public static SharedPreferences spf;
    SharedPreferences.Editor spfedit;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent)
    {
        spf = getApplicationContext().getSharedPreferences("comexamplemessagerecorder", MODE_PRIVATE);
//        if(accessibilityEvent!=null && accessibilityEvent.getEventType()==AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED)
//        {
//            AccessibilityNodeInfo source = accessibilityEvent.getSource();
//            if(source!=null)
//            {
//                String text = source.getText().toString();
//                String app = source.getPackageName().toString();
//
//                // putting things into shared preference
//                spfedit = spf.edit();
//                spfedit.putString(app,text);
//                Toast.makeText(this, app, Toast.LENGTH_SHORT).show();
//                spfedit.apply();
//
//                source.recycle();
//            }
//        }

        String packageName = accessibilityEvent.getPackageName().toString();
        String appLabel,text;
        PackageManager pm = this.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(packageName,0);
            AccessibilityNodeInfo source = accessibilityEvent.getSource();
            appLabel = pm.getApplicationLabel(appInfo).toString();
            text = source.getText().toString();
            spfedit = spf.edit();
            spfedit.putString(appLabel,text);
            spfedit.apply();
            source.recycle();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(this, "interrupt", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        ms = this;

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();

        // Set the type of events that this service wants to listen to. Others
        // won't be passed to this service.
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED;

        // If you only want this service to work with specific applications, set their
        // package names here. Otherwise, when the service is activated, it will listen
        // to events from all applications.
        //info.packageNames = new String[] {"com.example.android.myFirstApp", "com.example.android.mySecondApp"};

        // Set the type of feedback your service will provide.
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;

        // Default services are invoked only if no package-specific ones are present
        // for the type of AccessibilityEvent generated. This service *is*
        // application-specific, so the flag isn't necessary. If this was a
        // general-purpose service, it would be worth considering setting the
        // DEFAULT flag.

        info.flags = AccessibilityServiceInfo.DEFAULT;

        info.notificationTimeout = 100;

        this.setServiceInfo(info);
    }

    public void stopMyService() {
        if(ms!=null)
            ms.disableSelf();
    }
}
