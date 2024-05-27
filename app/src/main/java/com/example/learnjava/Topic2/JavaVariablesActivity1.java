package com.example.learnjava.Topic2;

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

public class JavaVariablesActivity1 extends AppCompatActivity {

    TextView javaVariablesTextView;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_java_variables);
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

        javaVariablesTextView = findViewById(R.id.javaVariablesTextView);

        String variableDescription = "Variables are containers for storing data values.\n\n" +
                "In Java, there are different types of variables, for example:\n\n" +
                "• String - stores text, such as \"Hello\". String values are surrounded by double quotes\n" +
                "• int - stores integers (whole numbers), without decimals, such as 123 or -123\n" +
                "• float - stores floating point numbers, with decimals, such as 19.99 or -19.99\n" +
                "• char - stores single characters, such as 'a' or 'B'. Char values are surrounded by single quotes\n" +
                "• boolean - stores values with two states: true or false";

        javaVariablesTextView.setText(variableDescription);
    }

    public void javaDeclare(View view){

        saveScoreToFirebase(databaseReference, email,"topic2", "theory", "passed", "1/6");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs2", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nextActivityT2", "com.example.learnjava.Topic2.DeclaringVariablesActivity2");
        editor.apply();

        Intent intent = new Intent(this, DeclaringVariablesActivity2.class);
        startActivity(intent);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {showExitConfirmationDialog(this, this);}
}