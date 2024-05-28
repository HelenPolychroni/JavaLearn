package com.example.learnjava.HiddenTopic4;

import static com.example.learnjava.Topic1.JavaIntroductionActivity.showExitConfirmationDialog;
import static com.example.learnjava.Topic1.JavaIntroductionReviseActivity.showCustomBottomDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.learnjava.GeneralActivity;
import com.example.learnjava.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ExerciseReviseActivity extends AppCompatActivity {

    TextView textView;
    EditText response;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email;
    SharedPreferences sharedPreferences;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exercise_revise);
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

        sharedPreferences = getSharedPreferences("MyPrefs4", Context.MODE_PRIVATE);

        response = findViewById(R.id.response);

        textView = findViewById(R.id.textView70);
        textView.setText("static void myMethod() {\n" +
                "  System.out.println(\"I just got executed!\");\n" +
                "}\n" +
                "\n" +
                "public static void main(String[] args) {");
    }

    public void checkResultTopic4(View view){

        String response = this.response.getText().toString().trim();

        if (response.isEmpty()){
            Toast.makeText(this, "Please fill necessary field", Toast.LENGTH_SHORT).show();
        }
        else{
            boolean flag = false;
            String className;

            if (response.equals("myMethod()")){
                System.out.println("Correct");
                flag = true;

                showCustomBottomDialog(this, "Your answer is correct!", "check",
                        databaseReference, firebaseUser, GeneralActivity.class,
                        "test1", response, flag, "3/4", "", "topic4");

                className = "com.example.learnjava.GeneralActivity";
            }
            else {
                System.out.println("Wrong answer");

                showCustomBottomDialog(this, "Your answer is wrong!", "cross",
                            databaseReference, firebaseUser, GeneralActivity.class,
                            "test1", response, flag, "3/4", "", "topic4");

                    className = "com.example.learnjava.HiddenTopic4.ExerciseReviseActivity";
            }

            // Save the modified value back to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("nextActivity4", className);
            editor.apply();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog(this, this);
    }

}