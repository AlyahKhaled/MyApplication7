package com.example.acer.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class homeuser extends AppCompatActivity implements View.OnClickListener {
    final String TAG = this.getClass().getName();

    SharedPreferences pref;//p
    SharedPreferences.Editor editor;

    ImageButton profileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeuser);

        profileBtn = (ImageButton) findViewById(R.id.profileBtn1);

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);

String UserName=pref.getString("UserName", "");

        Log.d(TAG, pref.getString("UserName", ""));
        Log.d(TAG, pref.getString("PassWord", ""));

        profileBtn.setOnClickListener(this);

    }


    public void onClick(View v) {
        Intent intent = new Intent(homeuser.this,profileuser.class);
        startActivity(intent);

    }
/*
    public void friends (View view)
    {
        Intent intent = new Intent(homeuser.this,frind.class);
        startActivity(intent);
    }

    public void notifications(View view)
    {
        Intent intent = new Intent(homeuser.this,notifocations.class);
        startActivity(intent);
    }

    public void invitation (View view)
    {
        Intent intent = new Intent(homeuser.this,invitationpage.class);
        startActivity(intent);
    }

    public void rest (View view)
    {
        Intent intent = new Intent(homeuser.this,restorantlist.class);
        startActivity(intent);
    }*/



}
