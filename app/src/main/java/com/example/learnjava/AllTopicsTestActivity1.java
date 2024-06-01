package com.example.learnjava;

import static com.example.learnjava.AllTopicsTestActivity2.showCustomBottomDialogR;
import static com.example.learnjava.Topic1.JavaIntroductionActivity.showExitConfirmationDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllTopicsTestActivity1 extends AppCompatActivity {

    RadioGroup radioGroup;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_topics_test1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        if (firebaseUser != null) {
            email = firebaseUser.getEmail();
        }

        radioGroup = findViewById(R.id.radioGroupRevise);
    }

    public void checkResult(View view) {

        // Get the ID of the checked radio button
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (checkedRadioButtonId == -1) {
            // No radio button is checked
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
        }
        else {
            // Get the checked radio button
            RadioButton radioButton = findViewById(checkedRadioButtonId);

            // Check the text of the checked radio button
            String reply = radioButton.getText().toString();
            String className;
            boolean flag = false;

            if (radioButton.getText().toString().equals("true")) {
                System.out.println("Right answer");

                flag = true;
                showCustomBottomDialogR(this, "Your answer is correct!", "check",
                        databaseReference, firebaseUser, AllTopicsTestActivity2.class,
                        "test1", reply, flag, "revise", "1/3", "0/3");

                className = "com.example.learnjava.AllTopicsTestActivity2";
            }
            else {
                System.out.println("Wrong answer");

                showCustomBottomDialogR(this, "Your answer is wrong!", "cross",
                        databaseReference, firebaseUser, AllTopicsTestActivity2.class,
                        "test1", reply, flag, "revise", "1/3", "0/3");

                className = "com.example.learnjava.AllTopicsTestActivity1";
            }

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsR", Context.MODE_PRIVATE);

            // Save the modified value back to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("nextActivityR", className);
            editor.apply();

        }
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog(this, this);
    }
}