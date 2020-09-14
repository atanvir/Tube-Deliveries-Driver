package com.TubeDeliveriesDriverModule.Adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.TubeDeliveriesDriverModule.Activity.LoginActivity;
import com.TubeDeliveriesDriverModule.Activity.MainActivity;
import com.TubeDeliveriesDriverModule.Model.CommonModel;
import com.TubeDeliveriesDriverModule.Model.ResponseBean;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesConnection;
import com.TubeDeliveriesDriverModule.Retrofit.ServicesInterface;
import com.TubeDeliveriesDriverModule.SharePrefrences.SPrefrenceValues;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;
import com.TubeDeliveriesDriverModule.Utils.ImageGlider;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdaptor extends RecyclerView.Adapter<NotificationAdaptor.NotifcHolder> {
    private Context context;
    private List<ResponseBean> list;
    private NotificationClick listner;

    public NotificationAdaptor(Context context, List<ResponseBean> list,NotificationClick listner) {
        this.context = context;
        this.list = list;
        this.listner=listner;
    }

    @NonNull
    @Override
    public NotifcHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.notification_layout,parent,false);
        return new NotifcHolder(view);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(@NonNull NotifcHolder holder, int position) {
        if(list.get(position).getBody().contains("Pickup Location :"))
        {
            holder.tvTitle.append(CommonUtils.getColoredString("Pickup Location :", ContextCompat.getColor(context,R.color.black)));
            holder.tvTitle.append(CommonUtils.getColoredString(list.get(position).getBody().split("Pickup Location :")[1],ContextCompat.getColor(context,R.color.colorPrimaryDark)));
        }else
        {
            holder.tvTitle.setText(list.get(position).getBody());
        }
        holder.tvTitle.setText(list.get(position).getBody());
        if(list.get(position).getType().equalsIgnoreCase("pickup")) {
            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.btnDecline.setVisibility(View.VISIBLE);
        }
        if(!list.get(position).getAcceptance().equalsIgnoreCase("0"))
        {
            holder.btnAccept.setVisibility(View.GONE);
            holder.btnDecline.setVisibility(View.GONE);
        }
        holder.tvDateAndTime.setText(CommonUtils.getMyPrettyDate(CommonUtils.milliseconds(list.get(position).getDate())));
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class NotifcHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ProgressBar progressBar;
        TextView tvDateAndTime,tvTitle;
        CircleImageView ciProfilePic;
        Button btnAccept,btnDecline;

        public NotifcHolder(@NonNull View itemView) {
        super(itemView);
        ciProfilePic=itemView.findViewById(R.id.ciProfilePic);
        progressBar=itemView.findViewById(R.id.progressBar);
        tvTitle=itemView.findViewById(R.id.tvTitle);
        tvDateAndTime=itemView.findViewById(R.id.tvDateAndTime);
        btnAccept=itemView.findViewById(R.id.btnAccept);
        btnDecline=itemView.findViewById(R.id.btnDecline);
        btnAccept.setOnClickListener(this);
        btnDecline.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAccept:
                if (listner != null) {
                    listner.onRequestAccept(list.get(getAdapterPosition()).getOrder_id());
                }
                break;

                case R.id.btnDecline:
                context.startActivity(new Intent(context, MainActivity.class));
                break;
            }
        }
    }


    public interface NotificationClick
    {
        void onRequestAccept(String orderId);
    }
}
