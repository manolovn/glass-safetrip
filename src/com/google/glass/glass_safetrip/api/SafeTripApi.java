package com.google.glass.glass_safetrip.api;

import com.google.glass.glass_safetrip.entity.Accident;
import com.google.glass.glass_safetrip.entity.Emergency;
import com.google.glass.glass_safetrip.entity.SpeedTrap;
import retrofit.http.Multipart;
import retrofit.http.POST;

/**
 * glass
 * com.google.glass.glass_safetrip.api
 *
 * @autor manolo
 */
public interface SafeTripApi {

    @Multipart
    @POST("/accident")
    Accident addAccident();

    @Multipart
    @POST("/emergency")
    Emergency addEmergency();

    @Multipart
    @POST("/speedtrap")
    SpeedTrap addSpeedTrap();

}
