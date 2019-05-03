package revolution.ph.developer.remediofacil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.maps.android.SphericalUtil;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import javax.annotation.Nullable;

import static revolution.ph.developer.remediofacil.MainActivity.ids;

public class CarrinhoActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter, AdapterCart.AnalizarClickPayFinal {

    private static final int REQUEST_CHECK_SETTINGS = 109;
    private GoogleMap mMap = null;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 345;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private int M_MAX_ENTRIES = 5;
    private LatLng mDefaultLocation, autoCompleteLocation;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private LatLng[] mLikelyPlaceLatLngs;
    private String[] mLikelyPlaceAttributions;
    private float DEFAULT_ZOOM = 17;
    private View btVoltar, closeHelp;
    private ProgressBar pb;

    public static ArrayList<Float> valores = new ArrayList();
    private int somo = 0;

    private AdapterCart adapter;

    private TextView tv_nome_rua_cart, btAjuda, tv_exemplo_help;

    private LinearLayout content_layout_cart;

    public static ArrayList<CarComprasActivy> produtoss = new ArrayList<>();

    private RecyclerView rv;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private Parcelable mCameraPosition;
    private Parcelable mCurrentLocation;
    private SupportMapFragment mapFragment;
    private CoordinatorLayout coordinator_layout_cart;

    private BottomSheetBehavior<LinearLayout> sheetBehavior;
    private PlaceAutocompleteFragment autocompleteFragment;
    private CollectionReference cart;
    private int precoEntrega = 0;
    private TickerView taxaEntregaTV, totalTV, ttcomprasTV;

    private FloatingActionButton fab;
    private FrameLayout containerHelp;

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winPar = win.getAttributes();
        if (on) {
            winPar.flags |= bits;
        } else {
            winPar.flags &= ~bits;
        }
        win.setAttributes(winPar);
    }


    @Override
    protected void onStart() {
        super.onStart();
        cart.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                somo = 0;
                valores.clear();
                produtoss.clear();
                for (int i = 0; i < querySnapshot.getDocuments().size(); i++) {
                    CarComprasActivy carComprasActivy = (CarComprasActivy) querySnapshot.getDocuments().get(i).toObject(CarComprasActivy.class);
                    valores.add(Float.valueOf(carComprasActivy.getValorTotal()));
                    produtoss.add(carComprasActivy);
                }
                for (int j = 0; j < valores.size(); j++) {
                    somo = (int) (((float) somo) + valores.get(j));
                }


                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(String.valueOf(somo + precoEntrega));
                stringBuilder.append(",00");
                totalTV.setText(stringBuilder.toString());
                ttcomprasTV.setText(String.valueOf(somo) + ",00");
                if (querySnapshot.getDocuments().size() == 0) {
//                    PayFinalActivity.this.emptyCar.setVisibility(0);
//                    PayFinalActivity.this.mPayList.setVisibility(8);
                } else {
//                    PayFinalActivity.this.emptyCar.setVisibility(8);
//                    PayFinalActivity.this.mPayList.setVisibility(0);
                }
                adapter.swapDados(produtoss);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        coordinator_layout_cart = (CoordinatorLayout) findViewById(R.id.coordinator_layout_cart);

        rv = (RecyclerView) findViewById(R.id.rv_cart);
        containerHelp = (FrameLayout) findViewById(R.id.container_help);
        fab = (FloatingActionButton) findViewById(R.id.fab_carrinho);
        tv_nome_rua_cart = (TextView) findViewById(R.id.tv_nome_rua_cart);
        tv_exemplo_help = (TextView) findViewById(R.id.tv_exemplo_help);
        btAjuda = (TextView) findViewById(R.id.bt_mudar_endereco);
        taxaEntregaTV = (TickerView) findViewById(R.id.taxa_entrega);
        totalTV = (TickerView) findViewById(R.id.total_cart);
        ttcomprasTV = (TickerView) findViewById(R.id.total_compras);
        btVoltar = (View) findViewById(R.id.bt_voltar_cart);
        closeHelp = (View) findViewById(R.id.close_help);
        pb = (ProgressBar) findViewById(R.id.pb_cart);
        taxaEntregaTV.setCharacterList(TickerUtils.getDefaultNumberList());
        totalTV.setCharacterList(TickerUtils.getDefaultNumberList());
        ttcomprasTV.setCharacterList(TickerUtils.getDefaultNumberList());
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout_cart);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        cart = firestore.collection("carComprasActivy").document("usuario").collection(auth.getCurrentUser().getUid());

        content_layout_cart = (LinearLayout) findViewById(R.id.content_layout_cart);

        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        mDefaultLocation = new LatLng(-3.10719, -60.0261);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setHint("Sua rua, número da sua casa");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                tv_nome_rua_cart.setText(place.getAddress());
                if (mMap != null) {
                    autoCompleteLocation = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                    float factor = getResources().getDisplayMetrics().density;
                    int h = (int) (450 * factor);
                    int top = (int) (80 * factor);
                    mMap.setPadding(0, top, 0, h);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(autoCompleteLocation, 18));
                    mMap.addMarker(new MarkerOptions()
                            .position(autoCompleteLocation));
                }
            }

            @Override
            public void onError(Status status) {

            }
        });

        sheetBehavior = BottomSheetBehavior.from(content_layout_cart);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);//initially state to fully expanded
        rv.setLayoutManager(new LinearLayoutManager(CarrinhoActivity.this, RecyclerView.HORIZONTAL, false));
        adapter = new AdapterCart(CarrinhoActivity.this, CarrinhoActivity.this);
        rv.setAdapter(adapter);

        btAjuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerHelp.setVisibility(View.VISIBLE);

                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CarrinhoActivity.this, ConfirmarCompraActivity.class);
                startActivity(intent);
            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        closeHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerHelp.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!gpsLigado()) {
            ligarGps();
        } else {
            mapFragment.getMapAsync(this);
        }
    }

    //margem do mapa = tamanho do linear layout container

    private void ligarGps() {
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY))
                .addLocationRequest(LocationRequest.create().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY));
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                mapFragment.getMapAsync(CarrinhoActivity.this);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(CarrinhoActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException se) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        break;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:

                switch (resultCode) {

                    case Activity.RESULT_OK:
                        mapFragment.getMapAsync(CarrinhoActivity.this);
                        break;
                    case Activity.RESULT_CANCELED:

                        break;
                }

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                } else if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    finish();
                }
            }
        }
        updateLocationUI();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        //pb.setVisibility(View.GONE);

        if (mMap != null) {
            mMap.clear();
        }

        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

//        Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        mMap.setInfoWindowAdapter(this);
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                getDeviceLocation();
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission(this);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        if (autoCompleteLocation != null) {
            exibirEnderecoAtual();
            float factor = getResources().getDisplayMetrics().density;
            int h = (int) (450 * factor);
            int top = (int) (80 * factor);

            mMap.setPadding(0, top, 0, h);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(autoCompleteLocation, 18));
            mMap.addMarker(new MarkerOptions()
                    .position(autoCompleteLocation));
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            return;
        }

        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(CarrinhoActivity.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.

                            pb.setVisibility(View.GONE);

                            mLastKnownLocation = (Location) task.getResult();
                            if (mLastKnownLocation == null) {
                                //getDeviceLocation();
                                return;
                            }
                            mDefaultLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            exibirEnderecoAtual();
                            float factor = getResources().getDisplayMetrics().density;
                            int h = (int) (450 * factor);
                            int top = (int) (80 * factor);

                            mMap.setPadding(0, top, 0, h);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 18));
                            mMap.addMarker(new MarkerOptions()
                                    .position(mDefaultLocation));
                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        } else {

                            //getDeviceLocation();

//                            float factor = getResources().getDisplayMetrics().density;
//                            int h = (int) (450 * factor);
//                            int top = (int) (80 * factor);
//                            mMap.setPadding(0, top, 0, h);
//                            Log.d("TesteMap", "Current location is null. Using defaults.");
//                            Log.e("TesteMap", "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 19));
//                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exceptionnn: %s", e.getMessage());
        } catch (NullPointerException e) {
            Log.e("Exceptionnn: %s", e.getMessage());
        }
    }


    private void exibirEnderecoAtual() {

        if (autoCompleteLocation != null) {
            mDefaultLocation = autoCompleteLocation;
        }

        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(mDefaultLocation.latitude, mDefaultLocation.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
            exibirEnderecoAtual();
        }

        if (addresses != null) {
            String rua = addresses.get(0).getThoroughfare();
            autocompleteFragment.setText(rua + ",");
            tv_nome_rua_cart.setText(rua);
            tv_exemplo_help.setText(rua + ", 10");
            precoEntrega = calcularEntregaRapida(mDefaultLocation.latitude, mDefaultLocation.longitude);
            String valorDaEntrega = String.valueOf(precoEntrega) + ",00";
            String valorDasCompras = String.valueOf(somo) + ",00";
            int tt = precoEntrega + somo;
            String total = String.valueOf(tt) + ",00";
            totalTV.setText(total);
            ttcomprasTV.setText(valorDasCompras);
            taxaEntregaTV.setText(valorDaEntrega);
        }

    }

    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener
                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                                // Set the count, handling cases where less than 5 entries are returned.
                                int count;
                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                                    count = likelyPlaces.getCount();
                                } else {
                                    count = M_MAX_ENTRIES;
                                }

                                int i = 0;
                                mLikelyPlaceNames = new String[count];
                                mLikelyPlaceAddresses = new String[count];
                                mLikelyPlaceAttributions = new String[count];
                                mLikelyPlaceLatLngs = new LatLng[count];

                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                    // Build a list of likely places to show the user.
                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
                                            .getAddress();
                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
                                            .getAttributions();
                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                                    i++;
                                    if (i > (count - 1)) {
                                        break;
                                    }
                                }

                                // Release the place likelihood buffer, to avoid memory leaks.
                                likelyPlaces.release();

                                // Show a dialog offering the user the list of likely places, and add a
                                // marker at the selected place.
                                openPlacesDialog();

                            } else {
                                Log.e("TesteMap", "Exception: %s", task.getException());
                            }
                        }
                    });
        } else {
            // The user has not granted permission.
            Log.i("TesteMap", "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title("Manaus")
                    .position(mDefaultLocation)
            );

            // Prompt the user for permission.
            getLocationPermission(this);
        }
    }

    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.

                if (mMap != null) {
                    mMap.clear();
                }

                autoCompleteLocation = null;

                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
                String markerSnippet = mLikelyPlaceAddresses[which];
                if (mLikelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
                }

                mDefaultLocation = new LatLng(markerLatLng.latitude, markerLatLng.longitude);

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .position(mDefaultLocation)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation,
                        DEFAULT_ZOOM));

                autocompleteFragment.setText(markerSnippet);
                tv_nome_rua_cart.setText(markerSnippet);
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Escolha um lugar")
                .setItems(mLikelyPlaceNames, listener)
                .show();
    }

    private void getLocationPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // Inflate the layouts for the info window, title and snippet.
        View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents, null);

        TextView title = ((TextView) infoWindow.findViewById(R.id.title));
        title.setText(marker.getTitle());

        TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
        snippet.setText(marker.getSnippet());

        return infoWindow;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void aumentarQuantidade(CarComprasActivy carComprasActivy, String str, int i) {
        DocumentReference document = cart.document(str);
        WriteBatch batch = firestore.batch();
        batch.update(document, "quantidade", Integer.valueOf(carComprasActivy.getQuantidade() + 1));
        batch.update(document, "valorTotal", Float.valueOf(carComprasActivy.getValorTotal() + carComprasActivy.getValorUni()));
        batch.commit();
    }

    @Override
    public void diminuirQuantidade(CarComprasActivy carComprasActivy, String str, int i) {
        DocumentReference document = cart.document(str);
        if (carComprasActivy.getQuantidade() > 1) {
            WriteBatch batch = firestore.batch();
            batch.update(document, "quantidade", Integer.valueOf(carComprasActivy.getQuantidade() - 1));
            batch.update(document, "valorTotal", Float.valueOf(carComprasActivy.getValorTotal() - carComprasActivy.getValorUni()));
            batch.commit();
        }
    }

    @Override
    public void removerProduto(String str, int p) {
        DocumentReference reference = cart.document(str);
        if (ids.contains(str)) {
            ids.remove(str);
        }
        reference.delete();
        produtoss.remove(p);
        adapter.notifyItemRemoved(p);
        if (produtoss.size() == 0) {
            finish();
        }
    }

    private boolean gpsLigado() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private int calcularEntregaRapida(double la, double lo) {
        int placeIdObj = (int) SphericalUtil.computeDistanceBetween(new LatLng(-3.1174754000000005d, -60.001966200000005d), new LatLng(la, lo));
        if (placeIdObj > 30000) {
            return 30;
        }
        if (placeIdObj > 20000) {
            return 20;
        }
        if (placeIdObj > 15000) {
            return 18;
        }
        if (placeIdObj > 12000) {
            return 15;
        }
        if (placeIdObj > 10000) {
            return 12;
        }
        if (placeIdObj > 8000) {
            return 10;
        }
        if (placeIdObj > 6000) {
            return 8;
        }
        return placeIdObj > 4000 ? 6 : 5;
    }

    private int calcularEntregaUltraRapida(double la, double lo) {
        return calcularEntregaRapida(la, lo) * 2;
    }

    private int calcularEntregaFacil(PlaceIdObj placeIdObj) {
        return ((int) SphericalUtil.computeDistanceBetween(new LatLng(-3.1174754000000005d, -60.001966200000005d), new LatLng(placeIdObj.getLat(), placeIdObj.getLng()))) > 10000 ? 5 : 3;
    }

    public void verProximidades(View view) {
        showCurrentPlace();
    }
}
