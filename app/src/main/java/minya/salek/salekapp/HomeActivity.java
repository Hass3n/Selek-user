package minya.salek.salekapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.Objects;
import minya.salek.salekapp.Apiplaces.MapActivity;
import minya.salek.salekapp.Apiplaces.PermissionsActivity;


public class HomeActivity extends AppCompatActivity {

    ImageView userImage;
    TextView name;
    ImageView scooters,Box;
    FirebaseUser currentUser;
    DatabaseReference reference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        userImage = findViewById(R.id.user_image);
        name=findViewById(R.id.username_txt);
        scooters=findViewById(R.id.scooter);
        Box=findViewById(R.id.box);

        scooters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(HomeActivity.this, PermissionsActivity.class);
                in.putExtra("typecar","موتوسيكل");
                startActivity(in);


            }
        });

        Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent in=new Intent(HomeActivity.this, PermissionsActivity.class);
                in.putExtra("typecar","نقل");
                startActivity(in);*/

                Toast.makeText(HomeActivity.this,"غير متاح الان  ",Toast.LENGTH_LONG).show();


            }
        });



       /* currentUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = currentUser.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //User usersData = dataSnapshot.child(parentDbName).child(email).getValue(User.class);

                Prevalent.currentOnlineUser = snapshot.getValue(UserModel.class);
                Toast.makeText(HomeActivity.this, ""+Prevalent.currentOnlineUser.getEmail(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        if (Prevalent.currentOnlineUser.getImageUrl() == null) {
            userImage.setImageDrawable(getResources().getDrawable(R.drawable.mask_group));
        } else {
            Picasso.get().load(Prevalent.currentOnlineUser.getImageUrl()).placeholder(R.drawable.mask_group).into(userImage);

        }

        name.setText(Prevalent.currentOnlineUser.getName());
        /*Glide.with(this).load(Prevalent.currentOnlineUser.getImageUrl())
                .error(R.drawable.mask_group).fallback(R.drawable.mask_group).into(userImage);
*/
        /*if (Prevalent.currentOnlineUser.getImageUrl() !=null) {
            Glide.with(this).load(Prevalent.currentOnlineUser.getImageUrl())
                    .into(userImage);
        }
        else {
            userImage.setImageDrawable(getResources().getDrawable(R.drawable.mask_group));
        }*/
        
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, UserProfileActivity.class));
            }
        });
    }
}
