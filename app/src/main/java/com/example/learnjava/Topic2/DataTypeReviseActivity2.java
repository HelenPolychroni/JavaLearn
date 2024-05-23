package com.example.learnjava.Topic2;

import static com.example.learnjava.Topic1.JavaIntroductionReviseActivity.showCustomBottomDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class DataTypeReviseActivity2 extends AppCompatActivity {

    EditText editText, editText1, editText2, editText3, editText4;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    int topic1Score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_type_revise1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");


        editText = findViewById(R.id.editTextText);
        editText1 = findViewById(R.id.editTextText);
        editText2 = findViewById(R.id.editTextText2);
        editText3 = findViewById(R.id.editTextText3);
        editText4 = findViewById(R.id.editTextText4);
    }

    public void check(View view) {

        String text, text1, text2, text3, text4;

        text = editText.getText().toString().trim();
        text1 = editText1.getText().toString().trim();
        text2 = editText2.getText().toString().trim();
        text3 = editText3.getText().toString().trim();
        text4 = editText4.getText().toString().trim();

        boolean flag = false;

        if (text1.isEmpty() || text2.isEmpty() || text3.isEmpty() || text4.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else{
            String reply = text + "," + text1 + "," +  text2 + "," + text3 + "," + text4;
            if (text.equals("int") && text1.equals("float") && text2.equals("char") &&
                    text3.equals("boolean") && text4.equals("String")) {

                flag = true;
                /*showCustomBottomDialog(this,"Your answer is correct!", "check",
                        databaseReference, firebaseUser, DataTypeReviseActivity1.class,
                        "Tap to continue","test1", reply, flag);*/

                showCustomBottomDialog(this, "Your answer is correct!", "check",
                        databaseReference, firebaseUser, DataTypeReviseActivity2.class,
                        "test1", reply, flag,"4/6", "5/6", "topic2");
            }
            else{
                /*showCustomBottomDialog(this,"Your answer is wrong!", "cross",
                        databaseReference, firebaseUser,  DataTypeReviseActivity1.class,
                        "Tap to continue","test1", reply, flag);*/

                showCustomBottomDialog(this, "Your answer is wrong!", "cross",
                        databaseReference, firebaseUser, DataTypeReviseActivity2.class,
                        "test1", reply, flag,"4/6", "5/6", "topic2");
            }
        }

    }

    /*public static void showCustomBottomDialog(Context context, String message, String drawableName,
                                              DatabaseReference databaseReference, FirebaseUser firebaseUser,
                                              int topic1Score, Class<?> className, int l, String buttonText, String child,
                                              String reply, boolean flag) {
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

        // Use an array to hold the mutable value of topic1Score
        final int[] mutableScore = {topic1Score};


        button.setOnClickListener(v -> {

            saveScoreToFirebase1(databaseReference, firebaseUser.getEmail(),child, "reply", reply, "isCorrect", flag, scoreTopic2);


            Intent intent = new Intent(context, className);
            context.startActivity(intent);
        });

        // Show the dialog
        dialog.show();
    }*/
}