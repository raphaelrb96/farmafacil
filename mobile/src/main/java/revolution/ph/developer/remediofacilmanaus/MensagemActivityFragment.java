package revolution.ph.developer.remediofacilmanaus;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * A placeholder fragment containing a simple view.
 */
public class MensagemActivityFragment extends Fragment implements CentralMensagemAdapter.CentralMensagemClickListener {

    private RecyclerView rv;
    private FirebaseFirestore firestore;
    private CentralMensagemAdapter adapter;
    private ArrayList<CentralMensagens> list;

    public MensagemActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mensagem, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv_central_mensagem);
        firestore = FirebaseFirestore.getInstance();
        final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (list == null) return;

                CentralMensagens centralMensagens = list.get(viewHolder.getAdapterPosition());

                DocumentReference collection = firestore.collection("centralMensagens").document(centralMensagens.getUid());

                collection.delete();
                list.remove(viewHolder.getAdapterPosition());

                adapter.notifyDataSetChanged();

            }
        };

        firestore.collection("centralMensagens").orderBy("time", Query.Direction.DESCENDING).addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots == null) return;
                if(!queryDocumentSnapshots.isEmpty()) {
                    list = new ArrayList<>();
                    for (int i = 0; i < queryDocumentSnapshots.getDocuments().size(); i++) {
                        list.add(queryDocumentSnapshots.getDocuments().get(i).toObject(CentralMensagens.class));
                    }
                    rv.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new CentralMensagemAdapter(list, MensagemActivityFragment.this, getActivity());
                    rv.setAdapter(adapter);
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                    itemTouchHelper.attachToRecyclerView(rv);
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
