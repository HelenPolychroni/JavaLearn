package com.example.learnjava.Topic3;

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

public class OperatorsReviseActivity1 extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email;
    EditText editText10, editText11, editText12, editText13, editText14;

    static String className_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_operators_revise1);
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

        editText10 = findViewById(R.id.editTextText10);
        editText11 = findViewById(R.id.editTextText11);
        editText12 = findViewById(R.id.editTextText12);
        editText13 = findViewById(R.id.editTextText13);
        editText14 = findViewById(R.id.editTextText14);
    }

    public void checkResult(View view){

        String text10, text11, text12, text13, text14;
        text10 = editText10.getText().toString().trim();
        text11 = editText11.getText().toString().trim();
        text12 = editText12.getText().toString().trim();
        text13 = editText13.getText().toString().trim();
        text14 = editText14.getText().toString().trim();


        if(text10.isEmpty() || text11.isEmpty() || text12.isEmpty() || text13.isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else{

            String reply = text10 + "," + text11 + ", " + text12 + ", " + text13 + ", " + text14;

            // Use the callback to handle the result of the async operation
            getTest2IsCorrect(databaseReference, firebaseUser.getEmail(),"topic3", newClassName -> {

                className_ = newClassName;
                System.out.println("New className: " + className_);

                String className;
                boolean flag = false;

                System.out.println("ClassName new: " + className_);


                if (text10.equals("15") && text11.equals("5") && text12.equals("50") &&
                    text13.equals("2") && text14.equals("1")) {

                    flag = true;

                    if (className_ != null) {

                        JavaIntroductionReviseActivity.showCustomBottomDialog(OperatorsReviseActivity1.this, "Your answer is correct!", "check",
                                databaseReference, firebaseUser, getClassFromString(className_),
                                "test1", reply, flag,"1/7", "0/7", "topic3");

                        className = className_;
                    }
                    else{ // null

                        JavaIntroductionReviseActivity.showCustomBottomDialog(OperatorsReviseActivity1.this, "Your answer is correct!", "check",
                                databaseReference, firebaseUser, OperatorsReviseActivity2.class,
                                "test1", reply, flag,"1/7", "0/7", "topic3");

                        className = "com.example.learnjava.Topic3.OperatorsReviseActivity2";
                    }
                }
                else { // wrong answer

                    if (className_ != null) {

                        JavaIntroductionReviseActivity.showCustomBottomDialog(OperatorsReviseActivity1.this, "Your answer is wrong!", "cross",
                                databaseReference, firebaseUser, getClassFromString(className_),
                                "test1", reply, flag, "1/7", "0/7", "topic3");

                        className = className_;
                    }
                    else {

                        JavaIntroductionReviseActivity.showCustomBottomDialog(OperatorsReviseActivity1.this, "Your answer is wrong!", "cross",
                                databaseReference, firebaseUser, OperatorsReviseActivity2.class,
                                "test1", reply, flag, "1/7", "0/7", "topic3");

                        className = "com.example.learnjava.Topic3.OperatorsReviseActivity1";
                    }
                }

                System.out.println("Next Activity T3:" + className);

                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs3", Context.MODE_PRIVATE);
                // Save the modified value back to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("nextActivityT3", className);
                editor.apply();
            });
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {showExitConfirmationDialog(this, this);}
}