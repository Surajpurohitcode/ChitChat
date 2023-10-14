package com.example.chitchat.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.chitchat.R;
import com.example.chitchat.databinding.FragmentRecevierProfileBinding;


public class RecevierProfileFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentRecevierProfileBinding binding = FragmentRecevierProfileBinding.inflate(inflater,container,false);

        String profileImage = getActivity().getIntent().getStringExtra("recevier_profileImg");
        Glide.with(getActivity()).load(profileImage).into(binding.recevierProfileImage);
        String username = getActivity().getIntent().getStringExtra("recevier_username");
        binding.recevierUsername.setText(username);

        return binding.getRoot();
    }
}