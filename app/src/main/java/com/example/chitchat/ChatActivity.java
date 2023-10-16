package com.example.chitchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.chitchat.adapter.MessageAdpter;
import com.example.chitchat.databinding.ActivityChatBinding;
import com.example.chitchat.model.MessageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;

    String isOnline;

    ArrayList<MessageModel> messageModelArrayList= new ArrayList<>();

    String senderRoom;

    String reciverRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityChatBinding binding = ActivityChatBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();


        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String name = getIntent().getStringExtra("username");
        String profileImage =  getIntent().getStringExtra("profileImage");
        String receiverUid = getIntent().getStringExtra("user_uid");


        binding.recevierProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, RecevierProileActivity.class);
                intent.putExtra("recevierUidprofile",receiverUid);
                startActivity(intent);
            }
        });

        databaseReference.child("users").child(receiverUid).child("online").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                messageModelArrayList.clear();
                if (task.isSuccessful())
                {
                    isOnline = task.getResult().getValue().toString();
                    if (isOnline.equals("true"))
                    {
                        binding.contactOnlineStatus.setText("Online");
                    }
                    else
                    {
                        binding.contactOnlineStatus.setText("Offline");
                        binding.contactOnlineStatus.setTextColor(Color.GRAY);
                    }
                }
            }
        });


        senderRoom = receiverUid+mAuth.getCurrentUser().getUid();
        reciverRoom = mAuth.getCurrentUser().getUid()+receiverUid;

        binding.chatList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        MessageAdpter adpter = new MessageAdpter(this,messageModelArrayList);

        binding.chatList.setAdapter(adpter);



        binding.chatUsername.setText(name);
        Glide.with(getApplicationContext()).load(profileImage).into(binding.chatProfileImage);

        binding.messageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.attachmentBtn.setVisibility(View.GONE);
                binding.cameraBtn.setVisibility(View.GONE);
                binding.micBtn.setVisibility(View.GONE);
                binding.messageSend.setVisibility(View.VISIBLE);

                if (binding.messageBox.getText().toString().isEmpty())
                {
                    binding.attachmentBtn.setVisibility(View.VISIBLE);
                    binding.cameraBtn.setVisibility(View.VISIBLE);
                    binding.micBtn.setVisibility(View.VISIBLE);
                    binding.messageSend.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        databaseReference.child("chats").child(senderRoom).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModelArrayList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    MessageModel messageModel = snapshot1.getValue(MessageModel.class);
                    messageModelArrayList.add(messageModel);
                }
                adpter.notifyDataSetChanged();
                binding.chatList.scrollToPosition(messageModelArrayList.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.messageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.messageBox.getText().toString();

                MessageModel messageModel = new MessageModel(message,mAuth.getCurrentUser().getPhoneNumber());

                databaseReference.child("chats").child(senderRoom).child("messages").push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        databaseReference.child("chats").child(reciverRoom).child("messages").push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                databaseReference.child("users").child(mAuth.getUid()).child("lastMessage").setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                       databaseReference.child("users").child(receiverUid). child("lastMessage").setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {

                                           }
                                       });
                                    }
                                });

                            }
                        });
                    }
                });

                binding.messageBox.setText("");
            }
        });


    }
}