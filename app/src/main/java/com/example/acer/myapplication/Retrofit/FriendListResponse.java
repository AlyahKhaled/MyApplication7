package com.example.acer.myapplication.Retrofit;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 5/18/2017.
 */

public class FriendListResponse implements Serializable{


    @SerializedName("friends")
    @Expose
    private List<Friend> friends = null;
    private final static long serialVersionUID = 6121344041618613199L;

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

}
