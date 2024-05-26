package com.example.learnjava;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
    ImageView menu, imageViewT1, imageViewT2, imageViewT3;
    LinearLayout profile, statistics, logout;
    String email, score_;
    FirebaseAuth auth;
    TextView textViewEmail;
    static TextView scoreTopic1;
    static TextView scoreTopic2;
    static TextView scoreTopic3;
    FirebaseUser firebaseUser;
    static Boolean flagT1T1, flagT1T2, flagT2T1, flagT2T2, flagT3T1, flagT3T2;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    static TextView ScoreTextView;

    static int tscore;
    boolean flag;

    Class<?> className;
    SharedPreferences sharedPreferences, sharedPreferences2, sharedPreferences3;
    ImageButton topic1, topic2, topic3;
    String nextActivity, nextActivity2, nextActivity3;
    Class<?> nextActivityClass, nextActivityClass2, nextActivityClass3;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_general);

        imageViewT1 = findViewById(R.id.imageView5);
        imageViewT2 = findViewById(R.id.imageView8);
        imageViewT3 = findViewById(R.id.imageView10);

        // TOPIC1
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String defaultV = "com.example.learnjava.Topic1.JavaIntroductionActivity";
        nextActivity = sharedPreferences.getString("nextActivity", defaultV);

        System.out.println("Next activity is " + nextActivity);

        if (!nextActivity.equals("com.example.learnjava.GeneralActivity")) {
            try {
                nextActivityClass = Class.forName(nextActivity);
                System.out.println("Next activity class is " + nextActivityClass);
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
                // Handle the exception
            }
        }

        // TOPIC2
        sharedPreferences2 = getSharedPreferences("MyPrefs2", Context.MODE_PRIVATE);
        String defaultV2 = "com.example.learnjava.Topic2.JavaVariablesActivity1";
        nextActivity2 = sharedPreferences2.getString("nextActivityT2", defaultV2);

        System.out.println("\nNext activity T2 is " + nextActivity2);

        if (!nextActivity2.equals("com.example.learnjava.GeneralActivity")) {
            try {
                nextActivityClass2 = Class.forName(nextActivity2);
                System.out.println("Next activity T2 class is " + nextActivityClass2);
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
                // Handle the exception
            }
        }

        // TOPIC3
        sharedPreferences3 = getSharedPreferences("MyPrefs3", Context.MODE_PRIVATE);
        String defaultV3 = "com.example.learnjava.Topic3.JavaOperators1Activity";
        nextActivity3 = sharedPreferences3.getString("nextActivityT3", defaultV3);

        System.out.println("\nNext activity T3 is " + nextActivity3);

        if (!nextActivity3.equals("com.example.learnjava.GeneralActivity")) {
            try {
                nextActivityClass3 = Class.forName(nextActivity3);
                System.out.println("Next activity T3 class is " + nextActivityClass3);
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
                // Handle the exception
            }
        }

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/


        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        topic1 = findViewById(R.id.topic1);
        topic2 = findViewById(R.id.topic2);
        topic3 = findViewById(R.id.topic3);

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

            retrieveAndSetScoreTextView(databaseReference, email, new ScoreCallback() {
                @Override
                public void onScoreRetrieved(String userScoreT1, String userScoreT2, String userScoreT3) {
                    // Use the retrieved scores in another function
                    int scoreT1 = extractFirstValueAsInt(userScoreT1);
                    int scoreT2 = extractFirstValueAsInt(userScoreT2);

                    handleButtons(scoreT1, scoreT2);
                }
            });

            sumTotals(databaseReference, email);
            //checkScoreEqualsTo(databaseReference, email, 3);
        }

        System.out.println("Score topic1 " + scoreTopic1.getText().toString());
        System.out.println("Score topic2 " + scoreTopic2.getText().toString());
        System.out.println("Score topic3 " + scoreTopic3.getText().toString());

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

    public void handleButtons(int scoreT1, int scoreT2){

        if (scoreT1 < 2) {
            topic1.setEnabled(true);

            topic2.setEnabled(false);
            topic3.setEnabled(false);
        }
        else if (scoreT2 < 4) {
            topic1.setEnabled(true);
            topic2.setEnabled(true);

            topic3.setEnabled(false);
        }
        else{
            topic1.setEnabled(true);
            topic2.setEnabled(true);
            topic3.setEnabled(true);
        }

    }

    private int extractFirstValueAsInt(String input) {
        // Split the string at the "/" character
        System.out.println("Input is " + input);
        String[] parts = input.split("/");

        // Check if the split operation was successful
        if (parts.length > 0) {
            // Return the first part of the split string as an integer
            try {
                return Integer.parseInt(parts[0].trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Handle the error or return a default value
                return -1; // or any default value
            }
        }

        // Return a default value or handle the error as needed
        return -1; // or any default value
    }

    static void logout(Context context) {
        FirebaseAuth.getInstance().signOut();
        System.out.println("User signed out successfully!");
        Toast.makeText(context, "Logged out successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, StartUpActivity.class);
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

    public void javaIntro(View view) throws ClassNotFoundException {

        if (!Objects.equals(nextActivity, "com.example.learnjava.GeneralActivity")){

            Intent intent = new Intent(this, nextActivityClass);
            startActivity(intent);
        }

        /*if (!className.equals(GeneralActivity.class)) {

            intent = new Intent(this, className);
            startActivity(intent);

        }

        if (className.equals(GeneralActivity.class) && scoreTopic1.getText().toString().equals("4/4 Completed")
        && !scoreTopic2.getText().toString().equals("4/4 Completed")) {

            // show a message that you have successfully completed the course1 and then let them play again
            showCustomBottomDialog(this, "You have successfully finished the Introduction course",
                    "success", JavaIntroductionActivity.class,
                    "Tap to start the course again");
        }
        if (className.equals(GeneralActivity.class) && scoreTopic1.getText().toString().equals("4/4 Completed")
                && scoreTopic2.getText().toString().equals("6/6 Completed")
                && scoreTopic3.getText().toString().equals("7/7 Completed")) {

            // show a message that you have successfully completed the course1 and then let them play again
            showCustomBottomDialog(this, "Congratulations, you have successfully finished all courses",
                    "success", JavaIntroductionActivity.class,
                    "Tap to start the course again");
        }*/

     /*   if (className.equals(GeneralActivity.class) && scoreTopic2.getText().toString().equals("6/6 Completed")
                && !scoreTopic3.getText().toString().equals("7/7 Completed")) {

            // show a message that you have successfully completed the course1 and then let them play again
            showCustomBottomDialog(this, "You have successfully finished the Introduction course",
                    "success", JavaVariablesActivity1.class,
                    "Tap to start the course again");
        }

        */

        /*if (score == 0) {
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
        }*/
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


    public void javaTopic2(View view){

        if (!Objects.equals(nextActivity2, "com.example.learnjava.GeneralActivity")){

            Intent intent = new Intent(this, nextActivityClass2);
            startActivity(intent);
        }

    }

    public void topic3(View view){

        if (!Objects.equals(nextActivity3, "com.example.learnjava.GeneralActivity")){

            Intent intent = new Intent(this, nextActivityClass3);
            startActivity(intent);
        }
    }

    protected void onResume() {
        super.onResume();

        retrieveAndSetScoreTextView(databaseReference, email, new ScoreCallback() {
            @Override
            public void onScoreRetrieved(String userScoreT1, String userScoreT2, String userScoreT3) {
                // Use the retrieved scores in another function
                int scoreT1 = extractFirstValueAsInt(userScoreT1);
                int scoreT2 = extractFirstValueAsInt(userScoreT2);

                handleButtons(scoreT1, scoreT2);
            }
        });
    }

    public static void retrieveAndSetScoreTextView(DatabaseReference databaseReference, String email,
                                                   ScoreCallback callback) {


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

                        // Pass the retrieved scores to the callback
                        callback.onScoreRetrieved(scoreTopic1.getText().toString(), scoreTopic2.getText().toString(),
                                scoreTopic3.getText().toString());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("DatabaseError", "Error querying database: " + databaseError.getMessage());
                        // Handle database error here, if needed
                    }
                });
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



    public void checkScoreEqualsTo(DatabaseReference databaseReference, String email, int targetScore,
                                   String topicNum, Boolean flagT1, Boolean flagT2) {
        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            if (userSnapshot.child("scores").exists()) {
                                int userScore = userSnapshot.child("scores").child(topicNum).child("total").getValue(Integer.class);
                                boolean flag1 = userSnapshot.child("scores").child(topicNum).child("test1").child("isCorrect").getValue(boolean.class);
                                if (userScore == targetScore && flag1) {
                                    System.out.println("Score in test1 is " + targetScore);
                                    // flag = true;
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

    public void checkScoreEqualsTo2(DatabaseReference databaseReference, String email,
                                    String topicNum) {

        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            if (userSnapshot.child("scores").exists()) {
                                int userScore = userSnapshot.child("scores").child(topicNum).child("total").getValue(Integer.class);
                                boolean flag1 = userSnapshot.child("scores").child(topicNum).child("test1").child("isCorrect").getValue(boolean.class);

                                if (userScore == 2/4 && !flag1) {
                                    System.out.println("Again in test1");
                                    flagT1T1 = false;
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
}

