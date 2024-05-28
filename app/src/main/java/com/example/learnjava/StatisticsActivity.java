package com.example.learnjava;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private PieChart pieChart;
    private DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Initialize the PieChart
        pieChart = findViewById(R.id.pieChart);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        if (firebaseUser != null) {
            email = firebaseUser.getEmail();

            setupPieChart();
            retrieveAndSetScores();
        }
    }

    private void setupPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setTransparentCircleRadius(61f);

        // Setting description
        Description description = new Description();
        description.setText("Topics Percentages"); // Customize your description text here
        description.setTextSize(15f);
        description.setTextColor(getResources().getColor(R.color.black)); // Customize the color

        pieChart.setDescription(description);
    }

    private void retrieveAndSetScores() {
        databaseReference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String scoreT1 = userSnapshot.child("scores").child("topic1").child("total").getValue(String.class);
                            String scoreT2 = userSnapshot.child("scores").child("topic2").child("total").getValue(String.class);
                            String scoreT3 = userSnapshot.child("scores").child("topic3").child("total").getValue(String.class);
                            String scoreT4 = userSnapshot.child("scores").child("topic4").child("total").getValue(String.class);

                            List<PieEntry> entries = new ArrayList<>();
                            boolean allZero = true;

                            if (scoreT1 != null) {
                                int percentageT1 = (int) Math.ceil(Integer.parseInt(scoreT1.split("/")[0]) * 5.88);
                                entries.add(new PieEntry(percentageT1, "Topic 1"));
                                if (percentageT1 != 0) allZero = false;
                            }
                            else{
                                entries.add(new PieEntry(0, "Topic 1"));
                            }
                            if (scoreT2 != null) {
                                int percentageT2 = (int) Math.ceil(Integer.parseInt(scoreT2.split("/")[0]) * 5.88);
                                entries.add(new PieEntry(percentageT2, "Topic 2"));
                                if (percentageT2 != 0) allZero = false;
                            }
                            else{
                                entries.add(new PieEntry(0, "Topic 2"));
                            }
                            if (scoreT3 != null) {
                                int percentageT3 = (int) Math.ceil(Integer.parseInt(scoreT3.split("/")[0]) * 5.88);
                                entries.add(new PieEntry(percentageT3, "Topic 3"));
                                if (percentageT3 != 0) allZero = false;
                            }
                            else{
                                entries.add(new PieEntry(0, "Topic 3"));
                            }
                            if (scoreT4 != null) {
                                int percentageT4 = (int) Math.ceil(Integer.parseInt(scoreT4.split("/")[0]) * 5.88);
                                entries.add(new PieEntry(percentageT4, "Topic 4"));
                            }

                            PieDataSet dataSet = new PieDataSet(entries, "Scores");


                            // Set custom colors
                            List<Integer> colors = new ArrayList<>();
                            colors.add(getResources().getColor(R.color.kindagrey));
                            colors.add(getResources().getColor(R.color.kindabrown));
                            colors.add(getResources().getColor(R.color.kindadred));
                            colors.add(getResources().getColor(R.color.turquoise));
                            dataSet.setColors(colors);

                            // Increase text size
                            dataSet.setValueTextSize(16f);
                            dataSet.setValueFormatter(new PercentFormatter(pieChart));

                            PieData data = new PieData(dataSet);

                            if (allZero) {
                                pieChart.setNoDataText("No Data Available");
                                pieChart.setNoDataTextColor(Color.RED); // Change the color of the "No Data" text
                            } else {
                                pieChart.setData(data);
                            }

                            pieChart.invalidate(); // refresh
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
    }
}
