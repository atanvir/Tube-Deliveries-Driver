package com.TubeDeliveriesDriverModule.Adaptor;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.TubeDeliveriesDriverModule.Model.OrderMenu;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.SharePrefrences.SPreferenceKey;
import com.TubeDeliveriesDriverModule.SharePrefrences.SharedPreferenceWriter;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;
import com.bumptech.glide.Glide;

import java.util.List;


public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderViewHolder> {
    private Context context;
    private List<OrderMenu> list;
    private Double exchangeRate;
    private String currencySymbol;
    private String currency;

    public MyOrderAdapter(Context context, List<OrderMenu> list,String currency) {
        this.context = context;
        this.list = list;
        this.exchangeRate=Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.EXCHANGE_RATE));
        this.currencySymbol=SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.CURRENCY_SYMBOL);
        this.currency=currency;
    }

    @NonNull
    @Override
    public MyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_my_order_adapter,viewGroup,false);
        return new MyOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderViewHolder myOrderViewHolder, int i) {
        if(list.get(i).getImage()!=null && !list.get(i).getImage().isEmpty())
            Glide.with(context).load(list.get(i).getImage()).into(myOrderViewHolder.ivOrderAdapter);
            myOrderViewHolder.tvOrderNameAdapter.setText(list.get(i).getName());
            Double price;
            if(currency.equalsIgnoreCase("USD"))
            {
                price=list.get(i).getPrice()*exchangeRate;
            }else
            {
                price=list.get(i).getPrice();
            }
           myOrderViewHolder.tvCerditAdapter.setText(currencySymbol+" "+ CommonUtils.roundOff(""+price));

           myOrderViewHolder.tcOrderQuantPrice.setText(list.get(i).getQuantity()+"x"+currency+" "+CommonUtils.roundOff(""+price));

        if (String.valueOf(list.get(i).getItemType()).equalsIgnoreCase("0")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myOrderViewHolder.ivVegOrNonVeg.setBackground(context.getResources().getDrawable(R.drawable.veg_symbol, null));
            } else {
                myOrderViewHolder.ivVegOrNonVeg.setBackground(ContextCompat.getDrawable(context, R.drawable.veg_symbol));
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myOrderViewHolder.ivVegOrNonVeg.setBackground(context.getResources().getDrawable(R.drawable.nonveg_symbol, null));
            } else {
                myOrderViewHolder.ivVegOrNonVeg.setBackground(ContextCompat.getDrawable(context, R.drawable.nonveg_symbol));

            }
        }

    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyOrderViewHolder extends RecyclerView.ViewHolder {

        ImageView ivOrderAdapter,ivVegOrNonVeg;
        TextView tvOrderNameAdapter,tvCerditAdapter,tcOrderQuantPrice;

        public MyOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivOrderAdapter=itemView.findViewById(R.id.ivOrderAdapter);
            ivVegOrNonVeg=itemView.findViewById(R.id.ivVegOrNonVeg);
            tvOrderNameAdapter=itemView.findViewById(R.id.tvOrderNameAdapter);
            tvCerditAdapter=itemView.findViewById(R.id.tvCerditAdapter);
            tcOrderQuantPrice=itemView.findViewById(R.id.tcOrderQuantPrice);
        }
    }
}
