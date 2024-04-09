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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
    private String currentMeal = "Breakfast";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dining_hall);

        mDiningHallName = getIntent().getStringExtra("DiningHallName");


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
                getMenu(currentMeal);


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
                getMenu(currentMeal);



            }
        });

        dinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display dinner buttons layout and hide others
                LinearLayout layout = findViewById(R.id.reviewed_food_items);
                layout.removeAllViews();
                currentMeal = "Dinner";
                getMenu(currentMeal);


            }
        });



        getMenu("Breakfast");


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
    }


    private void getMenu(String meal){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase  = database.getReference("PalatePal").child("DiningHalls").child(mDiningHallName).child(meal);
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


    private void createButton(final String foodItem, final String foodReview) {
        final LinearLayout layout = findViewById(R.id.reviewed_food_items);

        // Create a horizontal LinearLayout to hold the menu item button and the "Create Review" button
        final LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Create the menu item button
        final Button button = new Button(this);
        button.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        button.setText(foodItem);
        buttonLayout.addView(button);

        // Create the "Create Review" button
        final Button reviewButton = new Button(this);
        reviewButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        reviewButton.setText("Create Review");
        buttonLayout.addView(reviewButton);

        layout.addView(buttonLayout);

        final LinearLayout reviewLayout = new LinearLayout(this);
        reviewLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        reviewLayout.setOrientation(LinearLayout.VERTICAL);
        reviewLayout.setVisibility(View.GONE);

        // Create the TextView for the review text
        TextView reviewTextView = new TextView(this);
        reviewTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        reviewTextView.setText(foodReview);  // Set the review text
        reviewTextView.setTextColor(Color.parseColor("#FFFDD0"));
        Typeface typeface = ResourcesCompat.getFont(this, R.font.lunch_fox);
        reviewTextView.setTypeface(typeface);
        reviewTextView.setTag(foodItem);  // Using foodItem as the tag
        reviewLayout.addView(reviewTextView);
        layout.addView(reviewLayout);

        // Set OnClickListener for the "Create Review" button
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a custom dialog with a text field and a submit button
                AlertDialog.Builder builder = new AlertDialog.Builder(DiningHallActivity.this);
                builder.setTitle("Create Review");

                // Set up the input
                final EditText input = new EditText(DiningHallActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String reviewText = input.getText().toString().trim();
                        addReview(foodItem, "\nUsername: " + reviewText );
                        // Save the review or perform any other action here
                        Toast.makeText(DiningHallActivity.this, "Review submitted: " + reviewText, Toast.LENGTH_SHORT).show();
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility
                if (reviewLayout.getVisibility() == View.GONE) {
                    reviewLayout.setVisibility(View.VISIBLE);
                } else {
                    reviewLayout.setVisibility(View.GONE);
                }
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
        getMenu(currentMeal);
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