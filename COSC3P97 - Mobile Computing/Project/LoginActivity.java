package com.example.b.bc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends Activity {

    // database object and reference
    FirebaseDatabase db = FirebaseDatabase.getInstance
            ("https://brockcaster-1512252912138.firebaseio.com");
    DatabaseReference ref = db.getReference();
    // android_id
    private String aid;
    // views
    private ProgressBar prog;
    private TextView tv;

    /*
     * method to handle activity launch/resume
     *
     * @param savedInstanceState - previous activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews(); // initialize views
        changeProgress(); // remove progress bar
        checkUser(); // check if Android_ID exists in FB
    }

    /*
     * method to initialize views
     */
    private void initViews() {
        tv = findViewById(R.id.loginText);
        prog = findViewById(R.id.progressBar2);
    }

    /*
     * method to remove progress bar after a time
     */
    private void changeProgress() {
        // start new thread
        Handler h = new Handler();
        /*
         * if internet access, it'll bring you to either
         * signup or login activities. If it doesn't
         * within five seconds, assume no internet access
         */
        h.postDelayed(new Runnable() {
            public void run() {
                /*
                 * after 5 seconds, remove prog. bar
                 * and replace with hidden view
                 * which tells user to establish net conn
                 */
                prog.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
            }
        }, 5000); // for 5 seconds
    }

    /*
     * method to check whether user exists or not
     *
     * @return truth value
     */
    public boolean checkUser() {
        // get phone Android_ID
        aid = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
        // get users sorted by id
        ref.child("users").orderByChild("id").equalTo(aid).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) { // if this user exists
                            login(); // login and then go to maps
                        } else { // if they don't exist
                            signup(); // then go to signup activity
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError e) {
                    }
                });
        return false;
    }

    /*
     * method to direct user to map activity after login
     */
    public void login() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish(); // close this activity
    }

    /*
     * method to direct user to signup activity
     * after potential failed login
     */
    public void signup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish(); // close this activity
    }
}