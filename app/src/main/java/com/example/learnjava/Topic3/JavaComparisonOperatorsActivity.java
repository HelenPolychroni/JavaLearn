package com.example.learnjava.Topic3;

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

public class JavaComparisonOperatorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_java_comparison_operators);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TableLayout tableLayout = findViewById(R.id.tableLayout5);

        String[][] data = {
                {"==", "Equal to", "x == y"},
                {"!=", "Not equal", "x != y"},
                {">", "Greater than", "x > y"},
                {"<", "Less than", "x < y"},
                {">=", "Greater than or equal to", "x >= y"},
                {"<=", "Less than or equal to", "x <= y"}
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

    public void javaLogical(View view){
        Intent intent = new Intent(this, JavaLogicalOperatorsActivity.class);
        startActivity(intent);
    }
}