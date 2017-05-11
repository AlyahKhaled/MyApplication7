package com.example.acer.myapplication;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class connectionDetector {
    Context context ;

    public connectionDetector (Context context)
    {
        this.context=context;
    }

    public boolean icConnected (){

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if(connectivity != null)
        {
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if(networkInfo !=null)
            {
                if(networkInfo.getState()== NetworkInfo.State.CONNECTED)
                {
                    return true ;
                }

            }
        }


        return  false;
    }
}
