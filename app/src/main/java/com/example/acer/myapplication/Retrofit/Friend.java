package com.example.acer.myapplication.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by user on 5/18/2017.
 */

public class Friend implements Serializable
{

    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("PersonalMessage")
    @Expose
    private Object personalMessage;
    private final static long serialVersionUID = -6004323427049047162L;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getPersonalMessage() {
        return personalMessage;
    }

    public void setPersonalMessage(Object personalMessage) {
        this.personalMessage = personalMessage;
    }
}
