package com.example.szonyeg_shop.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        new NotiHelper(context).send("It's time to shop something!");
    }
}