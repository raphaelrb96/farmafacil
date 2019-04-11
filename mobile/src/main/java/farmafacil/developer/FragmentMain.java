package farmafacil.developer;

import android.app.Fragment;
import android.content.Context;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class FragmentMain extends Fragment implements AdapterProdutos.ClickProdutoCliente {

    private BottomSheetBehavior<LinearLayout> sheetBehavior;
    private View filterIcon;
    private FirebaseFirestore firestore;

    public static FrameLayout containerFrag;
    public static RecyclerView mListMercadorias;
    public static FragmentManager manager;
    public static FragmentTransaction transaction;
    private FirebaseAuth auth;
    private CollectionReference carrinhoDoUsuario;
    private FirebaseFirestore firebaseFirestore;
    private AdapterProdutos mAdapter;
    private CarComprasActivy mComprasActivy;
    private String myUserAtual;
    private Query ref;
    private int referencia = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        mListMercadorias = (RecyclerView) view.findViewById(R.id.rv_fragment_main);

        filterIcon = coordinatorLayout.findViewById(R.id.filterIcon);
        LinearLayout contentLayout = coordinatorLayout.findViewById(R.id.contentLayout);

        firestore = FirebaseFirestore.getInstance();

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

        final ArrayList<ProdObj> prodObjs = new ArrayList<>();

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

        return view;
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
}
