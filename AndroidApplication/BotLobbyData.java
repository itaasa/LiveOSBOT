package com.itaasa.liveosbot;

import com.google.gson.annotations.SerializedName;

public class BotLobbyData {

    @SerializedName("BotID")
    public String botId;

    @SerializedName("ItemID")
    public String itemId;

    @SerializedName("BotName")
    public String botName;

    @SerializedName("World")
    public String botWorld;

    public String getBotId () { return botId; }

    public String getItemId() { return itemId; }

    public String getBotName() {
        return botName;
    }

    public String getBotWorld() {
        return botWorld;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public void setBotWorld(String botWorld) {
        this.botWorld = botWorld;
    }
}
