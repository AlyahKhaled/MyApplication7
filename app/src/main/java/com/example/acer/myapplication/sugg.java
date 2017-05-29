package com.example.acer.myapplication;



import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
                 if(sug.equals(arraySpinner[0])){sug="inMorning";}
                 else if(sug.equals(arraySpinner[1])){sug="inEvning";}
                 else if(sug.equals(arraySpinner[2])){sug="inWeakend";}
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



}
