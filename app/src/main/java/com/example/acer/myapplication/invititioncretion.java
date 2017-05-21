package com.example.acer.myapplication;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.myapplication.Activity.MyFriendList;
import com.example.acer.myapplication.Retrofit.APIService;
import com.example.acer.myapplication.Retrofit.ApiUtils;
import com.example.acer.myapplication.Retrofit.InvitationInfo;
import com.example.acer.myapplication.Utility.GPSTracker;

import retrofit2.Call;
import retrofit2.Callback;

//import com.example.dalal.gpnew.pref.UserInfo;


public class invititioncretion extends AppCompatActivity {

    //initialization
    public static double latitude=0;
    public static double longitude=0;
    public static EditText InvitationNametxt;
    public static EditText PlaceNametxt;
    public static EditText AdditionalInfotxt;
    public static TextView Time;
    public static TextView Date;
    public static String PlaceName;
    public static String InvitationName;
    public static String AdditionalInfo;
    public static String TimeInfo;
    public static String DateInfo;
    public static String DateTxt;
    public static String TimeTxt;
    String venueID= "1";
    public String id = "none";

    private APIService mAPIService;
    private String TAG = MyFriendList.class.getSimpleName();

    SharedPreferences pref;

    GPSTracker gpsTracker;

    public void openprofile(View v){
        Intent intent = new Intent(invititioncretion.this,profileuser.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invititioncretion);

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE); // 2 creat file

        Log.d(TAG, pref.getString("UserName", ""));// 3
        Log.d(TAG, pref.getString("PassWord", ""));// 4

        gpsTracker=new GPSTracker(this);

        InvitationNametxt= (EditText) findViewById(R.id.editText);
        PlaceNametxt= (EditText) findViewById(R.id.editText3);
        AdditionalInfotxt= (EditText) findViewById(R.id.editText4);
        Time= (TextView) findViewById(R.id.timeText);
        Date= (TextView) findViewById(R.id.Datetext);

        if(isNetworkAvailable())
        {
        //code for update the invitation
        if(getIntent().hasExtra("ID")){
            id = getIntent().getStringExtra("ID");

            mAPIService = ApiUtils.getAPIService();
            mAPIService.getInvitationInfo(id).enqueue(new Callback<InvitationInfo>() {
                @Override
                public void onResponse(Call<InvitationInfo> call, retrofit2.Response<InvitationInfo> response) {
                    Log.d(TAG, "post response");
                    Log.d(TAG, response.code() + "");
                    if (response.isSuccessful()) {
                        Log.d(TAG,response.body()+"");

                        InvitationNametxt.setText(response.body().getInvitationTopic());
                        PlaceNametxt.setText(response.body().getPlaceName());
                        AdditionalInfotxt.setText(response.body().getAdditionalInformation());
                        Time.setText(response.body().getTime());
                        Date.setText(response.body().getDate());

                        latitude = Double.parseDouble(response.body().getLatitude());
                        longitude = Double.parseDouble(response.body().getLongitude());
                    }
                }
                @Override
                public void onFailure(Call<InvitationInfo> call, Throwable t) {

                    Log.e(TAG, "Unable to submit post to API. "+t.getMessage());
                }
            });

            //hide the button with no need
            ((Button) findViewById(R.id.button3)).setVisibility(View.GONE);
            ((Button) findViewById(R.id.button14)).setVisibility(View.GONE);



            //if click to update
            ((Button) findViewById(R.id.button4)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int result= GetInfo();
                    if(result==0)
                    {

                        String username=pref.getString("UserName", ""); // 5 after intilisatien

                        Log.d(TAG,DateTxt);
                        Log.d(TAG,TimeTxt);

                        mAPIService = ApiUtils.getAPIService();
                        mAPIService.updateInvitation(PlaceName,latitude+"",longitude+"",DateTxt,TimeTxt,AdditionalInfo,InvitationName,username,venueID,id).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                Log.d(TAG, "post response");
                                Log.d(TAG, response.code() + "");
                                if (response.isSuccessful()) {
                                    Log.d(TAG,response.body()+"");

                                    if(response.body().equals("no")) {
                                        Toast.makeText(invititioncretion.this,"لم يتم تحديث الدعوة, الرجاء المحاولة مرة أخرى.",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(invititioncretion.this,"تم تحديث الدعوة بنجاح.",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                                Log.e(TAG, "Unable to submit post to API. "+t.getMessage());
                            }
                        });

                    }


                }
            });
        }
        }
        else
            Toast.makeText(this, "أنت غير متصل بالشبكة, الرجاء الاتصال بالشبكة وإعادة المحاولة", Toast.LENGTH_LONG).show();

    }


    //credit to http://stackoverflow.com/questions/5312334/how-to-handle-back-button-in-activity
    // A method to be called when the user click on the Device back button,
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            event.startTracking();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // A method to be called when the user leave the Back button after being pressed
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking() && !event.isCanceled())
        {
            Leave();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    // A method called when the user click on the cancel or return button
    public  void Back (View view)
    {
        Leave();
    }

    // A method to return to previous page after asking the user if he/she sure or not
    public void Leave()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(invititioncretion.this);
        //alertDialog.setCancelable(false);
        //TITLE, message, the "yes" button and the "No" button
        alertDialog.setTitle("مغادرة الصفحة!")

                .setMessage("هل تريد مغادرة الصفحة؟")

                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })

                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.cancel();
                    }
                }).show();
    }


    //a method that get the data
    public int GetInfo()
    {

        if(latitude == 0 && longitude ==0)
        {
            Toast.makeText(getApplicationContext(),
                    "يجب اختيار المكان!", Toast.LENGTH_SHORT)
                    .show();
            return 1;
        }
        InvitationName=InvitationNametxt.getText().toString();
        if(InvitationName.equals(""))
        {
            Toast.makeText(getApplicationContext(),
                    "يجب اختيار اسم الدعوة!", Toast.LENGTH_SHORT)
                    .show();
            return 1;
        }
        else
        {
            if(InvitationName.length()>20)
            {
                Toast.makeText(getApplicationContext(),
                        "يجب ان يكون طول اسم الدعوة أقل من أو يساوي 20 حرفاً !", Toast.LENGTH_SHORT)
                        .show();
                return 1;
            }
        }

        PlaceName=PlaceNametxt.getText().toString();
        if(PlaceName.equals(""))
        {
            Toast.makeText(getApplicationContext(),
                    "يجب اختيار اسم المكان!", Toast.LENGTH_SHORT)
                    .show();
            return 1;
        }
        else
        {
            if(PlaceName.length()>15)
            {
                Toast.makeText(getApplicationContext(),
                        "يجب ان يكون طول اسم المكان أقل من أو يساوي 15 حرفاً !", Toast.LENGTH_SHORT)
                        .show();
                return 1;
            }
        }

        TimeInfo=Time.getText().toString();
        int TxtPosition= TimeInfo.indexOf(":");
        TimeTxt=TimeInfo.substring(TxtPosition+1);
        if(TimeTxt.equals(""))
        {
            Toast.makeText(getApplicationContext(),
                    "يجب اختيار الوقت!", Toast.LENGTH_SHORT)
                    .show();
            return 1;
        }
        DateInfo=Date.getText().toString();
        int TxtPosition1= DateInfo.indexOf(":");
        DateTxt=DateInfo.substring(TxtPosition1+1);
        if(DateTxt.equals(""))
        {
            Toast.makeText(getApplicationContext(),
                    "يجب اختيار التاريخ!", Toast.LENGTH_SHORT)
                    .show();
            return 1;
        }
        AdditionalInfo=AdditionalInfotxt.getText().toString();
        if(AdditionalInfo.equals(""))
        {

            AdditionalInfo="لا يوجد معلومات إضافية.";

        }
        else
        {
            if(AdditionalInfo.length()>200)
            {
                Toast.makeText(getApplicationContext(),
                        "يجب ان يكون طول اسم المعلومات الإضافية أقل من أو يساوي 200 حرفاً !", Toast.LENGTH_SHORT)
                        .show();
                return 1;
            }
        }


        return 0;
    }

    // A method that go to friend list page and send the modt important information with the intent
    public  void list (View view)
    {
        if(isNetworkAvailable())
        {
        int result= GetInfo();
        if(result==0) {
            String username=pref.getString("UserName", ""); // 5 after intilisatien
            Intent intent = new Intent(invititioncretion.this, friendslist.class);
            Bundle bundle=new Bundle();
            bundle.putDouble("latitude" , latitude);
            bundle.putDouble("longitude", longitude);
            bundle.putString("InvitationName",InvitationName);
            bundle.putString("AdditionalInfo",AdditionalInfo);
            bundle.putString("DateTxt",DateTxt);
            bundle.putString("TimeTxt",TimeTxt);
            bundle.putString("PlaceName",PlaceName);
            bundle.putString("InviterName",username);
            //define that it is a new invitation
            bundle.putString("path","new");
            intent.putExtras(bundle);
            startActivity(intent);
        }

        }
        else
            Toast.makeText(this, "أنت غير متصل بالشبكة, الرجاء الاتصال بالشبكة وإعادة المحاولة", Toast.LENGTH_LONG).show();
    }

    //a method to get the information and save it in the data base
    public void SaveInvitation(View view)
    {



        if(isNetworkAvailable())
        {
            int result = GetInfo();
            if (result == 0) {

                String username=pref.getString("UserName", ""); // 5 after intilisatien

                Log.d(TAG, DateTxt);
                Log.d(TAG, TimeTxt);

                mAPIService = ApiUtils.getAPIService();
                mAPIService.SaveInvitation(PlaceName, latitude + "", longitude + "", DateTxt, TimeTxt, AdditionalInfo, InvitationName, username).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        Log.d(TAG, "post response");
                        Log.d(TAG, response.code() + "");
                        if (response.isSuccessful()) {
                            Log.d(TAG, response.body() + "");

                            if (response.body().equals("no")) {
                                Toast.makeText(invititioncretion.this, "الدعوة لم يتم حفظها, الرجاء المحاولة مرة أخرى.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(invititioncretion.this, "تم حفظ الدعوة.", Toast.LENGTH_SHORT).show();
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


        }
        else
            Toast.makeText(this, "أنت غير متصل بالشبكة, الرجاء الاتصال بالشبكة وإعادة المحاولة", Toast.LENGTH_LONG).show();

    }

    //A method called when the user click on the Time button and call the Time picker fragment class.
    public void onButtonClickedTime(View v){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"TimePicker");
    }

    //A method called when the user click on the Date button and call the Date picker fragment class.
    public void onButtonClickedDate(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(),"DatePicker");
    }

    //check if there is network before an action
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //A method called when the user click on the Map button and call the Main Map Activity class.
    public void onButtonClickedMap(View v){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (gpsTracker.canGetLocation() && isNetworkAvailable()) {
                Intent intent = new Intent(invititioncretion.this,MainMapsActivity.class);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                startActivityForResult(intent,100);
            }
            else {
                Toast.makeText(this, "خدمة الـ GPS  او الانترنت معطلة, الرجاء تفعيلها.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    1);

        }

    }

    //save the returned new information from the map
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                latitude = data.getDoubleExtra("latitude",0);
                longitude = data.getDoubleExtra("longitude",0);

                Toast.makeText(this, "تم الحفظ بنجاح!", Toast.LENGTH_SHORT).show();

                Log.d("Location : ",latitude+","+longitude);
            }
        }
    }

}
