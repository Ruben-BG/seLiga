package com.ruben.project.seliga.ui.startup;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ruben.project.seliga.MainActivity;
import com.ruben.project.seliga.R;
import com.ruben.project.seliga.ui.startup.addName.NameAddFragment;
import com.ruben.project.seliga.ui.startup.firstTime.FirstTimeFragment;
import com.ruben.project.seliga.ui.startup.importData.ImportDataFragment;

public class StartupActivity extends AppCompatActivity {
    private final Fragment firstTimeFragment = new FirstTimeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        showFragment(firstTimeFragment, R.anim.slide_in_right, R.anim.slide_out_left);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Do nothing to prevent going back to MainActivity
            }
        });
    }

    public void showFragment(Fragment fragment, int enterAnim, int exitAnim) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(enterAnim, exitAnim);
        fragmentTransaction.replace(R.id.nav_startup_fragment, fragment);
        fragmentTransaction.commit();
    }

    public void showImportDataFragment() {
        Fragment importDataFragment = new ImportDataFragment();
        showFragment(importDataFragment, R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void showNameAddFragment() {
        Fragment nameAddFragment = new NameAddFragment();
        showFragment(nameAddFragment, R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void showFirstTimeFragment() {
        showFragment(firstTimeFragment, R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void intentToMainActivity() {
        Intent intent = new Intent(StartupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}