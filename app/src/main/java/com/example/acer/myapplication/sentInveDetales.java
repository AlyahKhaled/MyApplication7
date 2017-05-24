package com.example.acer.myapplication;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    public connectionDetector cd ;
    public Button      GoCancel  ;
    public  HttpClient httpClient ;
    public  HttpPost httpPost ;
    public HttpResponse response ;
    public HttpEntity entity ;

    public String inMorning   ;
    public String inEvning    ;
    public String inWeakend   ;
    public String ChangePlace ;//heel
    //============================================end===============================================




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_inve_detales);

        pref      = getSharedPreferences("login.conf", Context.MODE_PRIVATE);//2
        Log.d(TAG, pref.getString("UserName", ""));//3
        Log.d(TAG, pref.getString("PassWord", ""));//4
        UserName   = pref.getString("UserName", "");//5
        //============================================initializations===============================

        presentTextView    = (TextView) findViewById(R.id.textView4);
        AppsentTextView    = (TextView) findViewById(R.id.textView5);
        suggestionTextView = (TextView) findViewById(R.id.textView7);
        GoCancel           =  (Button)  findViewById(R.id.cancelBu) ;
        lv                 = (ListView) findViewById(R.id.plist);
        appsentlv          = (ListView) findViewById(R.id.apsentlist);
        suggestionlv       = (ListView) findViewById(R.id.suggestionList);
        cd                 = new connectionDetector(this);
        Suggestions        = new ArrayList<String>();
        Present            = new ArrayList<String>();
        Appsents           = new ArrayList<String>();
        Enter              =true;

      if(!(getIntent().getExtras().getString("venueId")).equals(null))
        {  value =  (getIntent().getExtras().getString("venueId"));
        int l =  value.length();
        venueID = value.substring(1,l-1);}

        StrictMode.ThreadPolicy policy = new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //=================================check the in going connection============================
        if(cd.icConnected())
        {connectANDretrive ();}
        else
        { Toast.makeText(sentInveDetales.this,"حدث خطأ في الإتصال",Toast.LENGTH_SHORT).show();}

        dictionary ();
     }

     public void dictionary (){

      inMorning   ="هل يمكن أن نجتمع صباحا"  ;
      inEvning    ="هل يمكن أن نجتمع مساء" ;
      inWeakend   ="هل يمكن تأجيل الإجتماع لنهاية الأسبوع" ;
      ChangePlace ="هل يمكن تغير مكان الإجتماع " ;

     }

    public void connectANDretrive ()
    {
        try {

             httpClient = new DefaultHttpClient();
             httpPost = new HttpPost("http://zwarh.net/zwarhapp/Dalal/sentInvDeatales.php?value="+venueID);
             response = httpClient.execute(httpPost);
             entity = response.getEntity();
             is = entity.getContent();

        }
        catch (Exception e)
        {System.out.print("exception 1 caught");}

        //=======================================read the retrieved data============================
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            sb.append(line + "\n");
            result = sb.toString();

            int lengthResult = result.length();
           if(lengthResult>0)
           {    dictionary ();
                String sreOne = result.substring(1, lengthResult - 2);
                result = sreOne.replace('"', ' ');
                result = result.replace("inMorning",inMorning);
                result = result.replace("inEvning", inEvning);
                result = result.replace("inWeakend", inWeakend);
                result = result.replace("ChangePlace",ChangePlace);
                arr = result.split(",");
            }

              if (!result.contains("connections errore "))
              {
                if (!result.contains("no responses yet "))
                       { postiveResult();}
                else negativResult(1);
              }
              else
                {negativResult(0);}
        }
        catch (IOException e) {e.printStackTrace();}
    }

    private void negativResult(int messageNum) {

        String errorMessage ;

        if(messageNum ==1)
        {errorMessage = "لا يوجد أي ردود حتى الآن";
            presentTextView.setText("الحاضرين : 0" );
            AppsentTextView.setText("الغير حاضريـن : 0" );
            suggestionTextView.setText("الإقتراحــات: 0" );
        }
        else
            errorMessage = "حدث خطأ في الإتصال!";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(sentInveDetales.this);
        alertDialogBuilder.setMessage(errorMessage);
        alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        Toast.makeText(sentInveDetales.this,errorMessage,Toast.LENGTH_SHORT).show();

    }

    private void postiveResult() {

//============================split the contant of the retrieved Data==============================
        int length = arr.length;
        int i = 0;
        while (i < length && length>0) {
            System.out.print("here");
            if (arr[i].contains("coming")) {
                Present.add(arr[i + 1]);
            }//to add the name

            else if (arr[i].contains("notComing")) {
                Appsents.add(arr[i + 1]);
            } //to add the name

            else if (arr[i].contains("suggestion")) {
                Suggestions.add(arr[i + 1] + ": " + arr[i + 2]);
            } //to add the name and suggestion
            i = i + 3;
        }
//================================ now fill the list view with the names============================


        presentTextView.setText("الحاضرين : " + Present.size());
        AppsentTextView.setText("الغير حاضريـن" + Appsents.size());
        suggestionTextView.setText("الإقتراحــات" + Suggestions.size());

        lv.setAdapter(new ArrayAdapter<String>(sentInveDetales.this, android.R.layout.simple_list_item_1, Present));
        appsentlv.setAdapter(new ArrayAdapter<String>(sentInveDetales.this, android.R.layout.simple_list_item_1, Appsents));
        suggestionlv.setAdapter(new ArrayAdapter<String>(sentInveDetales.this, android.R.layout.simple_list_item_1, Suggestions));

    }

    public void cancelInvitation (View view)
    {
        Intent intent = new Intent(sentInveDetales.this, cancelInvitationLayout.class);


        if(cd.icConnected()&&!venueID.equals(null))
        { intent.putExtra("venueId",venueID);
            startActivity(intent);}
        else
        { Toast.makeText(sentInveDetales.this,"Network connection problems",Toast.LENGTH_SHORT).show();
          Toast.makeText(sentInveDetales.this,"لايمكن إلغاء الدعوة",Toast.LENGTH_SHORT).show();}
    }

    public void Back (View view)
    {
        onBackPressed();
    }

    public void toProfUser (View view)
    {
        Intent intent = new Intent(sentInveDetales.this,profileuser.class);
        startActivity(intent);
    }


}
