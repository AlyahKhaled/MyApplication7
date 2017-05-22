package com.example.acer.myapplication.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InvitationInfo implements Serializable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("InviterUserName")
    @Expose
    private String inviterUserName;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Time")
    @Expose
    private String time;
    @SerializedName("Companions")
    @Expose
    private String companions;
    @SerializedName("InvitationStates")
    @Expose
    private String invitationStates;
    @SerializedName("additionalInformation")
    @Expose
    private String additionalInformation;
    @SerializedName("VenueID")
    @Expose
    private String venueID;
    @SerializedName("invitationTopic")
    @Expose
    private String invitationTopic;
    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("PlaceName")
    @Expose
    private String placeName;
    @SerializedName("RestaurantName")
    @Expose
    private String restaurantName;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    private final static long serialVersionUID = -5454881712112372207L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInviterUserName() {
        return inviterUserName;
    }

    public void setInviterUserName(String inviterUserName) {
        this.inviterUserName = inviterUserName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCompanions() {
        return companions;
    }

    public void setCompanions(String companions) {
        this.companions = companions;
    }

    public String getInvitationStates() {
        return invitationStates;
    }

    public void setInvitationStates(String invitationStates) {
        this.invitationStates = invitationStates;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getVenueID() {
        return venueID;
    }

    public void setVenueID(String venueID) {
        this.venueID = venueID;
    }

    public String getInvitationTopic() {
        return invitationTopic;
    }

    public void setInvitationTopic(String invitationTopic) {
        this.invitationTopic = invitationTopic;
    }

    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}