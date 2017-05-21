package com.example.acer.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class admin extends AppCompatActivity implements View.OnClickListener {
    final String TAG = this.getClass().getName();

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ImageButton profileBtn;

    connectionDetector cd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        profileBtn = (ImageButton) findViewById(R.id.imageButton3);

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        Log.d(TAG, pref.getString("UserName", ""));
        Log.d(TAG, pref.getString("PassWord", ""));

        cd = new connectionDetector(this);

        if (cd.icConnected()) {
        profileBtn.setOnClickListener(this);
        }else
        { Toast.makeText(admin.this,"Network connection problems",Toast.LENGTH_SHORT).show();}
    }



    @Override
    public void onClick(View v) {
        Intent intent = new Intent(admin.this,admienprofile.class);
        startActivity(intent);
    }
    public void list (View v) {
        Intent intent = new Intent(admin.this,adminListOfUsers.class);
        startActivity(intent);
    }
    public void Back (View view)
    {
        onBackPressed();
    }
    public void toProfile (View view)
    {
        Intent intent = new Intent(admin.this,admienprofile.class);
        startActivity(intent);
    }
}
