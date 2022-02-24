package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailEdt, passwordEdt, idNum;
    private ProgressBar progressBarRegister;

    private FirebaseAuth mAuth;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        mAuth = FirebaseAuth.getInstance();
        emailEdt = findViewById(R.id.emailEdt);
        passwordEdt = findViewById(R.id.passwordEdt);
        idNum = findViewById(R.id.idNum);
        progressBarRegister = findViewById(R.id.progressBarRegister);


    }

    public void returnLogin(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public void goHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void Register(View view) {
        closeKeyboard();
        progressBarRegister.setVisibility(View.VISIBLE);
        String user_id = idNum.getText().toString().trim();
        String email = emailEdt.getText().toString().trim();
        String password = passwordEdt.getText().toString().trim();


        if (user_id.isEmpty()) {
            idNum.setError("Empty Field");
            idNum.requestFocus();
            return;
        }

        if (password.isEmpty() || password.length() < 6) {

            passwordEdt.setError("The password should contain more than 6 symbols");
            passwordEdt.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            emailEdt.setError("Please provide valid email");
            emailEdt.requestFocus();
            return;

        }

        progressBarRegister.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(user_id, password, email);
                    firestore.collection("User").document(email).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBarRegister.setVisibility(View.GONE);
                                Toast.makeText(SignUpActivity.this, "Register success :)", Toast.LENGTH_SHORT).show();
                                goHomeActivity();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Register Failed :(", Toast.LENGTH_SHORT).show();
                                progressBarRegister.setVisibility(View.GONE);


                            }
                        }
                    });


                }
            }
        });


    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}