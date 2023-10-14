package com.example.chitchat.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UsersAdpter extends RecyclerView.Adapter<UsersAdpter.ViewHolder> {
    ArrayList<UserModel> arrayList;
    Context context;

    public UsersAdpter(ArrayList<UserModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_view_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdpter.ViewHolder holder, int position) {
        holder.userName.setText(arrayList.get(position).getName());
        Glide.with(context).load(arrayList.get(position).getProfileImage()).into(holder.profileImage);
        holder.lastMessage.setText(arrayList.get(position).getLastMessage());


        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.userprofileimage_layout_alert);

                TextView textView = dialog.findViewById(R.id.dialog_username);
                ImageView imageView = dialog.findViewById(R.id.dialog_profileImage);
                textView.setText(arrayList.get(position).getName());

                Glide.with(context).load(arrayList.get(position).getProfileImage()).into(imageView);

                dialog.show();
            }
        });

        holder.usercard.setOnClickListener(new View.OnClickListener() {
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
        TextView userName,lastMessage,messageTime;

        CardView usercard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.users_profile_img);
            userName = itemView.findViewById(R.id.users_username);
            lastMessage = itemView.findViewById(R.id.users_last_message);
            messageTime = itemView.findViewById(R.id.users_last_message_time);
            usercard = itemView.findViewById(R.id.user_list_card);
        }
    }
}
