package com.example.chitchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chitchat.databinding.ActivitySetupNameBinding;

public class SetupNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySetupNameBinding binding = ActivitySetupNameBinding.inflate(getLayoutInflater());
        View view  = binding.getRoot();
        setContentView(view);

        binding.verifyNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.nameInputText.getText().toString();
                if (name.isEmpty())
                {
                    Toast.makeText(SetupNameActivity.this, "Please Enter Your Name!", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(SetupNameActivity.this,SetupPhotoActivity.class);
                    intent.putExtra("user_name",name);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}