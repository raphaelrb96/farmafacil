package revolution.ph.developer.remediofacilmanaus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import revolution.ph.developer.remediofacilmanaus.analitycs.AnalitycsFacebook;
import revolution.ph.developer.remediofacilmanaus.analitycs.AnalitycsGoogle;
import revolution.ph.developer.remediofacilmanaus.objects.CompraFinalParcelable;
import revolution.ph.developer.remediofacilmanaus.objects.UserStreamView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
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

import static revolution.ph.developer.remediofacilmanaus.FragmentMain.ADMINISTRADOR;
import static revolution.ph.developer.remediofacilmanaus.FragmentMain.pathFotoUser;
import static revolution.ph.developer.remediofacilmanaus.FragmentMain.user;
import static revolution.ph.developer.remediofacilmanaus.MainActivity.ids;

public class CarrinhoActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter, AdapterCart.AnalizarClickPayFinal, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

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

    private boolean proximidadeSelected = false;

    private String ruaMain = null;

    public static ArrayList<Float> valores = new ArrayList();
    private int somo = 0;

    private AdapterCart adapter;

    private TextView tv_nome_rua_cart, btAjuda;

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

    private TextInputEditText et_numero_casa;

    private FloatingActionButton fab;
    private FrameLayout containerHelp;
    private AnalitycsFacebook analitycsFacebook;
    private AnalitycsGoogle analitycsGoogle;
    private DocumentReference usuarioRef;
    private CollectionReference enderecosColecao;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LocationCallback locationCallback;

    private boolean ATRASAR = false;
    private int numero = 15;
    private ExtendedFloatingActionButton efabCurrentPlace;
    private TextInputEditText et_complemento;

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
    protected void onStop() {
        super.onStop();
        mMap.clear();
        mMap = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        cart.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (querySnapshot == null) return;
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
                stringBuilder.append(String.valueOf(somo));
                stringBuilder.append(",00");
                totalTV.setText(stringBuilder.toString());
                ttcomprasTV.setText(String.valueOf(somo) + ",00");
                if (querySnapshot.getDocuments().size() == 0) {
//                    PayFinalActivity.this.emptyCar.setVisibility(0);
//                    PayFinalActivity.this.mPayList.setVisibility(8);
                    finish();
                } else {
//                    PayFinalActivity.this.emptyCar.setVisibility(8);
//                    PayFinalActivity.this.mPayList.setVisibility(0);
                }
                adapter.swapDados(produtoss);
            }
        });

        if (!ADMINISTRADOR) {
            analitycsFacebook.logUserVisitaCarrinhoEvent(user.getDisplayName(), user.getUid(), pathFotoUser);
            analitycsGoogle.logUserVisitaCarrinhoEvent(user.getDisplayName(), user.getUid(), pathFotoUser);
            UserStreamView userStreamView = new UserStreamView(user.getDisplayName(), user.getUid(), pathFotoUser, System.currentTimeMillis());
            firestore.collection("Eventos").document("stream").collection("cart").document(user.getUid()).set(userStreamView);
        }
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
        efabCurrentPlace = (ExtendedFloatingActionButton) findViewById(R.id.efab_current_place);
        tv_nome_rua_cart = (TextView) findViewById(R.id.tv_nome_rua_cart);
//        tv_exemplo_help = (TextView) findViewById(R.id.tv_exemplo_help);
        btAjuda = (TextView) findViewById(R.id.bt_mudar_endereco);
        taxaEntregaTV = (TickerView) findViewById(R.id.taxa_entrega);
        totalTV = (TickerView) findViewById(R.id.total_cart);
        ttcomprasTV = (TickerView) findViewById(R.id.total_compras);
        btVoltar = (View) findViewById(R.id.bt_voltar_cart);
        closeHelp = (View) findViewById(R.id.close_help);
        pb = (ProgressBar) findViewById(R.id.pb_cart);
        et_numero_casa = (TextInputEditText) findViewById(R.id.et_numero_casa);
        et_complemento = (TextInputEditText) findViewById(R.id.et_complemento);
        taxaEntregaTV.setCharacterList(TickerUtils.getDefaultNumberList());
        totalTV.setCharacterList(TickerUtils.getDefaultNumberList());
        ttcomprasTV.setCharacterList(TickerUtils.getDefaultNumberList());
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout_cart);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        analitycsFacebook = new AnalitycsFacebook(this);
        analitycsGoogle = new AnalitycsGoogle(this);
        usuarioRef = firestore.collection("Usuario").document(user.getUid());
        enderecosColecao = firestore.collection("Enderecos").document("Clientes").collection(user.getUid());
        cart = firestore.collection("carComprasActivy").document("usuario").collection(auth.getCurrentUser().getUid());

        content_layout_cart = (LinearLayout) findViewById(R.id.content_layout_cart);

        if (savedInstanceState != null) {
//            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
//            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        mDefaultLocation = new LatLng(-3.10719, -60.0261);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setHint("Sua rua, número da sua casa");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                tv_nome_rua_cart.setText(place.getAddress());
                proximidadeSelected = false;
                if (mMap != null) {
                    mMap.clear();
                    autoCompleteLocation = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                    mDefaultLocation = autoCompleteLocation;
                    float factor = getResources().getDisplayMetrics().density;
                    int h = (int) (450 * factor);
                    int top = (int) (80 * factor);
                    mMap.setPadding(0, top, 0, h);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(autoCompleteLocation, 18));
                    marcar();
                    analitycsFacebook.logUserPesquisouEnderecoEvent(user.getDisplayName(), user.getUid(), pathFotoUser, place.getAddress().toString());
                    analitycsGoogle.logUserPesquisouEnderecoEvent(user.getDisplayName(), user.getUid(), pathFotoUser, place.getAddress().toString());
                    //definirNovoEndereco(exibirEnderecoAtual(), autoCompleteLocation);
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

        efabCurrentPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gpsLigado()) {
                    autoCompleteLocation = null;
                    proximidadeSelected = false;
                    getDeviceLocation();
                }
            }
        });

        et_numero_casa.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    esconderTeclado(et_numero_casa);
                    return true;
                }
                return false;
            }
        });

        et_complemento.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    esconderTeclado(et_complemento);
                    return true;
                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String complementoString = et_complemento.getText().toString();
                String numeroString = et_numero_casa.getText().toString();
                if (numeroString.equals("")) {
                    Toast.makeText(CarrinhoActivity.this, "Insira o numero da casa", Toast.LENGTH_LONG).show();
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    containerHelp.setVisibility(View.VISIBLE);
                    return;
                }

//                if (complementoString.equals("")) {
//                    Toast.makeText(CarrinhoActivity.this, "Insira o complemento no seu endereço", Toast.LENGTH_LONG).show();
//                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                    return;
//                }
                Intent intent = new Intent(CarrinhoActivity.this, ConfirmarCompraActivity.class);
                if (somo != 0 && ruaMain != null) {

                    int itens = 0;

                    for (int i = 0; i < produtoss.size(); i++) {
                        int quant = produtoss.get(i).getQuantidade();
                        itens = itens + quant;
                    }

                    String sFinal = ruaMain + ", " + numeroString;

                    if (!complementoString.equals("")) {
                        sFinal = sFinal + " - " + complementoString;
                    }

                    CompraFinalParcelable cfp = new CompraFinalParcelable(sFinal, mDefaultLocation.latitude, mDefaultLocation.longitude, itens , user.getUid(), user.getDisplayName(), pathFotoUser, precoEntrega, somo);
                    intent.putExtra("cfp", cfp);
                    startActivity(intent);
                }
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

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };

    }

    private void esconderTeclado(TextInputEditText et) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mMap != null) {
            mMap.clear();
        }

        mapFragment.getMapAsync(this);


//        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                //Log.d("GPS_TESTE", "onLocationChanged");
//                //chamado varias vezes por segund
//
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//                //Log.d("GPS_TESTE", "onStatusChanged");
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//                Log.d("GPS_TESTE", "onProviderEnabled");
//                if (mMap == null) {
//                    mapFragment.getMapAsync(CarrinhoActivity.this);
//                }
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//                Log.d("GPS_TESTE", "onProviderDisabled");
//                ligarGps();
//            }
//        };
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        } else {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }
    }


    private void definirNovoEndereco(Address addressFilho, LatLng latLng) {
        mDefaultLocation = latLng;

        if (addressFilho != null) {
            Address address = addressFilho;
            DocumentReference docRef = enderecosColecao.document();
            exibirEnderecoAtual();
//            Endereco endereco = new Endereco(address.getAddressLine(0), address.getThoroughfare(), latLng.latitude, latLng.longitude, 0, address.toString(), docRef.getId(), System.currentTimeMillis());
//            docRef.set(endereco);
        }
    }

    //margem do mapa = tamanho do linear layout container

    private void ligarGps() {
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY))
                .addLocationRequest(LocationRequest.create().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY));
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        Log.d("GPS_TESTE", "task");

        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.d("GPS_TESTE", "sucess");
                //mapFragment.getMapAsync(this);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                Log.d("GPS_TESTE", "falha");
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
//                        mapFragment.onResume();
                        Log.d("GPS_TESTE", "result ok");
                        ATRASAR = true;
                        updateLocationUI();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
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

        Log.d("GPS_TESTE", "onMapReady");

        //pb.setVisibility(View.GONE);

        if (mMap != null) {
            mMap.clear();
        }

        mMap = googleMap;
        //mMap.setOnMarkerClickListener(CarrinhoActivity.this);
        mMap.setOnMarkerDragListener(CarrinhoActivity.this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 13));
        float factor = getResources().getDisplayMetrics().density;
        int h = (int) (450 * factor);
        int top = (int) (100 * factor);

        mMap.setPadding(0, top, 0, h);
        mMap.setInfoWindowAdapter(this);

//        Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        if (!gpsLigado()) {
            ligarGps();
            return;
        }

        updateLocationUI();

        getDeviceLocation();


    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                getDeviceLocation();
            } else {
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

        pb.setVisibility(View.VISIBLE);

        Log.d("GPS_TESTE", "GetdeviceLocation");

        if (autoCompleteLocation != null) {
            pb.setVisibility(View.GONE);
            exibirEnderecoAtual();
            float factor = getResources().getDisplayMetrics().density;
            int h = (int) (450 * factor);
            int top = (int) (80 * factor);

            mDefaultLocation = autoCompleteLocation;

            mMap.setPadding(0, top, 0, h);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(autoCompleteLocation, 18));
            marcar();
            return;
        }

        if (proximidadeSelected) {
            pb.setVisibility(View.GONE);
            exibirEnderecoAtual();
            float factor = getResources().getDisplayMetrics().density;
            int h = (int) (450 * factor);
            int top = (int) (80 * factor);

            mMap.setPadding(0, top, 0, h);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 18));
            marcar();
            return;
        }


        try {
            if (mLocationPermissionGranted) {
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(CarrinhoActivity.this);
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(CarrinhoActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            Log.d("GPS_TESTE", "GetdeviceLocation ultimo local null");
                            if (ATRASAR) {
                                numero =15;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getDeviceLocation();
                                    }
                                }, 15000);
                            } else {
                                getDeviceLocation();
                            }

                            return;
                        }
                        pb.setVisibility(View.GONE);
                        ATRASAR =false;

                        mLastKnownLocation = location;

                        mDefaultLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                        exibirEnderecoAtual();
                        float factor = getResources().getDisplayMetrics().density;
                        int h = (int) (450 * factor);
                        int top = (int) (80 * factor);

                        mMap.setPadding(0, top, 0, h);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 18));
                        marcar();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exceptionnn: %s", e.getMessage());
        } catch (NullPointerException e) {
            Log.e("Exceptionnn: %s", e.getMessage());
        }
    }

    public static Bitmap createCustomMarker(Context context, Bitmap resource, String _name) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_user, null);

        ImageView markerImage = (ImageView) marker.findViewById(R.id.img_perfil_mapa);
        markerImage.setImageBitmap(resource);
        TextView txt_name = (TextView) marker.findViewById(R.id.nome_marker_user);
        txt_name.setText(_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }

    private void marcar() {

        if (mMap == null) {
            return;
        }

        mMap.clear();
        float factor = getResources().getDisplayMetrics().density;
        int h = (int) (450 * factor);
        int top = (int) (100 * factor);
        mMap.setPadding(0, top, 0, h);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 18));
        mMap.addMarker(new MarkerOptions()
                .title(user.getDisplayName())
                .position(mDefaultLocation)
                .draggable(true)
                .snippet("Precione e arraste")
        );
    }

    private String formatarAddress(String endereco) {
        String novaRua = "";
        for (int i = 0; i < endereco.length(); i++) {
            char cha = endereco.charAt(i);
            String s = String.valueOf(cha);
            if (!s.equals("-")) {
                novaRua = novaRua + s;
            } else {
                return novaRua;
            }
        }
        return novaRua;
    }

    private Address exibirEnderecoAtual() {

        if (autoCompleteLocation != null) {
            mDefaultLocation = autoCompleteLocation;
        }

        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(mDefaultLocation.latitude, mDefaultLocation.longitude, 1);
            if (addresses != null) {
                String rua = addresses.get(0).getThoroughfare();
                String endereco = addresses.get(0).getAddressLine(0);
                //String novaRua = formatarAddress(endereco);
                ruaMain = rua;
                autocompleteFragment.setText(rua);
                tv_nome_rua_cart.setText(rua);
//                tv_exemplo_help.setText(rua + ", 10");
                precoEntrega = calcularEntregaRapida(mDefaultLocation.latitude, mDefaultLocation.longitude);
                String valorDaEntrega = String.valueOf(precoEntrega) + ",00";
                String valorDasCompras = String.valueOf(somo) + ",00";
                int tt = precoEntrega + somo;
                String total = String.valueOf(somo) + ",00";
                totalTV.setText(total);
                ttcomprasTV.setText(valorDasCompras);
                taxaEntregaTV.setText(valorDaEntrega);

                return addresses.get(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
            //exibirEnderecoAtual();
        }

        return null;

    }

    private void showCurrentPlace() {

        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            pb.setVisibility(View.VISIBLE);
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
                                pb.setVisibility(View.GONE);
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
            marcar();

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
                marcar();

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation,
                        DEFAULT_ZOOM));

                proximidadeSelected = true;

                exibirEnderecoAtual();

                //definirNovoEndereco(exibirEnderecoAtual(), mDefaultLocation);

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
        //todo 01-- implementar um dialogo para o usuario apenas escrever o endereço, caso não consiga
        //todo 01-- cadastrar pelo gps, e isso deixara a taxa mais alta
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        showCurrentPlace();
        return true;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (autoCompleteLocation != null) {
            autoCompleteLocation = null;
        }
        mDefaultLocation = marker.getPosition();
        marcar();
        exibirEnderecoAtual();
    }

    private Bitmap iconeUsuario(Bitmap scaleBitmapImage) {
        int targetWidth = 110;
        int targetHeight = 110;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);

        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2, ((float) targetHeight - 1) / 2, (Math.min(((float) targetWidth), (float) targetHeight)) / 2, Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;

        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()), new Rect(0, 0, targetWidth, targetHeight), new Paint(Paint.FILTER_BITMAP_FLAG));

        return targetBitmap;
    }
}
