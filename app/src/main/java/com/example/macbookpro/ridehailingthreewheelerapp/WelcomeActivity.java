package com.example.macbookpro.ridehailingthreewheelerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private Button DriverBtn;
    private Button CustomerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        DriverBtn = (Button)findViewById(R.id.DriverBtn);
        CustomerBtn = (Button)findViewById(R.id.CustomerBtn);

        DriverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent driverLoginIntent = new Intent(WelcomeActivity.this, DriverLoginActivity.class);
                startActivity(driverLoginIntent);
            }
        });

        CustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CustomerLoginIntent = new Intent(WelcomeActivity.this, CustomerLoginActivity.class);
                startActivity(CustomerLoginIntent);
            }
        });
    }
}
