package com.example.learnjava;

import static com.example.learnjava.JavaIntroductionActivity.saveScoreToFirebase;
import static com.example.learnjava.JavaIntroductionActivity.showExitConfirmationDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class JavaIntroductionReviseActivity extends AppCompatActivity {

    TextView textView;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    Button checkResultButton;
    RadioGroup radioGroup;
    int topic1Score;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_java_introduction_revise);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the integer variable from the intent
        topic1Score = getIntent().getIntExtra("topic1Score", 0); // 0 is the default value if the key is not found
        System.out.println("Received topic1Score from intro2: " + topic1Score);


        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");


        textView = findViewById(R.id.textView10);
        textView.setText(R.string.javaIntro_revise);

        radioButton1 = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);

        radioButton1.setText("A programming language");
        radioButton2.setText("An island");
        radioButton3.setText("A video game console");
        radioButton4.setText("An AI robot");

        checkResultButton = findViewById(R.id.checkResultButton);
        radioGroup = findViewById(R.id.radioGroup);
    }

    public void checkResultJI(View view){

        // Get the ID of the checked radio button
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (checkedRadioButtonId == -1) {
            // No radio button is checked
            Toast.makeText(JavaIntroductionReviseActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
        } else {
            // Get the checked radio button
            RadioButton radioButton = findViewById(checkedRadioButtonId);

            // Check the text of the checked radio button
            String r = "A programming language";
            String reply = radioButton.getText().toString().trim();
            boolean flag = false;
            if (radioButton.getText().toString().equals(r)) {
                flag = true;
                showCustomBottomDialog(JavaIntroductionReviseActivity.this,"Your answer is correct!", "check",
                        databaseReference, firebaseUser, topic1Score, JavaIntroductionReviseActivity2.class, 2,
                        "Tap to continue","test1", reply, flag);

            } else {
                showCustomBottomDialog(JavaIntroductionReviseActivity.this,"Your answer is wrong!", "cross",
                        databaseReference, firebaseUser, topic1Score, JavaIntroductionReviseActivity2.class, 2,
                        "Tap to continue","test1", reply, flag);
            }
        }
    }

    public static void showCustomBottomDialog(Context context, String message, String drawableName,
                                              DatabaseReference databaseReference, FirebaseUser firebaseUser,
                                              int topic1Score, Class<?> className, int l, String buttonText, String child,
                                              String reply, boolean flag) {
        // Create a dialog
        Dialog dialog = new Dialog(context);
        // Set the content view to your custom layout
        dialog.setContentView(R.layout.custom_bottom_dialog);

        // Set dialog width and height
        Objects.requireNonNull(dialog.getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        // Find views in the custom layout
        ImageView imageView = dialog.findViewById(R.id.imageView);
        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        Button button = dialog.findViewById(R.id.buttond);

        // Set properties and click listener for views
        int resourceId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        imageView.setImageResource(resourceId);
        textViewMessage.setText(message);
        button.setText(buttonText);

        // Use an array to hold the mutable value of topic1Score
        final int[] mutableScore = {topic1Score};


        button.setOnClickListener(v -> {
            Intent intent = new Intent(context, className);
            if (mutableScore[0] == l && drawableName.equals("check")) {
                System.out.println("increase score");
                mutableScore[0]++;
            }

            if (mutableScore[0]-1 == l){
                System.out.println("save score");
                saveScoreToFirebase1(databaseReference, firebaseUser.getEmail(), mutableScore[0], child, "reply", reply, "isCorrect", flag);
            }

            intent.putExtra("topic1Score", mutableScore[0]); // Add the int variable to the intent
            context.startActivity(intent);
        });

        // Show the dialog
        dialog.show();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    static void saveScoreToFirebase1 (DatabaseReference databaseReference, String email,
                                     int score, String child, String child1, String reply, String child2, Boolean isCorrect){
        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            DatabaseReference replyRef = userSnapshot.child("scores").child("topic1score").child(child).child(child1).getRef();
                            replyRef.setValue(reply);

                            DatabaseReference isCorrectRef = userSnapshot.child("scores").child("topic1score").child(child).child(child2).getRef();
                            isCorrectRef.setValue(isCorrect);

                            DatabaseReference tscoreRef = userSnapshot.child("scores").child("topic1score").child("total").getRef();
                            tscoreRef.setValue(score)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            System.out.println("Data successfully saved in " + child);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle error while saving score
                                            System.out.println("Failed to save data in " + child + e.getMessage());
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle onCancelled event
                    }
                });
    }


}