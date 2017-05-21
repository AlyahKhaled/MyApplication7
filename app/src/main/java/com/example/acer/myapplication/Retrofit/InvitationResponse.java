package com.example.acer.myapplication.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class InvitationResponse implements Serializable
{

    @SerializedName("invitations")
    @Expose
    private List<Invitation> invitations = null;
    private final static long serialVersionUID = -1235884736664563310L;

    public List<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
    }

}