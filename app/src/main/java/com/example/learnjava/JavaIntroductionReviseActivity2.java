package com.example.learnjava;

import static com.example.learnjava.JavaIntroductionReviseActivity.showCustomBottomDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class JavaIntroductionReviseActivity2 extends AppCompatActivity {

    TextView textView;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    Button checkResultButton;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_java_introduction_revise2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.textView16);
        textView.setText(R.string.javaIntro_revise2);

        radioButton1 = findViewById(R.id.radioButton5);
        radioButton2 = findViewById(R.id.radioButton6);
        radioButton3 = findViewById(R.id.radioButton7);
        radioButton4 = findViewById(R.id.radioButton8);

        radioButton1.setText("It is not platform-independent");
        radioButton2.setText("It is open source and free");
        radioButton3.setText("It is very popular");
        radioButton4.setText("It is fast and powerful");

        checkResultButton = findViewById(R.id.checkResultButton2);
        radioGroup = findViewById(R.id.radioGroup2);
    }

    public void checkResultJI2(View view){
        // Get the ID of the checked radio button
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (checkedRadioButtonId == -1) {
            // No radio button is checked
            Toast.makeText(JavaIntroductionReviseActivity2.this, "Please select an option", Toast.LENGTH_SHORT).show();
        } else {
            // Get the checked radio button
            RadioButton radioButton = findViewById(checkedRadioButtonId);

            // Check the text of the checked radio button
            if (radioButton.getText().toString().equals("It is not platform-independent")) {
                showCustomBottomDialog(JavaIntroductionReviseActivity2.this, "Your answer is correct!", "check");
            } else {
                showCustomBottomDialog(JavaIntroductionReviseActivity2.this, "Your answer is wrong!", "cross");
            }
        }
    }
}