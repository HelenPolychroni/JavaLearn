package com.example.learnjava.HiddenTopic4;

import static com.example.learnjava.Topic1.JavaIntroduction2Activity.saveScoreToFirebase;
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

public class CreateAMethodActivity2 extends AppCompatActivity {
    TextView textView, textView1, textView2;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_amethod2);
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

        textView = findViewById(R.id.textToSpeak15);
        textView.setText("A method must be declared within a class. " +
                "It is defined with the name of the method, followed by parentheses ()." +
                " Java provides some pre-defined methods, such as System.out.println(), " +
                "but you can also create your own methods to perform certain actions:");

        textView1 = findViewById(R.id.textView58);
        textView1.setText("public class Main {\n" +
                "  static void myMethod() {\n" +
                "    // code to be executed\n" +
                "  }\n" +
                "}");

        textView2 = findViewById(R.id.textView59);
        textView2.setText("Example Explained\n\n" +
                "\u25CFmyMethod() is the name of the method\n" +
                "\u25CFstatic means that the method belongs to the Main class and not an" +
                " object of the Main class. You will learn more about objects and how to " +
                "access methods through objects later in this tutorial.\n" +
                "\u25CFvoid means that this method does not have a return value.");

    }

    public void callMethod(View view){

        saveScoreToFirebase(databaseReference, email, "topic4", "1/4");

        // Modify the value
        String newValue = "com.example.learnjava.HiddenTopic4.CallAMethodActivity3";

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs4", Context.MODE_PRIVATE);

        // Save the modified value back to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nextActivity4", newValue);
        editor.apply();

        Intent intent = new Intent(this, CallAMethodActivity3.class);
        startActivity(intent);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog(this, this);
    }


}