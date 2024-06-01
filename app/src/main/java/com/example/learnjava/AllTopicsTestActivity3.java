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

public class AllTopicsTestActivity3 extends AppCompatActivity {

    RadioGroup radioGroup2, radioGroup3;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_topics_test3);
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

        radioGroup2 = findViewById(R.id.radioGroup2);
        radioGroup3 = findViewById(R.id.radioGroup3);
    }

    public void checkResult(View view){
        // Get the ID of the checked radio button
        int checkedRadioButtonId = radioGroup2.getCheckedRadioButtonId();
        int checkedRadioButtonId2 = radioGroup3.getCheckedRadioButtonId();

        if (checkedRadioButtonId == -1 || checkedRadioButtonId2 == -1) {
            // No radio button is checked
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
        }
        else {
            // Get the checked radio button
            RadioButton radioButton = findViewById(checkedRadioButtonId);
            RadioButton radioButton2 = findViewById(checkedRadioButtonId2);

            // Check the text of the checked radio button
            String reply = radioButton.getText().toString() + "," + radioButton2.getText().toString();

            boolean flag = false;
            String className = "";

            if (radioButton.getText().toString().equals("false") &&
                    radioButton2.getText().toString().equals("false")) {

                System.out.println("Right answer");

                flag = true;

                showCustomBottomDialogR(this, "Your answer is correct!", "check",
                        databaseReference, firebaseUser, GeneralActivity.class,
                        "test3", reply, flag, "revise","1/3", "0/3");


                className = "com.example.learnjava.GeneralActivity";

            }
            else{ // wrong answer
                showCustomBottomDialogR(this, "Your answer is wrong!", "cross",
                        databaseReference, firebaseUser, GeneralActivity.class,
                        "test3", reply, flag,"revise", "1/3", "0/3");

                className = "com.example.learnjava.AllTopicsTestActivity3";
            }

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsR", Context.MODE_PRIVATE);

            // Save the modified value back to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String nextActivity = sharedPreferences.getString("nextActivityR",
                    "com.example.learnjava.AllTopicsTestActivity1");

            if (!nextActivity.equals("com.example.learnjava.AllTopicsTestActivity2") &&
                    !nextActivity.equals("com.example.learnjava.AllTopicsTestActivity1")){

                editor.putString("nextActivityR", className);
                editor.apply();
            }
        }
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog(this, this);
    }
}