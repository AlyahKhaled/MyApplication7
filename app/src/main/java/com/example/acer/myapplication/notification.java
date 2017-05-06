package com.example.acer.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class notification extends AppCompatActivity {

    public Button inc,frie ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        inc= (Button)findViewById(R.id.button6);
        frie= (Button)findViewById(R.id.button4);}



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
    }

