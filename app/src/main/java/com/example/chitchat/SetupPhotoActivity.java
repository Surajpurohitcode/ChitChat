package com.example.chitchat;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chitchat.databinding.ActivitySetupPhotoBinding;
import com.example.chitchat.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SetupPhotoActivity extends AppCompatActivity {

    ActivityResultLauncher<String> mPhoto;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;

    FirebaseStorage storage;

    Uri profileUri;

    String userName;

    boolean isComplete = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySetupPhotoBinding binding = ActivitySetupPhotoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        userName = getIntent().getStringExtra("user_name");

        mPhoto = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                profileUri = result;
                binding.profileImgInput.setImageURI(profileUri);
            }
        });


        binding.profileImgInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mPhoto.launch("image/*");
            }
        });

        binding.verifyPimageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (profileUri.equals(null))
                {
                    isComplete = false;
                }
                else
                {
                    isComplete = true;
                    StorageReference storageReference = storage.getReference();
                    storageReference.child("profileImages").child(mAuth.getCurrentUser().getUid()).putFile(profileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String photoStringLink = uri.toString();
                                    uploadData(photoStringLink);
                                }
                            });

                        }
                    });
                }
            }
        });

    }

    private void uploadData(String profileImg) {
        DatabaseReference databaseReference = mDatabase.getReference();
        UserModel userModel = new UserModel(userName,mAuth.getCurrentUser().getPhoneNumber(),profileImg,"Hello I am using chitchat",null,mAuth.getCurrentUser().getUid(),true,true);
        databaseReference.child("users").child(mAuth.getCurrentUser().getUid()).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(SetupPhotoActivity.this, "Data Upload!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SetupPhotoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(SetupPhotoActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}