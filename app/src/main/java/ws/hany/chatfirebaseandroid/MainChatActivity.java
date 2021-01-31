package ws.hany.chatfirebaseandroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainChatActivity extends AppCompatActivity {

    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        setupDisplayName();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // Link the Views in the layout to the Java code
        mInputText = findViewById(R.id.messageInput);
        ImageButton sendButton = findViewById(R.id.sendButton);
        mChatListView = findViewById(R.id.chat_list_view);

        mInputText.setOnEditorActionListener((v, actionId, event) -> {

            sendMessage();
            return true;
        });

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void setupDisplayName(){

        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE);

        mDisplayName = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY, null);

        if (mDisplayName == null) mDisplayName = "Anonymous";
    }


    private void sendMessage() {

        Log.d("TestChat", "I sent something");
        String input = mInputText.getText().toString();
        if (!input.equals("")) {
            InstantMessage chat = new InstantMessage(input, mDisplayName);
            mDatabaseReference.child("messages").push().setValue(chat);
            mInputText.setText("");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new ChatListAdapter(this, mDatabaseReference, mDisplayName);
        mChatListView.setAdapter(mAdapter);
    }


    @Override
    public void onStop() {
        super.onStop();

        mAdapter.cleanup();
    }
}