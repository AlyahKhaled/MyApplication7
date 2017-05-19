package com.example.acer.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class apologization_not extends AppCompatActivity {


    final String TAG = this.getClass().getName();
    SharedPreferences pref;
    String UserName;
    public static String selectedFromList;
    public static String ID1;
    public static int ID;
    int ID2;
    InputStream is;
    String line = null;
    String result = null;
    public String[] arr;
    String result2 = null;
    public String[] arr2;
    public ArrayList<String> listitems;
    public ArrayList<String> listitems2;
    connectionDetector cd ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apologization_not);

        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        Log.d(TAG, pref.getString("UserName", ""));// 3
        Log.d(TAG, pref.getString("PassWord", ""));// 4
        UserName = pref.getString("UserName", ""); // 5
        cd= new connectionDetector(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//==============================================================================================================
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Mai/cancle.php?UserName=" + UserName);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
                sb.append(line + "\n");
            result = sb.toString();
            result = result.replace('"',' ');

            int length = result.length();
            String sreOne = result.substring(1, length - 2);

            //use toString() to get the data result
            result = sb.toString();
            arr = sreOne.split(",");


            listitems = new ArrayList<>(Arrays.asList(arr));
            CustomAdapter Adapter = new CustomAdapter(listitems);
            ListView lv = (ListView) findViewById(R.id.list);
            lv.setAdapter(Adapter);


        }   catch (Exception e) {
            System.out.print("exception 1 caught");
            //exception handel code
        }

//==============================================================================================================

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Mai/cancel2.php?UserName=" + UserName);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            sb.append(line + "\n");
            result2 = sb.toString();
            result2 = result2.replace('"',' ');
            int length = result2.length();
            String sreOne = result2.substring(1, length - 2);
            //use toString() to get the data result
            result2 = sb.toString();
            arr2 = sreOne.split(",");
            listitems2 = new ArrayList<>(Arrays.asList(arr2));
        }    catch (Exception e) {
            System.out.print("exception 1 caught");
            //exception handel code
        }
    }

    //==============================================================================================================
    public void display(String  text ){

        Toast toast= Toast.makeText(this,text,Toast.LENGTH_LONG);
        toast.show();

    }
    //==============================================================================================================
    public class CustomAdapter extends BaseAdapter {



        ArrayList<String> Items=new ArrayList<String>();
        CustomAdapter(ArrayList<String>Items){this.Items=Items;}

        @Override
        public int getCount() {
            return Items.size();
        }

        @Override
        public String getItem(int position) {
            return Items.get(position).toString();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            LayoutInflater lineflater=getLayoutInflater();
            View view1=lineflater.inflate(R.layout.apo_list,null);
            Button Accept=(Button)view1.findViewById(R.id.button7);

            TextView textView =(TextView)view1.findViewById(R.id.textView4);
            textView.setText(Items.get(position).toString());
            final String name=Items.get(position).toString();
            final int pos=position;

            View view2 = lineflater.inflate(R.layout.empty_list, null);
            TextView textView2 = (TextView) view2.findViewById(R.id.textView4);
            textView2.setText(" لا توجد رسائل اعتذار :(");

            if(!arr[0].equals("ul")) {

                Accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedFromList = name.toString();
                        selectedFromList = selectedFromList.replaceAll("\\s+", "");
                        ID1=listitems2.get(pos);
                        ID1 =ID1.replaceAll("\\s+", "");
                        ID=Integer.parseInt(ID1);
                        //display(ID1);

                        ID2 = pos;

                        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
                        Intent intent = new Intent(apologization_not.this, cancelation_message.class);
                        startActivity(intent);
                        //do things

                    }
                });



                return view1;
            }
            return view2;
        }


    }


    public void Back (View view)
    {
        onBackPressed();

    }

    public void profile (View view)
    {
        Intent intent = new Intent(apologization_not.this, profileuser.class);
        startActivity(intent);
    }

    }

