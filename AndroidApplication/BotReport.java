package com.itaasa.liveosbot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class BotReport extends AppCompatActivity implements AsyncResponse{

    private ArrayList<BotReportData> botDataList;
    private BotReportData brd;
    private HashMap postData = new HashMap();
    private TextView tViewBotName, tViewStatus, tViewActive, tViewSkillName, tViewItemName, tViewCollect,
                tViewXpPerHour, tViewGpPerHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot_report);

    //PUT BIG BLOCKS INTO FUNCTIONS
        tViewBotName = findViewById(R.id.botNameTextView);
        tViewStatus = findViewById(R.id.StatusTextView);
        tViewActive = findViewById(R.id.IsActiveTextView);
        tViewSkillName = findViewById(R.id.SkillTextView);
        tViewItemName = findViewById (R.id.CollectingTextView);
        tViewCollect = findViewById(R.id.TotalCollectedTextView);
        tViewXpPerHour = findViewById(R.id.XpPerHourTextView);
        tViewGpPerHour = findViewById(R.id.GpPerHourTextView);

        PostResponseAsyncTask readTask =  new PostResponseAsyncTask(BotReport.this, this);
        readTask.execute("http://REPLACEWITHIP4VADDRESS/liveosbot/botreportdata.php");

    }

    @Override
    public void processFinish(String s) {
        botDataList = new JsonConverter<BotReportData>().toArrayList(s, BotReportData.class);

        //Obtaining data of bot and placing into text views
        brd = botDataList.get(0);

        tViewBotName.setText(String.valueOf(brd.getBotName()));

        //if botisactive
            tViewStatus.setText(String.valueOf(""));

        tViewSkillName.setText(String.valueOf(brd.getSkillName()));
        tViewItemName.setText(String.valueOf(brd.getItemName()));
        tViewCollect.setText(String.valueOf(brd.getNumOfItems()));
        tViewXpPerHour.setText(String.valueOf(brd.getXpPerHour()));
        tViewGpPerHour.setText(String.valueOf(brd.getGpPerHour()));





    }
}
