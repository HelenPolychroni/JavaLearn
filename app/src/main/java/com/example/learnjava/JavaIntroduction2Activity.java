package com.example.learnjava;

import static com.example.learnjava.JavaIntroductionActivity.saveScoreToFirebase;
import static com.example.learnjava.JavaIntroductionActivity.showExitConfirmationDialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
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

public class JavaIntroduction2Activity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private TextView textToSpeak;
    int topic1Score;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String score_;


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

        // Retrieve the integer variable from the intent
        topic1Score = getIntent().getIntExtra("topic1Score", 0); // 0 is the default value if the key is not found
        System.out.println("Received topic1Score from intro: " + topic1Score);


        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        textToSpeak = findViewById(R.id.textToSpeak2);
        textToSpeak.setText(getString(R.string.java_use));

        ImageButton textToSpeechButton = findViewById(R.id.textToSpeechButton2);

        textToSpeech = new TextToSpeech(this, this);


        textToSpeechButton.setOnClickListener(v -> toggleTextToSpeech());
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

    public void javaRevise(View view){

        if (topic1Score == 1) {
            topic1Score++;
            saveScoreToFirebase(databaseReference, firebaseUser.getEmail(), topic1Score, "theory", "passed");
        }

        Intent intent = new Intent(this, JavaIntroductionReviseActivity.class);
        intent.putExtra("topic1Score", topic1Score);
        startActivity(intent);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog(this, this);
    }


}