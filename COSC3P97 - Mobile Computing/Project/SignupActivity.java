package com.example.b.bc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends Activity {

    // database object and reference
    FirebaseDatabase db = FirebaseDatabase.getInstance
            ("https://brockcaster-1512252912138.firebaseio.com");
    DatabaseReference ref = db.getReference();
    // fields and buttons
    private EditText userField;
    private TextView aidField;
    private Button submitButton;
    // alert dialog
    private AlertDialog alert;
    private String aid;

    /*
     * method to handle activity begin/resume
     *
     * @param savedInstanceState - previous activity instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initViews(); // set up views
        populateAidField(); // get android_id
        addListener(); // add listeners
    }

    /*
     * method to initialize views
     */
    public void initViews() {
        userField = findViewById(R.id.userField);
        aidField = findViewById(R.id.aidField);
        submitButton = findViewById(R.id.submitButton);
        alert = new AlertDialog.Builder(this).create();
        // all alerts are error so set it here
        alert.setTitle("Error!");
    }

    /*
     * method to grab user android_id
     */
    public void populateAidField() {
        // get the id
        aid = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
        // set global aid to that string
        aidField.setText(aid);
    }

    /*
     * method to add listeners to buttons
     */
    public void addListener() {
        final Context aContext = this;
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                String checkString = userField.getText().toString();
                // if empty string
                if (checkString.equals("")) {
                    alert.setMessage("Username field left blank.");
                    alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int w) {
                                    dialog.dismiss();
                                }
                            });
                    alert.show();
                // if username too short
                } else if (checkString.length() < 3) {
                    alert.setMessage("Usernames must be between 3 and 15 characters.");
                    alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int w) {
                                    dialog.dismiss();
                                }
                            });
                    alert.show();
                // otherwise let's go!
                } else {
                    // instantiate and initialize new user
                    Users newUser = new Users(aid, checkString);
                    // add to fb
                    ref.child("users").child(aid).setValue(newUser);
                    Intent intent = new Intent(aContext, MapsActivity.class);
                    // start map activity
                    startActivity(intent);
                    // close this activity
                    finish();
                }
            }
        });
    }
}