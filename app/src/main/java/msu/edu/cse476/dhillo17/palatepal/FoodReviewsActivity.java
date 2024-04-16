package msu.edu.cse476.dhillo17.palatepal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Objects;

public class FoodReviewsActivity extends AppCompatActivity {

    private FoodReview[] foodReviews;
    private String mFoodName;
    private String mDiningHallName;
    private String mMeal;
    private String mUsername;
    private String mReviews;
    private double mRating;
    private TextView reviewsText;
    private EditText mleaveReviewEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_reviews);

        mFoodName = getIntent().getStringExtra("FoodName");
        mDiningHallName = getIntent().getStringExtra("DiningHall");
        mMeal = getIntent().getStringExtra("Meal");
        mUsername = getIntent().getStringExtra("Username");
        mRating = getIntent().getDoubleExtra("Rating", 0.0);
        mReviews = getIntent().getStringExtra("Reviews");

        reviewsText = (TextView) findViewById(R.id.no_reviews_Text);
        if(mReviews != null)
        {
            reviewsText.setText(mReviews);
            reviewsText.setGravity(Gravity.START);
        }

        TextView nameText = (TextView) findViewById(R.id.FoodItem);
        nameText.setText(mFoodName);

        mleaveReviewEditText = (EditText) findViewById(R.id.leaveReviewEditText);

        findViewById(R.id.back_arrow_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        findViewById(R.id.leave_review_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String newReviewText = mleaveReviewEditText.getText().toString();
                if(newReviewText.equals(""))
                {
                    return;
                }

                addReview(mFoodName, "\nUsername: " + mUsername +"\nReviews: " + newReviewText );
                // Save the review or perform any other action here
                Toast.makeText(getBaseContext(), "Review submitted: " + newReviewText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addReview(String dish, String review) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference diningHallRef = database.getReference("PalatePal").child("DiningHalls").child(mDiningHallName).child(mMeal).child(dish);

        // Append the new review to the existing reviews (if any)
        diningHallRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String existingReviews = dataSnapshot.getValue(String.class);
                if (existingReviews != null) {
                    // Append the new review to the existing ones, separated by a delimiter (e.g., newline)
                    String newReviews = existingReviews + "\n" + review;
                    // Update the database with the new reviews
                    diningHallRef.setValue(newReviews);
                    //updateReviewText(dish, newReviews);
                    updateReviews(newReviews);
                } else {
                    // If there are no existing reviews, just set the new review as the value
                    diningHallRef.setValue(review);
                    updateReviews(review);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });
    }

    public void updateReviews(String review)
    {
        mReviews = mReviews + "\n\n" + review;
        mleaveReviewEditText.setText("");
        reviewsText.setText(mReviews);
        // Close keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }
}

