package com.example.acer.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

public class cancelation_message extends AppCompatActivity {

    final String TAG = this.getClass().getName();
    SharedPreferences pref;
    String UserName;
    public Button sug,ignor,accept ;
    ListView lv ;
    String line = null;
    String result = null;
    String [] arr ;
    public InputStream is ;
    Button map;
    connectionDetector cd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelation_message);

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        Log.d(TAG, pref.getString("UserName", ""));// 3
        Log.d(TAG, pref.getString("PassWord", ""));// 4
        UserName = pref.getString("UserName", ""); // 5
        lv = (ListView) findViewById(R.id.list);
        map = (Button) findViewById(R.id.button2);
        sug= (Button)findViewById(R.id.sug);
        ignor= (Button)findViewById(R.id.ignor);
        accept= (Button)findViewById(R.id.accept);


//==============================================================================================================

        StrictMode.ThreadPolicy policy = new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//==============================================================================================================
        if(cd.icConnected()){
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Mai/cancel_message.php?ID=" + apologization_not.ID);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
//==============================================================================================================

            is = entity.getContent();


        }   catch (Exception e){
            System.out.print("exception 1 caught");
            //exception handel code
        }
//==============================================================================================================
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);

            StringBuilder sb = new StringBuilder();
            while ((line=reader.readLine())!=null)
                sb.append(line+"\n");

            result=sb.toString();
            result=result.replace('"',' ');
            result=result.replace(']',' ');
            result=result.replace('[',' ');
            int length =result.length();
            String sreOne =result.substring(1,length-2);

            //use toString() to get the data result
            result=sb.toString();
            // check the data
            System.out.println(sreOne);
            arr= sreOne.split(",");

//==============================================================================================================
            lv.setAdapter(new ArrayAdapter<String>(cancelation_message .this,android.R.layout.simple_list_item_1,arr));


        }   catch (IOException e) {
            e.printStackTrace();
        }
    }else
        { Toast.makeText(cancelation_message.this,"Network connection problems",Toast.LENGTH_SHORT).show();}}


    public void Back (View view)
    {
        onBackPressed();
    }

    // to go to the profile
    public void profile (View view)
    {
        Intent intent = new Intent(cancelation_message .this, profileuser.class);
        startActivity(intent);
    }
}
