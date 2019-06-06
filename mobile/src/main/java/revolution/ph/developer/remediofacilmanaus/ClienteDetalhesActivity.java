package revolution.ph.developer.remediofacilmanaus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

import revolution.ph.developer.remediofacilmanaus.adapter.AdapterCarrinhoAddUserAnalyics;
import revolution.ph.developer.remediofacilmanaus.adapter.AdapterEnviarProduto;
import revolution.ph.developer.remediofacilmanaus.adapter.AdapterTermos;
import revolution.ph.developer.remediofacilmanaus.objects.ProdutoCartUserAnalyics;
import revolution.ph.developer.remediofacilmanaus.objects.TermosDePesquisa;
import revolution.ph.developer.remediofacilmanaus.objects.UsuarioParcelable;

import static revolution.ph.developer.remediofacilmanaus.FragmentMain.pathFotoUser;
import static revolution.ph.developer.remediofacilmanaus.FragmentMain.user;

public class ClienteDetalhesActivity extends AppCompatActivity implements AdapterEnviarProduto.EnviarProdutoListener {

    private View bt_voltar_cliente, bt_enviar_mensagem_cliente;
    private ImageView img_perfil_cliente;
    private TextView tv_toolbar_nome_cliente, tv_ultimo_login_cliente,
            tv_primeiro_login_cliente, tv_provedor_login_cliente, tv_email_cliente;
    private TextInputEditText et_mensagem_cliente;
    private LinearLayout ll_bt_mensagem_tela_cliente, ll_bt_cupom_tela_cliente,
            ll_bt_produtos_tela_cliente, container_carrinho_analyics_cliente,
            container_termos_pesquisa_cliente, container_enviar_produto_cliente;

    private EditText et_pesquisar_produto_enviar_cliente;
    private ImageButton bt_pesquisar_produto_enviar_cliente;

    private Switch switch_carrinho_analyics_cliente, switch_termos_pesquisa_cliente;

    private RecyclerView rv_produtos_cart_add_analyics, rv_termo_pesquisa_user, rv_enviar_produtos_cliente;

    private FirebaseFirestore firestore;

    private UsuarioParcelable usuarioParcelable;

    private AdapterCarrinhoAddUserAnalyics adapterCarrinhoAnalyics;
    private AdapterTermos adapterTermos;
    private AdapterEnviarProduto adapterEnviarProduto;

    private ArrayList<ProdutoCartUserAnalyics> prods;

    private ProgressBar pb;
    private ArrayList<ProdObj> produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_detalhes);
        bt_voltar_cliente = (View) findViewById(R.id.bt_voltar_cliente);
        bt_enviar_mensagem_cliente = (View) findViewById(R.id.bt_enviar_mensagem_cliente);

        img_perfil_cliente = (ImageView) findViewById(R.id.img_perfil_cliente);

        bt_pesquisar_produto_enviar_cliente = (ImageButton) findViewById(R.id.bt_pesquisar_produto_enviar_cliente);

        pb = (ProgressBar) findViewById(R.id.pb_cliente_detalhe);

        rv_produtos_cart_add_analyics = (RecyclerView) findViewById(R.id.rv_produtos_cart_add_analyics);
        rv_termo_pesquisa_user = (RecyclerView) findViewById(R.id.rv_termo_pesquisa_user);
        rv_enviar_produtos_cliente = (RecyclerView) findViewById(R.id.rv_enviar_produtos_cliente);

        switch_carrinho_analyics_cliente = (Switch) findViewById(R.id.switch_carrinho_analyics_cliente);
        switch_termos_pesquisa_cliente = (Switch) findViewById(R.id.switch_termos_pesquisa_cliente);

        tv_toolbar_nome_cliente = (TextView) findViewById(R.id.tv_toolbar_nome_cliente);
        tv_ultimo_login_cliente = (TextView) findViewById(R.id.tv_ultimo_login_cliente);
        tv_primeiro_login_cliente = (TextView) findViewById(R.id.tv_primeiro_login_cliente);
        tv_provedor_login_cliente = (TextView) findViewById(R.id.tv_provedor_login_cliente);
        tv_email_cliente = (TextView) findViewById(R.id.tv_email_cliente);

        et_mensagem_cliente = (TextInputEditText) findViewById(R.id.et_mensagem_cliente);

        et_pesquisar_produto_enviar_cliente = (EditText) findViewById(R.id.et_pesquisar_produto_enviar_cliente);

        ll_bt_mensagem_tela_cliente = (LinearLayout) findViewById(R.id.ll_bt_mensagem_tela_cliente);
        ll_bt_cupom_tela_cliente = (LinearLayout) findViewById(R.id.ll_bt_cupom_tela_cliente);
        ll_bt_produtos_tela_cliente = (LinearLayout) findViewById(R.id.ll_bt_produtos_tela_cliente);
        container_carrinho_analyics_cliente = (LinearLayout) findViewById(R.id.container_carrinho_analyics_cliente);
        container_termos_pesquisa_cliente = (LinearLayout) findViewById(R.id.container_termos_pesquisa_cliente);
        container_enviar_produto_cliente = (LinearLayout) findViewById(R.id.container_enviar_produto_cliente);

        firestore = FirebaseFirestore.getInstance();

        if (getIntent().getParcelableExtra("user") == null) {
            finish();
        }

        rv_produtos_cart_add_analyics.setLayoutManager(new LinearLayoutManager(ClienteDetalhesActivity.this, RecyclerView.HORIZONTAL, false));
        rv_termo_pesquisa_user.setLayoutManager(new LinearLayoutManager(ClienteDetalhesActivity.this));
        rv_enviar_produtos_cliente.setLayoutManager(new LinearLayoutManager(ClienteDetalhesActivity.this, RecyclerView.HORIZONTAL, false));

        usuarioParcelable = getIntent().getParcelableExtra("user");
        Glide.with(this).load(usuarioParcelable.getPathFoto()).into(img_perfil_cliente);
        tv_toolbar_nome_cliente.setText(usuarioParcelable.getNome());
        tv_ultimo_login_cliente.setText(DateFormatacao.dataCompletaCorrigidaSmall2(new Date(usuarioParcelable.getUltimoLogin()), new Date()));
        tv_primeiro_login_cliente.setText(DateFormatacao.dataCompletaCorrigidaSmall2(new Date(usuarioParcelable.getPrimeiroLogin()), new Date()));
        tv_provedor_login_cliente.setText(usuarioParcelable.getProvedor());
        tv_email_cliente.setText(usuarioParcelable.getEmail());

        listeners();

        firestore.collection("produtos").get().addOnSuccessListener(this, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots1) {
                if (queryDocumentSnapshots1 != null) {
                    if (queryDocumentSnapshots1.size() > 0) {
                        produtos = new ArrayList<>();
                        for (int i = 0; i < queryDocumentSnapshots1.size(); i++) {
                            ProdObj ob = queryDocumentSnapshots1.getDocuments().get(i).toObject(ProdObj.class);
                            produtos.add(ob);
                        }

                        if (produtos.size() > 0) {
                            container_enviar_produto_cliente.setVisibility(View.VISIBLE);
                            adapterEnviarProduto = new AdapterEnviarProduto(ClienteDetalhesActivity.this, produtos, ClienteDetalhesActivity.this);
                            rv_enviar_produtos_cliente.setAdapter(adapterEnviarProduto);
                        } else {
                            container_enviar_produto_cliente.setVisibility(View.GONE);
                        }
                    }
                }


                firestore.collection("CarrinhoAnalytics").document(usuarioParcelable.getUid()).collection("produtos").orderBy("ultimaVezAdicionadoAoCart", Query.Direction.DESCENDING).limit(100).addSnapshotListener(ClienteDetalhesActivity.this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {

                            prods = new ArrayList<>();
                            for(int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                                ProdutoCartUserAnalyics obj = queryDocumentSnapshots.getDocuments().get(i).toObject(ProdutoCartUserAnalyics.class);
                                prods.add(obj);
                            }

                            if (prods.size() > 0) {
                                container_carrinho_analyics_cliente.setVisibility(View.VISIBLE);
                                adapterCarrinhoAnalyics = new AdapterCarrinhoAddUserAnalyics(ClienteDetalhesActivity.this, prods);
                                rv_produtos_cart_add_analyics.setAdapter(adapterCarrinhoAnalyics);
                            } else {
                                container_carrinho_analyics_cliente.setVisibility(View.GONE);
                            }

                        }


                        firestore.collection("termosDePesquisaUser").document(usuarioParcelable.getUid()).collection("termos").orderBy("ultimaPesquisa", Query.Direction.DESCENDING).limit(150).addSnapshotListener(ClienteDetalhesActivity.this, new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if (queryDocumentSnapshots == null) {
                                    pb.setVisibility(View.GONE);
                                    return;
                                }

                                ArrayList<TermosDePesquisa> termos = new ArrayList<>();
                                for(int j = 0; j < queryDocumentSnapshots.getDocuments().size(); j++) {
                                    TermosDePesquisa o = queryDocumentSnapshots.getDocuments().get(j).toObject(TermosDePesquisa.class);
                                    termos.add(o);
                                }

                                if (termos.size() > 0) {
                                    container_termos_pesquisa_cliente.setVisibility(View.VISIBLE);
                                    adapterTermos = new AdapterTermos(ClienteDetalhesActivity.this, termos);
                                    rv_termo_pesquisa_user.setAdapter(adapterTermos);
                                    rv_termo_pesquisa_user.setNestedScrollingEnabled(false);
                                } else {
                                    container_termos_pesquisa_cliente.setVisibility(View.GONE);
                                }

                                pb.setVisibility(View.GONE);

                            }
                        });

                    }
                });


            }
        });


    }

    private void listeners() {
        bt_voltar_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        switch_carrinho_analyics_cliente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (prods == null || prods.size() < 1) {
                    return;
                }

                if (isChecked) {
                    adapterCarrinhoAnalyics = new AdapterCarrinhoAddUserAnalyics(ClienteDetalhesActivity.this, bubbleSortCartAnalyicsTopAdd(prods));
                    rv_produtos_cart_add_analyics.setAdapter(adapterCarrinhoAnalyics);
                } else {
                    adapterCarrinhoAnalyics = new AdapterCarrinhoAddUserAnalyics(ClienteDetalhesActivity.this, bubbleSortCartAnalyicsRecentes(prods));
                    rv_produtos_cart_add_analyics.setAdapter(adapterCarrinhoAnalyics);
                }
            }
        });

        bt_enviar_mensagem_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_mensagem_cliente.getText().toString().length() < 1) return;

                String str = et_mensagem_cliente.getText().toString();

                CollectionReference collection = firestore.collection("centralMensagens");
                CollectionReference collectionMensagens = firestore.collection("mensagens").document("ativas").collection(usuarioParcelable.getUid());
                WriteBatch batch = firestore.batch();
                MensagemObject mensagemObject = new MensagemObject(System.currentTimeMillis(), user.getUid(), 1, "", null, str);

                CentralMensagens centralMensagens = new CentralMensagens(new Date(), System.currentTimeMillis(), usuarioParcelable.getUid(), usuarioParcelable.getPathFoto(), mensagemObject.getMenssagemText(), 0, 0, usuarioParcelable.getNome());

                batch.set(collection.document(usuarioParcelable.getUid()), centralMensagens);
                batch.set(collectionMensagens.document(), mensagemObject);
                batch.commit();
                et_mensagem_cliente.clearFocus();
                et_mensagem_cliente.setText("");

            }
        });

        ll_bt_produtos_tela_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClienteDetalhesActivity.this, InventarioActivity.class);
                startActivity(intent);
            }
        });

        ll_bt_mensagem_tela_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClienteDetalhesActivity.this, MensagemActivity.class);
                startActivity(intent);
            }
        });

        bt_pesquisar_produto_enviar_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = et_pesquisar_produto_enviar_cliente.getText().toString();
                ArrayList<ProdObj> resultadoPesquisa = new ArrayList<>();

                for (int x = 0; x < produtos.size(); x++) {
                    Log.d("Teste123", String.valueOf(x));
                    String nomeProd = produtos.get(x).getProdName().toLowerCase();
                    boolean palavraIdentica = nomeProd.equals(s.toLowerCase());
                    boolean palavraParecida = nomeProd.contains(s.toLowerCase());
                    boolean palavraChaveExiste = produtos.get(x).getTag().containsKey(s.toLowerCase());
                    if (palavraChaveExiste || palavraIdentica || palavraParecida) {
                        resultadoPesquisa.add(produtos.get(x));
                        //Log.d("Teste123", String.valueOf(x) + " adicionada a liste");
                        //Log.d("Teste123", resultadoPesquisa.toString());
                    }
                }

                if (resultadoPesquisa.size() > 0) {
                    container_enviar_produto_cliente.setVisibility(View.VISIBLE);
                    adapterEnviarProduto = new AdapterEnviarProduto(ClienteDetalhesActivity.this, resultadoPesquisa, ClienteDetalhesActivity.this);
                    rv_enviar_produtos_cliente.setAdapter(adapterEnviarProduto);
                }
            }
        });

        bt_pesquisar_produto_enviar_cliente.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                container_enviar_produto_cliente.setVisibility(View.VISIBLE);
                adapterEnviarProduto = new AdapterEnviarProduto(ClienteDetalhesActivity.this, produtos, ClienteDetalhesActivity.this);
                rv_enviar_produtos_cliente.setAdapter(adapterEnviarProduto);
                return false;
            }
        });
    }

    private ArrayList<ProdutoCartUserAnalyics> bubbleSortCartAnalyicsRecentes(ArrayList<ProdutoCartUserAnalyics> array) {
        for (int i = 1; i < array.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (array.get(i).getUltimaVezAdicionadoAoCart() > array.get(j).getUltimaVezAdicionadoAoCart()) {
                    ProdutoCartUserAnalyics temp = array.get(i);
                    array.add(i, array.get(j));
                    array.add(j, temp);
                }
            }
        }

        return array;
    }

    private ArrayList<ProdutoCartUserAnalyics> bubbleSortCartAnalyicsTopAdd(ArrayList<ProdutoCartUserAnalyics> array) {
        for (int i = 1; i < array.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (array.get(i).getNumeroAddCart() > array.get(j).getNumeroAddCart()) {
                    ProdutoCartUserAnalyics temp = array.get(i);
                    array.add(i, array.get(j));
                    array.add(j, temp);
                }
            }
        }

        return array;
    }

    @Override
    public void enviarProduto(ProdObj obj) {
        CollectionReference collection = firestore.collection("centralMensagens");
        CollectionReference collectionMensagens = firestore.collection("mensagens").document("ativas").collection(usuarioParcelable.getUid());
        WriteBatch batch = firestore.batch();
        MensagemObject mensagemObject = new MensagemObject(System.currentTimeMillis(), user.getUid(), 2, "", obj, "");

        CentralMensagens centralMensagens = new CentralMensagens(new Date(), System.currentTimeMillis(), usuarioParcelable.getUid(), usuarioParcelable.getPathFoto(), mensagemObject.getMenssagemText(), 0, 0, usuarioParcelable.getNome());

        batch.set(collection.document(usuarioParcelable.getUid()), centralMensagens);
        batch.set(collectionMensagens.document(), mensagemObject);
        batch.commit();
        et_mensagem_cliente.clearFocus();
        et_mensagem_cliente.setText("");
        container_enviar_produto_cliente.setVisibility(View.VISIBLE);
        adapterEnviarProduto = new AdapterEnviarProduto(ClienteDetalhesActivity.this, produtos, ClienteDetalhesActivity.this);
        rv_enviar_produtos_cliente.setAdapter(adapterEnviarProduto);
    }
}
