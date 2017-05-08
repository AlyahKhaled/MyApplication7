package com.example.acer.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;

public class resevedInvitatio extends AppCompatActivity {

    final String TAG = this.getClass().getName();
    String   UserName  ;
    SharedPreferences pref;//1

    ListView lv ;
    InputStream is ;
    String line = null;
    String result = null;
    String [] arr ;
    ArrayList<String> inviTopic ;
    ArrayList<String> invID ;
    connectionDetector cd ;
    public  boolean Enter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reseved_invitatio);

        pref      = getSharedPreferences("login.conf", Context.MODE_PRIVATE);//2
        Log.d(TAG, pref.getString("UserName", ""));//3
        Log.d(TAG, pref.getString("PassWord", ""));//4
        UserName   = pref.getString("UserName", "");//5

        lv = (ListView) findViewById(R.id.list);
        inviTopic= new ArrayList<String>();
        invID=new ArrayList<String>();
        cd= new connectionDetector(this);
        Enter=true;

        StrictMode.ThreadPolicy policy = new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        if(cd.icConnected())
        {
            connectANDretrive ();
        }
        else
        {
            Toast.makeText(resevedInvitatio.this,"مشكلة في الإتصال بالإنترنت",Toast.LENGTH_SHORT).show();
        }

    }


    public void connectANDretrive ()
    {
        System.out.print("here we are ");
        // this in order to set up the code to fetch data from database

        try {
            HttpClient httpClient = new DefaultHttpClient();
            // spacifi url for the retrive
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Dalal/EE.php?value="+UserName);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // set up the input stream to receive the data
            is = entity.getContent();
        }catch (Exception e){
            System.out.print("exception 1 caught");
            //exception handel code
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);

            // creat String builder object to hold the data

            StringBuilder sb = new StringBuilder();
            while ((line=reader.readLine())!=null)
                sb.append(line+"\n");

            //use toString() to get the data result
            result=sb.toString();
            int length = result.length();
            String str =result.replace('"',' ');
            String sreOne =str.substring(1,length-2);//i did not start from index 0 cause the string is retreved with spaces at the beging
            // chek the data
            arr= sreOne.split(",");
            System.out.print(sreOne);
            //==============================================Data spliting===============================================
            if(sreOne.contains(" nothing to print "))
            {Enter=false;
                Toast.makeText(resevedInvitatio.this,"no invitations yet",Toast.LENGTH_SHORT).show();}
            int length2 = arr.length;
            int index =0;
            int i =0;
            while (index<length2&&Enter)
            {   inviTopic.add(arr[index]);
                invID.add(arr[index+1]);
                index=index+2;
            }
            //==========================now fill the list view with the Invitations=======================================
            lv.setAdapter(new ArrayAdapter<String>(resevedInvitatio.this,android.R.layout.simple_list_item_1,inviTopic));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(resevedInvitatio.this,resevedInvitationDeatales.class);
                    intent.putExtra("invID",invID.get(position));
                    System.out.print("********** invID "+invID.get(position)+"*********");
                    startActivity(intent);
                }
            });
            //============================================================================================================


        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
