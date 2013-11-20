package com.google.glass.glass_safetrip.api;

import com.google.glass.glass_safetrip.entity.Accident;
import com.google.glass.glass_safetrip.entity.Emergency;
import com.google.glass.glass_safetrip.entity.SpeedTrap;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * glass
 * com.google.glass.glass_safetrip.api
 *
 * @autor manolo
 */
public interface SafeTripApi {

    @FormUrlEncoded
    @POST("/accident")
    void addAccident(@Field("longitude") double longitude, @Field("latitude") double latitude, @Field("comment") String comment, Callback<Accident> callback);

    //    @Multipart
    @FormUrlEncoded
    @POST("/emergency")
    void addEmergency(@Field("longitude") double longitude, @Field("latitude") double latitude, @Field("comment") String comment, Callback<Emergency> callback);

    //    @Multipart
    @FormUrlEncoded
    @POST("/speedtrap")
    SpeedTrap addSpeedTrap(@Field("longitude") double longitude, @Field("latitude") double latitude, @Field("comment") String comment);

}
