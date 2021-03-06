package com.project.pointage.ui.login;

import android.Manifest;
import android.app.Activity;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;



import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;


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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.project.location.*;
import com.project.pointage.*;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private Authentification authentification = null;
    private String username = null;
    private String password = null;
    private Message messenger = new Message();



    private FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    private DatabaseReference myRef ;



    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        usernameEditText.setText("");
        passwordEditText.setText("");


        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }

            }

        });

             loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {

                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);
                usernameEditText.setText("");
                passwordEditText.setText("");
                //Complete and destroy login activity once successful
                //finish();
            }
        });

        final TextWatcher afterTextChangedListener = new TextWatcher() {
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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);

                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

            //Remove the authentification
                authentification = new Authentification(LoginActivity.this, username, password);
                //isCheckUser = authentification.checkUser(database);


                //VERIFIER SI L'UTILISATEUR EST DANS LA BASE DE DONNEES

                if (authentification.isNetworkAvailable(LoginActivity.this)) {

                    myRef = database2.getReference(username);
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String passwordCheck = dataSnapshot.child("password").getValue(String.class);

                            if (authentification.get_hash_pasword(password).equals(passwordCheck)) {
                                Log.i("debug", "the user exist firebase");
                                // authentification.createUser(dataSnapshot, database);
                                authentification.session(dataSnapshot.child("nom").getValue(String.class),1);
                                loginViewModel.login(username, password);
                            }
                            else {
                              onCancelled(DatabaseError.fromException(new Exception("Le mot de passe et/ou l'identifiant est incorrect")));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("debug", "Failed to read value.", error.toException());
                            loadingProgressBar.setVisibility(View.INVISIBLE);
                            Log.i("debug", "Login failed can not reed the value");
                            messenger.message(LoginActivity.this, "Login failed", "Le mot de passe et/ou l'identifiant est incorrect", 0);
                        }
                    });
                }
                else{
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    messenger.message(LoginActivity.this,"Connexion internet","Activer votre connexion internet.",0);
                }
            }
        });
    }


    private void updateUiWithUser(LoggedInUserView model) {

       // String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
       // Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, EmployeCompanies.class);
        startActivity(intent);
    }


    private void showLoginFailed(@StringRes Integer errorString) {
       // Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
        messenger.message(LoginActivity.this,"Error",""+errorString,0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }


}
