package com.example.macbookpro.ridehailingthreewheelerapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerLoginActivity extends AppCompatActivity {

    private TextView CustomerStatusTextView;
    private TextView CustomerLinkTextView;
    private Button CustomerLoginBtn;
    private Button CustomerRegisterBtn;
    private EditText CustomerEmailEditText;
    private EditText CustomerPasswordEditText;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        mAuth = FirebaseAuth.getInstance();

        CustomerStatusTextView = (TextView)findViewById(R.id.CustomerStatusTextView);
        CustomerLinkTextView = (TextView)findViewById(R.id.CustomerLinkTextView);
        CustomerEmailEditText = (EditText)findViewById(R.id.CustomerEmailEditText);
        CustomerPasswordEditText = (EditText)findViewById(R.id.CustomerPasswordEditText);
        CustomerLoginBtn = (Button)findViewById(R.id.CustomerLoginBtn);
        CustomerRegisterBtn = (Button)findViewById(R.id.CustomerRegisterBtn);

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent loginIntent = new Intent(CustomerLoginActivity.this, MapActivity.class);
                    startActivity(loginIntent);
                    finish();
                    return;
                }
            }
        };

        CustomerRegisterBtn.setEnabled(false);
        CustomerRegisterBtn.setVisibility(View.INVISIBLE);

        CustomerLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerLoginBtn.setVisibility(View.INVISIBLE);
                CustomerRegisterBtn.setVisibility(View.VISIBLE);
                CustomerLinkTextView.setVisibility(View.INVISIBLE);
                CustomerStatusTextView.setText("Customer Registration");
                CustomerRegisterBtn.setEnabled(true);
            }
        });

        CustomerRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String DriverEmail = CustomerEmailEditText.getText().toString();
                final String DriverPassword = CustomerPasswordEditText.getText().toString();

                mAuth.createUserWithEmailAndPassword(DriverEmail, DriverPassword).addOnCompleteListener(CustomerLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(CustomerLoginActivity.this, "Sign Up Error", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_id = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user_id);
                            current_user_id.setValue(true);
                        }
                    }
                });
            }
        });

        CustomerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String DriverEmail = CustomerEmailEditText.getText().toString();
                final String DriverPassword = CustomerPasswordEditText.getText().toString();

                mAuth.signInWithEmailAndPassword(DriverEmail, DriverPassword).addOnCompleteListener(CustomerLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(CustomerLoginActivity.this, "Sign In Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
