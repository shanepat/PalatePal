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
    private String reviews = "";
    private Double ratingStars = 0.0;
    private int numReviews = 0;
    private String diningHall;
    private String meal;
    private String username;

    public FoodItem(Context context, String foodName, String reviews,
                    Double ratingStars, int numReviews, String diningHall, String meal,String userName)
    {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.sample_food_item, this, true);

        this.foodName = foodName;
        this.reviews = reviews;
        this.ratingStars = ratingStars;
        this.numReviews = numReviews;
        this.diningHall = diningHall;
        this.meal = meal;
        this.username = userName;


        // Food name
        TextView foodNameView = (TextView)findViewById(R.id.food_name);
        foodNameView.setText(foodName);

        String[] parts = reviews.split("\n");
        for (int i = 0; i < parts.length; i++) {
            if (Objects.equals(parts[i], ""))
            {
                break;
            }
            this.topReview = this.topReview + "\n" + parts[i];
        }

        // Top review
        TextView topReviewView = (TextView)findViewById(R.id.top_review);
        topReviewView.setText(this.topReview);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}