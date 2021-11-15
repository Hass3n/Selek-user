package minya.salek.salekapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import minya.salek.salekapp.Model.UserModel;

public class UserProfileActivity extends AppCompatActivity {

    private Button oldTripBtn;
    private TextView name,id,phone,email;
    private ImageView profileImage, editPen;
    private Uri ImageUri;
    private static final int GALLERY_REQUEST_CODE = 10;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 100;

    private DatabaseReference root ;
    private StorageReference reference ;
    FirebaseUser currentUser;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        oldTripBtn = findViewById(R.id.old_trip_btn);
        name = findViewById(R.id.user_name_txt);
        id = findViewById(R.id.user_id_data);
        phone = findViewById(R.id.user_Phone_txt);
        email = findViewById(R.id.user_email_data);
        profileImage = findViewById(R.id.profile_image);
        editPen = findViewById(R.id.edit_pen);

        root = FirebaseDatabase.getInstance().getReference("Users");
        reference =
                FirebaseStorage.getInstance().getReference("Users Image").child("Image");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        userId = currentUser.getUid();


        oldTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UserProfileActivity.this, UserOldTripActivity.class));
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(UserProfileActivity.this, CaptainProfileActivity.class));
            }
        });
        editPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(v);
            }
        });
    }
    public void pickImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }
    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                ImageUri = data.getData();
                if (ActivityCompat.checkSelfPermission(this
                        , Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //readFileFromUri();
                    profileImage.setImageURI(ImageUri);
                    uploadToFirebase(ImageUri);
                    //Toast.makeText(this, "First", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityCompat.requestPermissions(this
                            , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                            , READ_EXTERNAL_STORAGE_REQUEST_CODE);
                }
            } else {
                Toast.makeText(this, "لم يتم إختار صورة شخصية جديدة", Toast.LENGTH_SHORT).show();
            }
        }
        
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //readFileFromUri();
                profileImage.setImageURI(ImageUri);
                uploadToFirebase(ImageUri);

                //Toast.makeText(this, "Second", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "لا يمكننا عرض الصورة المختارة", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void uploadToFirebase(Uri uri) {
        final SweetAlertDialog loadingBar = new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        loadingBar.setTitleText("سالك");
        loadingBar.getProgressHelper().setBarColor(Color.parseColor("#FCCB09"));
        loadingBar.setContentText("جاري تغيير الصورة الشخصية");
        loadingBar.setCancelable(false);
        loadingBar.setCanceledOnTouchOutside(false);

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        UserModel model = new UserModel(Prevalent.currentOnlineUser.getName(), Prevalent.currentOnlineUser.getId(),Prevalent.currentOnlineUser.getPhone(),Prevalent.currentOnlineUser.getEmail(),Prevalent.currentOnlineUser.getPassword(),uri.toString() ,Prevalent.currentOnlineUser.getKey());

                        root.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                root.child(userId).setValue(model)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Prevalent.currentOnlineUser = model;
                                                } else {
                                                    String message = Objects.requireNonNull(task.getException()).toString();
                                                    Toast.makeText(UserProfileActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UserProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        loadingBar.dismiss();

                        final SweetAlertDialog successBar = new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        successBar.setTitleText("سالك");
                        successBar.setContentText("تم تغيير الصورة الشخصية بنجاح");
                        successBar.getProgressHelper().setBarColor(Color.parseColor("#FCCB09"));
                        successBar.setCancelable(false);
                        successBar.setCanceledOnTouchOutside(false);
                        successBar.setConfirmText("حسنا");
                        successBar.setConfirmClickListener(sDialog -> {
                            sDialog.dismiss();
                        });
                        successBar.show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                loadingBar.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                final SweetAlertDialog successBar = new SweetAlertDialog(UserProfileActivity.this, SweetAlertDialog.ERROR_TYPE);
                successBar.setTitleText("سالك");
                successBar.setContentText("حدث خطأ أثناء رفع الصورة الشخصية من فضلك حاول مجددا");
                successBar.setCancelable(false);
                successBar.setCanceledOnTouchOutside(false);
                successBar.setConfirmText("حسنا");
                successBar.setConfirmClickListener(sDialog -> {
                    sDialog.dismiss();
                });
                successBar.show();
            }
        });
    }

    private String getFileExtension(Uri mUri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }
    @Override
    protected void onStart() {
        super.onStart();

        if (Prevalent.currentOnlineUser != null) {
            name.setText(Prevalent.currentOnlineUser.getName());
            id.setText(Prevalent.currentOnlineUser.getId());
            phone.setText(Prevalent.currentOnlineUser.getPhone());
            email.setText(Prevalent.currentOnlineUser.getEmail());
            Glide.with(this).load(Prevalent.currentOnlineUser.getImageUrl())
                    .error(R.drawable.mask_group).into(profileImage);
        } else {
            name.setText("اسم المستخدم");
            id.setText("الرقم القومي");
            phone.setText("الموبايل");
            email.setText("");
            Glide.with(this).load(Prevalent.currentOnlineUser.getImageUrl())
                    .error(R.drawable.mask_group).into(profileImage);
        }
    }
}
