package com.TubeDeliveriesDriverModule.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.TubeDeliveriesDriverModule.Firebase.MyFirebaseMessageService;
import com.TubeDeliveriesDriverModule.Fragment.ContactUsFragment;
import com.TubeDeliveriesDriverModule.Fragment.EarningsFragment;
import com.TubeDeliveriesDriverModule.Fragment.NotificationFragment;
import com.TubeDeliveriesDriverModule.Fragment.SettingFragment;
import com.TubeDeliveriesDriverModule.Fragment.WebviewFragment;
import com.TubeDeliveriesDriverModule.Model.CommonModel;
import com.TubeDeliveriesDriverModule.Model.MyCurrentDetailsModel;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesConnection;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesInterface;
import com.TubeDeliveriesDriverModule.SharePrefrences.SPreferenceKey;
import com.TubeDeliveriesDriverModule.SharePrefrences.SPrefrenceValues;
import com.TubeDeliveriesDriverModule.SharePrefrences.SharedPreferenceWriter;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;
import com.TubeDeliveriesDriverModule.Utils.ImageGlider;
import com.TubeDeliveriesDriverModule.Utils.ParamEnum;
import com.TubeDeliveriesDriverModule.Utils.SwitchFragment;
import com.TubeDeliveriesDriverModule.Fragment.HomeFragment;
import com.TubeDeliveriesDriverModule.Fragment.MyProfileFragment;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.JsonObject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    DrawerLayout drawerLayout;
    private ConstraintLayout mainCl,clMyOrder,clMyProfile,clEarnings,clNotification,clSetting,clAboutus,clContactUs,clHelp,termsAndConditionCl,clLogout;
    private ImageView menuIv;
    private TextView tvName;
    private ToggleButton toggleButton;
    private CircleImageView ciProfile;
    private ProgressBar progressBar;
    private MyProfileFragment profileFragment = new MyProfileFragment();
    private HomeFragment homeFragment = new HomeFragment();
    private SettingFragment settingFragment= new SettingFragment();
    private ContactUsFragment contactUsFragment= new ContactUsFragment();
    private NotificationFragment notificationFragment= new NotificationFragment();
    private EarningsFragment earningsFragment= new EarningsFragment();
    private boolean isBack;
    private TextView tvTitle;
    boolean isLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CommonUtils.setToolbar(this,"My Orders");
        init();
        ImageGlider.setNormalImage(this,ciProfile,progressBar,SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.IMAGE));
        tvName.setText(SharedPreferenceWriter.getInstance(this).getString(SPreferenceKey.NAME));
        if(SharedPreferenceWriter.getInstance(MainActivity.this).getString(SPreferenceKey.CURRENCY_SYMBOL).equalsIgnoreCase("")) {
            CommonUtils.showLoadingDialog(this);
            myCurrentDetailsApi();
        }else
        {
            if(getIntent().getStringExtra("cameFrom")!=null) loadFragment(notificationFragment);
            else loadFragment(homeFragment);
        }



    }

    private void init() {
        tvTitle=findViewById(R.id.tvTitle);
        mainCl=findViewById(R.id.mainCl);
        drawerLayout = findViewById(R.id.drawer_layout);
        toggleButton = findViewById(R.id.toggleButton);
        ciProfile=findViewById(R.id.ciProfile);
        tvName=findViewById(R.id.tvName);
        progressBar=findViewById(R.id.progressBar);
        clMyProfile=findViewById(R.id.clMyProfile);
        clEarnings=findViewById(R.id.clEarnings);
        clMyOrder=findViewById(R.id.clMyOrder);
        clNotification=findViewById(R.id.clNotification);
        clSetting=findViewById(R.id.clSetting);
        clAboutus=findViewById(R.id.clAboutus);
        clContactUs=findViewById(R.id.clContactUs);
        clHelp=findViewById(R.id.clHelp);
        termsAndConditionCl=findViewById(R.id.termsAndConditionCl);
        clLogout=findViewById(R.id.clLogout);
        menuIv=findViewById(R.id.menuIv);
        menuIv.setOnClickListener(this);
        clMyProfile.setOnClickListener(this);
        clEarnings.setOnClickListener(this);
        clMyOrder.setOnClickListener(this);
        clNotification.setOnClickListener(this);
        clSetting.setOnClickListener(this);
        clAboutus.setOnClickListener(this);
        clContactUs.setOnClickListener(this);
        clHelp.setOnClickListener(this);
        termsAndConditionCl.setOnClickListener(this);
        clLogout.setOnClickListener(this);
        toggleButton.setOnCheckedChangeListener(this);
        toggleButton.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
       String online= SharedPreferenceWriter.getInstance(MainActivity.this).getString(SPreferenceKey.ONLINE);
       if(online.equalsIgnoreCase("1"))
       {
           toggleButton.setChecked(true);
           isLoad=true;

       }else if(online.equalsIgnoreCase("2"))
       {
           toggleButton.setChecked(false);
           isLoad=true;
       }else
       {
           toggleButton.setChecked(true);
           isLoad=true;
       }

        Log.e(SPreferenceKey.COUNTRY_CODE, SharedPreferenceWriter.getInstance(MainActivity.this).getString(SPreferenceKey.COUNTRY_CODE));
        Log.e(SPreferenceKey.CURRENCY, SharedPreferenceWriter.getInstance(MainActivity.this).getString(SPreferenceKey.CURRENCY));
        Log.e(SPreferenceKey.EXCHANGE_RATE, SharedPreferenceWriter.getInstance(MainActivity.this).getString(SPreferenceKey.EXCHANGE_RATE));
        Log.e(SPreferenceKey.CURRENCY_SYMBOL, SharedPreferenceWriter.getInstance(MainActivity.this).getString(SPreferenceKey.CURRENCY_SYMBOL));

    }

    private void myCurrentDetailsApi() {
        try {
            ServicesInterface servicesInterface = ServicesConnection.getInstance().createService(this,"http://ip-api.com/");
            Call<MyCurrentDetailsModel> call = servicesInterface.getMyDeatails();
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false, new Callback<MyCurrentDetailsModel>() {
                @Override
                public void onResponse(Call<MyCurrentDetailsModel> call, Response<MyCurrentDetailsModel> response) {
                    getCurrencyByCountry(response.body().getCountryCode());
                }

                @Override
                public void onFailure(Call<MyCurrentDetailsModel> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCurrencyByCountry(final String countryCode) {
        try {
            ServicesInterface servicesInterface = ServicesConnection.getInstance().createService(this,"https://restcountries.eu/rest/v1/");
            Call<MyCurrentDetailsModel> call = servicesInterface.getCurrency(countryCode);
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false, new Callback<MyCurrentDetailsModel>() {
                @Override
                public void onResponse(Call<MyCurrentDetailsModel> call, Response<MyCurrentDetailsModel> response) {
                    String currency=response.body().getCurrencies().get(0);
                    if(currency.equalsIgnoreCase("ZAR")) {
                        SharedPreferenceWriter.getInstance(MainActivity.this).writeStringValue(SPreferenceKey.CURRENCY, countryCode);
                    }else
                    {
                        SharedPreferenceWriter.getInstance(MainActivity.this).writeStringValue(SPreferenceKey.CURRENCY, "USD");
                    }
                    SharedPreferenceWriter.getInstance(MainActivity.this).writeStringValue(SPreferenceKey.COUNTRY_CODE,currency);
                    getExchangeRate(currency);
                }

                @Override
                public void onFailure(Call<MyCurrentDetailsModel> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getExchangeRate(final String currency) {
        try {
            ServicesInterface servicesInterface = ServicesConnection.getInstance().createService(this,"https://api.exchangeratesapi.io/");
            Call<JsonObject> call = servicesInterface.getExchangeRate("ZAR","USD");
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false, new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JsonObject object= response.body().getAsJsonObject("rates");
                        String currenycyAmt= String.valueOf(object.getAsJsonPrimitive("USD"));
                        SharedPreferenceWriter.getInstance(MainActivity.this).writeStringValue(SPreferenceKey.EXCHANGE_RATE,""+currenycyAmt);
                        getCurrencySymbol(!currency.equalsIgnoreCase("ZAR")?"USD":"ZAR");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCurrencySymbol(final String currency) {
        try {
            ServicesInterface servicesInterface = ServicesConnection.getInstance().createService(this,"https://www.localeplanet.com/api/auto/");
            Call<JsonObject> call = servicesInterface.getAllCurrencySymbols();
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, false, new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    CommonUtils.dismissLoadingDialog();
                    try {
                        JsonObject object= response.body().getAsJsonObject(currency);
                        SharedPreferenceWriter.getInstance(MainActivity.this).writeStringValue(SPreferenceKey.CURRENCY_SYMBOL, ""+String.valueOf(object.getAsJsonPrimitive("symbol_native")).replace("\"", ""));
                        if(getIntent().getStringExtra("cameFrom")!=null) loadFragment(notificationFragment);
                        else loadFragment(homeFragment);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.clEarnings:
            loadFragment(earningsFragment);
            drawerLayout.closeDrawers();
            break;

            case R.id.clMyOrder:
            startActivity(new Intent(this,MainActivity.class));
            break;

            case R.id.toggleButton:
            isLoad=true;
            break;

            case R.id.menuIv:
            drawerLayout.openDrawer(GravityCompat.START);
            break;

            case R.id.clMyProfile:
            loadFragment(profileFragment);
            drawerLayout.closeDrawers();
            break;

            case R.id.clNotification:
            loadFragment(notificationFragment);
            drawerLayout.closeDrawers();
            break;

            case R.id.clSetting:
            loadFragment(settingFragment);
            drawerLayout.closeDrawers();
            break;

            case R.id.clAboutus:
            staticContentWebviewLoad("About us","driver_about-us");
            break;

            case R.id.clHelp:
            staticContentWebviewLoad("Help","driver_help");
            break;

            case R.id.termsAndConditionCl:
            staticContentWebviewLoad("Terms & Condition","driver_terms");
            break;

            case R.id.clLogout:
            logoutApi();    
            break;

            case R.id.clContactUs:
            loadFragment(contactUsFragment);
            drawerLayout.closeDrawers();
            break;

        }

    }

    private void staticContentWebviewLoad(String title, String urlName) {
        drawerLayout.closeDrawers();
        tvTitle.setText(title);
        toggleButton.setVisibility(View.GONE);
        findViewById(R.id.btnBankDetails).setVisibility(View.GONE);
        Fragment fragment = new WebviewFragment();
        Bundle bundle= new Bundle();
        bundle.putString("url", ServicesConnection.BASE_URL+""+urlName);
        fragment.setArguments(bundle);
        SwitchFragment.changeFragment(getSupportFragmentManager(),fragment,true,true);

    }

    //driver-logout
    private void logoutApi() {
        try {
            ServicesInterface apiInterface= ServicesConnection.getInstance().createService(this);
            Call<CommonModel> call=apiInterface.driverLogout();
            ServicesConnection.getInstance().enqueueWithoutRetry(call, this, true,new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.body()!=null){
                        if(response.body().getStatus())
                        {
                            SPrefrenceValues.removeUserData(MainActivity.this);
                            CommonUtils.startActivity(MainActivity.this, LoginActivity.class);
                        }else
                        {
                            CommonUtils.showSnackBar(MainActivity.this,response.body().getMessage());
                        }

                    }else if(response.errorBody()!=null)
                    {
                        CommonUtils.errorResponse(MainActivity.this,response.errorBody());
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

    private void loadFragment(Fragment fragment) {
        boolean saveInstance=true;
        if(fragment instanceof HomeFragment)
        {
            saveInstance=false;
            tvTitle.setText("My Orders");
            toggleButton.setVisibility(View.VISIBLE);
            findViewById(R.id.btnBankDetails).setVisibility(View.GONE);
        }else if(fragment instanceof MyProfileFragment)
        {
            tvTitle.setText("My Profile");
            toggleButton.setVisibility(View.GONE);
            findViewById(R.id.btnBankDetails).setVisibility(View.VISIBLE);
        }else if(fragment instanceof ContactUsFragment) {
            tvTitle.setText("Contact Us");
            toggleButton.setVisibility(View.GONE);
            findViewById(R.id.btnBankDetails).setVisibility(View.GONE);

        }else if(fragment instanceof SettingFragment)
        {
            tvTitle.setText("Settings");
            toggleButton.setVisibility(View.GONE);
            findViewById(R.id.btnBankDetails).setVisibility(View.GONE);

        }else if(fragment instanceof NotificationFragment)
        {
            tvTitle.setText("Notifications");
            toggleButton.setVisibility(View.GONE);
            findViewById(R.id.btnBankDetails).setVisibility(View.GONE);

        }else if(fragment instanceof EarningsFragment)
        {
            tvTitle.setText("My Earnings");
            toggleButton.setVisibility(View.GONE);
            findViewById(R.id.btnBankDetails).setVisibility(View.GONE);

        }

        SwitchFragment.changeFragment(getSupportFragmentManager(),fragment,saveInstance, true);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            startActivity(new Intent(this,MainActivity.class));
         }
        else if(!isBack) {
         isBack = true;
         CommonUtils.showSnackBar(this, "Press again to exit");
         }
         else
            {
          finishAffinity();
            }
        }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isLoad) {
            CommonUtils.showLoadingDialog(this);
            driverAvailableApi();
        }
    }

    private void driverAvailableApi() {
        //in_progress_order
        try {
            ServicesInterface servicesInterface = ServicesConnection.getInstance().createService(this);
            Call<CommonModel> loginCall = servicesInterface.driverAvailable();

            ServicesConnection.getInstance().enqueueWithoutRetry(loginCall, this, false, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if (response.body() != null) {
                        if (response.body().getStatus()) {
                            driverDetailApi();
                        } else {
                            CommonUtils.dismissLoadingDialog();
                            CommonUtils.showSnackBar(MainActivity.this, response.body().getMessage());
                        }


                    } else if (response.errorBody() != null) {
                        CommonUtils.dismissLoadingDialog();
                        CommonUtils.errorResponse(MainActivity.this, response.errorBody());
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

    private void driverDetailApi() {
        try {
            ServicesInterface servicesInterface = ServicesConnection.getInstance().createService(this);
            Call<CommonModel> loginCall = servicesInterface.driverDetails();

            ServicesConnection.getInstance().enqueueWithoutRetry(loginCall, this, false, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    CommonUtils.dismissLoadingDialog();
                    if (response.body() != null) {
                        if (response.body().getStatus()) {
                            SharedPreferenceWriter.getInstance(MainActivity.this).writeStringValue(SPreferenceKey.ONLINE, ""+response.body().getDriverDetails().getDriver().getOnline());

                        } else {
                            CommonUtils.dismissLoadingDialog();
                            CommonUtils.showSnackBar(MainActivity.this, response.body().getMessage());
                        }


                    } else if (response.errorBody() != null) {
                        CommonUtils.dismissLoadingDialog();
                        CommonUtils.errorResponse(MainActivity.this, response.errorBody());
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


}