package com.example.macbookpro.ridehailingthreewheelerapp;

import android.app.ProgressDialog;
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

public class DriverLoginActivity extends AppCompatActivity {

    private TextView DriverStatusTextView;
    private TextView DriverLinkTextView;
    private Button DriverLoginBtn;
    private Button DriverRegisterBtn;
    private EditText DriverEmailEditText;
    private EditText DriverPasswordEditText;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        mAuth = FirebaseAuth.getInstance();

        DriverStatusTextView = (TextView)findViewById(R.id.DriverStatusTextView);
        DriverLinkTextView = (TextView)findViewById(R.id.DriverLinkTextView);
        DriverEmailEditText = (EditText)findViewById(R.id.DriverEmailEditText);
        DriverPasswordEditText = (EditText)findViewById(R.id.DriverPasswordEditText);
        DriverLoginBtn = (Button)findViewById(R.id.DriverLoginBtn);
        DriverRegisterBtn = (Button)findViewById(R.id.DriverRegisterBtn);
        loadingBar = new ProgressDialog(this);

        DriverRegisterBtn.setEnabled(false);
        DriverRegisterBtn.setVisibility(View.INVISIBLE);

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent loginIntent = new Intent(DriverLoginActivity.this, MapActivity.class);
                    startActivity(loginIntent);
                    finish();
                    return;
                }
            }
        };

        DriverLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverLoginBtn.setVisibility(View.INVISIBLE);
                DriverRegisterBtn.setVisibility(View.VISIBLE);
                DriverLinkTextView.setVisibility(View.INVISIBLE);
                DriverStatusTextView.setText("Driver Registration");
                DriverRegisterBtn.setEnabled(true);
            }
        });

        DriverRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String DriverEmail = DriverEmailEditText.getText().toString();
                final String DriverPassword = DriverPasswordEditText.getText().toString();

                loadingBar.setTitle("Driver Registration");
                loadingBar.setMessage("Please wait, you are getting registered to authenticate");
                loadingBar.show();

                mAuth.createUserWithEmailAndPassword(DriverEmail, DriverPassword).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(DriverLoginActivity.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                        else {
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_id = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id);
                            current_user_id.setValue(true);
                            loadingBar.dismiss();
                        }
                    }
                });
            }
        });

        DriverLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String DriverEmail = DriverEmailEditText.getText().toString();
                final String DriverPassword = DriverPasswordEditText.getText().toString();

                mAuth.signInWithEmailAndPassword(DriverEmail, DriverPassword).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(DriverLoginActivity.this, "Sign In Error", Toast.LENGTH_SHORT).show();

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
