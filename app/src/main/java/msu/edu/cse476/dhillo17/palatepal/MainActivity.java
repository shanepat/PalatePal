package msu.edu.cse476.dhillo17.palatepal;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

//button code from:
//https://www.geeksforgeeks.org/handling-click-events-button-android/

//Parts of this class are generated with ChatGPT and are explained with comments
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Declare variables in the view for later use
    private Button mGetReview;
    private TextView mReview;

    //Needed for utilizng google cloud
    private FirebaseAuth firebaseAuth;

    //For UI
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    //For saving data
    public static final String SHARED_PREFS = "sharedPrefs";
    String usernameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();

        
        //Find buttons in the view
        Button buttonCaseHall = findViewById(R.id.button_case_hall);
        Button button_wilson_hall = findViewById(R.id.button_wilson_hall);
        Button button_owen_hall = findViewById(R.id.button_owen_hall);
        Button button_shaw_hall = findViewById(R.id.button_shaw_hall);
        Button button_brody_hall = findViewById(R.id.button_brody_hall);

        // Add listeners for each dining hall button
        //This could be done more efficiently likely by doing this insid ethe XML as opposed to on create
        buttonCaseHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiningHallActivity("Case");
            }
        });

        //Buttons for each dining hall
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



        //Find UI elements in the view
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
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
            passUserData();
        } else if (menuItem.getItemId() == R.id.nav_map) {
            Intent mapIntent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(mapIntent);
            return true;
        } else if (menuItem.getItemId() == R.id.log_out) {

            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", "");
            editor.apply();

            FirebaseAuth.getInstance().signOut();
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void passUserData() {
        Intent intent = getIntent();

        usernameUser = intent.getStringExtra("username");




        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(usernameUser);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String emailFromDB = snapshot.child(usernameUser).child("email").getValue(String.class);
                    String usernameFromDB = snapshot.child(usernameUser).child("username").getValue(String.class);
                    String passwordFromDB = snapshot.child(usernameUser).child("password").getValue(String.class);
                    Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("username", usernameFromDB);
                    intent.putExtra("password", passwordFromDB);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }





    public void OnTargetedDiningHall(View view) {
        // Get the dining hall name from the clicked button's tag
        String diningHallName = view.getTag().toString();

        // Start the DiningHallActivity and pass the dining hall name as an extra
        Intent intent = new Intent(this, DiningHallActivity.class);
        intent.putExtra("DiningHallName", diningHallName);
        startActivity(intent);
    }

    private void openDiningHallActivity(String diningHallName) {
        Intent currentintent = getIntent();
        usernameUser = currentintent.getStringExtra("username");

        //Pass the diniing hall and username to the next view
        Intent intent = new Intent(MainActivity.this, DiningHallActivity.class);
        intent.putExtra("DiningHallName", diningHallName);
        intent.putExtra("username", usernameUser);
        startActivity(intent);
    }
    public void onHomeClick(View view) {
        // Do nothing to keep the user on the current page
    }

}