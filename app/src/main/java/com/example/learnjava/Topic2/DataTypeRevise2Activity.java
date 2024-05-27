package com.example.learnjava.Topic2;

import static com.example.learnjava.Topic1.JavaIntroductionActivity.showExitConfirmationDialog;
import static com.example.learnjava.Topic1.JavaIntroductionReviseActivity.showCustomBottomDialog;

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

import com.example.learnjava.GeneralActivity;
import com.example.learnjava.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataTypeRevise2Activity extends AppCompatActivity {

    EditText editText, editText2, editText3, editText4, editText5;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_type_revise1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");


        editText = findViewById(R.id.editTextText);
        editText2 = findViewById(R.id.editTextText2);
        editText3 = findViewById(R.id.editTextText3);
        editText4 = findViewById(R.id.editTextText4);
        editText5 = findViewById(R.id.editTextText5);
    }

    public void check(View view) {

        String text, text2, text3, text4, text5;

        text = editText.getText().toString().trim();
        text2 = editText2.getText().toString().trim();
        text3 = editText3.getText().toString().trim();
        text4 = editText4.getText().toString().trim();
        text5 = editText5.getText().toString().trim();

        boolean flag = false;

        if (text.isEmpty() || text2.isEmpty() || text3.isEmpty() || text4.isEmpty() || text5.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else{
            String reply = text + "," + "," +  text2 + "," + text3 + "," + text4 + "," + text5;
            String className;
            if (text.equals("int") && text2.equals("float") && text3.equals("char") &&
                    text4.equals("boolean") && text5.equals("String")) {

                flag = true;

                showCustomBottomDialog(this, "Your answer is correct!", "check",
                        databaseReference, firebaseUser, GeneralActivity.class,
                        "test2", reply, flag,"4/6", "5/6", "topic2");

                className = "com.example.learnjava.GeneralActivity";
            }
            else{
                showCustomBottomDialog(this, "Your answer is wrong!", "cross",
                        databaseReference, firebaseUser, GeneralActivity.class,
                        "test2", reply, flag,"4/6", "5/6", "topic2");

                className = "com.example.learnjava.Topic2.DataTypeRevise2Activity";
            }
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs2", Context.MODE_PRIVATE);

            // Save the modified value back to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String nextActivity2 = sharedPreferences.getString("nextActivityT2",
                    "com.example.learnjava.Topic2.JavaVariableActivity1");

            if (!nextActivity2.equals("com.example.learnjava.Topic2.VariablesRevise1Activity")){
                editor.putString("nextActivityT2", className);
                editor.apply();
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {showExitConfirmationDialog(this, this);}
}