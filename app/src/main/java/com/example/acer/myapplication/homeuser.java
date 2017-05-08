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

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String UserName ;//1

    ImageButton profileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeuser);

        profileBtn = (ImageButton) findViewById(R.id.profileBtn1);
        pref       = getSharedPreferences("login.conf", Context.MODE_PRIVATE);//2 the user name and pass are here
        UserName   = pref.getString("UserName", "");//5

        Log.d(TAG, pref.getString("UserName", ""));//3
        Log.d(TAG, pref.getString("PassWord", ""));//4

        profileBtn.setOnClickListener(this);
    }


    public void onClick(View v)
    {
        Intent intent = new Intent(homeuser.this,profileuser.class);
        startActivity(intent);
    }

    public void invitation (View view)
    {
        Intent intent = new Intent(homeuser.this,invitationOptiens.class);
        startActivity(intent);
    }


}
