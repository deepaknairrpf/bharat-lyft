package com.example.desilyft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.desilyft.Effects.ButtonAnimator;
import com.example.desilyft.services.LocationTrackerService;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;
    private TextView forgotPassword;

    private ProgressBar progressBar;
    private TextView progressMessage;
    SharedPreferences.Editor editor;

    final static String MY_PREFS_TOKEN = "tokenpreference";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();





        usernameField = (EditText)findViewById(R.id.username);
        passwordField = (EditText)findViewById(R.id.password);
        passwordField.setTransformationMethod(new PasswordTransformationMethod());
        loginButton = (Button)findViewById(R.id.login);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        ButtonAnimator.setEffect(loginButton, ButtonAnimator.Effects.SIMPLE_ON_TOUCH_GREY);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                Log.d("login button",username+password);
                login(username,password);
            }
        });
        Intent intent = new Intent(this, LocationTrackerService.class);
        startService(intent);


    }

    private void login(String username,String password) {
        String endPoint = "/api/token/";
        HashMap<String, Object> dataToBackend = new HashMap<>();
        dataToBackend.put("username", username);
        dataToBackend.put("password", password);
        Http.hit(endPoint, new JSONObject(dataToBackend), new LoginResponseHandler());
    }

    class LoginResponseHandler implements Callback {
        @Override
        public void handleResponse(HashMap<String, Object> response) {
            Log.d("login_response",response.toString());

            editor = getSharedPreferences(MY_PREFS_TOKEN, MODE_PRIVATE).edit();
            editor.putString("access", response.get("access").toString());
            editor.putString("refresh", response.get("refresh").toString());
            editor.apply();

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_TOKEN, MODE_PRIVATE);
            Intent intent = new Intent(LoginActivity.this, NavBarActivity.class);
            startActivity(intent);
        }
    }
}







