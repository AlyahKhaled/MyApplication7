package com.example.acer.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class apologiz extends AppCompatActivity {
    final String TAG = this.getClass().getName();
    SharedPreferences pref;
    String UserName;

    public EditText apo;

    public Button insert ;

    public InputStream is ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apologiz);


        apo = (EditText) findViewById(R.id.apo);

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        Log.d(TAG, pref.getString("UserName", ""));// 3
        Log.d(TAG, pref.getString("PassWord", ""));// 4

        UserName = pref.getString("UserName", ""); // 5


        insert= (Button)findViewById(R.id.button);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(apologiz.this);
                builder.setMessage("When send this message the invitation will be deleted are you sure you want to send");
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {

                        //do things
                    }
                });


                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);


                        try {
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Mai/delet_inv.php?selectedFromList=" + invitations.selectedFromList);
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                            HttpResponse response = httpClient.execute(httpPost);
                            HttpEntity entity = response.getEntity();
                            is = entity.getContent();
                            String msg = "Deleted succefully :) ";
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
        });


                List<NameValuePair> nameValuePair =new ArrayList<NameValuePair>(1);
                String apol = apo.getText().toString();
                nameValuePair.add(new BasicNameValuePair("apo",apol));
                nameValuePair.add(new BasicNameValuePair("UserName",UserName));


                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Mai/apo.php?selectedFromList="+invitations.selectedFromList);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    is=entity.getContent();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }




    }

