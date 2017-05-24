
package com.example.acer.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kosalgeek.android.md5simply.MD5;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = this.getClass().getName();


    Button btnLogin;
    EditText etUsername, etPassword;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    RadioGroup rg;
    RadioButton rb;
    String type,Na , Pa;
    boolean valid=true;
    connectionDetector cd ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        rg = (RadioGroup) findViewById(R.id.radioGroup);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        cd = new connectionDetector(this);


        if (cd.icConnected()) {
            btnLogin.setOnClickListener(this);
            pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.clear();
            editor.commit();

            String username = pref.getString("UserName", "");
            String password = pref.getString("PassWord", "");
            Log.d(TAG, pref.getString("PassWord", ""));

            HashMap postData = new HashMap();

            postData.put("txtUsername", username);
            postData.put("txtPassword", password);

            if (username.contains("Admin")) {
                if (!(username.equals("") && password.equals(""))) {
                    PostResponseAsyncTask task1 = new PostResponseAsyncTask(LoginActivity.this, postData,
                            new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {
                                    Log.d(TAG, s);
                                    if (s.contains("success")) {
                                        Toast.makeText(LoginActivity.this, "تم تسجيل دخولك بنجاح ", Toast.LENGTH_LONG).show();
                                        Intent in = new Intent(LoginActivity.this, admin.class);
                                        startActivity(in);
                                    } else {
                                        Toast.makeText(LoginActivity.this, " خطأ في كلمة المرور او اسم المستخدم ", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                    task1.execute("http://zwarh.net/zwarhapp/Alyah/loginAdmin.php");

                }

            } else {
                if (!(username.equals("") && password.equals(""))) {
                    PostResponseAsyncTask task1 = new PostResponseAsyncTask(LoginActivity.this, postData,
                            new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {
                                    Log.d(TAG, s);
                                    if (s.contains("success")) {
                                        Toast.makeText(LoginActivity.this, "تم تسجيل دخولك بنجاح", Toast.LENGTH_LONG).show();
                                        Intent in = new Intent(LoginActivity.this, homeuser.class);
                                        startActivity(in);
                                    } else {
                                        Toast.makeText(LoginActivity.this, " خطأ في كلمة المرور او اسم المستخدم", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                    task1.execute("http://zwarh.net/zwarhapp/Alyah/login.php");

                }
            }
        }else
        { Toast.makeText(LoginActivity.this,"Network connection problems",Toast.LENGTH_SHORT).show();}
    }

    @Override
    public void onClick(View v) {



        int radId = rg.getCheckedRadioButtonId();
        rb=(RadioButton) findViewById(radId);


        if (radId == -1)
        {
            Toast.makeText(LoginActivity.this, " اختر نوع الدخول الى زوارة ", Toast.LENGTH_LONG).show();

        }
        else
        {   type= (String) rb.getText();
            Na=etUsername.getText().toString();
            Pa= MD5.encrypt(etPassword.getText().toString());

            if(type.contains("User")){

                if(Na.isEmpty()){
                    etUsername.setError("ادخل اسم المستخدم ");
                    valid=false;
                    Toast.makeText(LoginActivity.this, " فشل تسجيل الدخول ", Toast.LENGTH_LONG).show();
                }else if(Pa.isEmpty()){
                    etPassword.setError("ادخل كلمة المرور ");
                    valid=false;
                    Toast.makeText(LoginActivity.this, " فشل تسجيل الدخول ", Toast.LENGTH_LONG).show();
                }

                if(valid){
                    HashMap postData = new HashMap();

                    postData.put("txtUsername", Na);
                    postData.put("txtPassword", Pa );


                    PostResponseAsyncTask task1 = new PostResponseAsyncTask(LoginActivity.this, postData,
                            new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {
                                    Log.d(TAG, s);
                                    if(s.contains("success")){


                                        editor.putString("UserName", Na);
                                        editor.putString("PassWord", Pa);
                                        editor.apply();

                                        Log.d(TAG, pref.getString("PassWord", ""));


                                        Toast.makeText(LoginActivity.this, "تم تسجيل دخولك بنجاح", Toast.LENGTH_LONG).show();
                                        Intent in = new Intent(LoginActivity.this, homeuser.class);
                                        startActivity(in);
                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this, " خطأ في كلمة المرور او اسم المستخدم", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                    task1.execute("http://zwarh.net/zwarhapp/Alyah/login.php");

                }}
            else if(type.contains("Admin")){


                if(Na.isEmpty()){
                    etUsername.setError("ادخل اسم المستخدم ");
                    valid=false;
                    Toast.makeText(LoginActivity.this, " فشل تسجيل الدخول ", Toast.LENGTH_LONG).show();
                }else if(Pa.isEmpty()){
                    etPassword.setError("ادخل كلمة المرور ");
                    valid=false;
                    Toast.makeText(LoginActivity.this, " فشل تسجيل الدخول ", Toast.LENGTH_LONG).show();
                }
                if(valid){

                    HashMap postData = new HashMap();

                    postData.put("txtUsername", Na);
                    postData.put("txtPassword", Pa);


                    PostResponseAsyncTask task1 = new PostResponseAsyncTask(LoginActivity.this, postData,
                            new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {
                                    Log.d(TAG, s);
                                    if(s.contains("success")){


                                        editor.putString("UserName", Na);
                                        editor.putString("PassWord", Pa);
                                        editor.apply();

                                        Log.d(TAG, pref.getString("PassWord", ""));


                                        Toast.makeText(LoginActivity.this, "تم تسجيل دخولك بنجاح", Toast.LENGTH_LONG).show();
                                        Intent in = new Intent(LoginActivity.this, admin.class);
                                        startActivity(in);
                                    }
                                    else{
                                        Toast.makeText(LoginActivity.this, " خطأ في كلمة المرور او اسم المستخدم", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                    task1.execute("http://zwarh.net/zwarhapp/Alyah/loginAdmin.php");


                }}
        }



    }





    public void forgetPass(View view){
        int radId = rg.getCheckedRadioButtonId();
        rb=(RadioButton) findViewById(radId);

        if (radId == -1)
        {
            Toast.makeText(LoginActivity.this, " اختر نوع الدخول الى زوارة لتغيير كلمة المرور ", Toast.LENGTH_LONG).show();

        }
        else {
            type= (String) rb.getText();
            if (type.contains("User")) {
                Intent in = new Intent(LoginActivity.this, forgetpasswored.class);
                startActivity(in);
            } else if (type.contains("Admin")) {
                Intent in = new Intent(LoginActivity.this, forgetpassworedAdmin.class);
                startActivity(in);
            }
        }
    }


    public void newUser(View view){
        int radId = rg.getCheckedRadioButtonId();
        rb=(RadioButton) findViewById(radId);

        if (radId == -1)
        {
            Toast.makeText(LoginActivity.this, " اختر نوع الدخول الى زوارة ", Toast.LENGTH_LONG).show();

        }else {
            type= (String) rb.getText();
            if(type.contains("User")){
                Intent in = new Intent(LoginActivity.this, registerUser.class);
                startActivity(in);}else if(type.contains("Admin")){
                Toast.makeText(LoginActivity.this, "يجب أن تكون مستخدم كي تدخل صفحة التسجيل ", Toast.LENGTH_LONG).show();}

        }}

}

