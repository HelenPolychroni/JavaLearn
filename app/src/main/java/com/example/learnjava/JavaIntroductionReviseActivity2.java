package com.example.learnjava;

import static com.example.learnjava.JavaIntroductionActivity.showExitConfirmationDialog;
import static com.example.learnjava.JavaIntroductionReviseActivity.showCustomBottomDialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JavaIntroductionReviseActivity2 extends AppCompatActivity {

    TextView textView;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    Button checkResultButton;
    RadioGroup radioGroup;
    int topic1Score;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String score_;
    Boolean flagActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_java_introduction_revise2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the integer variable from the intent
        topic1Score = getIntent().getIntExtra("topic1Score", 0); // 0 is the default value if the key is not found
        System.out.println("Received topic1Score from revise: " + topic1Score);


        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");


        textView = findViewById(R.id.textView16);
        textView.setText(R.string.javaIntro_revise2);

        radioButton1 = findViewById(R.id.radioButton5);
        radioButton2 = findViewById(R.id.radioButton6);
        radioButton3 = findViewById(R.id.radioButton7);
        radioButton4 = findViewById(R.id.radioButton8);

        radioButton1.setText("It is not platform-independent");
        radioButton2.setText("It is open source and free");
        radioButton3.setText("It is very popular");
        radioButton4.setText("It is fast and powerful");

        checkResultButton = findViewById(R.id.checkResultButton2);
        radioGroup = findViewById(R.id.radioGroup2);

        checkScoreEqualsTov1(databaseReference, firebaseUser.getEmail(), 3);
    }

    public void checkResultJI2(View view){

        // Get the ID of the checked radio button
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (checkedRadioButtonId == -1) {
            // No radio button is checked
            Toast.makeText(JavaIntroductionReviseActivity2.this, "Please select an option", Toast.LENGTH_SHORT).show();
        } else {
            // Get the checked radio button
            RadioButton radioButton = findViewById(checkedRadioButtonId);

            // Check the text of the checked radio button
            String reply = radioButton.getText().toString();
            boolean flag = false;

            //checkScoreEqualsTov1(databaseReference, firebaseUser.getEmail(), 3);
            System.out.println("FlagActivity is " + flagActivity);
            int l = 3;
            if (!flagActivity)
                l = 2;

            System.out.println("L is: " + l);
            if (radioButton.getText().toString().equals("It is not platform-independent")) {

                flag = true;
                showCustomBottomDialog(JavaIntroductionReviseActivity2.this, "Your answer is correct!", "check",
                        databaseReference, firebaseUser, topic1Score, GeneralActivity.class, l,
                        "Tap to continue", "test2", reply, flag);
            } else {
                showCustomBottomDialog(JavaIntroductionReviseActivity2.this, "Your answer is wrong!", "cross",
                        databaseReference, firebaseUser, topic1Score, GeneralActivity.class, l,
                        "Tap to continue", "test2", reply, flag);
            }
        }
    }

    public void checkScoreEqualsTov1(DatabaseReference databaseReference, String email, int targetScore) {
        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            if (userSnapshot.child("scores").exists()) {
                                int userScore = userSnapshot.child("scores").child("topic1score").child("total").getValue(Integer.class);
                                boolean flag1 = userSnapshot.child("scores").child("topic1score").child("test1").child("isCorrect").getValue(boolean.class);
                                if (userScore == targetScore && flag1) {
                                    System.out.println("Score in test1 is " + targetScore);
                                    flagActivity = true;
                                    break; // No need to continue if score is found
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error here, if needed
                        Log.e("DatabaseError", "Error querying database: " + databaseError.getMessage());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog(this, this);
    }

}