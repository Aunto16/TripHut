package com.example.triphut;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    EditText meditTextuser,meditTextemail,meditTextpass,meditTextphone,meditTextnid;
    Button mregister;
    ImageView mback;
    TextView mbacklog;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        changeStatusBarColor();
        meditTextuser = findViewById(R.id.editTextuser);
        meditTextemail = findViewById(R.id.editTextemail);
        meditTextpass = findViewById(R.id.editTextpass);
       meditTextphone = findViewById(R.id.editTextMobile);
       meditTextnid = findViewById(R.id.editTextnid);
        mregister = findViewById(R.id.btnregister);
        mback = findViewById(R.id.backbtn);
        mbacklog = findViewById(R.id.backlogin);
        mAuth = FirebaseAuth.getInstance();




        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),login.class));

            }
        });

        mbacklog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),login.class));

            }
        });



        mregister.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String name = meditTextuser.getText().toString().trim();
                String email = meditTextemail.getText().toString().trim();
                String nid = meditTextnid.getText().toString().trim();
                String phone = meditTextphone.getText().toString().trim();
                String password= meditTextpass.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    meditTextemail.setError("Email Is Required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    meditTextpass.setError("Password Is Required");
                    return;
                }
                if(password.length()<= 6){
                    meditTextpass.setError("Password must be large then 6 Character");
                    return;
                }

                //registration on Firebase
                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    User user = new User(name,email,nid,phone);

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(register.this, "User Created Succesfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                                startActivity(new Intent(getApplicationContext(),login.class));

                                            }
                                            else {
                                                Toast.makeText(register.this, "Failed To Create User!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else
                                {
                                    Toast.makeText(register.this, "Failed To Register", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });

    }

    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }
}