package revolution.ph.developer.remediofacilmanaus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import revolution.ph.developer.remediofacilmanaus.adapter.AdapterMinhasCompras;
import revolution.ph.developer.remediofacilmanaus.objects.CompraFinalizada;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import static revolution.ph.developer.remediofacilmanaus.FragmentMain.user;

public class MinhasComprasActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private ProgressBar pb;
    private LinearLayout containerErro;
    private FrameLayout toolbar;
    private RecyclerView rv;
    private View btVoltar;
    private String uid;

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
        setContentView(R.layout.activity_minhas_compras);
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
        toolbar = (FrameLayout) findViewById(R.id.toolbar_minhas_compras);
        btVoltar = (View) findViewById(R.id.bt_voltar_minhas_compras);
        firestore = FirebaseFirestore.getInstance();

        uid = getIntent().getStringExtra("uid");

        if (uid == null) {
            if (user == null) {
                return;
            }
            uid = user.getUid();
        }


        firestore.collection("MinhasCompras").document("Usuario")
                .collection(uid).orderBy("hora", Query.Direction.DESCENDING)
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
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

                                AdapterMinhasCompras adapter = new AdapterMinhasCompras(MinhasComprasActivity.this, compraFinalizadas);
                                rv.setLayoutManager(new LinearLayoutManager(MinhasComprasActivity.this));
                                rv.setAdapter(adapter);
                                pb.setVisibility(View.GONE);
                                rv.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MinhasComprasActivity.this, MensagemDetalheActivity.class).putExtra("id", uid));
            }
        });
    }
}
