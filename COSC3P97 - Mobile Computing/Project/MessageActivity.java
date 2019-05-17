package com.example.b.bc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MessageActivity extends Activity {

    // connect to database
    FirebaseDatabase db = FirebaseDatabase.getInstance
            ("https://brockcaster-1512252912138.firebaseio.com");
    // create a database reference to reference later
    DatabaseReference ref = db.getReference();
    // various views for UI elements
    private TextView chatBox;
    private TextView toolbarTitle;
    private EditText messageField;
    private Button backButton;
    private Button sendButton;
    // the name of the chatroom entered
    private String roomName;
    // user phone android_id
    private String aid;

    // on activity create, do ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message); // get approp. layout
        initViews(); // initiate views
        setUpListener(); // set up button listeners
        getRoomName(); // grab room name from intent
        getMessages(); // print messages on screen
    }

    /*
     * method to initialize instantiated views
     */
    private void initViews() {
        // get android_id
        aid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        // init views
        backButton = findViewById(R.id.backButton);
        sendButton = findViewById(R.id.sendButton);
        messageField = findViewById(R.id.messageField);
        toolbarTitle = findViewById(R.id.toolbar_title);
        chatBox = findViewById(R.id.chatBox);
        // makes chatBox scrollable
        chatBox.setMovementMethod(new ScrollingMovementMethod());
    }

    /*
     * method to grab messages from db
     * and print them on screen
     */
    private void getMessages() {
        // get entries in messages table, but only matching current room
        ref.child("messages").orderByChild("room").equalTo(roomName).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    // on function call
                    @Override
                    public void onDataChange(DataSnapshot dS) {
                        chatBox.setText(""); // reset chat
                        // used for printing in reverse order
                        ArrayList<Messages> listMess = new ArrayList<>();
                        // for each snapshot
                        for (DataSnapshot ss : dS.getChildren()) {
                            // grab the message
                            Messages m = ss.getValue(Messages.class);
                            // add to the arraylist to be sorted
                            listMess.add(m);
                        }
                        // reverse the array list
                        Collections.reverse(listMess);
                        // for each message found earlier (reverse order from key)
                        for (Messages m : listMess) {
                            // print them
                            printMessage(m);
                        }
                    }

                    // never used but mandatory method implementation
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    /*
     * method to construct the printable message
     * and then print it
     *
     * @param m - the message to print
     */
    private void printMessage(Messages m) {
        // print it formatted as HTML
        String htmlString = "";
        // want "header" to be bolded
        htmlString += "<b>";
        // append author
        htmlString += m.getAuthor();
        htmlString += " said at ";
        htmlString += "<i>";
        // append timestamp
        htmlString += m.getTimestamp();
        htmlString += "</i></b>";
        htmlString += ": ";
        // next line (message body)
        htmlString += "<br/>";
        // append the actual message
        htmlString += m.getMessage();
        // need spacing between messages
        htmlString += "<br/><br/>";
        // add it to the chatBox
        chatBox.append(Html.fromHtml(htmlString));
    }

    /*
     * method to set up the listeners for buttons
     * and for firebase changes
     */
    private void setUpListener() {
        final Context backContext = this;
        // toolbar back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // not sure if this is necessary but I null it in case
                backButton.setOnClickListener(null);
                // close activity, returning to map
                finish();
            }
        });
        // message send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // only do it if a message exists
                if (messageField.getText().toString().length() > 0) {
                    // get message from edittext field
                    String theMessage = messageField.getText().toString();
                    // erase edittext field
                    messageField.setText("");
                    // send it away
                    sendMessage(theMessage);
                }
            }
        });
        // for updating the chat on various firebase changes
        ref.child("messages").orderByChild("room")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dS, String aString) {
                        getMessages(); // if new child, refresh chatbox
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String aString) {
                        getMessages(); // likewise for changes
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        getMessages(); // likewise for deletions
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String aString) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    /*
     * method to send the user message to firebase
     *
     * @param input - the message to insert into fb
     */
    private void sendMessage(String input) {
        // need final for inner anonymous method visibility
        final String theMessage = input;
        // single event; grab username from users based on aid
        ref.child("users").child(aid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get HH:mm time
                String timeStamp = new SimpleDateFormat("HH.mm").format(new Date());
                // instantiate a user to get username
                Users u = dataSnapshot.getValue(Users.class);
                // construct the new message
                Messages m = new Messages(u.getName(), roomName, theMessage, timeStamp);
                // insert into fb
                ref.child("messages").child(ref.push().getKey()).setValue(m);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /*
     * method to find room name grabbed from intent
     */
    private void getRoomName() {
        // get room name from intent
        roomName = getIntent().getStringExtra("extra");
        // set global var to that string
        toolbarTitle.setText(roomName);
    }
}