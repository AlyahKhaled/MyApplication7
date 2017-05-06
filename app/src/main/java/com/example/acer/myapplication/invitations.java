package com.example.acer.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class invitations extends AppCompatActivity {

    public static String selectedFromList;
    public static int ID;
    InputStream is;
    String line = null;
    String result = null;
    public String[] arr;
    public ArrayList<String> listitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Mai/invitations.php");
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
                sb.append(line + "\n");
            result = sb.toString();
            result = result.replace('"', ' ');
            int length = result.length();
            String sreOne = result.substring(1, length - 2);

            //use toString() to get the data result
            result = sb.toString();
            arr = sreOne.split(",");

            listitems = new ArrayList<>(Arrays.asList(arr));
            CustomAdapter Adapter = new CustomAdapter(listitems);
            ListView lv = (ListView) findViewById(R.id.list);
            lv.setAdapter(Adapter);


        } catch (Exception e) {
            System.out.print("exception 1 caught");
            //exception handel code
        }

    }


    public void display(String  text ){

        Toast toast= Toast.makeText(this,text,Toast.LENGTH_LONG);
        toast.show();

    }

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
            View view1=lineflater.inflate(R.layout.invitation_list,null);
            Button Accept=(Button)view1.findViewById(R.id.button7);
            Button Delete=(Button)view1.findViewById(R.id.button9);
            TextView textView =(TextView)view1.findViewById(R.id.textView4);
            textView.setText(Items.get(position).toString());
            final String name=Items.get(position).toString();
            final int pos=position;

            Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedFromList = name.toString();
                    selectedFromList = selectedFromList.replaceAll("\\s+", "");

                    ID = pos ;

                    display(selectedFromList);

                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);


                    Intent intent = new Intent(invitations.this,invitation_info.class);
                    startActivity(intent);

                    //do things

                }
            });


            Delete.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(invitations.this);
                    builder.setMessage("Are you sure you wants to delete this invitation");
                    builder.setPositiveButton("No", new DialogInterface.OnClickListener() {


                        public void onClick(DialogInterface dialog, int id) {

                            //do things
                        }
                    });


                    builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            List<NameValuePair>  nameValuePair =new ArrayList<NameValuePair>(1);
                            String selectedFromList = name.toString();
                            selectedFromList = selectedFromList.replaceAll("\\s+", "");

                            try{
                                HttpClient httpClient = new DefaultHttpClient();
                                HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Mai/delet_inv.php?selectedFromList="+selectedFromList);
                                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                                HttpResponse response = httpClient.execute(httpPost);
                                HttpEntity entity = response.getEntity();
                                is=entity.getContent();
                                String msg = "Deleted succefully :) ";
                                Items.remove(Items.get(ID));
                                notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //do things
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });



            return view1;


        }


    }
}
