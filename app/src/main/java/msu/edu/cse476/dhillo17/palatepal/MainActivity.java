package msu.edu.cse476.dhillo17.palatepal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//button code from:
//        https://www.geeksforgeeks.org/handling-click-events-button-android/
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mGetReview;
    private TextView mReview;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mapButton = findViewById(R.id.button_map); // Adjust the ID to match your button
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        Button buttonCaseHall = findViewById(R.id.button_case_hall);
        Button buttonCowenHall = findViewById(R.id.button_cowen_hall);
        // Add listeners for each dining hall button
        buttonCaseHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiningHallActivity("CaseHall");
            }
        });
        buttonCowenHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiningHallActivity("CowenHall");
            }
        });


        mReview = findViewById(R.id.review);
        mGetReview = findViewById(R.id.firebase);
        mGetReview.setOnClickListener(this);

        Button sidebarButton = (Button) findViewById(R.id.button_sidebar);
        sidebarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.sidebar_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                            int id = item.getItemId();
                            if (id == R.id.my_account) {

                            } else if (id == R.id.my_reviews) {

                            } else if (id == R.id.my_dining_halls) {

                            } else if (id == R.id.my_account_settings) {

                            } else if (id == R.id.my_friends) {

                            }
                            return true;
                        }
                });
                popup.show();
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
//        myRef.child("review").setValue("Was really good");
//        myRef.child("ref_1").child("review").setValue("Was really good 4/5");
        Log.d("test", "made it");
        myRef.child("review").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    mReview.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }

    // TODO: Still need to work on this
    public void OnTargetedDiningHall(View view) {
        Intent intent = new Intent(this, DiningHallActivity.class);
        // intent.putExtra("dining_hall", diningHall);
        startActivity(intent);
    }
    private void openDiningHallActivity(String diningHallName) {
        Intent intent = new Intent(MainActivity.this, DiningHallActivity.class);
        intent.putExtra("DINING_HALL_NAME", diningHallName);
        startActivity(intent);
    }
    public void onHomeClick(View view) {
        // Do nothing to keep the user on the current page
    }

}