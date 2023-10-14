package com.example.chitchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.chitchat.databinding.ActivityOnboardingBinding;
import com.example.chitchat.databinding.ActivityVerificationPhoneNumberBinding;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityOnboardingBinding binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding.onboardingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnboardingActivity.this,VerificationPhoneNumberActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}