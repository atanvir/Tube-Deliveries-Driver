package com.TubeDeliveriesDriverModule.Fragment;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TubeDeliveriesDriverModule.Adaptor.EarningAdapter;
import com.TubeDeliveriesDriverModule.Model.CommonModel;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesConnection;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesInterface;
import com.TubeDeliveriesDriverModule.SharePrefrences.SPreferenceKey;
import com.TubeDeliveriesDriverModule.SharePrefrences.SharedPreferenceWriter;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;
import com.TubeDeliveriesDriverModule.Utils.ImageGlider;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarningsFragment extends Fragment {

    RecyclerView rvEarnings;
    TextView tvAmount;
    String currencySymbol,currency;
    Double exchangeRate;
    CircleImageView ciProfilePic;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_earnings, container,false);
        rvEarnings=view.findViewById(R.id.rvEarnings);
        tvAmount=view.findViewById(R.id.tvAmount);
        ciProfilePic=view.findViewById(R.id.ciProfilePic);
        progressBar=view.findViewById(R.id.progressBar);
        ImageGlider.setNormalImage(getActivity(), ciProfilePic, progressBar, SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.IMAGE));
        currencySymbol=SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.CURRENCY_SYMBOL);
        exchangeRate=Double.parseDouble(SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.EXCHANGE_RATE));
        currency=SharedPreferenceWriter.getInstance(getActivity()).getString(SPreferenceKey.CURRENCY);

        transactionApi();
        return view;
    }

    private void transactionApi() {
        try {
           ServicesInterface apiInterface= ServicesConnection.getInstance().createService(getActivity());
           Call<CommonModel> call= apiInterface.getTransactionsHistory();
           ServicesConnection.getInstance().enqueueWithoutRetry(call, getActivity(), true, new Callback<CommonModel>() {
               @Override
               public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                   if(response.isSuccessful())
                   {
                       CommonModel serverResponse=response.body();
                       if(serverResponse.getStatus())
                       {
                           rvEarnings.setLayoutManager(new LinearLayoutManager(getActivity()));
                           rvEarnings.setAdapter(new EarningAdapter(getActivity(),serverResponse.getTranscations()));
                           Double amount=0.0;
                           for(int i=0;i<serverResponse.getTranscations().size();i++)
                           {
                               amount+=Double.parseDouble( serverResponse.getTranscations().get(i).getAmount().replace("-", ""));
                           }
                          if(currency.equalsIgnoreCase("USD"))
                          {
                              tvAmount.setText(currencySymbol+" "+ CommonUtils.roundOff(""+amount*exchangeRate)+" cr");
                          }else
                          {
                              tvAmount.setText(currencySymbol+" "+CommonUtils.roundOff(""+amount)+" cr");
                          }
                       }
                   }

               }

               @Override
               public void onFailure(Call<CommonModel> call, Throwable t) {

               }
           });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
