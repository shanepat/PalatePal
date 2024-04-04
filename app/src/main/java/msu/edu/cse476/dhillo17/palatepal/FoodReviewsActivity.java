package msu.edu.cse476.dhillo17.palatepal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class FoodReviewsActivity extends AppCompatActivity {

    private FoodReview[] foodReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_reviews);

        // Get food items and reviews from firebase
    }
}