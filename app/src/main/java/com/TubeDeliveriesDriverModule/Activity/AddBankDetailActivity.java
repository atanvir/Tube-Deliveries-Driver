package com.TubeDeliveriesDriverModule.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.TubeDeliveriesDriverModule.Model.CommonModel;
import com.TubeDeliveriesDriverModule.Model.SignupModel;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesConnection;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesInterface;
import com.TubeDeliveriesDriverModule.Utils.AddRequestBody;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBankDetailActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner spnBank;
    TextInputEditText edBank,edAccountHolderName,edAccountNumber,edIBN;
    private String bankDetails[]={"Please select bank","Capitec Bank","FNB","Nedbank","Standard Bank","ABSA","TYME Bank"};
    Button btnSubmit;
    private SignupModel stepTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_detail);
        stepTwo=getIntent().getParcelableExtra("stepTwo");
        spnBank=findViewById(R.id.spnBank);
        edBank=findViewById(R.id.edBank);
        edAccountHolderName=findViewById(R.id.edAccountHolderName);
        edAccountNumber=findViewById(R.id.edAccountNumber);
        btnSubmit=findViewById(R.id.btnSubmit);
        edIBN=findViewById(R.id.edIBN);
        btnSubmit.setOnClickListener(this);
        spnBank.setOnItemSelectedListener(this);
        edBank.setOnClickListener(this);


        if(getIntent()!=null) {
            if (getIntent().getStringExtra("bank_type") != null) {
                edBank.setText(getIntent().getStringExtra("bank_type"));
            }
            if (getIntent().getStringExtra("name") != null) {
                edAccountHolderName.setText(getIntent().getStringExtra("name"));
            }
            if (getIntent().getStringExtra("account_number") != null) {
                edAccountNumber.setText(getIntent().getStringExtra("account_number"));
            }
            if (getIntent().getStringExtra("iban") != null) {
                edIBN.setText(getIntent().getStringExtra("iban"));
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.setToolbar(this,"Add bank Details");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSubmit:
//            stepTwo = new SignupModel();
//            stepTwo.setAccount_number("4172001500066747");
//            stepTwo.setBank_type("State Bank of India");
//            stepTwo.setCity("New Delhi");
//            stepTwo.setCountry_code("+91");
//            stepTwo.setDevice_token("ebA5bx3-QxeqpY0MYpL6az:APA91bFf9Hspz5ztgt2jKZ2Q8wHpwT5_sGS9JYAFJ8dFlZi2CgQhDB8PjA0b_1Xn74Etop0hMARNQUP0GDimzXBm1oZfKLXALvdTo4fov_uIIPOXA086XB11Jiqgxn8oPYShaHQ9Rygf");
//            stepTwo.setDevice_type("Android");
//            stepTwo.setDriver_image("/storage/emulated/0/DCIM/Camera/IMG_20200715_121519_1.jpg");
//            stepTwo.setEmail("rahularya@gmail.com");
//            stepTwo.setFirst_name("TANVIR");
//            stepTwo.setIban("PUNB0417200");
//            stepTwo.setInsurance_number("112232323");
//            stepTwo.setInsurance_image("/storage/emulated/0/DCIM/Camera/IMG_20200623_162014.jpg");
//            stepTwo.setLast_name("AHMED");
//            stepTwo.setLatitude(28.6361763);
//            stepTwo.setLongitude(77.037824);
//            stepTwo.setLicense_image("/storage/emulated/0/DCIM/Camera/IMG_20200623_162014.jpg");
//            stepTwo.setLicense_number("132223");
//            stepTwo.setModal("CHRYSLER");
//            stepTwo.setName("name");
//            stepTwo.setPassword("qwerty");
//            stepTwo.setPhone_no("8285403950");
//            stepTwo.setState("India");
//            stepTwo.setType("Bike");
//            List<String> list = new ArrayList<>();
//            list.add("/storage/emulated/0/DCIM/Camera/IMG_20200620_023245.jpg");
//            stepTwo.setVehicle_images(list);
//            stepTwo.setVehicle_number("122345678");
//            driverRegisterApi();
            if(checkValidation()) stepThreeApi();
            break;

            case R.id.edBank:
            CommonUtils.setSpinner(this, Arrays.asList(bankDetails), spnBank);
            break;

        }
    }

    private void stepThreeApi() {
        CommonUtils.showLoadingDialog(this);
        try {
            ServicesInterface apiInterface= ServicesConnection.getInstance().createService(this);
            Call<CommonModel> call=apiInterface.stepThreeApi(getUserData());
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false,new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.body()!=null){
                        if(response.body().getStatus())
                        {

                          if(getIntent()!=null)
                          {
                              if(getIntent().getStringExtra("iban")!=null && getIntent().getStringExtra("bank_type")!=null)
                              {
                                  CommonUtils.dismissLoadingDialog();
                                  Intent intent= new Intent();
                                  intent.putExtra("bank_type", edBank.getText().toString().trim());
                                  intent.putExtra("name", edAccountHolderName.getText().toString().trim());
                                  intent.putExtra("account_number", edAccountNumber.getText().toString().trim());
                                  intent.putExtra("iban", edIBN.getText().toString().trim());
                                  setResult(RESULT_OK,intent);
                                  finish();
                              }else
                              {
                                  stepTwo.setBank_type(edBank.getText().toString().trim());
                                  stepTwo.setName(edAccountHolderName.getText().toString().trim());
                                  stepTwo.setAccount_number(edAccountNumber.getText().toString().trim());
                                  stepTwo.setIban(edIBN.getText().toString().trim());
                                  driverRegisterApi();

                              }
                          }else
                          {
                              stepTwo.setBank_type(edBank.getText().toString().trim());
                              stepTwo.setName(edAccountHolderName.getText().toString().trim());
                              stepTwo.setAccount_number(edAccountNumber.getText().toString().trim());
                              stepTwo.setIban(edIBN.getText().toString().trim());
                              driverRegisterApi();

                          }


                        }else
                        {
                            CommonUtils.dismissLoadingDialog();
                            CommonUtils.showSnackBar(AddBankDetailActivity.this,response.body().getMessage());
                        }


                    }else if(response.errorBody()!=null)
                    {
                        CommonUtils.dismissLoadingDialog();
                        CommonUtils.errorResponse(AddBankDetailActivity.this,response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                    Log.e("failure", t.getMessage());

                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void driverRegisterApi() {
        try {
            ServicesInterface apiInterface= ServicesConnection.getInstance().createService(this);
            Call<CommonModel> call=apiInterface.driver_register(getDriverImage(),
                                                                getImages("vehicle_images",stepTwo.getVehicle_images()),
                                                                getSingleImages("license_image",stepTwo.getLicense_image()),
                                                                getSingleImages("insurance_image",stepTwo.getInsurance_image()),getData());
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false,new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    CommonUtils.dismissLoadingDialog();
                    if(response.body()!=null){
                        if(response.body().getStatus())
                        {
                            showCongratsDialog();
                        }else
                        {
                            CommonUtils.showSnackBar(AddBankDetailActivity.this,response.body().getMessage());
                        }
                    }else if(response.errorBody()!=null)
                    {
                        CommonUtils.errorResponse(AddBankDetailActivity.this,response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                    Log.e("failure", t.getMessage());

                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public  void showCongratsDialog() {
        final Dialog dialog = new Dialog(AddBankDetailActivity.this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_signup_complete);
        dialog.setCancelable(true);
        TextView tvOk=dialog.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CommonUtils.startActivity(AddBankDetailActivity.this, LoginActivity.class);
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                CommonUtils.startActivity(AddBankDetailActivity.this, LoginActivity.class);

            }
        });

        dialog.show();

    }


    private Map<String, RequestBody> getData() {
        AddRequestBody body = new AddRequestBody(stepTwo,"Bank Details");
        return body.getBody();
    }

    private MultipartBody.Part getSingleImages(String key,String image) throws IOException {
        MultipartBody.Part part =null;
        if(image!=null)
        {
            File file =new File(image);
            RequestBody body=RequestBody.create(MediaType.parse("" + "*/*"),new Compressor(this).compressToFile(file));
            part=MultipartBody.Part.createFormData(key, file.getName(),body);
        }

        return part;
    }

    private MultipartBody.Part[] getImages(String key,List<String> imgList) throws IOException {
        RequestBody imagesBody=null;
        MultipartBody.Part imgpart[]=new MultipartBody.Part[imgList.size()];

        for(int i=0;i<imgList.size();i++)
        {
            File imgFile=new File(imgList.get(i));
            imagesBody=RequestBody.create(MediaType.parse("*/*"),new Compressor(this).compressToFile(imgFile));
            imgpart[i]=MultipartBody.Part.createFormData(key+"["+i+"]",imgFile.getName(),imagesBody);

        }

        return imgpart;
    }

    private MultipartBody.Part getDriverImage() throws IOException {
        MultipartBody.Part part =null;
        if(stepTwo.getDriver_image()!=null)
        {
            File file =new File(stepTwo.getDriver_image());
            RequestBody body=RequestBody.create(MediaType.parse("" + "*/*"),new Compressor(this).compressToFile(file));
            part=MultipartBody.Part.createFormData("driver_image", file.getName(),body);
        }

        return part;
    }
    private SignupModel getUserData() {
        SignupModel model = new SignupModel();
        model.setBank_type(edBank.getText().toString().trim());
        model.setName(edAccountHolderName.getText().toString().trim());
        model.setAccount_number(edAccountNumber.getText().toString().trim());
        model.setIban(edIBN.getText().toString().trim());
        return model;
    }

    private boolean checkValidation() {
        boolean ret=true;
        if(edBank.getText().toString().equalsIgnoreCase(""))
        {
            ret=false;
            CommonUtils.showSnackBar(this,"Please select bank");
        }
        else if(edAccountHolderName.getText().toString().trim().equalsIgnoreCase(""))
        {
            ret=false;
            CommonUtils.showSnackBar(this,"Please enter account holder name");
        }else if(edAccountNumber.getText().toString().trim().equalsIgnoreCase(""))
        {
            ret=false;
            CommonUtils.showSnackBar(this,"Please enter account number");
        }else if(edIBN.getText().toString().trim().equalsIgnoreCase(""))
        {
            ret=false;
            CommonUtils.showSnackBar(this,"Please enter branch code");
        }


        return ret;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId())
        {
            case R.id.spnBank:
            if(position!=-1)
            {
                edBank.setText(bankDetails[position]);
            }
            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}