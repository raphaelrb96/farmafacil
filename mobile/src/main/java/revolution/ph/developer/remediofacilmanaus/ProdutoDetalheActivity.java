package revolution.ph.developer.remediofacilmanaus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import revolution.ph.developer.remediofacilmanaus.analitycs.AnalitycsFacebook;
import revolution.ph.developer.remediofacilmanaus.analitycs.AnalitycsGoogle;

import static revolution.ph.developer.remediofacilmanaus.FragmentMain.user;
import static revolution.ph.developer.remediofacilmanaus.MainActivity.ids;

public class ProdutoDetalheActivity extends AppCompatActivity {

    private TextView nomeProd, valor;
    private ImageView imagem;
    private FirebaseFirestore firebaseFirestore;
    private ProdObjParcelable prodObjParcelable;
    private Toast mToast;
    private ExtendedFloatingActionButton efab;
    private ProgressBar pb;

    private AnalitycsFacebook analitycsFacebook;
    private AnalitycsGoogle analitycsGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_detalhe);
        nomeProd = (TextView) findViewById(R.id.tv_detalhe_prod_nome);
        valor = (TextView) findViewById(R.id.tv_detalhe_prod_valor);
        imagem = (ImageView) findViewById(R.id.img_pod_detalhe);
        pb = (ProgressBar) findViewById(R.id.pb_prod_detalhe);
        efab = (ExtendedFloatingActionButton) findViewById(R.id.efab_prod_detalhe);
        analitycsFacebook = new AnalitycsFacebook(this);
        analitycsGoogle = new AnalitycsGoogle(this);
        prodObjParcelable = getIntent().getParcelableExtra("prod");
        if (prodObjParcelable == null) {
            finish();
            return;
        }

        Glide.with(this).load(prodObjParcelable.getImgCapa()).into(imagem);
        valor.setText(String.valueOf((int) prodObjParcelable.getProdValor()) + ",00");
        nomeProd.setText(prodObjParcelable.getProdName());

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void voltarDetalhe(View view) {
        onBackPressed();
    }

    public void abrirCarrinho() {
        Intent intent = new Intent(ProdutoDetalheActivity.this, CarrinhoActivity.class);
        startActivity(intent);
    }

    public void comprarProdDetalhe(View view) {
        if (user.isAnonymous() || user == null) {
            mToast = Toast.makeText(this, "Faça Login para poder efutuar sua compra", Toast.LENGTH_LONG);
            if (mToast != null) {
                mToast.cancel();
            }
            mToast.show();
        } else {
            final ProdObj prodObj = prodObjParcelable.getProd();
            final String str = prodObj.getIdProduto();
            CarComprasActivy carComprasActivy = new CarComprasActivy(str, prodObj.getProdName(), prodObj.getLaboratorio(), prodObj.getImgCapa(), prodObj.getProdValor(), 1, ((float) 1) * prodObj.getProdValor());
            DocumentReference reference = firebaseFirestore.collection("carComprasActivy").document("usuario").collection(user.getUid()).document(str);
            if (!ids.contains(str)) {

                pb.setVisibility(View.VISIBLE);
                efab.setVisibility(View.GONE);
                reference.set(carComprasActivy).addOnSuccessListener(ProdutoDetalheActivity.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ids.add(str);
                        abrirCarrinho();
                        finish();
                    }
                }).addOnFailureListener(ProdutoDetalheActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mToast = Toast.makeText(ProdutoDetalheActivity.this, "Erro ao Adicionar " + prodObj.getProdName() + " ao carrinho ", Toast.LENGTH_LONG);
                        if (mToast != null) {
                            mToast.cancel();
                        }
                        mToast.show();
                    }
                });

            } else {
                abrirCarrinho();
                finish();
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        analitycsGoogle.logProdutoVizualizadoEvent(prodObjParcelable.getProdName(), prodObjParcelable.getIdProduto(), prodObjParcelable.getProdValor());
        analitycsFacebook.logProdutoVizualizadoEvent(prodObjParcelable.getProdName(), prodObjParcelable.getIdProduto(), prodObjParcelable.getProdValor());
    }
}
