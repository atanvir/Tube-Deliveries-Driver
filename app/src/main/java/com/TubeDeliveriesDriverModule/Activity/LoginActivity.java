package com.TubeDeliveriesDriverModule.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.TubeDeliveriesDriverModule.Model.CommonModel;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesConnection;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesInterface;
import com.TubeDeliveriesDriverModule.SharePrefrences.SPreferenceKey;
import com.TubeDeliveriesDriverModule.SharePrefrences.SPrefrenceValues;
import com.TubeDeliveriesDriverModule.SharePrefrences.SharedPreferenceWriter;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;
import com.TubeDeliveriesDriverModule.Utils.LocationClass;
import com.TubeDeliveriesDriverModule.Utils.ParamEnum;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    TextView tv_Sign_Up,tv_forget_password;
    EditText ed_login_email,ed_login_password;
    private LocationClass locationClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin=findViewById(R.id.btnLogin);
        tv_Sign_Up=findViewById(R.id.tv_Sign_Up);
        ed_login_email=findViewById(R.id.ed_login_email);
        tv_forget_password=findViewById(R.id.tv_forget_password);
        ed_login_password=findViewById(R.id.ed_login_password);
        tv_Sign_Up.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        locationClass = new LocationClass(this);
        locationClass.getUpdateLocationListener();

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_Sign_Up:
            CommonUtils.startActivity(this, SignUpActivity.class);
            break;

            case R.id.btnLogin:
            if(checkValidation()) loginApi();
            break;

            case R.id.tv_forget_password:
            CommonUtils.startActivity(this, ForgotPasswordActivity.class);
            break;
        }
    }


    private void loginApi() {
            String type = "";
            if (ed_login_email.getText().toString().matches(CommonUtils.emailPattern))
                type = "email";
            else
                type = "phone";

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService(this);
                Call<CommonModel> loginCall = servicesInterface.driver_login(type, ed_login_email.getText().toString().trim(),
                        ed_login_email.getText().toString().trim(), ed_login_password.getText().toString(),
                        ParamEnum.ANDROID.theValue(), SharedPreferenceWriter.getInstance(LoginActivity.this).getString(SPreferenceKey.TOKEN), locationClass.getLatitude() + "", locationClass.getLongitude() + "");

                ServicesConnection.getInstance().enqueueWithoutRetry(loginCall, this, true, new Callback<CommonModel>() {
                    @Override
                    public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                        if (response.body() != null) {
                            if (response.body().getStatus()) {
                                SPrefrenceValues.saveUserData(LoginActivity.this, response.body());
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                CommonUtils.showSnackBar(LoginActivity.this, response.body().getMessage());
                            }


                        } else if (response.errorBody() != null) {
                            CommonUtils.errorResponse(LoginActivity.this, response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonModel> call, Throwable t) {
                        Log.e("failure", t.getMessage());

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
        }
    }

    private boolean checkValidation() {
        boolean ret=true;

        if(ed_login_email.getText().toString().trim().length()==0)
        {
            ret=false;
            if(ed_login_email.getText().toString().trim().length()==0) CommonUtils.showSnackBar(this,getString(R.string.enter_email_id));
        }else if(!(ed_login_password.getText().toString().trim().length()>0))
        {
            ret=false;
            CommonUtils.showSnackBar(this,"Please enter password");
        }

        return ret;
    }
}