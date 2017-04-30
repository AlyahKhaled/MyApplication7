package com.example.acer.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.io.InputStream;

public class admienprofile extends AppCompatActivity implements View.OnClickListener {
    final String TAG = this.getClass().getName();

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ImageButton logoutBtn;
    public InputStream is ;
    String UserName = pref.getString("UserName", "");
   // ListView lv ;
   // String line = null;
    //String result = null;
   // String [] arr ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admienprofile);

        logoutBtn = (ImageButton) findViewById(R.id.logoutBtn);
        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);

        Log.d(TAG, pref.getString("UserName", ""));
        Log.d(TAG, pref.getString("PassWord", ""));

        logoutBtn.setOnClickListener(this);
      /*  lv = (ListView) findViewById(R.id.listV1);


        StrictMode.ThreadPolicy policy = new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);




        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Alyah/retriveAdminInfo.php?UserName="+UserName);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();


            is = entity.getContent();


        }catch (Exception e){
            System.out.print("exception 1 caught");
            //exception handel code
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);

            StringBuilder sb = new StringBuilder();
            while ((line=reader.readLine())!=null)
                sb.append(line+"\n");

            result=sb.toString();
            result=result.replace('"',' ');
            int length =result.length();
            String sreOne =result.substring(1,length-2);

            //use toString() to get the data result
            result=sb.toString();
            // check the data
            System.out.println(sreOne);
            arr= sreOne.split(",");
            int arrLength = arr.length ;

            lv.setAdapter(new ArrayAdapter<String>(admienprofile.this,android.R.layout.simple_list_item_1,arr));




        }  catch (IOException e) {
            e.printStackTrace();
        }



    }*/

    }
    public void onClick(View view) {
        editor = pref.edit();
        editor.clear();
        editor.commit();
        Intent in = new Intent(admienprofile.this, LoginActivity.class);
        startActivity(in);
    }
}
