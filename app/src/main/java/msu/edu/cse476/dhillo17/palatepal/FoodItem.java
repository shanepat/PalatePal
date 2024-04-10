package msu.edu.cse476.dhillo17.palatepal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * TODO: document your custom view class.
 */
public class FoodItem extends LinearLayout {

    // Food item
    private String foodName = "";
    private String topReview = "";
    private Double ratingStars = 0.0;
    private int numReviews = 0;
    private String diningHall;
    private String meal;

//    public FoodItem(Context context) {
//        super(context);
//        init(null, 0);
//    }
//
//    public FoodItem(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(attrs, 0);
//    }
//
//    public FoodItem(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        init(attrs, defStyle);
//    }

    public FoodItem(Context context, String foodName, String reviews,
                    Double ratingStars, int numReviews, String diningHall, String meal)
    {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //View sampleView = LayoutInflater.from(context).inflate(R.layout.sample_food_item, null);
        inflater.inflate(R.layout.sample_food_item, this, true);

        this.foodName = foodName;
        this.topReview = reviews;
        this.ratingStars = ratingStars;
        this.numReviews = numReviews;
        this.diningHall = diningHall;
        this.meal = meal;

        if(Objects.equals(foodName, "Burger"))
        {
            String tmep = "";
        }

        // Food name
        TextView foodNameView = (TextView)findViewById(R.id.food_name);
        foodNameView.setText(foodName);

        // Top review
        TextView topReviewView = (TextView)findViewById(R.id.top_review);
        topReviewView.setText(reviews);

        // Num of reviews
        TextView numReviewsView = (TextView)findViewById(R.id.num_reviews_text);
        if(numReviews == 0)
        {
            numReviewsView.setText(R.string.num_reviews);
        }
        else
        {
            numReviewsView.setText(Integer.toString(numReviews) + " reviews");
        }

        // Set the number of stars on the display by falling through this if block
        ImageView star1View = (ImageView)findViewById(R.id.review_star_1);
        ImageView star2View = (ImageView)findViewById(R.id.review_star_2);
        ImageView star3View = (ImageView)findViewById(R.id.review_star_3);
        ImageView star4View = (ImageView)findViewById(R.id.review_star_4);
        ImageView star5View = (ImageView)findViewById(R.id.review_star_5);

        if(ratingStars >= 4.66)
        {
            star5View.setImageResource(R.drawable.full_star);
        } else if (ratingStars >= 4.33) {
            star5View.setImageResource(R.drawable.half_star);
        }
        if(ratingStars >= 3.66)
        {
            star4View.setImageResource(R.drawable.full_star);
        } else if (ratingStars >= 3.33) {
            star4View.setImageResource(R.drawable.half_star);
        }
        if(ratingStars >= 2.66)
        {
            star3View.setImageResource(R.drawable.full_star);
        } else if (ratingStars >= 2.33) {
            star3View.setImageResource(R.drawable.half_star);
        }
        if(ratingStars >= 1.66)
        {
            star2View.setImageResource(R.drawable.full_star);
        } else if (ratingStars >= 1.33) {
            star2View.setImageResource(R.drawable.half_star);
        }
        if(ratingStars >= 1)
        {
            star1View.setImageResource(R.drawable.full_star);
        }

        findViewById(R.id.leave_review_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a custom dialog with a text field and a submit button
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Create Review");

                // Set up the input
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String reviewText = input.getText().toString().trim();
                        addReview(foodName, "\nUsername: " + reviewText );
                        // Save the review or perform any other action here
                        Toast.makeText(getContext(), "Review submitted: " + reviewText, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void addReview(String dish, String review) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference diningHallRef = database.getReference("PalatePal").child("DiningHalls").child(diningHall).child(meal).child(dish);

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

    public void updateReviews(String newReview)
    {
        TextView reviewsTextView = (TextView)findViewById(R.id.top_review);
        reviewsTextView.setText(newReview);
        invalidate();
    }

}