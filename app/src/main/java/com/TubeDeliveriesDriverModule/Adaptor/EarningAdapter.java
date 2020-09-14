package com.TubeDeliveriesDriverModule.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.TubeDeliveriesDriverModule.Model.ResponseBean;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.SharePrefrences.SPreferenceKey;
import com.TubeDeliveriesDriverModule.SharePrefrences.SharedPreferenceWriter;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;

import java.util.List;

public class EarningAdapter extends  RecyclerView.Adapter<EarningAdapter.MyViewHolder> {

    private Context context;
    private List<ResponseBean> list;
    private String currencySymbol,countryCode;
    private Double exhangeRate;
    public EarningAdapter(Context context,List<ResponseBean> list)
    {
        this.context=context;
        this.list=list;
        this.currencySymbol= SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.CURRENCY_SYMBOL);
        this.countryCode= SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.CURRENCY);
        this.exhangeRate= Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.EXCHANGE_RATE));
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.adapter_my_earning, parent,false);
       return new EarningAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        list.get(position).setAmount(list.get(position).getAmount().replace("-", "").replace("+", ""));
        holder.tvOrderId.setText("Order id: "+list.get(position).getOrder_number());
        if(countryCode.equalsIgnoreCase("USD")) {
            if(list.get(position).getType().equalsIgnoreCase("credit")) {
                holder.tvAmount.setText(currencySymbol + " " + "+"+ CommonUtils.roundOff(""+Double.parseDouble(list.get(position).getAmount())*exhangeRate) + " cr");
            }
        }else
        {
            if(list.get(position).getType().equalsIgnoreCase("credit")) {
                holder.tvAmount.setText(currencySymbol + " " + "+"+list.get(position).getAmount() + " cr");
            }
        }

        holder.tvOrderDate.setText("Order date: "+list.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount,tvOrderId,tvOrderDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount=itemView.findViewById(R.id.tvAmount);
            tvOrderId=itemView.findViewById(R.id.tvOrderId);
            tvOrderDate=itemView.findViewById(R.id.tvOrderDate);
        }
    }
}
