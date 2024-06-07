package com.example.learnjava.Topic1;

import static com.example.learnjava.Topic1.JavaIntroductionActivity.showExitConfirmationDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    static boolean flagIntent;
    static String className_;
    static SharedPreferences sharedPreferences;
    static Dialog dialog;

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

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("nextActivity", "defaultValue");


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

        SharedPreferences sharedPreferencesF = getSharedPreferences("MyPrefsF", Context.MODE_PRIVATE);
        String valueF = sharedPreferencesF.getString("startOver", "false");
        System.out.println("valueF: " + valueF);

        // Get the ID of the checked radio button
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (checkedRadioButtonId == -1) {
            // No radio button is checked
            Toast.makeText(JavaIntroductionReviseActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
        }
        else {
            // Get the checked radio button
            RadioButton radioButton = findViewById(checkedRadioButtonId);

            // Check the text of the checked radio button
            String r = "A programming language";
            String reply = radioButton.getText().toString().trim();

            // Use the callback to handle the result of the async operation
            getTest2IsCorrect(databaseReference, firebaseUser.getEmail(),"topic1", valueF, newClassName -> {

                className_ = newClassName;
                System.out.println("New className topic1: " + className_);

                String className1;
                boolean flag = false;

                System.out.println("ClassName new t1: " + className_);

                if (radioButton.getText().toString().equals(r)) {

                    flag = true;

                    if (className_ != null){

                        showCustomBottomDialog(JavaIntroductionReviseActivity.this, "Your answer is correct!", "check",
                    databaseReference, firebaseUser, getClassFromString(className_)/*JavaIntroductionReviseActivity2.class*/,
                     "test1", reply, flag, "1/4","0/4", "topic1");

                        className1 = className_;
                    }
                    else{

                        showCustomBottomDialog(JavaIntroductionReviseActivity.this, "Your answer is correct!", "check",
                        databaseReference, firebaseUser, JavaIntroductionReviseActivity2.class,
                        "test1", reply, flag, "1/4","0/4", "topic1");

                        className1 = "com.example.learnjava.Topic1.JavaIntroductionReviseActivity2";
                    }
                }
                else {
                    if (className_ != null) {

                        showCustomBottomDialog(JavaIntroductionReviseActivity.this, "Your answer is wrong!", "cross",
                        databaseReference, firebaseUser, getClassFromString(className_),
                        "test1", reply, flag, "1/4", "0/4", "topic1");

                        className1 = className_;
                    }
                    else{
                        showCustomBottomDialog(JavaIntroductionReviseActivity.this, "Your answer is wrong!", "cross",
                        databaseReference, firebaseUser, JavaIntroductionReviseActivity2.class,
                        "test1", reply, flag, "1/4", "0/4", "topic1");

                        className1 = "com.example.learnjava.Topic1.JavaIntroductionReviseActivity";
                    }
                }
                System.out.println("Next Activity:" + className1);

                // Save the modified value back to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("nextActivity", className1);
                editor.apply();
            });
        }
    }

    // Helper method to get Class<?> from class name string
    public static Class<?> getClassFromString(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return JavaIntroductionReviseActivity2.class; // Fallback class
        }
    }

    public static void getTest2IsCorrect(DatabaseReference databaseReference, String email, String topicChild,
                                         String valueF, Test2Callback callback) {
        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            DataSnapshot test2Snapshot = userSnapshot.child("scores").child(topicChild).child("test2").child("isCorrect");
                            if (test2Snapshot.exists()) {
                                Boolean isCorrect = test2Snapshot.getValue(Boolean.class);
                                if (isCorrect != null && isCorrect && valueF.equals("false")){
                                    className_ = "com.example.learnjava.GeneralActivity";
                                }
                                System.out.println("Test 2 isCorrect: " + isCorrect);
                            } else {
                                System.out.println("Test 2 isCorrect value does not exist.");
                            }
                        }
                        callback.onTest2Result(className_);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("DatabaseError", "Error querying database: " + databaseError.getMessage());
                    }
                });
    }

    public static void showCustomBottomDialog(Context context, String message, String drawableName,
                                              DatabaseReference databaseReference, FirebaseUser firebaseUser,
                                              Class<?> className, String testChild,
                                              String reply, boolean flag, String initialScore, String zeroScore,
                                              String topicName) {
        // Create a dialog
        dialog = new Dialog(context);
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
            //saveScoreToFirebase1(databaseReference, firebaseUser.getEmail(), topicName, child, reply, flag, scoreTopic, scoreTopic2);

            saveScoreToFirebaseNew(databaseReference, firebaseUser.getEmail(), topicName, testChild,
                    reply, flag, initialScore, zeroScore, new ScoreUpdateCallback() {
                        @Override
                        public void onSuccess(String newScore) {

                            Intent intent = new Intent(context, className);
                            context.startActivity(intent);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            // Handle the error
                            Toast.makeText(context, "Failed to update score: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
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
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
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

    public static void saveScoreToFirebaseNew(DatabaseReference databaseReference, String email,
                                     String child, String testChild, String reply, Boolean isCorrect,
                                     String initialScore, String zeroScore, ScoreUpdateCallback callback) {

        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            DatabaseReference topicRef = userSnapshot.child("scores").child(child).getRef();

                            topicRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    Boolean isCorrect_ = snapshot.child("isCorrect").getValue(Boolean.class);
                                    if (isCorrect_ == null || !isCorrect_) {
                                        // Update isCorrect to true
                                        topicRef.child(testChild).child("isCorrect").setValue(isCorrect)
                                                .addOnSuccessListener(aVoid -> {
                                                    System.out.println("isCorrect successfully saved");
                                                    callback.onSuccess("isCorrect"); // Callback after successful save
                                                })
                                                .addOnFailureListener(e -> {
                                                    System.out.println("Failed to save isCorrect: " + e.getMessage());
                                                    callback.onFailure(e);
                                                });

                                        topicRef.child(testChild).child("reply").setValue(reply)
                                                .addOnSuccessListener(aVoid -> {
                                                    System.out.println("Reply successfully saved");
                                                    callback.onSuccess("reply"); // Callback after successful save
                                                })
                                                .addOnFailureListener(e -> {
                                                    System.out.println("Failed to save reply: " + e.getMessage());
                                                    callback.onFailure(e);
                                                });

                                        if (isCorrect) {
                                            // Retrieve and increment the total score
                                            String total = snapshot.child("total").getValue(String.class);

                                            if (total != null && total.contains("/")) {
                                                String[] parts = total.split("/");
                                                int currentScore = Integer.parseInt(parts[0]);
                                                int maxScore = Integer.parseInt(parts[1]);
                                                currentScore = Math.min(currentScore + 1, maxScore); // Ensure we don't exceed max score

                                                // Save the new total score
                                                String newTotal = currentScore + "/" + maxScore;
                                                topicRef.child("total").setValue(newTotal)
                                                        .addOnSuccessListener(aVoid -> {
                                                            System.out.println("Total score successfully saved");
                                                            callback.onSuccess(newTotal); // Callback after successful save
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            System.out.println("Failed to save total score: " + e.getMessage());
                                                            callback.onFailure(e);
                                                        });
                                            }
                                            else{
                                                topicRef.child("total").setValue(initialScore)
                                                        .addOnSuccessListener(aVoid -> {
                                                            System.out.println("Total score successfully saved");
                                                            callback.onSuccess(initialScore);
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            System.out.println("Failed to save total score: " + e.getMessage());
                                                            callback.onFailure(e);
                                                        });
                                            }
                                        }
                                        else {

                                            String total = snapshot.child("total").getValue(String.class);
                                            if (total == null){
                                                topicRef.child("total").setValue(zeroScore)
                                                        .addOnSuccessListener(aVoid -> {
                                                            System.out.println("Total score successfully saved");
                                                            callback.onSuccess(zeroScore);
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            System.out.println("Failed to save total score: " + e.getMessage());
                                                            callback.onFailure(e);
                                                        });
                                            }else{

                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    System.out.println("Database error: " + databaseError.getMessage());
                                    callback.onFailure(new Exception(databaseError.getMessage()));
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("Database error: " + databaseError.getMessage());
                        callback.onFailure(new Exception(databaseError.getMessage()));
                    }
                });
    }
}

