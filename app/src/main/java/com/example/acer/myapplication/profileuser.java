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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
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
    ListView lv ;
    String line = null;
    String result = null;
    String [] arr ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileuser);

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        Log.d(TAG, pref.getString("UserName", ""));
        Log.d(TAG, pref.getString("PassWord", ""));
        logoutBtn = (ImageButton) findViewById(R.id.logoutBtn1);
       delete = (ImageButton) findViewById(R.id.imageButton);
        logoutBtn.setOnClickListener(this);

        UserName = pref.getString("UserName", "");

        logoutBtn.setOnClickListener(this);
        lv = (ListView) findViewById(R.id.listV);



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
                sb.append(line+"\n");

            result=sb.toString();
            result=result.replace('"',' ');
            int length =result.length();
            String sreOne =result.substring(1,length-2);

            //use toString() to get the data result
            result=sb.toString();
            // check the data
            System.out.println(sreOne);
            arr= sreOne.split(",");
            int arrLength = arr.length ;

            lv.setAdapter(new ArrayAdapter<String>(profileuser.this,android.R.layout.simple_list_item_1,arr));




        }  catch (IOException e) {
            e.printStackTrace();
        }





    }



    public void onClick(View view) {
        editor = pref.edit();
        editor.clear();
        editor.commit();
        Intent in = new Intent(profileuser.this, LoginActivity.class);
        startActivity(in);
    }



    public void homee(View view){
        Intent in = new Intent(profileuser.this, admin.class);
        startActivity(in);
    }


public void deleteAccount(View view){




            AlertDialog.Builder builder = new AlertDialog.Builder(profileuser.this);
            builder.setMessage("are you sure you wants to delete this invitation");
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
                    //do things
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }


}


