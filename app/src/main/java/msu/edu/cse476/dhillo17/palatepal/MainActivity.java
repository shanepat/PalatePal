package msu.edu.cse476.dhillo17.palatepal;

import msu.edu.cse476.dhillo17.palatepal.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//button code from:
//        https://www.geeksforgeeks.org/handling-click-events-button-android/
public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private Button mGetReview;
    private TextView mReview;
    FirebaseAuth firebaseAuth;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        Button buttonCaseHall = findViewById(R.id.button_case_hall);
        Button button_wilson_hall = findViewById(R.id.button_wilson_hall);
        Button button_owen_hall = findViewById(R.id.button_owen_hall);
        Button button_shaw_hall = findViewById(R.id.button_shaw_hall);
        Button button_brody_hall = findViewById(R.id.button_brody_hall);

        // Add listeners for each dining hall button
        buttonCaseHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiningHallActivity("Case");
            }
        });

        button_wilson_hall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiningHallActivity("Wilson");
            }
        });
        button_owen_hall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiningHallActivity("Owen");
            }
        });
        button_shaw_hall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiningHallActivity("Shaw");
            }
        });
        button_brody_hall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiningHallActivity("Brody");
            }
        });



        mReview = findViewById(R.id.review);
        mGetReview = findViewById(R.id.firebase);
        mGetReview.setOnClickListener(this);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


    }
    @Override
    public void onBackPressed()  {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_home) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (menuItem.getItemId() == R.id.my_account) {
            Intent profileIntent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(profileIntent);
        } else if (menuItem.getItemId() == R.id.nav_map) {
            Intent mapIntent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(mapIntent);
            return true;
        } else if (menuItem.getItemId() == R.id.log_out) {
            firebaseAuth.signOut();
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
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
    //chat
    public void OnTargetedDiningHall(View view) {
        // Get the dining hall name from the clicked button's tag
        String diningHallName = view.getTag().toString();

        // Start the DiningHallActivity and pass the dining hall name as an extra
        Intent intent = new Intent(this, DiningHallActivity.class);
        intent.putExtra("DiningHallName", diningHallName);
        startActivity(intent);
    }

    private void openDiningHallActivity(String diningHallName) {

        Intent intent = new Intent(MainActivity.this, DiningHallActivity.class);
        intent.putExtra("DiningHallName", diningHallName);
        startActivity(intent);
    }
    public void onHomeClick(View view) {
        // Do nothing to keep the user on the current page
    }

}