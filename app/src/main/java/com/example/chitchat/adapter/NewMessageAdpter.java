package com.example.chitchat.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chitchat.ChatActivity;
import com.example.chitchat.R;
import com.example.chitchat.model.UserModel;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class NewMessageAdpter extends RecyclerView.Adapter<NewMessageAdpter.ViewHolder> {

    ArrayList<UserModel> arrayList;
    Context context;

    public NewMessageAdpter(ArrayList<UserModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewMessageAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contacts_list_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull NewMessageAdpter.ViewHolder holder, int position) {
        holder.username.setText(arrayList.get(position).getName());
        Glide.with(context).load(arrayList.get(position).getProfileImage()).placeholder(R.drawable.profile_icon).into(holder.profileImage);

        if (arrayList.get(position).isOnline() == true)
        {
            holder.onlineStatus.setText("Online");
            holder.onlineStatus.setTextColor(R.color.green);
            holder.onlineStatus.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.onlineStatus.setText("Offline");
            holder.onlineStatus.setTextColor(Color.GRAY);
            holder.onlineStatus.setVisibility(View.VISIBLE);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("username",arrayList.get(position).getName());
                intent.putExtra("profileImage",arrayList.get(position).getProfileImage());
                intent.putExtra("user_uid",arrayList.get(position).getUid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircularImageView profileImage;
        TextView username,onlineStatus;

        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.contact_profile_img);
            username = itemView.findViewById(R.id.contact_username);
            onlineStatus = itemView.findViewById(R.id.contact_online_status);
            cardView = itemView.findViewById(R.id.new_message_card_view);

        }
    }
}
