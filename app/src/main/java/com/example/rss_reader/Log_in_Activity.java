package com.example.rss_reader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Log_in_Activity extends AppCompatActivity {
    EditText email;
    EditText password;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.Email_Input);
        password = findViewById(R.id.password_Input);
    }
    public void moveToSignUpFromLog(View view){
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }
    public void logUser(View view){
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        if(emailText.isEmpty()){
            email.setError("Enter email");
            email.requestFocus();
            return;
        }
        if(passwordText.isEmpty()){
            password.setError("Enter password");
            password.requestFocus();
            return;
        }
        auth.signInWithEmailAndPassword(emailText,passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Log_in_Activity.this, "log in successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),RssFeedActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}