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
                    "Content-Type:application/json",
                    "Authorization:key=AAAAEXDTeSA:APA91bGjefYjKkO59T5Pd5Y6hvrzBu1ZjgrCMQWQYKrVLyINM-LBJRYLETlWuO_zcgyFm_mg64yGAvq_dfHmtg0Y-uiWQL1kMJEzzj2bTScFjtMEXzpiNPbHTmsvSMdwfqbP1s86JbIB"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
