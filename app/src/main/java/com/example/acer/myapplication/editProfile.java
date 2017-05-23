package com.example.acer.myapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.List;

public class editProfile extends AppCompatActivity {
    final String TAG = this.getClass().getName();

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String UserName;
    String birth ="null";
    String Namee ;


    public EditText nameUs ;
    Button editB;
    ImageButton dat;
    TextView mDisplayDate;
    String linee=null;
    String resulte=null;

    public InputStream iss ;

    DatePickerDialog.OnDateSetListener mDateSetListener;


    public InputStream is ;


    String line = null;
    String result = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameUs    = (EditText) findViewById(R.id.nameUs);


        editB = (Button) findViewById(R.id.editB);
        dat = (ImageButton) findViewById(R.id.imageButton8);


        mDisplayDate = (TextView) findViewById(R.id.textView9);

  pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        Log.d(TAG, pref.getString("UserName", ""));
        Log.d(TAG, pref.getString("PassWord", ""));

        UserName = pref.getString("UserName", "");



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ///////////////////////////////////////////////////
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Alyah/retriveName.php?UserName="+UserName);
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



        dat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();

                int year = cal.get(java.util.Calendar.YEAR);
                int month = cal.get(java.util.Calendar.MONTH);
                int day = cal.get(java.util.Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        editProfile.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: yyyy-mm-dd: " + year + "-" + month + "-" + day);

                String date = year + "-" + month + "-" + day;
                mDisplayDate.setText(date);
                birth=mDisplayDate.getText().toString();


            }
        };




    }






    public void dat (View view) {
        edit();
    }



    public void Back(View v){
        edit();

    }

    public void edit(){


        Namee =nameUs.getText().toString();

        if(Namee.isEmpty()||Namee.length()>20) {
            nameUs.setError("ادخل اسمك ");
            Toast.makeText(editProfile.this, " فشل التعديل ", Toast.LENGTH_LONG).show();
        }
         else {


            if (!(Namee.contains(result)) || !(birth.contains("null"))) {


                AlertDialog.Builder builder = new AlertDialog.Builder(editProfile.this);
                builder.setMessage("هل انت متأكد تريد حفظ التعديلات ؟");
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int id) {
                        Intent in = new Intent(editProfile.this, profileuser.class);
                        startActivity(in);
                    }
                });


                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!(Namee.contains(result))) {


                            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);

                            nameValuePair.add(new BasicNameValuePair("Namee", Namee));
                            try {
                                HttpClient httpClient = new DefaultHttpClient();
                                HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Alyah/editName.php?UserName=" + UserName);
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

                                } else {
                                    String msg = "لم يتم تعديل اسمك ";
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception e) {
                                Log.e("Fail 2", e.toString());
                            }


                        }


                        if (!(birth.contains("null"))) {

                            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);

                            nameValuePair.add(new BasicNameValuePair("birth", birth));
                            try {
                                HttpClient httpClient = new DefaultHttpClient();
                                HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Alyah/editBirth.php?UserName=" + UserName);
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
                                    String msg = "تم تعديل تاريخ ميلادك ";
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                                } else {
                                    String msg = "لم يتم تعديل تاريخ ميلادك ";
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception e) {
                                Log.e("Fail 2", e.toString());
                            }


                        }

                        Intent in = new Intent(editProfile.this, profileuser.class);
                        startActivity(in);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            } else {
                String msg = "لا يوجد تعديلات ";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Intent in = new Intent(editProfile.this, profileuser.class);
                startActivity(in);

            }
        }
    }
}














