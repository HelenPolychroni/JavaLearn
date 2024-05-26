package com.example.learnjava.Topic3;

import static com.example.learnjava.Topic1.JavaIntroductionReviseActivity.showCustomBottomDialog;

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

import com.example.learnjava.GeneralActivity;
import com.example.learnjava.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OperatorsReviseActivity2 extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_operators_revise2);
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

        radioGroup = findViewById(R.id.radioGroup2);
    }

    public void checkResult(View view) {
        // Get the ID of the checked radio button
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (checkedRadioButtonId == -1) {
            // No radio button is checked
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
        } else {
            // Get the checked radio button
            RadioButton radioButton = findViewById(checkedRadioButtonId);

            // Check the text of the checked radio button
            String reply = radioButton.getText().toString();
            String className;
            boolean flag = false;

            if (radioButton.getText().toString().equals("true")) {
                System.out.println("Right answer");

                flag = true;
                showCustomBottomDialog(this, "Your answer is correct!", "check",
                        databaseReference, firebaseUser, GeneralActivity.class,
                        "test2", reply, flag, "5/7", "6/7", "topic3");

                className = "com.example.learnjava.GeneralActivity";
            } else {
                System.out.println("Wrong answer");

                showCustomBottomDialog(this, "Your answer is wrong!", "cross",
                        databaseReference, firebaseUser, GeneralActivity.class,
                        "test2", reply, flag, "5/7", "6/7", "topic3");

                className = "com.example.learnjava.Topic3.OperatorReviseActivity2";
            }

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs3", Context.MODE_PRIVATE);

            // Save the modified value back to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String nextActivity3 = sharedPreferences.getString("nextActivityT3",
                    "com.example.learnjava.Topic3.JavaOperators1Activity");

            if (!nextActivity3.equals("com.example.learnjava.Topic3.OperatorsReviseActivity1")){
                editor.putString("nextActivityT3", className);
                editor.apply();
            }
        }
    }
}