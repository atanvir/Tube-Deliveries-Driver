package com.TubeDeliveriesDriverModule.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
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
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OnGoingOrderAdapter extends RecyclerView.Adapter<OnGoingOrderAdapter.OnGoingViewHolder> {
    private Context context;
    private List<ResponseBean> onGoingList;
    private List<String> statusList;
    private ChangeOrderStatus listner;
    private Double exchangeRate;
    private String currencySymbol;

    public OnGoingOrderAdapter(Context context, List<ResponseBean> onGoingList,ChangeOrderStatus listner) {
        this.context = context;
        this.onGoingList = onGoingList;
        this.statusList=new ArrayList<>();
        statusList.add(0,"Please select status");
        statusList.add(1,"Delivery has arrived");
        statusList.add(2,"Delivered");
        statusList.add(3,"Rejected");
        this.listner=listner;
        this.exchangeRate=Double.parseDouble(SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.EXCHANGE_RATE));
        this.currencySymbol=SharedPreferenceWriter.getInstance(context).getString(SPreferenceKey.CURRENCY_SYMBOL);
    }

    @NonNull
    @Override
    public OnGoingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_ongoing_items,viewGroup,false);
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
        onGoingViewHolder.edStatus.setText(onGoingList.get(i).getDeliveryStatus());
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
    }


    @Override
    public int getItemCount() {
        return onGoingList!=null?onGoingList.size():0;
    }

    public class OnGoingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, AdapterView.OnItemSelectedListener {
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
     TextInputEditText edStatus;
     Spinner spnStatus;



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
            mainCl=itemView.findViewById(R.id.mainCl);
            ivDropDownOrder=itemView.findViewById(R.id.ivDropDownOrder);
            tvMobile=itemView.findViewById(R.id.tvMobile);
            edStatus=itemView.findViewById(R.id.edStatus);
            spnStatus=itemView.findViewById(R.id.spnStatus);
            mainCl.setOnClickListener(this);
            tvAddressMyOrders.setOnClickListener(this);
            edStatus.setOnClickListener(this);
            spnStatus.setOnItemSelectedListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.mainCl:
                if(cl_myOrders.getVisibility()== View.GONE){
                    ivDropDownOrder.setImageResource(R.drawable.drop_down_ic);
                    constraintLayout.setBackground(context.getDrawable(R.drawable.bg_expand));
                    cl_myOrders.setVisibility(View.VISIBLE);
                }else {
                    ivDropDownOrder.setImageResource(R.drawable.drop_down_icon);
                    constraintLayout.setBackground(context.getDrawable(R.drawable.bg_collapse));
                    cl_myOrders.setVisibility(View.GONE);
                }
                break;


                case R.id.tvAddressMyOrders:
                CommonUtils.startGoogleMap(context,onGoingList.get(getAdapterPosition()).getUser().getLatitude(),onGoingList.get(getAdapterPosition()).getUser().getLongitude());
                break;

                case R.id.edStatus:
                if(onGoingList.get(getAdapterPosition()).getDeliveryStatus().equalsIgnoreCase("Out For Delivery") || onGoingList.get(getAdapterPosition()).getDeliveryStatus().equalsIgnoreCase("Delivery has arrived")) {
                    CommonUtils.setSpinner(context, statusList, spnStatus);
                }else
                {
                    CommonUtils.showSnackBar(context,"You can change the order status once after order is out for delivery");
                }
                break;
            }
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position!=0)
            {
                if(listner!=null){
                    edStatus.setText(statusList.get(position));
                    listner.onStatusChange(statusList.get(position),onGoingList.get(getAdapterPosition()).getOrder_number());
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }


    public interface ChangeOrderStatus{
        void onStatusChange(String status,String orderId);
    }

}
