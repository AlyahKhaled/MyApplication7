package com.example.acer.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class resevedInvitationDeatales extends AppCompatActivity {

    TextView InvIDtexView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reseved_invitation_deatales);

        String invId = (getIntent().getExtras().getString("invID"));
        System.out.println("the invId: "+invId);
        InvIDtexView = (TextView) findViewById(R.id.textView3);
        InvIDtexView.setText("MIMI ... the invitation ID is: "+invId);
    }
}
