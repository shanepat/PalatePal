package msu.edu.cse476.dhillo17.palatepal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.util.Objects;

public class DiningHallActivity extends AppCompatActivity {

    private TextView mFood;
    private TextView mReview;
    private TextView mMenu;
    private double latitude = 0;
    private double longitude = 0;
    private boolean valid = false;
    private LocationManager locationManager = null;
    private ActiveListener activeListener = new ActiveListener();
    private DatabaseReference mDatabase;

    private TextView diningHallHeader = null;

    private String mDiningHallName = "Case";
    private Button breakfastButton;
    private Button lunchButton;
    private Button dinnerButton;
    private String currentMeal;
    private int alternatingColor;
    private final int lightGreen = Color.rgb(0,40,20);
    private final int darkGreen = Color.rgb(32,68,60);

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dining_hall);

        mDiningHallName = getIntent().getStringExtra("DiningHallName");

        userName = getIntent().getStringExtra("username");

        // Initialize buttons
        breakfastButton = findViewById(R.id.breakfast_button);
        lunchButton = findViewById(R.id.lunch_button);
        dinnerButton = findViewById(R.id.dinner_button);
        diningHallHeader = findViewById(R.id.DinningHall);
        diningHallHeader.setText(mDiningHallName);
        // Set click listeners for meal selector buttons
        breakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display breakfast buttons layout and hide others
                LinearLayout layout = findViewById(R.id.reviewed_food_items);
                layout.removeAllViews();
                currentMeal = "Breakfast";
                getMenu();
                updateMealUI();

            }
        });

        lunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display lunch buttons layout and hide others
                Log.d("menu", "made it");
                LinearLayout layout = findViewById(R.id.reviewed_food_items);
                layout.removeAllViews();
                currentMeal = "Lunch";
                getMenu();
                updateMealUI();


            }
        });

        dinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display dinner buttons layout and hide others
                LinearLayout layout = findViewById(R.id.reviewed_food_items);
                layout.removeAllViews();
                currentMeal = "Dinner";
                getMenu();
                updateMealUI();

            }
        });

        findViewById(R.id.back_arrow_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.e("KIKOKIKO", String.valueOf(latitude));

        // For older version of Android
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        // Default value
        currentMeal = "Dinner";
        // Get the meal based on time of day
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalTime currentTime = LocalTime.now();
            LocalTime elevenAM = LocalTime.of(11,0);
            LocalTime threePM = LocalTime.of(15,0);

            if(currentTime.isBefore(elevenAM))
            {
                currentMeal = "Breakfast";
            } else if (currentTime.isAfter(threePM)) {
                currentMeal = "Dinner";
            } else {
                currentMeal = "Lunch";
            }
        }
        updateMealUI();
        getMenu();
    }

    private void updateMealUI()
    {
        int grey = Color.rgb(38,38,38);
        int white = Color.rgb(255,255,255);
        breakfastButton.setTextColor(grey);
        lunchButton.setTextColor(grey);
        dinnerButton.setTextColor(grey);
        if(Objects.equals(currentMeal, "Breakfast")) {
            breakfastButton.setTextColor(white);
        } else if (Objects.equals(currentMeal, "Lunch")) {
            lunchButton.setTextColor(white);
        } else if (Objects.equals(currentMeal, "Dinner")) {
            dinnerButton.setTextColor(white);
        }
    }

    private void getMenu(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase  = database.getReference("PalatePal").child("DiningHalls").child(mDiningHallName).child(currentMeal);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterate through each child of the Dinner node
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Get the key (e.g., Burger, Chicken) and value (e.g., Burger was good)
                    String foodItem = childSnapshot.getKey();
                    String foodReview = childSnapshot.getValue(String.class);

                    // Create a button dynamically
                    createButton(foodItem, foodReview);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });
    }

    private void displayMenu(String mealType) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference menuRef = database.getReference("PalatePal").child("DiningHalls").child("Case").child(mealType);
        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringBuilder menuText = new StringBuilder();
                // Iterate through each child of the selected meal type node
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Append the food item and review to the menuText StringBuilder
                    menuText.append(childSnapshot.getKey()).append(": ").append(childSnapshot.getValue(String.class)).append("\n");
                }
                // Set the menu text to the TextView
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });
    }

    private int nextAlternatingColor()
    {
        if(alternatingColor == 0 || alternatingColor == darkGreen)
        {
            alternatingColor = lightGreen;
        } else if (alternatingColor == lightGreen) {
            alternatingColor = darkGreen;
        }
        return alternatingColor;
    }

    private void createButton(final String foodItem, final String foodReview) {
        final LinearLayout layout = findViewById(R.id.reviewed_food_items);
        LinearLayout foodView = new FoodItem(this, foodItem, foodReview, 5.0,
                1, mDiningHallName, currentMeal,userName);
        foodView.setBackgroundColor(nextAlternatingColor());
        layout.addView(foodView);

        foodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent foodReviewsIntent = new Intent(DiningHallActivity.this, FoodReviewsActivity.class);
                foodReviewsIntent.putExtra("FoodName", foodItem);
                foodReviewsIntent.putExtra("Reviews", foodReview);
                foodReviewsIntent.putExtra("DiningHall", mDiningHallName);
                foodReviewsIntent.putExtra("Meal", currentMeal);
                foodReviewsIntent.putExtra("Username", userName);
                foodReviewsIntent.putExtra("Rating", 5.0);
                startActivity(foodReviewsIntent);
            }

        });

    }

    public void addReview(String dish, String review) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference diningHallRef = database.getReference("PalatePal").child("DiningHalls").child(mDiningHallName).child(currentMeal).child(dish);

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
                    updateReviewText(dish, newReviews);
                } else {
                    // If there are no existing reviews, just set the new review as the value
                    diningHallRef.setValue(review);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });
    }

    private void updateReviewText(String dish, String review) {
        LinearLayout layout = findViewById(R.id.reviewed_food_items);

        // Find the review TextView by tag (assuming the dish name is unique)
        TextView reviewTextView = layout.findViewWithTag(dish);

        // Update the text of the review TextView
        if (reviewTextView != null) {
            reviewTextView.setText(review);
        }
    }

    private void refreshPage() {
        // Clear the current menu and reviews
        LinearLayout layout = findViewById(R.id.reviewed_food_items);
        layout.removeAllViews();
        mMenu.setText("");

        // Re-fetch the menu for the current meal
        getMenu();
    }



    public void getReview(View view){
        String dish = view.getTag().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference diningHallRef = database.getReference("PalatePal").child("DiningHalls").child("Case").child("Dinner");
        diningHallRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting food data", task.getException());
                } else {
                    Log.d("menu", String.valueOf(task.getResult()));
                    mMenu.setText(String.valueOf(task.getResult()));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListeners();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerListeners();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onGpsButton(View view) {
        Uri gmmIntentUri = Uri.parse(String.format("google.navigation:q=%s", mDiningHallName));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void onLocation(Location location) {
        if(location == null) {
            return;
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        valid = true;
    }


    private void registerListeners() {
        unregisterListeners();
        // Create a Criteria object
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);

        String bestAvailable = locationManager.getBestProvider(criteria, true);

        if(bestAvailable != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(bestAvailable, 500, 1, activeListener);

            // Get last location
            Location location = locationManager.getLastKnownLocation(bestAvailable);
            onLocation(location);
        }
    }
    private void unregisterListeners() {
        locationManager.removeUpdates(activeListener);
    }


    private class ActiveListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            onLocation(location);
        }
        @Override
        public void onStatusChanged(String s, int status, Bundle extras) { }
        @Override
        public void onProviderEnabled(String s) {
        }
        @Override
        public void onProviderDisabled(String s) {
            registerListeners();
        }
    }
}