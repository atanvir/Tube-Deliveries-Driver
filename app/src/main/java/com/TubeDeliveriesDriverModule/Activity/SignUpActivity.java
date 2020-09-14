package com.TubeDeliveriesDriverModule.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.TubeDeliveriesDriverModule.Model.CommonModel;
import com.TubeDeliveriesDriverModule.Model.SignupModel;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesConnection;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesInterface;
import com.TubeDeliveriesDriverModule.SharePrefrences.SPreferenceKey;
import com.TubeDeliveriesDriverModule.SharePrefrences.SharedPreferenceWriter;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;
import com.TubeDeliveriesDriverModule.Utils.LocationClass;
import com.TubeDeliveriesDriverModule.Utils.MultiTextWatcher;
import com.TubeDeliveriesDriverModule.Utils.ParamEnum;
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

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, MultiTextWatcher.TextWatcherWithInstance {
    private Button bt_signup;
    private TextInputEditText ed_signup_confirm_password,ed_signup_password,ed_signup_address,ed_signup_first_name,ed_signup_last_name,ed_signup_email,ed_signup_phone,edState,edCity;
    private Spinner spnState,spnCity;
    private TextView tvMatchedPass;
    private CheckBox agreedCB;
    private final int STATE_REQ=13;
    private final int CITY_REQ=11;
    private LocationClass gpsTracker;
    private Double latitue,longitude;
    private CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        bt_signup=findViewById(R.id.bt_signup);
        ed_signup_confirm_password=findViewById(R.id.ed_signup_confirm_password);
        ed_signup_password=findViewById(R.id.ed_signup_password);
        tvMatchedPass=findViewById(R.id.tvMatchedPass);
        countryCodePicker=findViewById(R.id.countryCodePicker);
        countryCodePicker.setAutoDetectedCountry(true);
        ed_signup_first_name=findViewById(R.id.ed_signup_first_name);
        ed_signup_last_name=findViewById(R.id.ed_signup_last_name);
        ed_signup_email=findViewById(R.id.ed_signup_email);
        ed_signup_phone=findViewById(R.id.ed_signup_phone);
        edState=findViewById(R.id.edState);
        edCity=findViewById(R.id.edCity);
        agreedCB=findViewById(R.id.agreedCB);
        spnState=findViewById(R.id.spnState);
        spnCity=findViewById(R.id.spnCity);
        bt_signup.setOnClickListener(this);
        edCity.setOnClickListener(this);
        edState.setOnClickListener(this);
        new MultiTextWatcher().registerEditText(ed_signup_confirm_password).registerEditText(ed_signup_password).setCallback(this);
        gpsTracker= new LocationClass(this);
        gpsTracker.getUpdateLocationListener();
        countryCodePicker.setAutoDetectedCountry(true);

        if(locationPerimission()) {
            setUpLocationSettingsTaskStuff();}
    }

    private boolean locationPerimission() {
        boolean ret=true;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ret = false;
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 11);

            }
        }

        return  ret;
    }

    public  void setUpLocationSettingsTaskStuff() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(10 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        SettingsClient client = LocationServices.getSettingsClient(this);
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
                                resolvable.startResolutionForResult(SignUpActivity.this, 17);

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



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_signup:
            if (validation())
            {stepOneApi();}
            break;

            case R.id.edState:
            search(TypeFilter.REGIONS,STATE_REQ);
            break;

            case R.id.edCity:
            search(TypeFilter.CITIES,CITY_REQ);
            break;
        }
    }

    private void stepOneApi() {
        try {
           ServicesInterface apiInterface= ServicesConnection.getInstance().createService(this);
           Call<CommonModel> call=apiInterface.stepOneAPi(getUserData());
           ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true,new Callback<CommonModel>() {
               @Override
               public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                   if(response.body()!=null){
                       if(response.body().getStatus())
                       {
                           Intent intent= new Intent(SignUpActivity.this,OTPActivity.class);
                           intent.putExtra("stepOne", getUserData());
                           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(intent);
                       }else
                       {
                           CommonUtils.showSnackBar(SignUpActivity.this,response.body().getMessage());
                       }

                   }else if(response.errorBody()!=null)
                   {
                       CommonUtils.errorResponse(SignUpActivity.this,response.errorBody());
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

    private SignupModel getUserData() {
        SignupModel model=new SignupModel();
        model.setFirst_name(ed_signup_first_name.getText().toString().trim());
        model.setLast_name(ed_signup_last_name.getText().toString().trim());
        model.setEmail(ed_signup_email.getText().toString().trim());
        model.setCountry_code(countryCodePicker.getSelectedCountryCodeWithPlus());
        model.setPhone_no(ed_signup_phone.getText().toString().trim());
        model.setPassword(ed_signup_password.getText().toString().trim());
        model.setState(edState.getText().toString().trim());
        model.setDevice_type(ParamEnum.ANDROID.theValue());
        model.setDevice_token(SharedPreferenceWriter.getInstance(SignUpActivity.this).getString(SPreferenceKey.TOKEN));
        model.setLatitude(latitue!=null?latitue:gpsTracker.getLatitude());
        model.setLongitude(longitude!=null?longitude:gpsTracker.getLongitude());
        model.setCity(edCity.getText().toString().trim());
        return model;
    }

    private void search(TypeFilter type, int REQ) {
        Places.initialize(this,getResources().getString(R.string.google_map_api_key));
        List<Place.Field> fields1 = Arrays.asList(Place.Field.NAME);
        Intent intent1 = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields1).setTypeFilter(type).build(this);
        startActivityForResult(intent1, REQ);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0)
        {
            switch (requestCode)
            {
                case 11:
                    boolean coarseLocation= grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean fineLocation= grantResults[1]==PackageManager.PERMISSION_GRANTED;

                    if(coarseLocation && fineLocation)
                    {
                        setUpLocationSettingsTaskStuff();

                    }else
                    {
                        CommonUtils.showSnackBar(this,"You need to allow permission for the security purpose");
                    }
                    break;

            }

        }


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {

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


            }


        }else if(resultCode==RESULT_CANCELED)
        {
            switch (requestCode)
            {
                case 17:
                if(gpsTracker.getLocation()!=null)
                {
                    latitue= gpsTracker.getLatitude();
                    longitude=gpsTracker.getLongitude();
                }
                break;
            }
        }
        }


    private boolean validation() {
        boolean ret = true;
        if (ed_signup_first_name.getText().toString().isEmpty()) {
            ret = false;
            CommonUtils.showSnackBar(this, "Please enter first name");
        } else if (ed_signup_last_name.getText().toString().isEmpty()) {
            ret = false;
            CommonUtils.showSnackBar(this, "Please enter last name");
        } else if (ed_signup_email.getText().toString().trim().length() == 0 || !Patterns.EMAIL_ADDRESS.matcher(ed_signup_email.getText().toString()).matches()) {
            ret = false;
            if (ed_signup_email.getText().toString().trim().length() == 0)
                CommonUtils.showSnackBar(this, getString(R.string.enter_email_id));
            else if (!Patterns.EMAIL_ADDRESS.matcher(ed_signup_email.getText().toString()).matches())
                CommonUtils.showSnackBar(this, getString(R.string.enter_valid_email_id));
        } else if (!(ed_signup_phone.getText().toString().trim().length() > 0) || ed_signup_phone.getText().toString().trim().length()<9) {
            ret = false;
            if(!(ed_signup_phone.getText().toString().trim().length() > 0) ) CommonUtils.showSnackBar(this, "Please enter mobile number");
            else if(ed_signup_phone.getText().toString().trim().length()<9) CommonUtils.showSnackBar(this, "Please enter valid mobile number");
        } else if (edState.getText().toString().isEmpty() && edState.getText().toString().equalsIgnoreCase("")) {
            ret = false;
            CommonUtils.showSnackBar(this, "Please select State");
        }
        else if (edCity.getText().toString().isEmpty() && edCity.getText().toString().equalsIgnoreCase("")) {
            ret = false;
            CommonUtils.showSnackBar(this, "Please select City");
        }else if (ed_signup_password.getText().toString().length() <= 7) {
            ret = false;
            CommonUtils.showSnackBar(this, "Please enter password atleast of eight digits");

        } else if (!ed_signup_confirm_password.getText().toString().matches(ed_signup_password.getText().toString())) {
            ret = false;
            CommonUtils.showSnackBar(this, "Password not match");
        } else if (!agreedCB.isChecked()) {
            CommonUtils.showSnackBar(this, "Please mark as agreed to the Terms & Conditions");
            ret = false;
        }

        return ret;
    }


    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.setToolbar(this, "Sign Up");
    }



    @Override
    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
        switch (editText.getId()) {
            case R.id.ed_signup_password:
                if (s.toString().equals(ed_signup_confirm_password.getText().toString())) tvMatchedPass.setVisibility(View.VISIBLE);
                else tvMatchedPass.setVisibility(View.GONE);
                break;

            case R.id.ed_signup_confirm_password:
                if (s.toString().equals(ed_signup_password.getText().toString())) tvMatchedPass.setVisibility(View.VISIBLE);
                else tvMatchedPass.setVisibility(View.GONE);
                break;
        }


    }

    @Override
    public void afterTextChanged(EditText editText, Editable editable) {

    }
}