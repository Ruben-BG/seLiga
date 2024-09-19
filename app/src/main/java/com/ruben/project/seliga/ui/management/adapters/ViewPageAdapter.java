package com.ruben.project.seliga.ui.management.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ruben.project.seliga.ui.management.fragments.ChargeFragment;
import com.ruben.project.seliga.ui.management.fragments.ClientFragment;
import com.ruben.project.seliga.ui.management.fragments.PaymentFragment;

public class ViewPageAdapter extends FragmentStateAdapter {

    public ViewPageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new PaymentFragment();
            case 2:
                return new ChargeFragment();
            default:
                return new ClientFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
