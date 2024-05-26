package com.example.learnjava.Topic3;

import static com.example.learnjava.Topic1.JavaIntroduction2Activity.saveScoreToFirebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JavaAssignmentOperators3Activity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_assignment_operators);
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

        TableLayout tableLayout = findViewById(R.id.tableLayout5);

        String[][] data = {
                {"=", "x = 5", "x = 5"},
                {"+=", "x += 3", "x = x + 3"},
                {"-=", "x -= 3", "x = x - 3"},
                {"*=", "x *= 3", "x = x * 3"},
                {"/=", "x /= 3", "x = x / 3"},
                {"%=", "x %= 3", "x = x % 3"},
                {"&=", "x &= 3", "x = x & 3"},
                {"|=", "x |= 3", "x = x | 3"},
                {"^=", "x ^= 3", "x = x ^ 3"},
                {">>=", "x >>= 3", "x = x >> 3"},
                {"<<=", "x <<= 3", "x = x << 3"}
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
                backgroundColor = Color.rgb(7, 59, 99);
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

    public void javaComparison(View view) {
        // from javaIntroduction2
        saveScoreToFirebase(databaseReference, email,"topic3","2/7");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs3", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nextActivityT3", "com.example.learnjava.Topic3.JavaComparisonOperators4Activity");
        editor.apply();

        Intent intent = new Intent(this, JavaComparisonOperators4Activity.class);
        startActivity(intent);

    }
}