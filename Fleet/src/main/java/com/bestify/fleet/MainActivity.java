/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.bestify.fleet;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {

    Boolean loginModeActive = false;

    public void redirectIfLoggedIn() {

        if (ParseUser.getCurrentUser() != null) {

            Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
            startActivity(intent);

        }

    }

    public void toggleLoginMode(View view) {

        EditText emailEditText = (EditText) findViewById(R.id.emailEditText);

        Button loginSignupButton = (Button) findViewById(R.id.loginSignupButton);

        TextView toggleLoginModeTextView = (TextView) findViewById(R.id.toggleLoginModeTextView);

        if (loginModeActive) {

            loginModeActive = false;
            emailEditText.setVisibility(View.VISIBLE);
            loginSignupButton.setText("Sign Up");
            toggleLoginModeTextView.setText("Or, log in");


        } else {

            loginModeActive = true;
            emailEditText.setVisibility(View.INVISIBLE);
            loginSignupButton.setText("Log In");
            toggleLoginModeTextView.setText("Or, sign up");

        }

    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

  public void signupLogin(View view) {

      EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
      EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
      EditText emailEditText = (EditText) findViewById(R.id.emailEditText);

          if (loginModeActive) {
              if(usernameEditText.getText().toString().length() == 9){
              ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                  @Override
                  public void done(ParseUser user, ParseException e) {
                      if (e == null) {
                          Log.i("Info", "user logged in");
                          redirectIfLoggedIn();
                      } else {
                          String message = e.getMessage();
                          if (message.toLowerCase().contains("java")) {
                              message = e.getMessage().substring(e.getMessage().indexOf(" "));
                          }
                          Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                      }
                  }
              });
              }else{
                  Toast.makeText(this, "Nieprawidłowy numer telefonu", Toast.LENGTH_SHORT).show();
              }
          } else {
              if(isValidEmail(emailEditText.getText().toString()) && usernameEditText.getText().toString().length() == 9){
              ParseUser user = new ParseUser();
              user.setUsername(usernameEditText.getText().toString());
              user.setPassword(passwordEditText.getText().toString());
              user.put("EmailS", emailEditText.getText().toString());
              user.put("userType", "sender");
              user.signUpInBackground(new SignUpCallback() {
                  @Override
                  public void done(ParseException e) {
                      if (e == null) {
                          Log.i("Info", "user signed up");
                          redirectIfLoggedIn();
                      } else {
                          String message = e.getMessage();
                          if (message.toLowerCase().contains("java")) {
                              message = e.getMessage().substring(e.getMessage().indexOf(" "));
                          }
                          Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                      }
                  }
              });
              }else if(!isValidEmail(emailEditText.getText().toString())){
                  Toast.makeText(this, "Nieprawidłowy email", Toast.LENGTH_SHORT).show();
              }else{
                  Toast.makeText(this, "Nieprawidłowy numer telefonu", Toast.LENGTH_SHORT).show();
              }
          }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
      setTitle("Logowanie");
      redirectIfLoggedIn();
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}