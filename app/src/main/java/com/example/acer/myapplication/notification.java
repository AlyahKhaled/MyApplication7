package com.example.acer.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class notification extends AppCompatActivity {
    final String TAG = this.getClass().getName();
    public Button inc, frie;
    SharedPreferences pref;
    String UserName;
    connectionDetector cd ;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        cd= new connectionDetector(this);
        inc = (Button) findViewById(R.id.button6);
        frie = (Button) findViewById(R.id.button4);
        pref =getSharedPreferences("login.conf",Context.MODE_PRIVATE);
        Log.d(TAG,pref.getString("UserName",""));
        Log.d(TAG,pref.getString("PassWord",""));
        UserName =pref.getString("UserName","");}


    public void inv (View view)
    {
        Intent intent = new Intent(notification.this, invitations.class);
        startActivity(intent);
    }
    public void frie (View view)
    {
        Intent intent = new Intent(notification.this, FriendsRequests.class);
        startActivity(intent);
    }

    public void apo (View view)
    {
        Intent intent = new Intent(notification.this, apologization_not.class);
        startActivity(intent);
    }
    public void Back(View view)
    {
        Intent intent = new Intent(notification.this, homeuser.class);
        startActivity(intent);
    }
    public void profile(View view)
    {
        Intent intent = new Intent(notification.this, profileuser.class);
        startActivity(intent);
    }
    }

