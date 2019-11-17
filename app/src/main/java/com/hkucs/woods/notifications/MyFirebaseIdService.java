package com.hkucs.woods.notifications;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseIdService extends FirebaseMessagingService {

        @Override
        public void onNewToken(String s) {
            super.onNewToken(s);
            Log.d("NEW_TOKEN",s);
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            String refreshToken = FirebaseInstanceId.getInstance().getToken();
            if (firebaseUser!= null){
                updateToken(refreshToken);
            }
        }

        private void updateToken(String refreshToken ){
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("tokens");
            Token token = new Token(refreshToken);
        reference.child(firebaseUser.getUid()).setValue(token);
    }
}
