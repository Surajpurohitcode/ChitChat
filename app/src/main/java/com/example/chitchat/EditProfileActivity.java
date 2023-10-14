package com.example.chitchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.chitchat.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;

    String profileImg,username,phoneNumber,userBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditProfileBinding binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();


        databaseReference.child("users").child(mAuth.getUid()).child("profileImage").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    profileImg = task.getResult().getValue().toString();
                    Glide.with(EditProfileActivity.this).load(profileImg).into(binding.updateProfileImage);
                }
            }
        });

        databaseReference.child("users").child(mAuth.getCurrentUser().getUid()).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    username = task.getResult().getValue().toString();
                    binding.updateUsername.setText(username);
                }
            }
        });

        databaseReference.child("users").child(mAuth.getCurrentUser().getUid()).child("userBio").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    userBio = task.getResult().getValue().toString();
                    binding.updateBio.setText(userBio);
                }

            }
        });

        databaseReference.child("users").child(mAuth.getCurrentUser().getUid()).child("phoneNumber").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful())
                {
                    phoneNumber = task.getResult().getValue().toString();
                    binding.updatePhoneNumber.setText(phoneNumber);
                }

            }
        });



    }
}