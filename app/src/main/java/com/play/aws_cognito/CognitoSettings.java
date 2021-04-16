package com.play.aws_cognito;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class CognitoSettings {

    private String userPoolId = "";
    private String clientId = "";
    private String clientSecret ="";
    private Regions cognitoRegion = Regions.DEFAULT_REGION;


private Context context;

public CognitoSettings(Context context) {
    this.context =context;

}
    public static final String TAG ="Signup Activity";
    GenericHandler confirmationCallback = new GenericHandler() {

        @Override
        public void onSuccess() {
            // User was successfully confirmed
            Toast.makeText(context,"Success ! User Confirmed", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Sign-up success. User confirmed ");
            Intent i = new Intent(context,login.class);
            context.startActivity(i);
        }

        @Override
        public void onFailure(Exception exception) {

            Log.d(TAG, "Sign-up Failure. "+exception.getLocalizedMessage());

        }
    };


    public void confirmUser(String useremail,String code){
        CognitoSettings cognitoSettings =new CognitoSettings(context);
        CognitoUserPool userPool =  cognitoSettings.getUserPool();
        CognitoUser cognitoUser =  userPool.getUser(useremail);
        cognitoUser.confirmSignUpInBackground(code,false, confirmationCallback);
        //cognitoUser.confirmSignUp(code,false, confirmationCallback);
    }


public String getUserPoolId() {
    return userPoolId;
}
    public String getClientId() {
        return clientId;
    }

    public String getClientSecretd() {
        return clientSecret;
    }

    public Regions getCognitoRegion() {
    return cognitoRegion;
    }

    public CognitoUserPool getUserPool() {
    return new CognitoUserPool(context, userPoolId, clientId, clientSecret, cognitoRegion);
    }
}
