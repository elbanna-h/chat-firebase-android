package ws.hany.chatfirebaseandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    // Constants
    static final String CHAT_PREFS = "ChatPrefs";
    static final String DISPLAY_NAME_KEY = "username";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;

    // Firebase instance variables
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = findViewById(R.id.register_email);
        mPasswordView = findViewById(R.id.register_password);
        mConfirmPasswordView = findViewById(R.id.register_confirm_password);
        mUsernameView = findViewById(R.id.register_username);

        // Keyboard sign in action
        mConfirmPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == 200 || id == EditorInfo.IME_NULL) {
                attemptRegistration();
                return true;
            }
            return false;
        });

        mAuth = FirebaseAuth.getInstance();
    }

    // Executed when Sign Up button is pressed.
    public void signUp(View v) {
        attemptRegistration();
    }

    private void attemptRegistration() {

        // Reset errors displayed in the form.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            createFirebaseUser();
        }
    }

    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {

        String confirmPassword = mConfirmPasswordView.getText().toString();
        return confirmPassword.equals(password) && password.length() > 4;
    }

    private void createFirebaseUser() {

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                task -> {
                    Log.d("TestChat", "createUser onComplete: " + task.isSuccessful());

                    if(!task.isSuccessful()){
                        Log.d("TestChat", "user creation failed");
                        showErrorDialog();
                    } else {
                        saveDisplayName();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        finish();
                        startActivity(intent);
                    }
                });
    }

    // Hany: Save data locally in SharedPreferences
    private void saveDisplayName() {
        String displayName = mUsernameView.getText().toString();
        SharedPreferences prefs = getSharedPreferences(CHAT_PREFS, 0);
        prefs.edit().putString(DISPLAY_NAME_KEY, displayName).apply();
    }


    private void showErrorDialog(){

        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage("Registration attempt failed")
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}