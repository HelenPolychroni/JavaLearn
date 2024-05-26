package com.example.learnjava.Topic2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.learnjava.R;
import com.example.learnjava.Topic1.JavaIntroduction2Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JavaDataTypesActivity3 extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_java_data_types);
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
    }

    public void javaPrimitive(View view){

        // from javaIntroduction2
        JavaIntroduction2Activity.saveScoreToFirebase(databaseReference, email,"topic2","2/6");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs2", Context.MODE_PRIVATE);

        // Save the modified value back to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nextActivityT2", "com.example.learnjava.Topic2.PrimitiveDataTypesActivity4");
        editor.apply();

        Intent intent = new Intent(this, PrimitiveDataTypesActivity4.class);
        startActivity(intent);
    }
}