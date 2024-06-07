package com.example.learnjava.Topic2;

import static com.example.learnjava.Topic1.JavaIntroductionActivity.showExitConfirmationDialog;
import static com.example.learnjava.Topic1.JavaIntroductionReviseActivity.getClassFromString;
import static com.example.learnjava.Topic1.JavaIntroductionReviseActivity.getTest2IsCorrect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.learnjava.R;
import com.example.learnjava.Topic1.JavaIntroductionReviseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VariablesRevise1Activity extends AppCompatActivity {

    EditText editText6, editText7, editText8;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    static String className_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_variables_revise);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editText6 = findViewById(R.id.editTextText6);
        editText7 = findViewById(R.id.editTextText7);
        editText8 = findViewById(R.id.editTextText8);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
    }

    public void checkResult(View view){

        SharedPreferences sharedPreferencesF = getSharedPreferences("MyPrefsF", Context.MODE_PRIVATE);
        String valueF = sharedPreferencesF.getString("startOver", "false");
        System.out.println("valueF: " + valueF);

        String text6, text7, text8;
        text6 = editText6.getText().toString().trim();
        text7 = editText7.getText().toString().trim();
        text8 = editText8.getText().toString().trim();


        if (text6.isEmpty() || text7.isEmpty() || text8.isEmpty()) {

            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else{

            String reply = text6 + "," + text7 + ", " + text8;

            getTest2IsCorrect(databaseReference, firebaseUser.getEmail(),"topic2", valueF, newClassName -> {

                className_ = newClassName;
                System.out.println("New className in topic2: " + className_);

                boolean flag = false;
                String className;

                if (text6.equals("String") && text7.equals("carName") && text8.equals("\"Volvo\"")) {

                    flag = true;

                    if (className_ == null) {

                        JavaIntroductionReviseActivity.showCustomBottomDialog(VariablesRevise1Activity.this, "Your answer is correct!", "check",
                        databaseReference, firebaseUser, DataTypeRevise2Activity.class,
                        "test1", reply, flag, "1/6", "0/6", "topic2");

                        className = "com.example.learnjava.Topic2.DataTypeRevise2Activity";
                    }
                    else{

                        JavaIntroductionReviseActivity.showCustomBottomDialog(VariablesRevise1Activity.this, "Your answer is correct!", "check",
                        databaseReference, firebaseUser, getClassFromString(className_),
                        "test1", reply, flag, "1/6", "0/6", "topic2");

                        className = className_;
                    }
                }
                else {
                    if (className_ == null) {

                        JavaIntroductionReviseActivity.showCustomBottomDialog(VariablesRevise1Activity.this, "Your answer is wrong!", "cross",
                        databaseReference, firebaseUser, DataTypeRevise2Activity.class,
                        "test1", reply, flag, "1/6", "0/6", "topic2");

                        className = "com.example.learnjava.Topic2.VariablesRevise1Activity";
                    }
                    else{

                        JavaIntroductionReviseActivity.showCustomBottomDialog(VariablesRevise1Activity.this, "Your answer is wrong!", "cross",
                        databaseReference, firebaseUser, getClassFromString(className_),
                        "test1", reply, flag, "1/6", "0/6", "topic2");

                        className = className_;
                    }
                }

                System.out.println("Topic2 classname: " + className);

                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs2", Context.MODE_PRIVATE);
                // Save the modified value back to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("nextActivityT2", className);
                editor.apply();
            });
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {showExitConfirmationDialog(this, this);}
}