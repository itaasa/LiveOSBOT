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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class BotReportActivity extends AppCompatActivity implements AsyncResponse {

    private ArrayList<BotReportData> botDataList;
    private BotReportData brd;
    private HashMap postData = new HashMap();
    private TextView tViewBotName, tViewStatus, tViewSkillName, tViewItemName, tViewCollect,
            tViewXpPerHour, tViewGpPerHour, tViewCurrentLvl, tViewTotalXp, tViewXpNextLevel, tViewTimeNextLevel;
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot_report);

        //PUT BIG BLOCKS INTO FUNCTIONS
        tViewBotName = findViewById(R.id.botNameTextView);
        tViewStatus = findViewById(R.id.StatusTextView);
        tViewSkillName = findViewById(R.id.SkillTextView);
        tViewItemName = findViewById(R.id.CollectingTextView);
        tViewCollect = findViewById(R.id.TotalCollectedTextView);
        tViewXpPerHour = findViewById(R.id.XpPerHourTextView);
        tViewGpPerHour = findViewById(R.id.GpPerHourTextView);

        tViewCurrentLvl = findViewById(R.id.CurrentLevelTextView);
        tViewTotalXp = findViewById(R.id.TotalXpTextView);
        tViewXpNextLevel = findViewById(R.id.XpNextLevelTextView);
        tViewTimeNextLevel = findViewById(R.id.TimeNextLevelTextView);


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
        else {
            tViewStatus.setText(String.valueOf(getString(R.string.DefaultStatus)));
            tViewStatus.setTextColor(getResources().getColor(R.color.colorOfflineText));
        }
/*
        if (brd.isActive())
            tViewActive.setText(String.valueOf(getString(R.string.BotIsActive)));
        else
            tViewActive.setText(String.valueOf(getString(R.string.BotIsNotActive)));
*/
        tViewSkillName.setText(String.valueOf(brd.getSkillName()));
        tViewItemName.setText(String.valueOf(brd.getItemName()));
        tViewCollect.setText(numberToString(brd.getNumOfItems()));
        tViewXpPerHour.setText(numberToString(brd.getXpPerHour()));
        tViewGpPerHour.setText(numberToString(brd.getGpPerHour()));
        tViewCurrentLvl.setText(String.valueOf(brd.getCurrentLvl()));
        tViewTotalXp.setText(numberToString(brd.getTotalXp()));
        tViewXpNextLevel.setText(numberToString(brd.getXpNextLevel()));
        tViewTimeNextLevel.setText(brd.getTimeNextLevel());


    }

    public String numberToString (int x) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(x);
    }

    public String numberToString (float x) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(x);
    }

}




