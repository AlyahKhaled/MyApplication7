package com.example.acer.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.myapplication.Activity.MyFriendList;
import com.example.acer.myapplication.MyUtil;
import com.example.acer.myapplication.R;
import com.example.acer.myapplication.Retrofit.APIService;
import com.example.acer.myapplication.Retrofit.ApiUtils;
import com.example.acer.myapplication.Retrofit.Friend;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

//import com.example.acer.myapplication.pref.UserInfo;


public class FriendListAdapter extends ArrayAdapter<Friend> {

    private Activity context;
    private List<Friend> data;
    private int resId;
    SharedPreferences pref;

    private APIService mAPIService;
    private String TAG = FriendListAdapter.class.getSimpleName();

    public FriendListAdapter(Context context, int resource, List<Friend> objects) {
        super(context, resource, objects);
        this.context = (Activity) context;
        this.resId = resource;
        this.data = objects;


        pref = context.getSharedPreferences("login.conf", Context.MODE_PRIVATE); // 2 creat file

//        Log.d(TAG, pref.getString("UserName", ""));// 3
//        Log.d(TAG, pref.getString("PassWord", ""));// 4

    }

    @Override
    public View getView(final int position, View v, final ViewGroup parent) {

        final Holder h;
        if (v == null) {
            h = new Holder();
            LayoutInflater inflater = context.getLayoutInflater();
            v = inflater.inflate(resId, parent, false);
            h.name = (TextView) v.findViewById(R.id.textViewName);
            h.palce = (TextView) v.findViewById(R.id.textViewPlace);
            h.img = (ImageView) v.findViewById(R.id.imageViewdelete);
            v.setTag(h);
        } else {
            h = (Holder) v.getTag();
        }
        h.name.setText(data.get(position).getName());

        h.palce.setText(data.get(position).getUserName());
        h.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = pref.getString("UserName", ""); // 5 after intilisatien
                final String friendname = data.get(position).getUserName();

                if (!MyUtil.isNetworkAvailable(getContext())) {
                    Toast.makeText(context, " خطأ فى الوصول الى الشبكة ", Toast.LENGTH_LONG).show();
                    return;
                }

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                mAPIService = ApiUtils.getAPIService();
                                mAPIService.deleteFriend(username, friendname).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                        Log.d(TAG, "post response");
                                        Log.d(TAG, response.code() + "");
                                        if (response.isSuccessful()) {
                                            Log.d(TAG, response.body() + "");

                                            if (response.body().equals("no")) {
                                                Toast.makeText(context, "لم يتم حذف الصديق.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "تمت عملية الحذف بنجاح.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(context, MyFriendList.class);
                                                context.startActivity(intent);
                                                context.finish();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {

                                        Log.e(TAG, "Unable to submit post to API. " + t.getMessage());
                                    }
                                });


                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("برجاء تأكيد حذف الصديق؟").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }


            });

        return v;
        }


        private class Holder {

            TextView name, palce;
            ImageView img;

        }

    }

