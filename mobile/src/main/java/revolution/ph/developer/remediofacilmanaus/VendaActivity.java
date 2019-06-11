package revolution.ph.developer.remediofacilmanaus;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import revolution.ph.developer.remediofacilmanaus.objects.CarComprasActivyParcelable;
import revolution.ph.developer.remediofacilmanaus.objects.CompraFinalizadaParcelable;

import static revolution.ph.developer.remediofacilmanaus.FragmentMain.user;

public class VendaActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private View bt_voltar_venda;
    private SupportMapFragment mapFragment;
    private ImageView img_perfil_venda;
    private TextView tv_nome_usuario_venda, hora_venda, tv_status_venda;
    private View bt_ligar_venda;
    private LinearLayout ll_bt_confirmar_venda, ll_bt_entregar_venda, ll_bt_cancelar_venda, ll_bt_concluir_venda;
    private RecyclerView rv_vendas;
    private TextView taxa_entrega_venda, total_compras_venda, total_venda;
    private TextView tv_pagamento_venda, tv_tipo_entrega_venda, tv_nome_rua_venda;
    private ProgressBar pb;

    private ArrayList<CarComprasActivyParcelable> listaProdutos;
    private CompraFinalizadaParcelable dadosCompra;

    private AdapterVenda adapter;
    private LatLng mDefaultLocation;

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
        setContentView(R.layout.activity_venda);

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

        findView();

        listaProdutos = getIntent().getParcelableArrayListExtra("itens");
        dadosCompra = getIntent().getParcelableExtra("compra");

        preencherInterface();

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        listeners();
    }

    private void listeners() {
        bt_voltar_venda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); 
            }
        });
        bt_ligar_venda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + dadosCompra.getPhoneUser().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });
        ll_bt_cancelar_venda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateState(3);
            }
        });
        ll_bt_confirmar_venda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateState(2);
            }
        });
        ll_bt_entregar_venda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateState(4);
            }
        });
        ll_bt_concluir_venda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateState(5);
            }
        });
    }

    private void updateState(final int i) {
        pb.setVisibility(View.VISIBLE);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        WriteBatch batch = firestore.batch();

        DocumentReference compras = firestore.collection("Compras").document(dadosCompra.getIdCompra());
        DocumentReference minhasCompras = firestore.collection("MinhasCompras").document("Usuario").collection(dadosCompra.getUidUserCompra()).document(dadosCompra.getIdCompra());

        batch.update(compras, "statusCompra", i);
        batch.update(minhasCompras, "statusCompra", i);

        batch.commit().addOnSuccessListener(VendaActivity.this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pb.setVisibility(View.GONE);
                switch (i) {
                    case 1:
                        tv_status_venda.setText("Aguardando a confirmação do pedido");
                        break;
                    case 2:
                        tv_status_venda.setText("Confirmada");
                        break;
                    case 3:
                        tv_status_venda.setText("Cancelada");
                        break;
                    case 4:
                        tv_status_venda.setText("Saiu para entrega");
                        break;
                    case 5:
                        tv_status_venda.setText("Concluida");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void preencherInterface() {
        mDefaultLocation = new LatLng(dadosCompra.getLat(), dadosCompra.getLng());
        Glide.with(this).load(dadosCompra.getPathFotoUser()).into(img_perfil_venda);
        tv_nome_usuario_venda.setText(dadosCompra.getUserNome());
        hora_venda.setText(DateFormatacao.dataCompletaCorrigidaSmall2(new Date(dadosCompra.getHora()), new Date()));
        int status = dadosCompra.getStatusCompra();
        switch (status) {
            case 1:
                tv_status_venda.setText("Aguardando a confirmação do pedido");
                break;
            case 2:
                tv_status_venda.setText("Confirmada");
                break;
            case 3:
                tv_status_venda.setText("Cancelada");
                break;
            case 4:
                tv_status_venda.setText("Saiu para entrega");
                break;
            case 5:
                tv_status_venda.setText("Concluida");
                break;
            default:
                break;
        }
        rv_vendas.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        adapter = new AdapterVenda(this, listaProdutos);
        rv_vendas.setAdapter(adapter);
        taxa_entrega_venda.setText((int) dadosCompra.getFrete() + ",00");
        total_compras_venda.setText((int) dadosCompra.getCompraValor() + ",00");
        total_venda.setText((int) dadosCompra.getValorTotal() + ",00");
        int tipoPadamento = dadosCompra.getFormaDePagar();
        String formaDePagamento = "";
        switch (tipoPadamento) {
            case 1:
                //debito
                formaDePagamento = "Debito";
                break;
            case 2:
                formaDePagamento = "Crédito";
                //credito
                break;
            case 3:
                formaDePagamento = "Alimentação";
                //alimentacao
                break;
            default:
                formaDePagamento = "Dinheiro";
                //dinheiro
                break;
        }

        formaDePagamento = formaDePagamento + ", " + dadosCompra.getDetalhePag();
        tv_pagamento_venda.setText(formaDePagamento);
        if (dadosCompra.getTipoDeEntrega() == 1) {
            //facil
            tv_tipo_entrega_venda.setText("Fácil");
        } else if (dadosCompra.getTipoDeEntrega() == 2) {
            tv_tipo_entrega_venda.setText("Rápida");
        } else {
            tv_tipo_entrega_venda.setText("Grátis");
        }

        tv_nome_rua_venda.setText(dadosCompra.getAdress() + " - " + dadosCompra.getComplemento());
    }

    private void findView() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        bt_voltar_venda = (View) findViewById(R.id.bt_voltar_venda);
        bt_ligar_venda = (View) findViewById(R.id.bt_ligar_venda);
        img_perfil_venda = (ImageView) findViewById(R.id.img_perfil_venda);
        tv_nome_usuario_venda = (TextView) findViewById(R.id.tv_nome_usuario_venda);
        hora_venda = (TextView) findViewById(R.id.hora_venda);
        tv_status_venda = (TextView) findViewById(R.id.tv_status_venda);
        taxa_entrega_venda = (TextView) findViewById(R.id.taxa_entrega_venda);
        total_compras_venda = (TextView) findViewById(R.id.total_compras_venda);
        total_venda = (TextView) findViewById(R.id.total_venda);
        tv_pagamento_venda = (TextView) findViewById(R.id.tv_pagamento_venda);
        tv_tipo_entrega_venda = (TextView) findViewById(R.id.tv_tipo_entrega_venda);
        tv_nome_rua_venda = (TextView) findViewById(R.id.tv_nome_rua_venda);
        ll_bt_confirmar_venda = (LinearLayout) findViewById(R.id.ll_bt_confirmar_venda);
        ll_bt_entregar_venda = (LinearLayout) findViewById(R.id.ll_bt_entregar_venda);
        ll_bt_cancelar_venda = (LinearLayout) findViewById(R.id.ll_bt_cancelar_venda);
        ll_bt_concluir_venda = (LinearLayout) findViewById(R.id.ll_bt_concluir_venda);
        rv_vendas = (RecyclerView) findViewById(R.id.rv_vendas);
        pb = (ProgressBar) findViewById(R.id.pb_venda);
    }

    private void marcar() {

        if (mMap == null) {
            return;
        }

        mMap.clear();
        float factor = getResources().getDisplayMetrics().density;
        int h = (int) (80 * factor);
        int top = (int) (30 * factor);
        mMap.setPadding(0, top, 0, h);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 18));
        mMap.addMarker(new MarkerOptions()
                .position(mDefaultLocation)
                .draggable(true)
        );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        pb.setVisibility(View.GONE);

        marcar();
    }

    public void abrirMapa(View view) {
        if (mDefaultLocation == null) return;
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", mDefaultLocation.latitude, mDefaultLocation.longitude, "Destino");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
}
