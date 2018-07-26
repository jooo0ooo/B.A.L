package com.pingrae.bal;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ManualFragment extends Fragment{
    int MAX_PAGE=4;
    Fragment cur_fragment=new Fragment();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final ViewGroup rootGroup =(ViewGroup)inflater.inflate(R.layout.fragment_manual,container,false);

        ViewPager viewPager=(ViewPager)rootGroup.findViewById(R.id.viewpager);
        viewPager.setAdapter(new adapter(getChildFragmentManager()));
        return rootGroup;
    }

    private class adapter extends FragmentPagerAdapter {
        public adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position<0 || MAX_PAGE<=position)
                return null;
            switch (position){
                case 0:
                    cur_fragment=new manual_page_1();
                    break;

                case 1:
                    cur_fragment=new manual_page_2();
                    break;

                case 2:
                    cur_fragment=new manual_page_3();
                    break;

                case 3:
                    cur_fragment=new manual_page_4();
                    break;

            }

            return cur_fragment;
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}

