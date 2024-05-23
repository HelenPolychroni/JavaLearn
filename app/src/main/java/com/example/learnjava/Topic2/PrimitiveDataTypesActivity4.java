package com.example.learnjava.Topic2;

import static com.example.learnjava.Topic1.JavaIntroductionActivity.saveScoreToFirebase;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

public class PrimitiveDataTypesActivity4 extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_primitive_data_types);
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

        TableLayout tableLayout = findViewById(R.id.tableLayout);

        String[][] data = {
                {"byte", "1 byte", "Stores whole numbers from -128 to 127"},
                {"short", "2 bytes", "Stores whole numbers from -32,768 to 32,767"},
                {"int", "4 bytes", "Stores whole numbers from -2,147,483,648 to 2,147,483,647"},
                {"long", "8 bytes", "Stores whole numbers from -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807"},
                {"float", "4 bytes", "Stores fractional numbers. Sufficient for storing 6 to 7 decimal digits"},
                {"double", "8 bytes", "Stores fractional numbers. Sufficient for storing 15 decimal digits"},
                {"boolean", "1 bit", "Stores true or false values"},
                {"char", "2 bytes", "Stores a single character/letter or ASCII values"}
        };

        for (int i = 0; i < data.length; i++) {
            TableRow tableRow = new TableRow(this);

            // Alternate row colors
            int backgroundColor;
            int textColor;
            if (i % 2 == 0) {
                backgroundColor = Color.WHITE;
                textColor = Color.BLACK;
            } else {
                backgroundColor = Color.rgb(7,59,99);
                textColor = Color.WHITE;
            }
            tableRow.setBackgroundColor(backgroundColor);

            for (String cell : data[i]) {
                TextView textView = new TextView(this);
                textView.setText(cell);
                textView.setPadding(8, 8, 8, 8);
                textView.setTextColor(textColor);
                textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                textView.setSingleLine(false); // Allow multi-line text
                textView.setHorizontallyScrolling(false); // Disable horizontal scrolling
                textView.setMaxLines(4); // Set a maximum number of lines
                textView.setEllipsize(android.text.TextUtils.TruncateAt.END); // Ellipsize if text is too long
                tableRow.addView(textView);
            }

            tableLayout.addView(tableRow);
        }
    }

    public void Revise1(View view){
        // from javaIntroduction2
        JavaIntroduction2Activity.saveScoreToFirebase(databaseReference, email,"topic2","3/6");

        Intent intent = new Intent(this, VariablesRevise1Activity.class);
        startActivity(intent);
    }
}