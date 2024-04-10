package msu.edu.cse476.dhillo17.palatepal;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private FirebaseAuth mFirebaseAuth;                     // Firebase Authentication
    private DatabaseReference mDatabaseRef;
    private UserAccount mUser;

    private EditText mNewPassWord,mNewUserName;
    public static final String SHARED_PREFS = "sharedPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("PalatePal");



        Button btnChange = findViewById(R.id.btn_applychange);

        mNewUserName = findViewById(R.id.et_name);
        mNewPassWord = findViewById(R.id.et_pwd);



        TextView HelloText = findViewById(R.id.hellouser);


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (mNewPassWord.getText().toString() != ""){
                    user.updatePassword(mNewPassWord.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AccountActivity.this, "Complete Update :)",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(AccountActivity.this, "Incomplete Update :(",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                if (mNewUserName.getText().toString() != ""){
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(mNewUserName.getText().toString()).build();
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                    } else {
                                        Log.e(TAG, "Failed to update user profile.", task.getException());
                                    }
                                }
                            });

                }

            }
        });


        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView= findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.my_account);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_home) {
            Intent profileIntent = new Intent(AccountActivity.this, MainActivity.class);
            startActivity(profileIntent);
        } else if (menuItem.getItemId() == R.id.my_account) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;

        } else if (menuItem.getItemId() == R.id.nav_map) {
            Intent mapIntent = new Intent(AccountActivity.this, MapsActivity.class);
            startActivity(mapIntent);
            return true;
        } else if (menuItem.getItemId() == R.id.log_out) {

            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", "");
            editor.apply();

            FirebaseAuth.getInstance().signOut();
            Intent loginIntent = new Intent(AccountActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}







//        user.updateEmail()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "User email address updated.");
//                        }
//                    }
//                });


