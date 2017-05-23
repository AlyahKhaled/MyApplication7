package com.example.acer.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
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
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class invitation_info extends AppCompatActivity {
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
        setContentView(R.layout.activity_invitation_info);
        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        Log.d(TAG, pref.getString("UserName", ""));// 3
        Log.d(TAG, pref.getString("PassWord", ""));// 4
        UserName = pref.getString("UserName", ""); // 5
        lv = (ListView) findViewById(R.id.list);
        map = (Button) findViewById(R.id.button2);
        sug= (Button)findViewById(R.id.sug);
        ignor= (Button)findViewById(R.id.ignor);
        accept= (Button)findViewById(R.id.accept);
        cd = new connectionDetector(this);
        if(cd.icConnected()){

 //==============================================================================================================

        StrictMode.ThreadPolicy policy = new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//==============================================================================================================

        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Mai/invitation_info.php?ID=" + invitations.ID);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

//==============================================================================================================


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
                result=result.replace("Invitation Name","اسم الدعوة");
                result=result.replace("Inviter UserName","اسم الداعي");
                result=result.replace("Date of the event","اليوم");
                result=result.replace("Time of the event","الوقت");
                result=result.replace("additional Information","معلومات اضافية");
                result=result.replace("Place Name","اسم المكان");
                result=result.replace("null","لا توجد معلومات اضافية ");
                result=result.replace("No additional information","لا توجد معلومات اضافية ");
                result=result.replace("Be on time"," تعالوا في الوقت ");
                result=result.replace("No kids allowed","يمنع اصطحاب الاطفال ");
                result=result.replace("Job meeting","   اجتماع عمل ");
                result=result.replace("Allowed to bring friends "," يمكنك احضتر مرافق ");
                result=result.replace("Celebration party","  حفلة معايدة ");
                result=result.replace("Friends gathering","   اجتماع اصدقاء ");
                result=result.replace("Children party","     حفلة اطفال ");
                result=result.replace("Graduation party"," حفلة نجاح ");
                result=result.replace("Home"," المنزل ");
                result=result.replace("Outside"," خارج المنزل ");

                int length =result.length();

                String sreOne =result.substring(1,length-2);
                //use toString() to get the data result
                result=sb.toString();
                // check the data
                System.out.println(sreOne);
                arr= sreOne.split(",");

//==============================================================================================================

            }   catch (IOException e) {
                e.printStackTrace();
            }


//==============================================================================================================

            lv.setAdapter(new ArrayAdapter<String>(invitation_info.this,android.R.layout.simple_list_item_1,arr));

                accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                String selectedFromList= invitations.selectedFromList;
                List<NameValuePair> nameValuePair =new ArrayList<NameValuePair>(1);
                nameValuePair.add(new BasicNameValuePair("UserName", UserName));
                nameValuePair.add(new BasicNameValuePair("selectedFromList",selectedFromList));
//==============================================================================================================

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Mai/come.php?ID=" + invitations.ID);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    is=entity.getContent();
                    String msg = "تم ارسال الموافقة بنجاح";
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
//==============================================================================================================
                ignor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<NameValuePair> nameValuePair =new ArrayList<NameValuePair>(1);
                AlertDialog.Builder builder = new AlertDialog.Builder(invitation_info.this);
                builder.setMessage("هل انت متاكد انك تريد الاعتذار عن هذه الدعوة ");
                builder.setPositiveButton("لا", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {

                        //do things
                    }
                });

                            builder.setNegativeButton("نعم", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String selectedFromList= invitations.selectedFromList;
                            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
                            nameValuePair.add(new BasicNameValuePair("selectedFromList",selectedFromList));
//==============================================================================================================

                        try{
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Mai/apo.php?ID=" + invitations.ID);
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                            HttpResponse response = httpClient.execute(httpPost);
                            HttpEntity entity = response.getEntity();
                            is=entity.getContent();
                            String msg = "تم ارسال الاعتذار بنجاح ";
                            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }




                        }

                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

   }else
        { Toast.makeText(invitation_info.this,"حدث خطأ في الإتصال!",Toast.LENGTH_SHORT).show();}}

    //============================================================================================================== To go Back

    public void Back (View view)
    {
        onBackPressed();
    }

// to go to the profile
    public void profile (View view)
    {
        Intent intent = new Intent(invitation_info.this, profileuser.class);
        startActivity(intent);
    }
// To enrt sugg
    public void sugg (View view)
    {
        Intent intent = new Intent(invitation_info.this, sugg.class);
        startActivity(intent);
    }
//to go to the map
    public void map (View view)
    {
        Intent intent = new Intent(invitation_info.this,coordenation.class);
        startActivity(intent);
    }
}
