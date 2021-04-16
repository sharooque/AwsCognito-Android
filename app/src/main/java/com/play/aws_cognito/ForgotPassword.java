package com.play.aws_cognito;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;

public class ForgotPassword extends AppCompatActivity {

    private static final String TAG = "Cognito";
    private ForgotPasswordContinuation resultContinuation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setTitle("Forgot Password");

        final EditText email = findViewById(R.id.useremail);
        final EditText code = findViewById(R.id.Code);
        final EditText newPassword = findViewById(R.id.newPassword);
        Button getcodebutton = findViewById(R.id.getcode);
        Button submit = findViewById(R.id.Submit);

        final ForgotPasswordHandler callback = new ForgotPasswordHandler(){

            @Override
            public void onSuccess(){
                Log.i(TAG, "Password changed successfully");
                Toast.makeText(ForgotPassword.this,"Password reset successful",Toast.LENGTH_LONG).show();
                startActivity(new Intent(ForgotPassword.this, login.class));
            }

            @Override
            public void getResetCode(ForgotPasswordContinuation continuation){
                Log.i("TAG","in getResetCode");

                CognitoUserCodeDeliveryDetails codeSentHere = continuation.getParameters();
                Log.i(TAG,"code sent here "+codeSentHere.getDestination());

                resultContinuation = continuation;
            }

            public void onFailure(Exception exception){
                Log.i(TAG, "Password change failed"+exception.getLocalizedMessage());
                Toast.makeText(ForgotPassword.this,"Password reset failed",Toast.LENGTH_LONG).show();
            }

        };

        getcodebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                CognitoSettings cognitos = new CognitoSettings(ForgotPassword.this);
                CognitoUser thisUser = cognitos.getUserPool().getUser(String.valueOf(email.getText()));
                Log.i(TAG,"Calling forgot password to get confirmation code ");
                thisUser.forgotPasswordInBackground(callback);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i(TAG, "got code and password, setting continuation object");
                resultContinuation.setPassword(String.valueOf(newPassword.getText()));
                resultContinuation.setVerificationCode(String.valueOf(code.getText()));
                Log.i(TAG, "got code and password, calling continueTask()");
                resultContinuation.continueTask();
            }
        });
    }
}