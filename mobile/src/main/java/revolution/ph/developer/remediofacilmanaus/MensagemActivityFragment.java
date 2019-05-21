package revolution.ph.developer.remediofacilmanaus;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * A placeholder fragment containing a simple view.
 */
public class MensagemActivityFragment extends Fragment implements CentralMensagemAdapter.CentralMensagemClickListener {

    private RecyclerView rv;
    private FirebaseFirestore firestore;
    private CentralMensagemAdapter adapter;

    public MensagemActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mensagem, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv_central_mensagem);
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("centralMensagens").orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots == null) return;
                if(!queryDocumentSnapshots.isEmpty()) {
                    ArrayList<CentralMensagens> list = new ArrayList<>();
                    for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                        list.add(queryDocumentSnapshots.getDocuments().get(i).toObject(CentralMensagens.class));
                    }
                    rv.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new CentralMensagemAdapter(list, MensagemActivityFragment.this, getActivity());
                    rv.setAdapter(adapter);
                }
            }
        });
        return view;
    }

    @Override
    public void abrirMensagem(String uid) {
        startActivity(new Intent(getActivity(), MensagemDetalheActivity.class).putExtra("id", uid));
    }
}
