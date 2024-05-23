package com.example.learnjava.Topic1;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.learnjava.R;
import com.example.learnjava.StartUpActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JavaIntroductionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private TextView textToSpeak;
    int topic1Score = 0;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email, score_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_java_introduction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the integer variable from the intent
        topic1Score = getIntent().getIntExtra("topic1Score", 0); // 0 is the default value if the key is not found
        System.out.println("Received topic1Score from general: " + topic1Score);


        textToSpeak = findViewById(R.id.textToSpeak);
        textToSpeak.setText(getString(R.string.java_is));

        ImageButton textToSpeechButton = findViewById(R.id.textToSpeechButton);

        textToSpeech = new TextToSpeech(this, this);


        textToSpeechButton.setOnClickListener(v -> toggleTextToSpeech());

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        if (firebaseUser != null) {
            email = firebaseUser.getEmail();
        }
    }

    private void toggleTextToSpeech() {
        if (textToSpeech != null) {
            if (textToSpeech.isSpeaking()) {
                Toast.makeText(this, "Text to speech disabled", Toast.LENGTH_SHORT).show();

                textToSpeech.stop();
            } else {
                Toast.makeText(this, "Text to speech enabled", Toast.LENGTH_SHORT).show();
                String cleanedText = textToSpeak.getText().toString().replace("●", "");
                textToSpeech.speak(cleanedText, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Text-to-speech engine initialized successfully
            Toast.makeText(this, "Text to speech enabled", Toast.LENGTH_SHORT).show();
            String cleanedText = textToSpeak.getText().toString().replace("●", "");
            textToSpeech.speak(cleanedText, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            Toast.makeText(this, "Text to speech initialization failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    public void useJava(View view) {

        System.out.println("Topic score is: " + topic1Score);

        saveScoreToFirebase(databaseReference, email,"topic1","theory", "passed", "1/4");

        /*if (topic1Score == 0) {
            topic1Score++;
            saveScoreToFirebase(databaseReference, email, topic1Score, "theory", "passed");
        }*/
        Intent intent = new Intent(this, JavaIntroduction2Activity.class);
        intent.putExtra("topic1Score", topic1Score);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTextToSpeech();
    }

    private void stopTextToSpeech() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog(this, this);
    }

    public static void showExitConfirmationDialog(Context context, final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Quit the course?")
                .setMessage("You will lose your progress in the ongoing topic.")
                .setCancelable(false)
                .setPositiveButton("Quit", (dialog, id) -> {
                    // Finish the activity or perform action to quit the course
                    activity.finish();
                    Intent intent = new Intent(context, StartUpActivity.class);
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


    public static void saveScoreToFirebase(DatabaseReference databaseReference, String email,
                                           String child1, String child2, String reply, String setScore) {

        System.out.println("Child1: " + child1);
        System.out.println("Child2: " + child2);

        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            DatabaseReference theoryRef = userSnapshot.child("scores").child(child1).child(child2).getRef();
                            DatabaseReference tscoreRef = userSnapshot.child("scores").child(child1).child("total").getRef();

                            tscoreRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        // If 'total' does not exist, set it
                                        tscoreRef.setValue(setScore)
                                                .addOnSuccessListener(aVoid -> {
                                                    System.out.println("Score successfully saved in " + child1);
                                                    // Set theoryRef after setting total
                                                    setTheoryRef(theoryRef, reply);

                                                })
                                                .addOnFailureListener(e -> {
                                                    // Handle error while saving score
                                                    System.out.println("Failed to save score in theory: " + e.getMessage());
                                                });
                                    }
                                    else System.out.println("Total score does not exist");
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
    private static void setTheoryRef(DatabaseReference theoryRef, String reply) {
            theoryRef.setValue(reply)
                    .addOnSuccessListener(aVoid -> System.out.println("Reply successfully saved: " + reply))
                    .addOnFailureListener(e -> {
                        // Handle error while saving reply
                        System.out.println("Failed to save reply: " + e.getMessage());
                    });
        }
}
