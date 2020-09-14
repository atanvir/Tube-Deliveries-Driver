package com.TubeDeliveriesDriverModule.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TubeDeliveriesDriverModule.Activity.MainActivity;
import com.TubeDeliveriesDriverModule.Adaptor.CommonImageAdpter;
import com.TubeDeliveriesDriverModule.Adaptor.CompletedOrderAdapter;
import com.TubeDeliveriesDriverModule.Adaptor.OnGoingOrderAdapter;
import com.TubeDeliveriesDriverModule.Model.CommonModel;
import com.TubeDeliveriesDriverModule.Model.ResponseBean;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesConnection;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesInterface;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;
import com.airbnb.lottie.LottieAnimationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener, OnGoingOrderAdapter.ChangeOrderStatus {
    private TextView tvProgress,tvHistory;
    private RecyclerView rv;
    private LottieAnimationView lottie;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view =inflater.inflate(R.layout.fragment_home, container,false);
        tvProgress = view.findViewById(R.id.tvProgress);
        tvHistory=view.findViewById(R.id.tvHistory);
        lottie=view.findViewById(R.id.lottie);
        rv=view.findViewById(R.id.rv);
        tvProgress.setOnClickListener(this);
        tvHistory.setOnClickListener(this);
        setUpReclerView(null);
        inProgressApi(true);

        return view;
    }

    private void setUpReclerView(List<ResponseBean> responseBeanList) {
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new OnGoingOrderAdapter(getContext(),responseBeanList,null));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tvProgress:
            settingBackground("In progress");
            inProgressApi(true);
            break;

            case R.id.tvHistory:
            settingBackground("History");
            historyApi(true);
            break;
        }
    }

    private void historyApi(boolean isLoader) {
        try {
            ServicesInterface servicesInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonModel> loginCall = servicesInterface.history();

            ServicesConnection.getInstance().enqueueWithoutRetry(loginCall, getActivity(), isLoader, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    CommonUtils.dismissLoadingDialog();
                    if (response.body() != null) {
                        if (response.body().getStatus()) {
                            if(response.body().getOngoing_order().size()>0) {
                                lottie.setVisibility(View.GONE);
                            }else {
                                lottie.setVisibility(View.VISIBLE);
                            }

                            rv.setLayoutManager(new LinearLayoutManager(getContext()));
                            rv.setAdapter(new CompletedOrderAdapter(getActivity(), response.body().getOngoing_order()));

                        } else {
                            CommonUtils.showSnackBar(getActivity(), response.body().getMessage());
                        }
                    } else if (response.errorBody() != null) {
                        CommonUtils.errorResponse(getActivity(), response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                   // Log.e("failure", t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void inProgressApi(boolean isLoader) {
        try {
            ServicesInterface servicesInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonModel> loginCall = servicesInterface.inProgress();

            ServicesConnection.getInstance().enqueueWithoutRetry(loginCall, getActivity(), isLoader, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    CommonUtils.dismissLoadingDialog();
                    if (response.body() != null) {
                        if (response.body().getStatus()) {
                            if(response.body().getOngoing_order().size()>0) {
                                lottie.setVisibility(View.GONE);

                            }else
                            {
                                lottie.setVisibility(View.VISIBLE);
                            }
                            rv.setLayoutManager(new LinearLayoutManager(getContext()));
                            rv.setAdapter(new OnGoingOrderAdapter(getContext(), response.body().getOngoing_order(), HomeFragment.this));
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


    private void settingBackground(String type) {
        if(type.equalsIgnoreCase("In progress"))
        {
            tvProgress.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            tvProgress.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.drawable_in_progress));
            tvHistory.setBackground(null);
            tvHistory.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));

        }else
        {
            tvHistory.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            tvHistory.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_layout_history));
            tvProgress.setBackground(null);
            tvProgress.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));
        }

    }

    @Override
    public void onStatusChange(String status,String orderId) {
        CommonUtils.showLoadingDialog(getActivity());
        changeOrderStatusApi(orderId,getStatus(status));
    }

    private String getStatus(String status) {
        String returnValue="";
        switch (status)
        {
            case "Rejected": returnValue="9"; break;
            case "Delivered": returnValue="5"; break;
            case "Delivery has arrived": returnValue="8"; break;

        }

        return returnValue;
    }

    private void changeOrderStatusApi(String orderId, final String status) {
        try {
            ServicesInterface servicesInterface = ServicesConnection.getInstance().createService(getActivity());
            Call<CommonModel> loginCall = servicesInterface.changeOrderStatus(orderId,status);

            ServicesConnection.getInstance().enqueueWithoutRetry(loginCall, getActivity(), false, new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    if (response.body() != null) {
                        if (response.body().getStatus()) {
                            if(status.equalsIgnoreCase("5") || status.equalsIgnoreCase("9")) {
                                settingBackground("History");
                                historyApi(false);
                            }else {
                                settingBackground("In progress");
                                inProgressApi(false);
                            }

                        } else {
                            CommonUtils.dismissLoadingDialog();
                            CommonUtils.showSnackBar(getActivity(), response.body().getMessage());
                        }


                    } else if (response.errorBody() != null) {
                        CommonUtils.dismissLoadingDialog();
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
}
