package revolution.ph.developer.remediofacil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.firestore.CollectionReference;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);
        rv = (RecyclerView) findViewById(R.id.rv_inventario);
        pb = (ProgressBar) findViewById(R.id.pb_inventario);
        firestore = FirebaseFirestore.getInstance();
        refProdutos = firestore.collection("produtos");
        refProdutos.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                lista(true);
                if (queryDocumentSnapshots != null) {
                    if (queryDocumentSnapshots.size() > 0) {
                        ArrayList<ProdObj> produtos = new ArrayList<>();
                        for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                            ProdObj ob = queryDocumentSnapshots.getDocuments().get(i).toObject(ProdObj.class);
                            produtos.add(ob);
                        }

                        rv.setLayoutManager(new LinearLayoutManager(InventarioActivity.this));
                        adapter = new AdapterProdutosInventario(produtos, InventarioActivity.this, InventarioActivity.this);
                        rv.setAdapter(adapter);
                    }
                }
            }
        });
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
        ProdObjParcelable prodObjParcelable = new ProdObjParcelable(obj.getCategoria(), obj.getDescr(), obj.isDisponivel(), obj.getIdProduto(), obj.getImgCapa(), obj.getLaboratorio(), obj.getNivel(), obj.getProdName(),  obj.getProdValor(), obj.getPrecoDeCompra(), obj.isPromocional(), obj.getTag());
        Intent intent = new Intent(this, CadastroDeProdutoActivity.class);
        intent.putExtra("prod", prodObjParcelable);
        Bundle bundle = new Bundle();
        bundle.putInt("cat", obj.getCategoria());
        bundle.putString("n", obj.getProdName());
        bundle.putFloat("vc", obj.getPrecoDeCompra());
        bundle.putFloat("vv", obj.getProdValor());
        bundle.putBoolean("p", obj.isPromocional());
        bundle.putString("l", obj.getLaboratorio());
        bundle.putString("d", obj.getDescr());
        ArrayList<String> arrayList = mapParaArray(obj.getTag());
        bundle.putStringArrayList("t", arrayList);
        bundle.putString("f", obj.getImgCapa());
        bundle.putString("id", obj.getIdProduto());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void criarProduto() {
        Intent intent = new Intent(this, CadastroDeProdutoActivity.class);
        startActivity(intent);
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
