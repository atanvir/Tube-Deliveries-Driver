package com.TubeDeliveriesDriverModule.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.TubeDeliveriesDriverModule.Firebase.MyFirebaseMessageService;
import com.TubeDeliveriesDriverModule.Firebase.NotificationHelper;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.SharePrefrences.SPreferenceKey;
import com.TubeDeliveriesDriverModule.SharePrefrences.SharedPreferenceWriter;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;
import com.google.firebase.messaging.FirebaseMessagingService;

public class SplashActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        CommonUtils.getDeviceToken(SplashActivity.this);
        if(checkPermissions()) intentCall();
        else intentCall();
   }

    private void intentCall() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPreferenceWriter.getInstance(SplashActivity.this).getBoolean(SPreferenceKey.IS_LOGIN)) {
                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                     if(getIntent().getStringExtra("body")!=null)
                     {
                         intent.putExtra("cameFrom", MyFirebaseMessageService.class.getSimpleName());
                     }
                     intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                     startActivity(intent);
                }
                else CommonUtils.startActivity(SplashActivity.this, LoginActivity.class);
            }
        }, 2000);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length >0) {
            switch (requestCode) {
                case PERMISSION_REQUEST_CODE:
                    boolean isPermissionDenied=false;
                    for(int i=0;i<grantResults.length;i++)
                    {
                        if(grantResults[i]==PackageManager.PERMISSION_DENIED)
                        {
                            isPermissionDenied=true;
                            break;
                        }
                    }

                    if(isPermissionDenied) Toast.makeText(this, "Please accept permissions due to security purpose", Toast.LENGTH_SHORT).show();
                    else intentCall();
                    break;
            }
        }
    }

}