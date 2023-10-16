package com.example.chitchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.chitchat.databinding.ActivityRecevierProileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecevierProileActivity extends AppCompatActivity {

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRecevierProileBinding binding = ActivityRecevierProileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        database = FirebaseDatabase.getInstance();

        String recevierID = getIntent().getStringExtra("recevierUidprofile");

        DatabaseReference databaseReference = database.getReference();

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        databaseReference.child("users").child(recevierID).child("profileImage").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    String profileImg = task.getResult().getValue().toString();
                    Glide.with(RecevierProileActivity.this).load(profileImg).into(binding.recevierProfileImageview);
                }
            }
        });

        databaseReference.child("users").child(recevierID).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    String recevierName = task.getResult().getValue().toString();
                    binding.recevierProfileUsername.setText(recevierName);
                }
            }
        });

        databaseReference.child("users").child(recevierID).child("userBio").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    String recevierBio = task.getResult().getValue().toString();
                    binding.recevierProfileBio.setText(recevierBio);
                }
            }
        });

        databaseReference.child("users").child(recevierID).child("phoneNumber").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    String recevierBio = task.getResult().getValue().toString();
                    binding.recevierProfileMobile.setText(recevierBio);
                }
            }
        });

    }
}