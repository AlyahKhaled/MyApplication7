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
    ArrayList<String> Dates ; // to save the Dates for each invitations

    public String  Celebration_party;
    public String  Friends_gathering;
    public String  Job_meeting;
    public String  Children_party;
    public String  Graduation_party;

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
        Dates=new ArrayList<String>();

        lv = (ListView) findViewById(R.id.sentList);
        cd= new connectionDetector(this);

        Dictienary ();

        StrictMode.ThreadPolicy policy = new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if(cd.icConnected())
        {connectANDretrev ();}
        else
        { Toast.makeText(sentInviitations.this,"حدث مشاكل في الإتصال",Toast.LENGTH_SHORT).show();}

    }

    public void Dictienary ()
    {
        Celebration_party ="حفلة معايدة";
        Friends_gathering = "اجتماع اصدقاء";
        Job_meeting = "اجتماع عمل";
        Children_party ="حفلة أطفال";
        Graduation_party = "حفلة نجاح";

    }

       public void connectANDretrev ()
    {
        //==========================================Connection======================================

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Dalal/sentInv.php?value="+UserName);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }catch (Exception e){
            System.out.print("exception 1 caught");

        }
        //===================================== reading data =======================================
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            sb.append(line + "\n");
            result = sb.toString();

             result = result.replace('"', ' ');
             result = result.replace("Celebration party",Celebration_party);
             result = result.replace("Friends gathering",Friends_gathering);
             result = result.replace("Job meeting",Job_meeting);
             result = result.replace("Children party",Children_party);
             result = result.replace("Graduation party ",Graduation_party);

                if (!result.contains("connections errore "))
                {
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
        String sreOne = result.substring(1, length - 2);

        arr = sreOne.split(",");
        int arrLength = arr.length;


        for (int i = 0; i < arrLength; ) {

                String str ="\n"+"نوع الدعوة: "+arr[i]+"\n"+"بتاريخ: "+arr[i+1]+"\n"+"الوقت: "+arr[i+2]+"\n";
                boolean add = invitations.add(str);
                if (add == true)
                   System.out.println("added successfully");
                boolean ad = venueId.add(arr[i+3]);
                if (ad == true)
                    System.out.println("added successfully");

            i=i+4;
        }

        lv.setAdapter(new ArrayAdapter<String>(sentInviitations.this, android.R.layout.simple_list_item_1, invitations));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean flag = false;
                String str = (lv.getItemAtPosition(position)).toString();

                if (!str.equals(" there are no invitations sent yet :( ")) {
                    Intent intent = new Intent(sentInviitations.this, sentInveDetales.class);
                    intent.putExtra("venueId", venueId.get(position));
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