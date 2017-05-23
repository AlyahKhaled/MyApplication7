package com.example.acer.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.myapplication.Activity.MyFriendList;
import com.example.acer.myapplication.Retrofit.APIService;
import com.example.acer.myapplication.Retrofit.ApiUtils;
import com.example.acer.myapplication.Retrofit.Friend;
import com.example.acer.myapplication.Retrofit.FriendListResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

//import com.example.acer.myapplication.pref.UserInfo;

public class friendslist extends AppCompatActivity implements View.OnClickListener {
    TextView tvBackpress;



    private APIService mAPIService;
    private String TAG = MyFriendList.class.getSimpleName();
    Map<Integer,String> friends = new HashMap<Integer,String>();
    SharedPreferences pref;
    Button button22;
    public boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendslist);
        setViews();
        tvBackpress.setOnClickListener(this);
        button22.setOnClickListener(this);

        if (!MyUtil.isNetworkAvailable(getApplicationContext())) {
            Toast.makeText(friendslist.this, " خطأ فى الوصول الى الشبكة ", Toast.LENGTH_LONG).show();
            return;
        }

        String username=pref.getString("UserName", ""); // 5 after intilisatien

        //retrive the friends
        mAPIService = ApiUtils.getAPIService();
        mAPIService.getFriendList(username).enqueue(new Callback<FriendListResponse>() {
            @Override
            public void onResponse(Call<FriendListResponse> call, retrofit2.Response<FriendListResponse> response) {
                Log.d(TAG, "post response");
                Log.d(TAG, response.code() + "");
                if (response.isSuccessful()) {
                    Log.d(TAG,response.body()+"");
                    if (response.body() != null) {
                        try{
                        if (response.body().getFriends().size() == 0) {
                            Toast.makeText(friendslist.this, "لم يتم العثور على أي أصدقاء.", Toast.LENGTH_SHORT).show();
                        } else {
                            List<Friend> friends = response.body().getFriends();
                            for (Friend f :
                                    friends) {
                                Log.i("AppInfo", "Friend: " + f.getUserName());
                            }

                            MyCustomAdapter myCustomAdapter = new MyCustomAdapter(friends);
                            final ListView listViewFriendList = (ListView) findViewById(R.id.list);
                            listViewFriendList.setAdapter(myCustomAdapter);
                            flag = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                        if (flag) {
                            Toast.makeText(friendslist.this, "لا يوجد أي أصدقاء.", Toast.LENGTH_SHORT).show();
                            ((TextView) findViewById(R.id.textView20)).setVisibility(View.VISIBLE);
                        }
                    }else{
                        Toast.makeText(friendslist.this, "لم يتم العثور على أي أصدقاء.", Toast.LENGTH_SHORT).show();
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
        tvBackpress= (TextView) findViewById(R.id.textView27);
        button22 = (Button) findViewById(R.id.button22);
        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE); // 2 creat file

        Log.d(TAG, pref.getString("UserName", ""));// 3
        Log.d(TAG, pref.getString("PassWord", ""));// 4
    }

    public void openprofile(View v){
        Intent intent = new Intent(friendslist.this,profileuser.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView27:
                finish();
                break;
            case R.id.button22:

                //if it is a new invitation
                if(getIntent().getStringExtra("path").equals("new")){

                    //put all the guests in one string to send it one time.(splitted with (,))
                    String guests = "";
                    try {

                        for (Integer key : friends.keySet()) {
                            guests += friends.get(key) + ",";
                        }
                        guests = guests.substring(0, guests.length() - 1);

                        Log.d(TAG, guests);
                    }
                    catch(Exception e){}

                    //get the info that came with the intent
                    Double latitude = getIntent().getDoubleExtra("latitude",0);
                    Double longitude = getIntent().getDoubleExtra("longitude",0);
                    String InvitationName = getIntent().getStringExtra("InvitationName");
                    String AdditionalInfo = getIntent().getStringExtra("AdditionalInfo");
                    String DateTxt = getIntent().getStringExtra("DateTxt");
                    String TimeTxt = getIntent().getStringExtra("TimeTxt");
                    String PlaceName = getIntent().getStringExtra("PlaceName");
                    String InviterName = getIntent().getStringExtra("InviterName");
                    if (!guests.equals("")) {
                        //ad an invitation
                        mAPIService = ApiUtils.getAPIService();
                        mAPIService.addInvitation(PlaceName, latitude + "", longitude + "", DateTxt, TimeTxt, AdditionalInfo, InvitationName, InviterName, guests).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                Log.d(TAG, "post response");
                                Log.d(TAG, response.code() + "");
                                if (response.isSuccessful()) {
                                    Log.d(TAG, response.body() + "");

                                    if (response.body().equals("no")) {
                                        Toast.makeText(friendslist.this, "لم يتم إرسال الرسالة, لا يوجد لديك أي أصدقاء.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(friendslist.this, "تم ارسال الدعوة.", Toast.LENGTH_SHORT).show();
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
                    }else{
                        Toast.makeText(friendslist.this, "لم يتم إختيار أصدقاء.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    //else if it was already saved and need only to insert guests
                    String ID = getIntent().getStringExtra("ID");

                    String guests = "";
                    try {

                        for (Integer key : friends.keySet()) {
                            guests += friends.get(key) + ",";
                        }
                        guests = guests.substring(0, guests.length() - 1);

                        Log.d(TAG, guests);
                    }
                    catch(Exception e){}
                    if (!guests.equals("")) {
                        mAPIService = ApiUtils.getAPIService();
                        mAPIService.addOnlyInvitation(ID, guests).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                Log.d(TAG, "post response");
                                Log.d(TAG, response.code() + "");
                                if (response.isSuccessful()) {
                                    Log.d(TAG, response.body() + "");

                                    if (response.body().equals("no")) {
                                        Toast.makeText(friendslist.this, "لم يتم إرسال الدعوة, لا يوجد لديك أصدقاء.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(friendslist.this, "تم إرسال الدعوة.", Toast.LENGTH_SHORT).show();
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
                    }else{
                        Toast.makeText(friendslist.this, "لم يتم إختيار أصدقاء.", Toast.LENGTH_SHORT).show();
                    }

                }


                break;

        }

    }


    class MyCustomAdapter extends BaseAdapter
    {

        List<Friend> Item= new ArrayList<Friend>();

        MyCustomAdapter(List<Friend> Item)
        {
            this.Item=Item;
        }

        @Override
        public int getCount() {
            return Item.size();
        }

        @Override
        public Object getItem(int position) {
            return Item.get(position).getUserName();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View view=inflater.inflate(R.layout.rowfrnd_view,null);

            TextView txtname= (TextView) view.findViewById(R.id.textView47);
            CheckBox CheckBox= (CheckBox) view.findViewById(R.id.checkBox2);
            txtname.setText(Item.get(position).getUserName());

            CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        friends.put(position,Item.get(position).getUserName());
                    }
                    else{
                        friends.remove(position);
                    }
                }
            });

            return view;
        }
    }
}
