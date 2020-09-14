package com.TubeDeliveriesDriverModule.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.TubeDeliveriesDriverModule.Model.CommonModel;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesConnection;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesInterface;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    TextView bt_forgot;
    EditText ed_forgot_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        bt_forgot=findViewById(R.id.bt_forgot);
        ed_forgot_email=findViewById(R.id.ed_forgot_email);
        bt_forgot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
           case R.id.bt_forgot:
           CommonUtils.hideKeyBoard(this, bt_forgot);
           if(checkValidation())
               forgetPassApi();
           break;
        }
    }

    private boolean checkValidation() {
        boolean ret=true;
        if(ed_forgot_email.getText().toString().trim().length()==0 || !Patterns.EMAIL_ADDRESS.matcher(ed_forgot_email.getText().toString()).matches())
        {
            ret=false;
            if(ed_forgot_email.getText().toString().trim().length()==0) CommonUtils.showSnackBar(this,getString(R.string.enter_email_id));
            else if(!Patterns.EMAIL_ADDRESS.matcher(ed_forgot_email.getText().toString()).matches()) CommonUtils.showSnackBar(this,getString(R.string.enter_valid_email_id));
        }

        return ret;
    }

    public void forgetPassApi() {

            try {
                ServicesInterface servicesInterface = ServicesConnection.getInstance().createService(this);
                Call<CommonModel> loginCall = servicesInterface.forgetPass(ed_forgot_email.getText().toString());
                ServicesConnection.getInstance().enqueueWithoutRetry(loginCall, this, true, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if (response.isSuccessful()) {
                        CommonModel bean = ((CommonModel) response.body());
                        if (bean.getStatus()) {
                                Toast.makeText(ForgotPasswordActivity.this, "Your password has been sent to your email id", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                        } else {
                            CommonUtils.showSnackBar(ForgotPasswordActivity.this, bean.getMessage());
                        }
                    }

                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                }
            });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.setToolbar(this,"Forgot Password");
    }

}