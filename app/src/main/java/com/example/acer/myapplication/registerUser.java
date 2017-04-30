package com.example.acer.myapplication;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerUser extends AppCompatActivity {

    //Declaration
    public EditText username, name, passwored, phoneNumber ,Fquestion ,Squestion , Tquestion;
    public  String  un , na ,pa , ph , fq , sq ,tq ;
    String line=null;
    String result=null;

    public Button insert ;
    public InputStream is ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        //initialise the field
        username    = (EditText) findViewById(R.id.username);
        name        = (EditText) findViewById(R.id.name);
        passwored   = (EditText) findViewById(R.id.pass);
        phoneNumber = (EditText) findViewById(R.id.phone);
        Fquestion   = (EditText) findViewById(R.id.BF);
        Squestion   = (EditText) findViewById(R.id.BT);
        Tquestion   = (EditText) findViewById(R.id.BC);
        insert= (Button)findViewById(R.id.button);


        // those to lines for get the network to work
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Register ();
            }
        });


    }
//=============================================================


    public void Register ()
    {
        initialise();

        if(!valied())
        {
            String msg = "Signup Failed";
            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        }
        else{
            onSignUpSuccess();
        }


    }
//=============================================================

    public boolean valied() {
        boolean valied = true;

        if(un.isEmpty()||un.length()>15){
            username.setError("ادخل اسم مستخدم لا يتجاوز 15 حرف");
            valied = false;}

        if(na.isEmpty()||na.length()>20){
            name.setError("ادخل اسم  لا يتجاوز 20 حرف");
            valied=false;}

        if(pa.isEmpty()||pa.length()>30){//it must be hashed
            passwored.setError("ادخل كلمة المرور");
            valied=false; }
        if(ph.isEmpty()||ph.length()!=10){
            phoneNumber.setError("الرجاء إدخال رقم هاتف صحيح");
            valied=false; }
        if(fq.isEmpty()||fq.length()>40){
            Fquestion.setError("الرجاء الإجابة على السؤال");//it must be hashed
            valied=false; }
        if(sq.isEmpty()||sq.length()>40){
            Squestion.setError("الرجاء الإجابة على السؤال");//it must be hashed
            valied=false; }
        if(tq.isEmpty()||tq.length()>40){
            Tquestion.setError("الرجاء الإجابة على السؤال");//it must be hashed
            valied=false; }




        return valied;
    }
//==============================================================

    public void initialise() {
        //initialise the Strings to test the content
        un = username.getText().toString();
        na = name.getText().toString();
        pa = passwored.getText().toString();
        ph= phoneNumber.getText().toString();
        fq= Fquestion.getText().toString();
        sq= Squestion.getText().toString();
        tq= Tquestion.getText().toString();

    }
//==============================================================
public void onSignUpSuccess() {
    //insert the Data into Data Base

    List<NameValuePair> nameValuePair =new ArrayList<NameValuePair>(1);

    nameValuePair.add(new BasicNameValuePair("uname",un));
    nameValuePair.add(new BasicNameValuePair("name",na));
    nameValuePair.add(new BasicNameValuePair("psw",pa));
    nameValuePair.add(new BasicNameValuePair("phone",ph));
    nameValuePair.add(new BasicNameValuePair("friend",fq));
    nameValuePair.add(new BasicNameValuePair("teacher",sq));
    nameValuePair.add(new BasicNameValuePair("city",tq));


    try{
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Dalal/addUser.php");
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        is=entity.getContent();



        // String msg = "Data Entered succefully";
        //Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
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
        if(result.contains("true"))
        {
            System.out.println("************************ result "+result+"**********************************************");
            String msg = "تم إنشاء الحساب بنجاح";
            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        }

        else{
            System.out.println("************************ result "+result+"**********************************************");
            username.setError("اسم المستخدم موجود مسبقا");
            String msg = "فشل العملية";
            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        }
        Log.e("pass 2", "connection success ");
    }
    catch(Exception e)
    {
        Log.e("Fail 2", e.toString());
    }
}
//==============================================================

    public boolean validPhoneNumber (String phone)
    { boolean valiedNum =true;
        Pattern pattern =Pattern.compile("\\d{3}-\\{7}");
        Matcher matcher =pattern.matcher(phone);

        if(phone.isEmpty()||phone.length()!=10)
        {
            if(matcher.matches())
            {
                valiedNum = true;
            }
            else
            {
                valiedNum =false;
            }
        }
        else {
            valiedNum =false;
        }

        return valiedNum;

    }
}
