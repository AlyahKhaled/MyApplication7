package com.example.acer.myapplication;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class profileuser extends AppCompatActivity implements View.OnClickListener {
    final String TAG = this.getClass().getName();

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ImageButton logoutBtn;
    ImageButton delete;
    public InputStream is ;
    String UserName;
    TextView usN,Na ,Bir;
    String line = null;
    String result = null;
    String [] arr ;
    connectionDetector cd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileuser);

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        Log.d(TAG, pref.getString("UserName", ""));
        Log.d(TAG, pref.getString("PassWord", ""));

        usN=(TextView)findViewById(R.id.textView11);
        Na=(TextView)findViewById(R.id.textView12);
        Bir=(TextView)findViewById(R.id.textView13);


        logoutBtn = (ImageButton) findViewById(R.id.logoutBtn1);
         delete = (ImageButton) findViewById(R.id.imageButton);


        UserName = pref.getString("UserName", "");

        cd = new connectionDetector(this);


        if (cd.icConnected()) {

        logoutBtn.setOnClickListener(this);




        StrictMode.ThreadPolicy policy = new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Alyah/retriveUserPro.php?UserName="+UserName);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();


            is = entity.getContent();


        }catch (Exception e){
            System.out.print("exception 1 caught");
            //exception handel code
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);

            StringBuilder sb = new StringBuilder();
            while ((line=reader.readLine())!=null)
                sb.append(line);

            result=sb.toString();
            System.out.println("**"+result+result+"**"+result.length());

            result = result.replace('"', ' ');
            int length = result.length();
            String sreOne = result.substring(1, length - 2);//i did not start from index 0 cause the string is retreved with spaces at the beging
            System.out.println("*******here is my Data*************");
            System.out.println(sreOne);
            arr = sreOne.split(",");



            String username        =arr[0].substring(1,arr[0].length());
            String name            =arr[1];
            String berthday        =arr[2];


            usN.setText(username);
            Na.setText(name);
            if (!(berthday.contains("nul"))){
            Bir.setText(berthday);}else{Bir.setVisibility(View.INVISIBLE);}



            System.out.println("username: "+username);
            System.out.println("name: "+name);
            System.out.println("berthday: "+berthday);



        }  catch (IOException e) {
            e.printStackTrace();
        }




        }else
        { Toast.makeText(profileuser.this,"Network connection problems",Toast.LENGTH_SHORT).show();}
    }



    public void onClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(profileuser.this);
        builder.setMessage("هل انت متأكد تريد تسجيل الخروج ؟");
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int id) {

                //do things
            }
        });


        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                editor = pref.edit();
                editor.clear();
                editor.commit();
                Intent in = new Intent(profileuser.this, LoginActivity.class);
                startActivity(in);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    public void homee(View view){
        Intent in = new Intent(profileuser.this, homeuser.class);
        startActivity(in);
    }

    public void editP(View view){
        if (cd.icConnected()) {
        Intent in = new Intent(profileuser.this, editProfile.class);
        startActivity(in);}else
        { Toast.makeText(profileuser.this,"Network connection problems",Toast.LENGTH_SHORT).show();}
    }


public void deleteAccount(View view){


    if (cd.icConnected()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(profileuser.this);
            builder.setMessage("هل أنت متأكد تريد حذف حسابك نهائيا ؟");
            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {


                public void onClick(DialogInterface dialog, int id) {

                    //do things
                }
            });


            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    List<NameValuePair> nameValuePair =new ArrayList<NameValuePair>(1);

                    UserName = UserName.replaceAll("\\s+", "");

                    try{
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Alyah/deleteUser.php?UserName="+UserName);
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                        HttpResponse response = httpClient.execute(httpPost);
                        HttpEntity entity = response.getEntity();
                        is=entity.getContent();
                        String msg = "Deleted succefully :) ";
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                        editor = pref.edit();
                        editor.clear();
                        editor.commit();
                        Intent in = new Intent(profileuser.this, LoginActivity.class);
                        startActivity(in);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //do thingss
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }

else
        { Toast.makeText(profileuser.this,"Network connection problems",Toast.LENGTH_SHORT).show();}
}}


