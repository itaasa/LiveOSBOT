package com.itaasa.liveosbot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity implements AsyncResponse {

    private Button btLogin;
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap postData = new HashMap();
                postData.put("txtUsername", etUsername.getText().toString());
                postData.put("txtPassword", etPassword.getText().toString());

                //Connecting to our database
                PostResponseAsyncTask connectTask = new PostResponseAsyncTask(LoginActivity.this, postData, LoginActivity.this);
                connectTask.execute("http://localhost/liveosbot/login.php");

            }
        });
    }

    @Override
    public void processFinish (String output){

        if (output.contains("success")) {
            Toast.makeText(this, "Login successful.", Toast.LENGTH_LONG).show();
            Intent in = new Intent (LoginActivity.this, LobbyActivity.class);
            startActivity(in);
        }
        else
            Toast.makeText(this, "Invalid login credentials.", Toast.LENGTH_LONG).show();
    }
}
