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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.myapplication.Activity.MyFriendList;
import com.example.acer.myapplication.Retrofit.APIService;
import com.example.acer.myapplication.Retrofit.ApiUtils;
import com.example.acer.myapplication.Retrofit.Invitation;
import com.example.acer.myapplication.Retrofit.InvitationResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

//import com.example.acer.myapplication.pref.UserInfo;

public class invitatonssaved extends AppCompatActivity {

    private APIService mAPIService;
    private String TAG = MyFriendList.class.getSimpleName();
    SharedPreferences pref;
    public InputStream is ;
    String line = null;
    String result = null;


    public void openprofile(View v){
        Intent intent = new Intent(invitatonssaved.this,profileuser.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitatonssaved);
        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE); // 2 creat file

        Log.d(TAG, pref.getString("UserName", ""));// 3
        Log.d(TAG, pref.getString("PassWord", ""));// 4
        String username =pref.getString("UserName", ""); // 5 after intilisatien

        //return the saved invitations
        mAPIService = ApiUtils.getAPIService();
        mAPIService.getInvitations(username).enqueue(new Callback<InvitationResponse>() {
            @Override
            public void onResponse(Call<InvitationResponse> call, retrofit2.Response<InvitationResponse> response) {
                Log.d(TAG, "post response");
                Log.d(TAG, response.code() + "");
                if (response.isSuccessful()) {
                    Log.d(TAG,response.body()+"");

                    try{
                        if(response.body().getInvitations().size() == 0) {
                            Toast.makeText(invitatonssaved.this,"لا يوجد أي دعوات.",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //display in the adapter
                            List<Invitation> invitations = response.body().getInvitations();
                            MyCustomAdapter myCustomAdapter=new MyCustomAdapter(invitations);
                            final ListView listViewFriendList = (ListView) findViewById(R.id.list);
                            listViewFriendList.setAdapter(myCustomAdapter);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<InvitationResponse> call, Throwable t) {

                Log.e(TAG, "Unable to submit post to API. "+t.getMessage());
            }
        });
    }
    public void deleteAllSaved(View view)
    {
        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE); // 2 creat file

        Log.d(TAG, pref.getString("UserName", ""));// 3
        Log.d(TAG, pref.getString("PassWord", ""));// 4
        String UserName = pref.getString("UserName", "");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try
        {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwara/DeleteSavedInv.php?UserName="+UserName);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();


            is = entity.getContent();


        }catch (Exception e)
        {
            System.out.print("exception 1 caught");
        }
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);

            StringBuilder sb = new StringBuilder();
            while ((line=reader.readLine())!=null)
                sb.append(line);

            result=sb.toString();
            if(result.contains("yes"))
            {
                Toast.makeText(invitatonssaved.this,"تم حذف الدعوات المحفوظة بنجاح",Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(invitatonssaved.this,"لم يتم حذف الدعوات المحفوظة, الرجاء حاول مرة أخرى",Toast.LENGTH_SHORT).show();

        }  catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  void Back (View view)
    {
        finish();
    }

    //if i want ot send it
    public  void frindslist (View view)
    {
        Intent intent = new Intent(invitatonssaved.this,friendslist.class);
        startActivity(intent);
    }

    class MyCustomAdapter extends BaseAdapter
    {

        List<Invitation> Item= new ArrayList<Invitation>();

        MyCustomAdapter(List<Invitation> Item)
        {
            this.Item=Item;
        }

        @Override
        public int getCount() {
            return Item.size();
        }

        @Override
        public Object getItem(int position) {
            return Item.get(position).getInvitationTopic();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View view=inflater.inflate(R.layout.item_invitation_saved,null);

            TextView txtname= (TextView) view.findViewById(R.id.textView);
            txtname.setText(Item.get(position).getInvitationTopic());


            ImageButton friends = (ImageButton) view.findViewById(R.id.imageButton12);
            friends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if i want to send
                    String ID = Item.get(position).getID();
                    Intent intent = new Intent(invitatonssaved.this,friendslist.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("path","saved");
                    bundle.putString("ID",ID);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            ImageButton save = (ImageButton) view.findViewById(R.id.imageButton8);
            //if i want to edit the invitation
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ID = Item.get(position).getID();
                    Intent intent = new Intent(invitatonssaved.this,invititioncretion.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("ID",ID);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            ImageButton delete = (ImageButton) view.findViewById(R.id.imageButton7);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(invitatonssaved.this);
                    //alertDialog.setCancelable(false);
                    //TITLE, message, the "yes" button and the "No" button
                    alertDialog.setTitle("حذف الدعوة")

                            .setMessage("هل تريد حذف الدعوة؟")

                            .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {

                    //if i want to delete an invitation
                    String ID = Item.get(position).getID();


                    mAPIService = ApiUtils.getAPIService();
                    mAPIService.deleteInvitation(ID).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                            Log.d(TAG, "post response");
                            Log.d(TAG, response.code() + "");
                            if (response.isSuccessful()) {
                                Log.d(TAG,response.body()+"");

                                if(response.body().equals("no")) {
                                    Toast.makeText(invitatonssaved.this,"Invitation not deleted.",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(invitatonssaved.this,"Invitation deleted.",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(invitatonssaved.this,invitatonssaved.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            Log.e(TAG, "Unable to submit post to API. "+t.getMessage());
                        }
                    });

                    //
                }
            })

                    .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //dialog.cancel();
                        }
                    }).show();


                }
            });

            return view;
        }
    }
}
