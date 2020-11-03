package com.example.covidtracker.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtracker.R;
import com.example.covidtracker.ui.exposure.ExposureActivity;
import com.example.covidtracker.ui.login.LoginViewModel;
import com.example.covidtracker.ui.login.LoginViewModelFactory;
import com.example.covidtracker.ui.status.StatusActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private FirebaseAuth mAuth;
    public static final String TAG = "Covid Tracker";
    public static final String COLLECTION_USERS = "users";
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        loginButton.setEnabled(false);
        loadingProgressBar = findViewById(R.id.loading);

        //Instantiate the authenticator
        mAuth = FirebaseAuth.getInstance();

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(usernameEditText.getText() != null && !usernameEditText.getText().equals("")  &&
                    passwordEditText.getText() != null && !passwordEditText.getText().equals("") )
                    loginButton.setEnabled(true);
                else
                    loginButton.setEnabled(false);
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    checkSignInOrCreate(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                checkSignInOrCreate(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    //creates account for new users
    private void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //initialize the user to the database
                            FirebaseFirestore reference = FirebaseFirestore.getInstance();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("covid", "false");
                            reference.collection(COLLECTION_USERS).document(email).set(map);

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException()) ;
                            Toast.makeText(LoginActivity.this, "Account Creation failed.",
                                    Toast.LENGTH_SHORT).show();
                            loadingProgressBar.setVisibility(View.GONE);
                            updateUI(null);
                        }

                    }
                });

    }

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            loadingProgressBar.setVisibility(View.GONE);
                            updateUI(null);
                        }
                    }
                });
    }

    //now check to see if there is profile info for this user. If not, create account. else sign in.
    public void checkSignInOrCreate(String email, String password){
        FirebaseFirestore reference = FirebaseFirestore.getInstance();
        DocumentReference docRef = reference.collection(COLLECTION_USERS)
                .document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();

                    //check if the account is in the database
                    if(document.exists())
                        signIn(email, password);

                    else
                        createAccount(email, password);
                }
            }
        });
    }

    public void updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this, "Sign in successful", Toast.LENGTH_LONG);
            Intent intent = getIntent();
            intent.setClass(this, ExposureActivity.class);
            startActivity(intent);

        }
        else
            Toast.makeText(this, "Sign in unsuccessful. please try again.", Toast.LENGTH_LONG);
    }


}