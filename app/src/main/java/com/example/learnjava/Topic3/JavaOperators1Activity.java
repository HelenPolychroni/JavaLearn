package com.example.learnjava.Topic3;

import static com.example.learnjava.Topic1.JavaIntroductionActivity.saveScoreToFirebase;

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

public class JavaOperators1Activity extends AppCompatActivity {

    TextView textToSpeak3;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_java_operators);
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

        textToSpeak3 = findViewById(R.id.textToSpeak3);

        String operatorsDescription = "Operators are used to perform operations on variables and values.\n\n" +
                "Java divides the operators into the following groups:\n\n" +
                "• Arithmetic operators\n" +
                "• Assignment operators\n" +
                "• Comparison operators\n" +
                "• Logical operators\n" +
                "• Bitwise operators";

        textToSpeak3.setText(operatorsDescription);
    }

    public void javaArithmetics(View view){

        saveScoreToFirebase(databaseReference, email,"topic3", "theory", "passed", "1/7");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs3", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nextActivityT3", "com.example.learnjava.Topic3.JavaArithmeticOperators2Activity");
        editor.apply();

        Intent intent = new Intent(this, JavaArithmeticOperators2Activity.class);
        startActivity(intent);
    }

}