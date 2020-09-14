package com.TubeDeliveriesDriverModule.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TubeDeliveriesDriverModule.Activity.MainActivity;
import com.TubeDeliveriesDriverModule.Adaptor.NotificationAdaptor;
import com.TubeDeliveriesDriverModule.Model.CommonModel;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesConnection;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesInterface;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment implements NotificationAdaptor.NotificationClick {
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView= view.findViewById(R.id.recycleView_notification);
        driverNotificationsApi(true);
        return view;
    }

    private void driverNotificationsApi(boolean loader) {
        try {
            ServicesInterface servicesInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonModel> loginCall = servicesInterface.driverNotifications();

            ServicesConnection.getInstance().enqueueWithoutRetry(loginCall, getActivity(), loader, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    CommonUtils.dismissLoadingDialog();
                    if (response.body() != null) {
                        if (response.body().getStatus()) {
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(new NotificationAdaptor(getActivity(), response.body().getNotificationList(),NotificationFragment.this));
                        } else {
                            CommonUtils.showSnackBar(getActivity(), response.body().getMessage());
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

    @Override
    public void onRequestAccept(String orderId) {
        CommonUtils.showLoadingDialog(getActivity());
        acceptOrderApi(orderId);
    }

    private void acceptOrderApi(String orderId) {
        try {
            ServicesInterface apiInterface= ServicesConnection.getInstance().createService(getActivity());
            Call<CommonModel> call=apiInterface.acceptOrder(orderId);
            ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), false,new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if(response.body()!=null){
                        if(response.body().getStatus())
                        {
                            CommonUtils.dismissLoadingDialog();
                            startActivity(new Intent(getActivity(),MainActivity.class));

                        }else
                        {
                            CommonUtils.dismissLoadingDialog();
                            CommonUtils.showSnackBar(getActivity(),response.body().getMessage());
                        }

                    }else if(response.errorBody()!=null)
                    {
                        CommonUtils.dismissLoadingDialog();
                        CommonUtils.errorResponse(getActivity(),response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
//                    Log.e("failure", t.getMessage());

                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}