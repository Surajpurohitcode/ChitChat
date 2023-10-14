package com.example.chitchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chitchat.databinding.ActivityVerificationCodeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationCodeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityVerificationCodeBinding binding = ActivityVerificationCodeBinding.inflate(getLayoutInflater());
        View view  = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();


        phoneNumber = getIntent().getStringExtra("user_number");
        binding.messageInfoPhone.setText("We’ve sent the code via SMS to "+phoneNumber);



        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("onVerificationCompleted", "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Toast.makeText(VerificationCodeActivity.this, "Error"+e, Toast.LENGTH_SHORT).show();
                Log.d("Firebase error",e.toString());

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        startPhoneNumberVerification(phoneNumber);


        binding.verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String otpCode = binding.pinView.getText().toString();

                if (otpCode.isEmpty())
                {
                    Toast.makeText(VerificationCodeActivity.this, "Enter Otp!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpCode);
                        signInWithPhoneAuthCredential(credential);
                    }catch (Exception e)
                    {
                        Toast.makeText(VerificationCodeActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });




    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Firebase_auth", "signInWithCredential:success");


                            boolean user = task.getResult().getAdditionalUserInfo().isNewUser();

                            if (user)
                            {
                                Intent intent = new Intent(VerificationCodeActivity.this,SetupNameActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Intent intent = new Intent(VerificationCodeActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("Firebase_auth", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerificationCodeActivity.this, "Otp is incorrect!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}