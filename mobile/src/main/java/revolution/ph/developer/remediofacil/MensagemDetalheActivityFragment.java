package revolution.ph.developer.remediofacil;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Frame;
import com.otaliastudios.cameraview.PictureResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javax.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MensagemDetalheActivityFragment extends Fragment implements View.OnTouchListener {


    private static final String DEMO_PHOTO_PATH = "GaleriaRemedioFacil";
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 46;
    private int REQUEST_IMAGE_CAPTURE = 571;
    private int REQUEST_IMAGE_GALERIA = 572;
    private FirebaseAuth auth;
    private CollectionReference collectionMensagens;
    private EditText editText;
    //private LinearLayout escolherFoto;
    private FloatingActionButton fabEnviar;
    private FirebaseFirestore firebaseFirestore;
    private ImageView image_chat_camera;
    private Uri mLocationForPhotos;
    private String menssagem;
    private ArrayList<MensagemObject> menssagens = new ArrayList();
    private boolean modoFoto = false;
    private boolean fotoTirada = false;
    private ProgressBar pb;
    private RecyclerView recyclerView;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    //private LinearLayout tirarFoto;
    private TextView tvListaVazia, tv_bt_acao_chat_camera;
    private CoordinatorLayout coordinator_layout_chat;
    private LinearLayout content_layout_chat;
    private BottomSheetBehavior<LinearLayout> sheetBehavior;
    private CameraView camera;
    private boolean cameraAberta = false;
    private boolean mCameraAudioPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_CAMERA_AUDIO = 678;
    private FrameLayout backgroundcamera;
    private LinearLayout btAbrirCamera;
    private LinearLayout bt_escolher_foto_chat;
    private View icon_bt_acao_chat_camera;
    private byte[] fotoByte = null;
    public boolean tecladoInput = false;

    public MensagemDetalheActivityFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        camera.destroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        camera.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        camera.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutInflater = inflater.inflate(R.layout.fragment_mensagem_detalhe, container, false);
        this.tvListaVazia = (TextView) layoutInflater.findViewById(R.id.tv_lista_vazia_mensagem);
        this.recyclerView = (RecyclerView) layoutInflater.findViewById(R.id.rv_mensagens);
        btAbrirCamera= (LinearLayout) layoutInflater.findViewById(R.id.bt_tirar_foto_chat);
        bt_escolher_foto_chat = (LinearLayout) layoutInflater.findViewById(R.id.bt_escolher_foto_chat);
        bt_escolher_foto_chat.setOnTouchListener(this);
        coordinator_layout_chat = (CoordinatorLayout) layoutInflater.findViewById(R.id.coordinator_layout_chat);
        coordinator_layout_chat.setOnTouchListener(this);
        camera = layoutInflater.findViewById(R.id.camera);
        icon_bt_acao_chat_camera = (View) layoutInflater.findViewById(R.id.icon_bt_acao_chat_camera);
        tv_bt_acao_chat_camera = (TextView) layoutInflater.findViewById(R.id.tv_bt_acao_chat_camera);
        image_chat_camera = (ImageView) layoutInflater.findViewById(R.id.foto_tirada_chat);
        backgroundcamera= (FrameLayout) layoutInflater.findViewById(R.id.background_chat_camera);
        content_layout_chat = (LinearLayout) layoutInflater.findViewById(R.id.content_layout_chat);
        content_layout_chat.setOnTouchListener(this);
        //this.tirarFoto = (LinearLayout) layoutInflater.findViewById(R.id.enviar_foto_mensagem);
        //this.escolherFoto = (LinearLayout) layoutInflater.findViewById(R.id.escolher_foto_mensagem);
        this.fabEnviar = (FloatingActionButton) layoutInflater.findViewById(R.id.fab_enviar_mensagem);
        //this.imageViewSend = (ImageView) layoutInflater.findViewById(R.id.imageView_send_mensagens);
        this.editText = (EditText) layoutInflater.findViewById(R.id.et_mensagem);
        editText.setOnTouchListener(this);
        this.pb = (ProgressBar) layoutInflater.findViewById(R.id.pb_mensagem);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true));
        this.auth = FirebaseAuth.getInstance();
        this.storage = FirebaseStorage.getInstance();
        this.storageReference = this.storage.getReference();
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        sheetBehavior = BottomSheetBehavior.from(content_layout_chat);
        sheetBehavior.setHideable(false);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    //sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                if (newState == 4){
                    Log.d("BottomSheet", "Baixo");
                    if (mLocationForPhotos != null || fotoByte != null) {
                        editText.setHint("Escreva uma descrição");
                    }
                    exibirCamera();
                } else if (newState == 3){
                    Log.d("BottomSheet", "Alto");
                    if (mLocationForPhotos == null || fotoByte == null) {
                        editText.setHint("Escreva uma mensagem");
                    }
                    ocultarCamera();
                }


            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
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
                recyclerView.scrollToPosition(0);
            }
        });
//        this.escolherFoto.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                escolherFoto();
//            }
//        });
//        this.tirarFoto.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        this.fabEnviar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modoFoto) {
                    //image_chat_camera.setVisibility(View.GONE);
                    modoFoto = false;
                    menssagem = editText.getText().toString();
                    editText.setText("");
                    salvarFotoEmStorage();
                    Log.d("Click", "enviar foto escolhida");
                } else if (fotoTirada) {
                    //foto tirada
                    if (editText.getText().length() > 0) {
                        //foto com descricao
                        menssagem = editText.getText().toString();
                        editText.setText("");
                        Log.d("Click", "enviar foto tirada com descricao");
                    } else {
                        //foto sem descricao
                        Log.d("Click", "enviar foto tirada sem descricao");
                    }

                    if (fotoByte != null) {
                        Log.d("Click", "enviar foto tirada - fotoByte != null");
                        salvarFotoCapturadaEmStorage(fotoByte);
                    }

                } else if (cameraAberta) {
                    Log.d("Click", "tirar foto");
                    camera.takePicture();
                } else {
                    if (editText.getText().length() > 0) {
                        menssagem = editText.getText().toString();
                        editText.setText("");
                        Log.d("Click", "enviar mensagem");
                        salvarDadosEmFirestore(null);
                    }
                }
            }
        });

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Log.d("TesteInput", "Com foco");
                tecladoInput = true;
                fabEnviar.setImageResource(R.drawable.ic_send_black_30dp);
                return false;
            }
        });


        bt_escolher_foto_chat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fotoTirada) {
                    escolherFoto();
                } else {
                    ocultarFotoTirada();
                    fotoTirada = false;
                    modoFoto = false;
                    fotoByte = null;
                    mLocationForPhotos = null;
                    toggleBtAcaoCamera();
                }
            }
        });

        camera.addCameraListener(new CameraListener() {
            @Override
            public void onCameraOpened(@NonNull CameraOptions options) {
                super.onCameraOpened(options);
            }

            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                fotoTirada = true;
                fotoByte = result.getData();
                exibirFotoTirada(fotoByte);
            }
        });

        return layoutInflater;
    }

    private void toggleBtAcaoCamera() {
        if (fotoTirada) {
            icon_bt_acao_chat_camera.setBackgroundResource(R.drawable.ic_add_a_photo_black_40dp);
            tv_bt_acao_chat_camera.setText("Tirar outra foto");
        } else {
            icon_bt_acao_chat_camera.setBackgroundResource(R.drawable.ic_photo_library_black_40dp);
            tv_bt_acao_chat_camera.setText("Escolher foto");
        }
    }

    private void exibirCamera() {
        cameraAberta = true;
        if (fotoByte == null && mLocationForPhotos == null && !tecladoInput) {
            fabEnviar.setImageResource(R.drawable.ic_photo_camera_black_24dp);
        }
        btAbrirCamera.setVisibility(View.GONE);
        backgroundcamera.setVisibility(View.GONE);
        ocultarRv();
        exibirBtEscolherFoto();
    }

    private void exibirFotoTirada(byte[] uriFotoTirada) {
        Glide.with(getActivity()).load(uriFotoTirada).into(image_chat_camera);
        image_chat_camera.setVisibility(View.VISIBLE);
        camera.close();
        cameraAberta = false;
        fabEnviar.setImageResource(R.drawable.ic_send_black_30dp);
        toggleBtAcaoCamera();
    }

    private void exibirFotoEscolhida(Uri uri) {
        Glide.with(getActivity()).load(uri).into(image_chat_camera);
        image_chat_camera.setVisibility(View.VISIBLE);
        camera.close();
        cameraAberta = false;
        fabEnviar.setImageResource(R.drawable.ic_send_black_30dp);
        fotoTirada = true;
        toggleBtAcaoCamera();
    }

    private void ocultarFotoTirada() {
        image_chat_camera.setVisibility(View.GONE);
        camera.open();
        cameraAberta = true;
        fabEnviar.setImageResource(R.drawable.ic_photo_camera_black_24dp);
        toggleBtAcaoCamera();
    }

    private void ocultarCamera() {
        cameraAberta = false;
        fabEnviar.setImageResource(R.drawable.ic_send_black_30dp);
        btAbrirCamera.setVisibility(View.VISIBLE);
        backgroundcamera.setVisibility(View.VISIBLE);
        exibirRv();
        ocultarBtEscolherFoto();
    }

    private void ocultarRv() {
        recyclerView.setVisibility(View.GONE);
    }

    private void exibirRv() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void ocultarBtEscolherFoto() {
        bt_escolher_foto_chat.setVisibility(View.GONE);
    }

    private void exibirBtEscolherFoto() {
        bt_escolher_foto_chat.setVisibility(View.VISIBLE);
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
                    ocultarFotoTirada();
                    ocultarCamera();
                    mLocationForPhotos = null;
                    salvarDadosEmFirestore(((Uri) task.getResult()).toString());
                    return;
                }
                Toast.makeText(getContext(), "Erro ao enviar Foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void esconderTeclado() {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void salvarFotoCapturadaEmStorage(byte[] x) {
        long currentTimeMillis = System.currentTimeMillis();
        StorageReference child = this.storageReference.child("midia_action_chat");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(currentTimeMillis);
        stringBuilder.append(this.auth.getCurrentUser().getUid());
        stringBuilder.append(".jpeg");
        final StorageReference child2 = child.child(stringBuilder.toString());
        child2.putBytes(x).continueWithTask(new Continuation<TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(Task<TaskSnapshot> task) throws Exception {
                if (task.isSuccessful()) {
                    return child2.getDownloadUrl();
                }
                throw task.getException();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {
                if (task.isSuccessful()) {
                    ocultarFotoTirada();
                    ocultarCamera();
                    fotoTirada = false;
                    fotoByte = null;
                    salvarDadosEmFirestore(task.getResult().toString());
                    return;
                }
                Toast.makeText(getContext(), "Erro ao enviar Foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == this.REQUEST_IMAGE_CAPTURE && i2 == -1) {
            this.mLocationForPhotos = intent.getData();
            exibirFotoEscolhida(intent.getData());
            this.editText.setHint("Adicione uma descrição");
            this.modoFoto = true;
        } else if (i == this.REQUEST_IMAGE_GALERIA && i2 == -1) {
            this.mLocationForPhotos = intent.getData();
            exibirFotoEscolhida(intent.getData());
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
//        if (i != 46) {
//            super.onRequestPermissionsResult(i, strArr, iArr);
//        } else if (iArr[0] == 0 && iArr[1] == 0 && iArr[2] == 0) {
//
//        } else {
//            shouldShowRequestPermissionRationale("android.permission.CAMERA");
//            shouldShowRequestPermissionRationale("android.permission.READ_EXTERNAL_STORAGE");
//            shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE");
//        }
        mCameraAudioPermissionGranted = false;
        switch (i) {
            case PERMISSIONS_REQUEST_ACCESS_CAMERA_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (iArr.length > 0 && iArr[0] == PackageManager.PERMISSION_GRANTED) {
                    mCameraAudioPermissionGranted = true;
                }
            }
        }
        updateCameraUI();
    }

    private void updateCameraUI() {
        if (mCameraAudioPermissionGranted) {
            ligarCamera();
        } else {
            getPermissions(getActivity());
        }
    }

    private void ligarCamera() {
        camera.open();
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

    private void getPermissions(Activity context) {
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            mCameraAudioPermissionGranted = true;
            updateCameraUI();
        } else {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    PERMISSIONS_REQUEST_ACCESS_CAMERA_AUDIO);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() != R.id.et_mensagem) {
            tecladoInput = false;
            fabEnviar.setImageResource(R.drawable.ic_photo_camera_black_24dp);
            esconderTeclado();
            Log.d("TesteInput", "Sem foco");
            return false;
        }
        return false;
    }
}
