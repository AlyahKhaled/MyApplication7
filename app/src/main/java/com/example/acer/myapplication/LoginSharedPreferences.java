package com.example.acer.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class LoginSharedPreferences extends AppCompatActivity {
    final String TAG = this.getClass().getName();

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_shared_preferences);

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);

        Log.d(TAG, pref.getString("UserName", ""));
        Log.d(TAG, pref.getString("PassWord", ""));

   /*     FloatingActionButton fabLogout = (FloatingActionButton) findViewById(R.id.fabLogout);
        fabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = pref.edit();
                editor.clear();
                editor.commit();
                Intent in = new Intent(SubActivity.this, MainActivity.class);
                startActivity(in);
            }
        });  for logout */



    }
}
