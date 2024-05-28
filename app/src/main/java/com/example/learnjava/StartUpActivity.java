package com.example.learnjava;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class StartUpActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();


        if (firebaseUser != null){
            Intent intent = new Intent(this, GeneralActivity.class);
            startActivity(intent);
        }
        else{
            // Delay the showing of the custom bottom sheet dialog by 3 seconds (3000 milliseconds)
            new Handler().postDelayed(() ->
                    showCustomBottomDialog(StartUpActivity.this,"Welcome to JavaLearn! " +
                            "\n\nLearnJava helps you master Java basics with three interactive topics " +
                            "and engaging tests.Track your progress and improve your skills " +
                            "step by step. Start learning Java " +
                            "today and unlock your coding potential!", "gesture"), 1200);
        }

        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.password);



    }

    public void signIn(View view) {

        if (TextUtils.isEmpty(email.getText().toString().trim()) &&
                TextUtils.isEmpty(password.getText().toString().trim())){
            Toast.makeText(StartUpActivity.this, "Login failed. Check your credentials.", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(this, GeneralActivity.class);

            auth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(StartUpActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                            startActivity(intent);
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(StartUpActivity.this, "Login failed.Check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


    public void showCustomBottomDialog(Context context, String message, String drawableName) {
        // Create a dialog
        Dialog dialog = new Dialog(context);
        // Set the content view to your custom layout
        dialog.setContentView(R.layout.custom_bottom_dialog);

        // Set dialog width and height
        Objects.requireNonNull(dialog.getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        // Find views in the custom layout
        ImageView imageView = dialog.findViewById(R.id.imageView);
        TextView textViewMessage = dialog.findViewById(R.id.textViewMessage);
        Button button = dialog.findViewById(R.id.buttond);

        // Set properties and click listener for views
        int resourceId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
        imageView.setImageResource(resourceId);
        textViewMessage.setText(message);
        textViewMessage.setGravity(Gravity.CENTER);
        button.setText("Got it!");

        button.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }


}