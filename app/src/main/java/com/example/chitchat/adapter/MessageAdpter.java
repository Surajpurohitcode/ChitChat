package com.example.chitchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.R;
import com.example.chitchat.model.MessageModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import kotlinx.coroutines.channels.Send;

public class MessageAdpter extends RecyclerView.Adapter {

    Context context;

    ArrayList<MessageModel> arrayList;
    final int messageSend = 1;
    final int messageRecived = 2;

    public MessageAdpter(Context context, ArrayList<MessageModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType)
        {
            case messageSend:
                View view = LayoutInflater.from(context).inflate(R.layout.sender_message_layout,parent,false);
                return new SenderViewHolder(view);

            case messageRecived:
                View view2 = LayoutInflater.from(context).inflate(R.layout.reciver_message_layout,parent,false);
                return new ReciverViewHolder(view2);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == messageSend)
        {
            ((SenderViewHolder) holder).senderMessage.setText(arrayList.get(position).getMessage());
        }
        else
        {
            ((ReciverViewHolder) holder).reciverMessage.setText(arrayList.get(position).getMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().equals(arrayList.get(position).getSenderID()))
        {
            return messageSend;
        }
        else
        {
            return messageRecived;
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class SenderViewHolder extends RecyclerView.ViewHolder
    {

        TextView senderMessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessage = itemView.findViewById(R.id.senderMessage);

        }
    }

    class ReciverViewHolder extends RecyclerView.ViewHolder
    {

        TextView reciverMessage;

        public ReciverViewHolder(@NonNull View itemView) {
            super(itemView);

            reciverMessage = itemView.findViewById(R.id.reciverMessage);
        }
    }

}
