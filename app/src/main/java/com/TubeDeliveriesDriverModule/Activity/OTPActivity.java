package com.TubeDeliveriesDriverModule.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.TubeDeliveriesDriverModule.Model.ResponseBean;
import com.TubeDeliveriesDriverModule.Model.SignupModel;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;

import com.chaos.view.PinView;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener, OnCompleteListener<AuthResult> {
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ImageView clVerification;
    private Pinview pinview;
    private SignupModel stepOneResponse;
    private String verificationCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        clVerification=findViewById(R.id.clVerification);
        pinview=findViewById(R.id.pinview);
        clVerification.setOnClickListener(this);
        stepOneResponse=getIntent().getParcelableExtra("stepOne");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(stepOneResponse.getCountry_code() + stepOneResponse.getPhone_no(), 60, TimeUnit.SECONDS, this, mCallback);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(OTPActivity.this);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.e("failed",e.getMessage());
            Toast.makeText(OTPActivity.this, "Number verification failed!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCode = s;
            mResendToken = forceResendingToken;
            Log.e("OTP Code", s);
        }
    };



    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.setToolbar(this, "Verification");
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clVerification:
            if(!verificationCode.equalsIgnoreCase("")) {
                if(pinview.getValue().toString().length()==6) {
                    verifyVerificationCode(verificationCode);
                }else
                {
                    CommonUtils.showSnackBar(this,"Please enter six digit otp");
                }
            }else
            {
                CommonUtils.showSnackBar(this, "Please wait for the otp");
            }
            break;

            case R.id.tvResendOtp:
            PhoneAuthProvider.getInstance().verifyPhoneNumber(stepOneResponse.getCountry_code() + stepOneResponse.getPhone_no(), 60, TimeUnit.SECONDS, this, mCallback);
            break;
        }
    }

    private void verifyVerificationCode(String verificationCode) {
        CommonUtils.showLoadingDialog(this);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, pinview.getValue().toString());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            Intent intent= new Intent(OTPActivity.this, DriverRegistrationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("stepOne", stepOneResponse);
            startActivity(intent);
        } else {
            Toast.makeText(OTPActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
            CommonUtils.dismissLoadingDialog();
        }
    }
}