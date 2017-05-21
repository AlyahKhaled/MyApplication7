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

public class sentInviitations extends AppCompatActivity {

    final String TAG = this.getClass().getName();
    String   UserName  ;
    SharedPreferences pref;//1
    //============================================DalalPART=========================================
    ListView lv ;//where attendees names inflate
    InputStream is ;//where attendees names inflate
    String line = null;//to save what has been read from DB
    String result = null;//to convert what have been read from string builder to String
    String [] arr ;//to suplet the result in array for easy modification
    connectionDetector cd ;

    ArrayList<String> invitations ;// to save the invitations
    ArrayList<String> venueId;// to save the venueId for each invitations
    //============================================end===============================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_inviitations);

        pref      = getSharedPreferences("login.conf", Context.MODE_PRIVATE);//2
        Log.d(TAG, pref.getString("UserName", ""));//3
        Log.d(TAG, pref.getString("PassWord", ""));//4
        UserName   = pref.getString("UserName", "");//5
    //============================================DalalPART=========================================
        invitations = new ArrayList<String>();
        venueId= new ArrayList<String>();
        lv = (ListView) findViewById(R.id.sentList);
        cd= new connectionDetector(this);

        StrictMode.ThreadPolicy policy = new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if(cd.icConnected())
        {connectANDretrev ();}
        else
        { Toast.makeText(sentInviitations.this,"Network connection problems",Toast.LENGTH_SHORT).show();}

    }


       public void connectANDretrev ()
    {
        // this in order to set up the code to fetch data from database

        try {
            HttpClient httpClient = new DefaultHttpClient();

            // spacial url for the retrive

            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Dalal/sentInv.php?value="+UserName);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // set up the input stream to receive the data

            is = entity.getContent();


        }catch (Exception e){
            System.out.print("exception 1 caught");
            //exception handel code
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

            // creat String builder object to hold the data

            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null)
                sb.append(line + "\n");
            //use toString() to get the data result
            result = sb.toString();
            System.out.println("**" + result + result + "**" + result.length());
            result = result.replace('"', ' ');

                if (!result.contains("connections errore ")) {
                    System.out.print("!result.contains (connections errore) "+!result.contains("connections errore "));
                if (!result.contains("there are no invitations sent yet "))
                     { postivResult();}
                else
                    {NegativResult(1);}

            }
            else{NegativResult(0);}

            }

            catch(IOException e){
                e.printStackTrace();
            }

    }

    private void NegativResult(int messageNum) {
        String errorMessage ;

        if(messageNum ==1)
            errorMessage = "لا يوجد دعوات";
        else
            errorMessage = "حدث خطأ في الإتصال!";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(sentInviitations.this);
        alertDialogBuilder.setMessage(errorMessage);
        alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        arr = errorMessage.split(",");
        lv.setAdapter(new ArrayAdapter<String>(sentInviitations.this, android.R.layout.simple_list_item_1, arr));
    }

    private void postivResult() {

        int length = result.length();
        String sreOne = result.substring(1, length - 2);//i did not start from index 0 cause the string is retreved with spaces at the beging
        // chek the data

        System.out.println("*******here is my Data************");
        System.out.println(sreOne);
        arr = sreOne.split(",");
        int arrLength = arr.length;


        for (int i = 0; i < arrLength; i++) {
            if (i % 2 == 0) {
                boolean add = invitations.add(arr[i]);
                if (add == true)
                    System.out.println("added successfully");
            } else if (i % 2 == 1) {
                boolean add = venueId.add(arr[i]);
                if (add == true)
                    System.out.println("added successfully");
            }

        }


        // now fill the list view with the names
        lv.setAdapter(new ArrayAdapter<String>(sentInviitations.this, android.R.layout.simple_list_item_1, invitations));

        // let provied the on click lstiener when and item is clicked

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean flag = false;
                String str = (lv.getItemAtPosition(position)).toString();
                System.out.println("*******str*******" + str + "************str************");
                if (!str.equals(" there are no invitations sent yet :( ")) {
                    Intent intent = new Intent(sentInviitations.this, sentInveDetales.class);
                    intent.putExtra("venueId", venueId.get(position));
                    System.out.println(" the position : " + position);
                    startActivity(intent);
                }

            }
        });
    }

    public void Back (View view)
    {
        onBackPressed();
    }


    public void profile (View view)
    {
        Intent intent = new Intent(sentInviitations.this, profileuser.class);
        startActivity(intent);
    }}