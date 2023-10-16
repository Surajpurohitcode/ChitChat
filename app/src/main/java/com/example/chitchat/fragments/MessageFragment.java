package com.example.chitchat.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chitchat.NewMessageActivity;
import com.example.chitchat.adapter.NewMessageAdpter;
import com.example.chitchat.adapter.UsersAdpter;
import com.example.chitchat.databinding.FragmentMessageBinding;
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

public class MessageFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ArrayList<UserModel> userArrayList  = new ArrayList<>();

    UsersAdpter adpter;

    FragmentMessageBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(inflater,container,false);


        Dexter.withContext(getActivity())
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (permissionGrantedResponse.equals(Manifest.permission.READ_CONTACTS));
                        {
                            doSomethingForEachUniquePhoneNumber(getActivity());
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



        binding.newMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewMessageActivity.class);
                startActivity(intent);
            }
        });






        return binding.getRoot();
    }

    private void doSomethingForEachUniquePhoneNumber(Context context) {

        binding.usersChatList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        adpter = new UsersAdpter(userArrayList,getActivity());
        binding.usersChatList.setAdapter(adpter);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();

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
                    userArrayList.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren())
                    {
                        UserModel model = snapshot1.getValue(UserModel.class);

                        if (arrayList.contains(model.getPhoneNumber()) && model.getLastMessage() !=null && !model.getPhoneNumber().equals(mAuth.getCurrentUser().getPhoneNumber()))
                        {
                            userArrayList.add(model);
                            checkUserList();

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

    private void checkUserList() {
        if (userArrayList.isEmpty())
        {
            binding.isEmptyLis.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.isEmptyLis.setVisibility(View.GONE);
            binding.usersChatList.setVisibility(View.VISIBLE);
        }
    }

}