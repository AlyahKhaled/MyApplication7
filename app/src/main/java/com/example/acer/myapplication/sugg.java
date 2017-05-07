package com.example.acer.myapplication;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    public EditText sugg;

    public Button insert ;
    String sug;
    public InputStream is ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugg);


        sugg = (EditText) findViewById(R.id.apo);
         insert= (Button)findViewById(R.id.button);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 sug = sugg.getText().toString();

                if(sug.length() == 0) {sugg.setError("please enter suggestion ");return; }

                if (sug.matches(".*[^a-z^A-Z].*")){sugg.setError("please enter valid suggestion "); return;}




                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);

                    nameValuePair.add(new BasicNameValuePair("sugg", sug));
                    nameValuePair.add(new BasicNameValuePair("selectedFromList", invitations.selectedFromList));


                    try {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Mai/Add_sugg.php?ID=" + invitations.ID);
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                        HttpResponse response = httpClient.execute(httpPost);
                        HttpEntity entity = response.getEntity();
                        is = entity.getContent();
                        String msg = "Message send succefully";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        });

    }
}
