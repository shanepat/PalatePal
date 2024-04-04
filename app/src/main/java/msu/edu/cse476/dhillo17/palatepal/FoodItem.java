package msu.edu.cse476.dhillo17.palatepal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * TODO: document your custom view class.
 */
public class FoodItem extends View {

    // Food item
    private String foodName = "";
    private String topReview = "";
    private Double ratingStars = 0.0;
    private int numReviews = 0;

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

    public FoodItem(Context context, String foodName, String topReview,
                    Double ratingStars, int numReviews)
    {
        super(context);

        this.foodName = foodName;
        this.topReview = topReview;
        this.ratingStars = ratingStars;
        this.numReviews = numReviews;


        // Food name
        TextView foodNameView = (TextView)findViewById(R.id.food_name);
        foodNameView.setText(foodName);

        // Top review
        TextView topReviewView = (TextView)findViewById(R.id.top_review);
        topReviewView.setText(topReview);

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

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}