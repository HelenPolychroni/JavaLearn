package com.example.learnjava.Topic1;

import static com.example.learnjava.Topic1.JavaIntroductionActivity.showExitConfirmationDialog;
import static com.example.learnjava.Topic1.JavaIntroductionReviseActivity.incrementFraction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class JavaIntroduction2Activity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private TextView textToSpeak;
    int topic1Score;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String score_;

    String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_java_introduction2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        textToSpeak = findViewById(R.id.textToSpeak2);
        textToSpeak.setText(getString(R.string.java_use));

        ImageButton textToSpeechButton = findViewById(R.id.textToSpeechButton2);

        //textToSpeech = new TextToSpeech(this, this);


        //textToSpeechButton.setOnClickListener(v -> toggleTextToSpeech());
    }

    /*private void toggleTextToSpeech() {
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
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

   /* @Override
    protected void onPause() {
        super.onPause();
        stopTextToSpeech();
    }

    private void stopTextToSpeech() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }*/

    public void javaRevise(View view){

        /*if (topic1Score == 1) {
            topic1Score++;
            saveScoreToFirebase(databaseReference, firebaseUser.getEmail(), topic1Score, "theory", "passed");
        }*/
        saveScoreToFirebase(databaseReference, firebaseUser.getEmail(), "topic1", "1/4");

        // Modify the value
        String newValue = "com.example.learnjava.Topic1.JavaIntroductionReviseActivity";

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("nextActivity", "defaultValue");

        // Save the modified value back to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nextActivity", newValue);
        editor.apply();


        Intent intent = new Intent(this, JavaIntroductionReviseActivity.class);
        startActivity(intent);
    }


    public static void saveScoreToFirebase(DatabaseReference databaseReference, String email,
                                    String topic, String score) {

        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            DatabaseReference tscoreRef = userSnapshot.child("scores").child(topic).child("total").getRef();

                            tscoreRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String totalScore = snapshot.getValue(String.class);
                                        if (score.equals(totalScore)) {
                                            System.out.println("Already passed previous theory");

                                            String result = incrementFraction(totalScore);

                                            tscoreRef.setValue(result)
                                                    .addOnSuccessListener(aVoid -> {
                                                        System.out.println("Score successfully saved in " + topic + ": " + score);

                                                    })
                                                    .addOnFailureListener(e -> {
                                                        // Handle error while saving score
                                                        System.out.println("Failed to save score in theory: " + e.getMessage());
                                                    });
                                        } else {
                                            System.out.println("Total score does not exist. Skipping setting theoryRef.");
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
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


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog(this, this);
    }


}