package com.example.acer.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.acer.myapplication.Activity.MyFriendList;

public class homeuser extends AppCompatActivity implements View.OnClickListener {
    final String TAG = this.getClass().getName();

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ImageButton profileBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeuser);

        profileBtn = (ImageButton) findViewById(R.id.imageButton3);

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        String UserName=pref.getString("UserName", "");
        Log.d(TAG, pref.getString("UserName", ""));
        Log.d(TAG, pref.getString("PassWord", ""));

        profileBtn.setOnClickListener(this);

    }

    public void onClick(View v) {
        Intent intent = new Intent(homeuser.this,profileuser.class);
        startActivity(intent);}

    public void notifications(View view)
    {
        Intent intent = new Intent(homeuser.this,notification.class);
        startActivity(intent);
    }


    public void Back (View view)
    {
        onBackPressed();
    }


   public void invitation (View view)
    {
        Intent intent = new Intent(homeuser.this,invitationOptiens.class);
        startActivity(intent);
   }

    public void friends(View view) {

        Intent intent = new Intent(homeuser.this, MyFriendList.class);
        startActivity(intent);
    }
    public void profile(View view) {

        Intent intent = new Intent(homeuser.this, profileuser.class);
        startActivity(intent);
    }



}
