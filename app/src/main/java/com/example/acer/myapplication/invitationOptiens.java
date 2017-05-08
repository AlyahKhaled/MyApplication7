package com.example.acer.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class invitationOptiens extends AppCompatActivity {
    final String TAG = this.getClass().getName();

    TextView toProfile ;
    TextView Back      ;
    String   UserName  ;
    SharedPreferences pref;//1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_optiens);

        toProfile = (TextView) findViewById(R.id.textView28);
        Back      = (TextView) findViewById(R.id.textView27);

        pref      = getSharedPreferences("login.conf", Context.MODE_PRIVATE);//2
        Log.d(TAG, pref.getString("UserName", ""));//3
        Log.d(TAG, pref.getString("PassWord", ""));//4

        UserName   = pref.getString("UserName", "");//5
        System.out.print(UserName+"this is the UserName");




    }

    public void Back ()
    {
        onBackPressed();
    }

    public void toProfile ()
    {
        Intent intent = new Intent(invitationOptiens.this,profileuser.class);
        startActivity(intent);
    }

    public void sentInv (View view)
    {
        Intent intent = new Intent(invitationOptiens.this,sentInviitations.class);
        startActivity(intent);
    }

    public void resevedINV (View view)
    {
        Intent intent = new Intent(invitationOptiens.this,invitations.class);
        startActivity(intent);
    }

}
