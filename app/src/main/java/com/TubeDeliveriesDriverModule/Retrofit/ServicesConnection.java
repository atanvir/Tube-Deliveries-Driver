package com.TubeDeliveriesDriverModule.Retrofit;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.SharePrefrences.SPreferenceKey;
import com.TubeDeliveriesDriverModule.SharePrefrences.SharedPreferenceWriter;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicesConnection {

    private static ServicesConnection connect;
    private ServicesInterface clientService;
    public static final int DEFAULT_RETRIES = 0;
    boolean isInValidToken=false;

    public static String BASE_URL="http://3.128.65.155/public/api/";

    public static synchronized ServicesConnection getInstance() {
        if (connect == null) {
            connect = new ServicesConnection();
        }
        return connect;
    }

    public ServicesInterface createService(final Context context) throws Exception {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);//
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.readTimeout(1, TimeUnit.MINUTES);
            httpClient.readTimeout(1, TimeUnit.MINUTES);
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request;
                    request = original.newBuilder()
                            .header("Authorization", SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.TOKEN_TYPE)+" "+SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.ACCESS_TOKEN))
                            .method(original.method(),original.body())
                            .build();
                    return chain.proceed(request);
                }

            });
            httpClient.addInterceptor(logging);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();

            clientService = retrofit.create(ServicesInterface.class);

        return clientService;
    }
    public ServicesInterface createService(final Context context,String BASE_URL) throws Exception {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);//
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(1, TimeUnit.MINUTES);
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        clientService = retrofit.create(ServicesInterface.class);

        return clientService;
    }

    //    enqueue
    public <T> boolean enqueueWithRetry(Call<T> call, final Activity activity, final boolean isLoader, final int retryCount, final Callback<T> callback) {
        if (CommonUtils.networkConnectionCheck(activity)) {
            if(isLoader)
            {
                if(activity!=null){
                    CommonUtils.showLoadingDialog(activity);
                }
            }
            call.enqueue(new ServicesRetryableCallback<T>(call, retryCount)
            {
                @Override
                public void onFinalResponse(Call<T> call, Response<T> response)
                {
                    if(CommonUtils.customLoader !=null)
                    {
                        if(isLoader) {
                            CommonUtils.dismissLoadingDialog();
                        }
                    }
                    callback.onResponse(call, response);
//                    checkValidUser(activity,callback,call,response);
                }

                @Override
                public void onFinalFailure(Call<T> call, Throwable t)
                {
                    if(CommonUtils.customLoader !=null)
                    {

                        CommonUtils.dismissLoadingDialog();
                    }
                    if(t instanceof SocketTimeoutException)
                    {


                    }


                    callback.onFailure(call, t);
                }
            });
           return true;
        } else {
            CommonUtils.showSnackBar(activity,activity.getString(R.string.no_internet));
            return false;
        }
    }

//    private <T> void checkValidUser(Context context,Callback<T> callback, Call<T> call, Response<T> response) {
//        boolean ret=true;
//        if(response.body() instanceof CommonModel)
//        {
//            if(((CommonModel) response.body()).getMessage().equalsIgnoreCase("Invalid Token"))
//            {
//               ret=false;
//            }
//        }else if(response.body() instanceof CommonDataStringModel)
//        {
//            if(((CommonDataStringModel) response.body()).getMessage().equalsIgnoreCase("Invalid Token"))
//            {
//                ret=false;
//            }
//        }else if(response.body() instanceof CommonListModel)
//        {
//            if(((CommonListModel) response.body()).getMessage().equalsIgnoreCase("Invalid Token"))
//            {
//                ret=false;
//            }
//        }
//
//        if(ret) {
//            callback.onResponse(call, response);
//        }else
//        {
//            Toast.makeText(context, R.string.login_expire, Toast.LENGTH_SHORT).show();
//            SPrefrenceValues.removeUserData(context);
//            CommonUtils.startActivity(context,LoginActivity.class);
//
//        }
//    }




    public  <T> boolean  enqueueWithoutRetry(Call<T> call, Activity activity, boolean isLoader, final Callback<T> callback) {
        return enqueueWithRetry(call, activity,isLoader, DEFAULT_RETRIES, callback);
    }
}
