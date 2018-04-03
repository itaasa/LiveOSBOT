package com.itaasa.liveosbot;

import com.google.gson.annotations.SerializedName;

public class BotReportData {

    @SerializedName("BotID")
    private int botID;

    @SerializedName("BotName")
    private String botName;

    @SerializedName("SkillName")
    private String skillName;

    @SerializedName("ItemName")
    private String itemName;

    @SerializedName("ItemID")
    private int itemID;

    @SerializedName("NumOfItems")
    private int numOfItems;

    @SerializedName("XpPerHour")
    private float xpPerHour;

    @SerializedName("GpPerHour")
    private float gpPerHour;


    //Getter functions
    int getBotID(){
        return this.botID;
    }

    String getBotName(){
        return this.botName;
    }

    String getSkillName(){
        return this.skillName;
    }

    String getItemName(){
        return this.itemName;
    }

    int getItemID(){
        return itemID;
    }

    int getNumOfItems(){
        return numOfItems;
    }

    float getXpPerHour(){
        return xpPerHour;
    }

    float getGpPerHour(){
        return gpPerHour;
    }

}