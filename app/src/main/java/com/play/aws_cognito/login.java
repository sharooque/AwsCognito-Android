package com.play.aws_cognito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;

public class login extends AppCompatActivity {
EditText usernamelg,userpasslg;
Button btnlogin, signupbtn, forgotPassword;
 ;
public static final String TAG="login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernamelg=findViewById(R.id.useremaillg);

        userpasslg=findViewById(R.id.userpasslg);


        btnlogin=findViewById(R.id.login);
        signupbtn= findViewById(R.id.signupbtn);
        forgotPassword = findViewById(R.id.forgotPassword);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, signup.class);
                startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });


        final AuthenticationHandler authenticationHandler= new AuthenticationHandler() {

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                Log.i(TAG, "in AuthenticationDetails()....");

                AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, String.valueOf(userpasslg.getText()), null);

                authenticationContinuation.setAuthenticationDetails(authenticationDetails);

                authenticationContinuation.continueTask();
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                Log.i(TAG, "in getMFACode: ");
            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                Log.i(TAG, "authenticationChallenge: ");
            }

            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                Log.i(TAG, "Login Success");
                Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intentlogin =new Intent(login.this, MainActivity.class);
                intentlogin.putExtra("Text",usernamelg.getText().toString());
                startActivity(intentlogin);
                finish();
            }

            @Override
            public void onFailure(Exception exception) {
                Log.i(TAG, "Login Failed " +exception.getLocalizedMessage());
                Toast.makeText(login.this, "Login Failed, Please Check UserName and Password", Toast.LENGTH_SHORT).show();
            }
        };
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CognitoSettings cognitoSettings = new CognitoSettings(login.this);

                CognitoUser thisUser = cognitoSettings.getUserPool().getUser(String.valueOf(usernamelg.getText()));
                Log.i(TAG, "Clicked... ");

                thisUser.getSessionInBackground(authenticationHandler);
            }
        });

    }

}