package com.demo.incampus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PhoneNumberActivity extends AppCompatActivity {

    EditText phone_text;
    String phoneNumber="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        phone_text=findViewById(R.id.enterNumber);
    }

    public void go_to_OTP_activity_function(View view)
    {
        phoneNumber=phone_text.getText().toString();
        Intent i=new Intent(this,OTPJava.class);
        i.putExtra("phoneNumber",phoneNumber);
        startActivity(i);
    }
}
