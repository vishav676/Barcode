package com.vishav.barcode;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PageAdapter extends FragmentStateAdapter {

    public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TicketsFragment();
            case 1:
                return new CardsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
    /*public PageAdapter(@NonNull FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TicketsFragment();
            case 1:
                return new CardsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }*/
}
