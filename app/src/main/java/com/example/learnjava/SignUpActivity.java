package com.example.learnjava;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    EditText email, password;
    String email_, pass, selectedGender;
    TextInputLayout EmailLayout1;
    RadioGroup gender;
    User user = new User();

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    int selectedRadioButtonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = findViewById(R.id.email);
        EmailLayout1 = findViewById(R.id.EmailLayout1);
        password = findViewById(R.id.password);
        gender = findViewById(R.id.gender);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    public void SignUpUser(View view) {

        if (checkUserInputs()) {
            auth.createUserWithEmailAndPassword(email_, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) { // Firebase Registration Successful

                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {

                                saveAllUserInfoToFirebase(email_, selectedGender);

                            } else showToast("Null user");
                        } else {
                            // Firebase Registration Failed
                            showToast("Registration failed!");
                        }
                    });
        }
    }

    public void saveAllUserInfoToFirebase (String email, String gender){

            DatabaseReference usersRef = databaseReference.child("users").push();
            usersRef.setValue(new User(email, gender))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            showToast("Data saved successfully");

                            Intent intent = new Intent(this, GeneralActivity.class);
                            startActivity(intent);
                        } else {
                            showToast("Error saving data");
                        }
                    });
    }

    public boolean checkUserInputs(){

        boolean flag = true;

        email_ = email.getText().toString().trim();
        pass = password.getText().toString().trim();

        if (email_.isEmpty()){  // check username
            //showMessage("Error","Username cannot be null.");
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if (!isValidEmail(email_)) {
            EmailLayout1.setError("Invalid email address");
            flag = false;
        }

        if (pass.isEmpty()){
            //showMessage("Error","Password cannot be null.");
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            flag = false;

        }
        else if (pass.length() < 6){
            Toast.makeText(this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
            flag = false;
        }

        int selectedRadioButtonId = gender.getCheckedRadioButtonId();  // gender
        if (selectedRadioButtonId != -1) {
            // Find the selected RadioButton by ID
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

            // Get the text (gender) from the selected RadioButton
            selectedGender = selectedRadioButton.getText().toString();
        }
        else {
            // No RadioButton selected
            flag = false;
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
        }

        return flag;
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void showMessage(String title, String message){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .show();
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
