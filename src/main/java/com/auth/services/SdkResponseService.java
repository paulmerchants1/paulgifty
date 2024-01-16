package com.auth.services;

import java.util.Map;

public interface SdkResponseService {
//    Map<String, Object> verifyApi(String eventId, String mobileNo, String sdkAuthToken);

    Map<String, Object> verifyApi(String mobileNo, String sdkAuthToken);

//    Map<String, Object> verifyApi(String eventId, String mobileNo, String sdkAuthToken);

    // New method to add data to the database
//    SdkResponse addSdkResponse(String eventId, String mobileNo, String sdkAuthToken);
}
