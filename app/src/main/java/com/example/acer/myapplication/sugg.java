package com.example.acer.myapplication;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sugg extends AppCompatActivity {

    public Spinner sugg;
    public Button insert ;
    String sug;
    public InputStream is ;
    public String[] arraySpinner;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugg);

        this.arraySpinner = new String[] {
          "هل يمكن ان نجتمع صباحا ", "هل يمكن ان نجتمع مساء", "هل يمكن تاجيل الاجتماع لنهاية الاسبوع", "هل يمكن تغيير مكان الاجتماع  ",
        };


        sugg = (Spinner) findViewById(R.id.apo);
        insert= (Button)findViewById(R.id.button);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, arraySpinner);
        sugg.setAdapter(adapter);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

                 insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 sug = sugg.getSelectedItem().toString();
                 if(sug.equals(arraySpinner[0])){sug="InMorning";}
                 else if(sug.equals(arraySpinner[1])){sug="InEvning";}
                 else if(sug.equals(arraySpinner[2])){sug="Weakened";}
                 else if(sug.equals(arraySpinner[3])){sug="ChangePlace";}

                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
                    nameValuePair.add(new BasicNameValuePair("sugg", sug));
                    nameValuePair.add(new BasicNameValuePair("selectedFromList", invitations.selectedFromList));


                    try {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Mai/Add_sugg.php?ID=" + invitations.ID);
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,"UTF-8"));
                        HttpResponse response = httpClient.execute(httpPost);
                        HttpEntity entity = response.getEntity();
                        is = entity.getContent();
                        String msg = " تم ارسال الاقتراح بنجاح ";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        notification();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
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
        Intent intent = new Intent(sugg.this, profileuser.class);
        startActivity(intent);
    }

    public void notification(){


        NotificationCompat.Builder notification=(NotificationCompat.Builder)new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.zwarahlogo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.zwarahlogo))
                .setContentTitle("Notification")
                .setContentText("You have a Suggestion ");

        NotificationManager NotificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationManager.notify(1,notification.build());
    }

}
