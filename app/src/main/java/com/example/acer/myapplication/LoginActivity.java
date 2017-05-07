
package com.example.acer.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    final String TAG = this.getClass().getName();


    Button btnLogin;
    EditText etUsername, etPassword;
    CheckBox cbRemember;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    RadioGroup rg;
    RadioButton rb;
    String type;

    boolean checkFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        rg = (RadioGroup) findViewById(R.id.radioGroup);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        cbRemember =(CheckBox)findViewById(R.id.checkBox);

        cbRemember.setOnCheckedChangeListener(this);
        checkFlag = cbRemember.isChecked();
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

        if(username.contains("Admin")) {
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

        }
        else{
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
    }


    @Override
    public void onClick(View v) {



        int radId = rg.getCheckedRadioButtonId();
        rb=(RadioButton) findViewById(radId);
        type= (String) rb.getText();

        if(type.contains("User")){
            HashMap postData = new HashMap();

            postData.put("txtUsername", etUsername.getText().toString());
            postData.put("txtPassword",  etPassword.getText().toString());


            PostResponseAsyncTask task1 = new PostResponseAsyncTask(LoginActivity.this, postData,
                    new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            Log.d(TAG, s);
                            if(s.contains("success")){

                                if(checkFlag) {
                                    editor.putString("UserName", etUsername.getText().toString());
                                    editor.putString("PassWord", etPassword.getText().toString());
                                    editor.apply();

                                    Log.d(TAG, pref.getString("PassWord", ""));
                                }

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

        }
        else{
            HashMap postData = new HashMap();

            postData.put("txtUsername", etUsername.getText().toString());
            postData.put("txtPassword", etPassword.getText().toString());


            PostResponseAsyncTask task1 = new PostResponseAsyncTask(LoginActivity.this, postData,
                    new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            Log.d(TAG, s);
                            if(s.contains("success")){

                                if(checkFlag) {
                                    editor.putString("UserName", etUsername.getText().toString());
                                    editor.putString("PassWord", etPassword.getText().toString());
                                    editor.apply();

                                    Log.d(TAG, pref.getString("PassWord", ""));
                                }

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


        }
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkFlag = isChecked;
        Log.d(TAG, "checkFlag: " + checkFlag);
    }


   public void forgetPass(View view){
       Intent in = new Intent(LoginActivity.this, forgetpasswored.class);
       startActivity(in);
    }

    public void newUser(View view){
        Intent in = new Intent(LoginActivity.this, registerUser.class);
        startActivity(in);
    }

}
