package com.example.learnjava.Topic3;

import static com.example.learnjava.GeneralActivity.showCustomBottomDialogReviseT1;
import static com.example.learnjava.Topic2.HiddenReviseActivity.saveScoreToFirebaseRevise;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

public class HiddenReviseActivity extends AppCompatActivity {

    RadioGroup radioGroup2, radioGroup3;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hidden_revise3);
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

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitConfirmationDialogRevise(this, this);
    }

    public static void showExitConfirmationDialogRevise(Context context, final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Quit the revise test?")
                .setMessage("You will lose your progress in the test and you will not be able to retake it.")
                .setCancelable(false)
                .setPositiveButton("Quit", (dialog, id) -> {
                    // Finish the activity or perform action to quit the course
                    activity.finish();
                    Intent intent = new Intent(context, GeneralActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    // Dismiss the dialog and do nothing
                    dialog.cancel();
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void checkResult(View view) {
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

            if (radioButton.getText().toString().equals("false") &&
                    radioButton2.getText().toString().equals("true")) {

                System.out.println("Right answer");

                flag = true;
                // go now solve the topic's tests
                showCustomBottomDialogReviseT1(this, "Your answer is right. You " +
                                "can now return and correctly solve the" +
                                " topic3's tests.", "check", GeneralActivity.class,
                        "Tap to continue on topic3");
                // save score and answer in firebase

            }
            else {// play again
                showCustomBottomDialogReviseT1(this, "Your answer is wrong",
                        "cross", com.example.learnjava.Topic3.HiddenReviseActivity.class,
                        "Tap to take the test again");

            }
            // save score and answer in firebase
            saveScoreToFirebaseRevise(databaseReference, email, "topic3", reply, flag);
        }
    }
}