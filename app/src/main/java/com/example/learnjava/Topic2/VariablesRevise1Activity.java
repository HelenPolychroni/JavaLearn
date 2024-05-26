package com.example.learnjava.Topic2;

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

        String text6, text7, text8;
        text6 = editText6.getText().toString().trim();
        text7 = editText7.getText().toString().trim();
        text8 = editText8.getText().toString().trim();

        boolean flag = false;

        if (text6.isEmpty() || text7.isEmpty() || text8.isEmpty()) {

            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else{
            String className;
            String reply = text6 + "," + text7 + ", " + text8;

            if (text6.equals("String") && text7.equals("carName") && text8.equals("\"Volvo\"")) {
                flag = true;
                JavaIntroductionReviseActivity.showCustomBottomDialog(this, "Your answer is correct!", "check",
                        databaseReference, firebaseUser, DataTypeRevise2Activity.class,
                        "test1", reply, flag,"4/6", "", "topic2");

                className = "com.example.learnjava.Topic2.DataTypeRevise2Activity";
            }
            else{
                JavaIntroductionReviseActivity.showCustomBottomDialog(this, "Your answer is wrong!", "cross",
                        databaseReference, firebaseUser, DataTypeRevise2Activity.class,
                        "test1", reply, flag,"4/6", "", "topic2");

                className = "com.example.learnjava.Topic2.VariablesRevise1Activity";
            }
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs2", Context.MODE_PRIVATE);

            // Save the modified value back to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("nextActivityT2", className);
            editor.apply();
        }
    }
}