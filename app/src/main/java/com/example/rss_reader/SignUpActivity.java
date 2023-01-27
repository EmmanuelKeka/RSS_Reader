package com.example.rss_reader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.Email_Input_signUP);
        password = findViewById(R.id.password_Input_signUP);
    }
    public void RegisterUser(View view){
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
        auth.createUserWithEmailAndPassword(emailText,passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(email.getText().toString(),false,"none");
                            user.setSub(false);

                            FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                            }
                                            else{
                                                System.out.println("nini lisusu");
                                            }
                                        }
                                    });
                            email.setText("");
                            password.setText("");
                            Toast.makeText(SignUpActivity.this, "Sign Up uccessfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    public void DirectToLogInPage(){

    }
}