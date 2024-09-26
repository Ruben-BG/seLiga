package com.ruben.project.seliga.ui.about;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ruben.project.seliga.R;

public class AboutFragment extends Fragment {

    private PackageManager packageManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        packageManager = requireContext().getPackageManager();

        TextView repositoryText = view.findViewById(R.id.check_repository_text);
        TextView linkedinText = view.findViewById(R.id.linkedin_text);
        TextView appVersion = view.findViewById(R.id.version_app_text);

        // Links
        repositoryText.setOnClickListener(v -> {
            String url = "https://github.com/Ruben-BG/seLiga";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        linkedinText.setOnClickListener(v -> {
            String url = "https://www.linkedin.com/in/ruben-braga/";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        // Especificação de versão do aplicativo
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                PackageInfo packageInfo = packageManager.getPackageInfo(requireContext().getOpPackageName(), 0);

                String versionName = packageInfo.versionName;

                appVersion.setText(getString(R.string.version, versionName));
            } else {
                PackageInfo packageInfo = packageManager.getPackageInfo(requireContext().getPackageName(), 0);
                appVersion.setText(getString(R.string.version, packageInfo.versionName));
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}