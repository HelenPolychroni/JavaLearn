package com.example.learnjava;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class JavaVariablesActivity extends AppCompatActivity {

    TextView javaVariablesTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_java_variables);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        javaVariablesTextView = findViewById(R.id.javaVariablesTextView);

        String variableDescription = "Variables are containers for storing data values.\n\n" +
                "In Java, there are different types of variables, for example:\n\n" +
                "• String - stores text, such as \"Hello\". String values are surrounded by double quotes\n" +
                "• int - stores integers (whole numbers), without decimals, such as 123 or -123\n" +
                "• float - stores floating point numbers, with decimals, such as 19.99 or -19.99\n" +
                "• char - stores single characters, such as 'a' or 'B'. Char values are surrounded by single quotes\n" +
                "• boolean - stores values with two states: true or false";

        javaVariablesTextView.setText(variableDescription);
    }
}