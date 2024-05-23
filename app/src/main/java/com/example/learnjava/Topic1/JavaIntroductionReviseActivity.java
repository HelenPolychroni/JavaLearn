package com.example.learnjava.Topic1;

import static com.example.learnjava.Topic1.JavaIntroductionActivity.showExitConfirmationDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.learnjava.R;
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

    public void checkResultJI(View view) {

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
                showCustomBottomDialog(JavaIntroductionReviseActivity.this, "Your answer is correct!", "check",
                        databaseReference, firebaseUser, JavaIntroductionReviseActivity2.class,
                         "test1", reply, flag, "2/4","", "topic1");

            } else {
                showCustomBottomDialog(JavaIntroductionReviseActivity.this, "Your answer is wrong!", "cross",
                        databaseReference, firebaseUser,  JavaIntroductionReviseActivity2.class,
                         "test1", reply, flag, "2/4", "", "topic1");
            }
        }
    }

    public static void showCustomBottomDialog(Context context, String message, String drawableName,
                                              DatabaseReference databaseReference, FirebaseUser firebaseUser,
                                              Class<?> className, String child,
                                              String reply, boolean flag, String scoreTopic, String scoreTopic2,
                                              String topicName) {
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
        button.setText("Tap to continue");

        button.setOnClickListener(v -> {
            Intent intent = new Intent(context, className);
           /* if (mutableScore[0] == l && drawableName.equals("check")) {
                System.out.println("increase score");
                mutableScore[0]++;
            }

            if (mutableScore[0] - 1 == l) {
                System.out.println("save score");
                saveScoreToFirebase1(databaseReference, firebaseUser.getEmail(), mutableScore[0], child, "reply", reply, "isCorrect", flag);
            }*/

            saveScoreToFirebase1(databaseReference, firebaseUser.getEmail(), topicName, child, reply, flag, scoreTopic, scoreTopic2);

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

    static void saveScoreToFirebase1(DatabaseReference databaseReference, String email,
                                    String child, String child1, String reply, Boolean isCorrect, String scoreTopic1, String scoreTopic2) {

        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                            DatabaseReference tscoreRef = userSnapshot.child("scores").child(child).child("total").getRef();
                            tscoreRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String totalScore = snapshot.getValue(String.class);

                                        if (scoreTopic1.equals(totalScore) || scoreTopic2.equals(totalScore)) {

                                            DatabaseReference replyRef = userSnapshot.child("scores").child(child).child(child1).child("reply").getRef();
                                            replyRef.setValue(reply);

                                            DatabaseReference isCorrectRef = userSnapshot.child("scores").child(child).child(child1).child("isCorrect").getRef();
                                            isCorrectRef.setValue(isCorrect);

                                            DatabaseReference tscoreRef = userSnapshot.child("scores").child(child).child("total").getRef();

                                            if (isCorrect){ // correct answer
                                                String result = incrementFraction(totalScore);

                                                tscoreRef.setValue(result)
                                                        .addOnSuccessListener(aVoid -> System.out.println("Data successfully saved in " + child1))
                                                        .addOnFailureListener(e -> {
                                                            // Handle error while saving score
                                                            System.out.println("Failed to save data in " + child1 + e.getMessage());
                                                        });
                                            }


                                            /*if (isCorrect && scoreTopic1.equals(totalScore) ) {
                                                //double newValue = currentValue + 0.25; // Adding 1/4
                                                tscoreRef.setValue("3/4")
                                                        .addOnSuccessListener(aVoid -> System.out.println("Data successfully saved in " + child1))
                                                        .addOnFailureListener(e -> {
                                                            // Handle error while saving score
                                                            System.out.println("Failed to save data in " + child1 + e.getMessage());
                                                        });
                                            }
                                            else if (isCorrect && "3/4".equals(totalScore)) {

                                                tscoreRef.setValue("4/4")
                                                        .addOnSuccessListener(aVoid -> System.out.println("Data successfully saved in " + child1))
                                                        .addOnFailureListener(e -> {
                                                            // Handle error while saving score
                                                            System.out.println("Failed to save data in " + child1 + e.getMessage());
                                                        });
                                            }*/
                                            else
                                                System.out.println("Score remains the same");
                                        }
                                        else {
                                            System.out.println("Total score does not exist. Skipping setting theoryRef.");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle onCancelled event
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public static String incrementFraction(String fraction) {
        // Split the fraction into numerator and denominator
        String[] parts = fraction.split("/");

        // Parse the numerator and denominator as integers
        int numerator = Integer.parseInt(parts[0]);
        int denominator = Integer.parseInt(parts[1]);

        // Increment the numerator
        numerator++;

        // Check if numerator equals denominator
        if (numerator == denominator) {
            // Reset numerator to 0 and increment denominator
            //numerator = 0;
            //denominator++;
        }

        // Format the result as a string
        return numerator + "/" + denominator;
    }

}

