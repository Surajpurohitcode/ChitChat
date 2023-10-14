package com.example.chitchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chitchat.R;
import com.example.chitchat.model.UserModel;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class InviteListAdpter extends RecyclerView.Adapter<InviteListAdpter.ViewHolder> {

    ArrayList<UserModel> arrayList = new ArrayList<>();
    Context context;

    public InviteListAdpter(ArrayList<UserModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public InviteListAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.invite_to_chitchat_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InviteListAdpter.ViewHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        //Glide.with(context).load(arrayList.get(position).getProfileImage()).placeholder(R.drawable.profile_icon).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        CircularImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.invite_contact_username);
            itemView = itemView.findViewById(R.id.invite_contact_profile_img);

        }
    }
}
