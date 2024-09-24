package com.ruben.project.seliga.ui.management;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ruben.project.seliga.R;
import com.ruben.project.seliga.ui.management.adapters.ViewPageAdapter;

public class ManagementFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = view.findViewById(R.id.management_view_pager);
        TabLayout tabLayout = view.findViewById(R.id.management_tab_layout);

        ViewPageAdapter adapter = new ViewPageAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(R.string.menu_clients);
                            break;
                        case 1:
                            tab.setText(R.string.menu_payments);
                            break;
                        case 2:
                            tab.setText(R.string.menu_charges);
                            break;
                    }
                }).attach();
    }

}