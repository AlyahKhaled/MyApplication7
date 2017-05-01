package com.example.acer.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;

public class adminListOfUsers extends AppCompatActivity {

    public String [] items ;
    public ArrayList<String> listItims ;
    public ArrayAdapter<String> adapter ;
    public ListView listView ;
    public EditText editText ;
    public InputStream is ;
    public String line = null;
    public String result = null;
    public TextView textView ;
    public int ItimPostion ;
    public boolean Users ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_of_users);


        listView = (ListView) findViewById(R.id.listview);
        editText = (EditText) findViewById(R.id.textSearch);
        textView = (TextView) findViewById(R.id.textitem);
        Users = false;
        //Data Base connection
        // this line of code will fill the  list view with the content in the array


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // this in order to set up the code to fetch data from database

        try {
            HttpClient httpClient = new DefaultHttpClient();

            // spacifi url for the retrive

            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Dalal/retrive.php?");
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // set up the input stream to receive the data

            is = entity.getContent();


        } catch (Exception e) {
            System.out.print("exception 1 caught");
            //exception handel code
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

            // creat String builder object to hold the data

            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
                sb.append(line + "\n");


            //orgnizing the array to be displayed
            result = sb.toString();
            result = result.replace('"', ' ');
            int length = result.length();
            if (!result.contains("no users found :/")) {
                Users = true;
                String sreOne = result.substring(3, length - 2);
                items = sreOne.split(",");
                //fill the adapter
                initList();
            } else {
                String sreTwo = "Sorry No Users Found ";
                items = sreTwo.split(",");
                listView.setAdapter(new ArrayAdapter<String>(adminListOfUsers.this, android.R.layout.simple_list_item_1, items));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //end of Database connection


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItimPostion = position;

                if (Users) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(adminListOfUsers.this);
                    alertDialogBuilder.setMessage("are you sure you want to delete this user? ");
                    alertDialogBuilder.setPositiveButton("No",new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });

                    alertDialogBuilder.setNegativeButton("yes",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    deletUser(items[ItimPostion]);

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            }


        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(""))
                {//reset listView
                    initList ();
                }

                else {
                    searchItem(s.toString());
                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    //**************************************************

    public void deletUser (String p)
    {
        System.out.println("********************* ItimPostion ***************************");
        System.out.println(p+"length [p] "+p.length()+p);
        String value =p.substring(1,p.length()-1);
        System.out.println(value+"length [str] "+value.length()+value);

        // Delet from database

        InputStream is=null;
        String result=null;
        String line=null;
        int code;


        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://zwarh.net/zwarhapp/Dalal/DeletUser.php?value="+value);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success ");
        }
        catch(Exception e)
        {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            String reslt = sb.toString();
            if(reslt.contains("1"))
                System.out.println("*******************Deleted*********************************");
            is.close();
            result = sb.toString();
            Log.e("pass 2", "connection success ");
        }
        catch(Exception e)
        {
            Log.e("Fail 2", e.toString());
        }

        // End Delet


    }


    public void searchItem (String textToSearch)
    {
        for (String item : items)
        {
            if(!item.contains(textToSearch))
            {
                listItims.remove(item);

            }
        }

        adapter.notifyDataSetChanged();


    }

    public void initList () {

        listItims=new ArrayList<>(Arrays.asList(items));
        adapter=new ArrayAdapter<String>(this, R.layout.listitem, R.id.textitem, listItims);
        listView.setAdapter(adapter);



    }



    public void register(View view)
    {
        Intent intent = new Intent(adminListOfUsers.this,registerUser.class);
        startActivity(intent);
    }


}
