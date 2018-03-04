package com.itaasa.liveosbot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;


public class MainActivity extends AppCompatActivity implements AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connecting to our database
        PostResponseAsyncTask connectTask = new PostResponseAsyncTask(MainActivity.this, this);
        connectTask.execute("http://10.0.2.2/liveosbot/login.php");

    }

    @Override
    public void processFinish (String output){
        if (output.contains("success"))
            Toast.makeText(this, "Connected to database.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Connection to database failed.", Toast.LENGTH_LONG).show();


        Intent in = new Intent (MainActivity.this, BotReport.class);
        startActivity(in);
    }

}
