package com.example.chitchat;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chitchat.adapter.NewMessageAdpter;
import com.example.chitchat.databinding.ActivityNewMessageBinding;
import com.example.chitchat.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.HashSet;

public class NewMessageActivity extends AppCompatActivity {

    ArrayList<UserModel> contactOnChitChat = new ArrayList<>();
    ArrayList<UserModel> contactNotOnChitChat = new ArrayList<>();

    FirebaseAuth mAuth;
    FirebaseDatabase database;

    ActivityNewMessageBinding binding;



    @Override
    protected void onStart() {
        super.onStart();
        boolean isOnline = true;

        if (mAuth.getCurrentUser() != null) {
            database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference();

            databaseReference.child("users").child(mAuth.getUid()).child("online").setValue(isOnline);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewMessageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();


        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.contactsOnChitchatList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));



        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (permissionGrantedResponse.equals(Manifest.permission.READ_CONTACTS));
                        {
                            doSomethingForEachUniquePhoneNumber(getApplicationContext());
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }


    private void doSomethingForEachUniquePhoneNumber(Context context) {

        DatabaseReference databaseReference = database.getReference();

        ArrayList<String> arrayList = new ArrayList<>();




        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                //plus any other properties you wish to query
        };

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        } catch (SecurityException e) {
            //SecurityException can be thrown if we don't have the right permissions
        }

        if (cursor != null) {
            try {
                HashSet<String> normalizedNumbersAlreadyFound = new HashSet<>();
                int indexOfNormalizedNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
                int indexOfDisplayName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int indexOfDisplayNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                while (cursor.moveToNext()) {
                    String normalizedNumber = cursor.getString(indexOfNormalizedNumber);
                    if (normalizedNumbersAlreadyFound.add(normalizedNumber)) {
                       String  displayName = cursor.getString(indexOfDisplayName);
                        String  displayNumber = cursor.getString(indexOfDisplayNumber);
                        //haven't seen this number yet: do something with this contact!
//                        Log.d("phoneNumber",normalizedNumber);

                        arrayList.add(normalizedNumber);

                    } else {
                        //don't do anything with this contact because we've already found this number
                    }
                }
            } finally {
                cursor.close();
            }

            databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    NewMessageAdpter adpter = new NewMessageAdpter(contactOnChitChat,context);
                    contactOnChitChat.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren())
                    {
                        UserModel model = snapshot1.getValue(UserModel.class);

                        if (arrayList.contains(model.getPhoneNumber()))
                        {
                            contactOnChitChat.add(model);
                            binding.contactsOnChitchatList.setAdapter(adpter);

                        }

                        adpter.notifyDataSetChanged();

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }


}