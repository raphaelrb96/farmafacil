package revolution.ph.developer.remediofacil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.annotation.Nullable;

public class InventarioActivity extends AppCompatActivity implements AdapterProdutosInventario.InventarioListener {

    private RecyclerView rv;
    private ProgressBar pb;
    private FirebaseFirestore firestore;
    private CollectionReference refProdutos;
    private AdapterProdutosInventario adapter;
    private ArrayList<ProdObj> produtos;
    private boolean modoSecreto = false;
    private QuerySnapshot mQueryDocumentSnapshots = null;
    private Toolbar toolbar;
    private CheckBox checkBox;
    private View voltar;
    private EditText etPesquisa;
    private ImageButton btPesquisar;
    private boolean automatico = false;
    private boolean isPesquisa = false;
    private Toast mToats;

    @Override
    protected void onStart() {
        super.onStart();
        refProdutos.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                mQueryDocumentSnapshots = queryDocumentSnapshots;
                lista(true);
                carregarListaMain(queryDocumentSnapshots);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);
        rv = (RecyclerView) findViewById(R.id.rv_inventario);
        pb = (ProgressBar) findViewById(R.id.pb_inventario);
        voltar = (View) findViewById(R.id.bt_voltar_inventario);
        checkBox = (CheckBox) findViewById(R.id.cb_inventtario);
        etPesquisa = (EditText) findViewById(R.id.et_pesquisar_inventario);
        btPesquisar = (ImageButton) findViewById(R.id.bt_pesquisar_inventario);
        automatico = false;
        mQueryDocumentSnapshots = null;
        firestore = FirebaseFirestore.getInstance();
        refProdutos = firestore.collection("produtos");
//        refProdutos.addSnapshotListener(InventarioActivity.this, new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//
//            }
//        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //automatico = isChecked;
                if (mQueryDocumentSnapshots != null) {
                    if (isChecked) {
                        modoSecreto = true;
                    } else {
                        modoSecreto = false;
                    }

                    carregarListaMain(mQueryDocumentSnapshots);
                }
            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPesquisa && mQueryDocumentSnapshots != null) {
                    isPesquisa = false;
                    etPesquisa.setText("");
                    esconderTeclado();
                    carregarListaMain(mQueryDocumentSnapshots);
                    return;
                }
                onBackPressed();
            }
        });

        etPesquisa.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etPesquisa.setFocusable(true);
                etPesquisa.setFocusableInTouchMode(true);
                return false;
            }
        });

        etPesquisa.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String pesq = etPesquisa.getText().toString();
                    if (pesq.length() > 0) {
                        etPesquisa.setText("");
                        esconderTeclado();
                        pesquisar(pesq);
                    }
                    return true;
                }
                return false;
            }
        });

        btPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pesq = etPesquisa.getText().toString();
                if (pesq.length() > 0) {
                    etPesquisa.setText("");
                    esconderTeclado();
                    pesquisar(pesq);
                }
            }
        });

//        checkBox.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (mQueryDocumentSnapshots != null) {
//                    modoSecreto = true;
//                    carregarListaMain(mQueryDocumentSnapshots);
//                }
//                return false;
//            }
//        });

    }

    private void pesquisar(final String pesq) {
        if(produtos.size() == 0) {
            return;
        }

        final ArrayList<ProdObj> searchProd = new ArrayList<>();

        isPesquisa = true;

        for (int i = 0; i < produtos.size(); i++) {

            final ProdObj obj = produtos.get(i);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                obj.getTag().forEach(new BiConsumer<String, Boolean>() {
                    @Override
                    public void accept(String s, Boolean aBoolean) {
                        if (s.equals(pesq) || pesq.contains(s) || s.contains(pesq)) {
                            if (!searchProd.contains(obj)) {
                                searchProd.add(obj);
                            }
                        }
                    }
                });

            }

            if (obj.getProdName().contains(pesq) || obj.getProdName().equals(pesq) || pesq.contains(obj.getProdName())) {
                if (!searchProd.contains(obj)) {
                    searchProd.add(obj);
                }
            }
        }

        if (searchProd.size() == 0) {
            return;
        }

        rv.setLayoutManager(new LinearLayoutManager(InventarioActivity.this));
        adapter = new AdapterProdutosInventario(searchProd, InventarioActivity.this, InventarioActivity.this, modoSecreto);
        rv.setAdapter(adapter);

    }

    private void carregarListaMain(QuerySnapshot queryDocumentSnapshots) {
        if (queryDocumentSnapshots != null) {
            if (queryDocumentSnapshots.size() > 0) {
                produtos = new ArrayList<>();
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    ProdObj ob = queryDocumentSnapshots.getDocuments().get(i).toObject(ProdObj.class);
                    produtos.add(ob);
                }

                rv.setLayoutManager(new LinearLayoutManager(InventarioActivity.this));
                adapter = new AdapterProdutosInventario(produtos, InventarioActivity.this, InventarioActivity.this, modoSecreto);
                rv.setAdapter(adapter);
            }
        }
    }

    private void esconderTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(etPesquisa.getWindowToken(), 0);
    }

    private void lista (boolean exibir) {
        if (exibir) {
            rv.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        } else {
            rv.setVisibility(View.GONE);
            pb.setVisibility(View.GONE);
        }
    }

    @Override
    public void abrirProduto(ProdObj obj) {
        ProdObjParcelable prodObjParcelable = new ProdObjParcelable(obj.getCategoria(), obj.getDescr(), obj.isDisponivel(), obj.getIdProduto(), obj.getImgCapa(), obj.getLaboratorio(), obj.getNivel(), obj.getProdName(),  obj.getProdValor(), obj.isPromocional(), obj.getTag(), obj.getFornecedores());
        Intent intent = new Intent(this, CadastroDeProdutoActivity.class);
        intent.putExtra("prod", prodObjParcelable);
        Bundle bundle = new Bundle();
        ArrayList<String> arrayList = mapParaArray(obj.getTag());
        bundle.putStringArrayList("t", arrayList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void criarProduto() {
        Intent intent = new Intent(this, CadastroDeProdutoActivity.class);
        startActivity(intent);
    }

    @Override
    public void disponibilidade(final ProdObj obj, boolean disponivel) {
        DocumentReference documentReference = firestore.collection("produtos").document(obj.getIdProduto());
        documentReference.update("disponivel", disponivel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (mToats != null) {
                    mToats.cancel();
                    mToats = null;
                }
                mToats = Toast.makeText(InventarioActivity.this, obj.getProdName() + " atualizado", Toast.LENGTH_LONG);
                mToats.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (mToats != null) {
                    mToats.cancel();
                }
                mToats = Toast.makeText(InventarioActivity.this, "Erro ao atualizar " + obj.getProdName(), Toast.LENGTH_LONG);
                mToats.show();
            }
        });
    }

    private ArrayList<String> mapParaArray(Map<String, Boolean> map) {

        final ArrayList<String> arrayList = new ArrayList<>();

        if(map == null){
            return arrayList;
        }

//        Object[] objects = map.values().toArray();
//
//        for (int i = 0; i < objects.length; i++) {
//            String key = objects[i].toString();
//            arrayList.add(key);
//        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            map.forEach(new BiConsumer<String, Boolean>() {
                @Override
                public void accept(String s, Boolean aBoolean) {
                    arrayList.add(s);
                }
            });
        }

        return arrayList;

    }

}
