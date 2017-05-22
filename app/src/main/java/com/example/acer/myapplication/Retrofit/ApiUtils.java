package com.example.acer.myapplication.Retrofit;

/**
 * Created by user on 5/18/2017.
 */

public class ApiUtils {

    private ApiUtils() {}

//    public static final String BASE_URL = "http://192.168.10.5/";

    public static final String BASE_URL = "http://zwarh.net/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

}
