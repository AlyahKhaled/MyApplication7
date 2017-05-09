package com.example.acer.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

public class sentInveDetales extends AppCompatActivity {

    final String TAG = this.getClass().getName();
    String            UserName  ;
    SharedPreferences pref;//1
    //============================================DalalPART=========================================
    public ListView    lv ;//where attendees names inflate
    public ListView    appsentlv ;//where apsent names inflate
    public ListView    suggestionlv ;//where attendees names inflate
    public InputStream is ;//where attendees names inflate
    public String      line   = null;//to save what has been read from DB
    public String      result = null;//to convert what have been read from string builder to String
    public String []   arr ;//to suplet the result in array for easy modification
    public String      value  ;// to save the venue Id for the where condition in php file
    public String      venueID;

    public ArrayList<String> Suggestions ; // to save the Suggestions and the suggester
    public ArrayList<String> Present ;     // to save the  Present Guest's name
    public ArrayList<String> Appsents ;    // to save the  Appsents Guest's name

    public TextView    presentTextView ;
    public TextView    AppsentTextView ;
    public TextView    suggestionTextView ;
    public boolean     Enter ;
    connectionDetector cd ;
    //============================================end===============================================




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_inve_detales);

        pref      = getSharedPreferences("login.conf", Context.MODE_PRIVATE);//2
        Log.d(TAG, pref.getString("UserName", ""));//3
        Log.d(TAG, pref.getString("PassWord", ""));//4
        UserName   = pref.getString("UserName", "");//5
        //============================================DalalPART=====================================
        //initializations

        presentTextView    = (TextView) findViewById(R.id.textView4);
        AppsentTextView    = (TextView) findViewById(R.id.textView5);
        suggestionTextView = (TextView) findViewById(R.id.textView7);
        lv                 = (ListView) findViewById(R.id.plist);
        appsentlv          = (ListView) findViewById(R.id.apsentlist);
        suggestionlv       = (ListView) findViewById(R.id.suggestionList);
        cd                 = new connectionDetector(this);
        Suggestions        = new ArrayList<String>();
        Present            = new ArrayList<String>();
        Appsents           = new ArrayList<String>();
        Enter              =true;

        //retrieve the value of the venueID
        value =  (getIntent().getExtras().getString("venueId"));
        int l =  value.length();
        venueID = value.substring(1,l-1);
        System.out.println("*********************************the venueId:"+venueID+venueID.length());



        // this is important to connect to the internet <not sure>
        StrictMode.ThreadPolicy policy = new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if(cd.icConnected())
        {connectANDretrive ();}
        else
        { Toast.makeText(sentInveDetales.this,"Network connection problems",Toast.LENGTH_SHORT).show();}
     }

    public void connectANDretrive ()
    {

        // this in order to set up the code to fetch data from database
        try {
            HttpClient httpClient = new DefaultHttpClient();
            // specific url for the retrieve
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Dalal/sentInvDeatales.php?value="+venueID);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // set up the input stream to receive the data
            is = entity.getContent();
        }//exception handel code
        catch (Exception e)
        {System.out.print("exception 1 caught");}

        //read the retrieved data
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            // create String builder object to hold the data
            StringBuilder sb = new StringBuilder();
            while ((line=reader.readLine())!=null)
                sb.append(line+"\n");
            System.out.println("**********************"+sb);
            result=sb.toString();
            // chek the data
            System.out.println("*******here is my Data************");
            System.out.print(result+"***LENGTH***"+result.length());

            int lengthResult =result.length();
            String sreOne =result.substring(1,lengthResult-2);//i did not start from index 0 cause the string is retreved with spaces at the beging
            result=sreOne.replace('"',' ');
            arr= result.split(",");
            System.out.println("*********"+result);

           if(result.equals(" no responses yet "))
           {Enter=false;
            Toast.makeText(sentInveDetales.this,"no responses yet",Toast.LENGTH_SHORT).show();}
//**************************************************************split the contant of the retrieved Data********************************************
            int length = arr.length ;
            int i=0 ;
            while (i< length&&Enter)
            {    System.out.print("here");
                if(arr[i].contains("coming"))
                {Present.add(arr[i+1]);}//to add the name

                else if(arr[i].contains("notComing"))
                {Appsents.add(arr[i+1]);} //to add the name

                else if(arr[i].contains("suggestion"))
                {Suggestions.add(arr[i+1]+": "+arr[i+2]);} //to add the name
                i=i+3;
            }
//**************************************************************print the data for testing********************************************************
            System.out.println("the coming guests : ");

            for (int a=0;a<Present.size();a++)
            {

                System.out.println(Present.get(a));

            }

            System.out.println("the not coming guests : ");

            for (int a=0;a<Appsents.size();a++)
            {

                System.out.println(Appsents.get(a));

            }

            System.out.println("the Suggestions  : ");

            for (int a=0;a<Suggestions.size();a++)
            {

                System.out.println(Suggestions.get(a));

            }
            System.out.println();

            //==================================================== now fill the list view with the names========================================

            presentTextView.setText("الحاضرين : "+Present.size());
            AppsentTextView.setText("الغير حاضريـن"+Appsents.size());
            suggestionTextView.setText("الإقتراحــات"+Suggestions.size());

            lv.setAdapter(new ArrayAdapter<String>(sentInveDetales.this,android.R.layout.simple_list_item_1,Present));
            appsentlv.setAdapter(new ArrayAdapter<String>(sentInveDetales.this,android.R.layout.simple_list_item_1,Appsents));
            suggestionlv.setAdapter(new ArrayAdapter<String>(sentInveDetales.this,android.R.layout.simple_list_item_1,Suggestions));
        }
        catch (IOException e) {e.printStackTrace();}
    }
}
