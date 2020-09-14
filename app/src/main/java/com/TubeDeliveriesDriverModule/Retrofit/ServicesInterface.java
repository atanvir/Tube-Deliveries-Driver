package com.TubeDeliveriesDriverModule.Retrofit;

import com.TubeDeliveriesDriverModule.Model.CommonModel;
import com.TubeDeliveriesDriverModule.Model.MyCurrentDetailsModel;
import com.TubeDeliveriesDriverModule.Model.SignupModel;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServicesInterface {

    @POST(AppConstants.STEP_ONE)
    Call<CommonModel> stepOneAPi(@Body SignupModel model);

    @GET(AppConstants.BRAND)
    Call<List<CommonModel>> brandList();

    @Multipart
    @POST(AppConstants.STEP_TWO)
    Call<CommonModel> stepTwo(@Part MultipartBody.Part driver_image,
                              @Part MultipartBody.Part vehicle_images[],
                              @Part MultipartBody.Part license_image,
                              @Part MultipartBody.Part insurance_image,
                              @PartMap Map<String, RequestBody> allData);

    @POST(AppConstants.STEP_THREE)
    Call<CommonModel> stepThreeApi(@Body SignupModel model);


    @Multipart
    @POST(AppConstants.REGISTER)
    Call<CommonModel> driver_register(@Part MultipartBody.Part driver_image,
                                      @Part MultipartBody.Part vehicle_images[],
                                      @Part MultipartBody.Part license_image,
                                      @Part MultipartBody.Part insurance_image,
                                      @PartMap Map<String, RequestBody> allData);


    @FormUrlEncoded
    @POST(AppConstants.LOGIN)
    Call<CommonModel> driver_login(@Field("type") String type,
                                   @Field("phone") String phone,
                                   @Field("email") String email,
                                   @Field("password") String password,
                                   @Field("device_type") String device_type,
                                   @Field("device_token") String device_token,
                                   @Field("latitude") String latitude,
                                   @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST(AppConstants.FORGOT_PASSWORD)
    Call<CommonModel> forgetPass(@Field("email") String email);

    @GET(AppConstants.DRIVER_DETAILS)
    Call<CommonModel> driverDetails();

    @GET(AppConstants.LOGOUT)
    Call<CommonModel> driverLogout();

    @Multipart
    @POST(AppConstants.DRIVER_UPDATE)
    Call<CommonModel> updateDriver(@Part MultipartBody.Part driver_image,
                                      @Part MultipartBody.Part vehicle_images[],
                                      @Part MultipartBody.Part license_image,
                                      @Part MultipartBody.Part insurance_image,
                                      @PartMap Map<String, RequestBody> allData);

    @FormUrlEncoded
    @POST(AppConstants.DRIVER_PASSWORD)
    Call<CommonModel> driverPassword(@Field("old_password") String old_password,
                                     @Field("new_password") String new_password);

    @GET(AppConstants.DRIVER_AVAILABILITY)
    Call<CommonModel> driverAvailable();

    @GET(AppConstants.IN_PROGRESS)
    Call<CommonModel> inProgress();

    @GET(AppConstants.HISTORY)
    Call<CommonModel> history();

    @GET(AppConstants.NOTIFICATIONS)
    Call<CommonModel> driverNotifications();

    @GET(AppConstants.ACCEPT_ORDER+"/{orderId}")
    Call<CommonModel> acceptOrder(@Path("orderId") String orderId);

    @FormUrlEncoded
    @POST(AppConstants.CHANGE_ORDER_STATUS)
    Call<CommonModel> changeOrderStatus(@Field("oid") String orderId,@Field("status") String status);

    @GET(AppConstants.GET_MY_DETAILS)
    Call<MyCurrentDetailsModel> getMyDeatails();

    @GET(AppConstants.GET_CURRENCY)
    Call<MyCurrentDetailsModel> getCurrency(@Path("code") String countryCode);


    @GET(AppConstants.EXCHAGE_RATE)
    Call<JsonObject> getExchangeRate(@Query("base") String base, @Query("symbols") String symbols);

    @GET(AppConstants.GET_CURRENCY_SYMBOL)
    Call<JsonObject> getAllCurrencySymbols();

    @GET(AppConstants.TRANSACTIONS)
    Call<CommonModel> getTransactionsHistory();
}







