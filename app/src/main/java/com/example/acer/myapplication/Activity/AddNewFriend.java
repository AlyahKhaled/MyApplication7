package com.example.acer.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.myapplication.MyUtil;
import com.example.acer.myapplication.R;
import com.example.acer.myapplication.Retrofit.APIService;
import com.example.acer.myapplication.Retrofit.ApiUtils;
import com.example.acer.myapplication.Retrofit.Friend;
import com.example.acer.myapplication.Retrofit.FriendListResponse;
import com.example.acer.myapplication.invitationOptiens;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

//import com.example.dalal.gpnew.pref.UserInfo;

public class AddNewFriend extends AppCompatActivity implements View.OnClickListener {
    ImageView ivBackPress,imageView5;
    EditText editTextSearch;
    TextView searchButton;

    private APIService mAPIService;
    private String TAG = MyFriendList.class.getSimpleName();
    SharedPreferences pref;

    private String FriendUsername = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_friend);




        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);

        Log.d(TAG, pref.getString("UserName", ""));
        Log.d(TAG, pref.getString("PassWord", ""));

        ivBackPress= (ImageView) findViewById(R.id.imageViewBack);
        searchButton =  (TextView) findViewById(R.id.textView4);
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        ivBackPress.setOnClickListener(this);
        imageView5.setOnClickListener(this);

        editTextSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction()==KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    String name = editTextSearch.getText().toString();

                    if(name.length() == 0){
                        Toast.makeText(AddNewFriend.this, "اسم المستخدم لا يمكن أن يكون فارغ.", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        mAPIService = ApiUtils.getAPIService();
                        mAPIService.searchAFriend(name).enqueue(new Callback<FriendListResponse>() {
                            @Override
                            public void onResponse(Call<FriendListResponse> call, retrofit2.Response<FriendListResponse> response) {
                                Log.d(TAG, "post response");
                                Log.d(TAG, response.code() + "");
                                if (response.isSuccessful()) {
                                    Log.d(TAG,response.body()+"");

                                    if(response.body().getFriends().size() == 0) {
                                        Toast.makeText(AddNewFriend.this,"عذراً, لا يوجد لديك أي صديق.",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        FriendUsername = response.body().getFriends().get(0).getUserName();
                                        ((TextView)findViewById(R.id.textView5)).setText(response.body().getFriends().get(0).getUserName());
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<FriendListResponse> call, Throwable t) {

                                Log.e(TAG, "Unable to submit post to API. "+t.getMessage());
                            }
                        });

                    }
                }

                return false;
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextSearch.getText().toString();

                if(name.length() == 0){
                    Toast.makeText(AddNewFriend.this, "اسم المستخدم لا يمكن أن يكون فارغ.", Toast.LENGTH_SHORT).show();
                }
                else{

                    mAPIService = ApiUtils.getAPIService();
                    mAPIService.searchAFriend(name).enqueue(new Callback<FriendListResponse>() {
                        @Override
                        public void onResponse(Call<FriendListResponse> call, retrofit2.Response<FriendListResponse> response) {
                            Log.d(TAG, "post response");
                            Log.d(TAG, response.code() + "");
                            if (response.isSuccessful()) {
                                Log.d(TAG,response.body()+"");

                                if(response.body().getFriends().size() == 0) {
                                    Toast.makeText(AddNewFriend.this,"عذراً, لا يوجد لديك أي صديق.",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    FriendUsername = response.body().getFriends().get(0).getUserName();
                                    ((TextView)findViewById(R.id.textView5)).setText(response.body().getFriends().get(0).getUserName());
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<FriendListResponse> call, Throwable t) {

                            Log.e(TAG, "Unable to submit post to API. "+t.getMessage());
                        }
                    });

                }

            }
        });

    }

    @Override
    public void onClick(View v) {



        switch (v.getId()){
            case R.id.imageViewBack:
                finish();
                break;
            case R.id.imageView5:

                final String username= pref.getString("UserName", "");

                if (!MyUtil.isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(AddNewFriend.this, " خطأ فى الوصول الى الشبكة ", Toast.LENGTH_LONG).show();
                    return;
                }



                //retrive the friends
                mAPIService = ApiUtils.getAPIService();
                mAPIService.getFriendList(username).enqueue(new Callback<FriendListResponse>() {
                    @Override
                    public void onResponse(Call<FriendListResponse> call, retrofit2.Response<FriendListResponse> response) {
                        Log.d(TAG, "post response");
                        Log.d(TAG, response.code() + "");
                        if (response.isSuccessful()) {
                            Log.d(TAG,response.body()+"");
                            List list = response.body().getFriends();
                            if (response.body().getFriends() != null) {
                                if (response.body().getFriends().size() > 0) {
                                    Boolean exist = false;
                                    for (Friend f :
                                            response.body().getFriends()) {
                                        if (f.getUserName().equals(FriendUsername)){
                                            exist = true;
                                            break;
                                        }
                                    }
                                    if(exist == true) {
                                        Toast.makeText(AddNewFriend.this, "صديق بالفعل، لا يمكن الإضافة مرة أخرى.", Toast.LENGTH_SHORT).show();
                                    }else{
                                        if(FriendUsername.equals("none")) {
                                            Toast.makeText(AddNewFriend.this, "ابحث عن صديق أولاً لللإضافة.", Toast.LENGTH_SHORT).show();
                                        }
                                        else if(FriendUsername.equals(username)) {
                                            Toast.makeText(AddNewFriend.this, "لا يمكنك إضافة نفسك.", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            System.out.print("********************************************************************************************************************");
                                            System.out.print("thhis is the names"+ username+FriendUsername);
                                            mAPIService = ApiUtils.getAPIService();
                                            mAPIService.addFriend(username, FriendUsername).enqueue(new Callback<String>() {
                                                @Override
                                                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                                    Log.d(TAG, "post response");
                                                    Log.d(TAG, response.code() + "");
                                                    if (response.isSuccessful()) {
                                                        Log.d(TAG, response.body() + "");

                                                        if (response.body().equals("no")) {
                                                            Toast.makeText(AddNewFriend.this, "لا يوجد أصدقاء", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            editTextSearch.setText("");
                                                            ((TextView) findViewById(R.id.textView5)).setText("");
                                                            Toast.makeText(AddNewFriend.this, "تم عملية إرسال الإضافة بنجاح", Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(getApplicationContext(), invitationOptiens.class);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<String> call, Throwable t) {

                                                    Log.e(TAG, "Unable to submit post to API. " + t.getMessage());
                                                }
                                            });
                                        }
                                    }
                                }
                            }else{
                                Toast.makeText(AddNewFriend.this, "Sorry, There is no friends", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<FriendListResponse> call, Throwable t) {

                        Log.e(TAG, "Unable to submit post to API. "+t.getMessage());
                    }
                });





                break;

        }
    }
}
