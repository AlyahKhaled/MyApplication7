package com.example.acer.myapplication.Retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by user on 5/18/2017.
 */

public interface APIService {




    @POST("/zwara/GetFreinds.php")
    @FormUrlEncoded
    Call<FriendListResponse> getFriendList(@Field("UserName") String UserName);

    @POST("/zwara/searchForAfriend.php")
    @FormUrlEncoded
    Call<FriendListResponse> searchAFriend(@Field("friendname") String friendname);

    @POST("/zwara/AddFriend.php")
    @FormUrlEncoded
    Call<String> addFriend(@Field("UserName") String UserName,
                           @Field("FriendUserName") String FriendUserName);

    @POST("/zwara/SaveInvitation.php")
    @FormUrlEncoded
    Call<String> SaveInvitation(@Field("PlaceName") String PlaceName,
                                @Field("latitude") String latitude,
                                @Field("longitude") String longitude,
                                @Field("DateTxt") String DateTxt,
                                @Field("TimeTxt") String TimeTxt,
                                @Field("AdditionalInfo") String AdditionalInfo,
                                @Field("InvitationName") String InvitationName,
                                @Field("InviterName") String InviterName);

    @POST("/zwara/retriveInvitation.php")
    @FormUrlEncoded
    Call<InvitationResponse> getInvitations(@Field("Uname") String Uname);

    @POST("/zwara/deleteInvitation.php")
    @FormUrlEncoded
    Call<String> deleteInvitation(@Field("ID") String ID);

    @POST("/zwarhapp/deleteinvitations.php")
    @FormUrlEncoded
    Call<String> deleteInvitations(@Field("Uname") String Uname);

    @POST("/zwara/deletFriend.php")
    @FormUrlEncoded
    Call<String> deleteFriend(@Field("UserName") String UserName,
                              @Field("FriendName") String FriendName);

    @POST("/zwara/AddInvitation.php")
    @FormUrlEncoded
    Call<String> addInvitation(@Field("PlaceName") String PlaceName,
                               @Field("latitude") String latitude,
                               @Field("longitude") String longitude,
                               @Field("DateTxt") String DateTxt,
                               @Field("TimeTxt") String TimeTxt,
                               @Field("AdditionalInfo") String AdditionalInfo,
                               @Field("InvitationName") String InvitationName,
                               @Field("InviterName") String InviterName,
                               @Field("guests") String guests
    );

    @POST("/zwara/updateInvitation.php")
    @FormUrlEncoded
    Call<String> updateInvitation(@Field("PlaceName") String PlaceName,
                                  @Field("latitude") String latitude,
                                  @Field("longitude") String longitude,
                                  @Field("DateTxt") String DateTxt,
                                  @Field("TimeTxt") String TimeTxt,
                                  @Field("AdditionalInfo") String AdditionalInfo,
                                  @Field("InvitationName") String InvitationName,
                                  @Field("InviterName") String InviterName,
                                  @Field("venueID") String VenueID,
                                  @Field("ID") String ID
    );

    @POST("/zwara/AddOnlyInvitation.php")
    @FormUrlEncoded
    Call<String> addOnlyInvitation(@Field("ID") String ID,
                                   @Field("guests") String guests
    );


    @POST("/zwara/respondFriendRequest.php")
    @FormUrlEncoded
    Call<String> respondFriendRequest(@Field("UserName") String UserName,
                                      @Field("FriendName") String FriendUserName,
                                      @Field("response") int response);


    @POST("/zwara/getInvitation.php")
    @FormUrlEncoded
    Call<InvitationInfo> getInvitationInfo(@Field("ID") String ID);

}
