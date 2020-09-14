package com.TubeDeliveriesDriverModule.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.TubeDeliveriesDriverModule.Adaptor.CommonImageAdpter;
import com.TubeDeliveriesDriverModule.BuildConfig;
import com.TubeDeliveriesDriverModule.Model.CommonModel;
import com.TubeDeliveriesDriverModule.Model.SignupModel;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesConnection;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesInterface;
import com.TubeDeliveriesDriverModule.Utils.AddRequestBody;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;
import com.TubeDeliveriesDriverModule.Utils.FilePath;
import com.TubeDeliveriesDriverModule.Utils.ImageGlider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverRegistrationActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, CommonImageAdpter.onImageClick {
    private CircleImageView ivProfilePic;
    private ProgressBar progressBar;
    private TextInputEditText edVehicletype,edBrand,edVehicleNumber,edLicenseNumber,edInsuranceNumber;
    private Spinner spnBrand,spnVehicle;
    private ConstraintLayout clVehchileImage,clLicenseImage,clVehchileInsuranceImage;
    private RecyclerView rvVehichleImage,rvLicence,rvInsurance;
    private Button submit_btn;
    private String vehicle[]={"Please select vehicle type","Bike","Car"};
    private List<String> brandNameList=new ArrayList<>();
    private String path;
    private final int PROFILE_REQ=10,VEHIClE_REQ=11,LICENCE_REQ=12,INSURANCE_REQ=13;
    private String driver_photo="";
    private List<String> vehichleImagesList=new ArrayList<>();
    private List<String> licenseImagesList=new ArrayList<>();
    private List<String> insuranceImagesList=new ArrayList<>();
    private int isLoad=-1;
    private SignupModel stepOneResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        init();
        stepOneResponse=getIntent().getParcelableExtra("stepOne");
        getBrandDetails();
    }

    private boolean checkPermission() {
        boolean ret=true;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ret = false;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
            }
        }

        return  ret;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 11:
            boolean isPermissionDenied=false;
            for(int i=0;i<grantResults.length;i++)
            {
                if(!(grantResults[i]==PackageManager.PERMISSION_GRANTED))
                {
                    isPermissionDenied=true;
                    break;
                }
            }

            if(isPermissionDenied)
            {

            }else
            {
                if(isLoad!=1)
                {
                   takePhotoIntent(isLoad);
                }
            }
            break;
        }


    }

    private void getBrandDetails() {
        try{
          ServicesInterface apiInterface= ServicesConnection.getInstance().createService(this,"https://private-anon-5ade488757-carsapi1.apiary-mock.com/");
          Call<List<CommonModel>> call=apiInterface.brandList();
          ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true, new Callback<List<CommonModel>>() {
              @Override
              public void onResponse(Call<List<CommonModel>> call, Response<List<CommonModel>> response) {
                  if(response.body()!=null)
                  {
                      for(int i=0;i<response.body().size();i++)
                      {
                          brandNameList.add(response.body().get(i).getName().toUpperCase());
                      }
//                      checkPermission();
                      brandNameList.add(0,"Please select brand");
                      ImageGlider.setNormalImage(DriverRegistrationActivity.this,ivProfilePic, progressBar, "");

                  }
              }

              @Override
              public void onFailure(Call<List<CommonModel>> call, Throwable t) {
                  Log.e("failure", t.getMessage());

              }
          });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void init() {
        ivProfilePic=findViewById(R.id.ivProfilePic);
        progressBar=findViewById(R.id.progressBar);
        edVehicletype=findViewById(R.id.edVehicletype);
        spnVehicle=findViewById(R.id.spnVehicle);
        edBrand=findViewById(R.id.edBrand);
        spnBrand=findViewById(R.id.spnBrand);
        edVehicleNumber=findViewById(R.id.edVehicleNumber);
        rvVehichleImage=findViewById(R.id.rvVehichleImage);
        clVehchileImage=findViewById(R.id.clVehchileImage);
        edLicenseNumber=findViewById(R.id.edLicenseNumber);
        rvLicence=findViewById(R.id.rvLicence);
        clLicenseImage=findViewById(R.id.clLicenseImage);
        edInsuranceNumber=findViewById(R.id.edInsuranceNumber);
        rvInsurance=findViewById(R.id.rvInsurance);
        clVehchileInsuranceImage=findViewById(R.id.clVehchileInsuranceImage);
        submit_btn=findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(this);
        clLicenseImage.setOnClickListener(this);
        clVehchileImage.setOnClickListener(this);
        clVehchileInsuranceImage.setOnClickListener(this);
        ivProfilePic.setOnClickListener(this);
        edVehicletype.setOnClickListener(this);
        edBrand.setOnClickListener(this);
        spnBrand.setOnItemSelectedListener(this);
        spnVehicle.setOnItemSelectedListener(this);
        setAdapter(vehichleImagesList, rvVehichleImage, "VEHICLE");
        setAdapter(licenseImagesList, rvLicence, "LICENSE");
        setAdapter(insuranceImagesList, rvInsurance, "INSURANCE");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.clLicenseImage:
            isLoad=LICENCE_REQ;
            if(checkPermission()) {
            if (checkImageLoadSize(licenseImagesList,LICENCE_REQ))
                takePhotoIntent(LICENCE_REQ);
            }
            break;

            case R.id.clVehchileImage:
            isLoad=VEHIClE_REQ;
            if(checkPermission()) {
            if (checkImageLoadSize(vehichleImagesList,VEHIClE_REQ))
                takePhotoIntent(VEHIClE_REQ);
            }
            break;

            case R.id.clVehchileInsuranceImage:
            isLoad=INSURANCE_REQ;
            if(checkPermission()) {
            if (checkImageLoadSize(insuranceImagesList,INSURANCE_REQ))
                takePhotoIntent(INSURANCE_REQ);
            }
            break;

            case R.id.ivProfilePic:
            isLoad=PROFILE_REQ;
            if(checkPermission()) {
                takePhotoIntent(PROFILE_REQ);
            }
            break;

            case R.id.edVehicletype:
            CommonUtils.setSpinner(DriverRegistrationActivity.this, Arrays.asList(vehicle), spnVehicle);
            break;

            case R.id.edBrand:
            CommonUtils.setSpinner(DriverRegistrationActivity.this, brandNameList, spnBrand);
            break;

            case R.id.submit_btn:
            if(checkValidataion())
            stepTwoApi();
            break;
        }
    }

    private void stepTwoApi() {
        try {
            ServicesInterface apiInterface= ServicesConnection.getInstance().createService(this);
            Call<CommonModel> call=apiInterface.stepTwo(getDriverImage(),getImages("vehicle_images",vehichleImagesList),getSingleImages("license_image",licenseImagesList),getSingleImages("insurance_image",insuranceImagesList), getData());
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true,new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.body()!=null){
                        if(response.body().getStatus())
                        {
                            stepOneResponse.setDriver_image(driver_photo);
                            stepOneResponse.setType(edVehicletype.getText().toString().trim());
                            stepOneResponse.setModal(edBrand.getText().toString().trim());
                            stepOneResponse.setVehicle_number(edVehicleNumber.getText().toString().trim());
                            stepOneResponse.setLicense_number(edLicenseNumber.getText().toString().trim());
                            stepOneResponse.setInsurance_number(edInsuranceNumber.getText().toString().trim());
                            stepOneResponse.setVehicle_images(vehichleImagesList);
                            stepOneResponse.setLicense_image(licenseImagesList.get(0));
                            stepOneResponse.setInsurance_image(insuranceImagesList.get(0));
                            Intent intent = new Intent(DriverRegistrationActivity.this,AddBankDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("stepTwo", stepOneResponse);
                            startActivity(intent);

                        }else
                        {
                            CommonUtils.showSnackBar(DriverRegistrationActivity.this,response.body().getMessage());
                        }

                    }else if(response.errorBody()!=null)
                    {
                        CommonUtils.errorResponse(DriverRegistrationActivity.this,response.errorBody());
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

    private MultipartBody.Part getSingleImages(String key, List<String> list) throws IOException {
        MultipartBody.Part part =null;
        if(list!=null)
        {
            File file =new File(list.get(0));
            RequestBody body=RequestBody.create(MediaType.parse("" + "*/*"),new Compressor(this).compressToFile(file));
            part=MultipartBody.Part.createFormData(key, file.getName(),body);
        }

        return part;
    }

    private Map<String, RequestBody> getData() {
        SignupModel model= new SignupModel();
        model.setType(edVehicletype.getText().toString().trim());
        model.setModal(edBrand.getText().toString().trim());
        model.setVehicle_number(edVehicleNumber.getText().toString().trim());
        model.setLicense_number(edLicenseNumber.getText().toString().trim());
        model.setInsurance_number(edInsuranceNumber.getText().toString().trim());

        AddRequestBody body= new AddRequestBody(model,"Driver");
        return body.getBody();

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
        if(driver_photo!=null)
        {
           File file =new File(driver_photo);
           RequestBody body=RequestBody.create(MediaType.parse("" +
                   "*/*"),new Compressor(this).compressToFile(file));
            part=MultipartBody.Part.createFormData("driver_image", file.getName(),body);
        }

        return part;
    }

    private boolean checkValidataion() {
        boolean ret=true;

        if(driver_photo.equalsIgnoreCase(""))
        {
            ret=false;
            CommonUtils.showSnackBar(this,"Please Upload photo");
        }else if(edVehicletype.getText().toString().trim().equalsIgnoreCase(""))
        {
            ret=false;
            CommonUtils.showSnackBar(this,"Please select vehchile type");
        }else if(edBrand.getText().toString().trim().equalsIgnoreCase(""))
        {
            ret=false;
            CommonUtils.showSnackBar(this,"Please select brand");
        }else if(edVehicleNumber.getText().toString().trim().equalsIgnoreCase(""))
        {
            ret=false;
            CommonUtils.showSnackBar(this,"Please enter vehchile number");
        }else if(!(vehichleImagesList.size()>0))
        {
            ret=false;
            CommonUtils.showSnackBar(this,"Please add vehicle image");
        }else if(edLicenseNumber.getText().toString().trim().equalsIgnoreCase(""))
        {
            ret=false;
            CommonUtils.showSnackBar(this,"Please add license Number");
        }else if(!(licenseImagesList.size()>0))
        {
            ret=false;
            CommonUtils.showSnackBar(this,"Please add License Image");
        }else if(edInsuranceNumber.getText().toString().trim().equalsIgnoreCase(""))
        {
            ret=false;
            CommonUtils.showSnackBar(this,"Please add insurance number");
        }else if(!(insuranceImagesList.size()>0))
        {
            ret=false;
            CommonUtils.showSnackBar(this,"Please add insurance image");
        }

        return ret;
    }

    private boolean checkImageLoadSize(List<String> list,int REQ) {
        boolean ret=true;

        switch (REQ)
        {
            case INSURANCE_REQ:
            if(!(list.size()<1)){ ret=false; CommonUtils.showSnackBar(this, "You can add only one image");}
            break;

            case LICENCE_REQ:
            if(!(list.size()<1)){ ret=false; CommonUtils.showSnackBar(this, "You can add only one image");}
            break;

            case VEHIClE_REQ:
            if(!(list.size()<2)){ ret=false; CommonUtils.showSnackBar(this, "You can add upto 2 images only");}
            break;

        }

        return ret;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position!=0)
        {
            switch (parent.getId())
            {
                case R.id.spnBrand:
                edBrand.setText(brandNameList.get(position));
                break;

                case R.id.spnVehicle:
                edVehicletype.setText(vehicle[position]);
                break;
            }
        }

    }


    private void takePhotoIntent(int code) {
        String capture_dir= Environment.getExternalStorageDirectory() + "/TubeDeliveries/Images/";
        File file = new File(capture_dir);
        if (!file.exists())
        {
            file.mkdirs();
        }
        path = capture_dir + System.currentTimeMillis() + ".jpg";
        Uri imageFileUri = FileProvider.getUriForFile(Objects.requireNonNull(this.getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", new File(path));
        Intent intent = new CommonUtils().getPickIntent(this,imageFileUri);
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case PROFILE_REQ:
                if (data != null) {
                    path = FilePath.getPath(this, Uri.parse(data.getDataString()));
                }
                    driver_photo = path;
                    ImageGlider.setNormalImage(this,ivProfilePic,progressBar,""+Uri.parse(driver_photo));
                break;

                case VEHIClE_REQ:
                if (data != null) {
                    path = FilePath.getPath(this, Uri.parse(data.getDataString()));
                }
                    vehichleImagesList.add(path);
                    rvVehichleImage.getAdapter().notifyDataSetChanged();
                break;

                case LICENCE_REQ:
                if (data != null) {
                    path = FilePath.getPath(this, Uri.parse(data.getDataString()));
                }
                    licenseImagesList.add(path);
                    rvLicence.getAdapter().notifyDataSetChanged();
                break;

                case INSURANCE_REQ:
                if (data != null) {
                    path = FilePath.getPath(this, Uri.parse(data.getDataString()));
                }
                    insuranceImagesList.add(path);
                    rvInsurance.getAdapter().notifyDataSetChanged();
                break;

            }
        }
    }

    private void setAdapter(List<String> list,RecyclerView recyclerView,String type) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new CommonImageAdpter(this,list,this,type));
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.setToolbar(this, "Complete your Driver Profile");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRemove(String type, int pos) {
        switch (type)
        {
            case "VEHICLE":
            vehichleImagesList.remove(pos);
            rvVehichleImage.getAdapter().notifyItemChanged(pos);
            break;

            case "LICENSE":
            licenseImagesList.remove(pos);
            rvLicence.getAdapter().notifyItemChanged(pos);
            break;

            case "INSURANCE":
            insuranceImagesList.remove(pos);
            rvInsurance.getAdapter().notifyItemChanged(pos);
            break;
        }
    }
}