package com.example.messagerecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    MyAdapter myAdapter;
    Button bt,st;
    PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setLogo(R.drawable.accic);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

        bt = findViewById(R.id.buttonstart);
        st = findViewById(R.id.buttonhighp);
        rv = findViewById(R.id.my_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter();
        rv.setAdapter(myAdapter);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.QUERY_ALL_PACKAGES) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_SETTINGS, android.Manifest.permission.QUERY_ALL_PACKAGES, android.Manifest.permission.WAKE_LOCK},1000);
                }

                if(wakeLock==null) {
                    wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyApp:MyWakeLockTag");
                    wakeLock.acquire();
                }

                if (!isAccessibilityServiceEnabled(MainActivity.this,MyService.class)) {
                    Intent serviceIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(serviceIntent);
                } else {
                    Toast.makeText(MainActivity.this, "service running", Toast.LENGTH_SHORT).show();
                }
            }
        });

        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wakeLock!=null) {
                    wakeLock.release();
                }
                if(isAccessibilityServiceEnabled(MainActivity.this,MyService.class)) {
//                    Intent serviceIntent = new Intent(MainActivity.this, MyService.class);
//                    stopService(serviceIntent);
                    MyService.ms.stopMyService();
                }
                if(isAccessibilityServiceEnabled(MainActivity.this,MyService.class)) {
                    Toast.makeText(MainActivity.this, "unable to stop service", Toast.LENGTH_SHORT).show();
                    wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyApp:MyWakeLockTag");
                    wakeLock.acquire();
                }
                Toast.makeText(MainActivity.this, "closing app", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        ArrayList<String> temp = new ArrayList<>();
        temp.add("temp1");
        temp.add("temp2");
        ViewModelForTexts vmft = new ViewModelProvider(this).get(ViewModelForTexts.class);
        myAdapter.setList(vmft.getList(getApplicationContext()));
        //myAdapter.setList(temp);

        SharedPreferences spfmain = this.getSharedPreferences("comexamplemessagerecorder", Context.MODE_PRIVATE);
        myAdapter.setMyOnItemClickListener(new MyAdapter.MyOnItemClickListener() {
            @Override
            public void myOnItemClick(String appname) {
                Dialog dg = new Dialog(MainActivity.this);
                dg.setContentView(R.layout.text_dialog);
                dg.show();
                dg.setCancelable(true);
                TextView t1=dg.findViewById(R.id.textViewappname), t2=dg.findViewById(R.id.textViewtexts);
                t1.setText(appname); t2.setText(spfmain.getString(appname,"---"));
            }
        });
    }

    public boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> service) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN);

        for (AccessibilityServiceInfo enabledService : enabledServices) {
            ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
            if (enabledServiceInfo.packageName.equals(context.getPackageName()) && enabledServiceInfo.name.equals(service.getName()))
                return true;
        }

        return false;
    }
}