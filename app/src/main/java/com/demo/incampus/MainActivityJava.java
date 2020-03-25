package com.demo.incampus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivityJava extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_java);
    }

    public void login_function(View view)
    {
        Intent i=new Intent(MainActivityJava.this,SignInJava.class);
        startActivity(i);
    }
    public void sign_up_function(View view)
    {
        Intent i=new Intent(MainActivityJava.this,SignUpJava.class);
        startActivity(i);
    }
}
