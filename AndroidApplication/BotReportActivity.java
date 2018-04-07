package com.itaasa.liveosbot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

public class BotReportActivity extends AppCompatActivity implements AsyncResponse {

    private ArrayList<BotReportData> botDataList;
    private BotReportData brd;
    private HashMap postData = new HashMap();
    private TextView tViewBotName, tViewStatus, tViewActive, tViewSkillName, tViewItemName, tViewCollect,
            tViewXpPerHour, tViewGpPerHour;
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot_report);

        //PUT BIG BLOCKS INTO FUNCTIONS
        tViewBotName = findViewById(R.id.botNameTextView);
        tViewStatus = findViewById(R.id.StatusTextView);
        tViewActive = findViewById(R.id.IsActiveTextView);
        tViewSkillName = findViewById(R.id.SkillTextView);
        tViewItemName = findViewById(R.id.CollectingTextView);
        tViewCollect = findViewById(R.id.TotalCollectedTextView);
        tViewXpPerHour = findViewById(R.id.XpPerHourTextView);
        tViewGpPerHour = findViewById(R.id.GpPerHourTextView);
        refreshButton = findViewById(R.id.refreshButton);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        Intent intentExtras  = getIntent();
        Bundle extrasBundle = intentExtras.getExtras();

        if (extrasBundle.containsKey("botID") && extrasBundle.containsKey("itemID")){
            String botId = extrasBundle.getString("botID");
            String itemId = extrasBundle.getString("itemID");

            Log.d("BotReportActivity", botId + "\t" + itemId);

            HashMap postData =  new HashMap();

            postData.put("inputBotID", botId);
            postData.put("inputItemID", itemId);

            PostResponseAsyncTask readTask = new PostResponseAsyncTask(BotReportActivity.this, postData, BotReportActivity.this);
            readTask.execute("http://192.168.56.1/liveosbot/botreportdata.php");
        }

        else {
            Log.d("BotReportActivity", "No data was passed.");
        }

    }

    @Override
    public void processFinish(String s) {
        botDataList = new JsonConverter<BotReportData>().toArrayList(s, BotReportData.class);

        //Obtaining data of bot and placing into text views
        brd = botDataList.get(0);

        tViewBotName.setText(String.valueOf(brd.getBotName()));

        if (brd.isOnline())
            tViewStatus.setText(String.valueOf(getString(R.string.BotIsOnline)));
        else
            tViewStatus.setText(String.valueOf(getString(R.string.DefaultStatus)));

        if (brd.isActive())
            tViewActive.setText(String.valueOf(getString(R.string.BotIsActive)));
        else
            tViewActive.setText(String.valueOf(getString(R.string.BotIsNotActive)));

        tViewSkillName.setText(String.valueOf(brd.getSkillName()));
        tViewItemName.setText(String.valueOf(brd.getItemName()));
        tViewCollect.setText(String.valueOf(brd.getNumOfItems()));
        tViewXpPerHour.setText(String.valueOf(brd.getXpPerHour()));
        tViewGpPerHour.setText(String.valueOf(brd.getGpPerHour()));

    }
}
