package com.example.learnjava;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.learnjava.Topic1.JavaIntroduction2Activity;
import com.example.learnjava.Topic1.JavaIntroductionActivity;
import com.example.learnjava.Topic1.JavaIntroductionReviseActivity;
import com.example.learnjava.Topic1.JavaIntroductionReviseActivity2;
import com.example.learnjava.Topic2.JavaVariablesActivity1;
import com.example.learnjava.Topic3.JavaOperators1Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class GeneralActivity extends AppCompatActivity{

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout profile, statistics, logout;
    String email, score_;
    FirebaseAuth auth;
    TextView textViewEmail;
    static TextView scoreTopic1;
    static TextView scoreTopic2;
    static TextView scoreTopic3;
    FirebaseUser firebaseUser;
    Boolean flag;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    static TextView ScoreTextView;

    static int tscore;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_general);

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");


        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);

        ScoreTextView = findViewById(R.id.ScoreTextView);

        // Update TextViews with user information
        textViewEmail = findViewById(R.id.email_menu);

        scoreTopic1 = findViewById(R.id.scoreTopic1);
        scoreTopic2 = findViewById(R.id.scoreTopic2);
        scoreTopic3 = findViewById(R.id.scoreTopic3);

        if (firebaseUser != null) {
            email = firebaseUser.getEmail();
            textViewEmail.setText(email);

            retrieveAndSetScoreTextView(databaseReference, email);

            sumTotals(databaseReference, email);
            //checkScoreEqualsTo(databaseReference, email, 3);
        }

        profile = findViewById(R.id.one);
        statistics = findViewById(R.id.two);
        logout = findViewById(R.id.three);

        menu.setOnClickListener(v -> openDrawer(drawerLayout));

        logout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(GeneralActivity.this);
            builder.setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            logout(GeneralActivity.this); // Call the logout method
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss(); // Dismiss the dialog
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    static void logout(Context context) {
        FirebaseAuth.getInstance().signOut();
        System.out.println("User signed out successfully!");
        Toast.makeText(context, "Logged out successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, GeneralActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void javaIntro(View view) {
        // depending on score navigate to the right activity
        int score = Integer.parseInt(scoreTopic1.getText().toString().split("/")[0]);
        System.out.println("Score in general is: " + score);
        Intent intent = null; // Declare intent variable here

        System.out.println("Test1 is passed " + flag);

        if (score == 0) {
            intent = new Intent(this, JavaIntroductionActivity.class);
            intent.putExtra("topic1Score", score);
            startActivity(intent);
        }
        else if (score == 1) {
            intent = new Intent(this, JavaIntroduction2Activity.class);
            intent.putExtra("topic1Score", score);
            startActivity(intent);
        }
        else if (score == 2) {
            intent = new Intent(this, JavaIntroductionReviseActivity.class);
            intent.putExtra("topic1Score", score);
            startActivity(intent);
        }
        else if (score == 3 && flag) {
            intent = new Intent(this, JavaIntroductionReviseActivity2.class);
            intent.putExtra("topic1Score", score);
            startActivity(intent);
        }
        else if (score == 3 && !flag) {
            intent = new Intent(this, JavaIntroductionReviseActivity.class);
            intent.putExtra("topic1Score", score);
            startActivity(intent);
        }
        else if (score == 4) {
            // show a message that you have successfully completed the course1 and then let them play again
            showCustomBottomDialog(this, "You have successfully finished the Introduction course",
                    "success", JavaIntroductionActivity.class,
                    "Tap to start the course again");
        }
    }

    public static void showCustomBottomDialog(Context context, String message, String drawableName,
                                               Class<?> className, String buttonText) {
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
        button.setText(buttonText);


        button.setOnClickListener(v -> {
            Intent intent = new Intent(context, className);
            context.startActivity(intent);
        });

        // Show the dialog
        dialog.show();
    }


    public static void retrieveAndSetScoreTextView(DatabaseReference databaseReference, String email) {


        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String userScoreT1 = null, userScoreT2 = null, userScoreT3 = null; // Default score if not found

                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            if (userSnapshot.child("scores").exists()) {
                                userScoreT1 = (String) userSnapshot.child("scores").child("topic1").child("total").getValue();
                                userScoreT2 = (String) userSnapshot.child("scores").child("topic2").child("total").getValue();
                                userScoreT3 = (String) userSnapshot.child("scores").child("topic3").child("total").getValue();

                                break; // Stop searching once the score is found
                            }
                        }

                        // topic1
                        if (userScoreT1 != null) {
                            scoreTopic1.setText(userScoreT1 + " Completed");
                        }
                        else scoreTopic1.setText("0/4 Completed");

                        // topic2
                        if (userScoreT2 != null) {
                            scoreTopic2.setText(userScoreT2 + " Completed");
                        }
                        else scoreTopic2.setText("0/4 Completed");

                        // topic3
                        if (userScoreT3 != null) {
                            scoreTopic3.setText(userScoreT3 + " Completed");
                        }
                        else scoreTopic3.setText("0/4 Completed");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("DatabaseError", "Error querying database: " + databaseError.getMessage());
                        // Handle database error here, if needed
                    }
                });
    }

    public void checkScoreEqualsTo(DatabaseReference databaseReference, String email, int targetScore) {
        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            if (userSnapshot.child("scores").exists()) {
                                int userScore = userSnapshot.child("scores").child("topic1").child("total").getValue(Integer.class);
                                boolean flag1 = userSnapshot.child("scores").child("topic1").child("test1").child("isCorrect").getValue(boolean.class);
                                if (userScore == targetScore && flag1) {
                                    System.out.println("Score in test1 is " + targetScore);
                                    flag = true;
                                    break; // No need to continue if score is found
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error here, if needed
                        Log.e("DatabaseError", "Error querying database: " + databaseError.getMessage());
                    }
                });
    }
    protected void onResume() {
        super.onResume();

        retrieveAndSetScoreTextView(databaseReference, email);
    }

    public void javaTopic2(View view){
        Intent intent = new Intent(this, JavaVariablesActivity1.class);
        startActivity(intent);
    }

    public void topic3(View view){
        Intent intent = new Intent(this, JavaOperators1Activity.class);
        startActivity(intent);
    }

    public static void sumTotals(DatabaseReference databaseReference, String email) {
        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            // Get the user's scores
                            DataSnapshot scoresSnapshot = userSnapshot.child("scores");

                            // Initialize the total sum
                            int totalSum = 0;

                            // Iterate through each topic and sum the totals
                            for (DataSnapshot topicSnapshot : scoresSnapshot.getChildren()) {
                                String totalStr = topicSnapshot.child("total").getValue(String.class);
                                if (totalStr != null) {
                                    // Parse the total string (format: "x/x")
                                    String[] parts = totalStr.split("/");
                                    if (parts.length == 2) {
                                        try {
                                            int total = Integer.parseInt(parts[0]);
                                            totalSum += total;
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            // Print or use the total sum
                            tscore = totalSum;
                            ScoreTextView.setText("Score " + (int) (Math.ceil(totalSum*5.88)) + "%");
                            System.out.println("Total sum for user " + email + ": " + totalSum);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle onCancelled event
                        System.out.println("Database error: " + databaseError.getMessage());
                    }
                });
    }
}

