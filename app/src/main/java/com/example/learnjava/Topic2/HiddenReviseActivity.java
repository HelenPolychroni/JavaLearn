package com.example.learnjava.Topic2;

import static com.example.learnjava.GeneralActivity.showCustomBottomDialogReviseT1;
import static com.example.learnjava.Topic3.HiddenReviseActivity.showExitConfirmationDialogRevise;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.learnjava.GeneralActivity;
import com.example.learnjava.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HiddenReviseActivity extends AppCompatActivity {

    EditText a1, a2, a3, a4;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hidden_revise2);
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

        a1 = findViewById(R.id.a1);
        a2 = findViewById(R.id.a2);
        a3 = findViewById(R.id.a3);
        a4 = findViewById(R.id.a4);
    }

    public void checkResult(View view){
        String text1, text2, text3, text4;
        text1 = a1.getText().toString().trim();
        text2 = a2.getText().toString().trim();
        text3 = a3.getText().toString().trim();
        text4 = a4.getText().toString().trim();

        if (text1.isEmpty() || text2.isEmpty() || text3.isEmpty() || text4.isEmpty()){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else{
            String reply = text1 + "," + "," +  text2 + "," + text3 + "," + text4;
            boolean flag = false;

            if (text1.equals("int") && text2.equals("float") && text3.equals("boolean") &&
                    text4.equals("String")) {

                flag = true;

                // go now solve the topic's tests
                showCustomBottomDialogReviseT1(this, "Your answer is right. You " +
                                "can now return and correctly solve the" +
                                " topic2's tests.", "check", GeneralActivity.class,
                        "Tap to continue on topic2");
            }
            else{ // play again
                showCustomBottomDialogReviseT1(this, "Your answer is wrong",
                        "cross", HiddenReviseActivity.class,
                        "Tap to take the test again");
            }
            // save score and answer in firebase
            saveScoreToFirebaseRevise(databaseReference, email, "topic2", reply, flag);
        }
    }

    public static void saveScoreToFirebaseRevise(DatabaseReference databaseReference, String email, String child1,
                                                 String reply, Boolean isCorrect) {
        System.out.println("Child1: " + child1);

        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            DatabaseReference topicRef = userSnapshot.child("scores").child(child1).getRef();

                            DatabaseReference reviseTestRef = topicRef.child("reviseTest");
                            reviseTestRef.child("reply").setValue(reply);
                            reviseTestRef.child("isCorrect").setValue(isCorrect)
                                    .addOnSuccessListener(aVoid -> System.out.println("Revise test updated successfully"))
                                    .addOnFailureListener(e -> System.out.println("Error updating revise test: " + e.getMessage()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle onCancelled event
                        System.out.println("Error querying database: " + databaseError.getMessage());
                    }
                });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitConfirmationDialogRevise(this, this);
    }
}