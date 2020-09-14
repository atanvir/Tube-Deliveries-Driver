package com.TubeDeliveriesDriverModule.Adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;
import com.TubeDeliveriesDriverModule.Utils.ImageGlider;

import java.io.File;
import java.util.List;

public class CommonImageAdpter extends RecyclerView.Adapter<CommonImageAdpter.MyViewHolder> {

    private Context context;
    private List<String> list;
    private onImageClick listner;
    private String type;

    public CommonImageAdpter(Context context,List<String> list,onImageClick listner,String type)
    {
        this.context=context;
        this.list=list;
        this.listner=listner;
        this.type=type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.adpter_custom_image, parent,false);
       return new CommonImageAdpter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ImageGlider.setRoundImage(context,holder.ivPhoto,holder.progressBar,""+Uri.parse(list.get(position)));
    }


    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ProgressBar progressBar;
        ImageView ivPhoto;
        ImageView ivRemove;
        public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar=itemView.findViewById(R.id.progressBar);
        ivPhoto=itemView.findViewById(R.id.ivPhoto);
        ivRemove=itemView.findViewById(R.id.ivRemove);
        ivRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.ivRemove:
                if(listner!=null) {
                listner.onRemove(type, getAdapterPosition());
                }
                break;
            }

        }



    }

    public interface onImageClick
    {
        void onRemove(String type,int pos);
    }
}
