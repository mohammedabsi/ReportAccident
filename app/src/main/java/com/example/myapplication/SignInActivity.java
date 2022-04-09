package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity {
    private ProgressBar progressBarlogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mfirebaseFirestore;

    private TextInputEditText emailEdt ,passwordEdt ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();

        progressBarlogin = findViewById(R.id.progressBarLogin);
        emailEdt = findViewById(R.id.emailEdt);
        passwordEdt = findViewById(R.id.passwordEdt);



    }






    public void goHome(View view) {
        closeKeyboard();
        progressBarlogin.setVisibility(View.VISIBLE);
        mfirebaseFirestore = FirebaseFirestore.getInstance();


        String email = emailEdt.getText().toString().trim();
        String password = passwordEdt.getText().toString().trim();

        if (email.isEmpty()) {
            emailEdt.setError("Enter e-mail");
            emailEdt.requestFocus();
            progressBarlogin.setVisibility(View.INVISIBLE);

            return;
        }
        if (password.isEmpty()) {
            passwordEdt.setError("Enter e-mail");
            passwordEdt.setError("Enter your password");
            progressBarlogin.setVisibility(View.INVISIBLE);

            return;
        }


        progressBarlogin.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    progressBarlogin.setVisibility(View.GONE);
                    Toast.makeText(SignInActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    finish();

                } else {
                    Toast.makeText(SignInActivity.this, "Login Failed , try again", Toast.LENGTH_SHORT).show();
                    progressBarlogin.setVisibility(View.GONE);

                }
            }
        });


    }
    public void returnRegister(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}