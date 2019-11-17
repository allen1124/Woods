package com.hkucs.woods.ui.message;

import com.hkucs.woods.notifications.MyResponse;
import com.hkucs.woods.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:applicatino/json",
                    "Authorization:key=AAAAVQjfb2I:APA91bHlRkv5D9whatn8hu_jEk4xr7RQN5dz8gCPr34PeWKDHMQBpl9LS3akwO3q1jjzahFTU0Yp3R0lyKCwyoogFKmN73OXYtuJCjdj9N9h7-4-Uw-JcaQa063-sBBBhzZiGt39Q71l"
            }
    )

    @POST("fsm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
