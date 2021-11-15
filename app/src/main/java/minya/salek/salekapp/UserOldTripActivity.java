package minya.salek.salekapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import minya.salek.salekapp.Model.OldTripModel;
import minya.salek.salekapp.Model.userTrip;

public class UserOldTripActivity extends AppCompatActivity {

    private TextView name, phone, noOldTrip,orders_trip;
    private ImageView userImage;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private ArrayList<userTrip> oldTrip;
    private DatabaseReference request;
    private String userId;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_old_trip);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.old_user_name_txt);
        phone = findViewById(R.id.old_trip_user_Phone_txt);
        userImage = findViewById(R.id.old_profile_image);
        progressBar = findViewById(R.id.user_old_trip_load);
        orders_trip = findViewById(R.id.order_trip);

        name.setText(Prevalent.currentOnlineUser.getName());
        phone.setText(Prevalent.currentOnlineUser.getPhone());


       Picasso.get().load(Prevalent.currentOnlineUser.getImageUrl()).placeholder(R.drawable.mask_group).into(userImage);

        noOldTrip = findViewById(R.id.user_old_trip_empty);
        recyclerView = findViewById(R.id.user_old_trip_recycler);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        request = database.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        userId = firebaseUser.getUid();

        oldTrip = new ArrayList<>();
         getOldTrip();

       // getOldTrip2();
    }

    private void getOldTrip() {
       // progressBar.setVisibility(View.VISIBLE);
        request.child("Accepted Trips").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                oldTrip.clear();


                   for(DataSnapshot dmo :snapshot.getChildren()) {

                           userTrip oldTripModel = dmo.getValue(userTrip.class);

                           Log.e("tripname", snapshot.child("endTrip").getValue() + "");

                           oldTrip.add(oldTripModel);


                   }


                if (oldTrip.isEmpty()) {
                    noOldTrip.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    orders_trip.setVisibility(View.GONE);

                }


                else

                {
                    orders_trip.setVisibility(View.GONE);
                    noOldTrip.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    OldTripAdapter adapter = new OldTripAdapter(UserOldTripActivity.this, oldTrip);
                    recyclerView.setLayoutManager(new LinearLayoutManager(UserOldTripActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(adapter);
                }
                    }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}

