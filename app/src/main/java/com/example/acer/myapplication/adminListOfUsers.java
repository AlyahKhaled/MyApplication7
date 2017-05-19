package com.example.acer.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
    public String   theClickedItem ;
    public String value ;
    public connectionDetector cd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_of_users);


        listView = (ListView) findViewById(R.id.listview);
        editText = (EditText) findViewById(R.id.textSearch);
        textView = (TextView) findViewById(R.id.textitem);
        cd= new connectionDetector(this);
        Users = true;

        if(connectionChecker ())
          connectAndRetriveUsers ();
        else
        { Toast.makeText(adminListOfUsers.this,"Network connection problems",Toast.LENGTH_SHORT).show();}


    }


    //******************************************Methods******************************************

    public void connectAndRetriveUsers ()
    {
        //=======================================DatBase connection===============================


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
           // this in order to set up the code to fetch data from database
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://zwarh.net/zwarhapp/Dalal/retrive.php?");
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent(); // set up the input stream to receive the data
            }
            catch (Exception e) { System.out.print("exception 1 caught");}

        try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                // create String builder object to hold the data
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null)
                sb.append(line + "\n");
               //organizing the array to be displayed
                result = sb.toString();
              //  System.out.println("*******result"+result+"***result Length"+result.length());
              //  System.out.println("*******result"+result.substring(0,5)+"***result Length"+result.length());
                result = result.replace('"', ' ');

                if (!result.contains("connections error ")) {
                    if (!result.contains("no users found ")) {
                        postiveResult();
                    } else {
                        negativResult(1);
                    }
                }
                else {
                    negativResult(0);}

        }
        catch (IOException e) {e.printStackTrace();}
        //==================================end of Database connection=============================

        //================================ifDeletionDetected========================================

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                theClickedItem = listView.getItemAtPosition(position).toString();
                 value =theClickedItem.substring(1,theClickedItem.length()-1);


                if (Users) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(adminListOfUsers.this);
                    alertDialogBuilder.setMessage("هل أنت متأكد من حذف هذا المستخدم؟ ");
                    alertDialogBuilder.setPositiveButton("No",new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) { }
                    });

                            alertDialogBuilder.setNegativeButton("yes",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    if(connectionChecker ())
                                        deletUser();
                                    else
                                    { Toast.makeText(adminListOfUsers.this,"تعذر الحذف",Toast.LENGTH_SHORT).show();
                                      Toast.makeText(adminListOfUsers.this,"لايمكن الإتصال في الإنترنت",Toast.LENGTH_SHORT).show();}


                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            }

        });

            //========================this part is for searching in the list========================

            editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(""))
                { listView.setAdapter(new ArrayAdapter<String>(adminListOfUsers.this, android.R.layout.simple_list_item_1, items)); }//fill listView

                else {searchItem(s.toString());}
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    private void postiveResult() {
        int length = result.length();
        String sreOne = result.substring(1, length - 2);
        items = sreOne.split(",");
        //fill the adapter
        listView.setAdapter(new ArrayAdapter<String>(adminListOfUsers.this, android.R.layout.simple_list_item_1,items));
    }

    private void negativResult(int messageNum) {

        Users =false;
        String errorMessage ;

        if(messageNum ==1)
            errorMessage = "لا يوجد مستخدمين!";
        else
            errorMessage = "حدث خطأ في الإتصال!";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(adminListOfUsers.this);
        alertDialogBuilder.setMessage(errorMessage);
        alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        items = errorMessage.split(",");
        listView.setAdapter(new ArrayAdapter<String>(adminListOfUsers.this, android.R.layout.simple_list_item_1, items));
    }


    public void deletUser ()
    {
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
            if(!is.equals(null))
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String reslt = sb.toString();
                if (reslt.contains("1"))
                    System.out.println("*******************Deleted*********************************");
                is.close();
                result = sb.toString();
                Log.e("pass 2", "connection success ");
            }
        }
        catch(Exception e)
        {
            Log.e("Fail 2", e.toString());
        }

        // End Delete

        if(connectionChecker ())
            connectAndRetriveUsers();
        else
        { Toast.makeText(adminListOfUsers.this,"تعذر التحديث",Toast.LENGTH_SHORT).show();
            Toast.makeText(adminListOfUsers.this,"لايمكن الإتصال في الإنترنت",Toast.LENGTH_SHORT).show();}


    }

    public void searchItem (String textToSearch)
    {
        listItims=new ArrayList<>(Arrays.asList(items));
        for (String item : items)
            { if(!item.contains(textToSearch))
            {listItims.remove(item);}}

        listView.setAdapter(new ArrayAdapter<String>(adminListOfUsers.this, android.R.layout.simple_list_item_1,listItims));
    }

    public void register(View view)
    {
        Intent intent = new Intent(adminListOfUsers.this,registerUser.class);
        startActivity(intent);
    }

    public void Back (View view)
    {
        onBackPressed();
    }

    public void admProf (View view)
    {
        Intent intent = new Intent(adminListOfUsers.this,admienprofile.class);
        startActivity(intent);
    }

    public boolean connectionChecker ()
    {
        boolean connectionstatose = false ;

        if(cd.icConnected())
            connectionstatose = true ;

        return connectionstatose ;

    }


}
