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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * TODO: document your custom view class.
 */
public class FoodReview extends View {
    // Who left the reviews
    private String userName = "";
    // What is the text of the review
    private String reviewText = "";
    // How many stars the review gave
    private int numOfStars = 0;
    // How many likes this review has
    private int numOfLikes = 0;
    // If the current signed in user has liked this review or now
    private boolean userLikedThis = false;


//    public FoodReview(Context context) {
//        super(context);
//        init(null, 0);
//    }
//
//    public FoodReview(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(attrs, 0);
//    }
//
//    public FoodReview(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        init(attrs, defStyle);
//    }

    public FoodReview(Context content, String username, String reviewtext, int numofstars,
                      int numoflikes, boolean userlikedthis)
    {
        super(content);

        init(username, reviewtext, numofstars, numoflikes, userlikedthis);
    }

    private void init(String username, String reviewtext, int numofstars, int numoflikes,
                      boolean userlikedthis) {
        // Load attributes
        userName = username;
        reviewText = reviewtext;
        numOfStars = numofstars;
        numOfLikes = numoflikes;
        userLikedThis = userlikedthis;

        // Like button, invert liked, as calling onLiked flips it again (back to what it should be)
        userLikedThis = !userLikedThis;
        onLike(null);

        // Num of likes on review
        TextView numLikeText = (TextView)findViewById(R.id.num_reviews_text);
        if(numOfLikes == 0)
        {
            numLikeText.setText(R.string.noLikes);
        }
        else
        {
            numLikeText.setText(Integer.toString(numOfLikes) + " likes");
        }

        // Set the username of the review
        TextView userNameView = (TextView)findViewById(R.id.usernameText);
        userNameView.setText(userName);

        // Set the text of the review
        TextView reviewTextView = (TextView)findViewById(R.id.reviewTextView);
        reviewTextView.setText(reviewText);

        // Set the number of stars on the display by falling through this if block
        if(numOfStars >= 5)
        {
            ImageView star5View = (ImageView)findViewById(R.id.review_star_5);
            star5View.setImageResource(R.drawable.full_star);
        }
        if(numOfStars >= 4)
        {
            ImageView star4View = (ImageView)findViewById(R.id.review_star_4);
            star4View.setImageResource(R.drawable.full_star);
        }
        if(numOfStars >= 3)
        {
            ImageView star3View = (ImageView)findViewById(R.id.review_star_3);
            star3View.setImageResource(R.drawable.full_star);
        }
        if(numOfStars >= 2)
        {
            ImageView star2View = (ImageView)findViewById(R.id.review_star_2);
            star2View.setImageResource(R.drawable.full_star);
        }
        if(numOfStars >= 1)
        {
            ImageView star1View = (ImageView)findViewById(R.id.review_star_1);
            star1View.setImageResource(R.drawable.full_star);
        }
    }

    public void onLike(View view)
    {
        // New value is opposite of what they have
        userLikedThis = !userLikedThis;

        // Update firebase that the user likes or does not like this review

        // Set the image button to reflect their like or no like
        ImageButton likeButton = (ImageButton)findViewById(R.id.likeButton);
        if(userLikedThis)
        {
            likeButton.setImageResource(R.drawable.like);
        }
        else
        {
            likeButton.setImageResource(R.drawable.no_like);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}