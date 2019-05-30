package revolution.ph.developer.remediofacilmanaus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import revolution.ph.developer.remediofacilmanaus.adapter.AdapterCentralCompras;
import revolution.ph.developer.remediofacilmanaus.objects.CarComprasActivyParcelable;
import revolution.ph.developer.remediofacilmanaus.objects.CompraFinalizada;
import revolution.ph.developer.remediofacilmanaus.objects.CompraFinalizadaParcelable;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CentralComprasActivity extends AppCompatActivity implements AdapterCentralCompras.ClickCentralCompra {

    private FirebaseFirestore firestore;
    private ProgressBar pb;
    private LinearLayout containerErro;
    private RecyclerView rv;
    private View bt_voltar_central_compras;

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
        setContentView(R.layout.activity_central_compras);
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
        rv = (RecyclerView) findViewById(R.id.rv_minhas_compras);
        pb = (ProgressBar) findViewById(R.id.pb_minhas_compras);
        containerErro = (LinearLayout) findViewById(R.id.ll_container_erro_minhas_compras);
        bt_voltar_central_compras = (View) findViewById(R.id.bt_voltar_central_compras);
        firestore = FirebaseFirestore.getInstance();

        bt_voltar_central_compras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        pb.setVisibility(View.VISIBLE);
        rv.setVisibility(View.GONE);
        firestore.collection("Compras").orderBy("hora", Query.Direction.DESCENDING).limit(30).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    if (queryDocumentSnapshots.getDocuments().size() < 1) {
                        pb.setVisibility(View.GONE);
                        containerErro.setVisibility(View.VISIBLE);
                    } else {
                        ArrayList<CompraFinalizada> compraFinalizadas = new ArrayList<>();
                        for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                            CompraFinalizada cf = queryDocumentSnapshots.getDocuments().get(i).toObject(CompraFinalizada.class);
                            compraFinalizadas.add(cf);
                        }

                        AdapterCentralCompras adapter = new AdapterCentralCompras(CentralComprasActivity.this, compraFinalizadas, CentralComprasActivity.this);
                        rv.setLayoutManager(new LinearLayoutManager(CentralComprasActivity.this));
                        rv.setAdapter(adapter);
                        pb.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                rv.setVisibility(View.GONE);
                pb.setVisibility(View.GONE);
                containerErro.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void detalhesCompra(CompraFinalizada compra) {
        ArrayList<CarComprasActivyParcelable> carComprasActivyParcelableArrayList = new ArrayList<>();
        for (int i = 0; i < compra.getListaDeProdutos().size(); i++) {
            CarComprasActivyParcelable p = new CarComprasActivyParcelable(compra.getListaDeProdutos().get(i));
            carComprasActivyParcelableArrayList.add(p);
        }

        CompraFinalizadaParcelable compraFinalizadaParcelable = new CompraFinalizadaParcelable(compra);
        Intent intent = new Intent(this, VendaActivity.class);
        intent.putParcelableArrayListExtra("itens", carComprasActivyParcelableArrayList);
        intent.putExtra("compra", compraFinalizadaParcelable);
        startActivity(intent);
    }
}
