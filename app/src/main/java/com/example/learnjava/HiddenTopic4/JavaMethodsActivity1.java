package com.example.learnjava.HiddenTopic4;

import static com.example.learnjava.Topic1.JavaIntroductionActivity.saveScoreToFirebase;
import static com.example.learnjava.Topic1.JavaIntroductionActivity.showExitConfirmationDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.learnjava.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JavaMethodsActivity1 extends AppCompatActivity {

    TextView textView;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_java_methods1);
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

        textView = findViewById(R.id.textToSpeak14);
        textView.setText("A method is a block of code which only runs when it is called.\n" +
                "\n" +
                "You can pass data, known as parameters, into a method.\n" +
                "\n" +
                "Methods are used to perform certain actions, and they are also known as functions.\n" +
                "\n" +
                "Why use methods? To reuse code: define the code once, and use it many times.");
    }

    public void createMethod(View view){

        saveScoreToFirebase(databaseReference, email,"topic4","theory", "passed", "1/4");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs4", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nextActivity4", "com.example.learnjava.HiddenTopic4.CreateAMethodActivity2");
        editor.apply();

        Intent intent = new Intent(this, CreateAMethodActivity2.class);
        startActivity(intent);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog(this, this);
    }


}