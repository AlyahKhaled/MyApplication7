package com.example.acer.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class addfrind extends AppCompatActivity implements View.OnClickListener{
    ImageView ivBackPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfrind);
        ivBackPress.setOnClickListener(this);
        setViews();

    }

    public void openprofile(View v){
        Intent intent = new Intent(addfrind.this,profileuser.class);
        startActivity(intent);
    }

    private void setViews() {
        ivBackPress= (ImageView) findViewById(R.id.imageView2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView2:
                finish();

                break;

        }

    }
}
