package com.TubeDeliveriesDriverModule.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.TubeDeliveriesDriverModule.Activity.AddBankDetailActivity;
import com.TubeDeliveriesDriverModule.Adaptor.CommonImageAdpter;
import com.TubeDeliveriesDriverModule.BuildConfig;
import com.TubeDeliveriesDriverModule.Model.CommonModel;
import com.TubeDeliveriesDriverModule.Model.ResponseBean;
import com.TubeDeliveriesDriverModule.Model.SignupModel;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesConnection;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesInterface;
import com.TubeDeliveriesDriverModule.SharePrefrences.SPreferenceKey;
import com.TubeDeliveriesDriverModule.SharePrefrences.SharedPreferenceWriter;
import com.TubeDeliveriesDriverModule.Utils.AddRequestBody;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;
import com.TubeDeliveriesDriverModule.Utils.FilePath;
import com.TubeDeliveriesDriverModule.Utils.ImageGlider;
import com.TubeDeliveriesDriverModule.Utils.LocationClass;
import com.TubeDeliveriesDriverModule.Utils.SwitchFragment;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

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

public class MyProfileFragment extends Fragment implements CommonImageAdpter.onImageClick, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private CircleImageView ciProfilePic;
    private ProgressBar progressBar;
    private TextInputEditText edVehicletype, edBrand, edVehicleNumber, edLicenseNumber, edInsuranceNumber;
    private Spinner spnBrand, spnVehicle;
    private ConstraintLayout clVehchileImage, clLicenseImage, clVehchileInsuranceImage;
    private RecyclerView rvVehichleImage, rvLicence, rvInsurance;
    private Button submit_btn;
    private String vehicle[] = {"Please select vehicle type", "Bike", "Car"};
    private List<String> brandNameList = new ArrayList<>();
    private String path;
    private final int PROFILE_REQ = 134, VEHIClE_REQ = 11, LICENCE_REQ = 12, INSURANCE_REQ = 13;
    private String driver_photo = "";
    private List<String> vehichleImagesList = new ArrayList<>();
    private List<String> licenseImagesList = new ArrayList<>();
    private List<String> insuranceImagesList = new ArrayList<>();
    private int isLoad = -1;
    private TextInputEditText ed_signup_first_name,ed_signup_last_name,ed_signup_email,ed_signup_phone,edState,edCity;
    private Spinner spnState,spnCity;
    private final int STATE_REQ=135;
    private final int CITY_REQ=102;
    private String countryCode;
    private LocationClass gpsTracker;
    private Double latitue,longitude;
    private CountryCodePicker countryCodePicker;
    private Button btUpdate;
    private Button btnBankDetails;
    private String bank_type;
    private String name;
    private String account_number;
    private String iban;
    private LocationClass locationClass;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        countryCodePicker=view.findViewById(R.id.countryCodePicker);
        ed_signup_first_name=view.findViewById(R.id.ed_signup_first_name);
        ed_signup_last_name=view.findViewById(R.id.ed_signup_last_name);
        ed_signup_email=view.findViewById(R.id.ed_signup_email);
        ed_signup_phone=view.findViewById(R.id.ed_signup_phone);
        edState=view.findViewById(R.id.edState);
        edCity=view.findViewById(R.id.edCity);
        spnState=view.findViewById(R.id.spnState);
        spnCity=view.findViewById(R.id.spnCity);
        edCity.setOnClickListener(this);
        edState.setOnClickListener(this);
        gpsTracker= new LocationClass(getActivity());
        gpsTracker.getUpdateLocationListener();
        ciProfilePic=view.findViewById(R.id.ciProfilePic);
        progressBar = view.findViewById(R.id.progressBar);
        edVehicletype = view.findViewById(R.id.edVehicletype);
        spnVehicle = view.findViewById(R.id.spnVehicle);
        edBrand = view.findViewById(R.id.edBrand);
        spnBrand = view.findViewById(R.id.spnBrand);
        edVehicleNumber = view.findViewById(R.id.edVehicleNumber);
        rvVehichleImage = view.findViewById(R.id.rvVehichleImage);
        clVehchileImage = view.findViewById(R.id.clVehchileImage);
        edLicenseNumber = view.findViewById(R.id.edLicenseNumber);
        rvLicence = view.findViewById(R.id.rvLicence);
        clLicenseImage = view.findViewById(R.id.clLicenseImage);
        edInsuranceNumber = view.findViewById(R.id.edInsuranceNumber);
        rvInsurance = view.findViewById(R.id.rvInsurance);
        clVehchileInsuranceImage = view.findViewById(R.id.clVehchileInsuranceImage);
        btUpdate=view.findViewById(R.id.btUpdate);
        clLicenseImage.setOnClickListener(this);
        clVehchileImage.setOnClickListener(this);
        clVehchileInsuranceImage.setOnClickListener(this);
        ciProfilePic.setOnClickListener(this);
        edVehicletype.setOnClickListener(this);
        edBrand.setOnClickListener(this);
        spnBrand.setOnItemSelectedListener(this);
        spnVehicle.setOnItemSelectedListener(this);
        btnBankDetails=getActivity().findViewById(R.id.btnBankDetails);
        btnBankDetails.setOnClickListener(this);
        setAdapter(vehichleImagesList, rvVehichleImage, "VEHICLE");
        setAdapter(licenseImagesList, rvLicence, "LICENSE");
        setAdapter(insuranceImagesList, rvInsurance, "INSURANCE");
        btUpdate.setOnClickListener(this);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CommonUtils.showLoadingDialog(getActivity());
        getBrandDetails();
    }

    private void getBrandDetails() {
        try{
            ServicesInterface apiInterface= ServicesConnection.getInstance().createService(getActivity(),"https://private-anon-5ade488757-carsapi1.apiary-mock.com/");
            Call<List<CommonModel>> call=apiInterface.brandList();
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), false, new Callback<List<CommonModel>>() {
                @Override
                public void onResponse(Call<List<CommonModel>> call, Response<List<CommonModel>> response) {
                    if(response.body()!=null)
                    {
                        for(int i=0;i<response.body().size();i++)
                        {
                            brandNameList.add(response.body().get(i).getName().toUpperCase());
                        }
                        brandNameList.add(0,"Please select brand");
                        locationClass= new LocationClass(getActivity());
                        driverDetailsApi();
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

    private void driverDetailsApi() {
        try {
            ServicesInterface apiInterface= ServicesConnection.getInstance().createService(getActivity());
            Call<CommonModel> call=apiInterface.driverDetails();
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), false,new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    CommonUtils.dismissLoadingDialog();
                    if(response.body()!=null){
                        if(response.body().getStatus())
                        {
                            setUpData(response.body().getDriverDetails());

                        }else
                        {
                            CommonUtils.showSnackBar(getActivity(),response.body().getMessage());
                        }

                    }else if(response.errorBody()!=null)
                    {
                        CommonUtils.errorResponse(getActivity(),response.errorBody());
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

    private void setUpData(ResponseBean data) {

        ed_signup_first_name.setText(data.getFirstName());
        ed_signup_last_name.setText(data.getLastName());
        ed_signup_email.setText(data.getEmail());
        ed_signup_phone.setText(data.getPhone());
        countryCodePicker.setAutoDetectedCountry(true);
        countryCode=data.getCountryCode();
        countryCodePicker.setCountryForPhoneCode(Integer.parseInt(data.getCountryCode()));
        edState.setText(data.getDriver().getState());
        edCity.setText(data.getDriver().getCity());
        edVehicletype.setText(data.getDriverProfile().getVehicleType());
        edBrand.setText(data.getDriverProfile().getModal());
        edVehicleNumber.setText(data.getDriverProfile().getVehicleNumber());
        List<String> imageList=new ArrayList<>();
        for(int i=0;i<data.getVehicleImage().size();i++)
        {
            imageList.add(data.getVehicleImage().get(i).getImage());
        }
        vehichleImagesList.clear();
        vehichleImagesList.addAll(imageList);
        driver_photo=data.getDriverProfile().getDriverImage();
        SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SPreferenceKey.IMAGE, data.getDriverProfile().getDriverImage());
        CircleImageView ciProfile =getActivity().findViewById(R.id.ciProfile);
        ProgressBar progressBar =getActivity().findViewById(R.id.progressBar);
        ImageGlider.setNormalImage(getActivity(),ciProfile,progressBar,SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.IMAGE));

        ImageGlider.setNormalImage(getActivity(), ciProfilePic, progressBar, data.getDriverProfile().getDriverImage());
        edLicenseNumber.setText(data.getDriverProfile().getLicenseNumber());
        licenseImagesList.clear();
        licenseImagesList.add(data.getDriverProfile().getLicenseImage());
        edInsuranceNumber.setText(data.getDriverProfile().getInsuranceNumber());
        insuranceImagesList.clear();
        insuranceImagesList.add(data.getDriverProfile().getInsuranceImage());
        rvInsurance.getAdapter().notifyDataSetChanged();
        rvVehichleImage.getAdapter().notifyDataSetChanged();
        rvLicence.getAdapter().notifyDataSetChanged();
        bank_type=data.getDriverBankDetail().getBankType();
        name=data.getDriverBankDetail().getName();
        account_number=data.getDriverBankDetail().getAccountNumber();
        iban=data.getDriverBankDetail().getIban();
        if(locationPerimission()) { setUpLocationSettingsTaskStuff();}
    }

    public  void setUpLocationSettingsTaskStuff() {


        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(10 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    if(gpsTracker.getLocation()!=null)
                    {
                        latitue= gpsTracker.getLatitude();
                        longitude=gpsTracker.getLongitude();
                    }
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(getActivity(), 17);

                            } catch (IntentSender.SendIntentException e) {
                            } catch (ClassCastException e) {
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // cannot do anything
                            break;
                    }
                }
            }
        });
    }



    private boolean locationPerimission() {
        boolean ret=true;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ret = false;
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 11);

            }
        }

        return  ret;
    }

    private void setAdapter(List<String> list, RecyclerView recyclerView, String type) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new CommonImageAdpter(getActivity(), list, this, type));
    }

    @Override
    public void onRemove(String type, int pos) {
        switch (type) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ciProfilePic:
            isLoad=PROFILE_REQ;
            if(checkPermission()) {
                takePhotoIntent(PROFILE_REQ);
            }
            break;

            case R.id.btnBankDetails:
            Intent intent=new Intent(getActivity(),AddBankDetailActivity.class);
            intent.putExtra("bank_type", bank_type);
            intent.putExtra("name", name);
            intent.putExtra("account_number", account_number);
            intent.putExtra("iban", iban);
            startActivityForResult(intent, 99);
            break;

            case R.id.btUpdate:
            if(checkValidation()){
                SignupModel model=new SignupModel();
                model.setDriver_image(driver_photo);
                model.setLatitude(locationClass.getLatitude());
                model.setLongitude(locationClass.getLongitude());
                model.setPhone_no(ed_signup_phone.getText().toString().trim());
                model.setEmail(ed_signup_email.getText().toString().trim());
                model.setCountry_code(countryCode);
                model.setFirst_name(ed_signup_first_name.getText().toString().trim());
                model.setLast_name(ed_signup_last_name.getText().toString().trim());
                model.setPhone_no(ed_signup_phone.getText().toString().trim());
                model.setState(edState.getText().toString().trim());
                model.setCity(edCity.getText().toString().trim());
                model.setType(edVehicletype.getText().toString().trim());
                model.setModal(edBrand.getText().toString().trim());
                model.setVehicle_number(edVehicleNumber.getText().toString().trim());
                model.setVehicle_images(vehichleImagesList);
                model.setLicense_number(edLicenseNumber.getText().toString().trim());
                model.setLicense_image(licenseImagesList.get(0));
                model.setInsurance_number(edInsuranceNumber.getText().toString().trim());
                model.setInsurance_image(insuranceImagesList.get(0));
                model.setBank_type(bank_type);
                model.setName(name);
                model.setAccount_number(account_number);
                model.setIban(iban);
                updateProfileApi(model);
            }
            break;

            case R.id.edState:
            search(TypeFilter.REGIONS,STATE_REQ);
            break;

            case R.id.edCity:
            search(TypeFilter.CITIES,CITY_REQ);
            break;

            case R.id.clLicenseImage:
            isLoad = LICENCE_REQ;
            if (checkPermission()) {
                if (checkImageLoadSize(licenseImagesList, LICENCE_REQ))
                    takePhotoIntent(LICENCE_REQ);
            }
            break;

            case R.id.clVehchileImage:
            isLoad = VEHIClE_REQ;
            if (checkPermission()) {
                if (checkImageLoadSize(vehichleImagesList, VEHIClE_REQ))
                    takePhotoIntent(VEHIClE_REQ);
            }
            break;

            case R.id.clVehchileInsuranceImage:
            isLoad = INSURANCE_REQ;
            if (checkPermission()) {
                if (checkImageLoadSize(insuranceImagesList, INSURANCE_REQ))
                    takePhotoIntent(INSURANCE_REQ);
            }
            break;

            case R.id.ivProfilePic:
            isLoad = PROFILE_REQ;
            if (checkPermission()) {
                takePhotoIntent(PROFILE_REQ);
            }
            break;

            case R.id.edVehicletype:
            CommonUtils.setSpinner(getActivity(), Arrays.asList(vehicle), spnVehicle);
            break;

            case R.id.edBrand:
            CommonUtils.setSpinner(getActivity(), brandNameList, spnBrand);
            break;

        }

    }

    private void updateProfileApi(SignupModel model) {
        try {
            ServicesInterface apiInterface= ServicesConnection.getInstance().createService(getActivity());
            Call<CommonModel> call=apiInterface.updateDriver(getDriverImage(),
                    getImages("vehicle_images",model.getVehicle_images()),
                    getSingleImages("license_image",model.getLicense_image()),
                    getSingleImages("insurance_image",model.getInsurance_image()),getData(model));
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), true,new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    CommonUtils.dismissLoadingDialog();
                    if(response.body()!=null){
                        if(response.body().getStatus())
                        {

                            SwitchFragment.changeFragment(getActivity().getSupportFragmentManager(), new HomeFragment(), false, true);
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }else
                        {
                            CommonUtils.showSnackBar(getActivity(),response.body().getMessage());
                        }
                    }else if(response.errorBody()!=null)
                    {
                        CommonUtils.errorResponse(getActivity(),response.errorBody());
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

    private Map<String, RequestBody> getData(SignupModel model) {
        AddRequestBody body=new AddRequestBody(model,"My Profile");
        return body.getBody();
    }

    private MultipartBody.Part getSingleImages(String key, String image) throws IOException {
        MultipartBody.Part part =null;
        if(image!=null && !image.contains("http"))
        {
            File file =new File(image);
            RequestBody body=RequestBody.create(MediaType.parse("" + "*/*"),new Compressor(getActivity()).compressToFile(file));
            part=MultipartBody.Part.createFormData(key, file.getName(),body);
        }

        return part;
    }

    private MultipartBody.Part getDriverImage() throws IOException {
        MultipartBody.Part part =null;
        if(driver_photo!=null && !driver_photo.contains("http"))
        {
            File file =new File(driver_photo);
            RequestBody body=RequestBody.create(MediaType.parse("" + "*/*"),new Compressor(getActivity()).compressToFile(file));
            part=MultipartBody.Part.createFormData("driver_image", file.getName(),body);
        }

        return part;
    }

    private MultipartBody.Part[] getImages(String key,List<String> imgList) throws IOException {
        RequestBody imagesBody=null;
        MultipartBody.Part imgpart[]=new MultipartBody.Part[imgList.size()];

        for(int i=0;i<imgList.size();i++)
        {
            if(!imgList.get(i).contains("http")) {
                File imgFile = new File(imgList.get(i));
                imagesBody = RequestBody.create(MediaType.parse("*/*"), new Compressor(getActivity()).compressToFile(imgFile));
                imgpart[i] = MultipartBody.Part.createFormData(key + "[" + i + "]", imgFile.getName(), imagesBody);
            }
        }

        return imgpart;
    }


    private boolean checkValidation() {
        boolean ret = true;
        if (ed_signup_first_name.getText().toString().isEmpty()) {
            ret = false;
            CommonUtils.showSnackBar(getActivity(), "Please enter first name");
        } else if (ed_signup_last_name.getText().toString().isEmpty()) {
            ret = false;
            CommonUtils.showSnackBar(getActivity(), "Please enter last name");
        } else if (ed_signup_email.getText().toString().trim().length() == 0 || !Patterns.EMAIL_ADDRESS.matcher(ed_signup_email.getText().toString()).matches()) {
            ret = false;
            if (ed_signup_email.getText().toString().trim().length() == 0)
                CommonUtils.showSnackBar(getActivity(), getString(R.string.enter_email_id));
            else if (!Patterns.EMAIL_ADDRESS.matcher(ed_signup_email.getText().toString()).matches())
                CommonUtils.showSnackBar(getActivity(), getString(R.string.enter_valid_email_id));
        } else if (!(ed_signup_phone.getText().toString().trim().length() > 0) || ed_signup_phone.getText().toString().trim().length()<=9) {
            ret = false;
            if(!(ed_signup_phone.getText().toString().trim().length() > 0) ) CommonUtils.showSnackBar(getActivity(), "Please enter mobile number");
            else if(ed_signup_phone.getText().toString().trim().length()<=9) CommonUtils.showSnackBar(getActivity(), "Please enter valid mobile number");
        } else if (edState.getText().toString().isEmpty() && edState.getText().toString().equalsIgnoreCase("")) {
            ret = false;
            CommonUtils.showSnackBar(getActivity(), "Please select State");
        }
        else if (edCity.getText().toString().isEmpty() && edCity.getText().toString().equalsIgnoreCase("")) {
            ret = false;
            CommonUtils.showSnackBar(getActivity(), "Please select City");
        }

        else if(driver_photo.equalsIgnoreCase(""))
        {
            ret=false;
            CommonUtils.showSnackBar(getActivity(),"Please Upload photo");
        }else if(edVehicletype.getText().toString().trim().equalsIgnoreCase(""))
        {
            ret=false;
            CommonUtils.showSnackBar(getActivity(),"Please select vehchile type");
        }else if(edBrand.getText().toString().trim().equalsIgnoreCase(""))
        {
            ret=false;
            CommonUtils.showSnackBar(getActivity(),"Please select brand");
        }else if(edVehicleNumber.getText().toString().trim().equalsIgnoreCase(""))
        {
            ret=false;
            CommonUtils.showSnackBar(getActivity(),"Please enter vehchile number");
        }else if(!(vehichleImagesList.size()>0))
        {
            ret=false;
            CommonUtils.showSnackBar(getActivity(),"Please add vehicle image");
        }else if(edLicenseNumber.getText().toString().trim().equalsIgnoreCase(""))
        {
            ret=false;
            CommonUtils.showSnackBar(getActivity(),"Please add license Number");
        }else if(!(licenseImagesList.size()>0))
        {
            ret=false;
            CommonUtils.showSnackBar(getActivity(),"Please add License Image");
        }else if(edInsuranceNumber.getText().toString().trim().equalsIgnoreCase(""))
        {
            ret=false;
            CommonUtils.showSnackBar(getActivity(),"Please add insurance number");
        }else if(!(insuranceImagesList.size()>0))
        {
            ret=false;
            CommonUtils.showSnackBar(getActivity(),"Please add insurance image");
        }

        return ret;
    }

    private void search(TypeFilter type, int REQ) {
        Places.initialize(getActivity(),getResources().getString(R.string.google_map_api_key));
        List<Place.Field> fields1 = Arrays.asList(Place.Field.NAME);
        Intent intent1 = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields1).setTypeFilter(type).build(getActivity());
        startActivityForResult(intent1, REQ);
    }

    private boolean checkImageLoadSize(List<String> list,int REQ) {
        boolean ret=true;

        switch (REQ)
        {
            case INSURANCE_REQ:
            if(!(list.size()<1)){ ret=false; CommonUtils.showSnackBar(getActivity(), "You can add only one image");}
            break;

            case LICENCE_REQ:
            if(!(list.size()<1)){ ret=false; CommonUtils.showSnackBar(getActivity(), "You can add only one image");}
            break;

            case VEHIClE_REQ:
            if(!(list.size()<2)){ ret=false; CommonUtils.showSnackBar(getActivity(), "You can add upto 2 images only");}
            break;

        }

        return ret;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 12:
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

            case 11:
                boolean coarseLocation= grantResults[0]==PackageManager.PERMISSION_GRANTED;
                boolean fineLocation= grantResults[1]==PackageManager.PERMISSION_GRANTED;

                if(coarseLocation && fineLocation)
                {
                    setUpLocationSettingsTaskStuff();

                }else
                {
                    CommonUtils.showSnackBar(getActivity(),"You need to allow permission for the security purpose");
                }
                break;
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
        Uri imageFileUri = FileProvider.getUriForFile(Objects.requireNonNull(getActivity().getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", new File(path));
        Intent intent = new CommonUtils().getPickIntent(getActivity(),imageFileUri);
        startActivityForResult(intent, code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView tvTitle=getActivity().findViewById(R.id.tvTitle);
        ToggleButton toggleButton=getActivity().findViewById(R.id.toggleButton);
        toggleButton.setVisibility(View.GONE);
        tvTitle.setText("My Profile");
        if(resultCode==getActivity().RESULT_OK) {
            switch (requestCode) {

                case 99:
                account_number=data.getStringExtra("account_number");
                iban=data.getStringExtra("iban");
                name=data.getStringExtra("name");
                bank_type=data.getStringExtra("bank_type");
                break;

                case PROFILE_REQ:
                if (data != null) {
                    path = FilePath.getPath(getActivity(), Uri.parse(data.getDataString()));
                }
                driver_photo = path;
                ImageGlider.setNormalImage(getActivity(),ciProfilePic,progressBar,""+Uri.parse(driver_photo));
                break;


                case CITY_REQ:
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    edCity.setText(place.getName());
                    break;

                case STATE_REQ:
                    Place state = Autocomplete.getPlaceFromIntent(data);
                    edState.setText(state.getName());
                    break;

                case 17:
                if(gpsTracker.getLocation()!=null)
                {
                    latitue= gpsTracker.getLatitude();
                    longitude=gpsTracker.getLongitude();
                }
                break;



                case VEHIClE_REQ:
                if (data != null) {
                    path = FilePath.getPath(getActivity(), Uri.parse(data.getDataString()));
                }
                vehichleImagesList.add(path);
                rvVehichleImage.getAdapter().notifyDataSetChanged();
                break;

                case LICENCE_REQ:
                if (data != null) {
                    path = FilePath.getPath(getActivity(), Uri.parse(data.getDataString()));
                }
                licenseImagesList.add(path);
                rvLicence.getAdapter().notifyDataSetChanged();
                break;

                case INSURANCE_REQ:
                if (data != null) {
                    path = FilePath.getPath(getActivity(), Uri.parse(data.getDataString()));
                }
                insuranceImagesList.add(path);
                rvInsurance.getAdapter().notifyDataSetChanged();
                break;

            }
        }
    }





    private boolean checkPermission() {
        boolean ret = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ret = false;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
            }
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}