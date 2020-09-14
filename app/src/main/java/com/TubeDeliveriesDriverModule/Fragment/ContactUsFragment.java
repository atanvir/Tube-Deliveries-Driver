package com.TubeDeliveriesDriverModule.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TubeDeliveriesDriverModule.Activity.MainActivity;
import com.TubeDeliveriesDriverModule.R;
import com.TubeDeliveriesDriverModule.Utils.CommonUtils;

public class ContactUsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_contact_us, container, false);
        return view;
    }


}