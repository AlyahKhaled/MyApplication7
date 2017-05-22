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

public class editProfileAdmin extends AppCompatActivity {
    final String TAG = this.getClass().getName();

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String UserName;
    String Namee ;
    public EditText nameUs;
    Button editB;
    String linee=null;
    String resulte=null;
    public InputStream iss ;
    public InputStream is ;
    String line = null;
    String result = null;
    connectionDetector cd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profileadmin);

        nameUs    = (EditText) findViewById(R.id.nameUs);


        editB = (Button) findViewById(R.id.editB);



  pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        Log.d(TAG, pref.getString("UserName", ""));
        Log.d(TAG, pref.getString("PassWord", ""));

        UserName = pref.getString("UserName", "");



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        cd = new connectionDetector(this);


        if (cd.icConnected()) {
        ///////////////////////////////////////////////////
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Alyah/retriveNameAdmin.php?UserName="+UserName);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();


            is = entity.getContent();


        }catch (Exception e){
            System.out.print("exception 1 caught");
            //exception handel code
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);

            StringBuilder sb = new StringBuilder();
            while ((line=reader.readLine())!=null)
                sb.append(line);

            result=sb.toString();
            result=result.replace('"',' ');
            result=result.replace(']',' ');
            result=result.replace('[',' ');
            result=result.replace(" ","");


            // check the data
            nameUs.setText(result);



        }  catch (IOException e) {
            e.printStackTrace();
        }

    }else
    { Toast.makeText(editProfileAdmin.this,"Network connection problems",Toast.LENGTH_SHORT).show();}
    }






    public void dat (View view)
    {
        edit();


       }


    public void Back(View v){
       edit();
    }

    public void edit(){

        Namee =nameUs.getText().toString();

        if(Namee.isEmpty()||Namee.length()>20) {
            nameUs.setError("ادخل اسمك ");
            Toast.makeText(editProfileAdmin.this, " فشل التعديل ", Toast.LENGTH_LONG).show();
        } else {

            if (!(Namee.contains(result))){

                AlertDialog.Builder builder = new AlertDialog.Builder(editProfileAdmin.this);
                builder.setMessage("هل انت متأكد تريد حفظ التعديلات ؟");
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {

                        Intent in = new Intent(editProfileAdmin.this, admienprofile.class);
                        startActivity(in);

                    }
                });


                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {




                        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);

                        nameValuePair.add(new BasicNameValuePair("Namee", Namee));
                        try {
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Alyah/editNameAdmin.php?UserName=" + UserName);
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                            HttpResponse response = httpClient.execute(httpPost);
                            HttpEntity entity = response.getEntity();
                            iss = entity.getContent();


                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(iss, "iso-8859-1"), 8);
                            StringBuilder sb = new StringBuilder();
                            while ((linee = reader.readLine()) != null) {
                                sb.append(linee + "\n");
                            }
                            iss.close();
                            resulte = sb.toString();
                            //test the query
                            if (resulte.contains("true")) {
                                System.out.println("************************ result " + resulte + "**********************************************");
                                String msg = "تم تعديل اسمك ";
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                            }
                            else{
                                String msg = "لم يتم تعديل اسمك ";
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            }

                        }
                        catch(Exception e)
                        {
                            Log.e("Fail 2", e.toString());
                        }




                        Intent in = new Intent(editProfileAdmin.this, admienprofile.class);
                        startActivity(in);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }else {String msg = "لا يوجد تعديل ";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();}
        }
    }
}














