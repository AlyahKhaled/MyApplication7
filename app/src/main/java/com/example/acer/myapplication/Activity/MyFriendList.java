package com.example.acer.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.acer.myapplication.Adapter.FriendListAdapter;
import com.example.acer.myapplication.R;
import com.example.acer.myapplication.Retrofit.APIService;
import com.example.acer.myapplication.Retrofit.ApiUtils;
import com.example.acer.myapplication.Retrofit.Friend;
import com.example.acer.myapplication.Retrofit.FriendListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

//import com.example.dalal.gpnew.pref.UserInfo;

public class MyFriendList extends AppCompatActivity implements View.OnClickListener {
    ListView listViewFriendList;
    ImageView ivAddFriend;
    ImageButton ivBackPress,imageButton2;

    private APIService mAPIService;
    private String TAG = MyFriendList.class.getSimpleName();

    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend_list);
        setViews();
        ivAddFriend.setOnClickListener(this);
        ivBackPress.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        listViewFriendList = (ListView) findViewById(R.id.listViewFriendList);

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE); // 2 creat file

        Log.d(TAG, pref.getString("UserName", ""));// 3
        Log.d(TAG, pref.getString("PassWord", ""));// 4

        String username=pref.getString("UserName", ""); // 5 after intilisatien

        Log.d(TAG,username);


        mAPIService = ApiUtils.getAPIService();
        mAPIService.getFriendList(username).enqueue(new Callback<FriendListResponse>() {
            @Override
            public void onResponse(Call<FriendListResponse> call, retrofit2.Response<FriendListResponse> response) {
                Log.d(TAG, "post response");
                Log.d(TAG, response.code() + "");
                if (response.isSuccessful()) {
                    Log.d(TAG,response.body().toString()+"");

                    try {
                        if (response.body().getFriends().size() == 0) {
                            Toast.makeText(MyFriendList.this, "لا يوجد أصدقاء.", Toast.LENGTH_SHORT).show();
                        } else {
                            List<Friend> friends = response.body().getFriends();
                            FriendListAdapter friendListAdapter = new FriendListAdapter(MyFriendList.this, R.layout.item_row_list_friend, friends);
                            listViewFriendList.setAdapter(friendListAdapter);
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<FriendListResponse> call, Throwable t) {

                Log.e(TAG, "Unable to submit post to API. "+t.getMessage());
            }
        });

    }

    private void setViews() {
        ivAddFriend = (ImageView) findViewById(R.id.imageViewAddFriend);
        ivBackPress= (ImageButton) findViewById(R.id.imageButton2);
        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewAddFriend:
                Intent intent=new Intent(MyFriendList.this,AddNewFriend.class);
                startActivity(intent);

                break;
            case R.id.imageButton2:
                finish();
                break;

        }
    }
}
