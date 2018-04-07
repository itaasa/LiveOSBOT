package com.itaasa.liveosbot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;

public class LobbyActivity  extends AppCompatActivity implements AsyncResponse {

    private ArrayList<BotLobbyData> botList;
    ListView lvLobbyBot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(LobbyActivity.this, this);
        taskRead.execute("http://IPV4ADDRESS/liveosbot/lobbybotdata.php");
    }

    @Override
    public void processFinish(String s) {
        botList = new JsonConverter<BotLobbyData>().toArrayList(s, BotLobbyData.class);

        BindDictionary<BotLobbyData> dict = new BindDictionary<>();

        dict.addStringField(R.id.lobbyBotName, new StringExtractor<BotLobbyData>() {
            @Override
            public String getStringValue(BotLobbyData blData, int position) {
                return blData.botName;
            }
        });

        dict.addStringField(R.id.botWorld, new StringExtractor<BotLobbyData>() {
            @Override
            public String getStringValue(BotLobbyData blData, int position) {
                return blData.botWorld;
            }
        });


        FunDapter<BotLobbyData> adapter = new FunDapter<>(LobbyActivity.this, botList, R.layout.custom_bot_lobby, dict);
        lvLobbyBot = (ListView) findViewById(R.id.lobbyList);
        lvLobbyBot.setAdapter(adapter);

        lvLobbyBot.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {

                Log.d("LobbyActivity", botList.get(i).getBotId() + "\t" + botList.get(i).getItemId());

                Intent intentBundle = new Intent(LobbyActivity.this, BotReportActivity.class);

                Bundle reportBundle = new Bundle();
                reportBundle.putString("botID", botList.get(i).getBotId());
                reportBundle.putString("itemID", botList.get(i).getItemId());

                intentBundle.putExtras(reportBundle);

                startActivity(intentBundle);
            }
        });
    }
}
