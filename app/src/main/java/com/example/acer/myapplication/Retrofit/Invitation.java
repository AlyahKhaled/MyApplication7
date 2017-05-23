package com.example.acer.myapplication.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Invitation implements Serializable
{

    @SerializedName("InviterUserName")
    @Expose
    private String inviterUserName;
    @SerializedName("invitationTopic")
    @Expose
    private String invitationTopic;
    @SerializedName("ID")
    @Expose
    private String ID;

    private final static long serialVersionUID = 7986813810534394219L;

    public String getInviterUserName() {
        return inviterUserName;
    }

    public void setInviterUserName(String inviterUserName) {
        this.inviterUserName = inviterUserName;
    }

    public String getInvitationTopic() {
        return invitationTopic;
    }

    public void setInvitationTopic(String invitationTopic) {
        this.invitationTopic = invitationTopic;
    }

    public String getID() {
        return ID;
    }

    public void setID(String venueID) {
        this.ID = ID;
    }

}