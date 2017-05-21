package com.example.acer.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

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
import java.util.HashMap;
import java.util.List;

public class forgetpassworedAdmin extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    final String TAG = this.getClass().getName();
    Button button3,button;
    EditText editText10 ,editText11 ,editText12;
    boolean flag=false;
    int que;
    String newPass;
    String UserName , ans;

    String line=null;
    String result=null;
    connectionDetector cd ;

    public InputStream is ;
    Spinner spinner;
    String[] quest = {"ماهو اسم صديقك المفضل ؟ "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassworedadmin);

        spinner = (Spinner) findViewById(R.id.spinner);
        editText10 = (EditText) findViewById(R.id.editText10);
        editText11 = (EditText) findViewById(R.id.editText11);
        editText12 = (EditText) findViewById(R.id.editText12);
        button3 = (Button) findViewById(R.id.button3);
        button = (Button) findViewById(R.id.button);

        cd = new connectionDetector(this);

        if (cd.icConnected()) {
            button3.setOnClickListener(this);
            spinner.setOnItemSelectedListener(this);
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, quest);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(aa);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }else
            { Toast.makeText(forgetpassworedAdmin.this,"Network connection problems",Toast.LENGTH_SHORT).show();}
    }

//==============================================================================================================

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        if(position==0)
            que=0;

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Toast.makeText(forgetpassworedAdmin.this, "اختر واحد من الأسئلة ", Toast.LENGTH_LONG).show();

    }

        public void check(View view) {
            UserName = editText10.getText().toString();
            ans= editText12.getText().toString();

            if(UserName.isEmpty()) {
                editText10.setError("ادخل اسم الستخدم ");
                Toast.makeText(forgetpassworedAdmin.this, " فشل التحقق من المستخدم ", Toast.LENGTH_LONG).show();
            } else if(ans.isEmpty()) {
                editText12.setError("ادخل الإجابة ");
                Toast.makeText(forgetpassworedAdmin.this, " فشلت التحقق من الإجابة ", Toast.LENGTH_LONG).show();
            } else{
            if (que == 0) {
                HashMap postData = new HashMap();

                postData.put("editText10", UserName);
                postData.put("editText12", ans);


                PostResponseAsyncTask task1 = new PostResponseAsyncTask(forgetpassworedAdmin.this, postData,
                        new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                Log.d(TAG, s);
                                if (s.contains("success")) {
                                    flag = true;
                                    Toast.makeText(forgetpassworedAdmin.this, "تم التحقق بنجاح ", Toast.LENGTH_LONG).show();


                                } else {
                                    Toast.makeText(forgetpassworedAdmin.this, " خطأ في اسم المستخدم أو اجابتك اعد الكرة", Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                task1.execute("http://zwarh.net/zwarhapp/Alyah/friend.php");

            }}
        }
    @Override
    //==============================================================================================================
        public void onClick(View v) {

        newPass = editText11.getText().toString();


        if(flag){

            if(newPass.isEmpty()) {
                editText11.setError("ادخل كلمة مرور صحيحة لا تتجاوز 30 رمز ");
                Toast.makeText(forgetpassworedAdmin.this, " فشلت العملية  ", Toast.LENGTH_LONG).show();
            } else {

                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);

                nameValuePair.add(new BasicNameValuePair("psw", newPass));
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Alyah/updateAdminPass.php?UserName=" + UserName);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //==============================================================================================================
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    result = sb.toString();
                    //test the query
                    if (result.contains("true")) {
                        System.out.println("************************ result " + result + "**********************************************");
                        String msg = "تم تغيير كلمة المرور";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                        Intent in = new Intent(forgetpassworedAdmin.this, LoginActivity.class);
                        startActivity(in);
                    } else {
                        String msg = "لم يتم تغيير كلمة المرور ";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Log.e("Fail 2", e.toString());
                }


            }

        }else{
            String msg = "تحقق من اجابتك اولا ";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

    }}

    public void Back(View view){
        Intent in = new Intent(forgetpassworedAdmin.this, LoginActivity.class);
        startActivity(in);
    }
}
