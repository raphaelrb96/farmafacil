package revolution.ph.developer.remediofacil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class AdmActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore firestore;
    private DocumentReference statusRef;
    private TextView status;
    private ExtendedFloatingActionButton abrir_fechar;
    private boolean aberto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm);
        status = (TextView) findViewById(R.id.tv_drogaria_aberta_fechada);
        abrir_fechar = (ExtendedFloatingActionButton) findViewById(R.id.efab_abrir_fechar_drogaria);
        firestore = FirebaseFirestore.getInstance();
        statusRef = firestore.collection("statusDrogaria").document("chave");
        statusRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    if (documentSnapshot.exists()) {
                        aberto = documentSnapshot.toObject(StatusDrogariaAbrirFechar.class).isAberto();
                    }
                }

                if (aberto) {
                    status.setText("Drogaria está aberta");
                } else {
                    status.setText("Drogaria está fechada");
                }

                abrir_fechar.setOnClickListener(AdmActivity.this);

            }
        });
    }

    public void verProdutos(View view) {
        Intent intent = new Intent(this, InventarioActivity.class);
        startActivity(intent);
    }

    public void verMensagens(View view) {
        Intent intent = new Intent(this, MensagemActivity.class);
        startActivity(intent);
    }

    public void verVendas(View view) {

    }

    public void verAnalytics(View view) {

    }

    private void abrirFechar() {
        status.setText("Aguarde...");
        if (aberto) {
            StatusDrogariaAbrirFechar fecharr = new StatusDrogariaAbrirFechar(false);
            statusRef.set(fecharr);
        } else {
            StatusDrogariaAbrirFechar abrirr = new StatusDrogariaAbrirFechar(true);
            statusRef.set(abrirr);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.efab_abrir_fechar_drogaria) {
            abrirFechar();
        }
    }

    public void verCupons(View view) {
    }
}
