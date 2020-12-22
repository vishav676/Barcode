package com.vishav.barcode.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vishav.barcode.Fragments.CardsFragment;
import com.vishav.barcode.Fragments.manualInsert;

public class PageAdapter2 extends FragmentStateAdapter {

    public PageAdapter2(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new manualInsert();
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
}
