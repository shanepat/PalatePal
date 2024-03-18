package msu.edu.cse476.dhillo17.palatepal;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DiningHallActivity extends AppCompatActivity {

    private TextView mFood;
    private TextView mReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dining_hall);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void getDiningHallInformation(String diningHallName) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference diningHallRef = database.getReference("dining_halls").child(diningHallName);

        diningHallRef.child("food").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting food data", task.getException());
                } else {
                    Log.d("firebase", "Food: " + String.valueOf(task.getResult().getValue()));
                    mFood.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });

        diningHallRef.child("review").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting review data", task.getException());
                } else {
                    Log.d("firebase", "Review: " + String.valueOf(task.getResult().getValue()));
                    mReview.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }
}