package com.play.aws_cognito;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;

public class signup extends AppCompatActivity {
    public static final String TAG ="Signup Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final CognitoSettings cognitos = new CognitoSettings(getApplicationContext());

        final EditText name =findViewById(R.id.name);
        final EditText username =findViewById(R.id.username);
        final EditText useremail =findViewById(R.id.useremail);
        final EditText userphno =findViewById(R.id.userphone);
        final EditText userpass =findViewById(R.id.userpass);
        //final EditText name =findViewById(R.id.name);
        final EditText code =findViewById(R.id.code);

        final CognitoUserAttributes userAttributes = new CognitoUserAttributes();

        final SignUpHandler signupCallback = new SignUpHandler() {
            @Override
            public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                Log.i(TAG, "Sign up Success "+ signUpConfirmationState);
                Toast.makeText(signup.this, "Registration success. Verification maybe pending.", Toast.LENGTH_SHORT).show();


                if (!signUpConfirmationState){
                    Log.i(TAG, "Sign up success. not verified, confirmation sent "+ cognitoUserCodeDeliveryDetails.getDestination());

                }else{
                    Log.i(TAG, "Sign up success. User confirmed.");
                }
            }

            @Override
            public void onFailure(Exception exception) {
                 Log.i(TAG,"Sign up Failure" +exception.getLocalizedMessage());
                Toast.makeText(signup.this, "Signup Failed | Email Exists", Toast.LENGTH_SHORT).show();

            }
        };

        Button btnsignup =findViewById(R.id.signupbtn);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                userAttributes.addAttribute("name",String.valueOf(name.getText()));
                userAttributes.addAttribute("custom:username",String.valueOf(username.getText()));
                userAttributes.addAttribute("phone_number",String.valueOf(userphno.getText()));
                userAttributes.addAttribute("email",String.valueOf(useremail.getText()));

                CognitoSettings cognitoSettings =new CognitoSettings(signup.this);

                cognitoSettings.getUserPool ().signUpInBackground(String.valueOf(useremail.getText())
                , String.valueOf(userpass.getText()), userAttributes,null,signupCallback);
            }
        });




        Button btnVerify =findViewById(R.id.verify);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cognitos.confirmUser(String.valueOf(useremail.getText()), code.getText().toString().replace(" ", ""));
                //finish();
            }
        });
    }
}