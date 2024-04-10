package msu.edu.cse476.dhillo17.palatepal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {


    FirebaseDatabase database;
    private FirebaseAuth mFirebaseAuth;                     // Firebase Authentication
    private DatabaseReference mDatabaseRef;                 // Realtime Database
    private EditText mEtName, mEtEmail, mEtPwd;  // User Info
    private ImageButton mBtnRegister;                       // Register button
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("PalatePal");

        mEtName = findViewById(R.id.et_name);
        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);

        mBtnRegister = (ImageButton) findViewById(R.id.btn_register);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                // Register processing
                String strName = mEtName.getText().toString();
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();


                database = FirebaseDatabase.getInstance();
                mDatabaseRef = database.getReference("users");


                UserAccount userAccount = new UserAccount(strEmail,strName, strPwd);
                mDatabaseRef.child(strName).setValue(userAccount);
                Toast.makeText(RegisterActivity.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);

                // Firebase Auth processing

//                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task)
//                    {
//                        if (task.isSuccessful()){
//                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
//                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                                    .setDisplayName(strName) // Set the display name here
//                                    .build();
//                            UserAccount account = new UserAccount();
//                            account.setName(strName);
//                            account.setEmailId(strEmail);
//                            account.setPassword(strPwd);
//                            mDatabaseRef.child("UserAccount").setValue(account);
//
//                            Toast.makeText(RegisterActivity.this, "Succesfully complete Registeration :)",Toast.LENGTH_SHORT).show();
//                            Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
//                            RegisterActivity.this.startActivity(loginIntent);
//                        }
//                        else {
//                            Toast.makeText(RegisterActivity.this, "Incomplete Registeration :(",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
            }
        });

    }

}
