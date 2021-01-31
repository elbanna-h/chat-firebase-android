package ws.hany.chatfirebaseandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = findViewById(R.id.login_email);
        mPasswordView = findViewById(R.id.login_password);

        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == 100 || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        mAuth = FirebaseAuth.getInstance();

    }

    public void signInExistingUser(View v)   {
        attemptLogin();
    }

    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this, ws.hany.chatfirebaseandroid.RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    private void attemptLogin() {

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (email.isEmpty())
            if (email.equals("") || password.equals("")) return;
        Toast.makeText(this, "Login in progress...", Toast.LENGTH_SHORT).show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {

            Log.d("FlashChat", "signInWithEmail() onComplete: " + task.isSuccessful());

            if (!task.isSuccessful()) {
                Log.d("FlashChat", "Problem signing in: " + task.getException());
                showErrorDialog();
            } else {
                Intent intent = new Intent(LoginActivity.this, MainChatActivity.class);
                finish();
                startActivity(intent);
            }

        });
    }

    private void showErrorDialog() {

        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage("There was a problem signing in")
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}