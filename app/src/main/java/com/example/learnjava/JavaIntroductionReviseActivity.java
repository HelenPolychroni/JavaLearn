package com.example.learnjava;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class JavaIntroductionReviseActivity extends AppCompatActivity {

    TextView textView;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    Button checkResultButton;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_java_introduction_revise);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.textView10);
        textView.setText(R.string.javaIntro_revise);

        radioButton1 = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);

        radioButton1.setText("A programming language");
        radioButton2.setText("An island");
        radioButton3.setText("A video game console");
        radioButton4.setText("An AI robot");

        checkResultButton = findViewById(R.id.checkResultButton);
        radioGroup = findViewById(R.id.radioGroup);
    }

    public void checkResultJI(View view){
        // Get the ID of the checked radio button
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (checkedRadioButtonId == -1) {
            // No radio button is checked
            Toast.makeText(JavaIntroductionReviseActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
        } else {
            // Get the checked radio button
            RadioButton radioButton = findViewById(checkedRadioButtonId);

            // Check the text of the checked radio button
            if (radioButton.getText().toString().equals("A programming language")) {
                // The user selected the first option
                // Perform your actions here
                showCustomBottomDialog(JavaIntroductionReviseActivity.this,"Your answer is correct!", "check");
                //showCustomSnackbar(view, "You are correct", R.drawable.check);

            } else {
                // The user selected the second option
                // Perform your actions here
                showCustomBottomDialog(JavaIntroductionReviseActivity.this,"Your answer is wrong!", "cross");
                //showCustomSnackbar(view, "You are wrong", R.drawable.cross);
            }
        }
    }

    public static void showCustomBottomDialog(Context context, String message, String drawableName) {
        // Create a dialog
        Dialog dialog = new Dialog(context);
        // Set the content view to your custom layout
        dialog.setContentView(R.layout.custom_bottom_dialog);

        // Set dialog width and height
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        // Find views in the custom layout
        ImageView imageView = dialog.findViewById(R.id.imageView);
        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        Button buttonContinue = dialog.findViewById(R.id.buttonContinue);

        // Set properties and click listener for views
        int resourceId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        imageView.setImageResource(resourceId);
        textViewMessage.setText(message);
        buttonContinue.setOnClickListener(v -> {
            Intent intent = new Intent(context, JavaIntroductionReviseActivity2.class);
            context.startActivity(intent);
        });

        // Show the dialog
        dialog.show();
    }



    private void showCustomSnackbar(View view, String message, int iconResource) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();

        TextView textView = snackbarView.findViewById(R.id.textViewMessage);
        ImageView imageView = snackbarView.findViewById(R.id.imageView);
        textView.setVisibility(View.INVISIBLE); // Hide default text view
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(iconResource);

        @SuppressLint("RestrictedApi") Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

        // Inflate custom layout into Snackbar
        View customSnackBarView = getLayoutInflater().inflate(R.layout.custom_snackbar_layout, null);
        // Add custom layout to Snackbar
        snackbarLayout.addView(customSnackBarView, 0);
        // Show the Snackbar
        snackbar.show();
    }
}