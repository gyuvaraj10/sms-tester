package com.example.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.IOException;

public class Server extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdusObj = (Object[]) bundle.get("pdus");
            final SmsMessage[] messages = new SmsMessage[pdusObj.length];
            int total = pdusObj.length;
            SmsMessage message1 = SmsMessage.createFromPdu((byte[]) pdusObj[total-1]);
            SmsMessage message2 = SmsMessage.createFromPdu((byte[]) pdusObj[total-1]);
            Log.d("SMS1",message1.getDisplayMessageBody());
            Log.d("SMS1",message1.getDisplayOriginatingAddress());
            Log.d("SMS2",message2.getDisplayMessageBody());
            Log.d("SMS2",message2.getDisplayOriginatingAddress());
//            for (int i = 0; i < pdusObj.length; i++) {
//                messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
//                final String address = messages[i].getDisplayOriginatingAddress();
//                final String message = messages[i].getDisplayMessageBody();
////                listener.onNewSMS(address, message);
//                Log.d("SMS",address);
//                Log.d("SMS",message);
//            }
        }
    }
}
