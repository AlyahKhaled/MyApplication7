package com.example.acer.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class admin extends AppCompatActivity implements View.OnClickListener {
    final String TAG = this.getClass().getName();

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ImageButton profileBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        profileBtn = (ImageButton) findViewById(R.id.imageButton3);

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        Log.d(TAG, pref.getString("UserName", ""));
        Log.d(TAG, pref.getString("PassWord", ""));


        profileBtn.setOnClickListener(this);

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
    InputStream is;
    String line = null;
    String result = null;

    public void deleteExpiredInvitations(View view) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
//new
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwara/IsThereCanceledInvitaton.php");
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();


            is = entity.getContent();


        } catch (Exception e) {
            System.out.print("exception 1 caught");
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
                sb.append(line);

            result = sb.toString();
            if (result.contains("yes")) {

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://zwarh.net/zwara/DeleteCanceledInvitaton.php");
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();


                    is = entity.getContent();


                } catch (Exception e) {
                    System.out.print("exception 1 caught");
                }
                try {
                    BufferedReader reader1 = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

                    StringBuilder sb1 = new StringBuilder();
                    while ((line = reader1.readLine()) != null)
                        sb1.append(line);

                    result = sb1.toString();
                    if (result.contains("yes")) {
                        Toast.makeText(admin.this, "تم حذف الدعوات منتهية الصلاحية بنجاح", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(admin.this, "لم يتم حذف الدعوات منتهية الصلاحية, الرجاء حاول مرة أخرى", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else
                Toast.makeText(admin.this, "لا يوجد دعوات لحذفها", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //end new
       /* try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwara/DeleteCanceledInvitaton.php");
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();


            is = entity.getContent();


        } catch (Exception e) {
            System.out.print("exception 1 caught");
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
                sb.append(line);

            result = sb.toString();
            if (result.contains("yes")) {
                Toast.makeText(admin.this, "تم حذف الدعوات منتهية الصلاحية بنجاح", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(admin.this, "لم يتم حذف الدعوات منتهية الصلاحية, الرجاء حاول مرة أخرى", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    public void toProfile (View view)
    {
        Intent intent = new Intent(admin.this,admienprofile.class);
        startActivity(intent);
    }
}
