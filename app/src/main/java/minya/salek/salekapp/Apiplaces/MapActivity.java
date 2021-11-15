package minya.salek.salekapp.Apiplaces;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.toolbox.HttpResponse;
import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.gms.location.LocationListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.skyfishjy.library.RippleBackground;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;

import cn.pedant.SweetAlert.SweetAlertDialog;
import minya.salek.salekapp.Apiplaces.Customspinner.CustomSpinner;
import minya.salek.salekapp.Apiplaces.Customspinner.FruitAdapter;
import minya.salek.salekapp.Apiplaces.Customspinner.inventory.Data;
import minya.salek.salekapp.Apiplaces.Nearbyplaces.GetNearbyPlacesData;
import minya.salek.salekapp.Captain.CaptainProfileActivity;
import minya.salek.salekapp.Model.CaptainModel;
import minya.salek.salekapp.Prevalent;
import minya.salek.salekapp.R;
import minya.salek.salekapp.RegistrationActivity;

public  class MapActivity extends AppCompatActivity implements OnMapReadyCallback , GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener , CustomSpinner.OnSpinnerEventsListener{
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    int result=0;
    int price=0;
    private  static   boolean changetype=false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

    private Location mLastKnownLocation;
    private LocationCallback locationCallback;

    private MaterialSearchBar materialSearchBar,currentlocation;
    private View mapView;
    private Button btnFind;
    private RippleBackground rippleBg;
    private SweetAlertDialog loadingDialog;

    private final float DEFAULT_ZOOM = 15;

    /*--------------------------------------*/
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentLocationmMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 80000;
    double latitude,longitude;
    public static   String tolocation="Unknown";
    /*---------------------------------------*/
    GetNearbyPlacesData getNearbyPlacesData;
    double Tolatitude,Tolongitude;
    public String Changesearch;

    private CustomSpinner spinner_fruits;

    private FruitAdapter adapter;
    DatabaseReference ref1,ref2,ref3,ref4;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
   static   int x=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        materialSearchBar = findViewById(R.id.searchBar);
        currentlocation=findViewById(R.id.searchBarcurrentlocation);
        currentlocation.setClickable(false);
        btnFind = findViewById(R.id.btn_find);
        rippleBg = findViewById(R.id.ripple_bg);
        spinner_fruits = findViewById(R.id.spinner_fruits);

        spinner_fruits.setSpinnerEventsListener(this);


        ref1 = FirebaseDatabase.getInstance().getReference("captainlocation");
        ref2 = FirebaseDatabase.getInstance().getReference();
        ref3 = FirebaseDatabase.getInstance().getReference();
        ref4 = FirebaseDatabase.getInstance().getReference();
        //Users
        GeoFire geoFire = new GeoFire(ref1);

       mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String uid=currentUser.getUid().toString();
        Log.e("Uid",uid);


      /*          ---------------------------            */

        root.child("Captains").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Log.e("snap",snapshot.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("esnap",error.getMessage());

            }
        });



        /*---------------------------------------*/




        adapter = new FruitAdapter(MapActivity.this, Data.getFruitList());
        spinner_fruits.setAdapter(adapter);
        adapter.setOnPlaceType(new FruitAdapter.OnPlaceType() {
          @Override
          public void onclickplacetype(String Placename) {
              Log.e("itemselect",Placename);
              Object dataTransfer[] = new Object[2];
              getNearbyPlacesData = new GetNearbyPlacesData();
              getNearbyPlacesData.setOnmarkclick(new GetNearbyPlacesData.Onmarkclick() {
                  @Override
                  public void onitemclick(String title,double  La,double lo) {
                      btnFind.setText("سالك ");
                      btnFind.setEnabled(true);
                      Log.e("select",title+"");
                      materialSearchBar.setText(title);
                     // materialSearchBar.setEnabled(false);
                      Tolatitude=La;
                      Tolongitude=lo;
                      Changesearch="SearchByclick";



                  }
              });
              switch(Placename)
              {
                  case "Hospital":

                      mMap.clear();

                      String hospital = "hospital";
                      String url = getUrl(latitude, longitude, hospital);
                      dataTransfer[0] = mMap;
                      dataTransfer[1] = url;

                      getNearbyPlacesData.execute(dataTransfer);
                      Toast.makeText(MapActivity.this, "Showing Nearby Hospitals", Toast.LENGTH_SHORT).show();

                      break;


                  case "Atm":
                      mMap.clear();
                      String atm = "atm";
                      url = getUrl(latitude, longitude, atm);
                      dataTransfer[0] = mMap;
                      dataTransfer[1] = url;

                      getNearbyPlacesData.execute(dataTransfer);

                      Toast.makeText(MapActivity.this, "Showing Nearby Atm", Toast.LENGTH_SHORT).show();
                      break;
                  case "School":
                      mMap.clear();
                      String school = "school";
                      url = getUrl(latitude, longitude, school);
                      dataTransfer[0] = mMap;
                      dataTransfer[1] = url;

                      getNearbyPlacesData.execute(dataTransfer);

                      Toast.makeText(MapActivity.this, "Showing Nearby school", Toast.LENGTH_SHORT).show();


                      break;

                  case "Restuarant":
                      mMap.clear();
                      String restuarant = "restuarant";
                      url = getUrl(latitude, longitude, restuarant);
                      dataTransfer[0] = mMap;
                      dataTransfer[1] = url;

                      getNearbyPlacesData.execute(dataTransfer);
                      Toast.makeText(MapActivity.this, "Showing Nearby Restuarant", Toast.LENGTH_SHORT).show();
                      break;


                  case "accounting":
                      mMap.clear();
                      String account = "accounting";
                      url = getUrl(latitude, longitude, account);
                      dataTransfer[0] = mMap;
                      dataTransfer[1] = url;

                      getNearbyPlacesData.execute(dataTransfer);
                      Toast.makeText(MapActivity.this, "Showing Nearby accounting", Toast.LENGTH_SHORT).show();
                      break;


                  case "bakery":
                      mMap.clear();
                      String back = "bakery";
                      url = getUrl(latitude, longitude, back);
                      dataTransfer[0] = mMap;
                      dataTransfer[1] = url;

                      getNearbyPlacesData.execute(dataTransfer);
                      Toast.makeText(MapActivity.this, "Showing Nearby bakery", Toast.LENGTH_SHORT).show();
                      break;

                  case "beauty_salon":
                      mMap.clear();
                      String beauty = "beauty_salon";
                      url = getUrl(latitude, longitude, "beauty_salon");
                      dataTransfer[0] = mMap;
                      dataTransfer[1] = url;
                      getNearbyPlacesData.execute(dataTransfer);

                      Toast.makeText(MapActivity.this, "Showing Nearby beauty_salon", Toast.LENGTH_SHORT).show();
                      break;


                  case "cafe":
                      mMap.clear();
                      String cafe2 = "cafe";
                     String  url2 = getUrl(latitude, longitude, cafe2);
                      dataTransfer[0] = mMap;
                      dataTransfer[1] = url2;
                      getNearbyPlacesData.execute(dataTransfer);

                      Toast.makeText(MapActivity.this, "Showing Nearby cafe", Toast.LENGTH_SHORT).show();
                      break;
                  case "church":
                      mMap.clear();
                      String church = "church";
                      url = getUrl(latitude, longitude, church);
                      dataTransfer[0] = mMap;
                      dataTransfer[1] = url;
                      getNearbyPlacesData.execute(dataTransfer);

                      Toast.makeText(MapActivity.this, "Showing Nearby church", Toast.LENGTH_SHORT).show();
                      break;


                  case "post_office":
                      mMap.clear();
                      String post_office = "post_office";
                      url = getUrl(latitude, longitude, post_office);
                      dataTransfer[0] = mMap;
                      dataTransfer[1] = url;
                      getNearbyPlacesData.execute(dataTransfer);

                      Toast.makeText(MapActivity.this, "Showing Nearby post_office", Toast.LENGTH_SHORT).show();
                      break;

//https://maps.googleapis.com/maps/api/place/nearbysearch/json?type=university&location=-33.8670522,151.1957362&radius=500&key=YOUR_API_KEY

                  case "doctor":
                      mMap.clear();
                      String doctor = "doctor";
                      url = getUrl(latitude, longitude, doctor);
                      dataTransfer[0] = mMap;
                      dataTransfer[1] = url;
                      getNearbyPlacesData.execute(dataTransfer);

                      Toast.makeText(MapActivity.this, "Showing Nearby doctor", Toast.LENGTH_SHORT).show();
                      break;


                  case "police":
                      mMap.clear();
                      String police = "police";
                      url = getUrl(latitude, longitude, police);
                      dataTransfer[0] = mMap;
                      dataTransfer[1] = url;
                      getNearbyPlacesData.execute(dataTransfer);

                      Toast.makeText(MapActivity.this, "Showing Nearby police", Toast.LENGTH_SHORT).show();
                      break;


                  case "supermarket":
                      mMap.clear();
                      String supermarket = "supermarket";
                      url = getUrl(latitude, longitude, supermarket);
                      dataTransfer[0] = mMap;
                      dataTransfer[1] = url;
                      getNearbyPlacesData.execute(dataTransfer);

                      Toast.makeText(MapActivity.this, "Showing Nearby supermarket", Toast.LENGTH_SHORT).show();
                      break;


                  case "university":
                      mMap.clear();
                      String university = "university";
                      url = getUrl(latitude, longitude, university);
                      dataTransfer[0] = mMap;
                      dataTransfer[1] = url;
                      getNearbyPlacesData.execute(dataTransfer);

                      Toast.makeText(MapActivity.this, "Showing Nearby university", Toast.LENGTH_SHORT).show();
                      break;

              }







          }
      });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);
        Places.initialize(MapActivity.this, getString(R.string.google_maps_api));
        placesClient = Places.createClient(this);

        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString(), true, null, true);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {
                    btnFind.setText("سالك ");
                    btnFind.setEnabled(true);
                    //opening or closing a navigation drawer
                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    btnFind.setText("سالك ");
                    btnFind.setEnabled(true);
                    materialSearchBar.disableSearch();
                }
            }
        });

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()

                        .setCountry("EG")
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();
                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if (task.isSuccessful()) {
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            if (predictionsResponse != null) {
                                predictionList = predictionsResponse.getAutocompletePredictions();
                                List<String> suggestionsList = new ArrayList<>();
                                for (int i = 0; i < predictionList.size(); i++) {
                                    AutocompletePrediction prediction = predictionList.get(i);
                                    suggestionsList.add(prediction.getFullText(null).toString());
                                }
                                materialSearchBar.updateLastSuggestions(suggestionsList);
                                if (!materialSearchBar.isSuggestionsVisible()) {
                                    materialSearchBar.showSuggestionsList();
                                }
                            }
                        } else {
                            Log.i("mytag", "prediction fetching task unsuccessful");
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setSuggstionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                if (position >= predictionList.size()) {
                    return;
                }
                AutocompletePrediction selectedPrediction = predictionList.get(position);
                String suggestion = materialSearchBar.getLastSuggestions().get(position).toString();
                materialSearchBar.setText(suggestion);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialSearchBar.clearSuggestions();
                    }
                }, 1000);
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                final String placeId = selectedPrediction.getPlaceId();
                List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);

                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();
                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                        Place place = fetchPlaceResponse.getPlace();
                        Log.i("mytag", "Place found: " + place.getName());
                        LatLng latLngOfPlace = place.getLatLng();
                        if (latLngOfPlace != null) {

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, DEFAULT_ZOOM));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            apiException.printStackTrace();
                            int statusCode = apiException.getStatusCode();
                            Log.i("mytag", "place not found: " + e.getMessage());
                            Log.i("mytag", "status code: " + statusCode);
                        }
                    }
                });
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });
        btnFind.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LatLng currentMarkerLocation = mMap.getCameraPosition().target;
                if(currentlocation.getPlaceHolderText().equals(" ")|| materialSearchBar.getText().equals(""))
                {
                         new SweetAlertDialog(MapActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(" تحذير  ")
                        .setContentText(" من فضلك يجب ان تختار الاماكن المتجه اليها  ")
                        .setConfirmText("تاكيد")
                        .setConfirmClickListener(sDialog -> {

                            sDialog.dismiss();

                        })
                        .show();

                }

                else {
                    if (Changesearch == "SearchByclick") {

                        result = (int) getKmFromLatLong(latitude, longitude, Tolatitude, Tolongitude);
                        Log.e("result", result + "");
                        Log.e("current", latitude + "");
                        Log.e("currents", longitude + "");
                        Log.e("Tolat", Tolatitude + "");
                        Log.e("Tolong", Tolongitude + "");
                        Log.e("coorect", getKmFromLatLong(latitude, longitude, Tolatitude, Tolongitude) + "");
                        // make query in Geo fire
                        // creates a new query around [37.7832, -122.4056] with a radius of 0.6 kilometers
                        double L = latitude;
                        double w = longitude;

                        if (result > 0 && result <= 2000) {

                            price = 6;

                        } else if (result > 2000 && result <= 4000) {

                            price = 8;

                        } else if (result > 4000 && result <= 8000) {

                            price = 10;

                        } else if (result > 8000 && result <= 10000) {

                            price = 12;

                        }
                        else if(result>10000 && result <=12000) {
                            price = 15;
                        }

                        else if(result>12000 && result <=14000) {
                            price = 18;
                        }
                        else if(result>14000 && result <=18000) {
                            price = 20;
                        }
                        else
                        {

                            price =25;
                        }
                        new SweetAlertDialog(MapActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setContentText(" المسافه التقريبه للرحله بالكيلو متر  " + result/1000+"")
                                .setTitleText(" سالك  ")
                                .setConfirmText("تاكيد")
                                .setConfirmClickListener(sDialog -> {


                                    GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(L, w), 6);

                                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                                        @Override
                                        public void onKeyEntered(String key, GeoLocation location) {
                                            // Read Date from firebase

                                            Log.e("key", key.toString());
                                            changetype=true;
                                            x=2;


                                            ReadDate(key.toString(), price);


                                        }

                                        @Override
                                        public void onKeyExited(String key) {

                                            Log.e("keyexited", key.toString());


                                        }

                                        @Override
                                        public void onKeyMoved(String key, GeoLocation location) {
                                            Log.e("keymoved", location.latitude + "");


                                        }

                                        @Override
                                        public void onGeoQueryReady() {

                                            Log.e("keyReady", "onGeoQueryReady");

                                            if(x>-1)
                                            {

                                                Toast.makeText(MapActivity.this,"يوجد كابتن قريب منك ",Toast.LENGTH_LONG).show();


                                            }

                                            else
                                            {
                                                // Toast.makeText(MapActivity.this,"hhh",Toast.LENGTH_LONG).show();

                                                btnFind.setText("لا يوجد كابتن  قريب منك ");
                                                btnFind.setEnabled(false);
                                            }




                                        }

                                        @Override
                                        public void onGeoQueryError(DatabaseError error) {
                                            Log.e("onGeoQueryError", "onGeoQueryError");

                                        }
                                    });

                                    sDialog.dismiss();





                                })
                                .show();




                    }



                    else {
                        LatLng L = getLocationFromAddress(MapActivity.this, materialSearchBar.getText().toString());
                        result = (int) getKmFromLatLong(latitude, longitude, L.latitude, L.longitude);
                        Log.e("result", result + "");
                        Log.e("current", latitude + "");
                        Log.e("currents", longitude + "");
                        Log.e("Tolat", L.latitude + "");
                        Log.e("Tolong", L.longitude + "");

                        double L1 = latitude;
                        double w1 = longitude;
                        if (result > 0 && result <= 2000) {

                            price = 6;

                        } else if (result > 2000 && result <= 4000) {

                            price = 8;

                        } else if (result > 4000 && result <= 8000) {

                            price = 10;

                        } else if (result > 8000 && result <= 10000) {

                            price = 12;

                        }
                        else if(result>10000 && result <=12000) {
                            price = 15;
                        }

                        else if(result>12000 && result <=14000) {
                            price = 18;
                        }
                        else if(result>14000 && result <=18000) {
                            price = 20;
                        }
                        else
                        {

                            price =25;
                        }
                        new SweetAlertDialog(MapActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setContentText(" المسافه التقريبه للرحله بالكياو متر  "+result/1000+"")
                                .setTitleText(" سالك  ")
                                .setConfirmText("تاكيد")
                                .setConfirmClickListener(sDialog -> {

                                    GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(L1, w1), 6);
                                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                                        @Override
                                        public void onKeyEntered(String key, GeoLocation location) {


                                            Log.e("key", key.toString());


                                            ReadDate(key.toString(), price);


                                        }

                                        @Override
                                        public void onKeyExited(String key) {

                                            Log.e("keyexited", key.toString());


                                        }

                                        @Override
                                        public void onKeyMoved(String key, GeoLocation location) {
                                            Log.e("keymoved", location.latitude + "");


                                        }

                                        @Override
                                        public void onGeoQueryReady() {

                                            Log.e("keyReady", "onGeoQueryReady");

                                        }

                                        @Override
                                        public void onGeoQueryError(DatabaseError error) {
                                            Log.e("onGeoQueryError", "onGeoQueryError");

                                        }




                                    });

                                    sDialog.dismiss();

                                })
                                .show();


                    }
                }

            }
        });



    }

    private void ReadDate(String key,int p) {

       /*String type  = getIntent().getStringExtra("typecars");
       Log.e("types",type);*/

        root.child("Captains").addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {




                for(DataSnapshot demo : snapshot.getChildren())
                {

                    if(key.equals(demo.getKey())) {
                        Log.e("ketca", demo.getKey().toString());
                        Log.e("image", demo.child("imageUrl").toString());
                        Log.e("name", demo.child("name").toString());
                        Log.e("Toloc", materialSearchBar.getText().toString());
                        showCustomDialog(demo.child("imageUrl").getValue().toString(),demo.child("name").getValue().toString(),currentlocation.getPlaceHolderText().toString(),materialSearchBar.getText().toString(),demo.getKey().toString(),p);

                       // Users

                    }


                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("esnap",error.getMessage());

            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
            /*------------------*/
            bulidGoogleApiClient();

        }

        //check if gps is enabled or not and then request user to enable it
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MapActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                getDeviceLocation();




            }
        });

        task.addOnFailureListener(MapActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(MapActivity.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (materialSearchBar.isSuggestionsVisible())
                    materialSearchBar.clearSuggestions();
                if (materialSearchBar.isSearchEnabled())
                    materialSearchBar.disableSearch();
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
                /*-----------------*/
                bulidGoogleApiClient();

            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                           // Log.e("lat",mLastKnownLocation.getLatitude()+"");
                            //Log.e("long",mLastKnownLocation.getLongitude()+"");


                            Geocoder geocoder;
                            List<Address> addresses = null;
                            geocoder = new Geocoder(MapActivity.this, Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                                // currentlocation.setText(address);
                                currentlocation.disableSearch();
                               currentlocation.setPlaceHolder(address);

                              //  Log.e("city", city);
                              //  Log.e("address", address);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }


                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(MapActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /*-----------------------------------------------*/





    protected synchronized void bulidGoogleApiClient() {
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();

    }


    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastlocation = location;
        if(currentLocationmMarker != null)
        {
            currentLocationmMarker.remove();

        }
        Log.d("lat = ",""+latitude);
       // LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());
      /*  MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentLocationmMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));*/

        if(client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }
    }



    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");

        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+getString(R.string.google_maps_api));
        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());
        return googlePlaceUrl.toString();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest,this);
        }
    }



    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }
    /*-------------------------------------------------*/

    //calc distances between two point

    public  float getKmFromLatLong(double lat1, double lng1, double lat2, double lng2){
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);
        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);
        float distanceInMeters = loc1.distanceTo(loc2);
        return distanceInMeters;
    }
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public void onPopupWindowOpened(Spinner spinner) {
        spinner_fruits.setBackground(getResources().getDrawable(R.drawable.bg_spinner_fruit_up));
    }

    @Override
    public void onPopupWindowClosed(Spinner spinner) {
        spinner_fruits.setBackground(getResources().getDrawable(R.drawable.bg_spinner_fruit));
    }
    void showCustomDialog(String u,String name,String c_location,String Tolocation,String k, int p) {
        final Dialog dialog = new Dialog(MapActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        View mView = LayoutInflater.from(MapActivity.this).inflate(R.layout.captain_notification_dialog, null);
        final TextView phone, location, destination, pricee;
        final Button acceptBtn, cancelBtn;
        final ImageView userIcon, callIcon,p_image;

        phone = mView.findViewById(R.id.notification_phone);
        location = mView.findViewById(R.id.current_location_txt);
        destination = mView.findViewById(R.id.destination_location_txt);
        pricee = mView.findViewById(R.id.notification_price_text);
        acceptBtn = mView.findViewById(R.id.notification_accept_btn);
        cancelBtn = mView.findViewById(R.id.notification_cancel_btn);
        p_image = mView.findViewById(R.id.profile_image);
        phone.setText(name);
        location.setText(c_location);
        destination.setText(Tolocation);
        pricee.setText(p+"");

        Glide.with(this).load(u)
                .error(R.drawable.mask_group).into(p_image);

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ref4.child("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM. yyyy");
                        String saveCurrentDate = currentDate.format(calendar.getTime());


                        HashMap<String,String> item=new HashMap<>();
                        item.put("userId",currentUser.getUid());
                        item.put("captainId",k);
                        item.put("IsAccept","No");
                        item.put("strip",currentlocation.getPlaceHolderText().toString());
                        item.put("endtrip",materialSearchBar.getText().toString());
                        item.put("tripDate",saveCurrentDate.toString());
                        item.put("tripPrice", p+"");
                        item.put("Image", snapshot.child("imageUrl").getValue().toString());
                        item.put("Phone", snapshot.child("phone").getValue().toString());
                        item.put("Uname", snapshot.child("name").getValue().toString());
                        Log.e("Phone",snapshot.child("phone").getValue().toString());
                        ref3.child("Trips").child(k).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.e("sucessfilter", "sucess");
                                result=0;
                                price=0;
                                dialog.dismiss();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("failed", e.getMessage());

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.e("failed", error.getMessage());


                    }
                });









            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                result=0;
                price=0;
                pricee.setText("  ");
                Tolatitude=0.0;
                Tolongitude=0.0;
            }
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(mView);
        dialog.setCancelable(false);
        dialog.show();
    }



}
