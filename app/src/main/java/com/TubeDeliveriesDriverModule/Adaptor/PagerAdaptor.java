package com.TubeDeliveriesDriverModule.Adaptor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerAdaptor extends FragmentPagerAdapter {

    List<Fragment> mFragmentList = new ArrayList<>();

    public PagerAdaptor(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public PagerAdaptor(@NonNull FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment,String title) {
        mFragmentList.add(fragment);
        notifyDataSetChanged();
    }
}
