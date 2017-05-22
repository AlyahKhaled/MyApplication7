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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class cancelInvitationLayout extends AppCompatActivity {
    final String TAG = this.getClass().getName();
    String UserName;
    SharedPreferences pref;//1

    //============================================DalalPART=========================================
    public String venueID;
    public connectionDetector cd;
    public Button cancelInv;
    public EditText apologizeMeass;
    public InputStream is;
    public String line = null;
    public String result = null;
    public String message;
    String[] arr;
    public String []arraySpinner ;
    public Spinner apolgies ;
    public String [] spinnerCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_invitation_layout);

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);//2
        Log.d(TAG, pref.getString("UserName", ""));//3
        Log.d(TAG, pref.getString("PassWord", ""));//4
        UserName = pref.getString("UserName", "");//5
        this.arraySpinner=new String [] {"أعتذر عن الإجتماع لظروف طارئة","أعتذر عن الإجتماع لظروف خاصة","أعتذر عن الإجتماع لظروف صحية","أعتذر عن الإجتماع لظروف عائلية","أعتذر عن الإجتماع لتغير جدول أعمالي"};
        spinnerCode=new String[]{"emergencies","personalReasons","healthIssues","familyReasons","Business"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arraySpinner);

        //============================================DalalPART=========================================
        //retrieve the value of the venueID
        venueID = (getIntent().getExtras().getString("venueId"));
        cd                 = new connectionDetector(this);
        cancelInv          = (Button) findViewById(R.id.button10);
        //apologizeMeass     = (EditText) findViewById(R.id.editText);
        apolgies           = (Spinner) findViewById(R.id.spinner2);
        apolgies.setAdapter(adapter);

         message=apolgies.getSelectedItem().toString();
            System.out.println("*************************"+message+"*******************************");




    }

   public void canselInv(View view) {

            //****

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cancelInvitationLayout.this);
            alertDialogBuilder.setMessage("هل أنت متأكد من إلغاء الدعوة ؟ ");
            alertDialogBuilder.setPositiveButton("No",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });

            alertDialogBuilder.setNegativeButton("yes",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            if(!(apolgies.toString()).equals(null)) {

                                if (cd.icConnected() && !venueID.equals(null)) {

                                    message = apolgies.getSelectedItem().toString();
                                    System.out.println("*************************" + message + "*******************************");

                                    String str= cancelationCode(message);
                                    cancel(str);
                                    Intent intent = new Intent(cancelInvitationLayout.this, invitationOptiens.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(cancelInvitationLayout.this, "حدث خطأ في الإتصال", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(cancelInvitationLayout.this, "لايمكن إلغاء الدعوة", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


    }

    public String cancelationCode(String m){
        String str =null;
        int  length = arraySpinner.length;

        for (int i=0;i<length;i++)
        if(arraySpinner[i].equals(m))
        {  str=spinnerCode[i];System.out.print("str= "+str+"m= "+m);}


             return str;

    }

    public void cancel (String message)
    {    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       StrictMode.setThreadPolicy(policy);


       List<NameValuePair> nameValuePair =new ArrayList<NameValuePair>(1);

       nameValuePair.add(new BasicNameValuePair("venueID",venueID));
       nameValuePair.add(new BasicNameValuePair("message",message));

       // this in order to set up the code to fetch data from database

       try{
           HttpClient httpClient = new DefaultHttpClient();
           HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Dalal/apologaizeMessage.php");
           httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
           HttpResponse response = httpClient.execute(httpPost);
           HttpEntity entity = response.getEntity();
           is=entity.getContent();


       } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
       } catch (ClientProtocolException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();

       }



       try
       {
           BufferedReader reader = new BufferedReader (new InputStreamReader(is,"iso-8859-1"),8);
           StringBuilder sb = new StringBuilder();
           while ((line = reader.readLine()) != null)
           {
               sb.append(line + "\n");
           }
           is.close();
           result = sb.toString();
           //test the query
           if(result.contains("1"))
           {
               String msg = "تم إلغاء الدعوة ";
               Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
           }
          else {      String msg = "فشل إلغاء الدعوة ";
               Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();}

           Log.e("pass 2", "connection success ");
       }
       catch(Exception e)
       {
           Log.e("Fail 2", e.toString());
       }

   }

    public void Back (View view)
    {
        onBackPressed();
    }

    public void Prof (View view)
    {
        Intent intent = new Intent(cancelInvitationLayout.this,profileuser.class);
        startActivity(intent);
    }



}
