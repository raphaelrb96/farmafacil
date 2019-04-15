package revolution.ph.developer.remediofacil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask.TaskSnapshot;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javax.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MensagemDetalheActivityFragment extends Fragment {


    private static final String DEMO_PHOTO_PATH = "GaleriaRemedioFacil";
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 46;
    private int REQUEST_IMAGE_CAPTURE = 571;
    private int REQUEST_IMAGE_GALERIA = 572;
    private FirebaseAuth auth;
    private CollectionReference collectionMensagens;
    private EditText editText;
    private LinearLayout escolherFoto;
    private FloatingActionButton fabEnviar;
    private FirebaseFirestore firebaseFirestore;
    private ImageView imageViewSend;
    private Uri mLocationForPhotos;
    private String menssagem;
    private ArrayList<MensagemObject> menssagens = new ArrayList();
    private boolean modoFoto = false;
    private ProgressBar pb;
    private RecyclerView recyclerView;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private LinearLayout tirarFoto;
    private TextView tvListaVazia;

    public MensagemDetalheActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutInflater = inflater.inflate(R.layout.fragment_mensagem_detalhe, container, false);
        this.tvListaVazia = (TextView) layoutInflater.findViewById(R.id.tv_lista_vazia_mensagem);
        this.recyclerView = (RecyclerView) layoutInflater.findViewById(R.id.rv_mensagens);
        this.tirarFoto = (LinearLayout) layoutInflater.findViewById(R.id.enviar_foto_mensagem);
        this.escolherFoto = (LinearLayout) layoutInflater.findViewById(R.id.escolher_foto_mensagem);
        this.fabEnviar = (FloatingActionButton) layoutInflater.findViewById(R.id.fab_enviar_mensagem);
        this.imageViewSend = (ImageView) layoutInflater.findViewById(R.id.imageView_send_mensagens);
        this.editText = (EditText) layoutInflater.findViewById(R.id.et_mensagem);
        this.pb = (ProgressBar) layoutInflater.findViewById(R.id.pb_mensagem);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true));
        this.auth = FirebaseAuth.getInstance();
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = this.storage.getReference();
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.collectionMensagens = this.firebaseFirestore.collection("mensagens").document("ativas").collection(getActivity().getIntent().getStringExtra("id"));
        this.collectionMensagens.orderBy("timeStamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                pb.setVisibility(View.GONE);
                int i = 0;
                if (querySnapshot.getDocuments().size() == 0) {
                    tvListaVazia.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    return;
                }
                tvListaVazia.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                menssagens = new ArrayList();
                while (i < querySnapshot.getDocuments().size()) {
                    menssagens.add((MensagemObject) ((DocumentSnapshot) querySnapshot.getDocuments().get(i)).toObject(MensagemObject.class));
                    i++;
                }
                Collections.reverse(menssagens);
                recyclerView.setAdapter(new MensagemDetalheAdapter(getActivity(), menssagens, auth.getCurrentUser().getUid()));
            }
        });
        this.escolherFoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                escolherFoto();
            }
        });
        this.tirarFoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.fabEnviar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modoFoto) {
                    imageViewSend.setVisibility(View.GONE);
                    modoFoto = false;
                    menssagem = editText.getText().toString();
                    editText.setText("");
                    salvarFotoEmStorage();
                } else if (editText.getText().length() > 0) {
                    menssagem = editText.getText().toString();
                    salvarDadosEmFirestore(null);
                }
            }
        });
        return layoutInflater;
    }

    private void escolherFoto() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, this.REQUEST_IMAGE_GALERIA);
        } else {
            Toast.makeText(getContext(), "Não é possivel escolher uma foto atualmente do seu aparelho", Toast.LENGTH_SHORT).show();
        }
    }

    private void salvarDadosEmFirestore(String str) {
        CollectionReference collection = this.firebaseFirestore.collection("centralMensagens");
        WriteBatch batch = this.firebaseFirestore.batch();
        Object centralMensagens = new CentralMensagens(new Date(), getActivity().getIntent().getStringExtra("id"));
        MensagemObject mensagemObject = new MensagemObject(System.currentTimeMillis(), this.auth.getCurrentUser().getUid(), 1, str, null, this.menssagem);
        batch.set(collection.document(this.auth.getCurrentUser().getUid()), centralMensagens);
        batch.set(this.collectionMensagens.document(), (Object) mensagemObject);
        batch.commit();
        this.menssagens.add(mensagemObject);
        this.editText.clearFocus();
        this.editText.setText("");
    }

    private void salvarFotoEmStorage() {
        long currentTimeMillis = System.currentTimeMillis();
        StorageReference child = this.storageReference.child("midia_action_chat");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(currentTimeMillis);
        stringBuilder.append(this.auth.getCurrentUser().getUid());
        stringBuilder.append(".jpeg");
        final StorageReference child2 = child.child(stringBuilder.toString());
        child2.putFile(this.mLocationForPhotos).continueWithTask(new Continuation<TaskSnapshot, Task<Uri>>() {
            public Task<Uri> then(@NonNull Task<TaskSnapshot> task) throws Exception {
                if (task.isSuccessful()) {
                    return child2.getDownloadUrl();
                }
                throw task.getException();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {
                if (task.isSuccessful()) {
                    salvarDadosEmFirestore(((Uri) task.getResult()).toString());
                    return;
                }
                Toast.makeText(getContext(), "Erro ao enviar Foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == this.REQUEST_IMAGE_CAPTURE && i2 == -1) {
            this.mLocationForPhotos = intent.getData();
            this.imageViewSend.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(this.mLocationForPhotos.toString()).into(this.imageViewSend);
            this.editText.setHint("Adicione uma descrição");
            this.modoFoto = true;
        } else if (i == this.REQUEST_IMAGE_GALERIA && i2 == -1) {
            this.mLocationForPhotos = intent.getData();
            this.imageViewSend.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(this.mLocationForPhotos.toString()).into(this.imageViewSend);
            this.editText.setHint("Adicione uma descrição");
            this.modoFoto = true;
        }
    }

    private File createImageFile() throws IOException {
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("JPEG_");
        stringBuilder.append(format);
        return File.createTempFile(stringBuilder.toString(), ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
    }

    @TargetApi(23)
    private void checkMultiplePermissions(int i, Context context) {
        String[] strArr = new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        if (!hasPermissions(context, strArr)) {
            ActivityCompat.requestPermissions((Activity) context, strArr, i);
        }
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i != 46) {
            super.onRequestPermissionsResult(i, strArr, iArr);
        } else if (iArr[0] == 0 && iArr[1] == 0 && iArr[2] == 0) {

        } else {
            shouldShowRequestPermissionRationale("android.permission.CAMERA");
            shouldShowRequestPermissionRationale("android.permission.READ_EXTERNAL_STORAGE");
            shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    private boolean hasPermissions(Context context, String... strArr) {
        if (!(VERSION.SDK_INT < 23 || context == null || strArr == null)) {
            for (String checkSelfPermission : strArr) {
                if (ActivityCompat.checkSelfPermission(context, checkSelfPermission) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

}
