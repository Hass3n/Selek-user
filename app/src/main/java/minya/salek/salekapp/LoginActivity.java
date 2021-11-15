package minya.salek.salekapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import minya.salek.salekapp.Apiplaces.MapActivity;
import minya.salek.salekapp.Apiplaces.PermissionsActivity;
import minya.salek.salekapp.Model.UserModel;

public class LoginActivity extends AppCompatActivity {


    EditText loginEmail, loginPassword;
    TextView forgetPassword;
    Button signUp, login;

    FirebaseAuth mAuth;
    SweetAlertDialog pDialog;
    FirebaseUser currentUser;
    DatabaseReference reference;
    String userId;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        loginEmail = findViewById(R.id.login_email_data);
        loginPassword = findViewById(R.id.login_password_data);
        forgetPassword = findViewById(R.id.forget_password);
        signUp = findViewById(R.id.new_user_btn);
        login = findViewById(R.id.login_btn);

        mAuth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(LoginActivity.this,HomeActivity.class));
               loginUser();
            }
        });
    }

    private void loginUser() {
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            loginEmail.setError("ادخل البريد الإلكتروني");
            loginEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("يجب ادخال بريد الكتروني صحيح مثلا : \n example@gmail.com");
            loginEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            loginPassword.setError("ادخل كلمة المرور");
            loginPassword.requestFocus();
        } else {
            if (!Prevalent.isConnection(LoginActivity.this)) {
                Prevalent.showCustomDialog(LoginActivity.this);
            } else {
                pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#FCCB09"));
                pDialog.setTitleText("جاري تسجيل الدخول");
                pDialog.setCancelable(false);
                pDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            reference = FirebaseDatabase.getInstance().getReference("Users");
                            userId = currentUser.getUid();

                            reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //User usersData = dataSnapshot.child(parentDbName).child(email).getValue(User.class);

                                    Prevalent.currentOnlineUser = snapshot.getValue(UserModel.class);
                                    Log.e("p",Prevalent.currentOnlineUser.getImageUrl()+"");
                                    //Toast.makeText(LoginActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                                    //Prevalent.currentOnlineUser = currentUser;
                                    if (Prevalent.currentOnlineUser != null) {
                                        pDialog.dismiss();
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        finish();
                                    }else {
                                        Toast.makeText(LoginActivity.this, "User logged in Failure", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(LoginActivity.this, "logged " + error, Toast.LENGTH_SHORT).show();

                                }
                            });

                        } else {
                            pDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

}
