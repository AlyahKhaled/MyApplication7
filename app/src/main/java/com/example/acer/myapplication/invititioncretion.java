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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.myapplication.Activity.MyFriendList;
import com.example.acer.myapplication.Retrofit.APIService;
import com.example.acer.myapplication.Retrofit.ApiUtils;
import com.example.acer.myapplication.Retrofit.InvitationInfo;
import com.example.acer.myapplication.Utility.GPSTracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

//import com.example.dalal.gpnew.pref.UserInfo;


public class invititioncretion extends AppCompatActivity {

    //initialization

    public static boolean isEditDate;
    public static boolean isEditTime;

    public static double latitude=0;
    public static double longitude=0;
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

    private Spinner mSpinnerInvitationName = null;
    private Spinner mSpinnerAdditionalText = null;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton;

    public void openprofile(View v){
        Intent intent = new Intent(invititioncretion.this,profileuser.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invititioncretion);

        PlaceName = "";
        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE); // 2 creat file

        Log.d(TAG, pref.getString("UserName", ""));// 3
        Log.d(TAG, pref.getString("PassWord", ""));// 4

        gpsTracker=new GPSTracker(this);

        mSpinnerInvitationName = (Spinner)findViewById(R.id.spinner_invitation_name);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mRadioGroup.clearCheck();
        mSpinnerAdditionalText= (Spinner) findViewById(R.id.spinner_additional_text);
        Time= (TextView) findViewById(R.id.timeText);
        Date= (TextView) findViewById(R.id.Datetext);

        //code for update the invitation
        if(getIntent().hasExtra("ID")){
            id = getIntent().getStringExtra("ID");

            isEditDate = true;
            //isEditTime = true;

            mAPIService = ApiUtils.getAPIService();
            mAPIService.getInvitationInfo(id).enqueue(new Callback<InvitationInfo>() {
                @Override
                public void onResponse(Call<InvitationInfo> call, retrofit2.Response<InvitationInfo> response) {
                    Log.d(TAG, "post response");
                    Log.d(TAG, response.code() + "");
                    if (response.isSuccessful()) {
                        Log.d("AppInfo",response.body()+"");

                        PlaceName = response.body().getPlaceName();
                        if (PlaceName.equals("Home")){
                            RadioButton btn = (RadioButton) findViewById(R.id.radio_btn1);
                            btn.setChecked(true);
                        }else{
                            RadioButton btn = (RadioButton) findViewById(R.id.radio_btn2);
                            btn.setChecked(true);
                        }

                        id = response.body().getID();
                        venueID = response.body().getVenueID();

                        String str = response.body().getInvitationTopic();
                        String str1 = response.body().getAdditionalInformation();

                        mSpinnerInvitationName.setSelection(getSpinnerInvitationIndex(response.body().getInvitationTopic(),
                                getResources().getStringArray(R.array.invitaion_name_array)));
                        //PlaceNametxt.setText(response.body().getPlaceName());
                        mSpinnerAdditionalText.setSelection(getSpinnerAdditionalIndex(response.body().getAdditionalInformation(),
                                getResources().getStringArray(R.array.additional_info_array)));

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                        Date date = null;
                        try
                        {
                            date = simpleDateFormat.parse(response.body().getTime());
                        }
                        catch (ParseException ex)
                        {
                            System.out.println("Exception "+ex);
                        }
                        if (date != null) {
                            Time.setText(simpleDateFormat.format(date));
                        } else {
                            Time.setText(response.body().getTime());
                        }
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
                        Log.d("DEBUG","Place name: wile seneding:"+PlaceName);
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

        if(latitude == 0 && longitude == 0)
        {
            Toast.makeText(getApplicationContext(),
                    "يجب اختيار المكان!", Toast.LENGTH_SHORT)
                    .show();
            return 1;
        }
        InvitationName = getInvitationName(mSpinnerInvitationName.getSelectedItem().toString());
        if(InvitationName == null || InvitationName.length() == 0)
        {
            Toast.makeText(getApplicationContext(),
                    "يجب اختيار اسم الدعوة!", Toast.LENGTH_SHORT)
                    .show();
            return 1;
        }

        int selectedId = mRadioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        mRadioButton = (RadioButton) findViewById(selectedId);

        if (mRadioButton != null)
            PlaceName = getPlaceName(mRadioButton.getText().toString());

        if(PlaceName == null || PlaceName.length() == 0)
        {
            Toast.makeText(getApplicationContext(),
                    "يجب اختيار اسم المكان!", Toast.LENGTH_SHORT)
                    .show();
            return 1;
        }
        Log.d("DEBUG","Place name:"+PlaceName);


        TimeInfo=Time.getText().toString();

        int TxtPosition= TimeInfo.indexOf(":");
        TimeTxt=TimePickerFragment.passingTime;
        //TimeTxt=Time.getText().toString();
        Log.d("DEBUG","Time:"+TimeTxt);
        Log.d("DEBUG","Time Info:"+TimeInfo);
        if(TimeTxt == null || TimeTxt.length() == 0)
        {
            Toast.makeText(getApplicationContext(),
                    "يجب اختيار الوقت!", Toast.LENGTH_SHORT)
                    .show();
            return 1;
        }
        //DateInfo=Date.getText().toString();
        if (!isEditDate) {
            DateInfo = DatePickerFragment.passingDate;

            if (DateInfo == null || DateInfo.length() == 0) {
                Toast.makeText(getApplicationContext(),
                        "يجب اختيار التاريخ!", Toast.LENGTH_SHORT)
                        .show();
                return 1;
            }

            //int TxtPosition1= DateInfo.indexOf(":");
            DateTxt = DateInfo;//.substring(TxtPosition1+1);
        }else{
            DateTxt = Date.getText().toString();
        }

        AdditionalInfo=getAdditionalTextName(mSpinnerAdditionalText.getSelectedItem().toString());
        if(AdditionalInfo == null || AdditionalInfo.equals(""))
        {

//            AdditionalInfo="لا يوجد معلومات إضافية.)";
            AdditionalInfo="No additional info ";
           // Toast.makeText(getApplicationContext(),"الرجاء اختيار معلومات اضافيه", Toast.LENGTH_SHORT)
             //       .show();

            //return 1;
        }


        return 0;
    }

    // A methodb that go to friend list page and send the modt important information with the intent
    public  void list (View view)
    {
        if(isNetworkAvailable())
        {

            int result = GetInfo();
            if (result == 0) {
                String username = pref.getString("UserName", ""); // 5 after intilisatien
                Intent intent = new Intent(invititioncretion.this, friendslist.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("latitude", latitude);
                bundle.putDouble("longitude", longitude);
                bundle.putString("InvitationName", InvitationName);
                bundle.putString("AdditionalInfo", AdditionalInfo);
                bundle.putString("DateTxt", DateTxt);
                bundle.putString("TimeTxt", TimeTxt);
                Log.d("DEBUG", "Place name:" + PlaceName);
                bundle.putString("PlaceName", PlaceName);
                bundle.putString("InviterName", username);
                //define that it is a new invitation
                bundle.putString("path", "new");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
        else
            Toast.makeText(invititioncretion.this, "أنت غير متصل بالشبكة, الرجاء الاتصال بالشبكة وإعادة المحاولة", Toast.LENGTH_LONG).show();



    }


    //a method to get the information and save it in the data base
    public void SaveInvitation(View view)
    {
        showDialog();


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

    private int getSpinnerInvitationIndex(String invitationName, String[] stringArray) {
        for(int i=0; i< stringArray.length; i++) {
            if (getInvitationName(stringArray[i]).equals(invitationName)) {
                return i;
            }
        }
        return 0;
    }

    private int getSpinnerAdditionalIndex(String invitationName, String[] stringArray) {
        for(int i=0; i< stringArray.length; i++) {
            if (getAdditionalTextName(stringArray[i]).equals(invitationName)) {
                return i;
            }
        }
        return 0;
    }

    private String getInvitationName(String arabicName) {

        String invitationName = "";
        switch (arabicName) {
            case "حفلة معايدة" :
                invitationName = "Celebration party";
                break;
            case "اجتماع أصدقاء" :
                invitationName = "Friends gathering";
                break;
            case "اجتماع عمل" :
                invitationName = "Job meeting";
                break;
            case "حفلة أطفال" :
                invitationName = "Children party";
                break;
            case "حفلة نجاح" :
                invitationName = "Graduation party";
                break;
        }
        return invitationName;
    }

    private String getPlaceName(String arabicName) {
        Log.d("DEBUG","Arrabic Name:"+arabicName);
        String placeName = "";
        switch (arabicName) {
            case "المنزل" :
                placeName = "Home";
                break;
            case "خارج المنزل" :
                placeName = "Outside";
                break;
            default:Log.d("DEBUG","Novalue match");
                break;
        }
        return placeName;
    }

    private String getAdditionalTextName(String arabicName) {
        String additionalTextName = "";
        switch (arabicName) {
            case "لا يوجد معلومات إضافية" :
                additionalTextName = "No additional information";
                break;
            case "الحضور على الوقت" :
                additionalTextName = "Be on time";
                break;
            case "يمنع إصطحاب الاطفال" :
                additionalTextName = "No kids allowed";
                break;
            case "يمكنك إحضار مرافق" :
                additionalTextName = "Allowed to bring friends";
                break;
            default: Log.d("DEBUG","No match found");
        }
        return additionalTextName;
    }

    public void showDialog() {
        Log.d("DEBUG","Dialog intiated");
        AlertDialog.Builder builder = new AlertDialog.Builder(invititioncretion.this);
        builder.setCancelable(false);
        builder.setMessage("هل انت متاكد؟ ");
        builder.create();
        builder.setPositiveButton( "نعم", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                saveInvitationMethod();
            }
        });
        builder.setNegativeButton( "لا", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();

    }

    private void saveInvitationMethod() {

        if(isNetworkAvailable())
        {
            int result = GetInfo();
            if (result == 0) {

                String username=pref.getString("UserName", ""); // 5 after intilisatien

                Log.d(TAG, DateTxt);
                Log.d(TAG, TimeTxt);

                mAPIService = ApiUtils.getAPIService();
                Log.d("DEBUG","Place name: wile seneding:"+PlaceName);
                mAPIService.SaveInvitation(PlaceName, latitude + "", longitude + "", DateTxt, TimeTxt, AdditionalInfo, InvitationName, username).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        Log.d(TAG, "Save post response");
                        Log.d(TAG, response.code() + "");
                        if (response.isSuccessful()) {
                            Log.d(TAG, response.body() + "");

                            if (response.body().equals("no")) {
                                Toast.makeText(invititioncretion.this, "الدعوة لم يتم حفظها, الرجاء المحاولة مرة أخرى.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(invititioncretion.this, "تم حفظ الدعوة.", Toast.LENGTH_SHORT).show();
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
}
