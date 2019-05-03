package revolution.ph.developer.remediofacil;

import android.app.Fragment;
import android.app.MediaRouteButton;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static revolution.ph.developer.remediofacil.MainActivity.ids;

public class FragmentMain extends Fragment implements AdapterProdutos.ClickProdutoCliente, FacebookCallback<LoginResult> {

    private static final int RC_SIGN_IN = 124;
    private static final int RC_SIGN_IN_ADM = 567;
    private BottomSheetBehavior<LinearLayout> sheetBehavior;
    private FirebaseFirestore firestore;

    public static FrameLayout containerFrag;
    public static RecyclerView mListMercadorias;
    public static FragmentManager manager;
    private LinearLayout containerLogin;
    private ScrollView containerMenu;
    private FirebaseAuth auth;
    private ExtendedFloatingActionButton efabFace, efabGoogle, efabCart;
    private CollectionReference carrinhoDoUsuario;
    private AdapterProdutos mAdapter;
    private CarComprasActivy mComprasActivy;
    private String myUserAtual;
    private int referencia = 0;
    private ArrayList<ProdObj> prodObjs;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private ImageView imgPerfil;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static FirebaseUser user = null;
    public static String pathFotoUser;
    private ProgressBar pb;
    private ImageButton btPesquisar;
    private TextView tvErro;
    private FrameLayout toolbar;

    private LinearLayout btOfertas, btMedicamentos, btPerfumaria, btSuplementos, btSair;

    private EditText etpesquisar;

    private int tipoReferencia = 4;
    private Query query;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        mListMercadorias = (RecyclerView) view.findViewById(R.id.rv_fragment_main);
        toolbar = (FrameLayout) view.findViewById(R.id.toolbar_main);
        etpesquisar= (EditText) view.findViewById(R.id.et_pesquisar);
        containerLogin = (LinearLayout) view.findViewById(R.id.container_login);
        btMedicamentos = (LinearLayout) view.findViewById(R.id.ll_bt_medicamentos);
        btSuplementos = (LinearLayout) view.findViewById(R.id.ll_bt_suplemento);
        btSair = (LinearLayout) view.findViewById(R.id.ll_bt_sair);
        btPerfumaria = (LinearLayout) view.findViewById(R.id.ll_bt_perfumaria);
        btOfertas = (LinearLayout) view.findViewById(R.id.ll_bt_ofertas);
        containerMenu = (ScrollView) view.findViewById(R.id.container_menu);
        btPesquisar = (ImageButton) view.findViewById(R.id.bt_pesquisar);

        //filterIcon = coordinatorLayout.findViewById(R.id.filterIcon);
        pb = (ProgressBar) view.findViewById(R.id.pb_main);
        efabGoogle = (ExtendedFloatingActionButton) view.findViewById(R.id.efab_login_google);
        efabFace = (ExtendedFloatingActionButton) view.findViewById(R.id.efab_login_face);
        efabCart = (ExtendedFloatingActionButton) view.findViewById(R.id.efab_cart);
        imgPerfil = (ImageView) view.findViewById(R.id.img_perfil);
        tvErro = (TextView) view.findViewById(R.id.tv_error_main);
        //View btChat = (View) view.findViewById(R.id.bt_abrir_chat);
        //View btcar = (View) view.findViewById(R.id.bt_abrir_carrinho);
        LinearLayout contentLayout = coordinatorLayout.findViewById(R.id.contentLayout);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        query = firestore.collection("produtos");

        sheetBehavior = BottomSheetBehavior.from(contentLayout);
        //sheetBehavior.setFitToContents(false);
        //sheetBehavior.setHideable(false);
        //prevents the boottom sheet from completely hiding off the screen
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        //initially state to fully expanded

        prodObjs = new ArrayList<>();


        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("750810743457-iq30b2ja17fp0ijtijom4hbfns8uroak.apps.googleusercontent.com")
                .requestProfile()
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        LoginManager.getInstance().registerCallback(callbackManager, FragmentMain.this);

        ligarOuvintes();

        return view;
    }

    private void ligarOuvintes() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                auth = firebaseAuth;
                user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    onSignedInInitialize();

                    if (user.isAnonymous()) {
                        efabCart.setVisibility(View.GONE);
                        toggleBackContainer(false);
                        obterListaDeProdutos(tipoReferencia);
                        return;
                    } else {
                        toggleBackContainer(true);
                        carregarFotoPerfil();
                        getListCart();
                    }

                } else {

                    toggleBackContainer(false);
                    ids.clear();
                    ids = new ArrayList();
                    if (isDeviceOnline()) {
                        //loginAdmin();
                        //ou
                        loginUsuarioAnonimo();
                        return;
                    } else {
                        //exibir interface vazia
                        telaInicialErro("Baixa conexão com a internet");
                    }
                }
            }
        };

        btPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pesq = etpesquisar.getText().toString();
                if (pesq.length() > 0) {
                    pesquisar(pesq);
                }
            }
        });

        efabFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(FragmentMain.this, Arrays.asList("public_profile", "email"));
            }
        });

        efabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar snackbar = null;

                if (user.isAnonymous()) {
                    showDialog(1);
                } else if (!isDeviceOnline()) {
                    showDialog(2);
                } else if (ids.size() == 0) {
                    showDialog(3);
                } else {
                    startActivity(new Intent(getActivity(), CarrinhoActivity.class));
                }

            }
        });

        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });

        btOfertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obterListaDeProdutos(4);
            }
        });

        btPerfumaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obterListaDeProdutos(3);
            }
        });

        btSuplementos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obterListaDeProdutos(2);
            }
        });

        btMedicamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obterListaDeProdutos(1);
            }
        });

        efabGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    startActivity(new Intent(getActivity(), MensagemDetalheActivity.class).putExtra("id", user.getUid()));
                }
            }
        });

        etpesquisar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etpesquisar.setFocusable(true);
                etpesquisar.setFocusableInTouchMode(true);
                return false;
            }
        });

        etpesquisar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String pesq = etpesquisar.getText().toString();
                    if (pesq.length() > 0) {
                        pesquisar(pesq);
                    }
                    return true;
                }
                return false;
            }
        });

        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdmActivity.class));
            }
        });

    }

    private void onSignedInInitialize() {
        carrinhoDoUsuario = FirebaseFirestore.getInstance().collection("carComprasActivy").document("usuario").collection(user.getUid());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

            }
            Log.d("TesteLogin", "Google");
        } else if (requestCode == RC_SIGN_IN_ADM) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                //abrirMensagem();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        } else {
            Log.d("TesteLogin", "Face");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        telaInicialLoadding();
        auth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.mAuthStateListener != null) {
            auth.removeAuthStateListener(this.mAuthStateListener);
        }
    }

    private void showDialog(int tipo) {
        switch (tipo) {
            case 1:
                AlertDialog.Builder dialogAnonimus = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialog)
                        .setTitle("Atenção")
                        .setMessage("Faça login para poder ter seu carrinho de compras")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            }
                        });
                AlertDialog alertDialogAnonimus = dialogAnonimus.create();
                alertDialogAnonimus.show();
                alertDialogAnonimus.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryDark));
                break;
            case 2:
                AlertDialog.Builder dialogOffline = new AlertDialog.Builder(getActivity())
                        .setTitle("Atenção")
                        .setMessage("Baixa conectividade. Verifique sua internet e tente novamente")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialogOff = dialogOffline.create();
                alertDialogOff.show();
                alertDialogOff.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryDark));
                break;
            case 3:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Atenção")
                        .setMessage("Seu carrinho está vazio")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryDark));
                break;
            default:
                break;
        }
    }

    private void carregarFotoPerfil() {
        if (auth == null || auth.getCurrentUser() == null) {
            return;
        }
        for(int xis = 0; xis < auth.getCurrentUser().getProviderData().size(); xis++) {
            try {
                String xs = auth.getCurrentUser().getProviderData().get(xis).getPhotoUrl().toString();

                if (auth.getCurrentUser().getProviderData().get(xis).getProviderId().equals("facebook.com")) {
                    xs = auth.getCurrentUser().getProviderData().get(xis).getPhotoUrl().toString() + "?type=large&redirect=true&width=500&height=500";
                }
                Log.d("FotoPerfil", xs);

                pathFotoUser = xs;
                Glide.with(getActivity()).load(xs).into(imgPerfil);
            } catch (NullPointerException e) {

            }

        }
    }

    private void toggleFilters(){
        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        }
        else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void openDetalhe(ProdObj prodObj) {

    }

    @Override
    public void onclick(int i, ColorStateList colorStateList, View view, ProdObj prodObj) {
        if (auth.getCurrentUser().isAnonymous()) {

            return;
        }
        String str = prodObj.getIdProduto();
        CarComprasActivy carComprasActivy = new CarComprasActivy(str, prodObj.getProdName(), prodObj.getLaboratorio(), prodObj.getImgCapa(), prodObj.getProdValor(), 1, ((float) 1) * prodObj.getProdValor());
        DocumentReference reference = carrinhoDoUsuario.document(str);
        if (colorStateList == ColorStateList.valueOf(getActivity().getResources().getColor(R.color.fab1))) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.findViewById(R.id.fab_produto_item).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
            }
            if (!ids.contains(str)) {
                ids.add(str);
            }
            reference.set(carComprasActivy);
            mAdapter.notifyItemChanged(i);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.findViewById(R.id.fab_produto_item).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab1)));
        }
        if (ids.contains(str)) {
            ids.remove(str);
        }
        reference.delete();
        mAdapter.notifyItemChanged(i);
    }

    @Override
    public void openChat() {
        startActivity(new Intent(getActivity(), MensagemActivity.class));
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        handleFacebookAccessToken(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onError(FacebookException error) {

    }

    private void loginAdmin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN_ADM);
    }

    private void loginUsuarioAnonimo() {
        auth.signInAnonymously()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG_Sucess", "signInAnonymously:success");
                            FirebaseUser user = auth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            telaInicialErro("Erro ao carregar lista de produtos...\n Tente Novamente");
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void myQuery(Query meuQuery, final boolean isPesquisa) {
        prodObjs.clear();

        meuQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {

                    final ArrayList<ProdObj> list = new ArrayList<>();

                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        ProdObj prod = queryDocumentSnapshots.getDocuments().get(i).toObject(ProdObj.class);
                        list.add(prod);
                    }


                    mAdapter = new AdapterProdutos(FragmentMain.this, getActivity(), list);
                    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    mListMercadorias.setLayoutManager(layoutManager);
                    mListMercadorias.setAdapter(mAdapter);
                    telaInicialSucesso();
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    return;
                }

                if (isPesquisa && queryDocumentSnapshots.size() == 0){
                    Toast.makeText(getActivity(), "Nenhum resultado para sua pesquisa", Toast.LENGTH_LONG).show();
                    obterListaDeProdutos(4);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                telaInicialErro("Erro ao carregar lista de produtos...\n Tente Novamente");
            }
        });
    }

    private void obterListaDeProdutos(int tipo) {
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        telaInicialLoadding();
        if (tipo != 4) {
            myQuery(firestore.collection("produtos").whereEqualTo("categoria", tipo), false);
        } else {
            myQuery(firestore.collection("produtos").whereEqualTo("promocional", true), false);
        }

    }

    private void esconderTeclado() {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(etpesquisar.getWindowToken(), 0);
    }

    private void pesquisar(String s) {
        String busca = "tag." + s.toLowerCase();
        telaInicialLoadding();
        esconderTeclado();
        myQuery(firestore.collection("produtos").whereEqualTo(busca, true), true);
    }

    private void getListCart () {
        carrinhoDoUsuario.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot querySnapshot) {
                            for (int i = 0; i < querySnapshot.getDocuments().size(); i++) {
                                String id = querySnapshot.getDocuments().get(i).getId();
                                if (!ids.contains(id)) {
                                    ids.add(id);
                                }
                            }

                            obterListaDeProdutos(tipoReferencia);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                telaInicialErro("Erro ao carregar lista de produtos...\n Tente Novamente");
            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult != null) {
                            FirebaseUser user = authResult.getUser();
                            Log.d("TesteLogin", "Goglle sucess");
                            updateUI(user);
                        } else {
                            Log.d("TesteLogin", "GoogleErro");
                            updateUI(null);
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult != null) {
                            FirebaseUser user = authResult.getUser();
                            updateUI(user);
                            Log.d("TesteLogin", "Facesucess");
                        } else {
                            updateUI(null);
                            Log.d("TesteLogin", "Faceerro");
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser firebaseUser) {
        Timber.d("UpdateUI");
        if (firebaseUser != null) {
            Log.d("TesteLogin", "UpdateUISucesso");
            //tirar container login e subir a lista de produtos
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            carregarFotoPerfil();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private boolean isDeviceOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void telaInicialLoadding() {
        mListMercadorias.setVisibility(View.GONE);
        efabCart.setVisibility(View.GONE);
        tvErro.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
    }

    private void telaInicialErro(String erro) {
        mListMercadorias.setVisibility(View.GONE);
        efabCart.setVisibility(View.GONE);
        tvErro.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);
        tvErro.setText(erro);
    }

    private void telaInicialSucesso() {
        mListMercadorias.setVisibility(View.VISIBLE);
        efabCart.setVisibility(View.VISIBLE);
        tvErro.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);
    }

    private void toggleBackContainer(boolean logado) {
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if(logado) {
            containerLogin.setVisibility(View.GONE);
            containerMenu.setVisibility(View.VISIBLE);
        } else {
            containerLogin.setVisibility(View.VISIBLE);
            containerMenu.setVisibility(View.GONE);
        }
    }

}
