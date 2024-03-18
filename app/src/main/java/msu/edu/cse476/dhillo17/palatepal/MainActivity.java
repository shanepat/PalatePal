package msu.edu.cse476.dhillo17.palatepal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

        mReview = findViewById(R.id.review);
        mGetReview = findViewById(R.id.firebase);
        mGetReview.setOnClickListener(this);

    }

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

}