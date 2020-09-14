package com.TubeDeliveriesDriverModule.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.TubeDeliveriesDriverModule.Activity.LoginActivity;
import com.TubeDeliveriesDriverModule.Activity.MainActivity;
import com.TubeDeliveriesDriverModule.Model.CommonModel;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesConnection;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesInterface;
import com.TubeDeliveriesDriverModule.SharePrefrences.SPreferenceKey;
import com.TubeDeliveriesDriverModule.SharePrefrences.SPrefrenceValues;
import com.TubeDeliveriesDriverModule.SharePrefrences.SharedPreferenceWriter;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;
import com.TubeDeliveriesDriverModule.Utils.ParamEnum;
import com.TubeDeliveriesDriverModule.Utils.SwitchFragment;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment implements View.OnClickListener {
    TextInputEditText edtCurrentPass,edtNewPass,edtConfirmPass;
    ConstraintLayout clChangePass;
    TextView tvChangePass;
    Button btnSaveSettings;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_setting, container, false);
        tvChangePass=view.findViewById(R.id.tvChangePass);
        clChangePass=view.findViewById(R.id.clChangePass);
        edtNewPass=view.findViewById(R.id.edtNewPass);
        edtCurrentPass=view.findViewById(R.id.edtCurrentPass);
        edtConfirmPass=view.findViewById(R.id.edtConfirmPass);
        btnSaveSettings=view.findViewById(R.id.btnSaveSettings);
        btnSaveSettings.setOnClickListener(this);
        tvChangePass.setOnClickListener(this);
        return view;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvChangePass:
            changePasswordVisibility();
            break;

            case R.id.btnSaveSettings:
            if(validation()) {updatePasswordApi();}
            break;
        }
    }

    private void updatePasswordApi() {
        try {
            ServicesInterface servicesInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonModel> loginCall = servicesInterface.driverPassword(edtCurrentPass.getText().toString().trim(),edtNewPass.getText().toString().trim());

            ServicesConnection.getInstance().enqueueWithoutRetry(loginCall, getActivity(), true, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if (response.body() != null) {
                        if (response.body().getStatus()) {
                            Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            SwitchFragment.changeFragment(getActivity().getSupportFragmentManager(), new HomeFragment(), false, true);
                        } else {
                            CommonUtils.showSnackBar(getActivity(),response.body().getMessage());


                        }


                    } else if (response.errorBody() != null) {
                        CommonUtils.errorResponse(getActivity(), response.errorBody());
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


    private boolean validation(){
        if(edtCurrentPass.getText().toString().length()==0)
        {
            CommonUtils.showSnackBar(getActivity(),"Please enter old password");
            return false;
        }
        if(!edtNewPass.getText().toString().matches(edtConfirmPass.getText().toString())){
            CommonUtils.showSnackBar(getActivity(),"New Password and Confirm Password do not match");
            return false;
        }
        return true;
    }




    private void changePasswordVisibility() {
        if(clChangePass.getVisibility()==View.GONE){
            clChangePass.setVisibility(View.VISIBLE);
            tvChangePass.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.drop_down_ic,0);
        }else {
            clChangePass.setVisibility(View.GONE);
            tvChangePass.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.drop_dwn,0);
        }
    }
}