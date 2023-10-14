package com.example.chitchat;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chitchat.databinding.ActivityVerificationPhoneNumberBinding;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationPhoneNumberActivity extends AppCompatActivity {

    String phoneNumber;

    FirebaseAuth mAuth;
    private static final int CREDENTIAL_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityVerificationPhoneNumberBinding binding = ActivityVerificationPhoneNumberBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ActivityResultLauncher<IntentSenderRequest> phoneNumberHintIntentResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                try {
                                    phoneNumber = Identity.getSignInClient(getApplicationContext()).getPhoneNumberFromIntent(result.getData());
                                    binding.phoneInputtext.setText(phoneNumber);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

        GetPhoneNumberHintIntentRequest request = GetPhoneNumberHintIntentRequest.builder().build();

        Identity.getSignInClient(VerificationPhoneNumberActivity.this)
                .getPhoneNumberHintIntent(request)
                .addOnSuccessListener(result -> {
                    try {
                        IntentSender intentSender = result.getIntentSender();
                        phoneNumberHintIntentResultLauncher.launch(new IntentSenderRequest.Builder(intentSender).build());
                    } catch (Exception e) {
                        Log.i("Error launching", "error occurred in launching Activity result");
                    }
                })
                .addOnFailureListener(e -> Log.i("Failure occurred", "Failure getting phone number"));

        binding.verifyPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phoneNumber = binding.phoneInputtext.getText().toString();

                if (phoneNumber.isEmpty())
                {
                    Toast.makeText(VerificationPhoneNumberActivity.this, "Phone Number is Empty!", Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    startPhoneNumberVerification(phoneNumber);
                    Intent intent = new Intent(VerificationPhoneNumberActivity.this,VerificationCodeActivity.class);
                    intent.putExtra("user_number",phoneNumber);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null)
        {
            Intent intent = new Intent(VerificationPhoneNumberActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}