package revolution.ph.developer.remediofacil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 176;
    private FirebaseUser user;
    public static ArrayList<String> ids = new ArrayList();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private CollectionReference carrinhoDoUsuario;
    private DocumentReference ref;
    private EventListener<QuerySnapshot> refCarrinho;
    private EventListener<DocumentSnapshot> refToken;
    private TextView semInternet;
    private EventListener<DocumentSnapshot> statusDrogaria;
    public static FloatingActionButton fab;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        fab = (FloatingActionButton) findViewById(R.id.fab_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (firebaseAuth != null) {
                    //DrogariaActivity.mUsuario = firebaseAuth;
//                    DrogariaActivity.mUsuarioUID = firebaseAuth.getUid();
//                    DrogariaActivity.this.meuUserAtual = firebaseAuth.getUid();
                    onSignedInInitialize();
//                    DrogariaActivity.this.semInternet.setVisibility(8);
                    return;
                }
                onSignedOutCleanup();
                ids.clear();
                ids = new ArrayList();
                if (isDeviceOnline()) {
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.PhoneBuilder().build());

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                    return;
                }
                //DrogariaActivity.lb.setVisibility(8);
//                DrogariaActivity.this.semInternet.setVisibility(0);
            }
        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CarrinhoActivity.class));
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.mAuthStateListener != null) {
            this.mAuth.removeAuthStateListener(this.mAuthStateListener);
        }
        dettachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
//        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//            Fragment fragmentMercadoriasList = new FragmentMercadoriasList();
//            fragmentMercadoriasList.setReferencia(4);
//            getSupportFragmentManager().beginTransaction().replace(C1270R.id.container_main, fragmentMercadoriasList).commitAllowingStateLoss();
//        }
        //FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        this.carrinhoDoUsuario = FirebaseFirestore.getInstance().collection("carComprasActivy").document("usuario").collection(user.getUid());
        this.ref = FirebaseFirestore.getInstance().collection("fcmToken").document(user.getUid());
        this.refCarrinho = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (querySnapshot == null) {
                    return;
                }
                if (querySnapshot.getDocuments().size() > 0) {
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
                    for (int i = 0; i < querySnapshot.getDocuments().size(); i++) {
                        String id = querySnapshot.getDocuments().get(i).getId();
                        if (!ids.contains(id)) {
                            ids.add(id);
                        }
                    }
                } else {
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab1)));
                }
            }
        };
        //this.refToken = new C17686();
        this.statusDrogaria = new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                if (((StatusDrogariaObj) documentSnapshot.toObject(StatusDrogariaObj.class)).isAberto() == null) {
//                    DrogariaActivity.lb.setVisibility(8);
//                    DrogariaActivity.this.fab.setVisibility(8);
//                    DrogariaActivity.this.containerMain.setVisibility(8);
//                    DrogariaActivity.this.drogariaFechada.setVisibility(0);
//                    return;
//                }
//                DrogariaActivity.this.fab.setVisibility(0);
//                DrogariaActivity.this.drogariaFechada.setVisibility(8);
//                DrogariaActivity.this.containerMain.setVisibility(0);
            }
        };
        FirebaseFirestore.getInstance().collection("statusDrogaria").document("chave").addSnapshotListener(MainActivity.this, this.statusDrogaria);
        this.carrinhoDoUsuario.addSnapshotListener(MainActivity.this, this.refCarrinho);
        //this.ref.addSnapshotListener(MainActivity.this, this.refToken);
    }

    private void onSignedOutCleanup() {
        dettachDatabaseReadListener();
    }

    private void onSignedInInitialize() {
        attachDatabaseReadListener();
    }

    private void dettachDatabaseReadListener() {
        //FirebaseMessaging.getInstance().setAutoInitEnabled(false);
    }

    private boolean isDeviceOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void abrirMensagem() {
        Toast.makeText(this, user.getPhoneNumber(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MensagemActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                abrirMensagem();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

}
