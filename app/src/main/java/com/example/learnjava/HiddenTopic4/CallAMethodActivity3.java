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

public class CallAMethodActivity3 extends AppCompatActivity {

    TextView textView, textView2, textView3, textView4;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_call_amethod);
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

        textView = findViewById(R.id.textToSpeak16);
        textView2 = findViewById(R.id.textView65);
        textView3 = findViewById(R.id.textView67);
        textView4 = findViewById(R.id.textView66);

        textView.setText("To call a method in Java, write the method's name followed" +
                " by two parentheses () and a semicolon;\n" +
                "\n" +
                "In the following example, myMethod() is used to print a " +
                "text (the action), when it is called:");

        textView2.setText("Example\n" +
                "Inside main, call the myMethod() method:");

        textView3.setText("public class Main {\n" +
                "  static void myMethod() {\n" +
                "    System.out.println(\"I just got executed!\");\n" +
                "  }\n" +
                "\n" +
                "  public static void main(String[] args) {\n" +
                "    myMethod();\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "// Outputs \"I just got executed!\"");

        textView4.setText("A method can also be called multiple times.");
    }

    public void revise(View view){

        saveScoreToFirebase(databaseReference, email, "topic4", "2/4");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs4", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nextActivity4", "com.example.learnjava.HiddenTopic4.ExerciseReviseActivity");
        editor.apply();

        Intent intent = new Intent(this, ExerciseReviseActivity.class);
        startActivity(intent);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog(this, this);
    }

}