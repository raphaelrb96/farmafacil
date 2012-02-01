package revolution.ph.developer.remediofacil;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static revolution.ph.developer.remediofacil.MainActivity.ids;

public class FragmentMain extends Fragment implements AdapterProdutos.ClickProdutoCliente {

    private BottomSheetBehavior<LinearLayout> sheetBehavior;
    private View filterIcon;
    private FirebaseFirestore firestore;

    public static FrameLayout containerFrag;
    public static RecyclerView mListMercadorias;
    public static FragmentManager manager;
    private FirebaseAuth auth;
    private CollectionReference carrinhoDoUsuario;
    private AdapterProdutos mAdapter;
    private CarComprasActivy mComprasActivy;
    private String myUserAtual;
    private int referencia = 0;
    private ArrayList<ProdObj> prodObjs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        mListMercadorias = (RecyclerView) view.findViewById(R.id.rv_fragment_main);

        filterIcon = coordinatorLayout.findViewById(R.id.filterIcon);
        View btChat = (View) view.findViewById(R.id.bt_abrir_chat);
        View btcar = (View) view.findViewById(R.id.bt_abrir_carrinho);
        LinearLayout contentLayout = coordinatorLayout.findViewById(R.id.contentLayout);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        carrinhoDoUsuario = FirebaseFirestore.getInstance().collection("carComprasActivy").document("usuario").collection(auth.getCurrentUser().getUid());

        sheetBehavior = BottomSheetBehavior.from(contentLayout);
        sheetBehavior.setFitToContents(false);
        sheetBehavior.setHideable(false);//prevents the boottom sheet from completely hiding off the screen
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);//initially state to fully expanded

        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFilters();
            }
        });

        btcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CarrinhoActivity.class));
            }
        });

        prodObjs = new ArrayList<>();


        btChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MensagemActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        firestore.collection("carComprasActivy").document("usuario").collection(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (int i = 0; i < querySnapshot.getDocuments().size(); i++) {
                    String id = querySnapshot.getDocuments().get(i).getId();
                    if (!ids.contains(id)) {
                        ids.add(id);
                    }
                }

                firestore.collection("produtos").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                                ProdObj prod = queryDocumentSnapshots.getDocuments().get(i).toObject(ProdObj.class);
                                prodObjs.add(prod);
                            }


                            mAdapter = new AdapterProdutos(FragmentMain.this, getActivity(), prodObjs);
                            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                            mListMercadorias.setLayoutManager(layoutManager);
                            mListMercadorias.setAdapter(mAdapter);
                        }
                    }
                });

//                            if (FragmentMercadoriasList.this.referencia != 4) {
//                                FirebaseFirestore.getInstance().collection(Constantes.FIREBASE_KEY_PRODUTS).whereEqualTo("categoria", (Object) Integer.valueOf(FragmentMercadoriasList.this.referencia)).get().addOnSuccessListener(new C17871());
//                            } else {
//                                FirebaseFirestore.getInstance().collection(Constantes.FIREBASE_KEY_PRODUTS).whereEqualTo("promocional", (Object) Boolean.valueOf(true)).get().addOnSuccessListener(new C17892());
//                            }
            }
        });
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
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.findViewById(R.id.fab_produto_item).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab1)));
        }
        if (ids.contains(str)) {
            ids.remove(str);
        }
        reference.delete();
    }

    @Override
    public void openChat() {

    }
}
