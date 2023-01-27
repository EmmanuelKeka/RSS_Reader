package com.example.rss_reader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if(FirebaseAuth.getInstance().getCurrentUser() !=null){
            Intent intent = new Intent(this,RssFeedActivity.class);
            startActivity(intent);
        }
    }
    public void moveToSignUp(View view){
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }
    public void moveToLogInPage(View view){
        Intent intent = new Intent(getApplicationContext(),Log_in_Activity.class);
        startActivity(intent);
    }
}