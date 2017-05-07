package com.example.acer.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class admienprofile extends AppCompatActivity implements View.OnClickListener {
    final String TAG = this.getClass().getName();

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String UserName;//1 taka it and assaigne the user name to it and send it in php file
    ImageButton logoutBtn;
    public InputStream is ;
    ListView lv ;
    String line = null;
    String result = null;
    String [] arr ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admienprofile);


        logoutBtn = (ImageButton) findViewById(R.id.logoutBtn);
        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE); // 2 creat file

        Log.d(TAG, pref.getString("UserName", ""));// 3
        Log.d(TAG, pref.getString("PassWord", ""));// 4

         UserName = pref.getString("UserName", ""); // 5 after intilisatien

        logoutBtn.setOnClickListener(this);
        lv = (ListView) findViewById(R.id.listV1);

        System.out.println("******************************************* UserName "+UserName+"*********************");

        StrictMode.ThreadPolicy policy = new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);




        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Alyah/retriveAdminInfo.php?UserName="+UserName);
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

            lv.setAdapter(new ArrayAdapter<String>(admienprofile.this,android.R.layout.simple_list_item_1,arr));




        }  catch (IOException e) {
            e.printStackTrace();
        }





    }
    public void onClick(View view) {
        editor = pref.edit();
        editor.clear();
        editor.commit();
        Intent in = new Intent(admienprofile.this, LoginActivity.class);
        startActivity(in);
    }

    public void homee(View view){
        Intent in = new Intent(admienprofile.this, admin.class);
        startActivity(in);
    }
}
