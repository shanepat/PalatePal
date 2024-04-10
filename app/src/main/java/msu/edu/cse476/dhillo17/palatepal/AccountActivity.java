package msu.edu.cse476.dhillo17.palatepal;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private DatabaseReference mDatabaseRef;

    TextView  profileEmail, profileUsername, profilePassword;

    public static final String SHARED_PREFS = "sharedPrefs";
    private EditText editEmail, editPassword;
    private String emailUser, usernameUser, passwordUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        Button btnPwdChange = findViewById(R.id.btn_pwdchange);
        Button btnEmailChange = findViewById(R.id.btn_emailchange);

        editEmail = findViewById(R.id.et_email);

        editPassword = findViewById(R.id.et_pwd);


        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUserName);
        profilePassword = findViewById(R.id.profilePwd);

        showAllUserData();





        btnEmailChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmailChanged()){
                    Toast.makeText(AccountActivity.this, "New Email Saved", Toast.LENGTH_SHORT).show();
                    passUserData();

                } else {
                    Toast.makeText(AccountActivity.this, "No Changes Found", Toast.LENGTH_SHORT).show();
                }
            }


        });

        btnPwdChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPasswordChanged()){
                    Toast.makeText(AccountActivity.this, "New Password Saved", Toast.LENGTH_SHORT).show();
                    passUserData();

                } else {
                    Toast.makeText(AccountActivity.this, "No Changes Found", Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(AccountActivity.this, MainActivity.class);
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

    public void showAllUserData(){

        Intent intent = getIntent();

        emailUser = intent.getStringExtra("email");
        usernameUser = intent.getStringExtra("username");
        passwordUser = intent.getStringExtra("password");


        TextView HelloText = findViewById(R.id.hellouser);
        HelloText.setText(usernameUser);

        profileEmail.setText(emailUser);
        profileUsername.setText(usernameUser);

        String maskedPassword = passwordUser.substring(0, passwordUser.length() - 3) + "***";

        profilePassword.setText(maskedPassword);

    }

    private boolean isEmailChanged() {
        if (editEmail != null){
            if (!emailUser.equals(editEmail.getText().toString())){
                mDatabaseRef.child(usernameUser).child("email").setValue(editEmail.getText().toString());
                emailUser = editEmail.getText().toString();
                return true;
            }
            else {
                return false;
            }
        } else {
            return false;
        }
    }
    private boolean isPasswordChanged() {
        if (editPassword != null ){
            if (!passwordUser.equals(editPassword.getText().toString())){
                mDatabaseRef.child(usernameUser).child("password").setValue(editPassword.getText().toString());
                passwordUser = editPassword.getText().toString();
                return true;
            }
            else {
                return false;
            }
        } else {
            return false;
        }
    }
}






