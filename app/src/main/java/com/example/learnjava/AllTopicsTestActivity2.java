package com.example.learnjava;

import static com.example.learnjava.Topic1.JavaIntroductionActivity.showExitConfirmationDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.learnjava.Topic1.JavaIntroductionReviseActivity;
import com.example.learnjava.Topic1.ScoreUpdateCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AllTopicsTestActivity2 extends AppCompatActivity {

    EditText r1, r2, r3;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_topics_revise2);
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

        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
    }

    public void check(View view){
        String s1 = r1.getText().toString().trim();
        String s2 = r2.getText().toString().trim();
        String s3 = r3.getText().toString().trim();

        boolean flag = false;

        if(s1.isEmpty() || s2.isEmpty() || s3.isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else{
            String reply = s1 + ", " + s2 + "," + s3;
            String className;

            if (s1.equals("char") && s2.equals("float") && s3.equals("boolean")) {

                flag = true;
                showCustomBottomDialogR(this, "Your answer is correct!", "check",
                        databaseReference, firebaseUser, AllTopicsTestActivity3.class,
                        "test2", reply, flag, "revise", "1/3", "0/3");

                className = "com.example.learnjava.AllTopicsTestActivity3";
            }
            else{ // wrong answer
                showCustomBottomDialogR(this, "Your answer is wrong!", "cross",
                        databaseReference, firebaseUser, AllTopicsTestActivity3.class,
                        "test2", reply, flag,"revise","1/3", "0/3");

                className = "com.example.learnjava.AllTopicsTestActivity2";
            }

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsR", Context.MODE_PRIVATE);

            // Save the modified value back to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String nextActivity = sharedPreferences.getString("nextActivityR",
                    "com.example.learnjava.AllTopicsTestActivity1");

            if (!nextActivity.equals("com.example.learnjava.AllTopicsTestActivity1")){
                editor.putString("nextActivityR", className);
                editor.apply();
            }
        }
    }

    static void saveScoreToFirebaseR(DatabaseReference databaseReference, String email,
                                     String child, String testChild, String reply, Boolean isCorrect) {

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
                                        topicRef.child(testChild).child("isCorrect").setValue(isCorrect);
                                        topicRef.child(testChild).child("reply").setValue(reply);

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
                                                topicRef.child("total").setValue(newTotal);
                                            }else{
                                                topicRef.child("total").setValue("1/3");
                                            }
                                        }
                                        else {
                                            String total = snapshot.child("total").getValue(String.class);
                                            if (total == null){
                                                topicRef.child("total").setValue("0/3");
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle possible errors.
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    public static void showCustomBottomDialogR(Context context, String message, String drawableName,
                                               DatabaseReference databaseReference, FirebaseUser firebaseUser,
                                               Class<?> className, String testChild,
                                               String reply, boolean flag, String topicName,
                                               String initialScore, String zeroScore) {
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

            JavaIntroductionReviseActivity.saveScoreToFirebaseNew(databaseReference, firebaseUser.getEmail(), topicName, testChild,
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

            //saveScoreToFirebaseR(databaseReference, firebaseUser.getEmail(), topicName, testChild, reply, flag);
        });

        // Show the dialog
        dialog.show();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog(this, this);
    }

}