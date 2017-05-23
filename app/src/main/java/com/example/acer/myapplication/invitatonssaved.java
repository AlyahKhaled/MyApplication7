package com.example.acer.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.example.acer.myapplication.pref.UserInfo;

public class invitatonssaved extends AppCompatActivity {

    private APIService mAPIService;
    private String TAG = MyFriendList.class.getSimpleName();
    SharedPreferences pref;
    ListView listViewFriendList;
    private MyCustomAdapter myCustomAdapter;
    List<Invitation> invitations;
    View view2;

    public void openprofile(View v) {
        Intent intent = new Intent(invitatonssaved.this, profileuser.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitatonssaved);
        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE); // 2 creat file

        Log.d(TAG, pref.getString("UserName", ""));// 3
        Log.d(TAG, pref.getString("PassWord", ""));// 4
        String username = pref.getString("UserName", ""); // 5 after intilisatien

        //return the saved invitations
        mAPIService = ApiUtils.getAPIService();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String username = pref.getString("UserName", "");
        mAPIService.getInvitations(username).enqueue(new Callback<InvitationResponse>() {
            @Override
            public void onResponse(Call<InvitationResponse> call, Response<InvitationResponse> response) {
                Log.d(TAG, "post response");
                Log.d(TAG, response.code() + "");
                if (response.isSuccessful()) {
                    Log.d(TAG, response.body() + "");

                    try {
                        if (response.body().getInvitations().size() == 0) {
                            Toast.makeText(invitatonssaved.this, "لا يوجد أي دعوات.", Toast.LENGTH_SHORT).show();
                        } else {
                            //display in the adapter
                            invitations = response.body().getInvitations();
                            myCustomAdapter = new MyCustomAdapter(invitations);
                            listViewFriendList = (ListView) findViewById(R.id.list);
                            listViewFriendList.setAdapter(myCustomAdapter);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<InvitationResponse> call, Throwable t) {

                Log.e(TAG, "Unable to submit post to API. " + t.getMessage());
            }
        });
    }

    public void Back(View view) {
        finish();
    }

    //if i want ot send it
    public void frindslist(View view) {
        Intent intent = new Intent(invitatonssaved.this, friendslist.class);
        startActivity(intent);
    }

    public void deleteAllInv(View view) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //return the saved invitations
                        mAPIService = ApiUtils.getAPIService();
                        String username = pref.getString("UserName", "");
                        mAPIService.deleteInvitations(username).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                Log.i("AppInfo", response.body().toString());
                                if (response.isSuccessful() && invitations!= null && !invitations.isEmpty()) {
                                    if (response.body().equals("yes")) {
                                        Toast.makeText(invitatonssaved.this, "تم حذف الدعوات.", Toast.LENGTH_SHORT).show();
                                        invitations.clear();
                                        myCustomAdapter.notifyDataSetChanged();

//                                    Intent intent = new Intent(getApplicationContext(), homeuser.class);
//                                    startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "لا توجد دعوات محفوظة", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), "لا توجد دعوات محفوظة", Toast.LENGTH_SHORT).show();;

                                }
                            }


                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.i("AppInfo", "Error on deleting invitations: " + t.toString());
                            }
                        });

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("برجاء تأكيد الحذف?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }

    class MyCustomAdapter extends BaseAdapter {

        List<Invitation> Item = new ArrayList<Invitation>();

        MyCustomAdapter(List<Invitation> Item) {
            this.Item = Item;
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
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.item_invitation_saved, null);

            TextView txtname = (TextView) view.findViewById(R.id.textView);
            txtname.setText(getInvitationName(Item.get(position).getInvitationTopic()));

            view2 = inflater.inflate(R.layout.empty_list, null);
            TextView textView2 = (TextView) view2.findViewById(R.id.textView4);
            textView2.setText(" لا توجد دعوات محفوظة :(");




            ImageButton friends = (ImageButton) view.findViewById(R.id.imageButton12);
            friends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if i want to send
                    String ID = Item.get(position).getID();
                    Intent intent = new Intent(invitatonssaved.this, friendslist.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("path", "saved");
                    bundle.putString("ID", ID);
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
                    Intent intent = new Intent(invitatonssaved.this, invititioncretion.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", ID);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            ImageButton delete = (ImageButton) view.findViewById(R.id.imageButton7);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if i want to delete an invitation
                    String ID = Item.get(position).getID();

                    mAPIService = ApiUtils.getAPIService();
                    mAPIService.deleteInvitation(ID).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d(TAG, "post response");
                            Log.d(TAG, response.code() + "");
                            if (response.isSuccessful()) {
                                Log.d(TAG, response.body() + "");

                                if (response.body().equals("no")) {
                                    Toast.makeText(invitatonssaved.this, "Invitation not deleted.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(invitatonssaved.this, "Invitation deleted.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(invitatonssaved.this, invitatonssaved.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            Log.e(TAG, "Unable to submit post to API. " + t.getMessage());
                        }
                    });
                }
            });

            return view;
        }
    }


    private String getInvitationName(String englishName) {

        String invitationName = "";
        switch (englishName) {
            case "Celebration party":
                invitationName = "حفلة معايدة";
                break;
            case "Friends gathering":
                invitationName = "اجتماع أصدقاء";
                break;
            case "Job meeting" :
                invitationName = "اجتماع عمل";
                break;
            case "Children party" :
                invitationName = "حفلة أطفال";
                break;
            case "Graduation party" :
                invitationName = "حفلة نجاح";
                break;
        }
        return invitationName;
    }
}
