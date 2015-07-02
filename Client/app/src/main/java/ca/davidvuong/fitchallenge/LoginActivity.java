package ca.davidvuong.fitchallenge;


import android.annotation.TargetApi;
import android.app.Activity;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {


    // UI references.
    private AutoCompleteTextView mEmailView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.name);

        Button mEmailSignInButton = (Button) findViewById(R.id.next_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: send stuff to next activity

                Intent i = new Intent(getApplicationContext(), ActivitySelection.class);
                String email = mEmailView.getText().toString();
                i.putExtra("userName", email);
                startActivity(i);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
    }
}