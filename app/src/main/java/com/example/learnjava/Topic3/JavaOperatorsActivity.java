package com.example.learnjava.Topic3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.learnjava.R;

public class JavaOperatorsActivity extends AppCompatActivity {

    TextView textToSpeak3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_java_operators);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textToSpeak3 = findViewById(R.id.textToSpeak3);

        String operatorsDescription = "Operators are used to perform operations on variables and values.\n\n" +
                "Java divides the operators into the following groups:\n\n" +
                "• Arithmetic operators\n" +
                "• Assignment operators\n" +
                "• Comparison operators\n" +
                "• Logical operators\n" +
                "• Bitwise operators";

        textToSpeak3.setText(operatorsDescription);
    }

    public void javaArithmetics(View view){
        Intent intent = new Intent(this, JavaArithmeticOperatorsActivity.class);
        startActivity(intent);
    }

}