package com.example.learnjava;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class JavaIntroductionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private ImageButton textToSpeechButton;
    private TextView textToSpeak;

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

        textToSpeak = findViewById(R.id.textToSpeak);
        textToSpeak.setText(getString(R.string.java_is));

        textToSpeechButton = findViewById(R.id.textToSpeechButton);

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

    public void useJava(View view) {
        Intent intent = new Intent(this, JavaIntroduction2Activity.class);
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
}