package com.TubeDeliveriesDriverModule.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TubeDeliveriesDriverModule.Model.ResponseBean;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.SharePrefrences.SPreferenceKey;
import com.TubeDeliveriesDriverModule.SharePrefrences.SharedPreferenceWriter;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;
import com.TubeDeliveriesDriverModule.Utils.ImageGlider;
import com.badoualy.stepperindicator.StepperIndicator;

import java.util.List;

public class CompletedOrderAdapter extends RecyclerView.Adapter<CompletedOrderAdapter.OnGoingViewHolder> {
    private Context context;
    private List<ResponseBean> onGoingList;
    private Double exchangeRate;
    private String currencySymbol;

    public CompletedOrderAdapter(Context context, List<ResponseBean> onGoingList) {
        this.context = context;
        this.onGoingList = onGoingList;
        this.exchangeRate=Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.EXCHANGE_RATE));
        this.currencySymbol=SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.CURRENCY_SYMBOL);
    }

    @NonNull
    @Override
    public OnGoingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_history_items,viewGroup,false);
        return new OnGoingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnGoingViewHolder onGoingViewHolder, int i) {
        ImageGlider.setRoundImage(context,onGoingViewHolder.ivOrderLogo,null , onGoingList.get(i).getSeller().getImage());
        onGoingViewHolder.tvRestaurantName.setText(onGoingList.get(i).getSeller().getName());
        onGoingViewHolder.tvOrderId.setText("Order Id: "+onGoingList.get(i).getOrder_number());
        onGoingViewHolder.tvDateAndTime.setText(onGoingList.get(i).getOrderDate());
        if(onGoingList.get(i).getCurrency().equalsIgnoreCase("USD"))
        {
            onGoingViewHolder.tvTotalPriceMyOrder.setText("Total Price: " + currencySymbol + " " + CommonUtils.roundOff(""+Double.parseDouble(onGoingList.get(i).getTotalPrice())*exchangeRate));
        }else {
            onGoingViewHolder.tvTotalPriceMyOrder.setText("Total Price: " + currencySymbol + " " + onGoingList.get(i).getTotalPrice());
        }
        onGoingViewHolder.tvDeliveryPerson.setText(onGoingList.get(i).getUser().getName());
        onGoingViewHolder.tvAddress.setText(onGoingList.get(i).getUser().getAddress());
        onGoingViewHolder.tvAddressMyOrders.setText(onGoingList.get(i).getUser().getAddress());
        onGoingViewHolder.tvPaymentModeType.setText(onGoingList.get(i).getPayment_type());
        onGoingViewHolder.recyclerMyOrders.setLayoutManager(new LinearLayoutManager(context));
        onGoingViewHolder.tvMobile.setText(onGoingList.get(i).getUser().getPhone());
        onGoingViewHolder.recyclerMyOrders.setAdapter(new MyOrderAdapter(context,onGoingList.get(i).getOrderMenu(),onGoingList.get(i).getCurrency()));
        if(i==0)
        {
            onGoingViewHolder.ivDropDownOrder.setImageResource(R.drawable.drop_down_ic);
            onGoingViewHolder.constraintLayout.setBackground(context.getDrawable(R.drawable.bg_expand));
            onGoingViewHolder.cl_myOrders.setVisibility(View.VISIBLE);
        }
        if(onGoingList.get(i).getDeliveryStatus().equalsIgnoreCase("Driver has rejected")) onGoingViewHolder.tvDeliveryStatus.setText("Rejected");
        else onGoingViewHolder.tvDeliveryStatus.setText(onGoingList.get(i).getDeliveryStatus());
    }


    @Override
    public int getItemCount() {
        return onGoingList!=null?onGoingList.size():0;
    }

    public class OnGoingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RecyclerView recyclerMyOrders;
        ConstraintLayout cl_myOrders;
        ImageView ivDropDownOrder;
        ImageView ivOrderLogo;
        TextView tvRestaurantName;
        TextView tvTotalPriceMyOrder;
        TextView tvOrderId;
        TextView tvDateAndTime;
        TextView tvRestaurantNameBelow;
        TextView tvAddressMyOrders;
        TextView tvPaymentModeType;
        TextView tvDeliveryPerson;
        TextView tvAddress;
        ConstraintLayout clDeliveryDetails;
        ConstraintLayout constraintLayout;
        ConstraintLayout mainCl;
        TextView tvMobile;
        TextView tvDeliveryStatus;



        public OnGoingViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerMyOrders=itemView.findViewById(R.id.recyclerMyOrders);
            cl_myOrders=itemView.findViewById(R.id.cl_myOrders);
            ivOrderLogo=itemView.findViewById(R.id.ivOrderLogo);
            tvRestaurantName=itemView.findViewById(R.id.tvRestaurantName);
            tvTotalPriceMyOrder=itemView.findViewById(R.id.tvTotalPriceMyOrder);
            tvOrderId=itemView.findViewById(R.id.tvOrderId);
            tvDateAndTime=itemView.findViewById(R.id.tvDateAndTime);
            tvRestaurantNameBelow=itemView.findViewById(R.id.tvRestaurantNameBelow);
            tvAddressMyOrders=itemView.findViewById(R.id.tvAddressMyOrders);
            tvPaymentModeType=itemView.findViewById(R.id.tvPaymentModeType);
            tvDeliveryPerson=itemView.findViewById(R.id.tvDeliveryPerson);
            tvAddress=itemView.findViewById(R.id.tvAddress);
            constraintLayout=itemView.findViewById(R.id.constraintLayout);
            clDeliveryDetails=itemView.findViewById(R.id.clDeliveryDetails);
            ivDropDownOrder=itemView.findViewById(R.id.ivDropDownOrder);
            tvMobile=itemView.findViewById(R.id.tvMobile);
            tvDeliveryStatus=itemView.findViewById(R.id.tvDeliveryStatus);
            mainCl=itemView.findViewById(R.id.mainCl);
            mainCl.setOnClickListener(this);
            tvAddressMyOrders.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.mainCl:
                if(cl_myOrders.getVisibility()== View.GONE){
                    ivDropDownOrder.setImageResource(R.drawable.drop_down_ic);
                    constraintLayout.setBackground(context.getDrawable(R.drawable.bg_expand));
                    cl_myOrders.setVisibility(View.VISIBLE
                    );
                }else {
                    ivDropDownOrder.setImageResource(R.drawable.drop_down_icon);
                    constraintLayout.setBackground(context.getDrawable(R.drawable.bg_collapse));
                    cl_myOrders.setVisibility(View.GONE);
                }
                break;


                case R.id.tvAddressMyOrders:
                CommonUtils.startGoogleMap(context,onGoingList.get(getAdapterPosition()).getUser().getLatitude(),onGoingList.get(getAdapterPosition()).getUser().getLongitude());
                break;


            }
        }
    }



}
