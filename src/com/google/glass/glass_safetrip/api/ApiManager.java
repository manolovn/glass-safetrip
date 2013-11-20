package com.google.glass.glass_safetrip.api;

import com.google.glass.glass_safetrip.Constants;
import com.google.gson.Gson;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * glass-safetrip
 * com.google.glass.glass_safetrip.api
 *
 * @autor manolo
 */
public class ApiManager {

    private SafeTripApi api;
    private RestAdapter restAdapter;

    public ApiManager() {
        // Api manager
        this.restAdapter = new RestAdapter.Builder()
                .setServer(Constants.API_URL)
                .setConverter(new GsonConverter(new Gson()))
                .build();

        // Api handler
        this.api = restAdapter.create(SafeTripApi.class);
    }

    public SafeTripApi getApi() {
        return this.api;
    }

}
