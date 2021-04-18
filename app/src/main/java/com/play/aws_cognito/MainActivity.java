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

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;

public class MainActivity extends AppCompatActivity {
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        String text = i.getStringExtra("Text");
        Log.i("Dashboard", text);

        final TextView displayName = findViewById(R.id.displayName);
        final TextView displayUsername = findViewById(R.id.displayUsername);
        final TextView displayMob = findViewById(R.id.displayMob);
        final TextView displayEmail = findViewById(R.id.displayEmail);

        CognitoSettings cognitoSettings = new CognitoSettings(MainActivity.this);

        Log.i("Dashboard", "inside dashboard page ");

        final CognitoUser thisUser = cognitoSettings.getUserPool().getCurrentUser();
        thisUser.getDetailsInBackground(new GetDetailsHandler() {
            @Override
            public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                String username = cognitoUserDetails.getAttributes().getAttributes().get("preferred_username");
                String name = cognitoUserDetails.getAttributes().getAttributes().get("name");
                String email  = cognitoUserDetails.getAttributes().getAttributes().get("email");
                String phone  = cognitoUserDetails.getAttributes().getAttributes().get("phone_number");
                displayUsername.setText(username);
                displayName.setText(name);
                displayMob.setText(phone);
                displayEmail.setText(email);
            }

            @Override
            public void onFailure(Exception exception) {
                Log.i("Dashboard","getDetailsInBackground failed "+exception.getLocalizedMessage());
            }
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("Dashboard", "logout button Clicked... ");
                thisUser.signOut();
                startActivity(new Intent(MainActivity.this,login.class));
                finish();
            }
        });
    }

}