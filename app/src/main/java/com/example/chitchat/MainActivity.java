package com.example.chitchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.chitchat.databinding.ActivityMainBinding;
import com.example.chitchat.fragments.ContactsFragment;
import com.example.chitchat.fragments.FullProfileFragment;
import com.example.chitchat.fragments.MessageFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;

    boolean isOnline = false;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

        isOnline = true;

        if (mAuth.getCurrentUser() != null)
        {
            database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference();

            databaseReference.child("users").child(mAuth.getUid()).child("online").setValue(isOnline);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnline = false;


        if (mAuth.getCurrentUser() != null)
        {

            database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference();

            databaseReference.child("users").child(mAuth.getUid()).child("online").setValue(isOnline);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnline = true;

        if (mAuth.getCurrentUser() != null)
        {
            database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference();

            databaseReference.child("users").child(mAuth.getUid()).child("online").setValue(isOnline);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOnline = false;

        if (mAuth.getCurrentUser() != null)
        {
            database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference();

            databaseReference.child("users").child(mAuth.getUid()).child("online").setValue(isOnline);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout,new MessageFragment()).commit();

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.message)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new MessageFragment()).commit();
                    binding.toolbar.setVisibility(View.VISIBLE);
                    binding.toolbar.setTitle("Message");
                } else if (item.getItemId() == R.id.users) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new ContactsFragment()).commit();
                    binding.toolbar.setVisibility(View.VISIBLE);
                    binding.toolbar.setTitle("Contacts");
                }

                else if (item.getItemId() == R.id.profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new FullProfileFragment()).commit();
                    //binding.toolbar.setTitle("Contacts");
                    binding.toolbar.setVisibility(View.GONE);
                }

                return true;
            }
        });

    }
}