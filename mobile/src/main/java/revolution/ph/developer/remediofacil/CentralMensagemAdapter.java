package revolution.ph.developer.remediofacil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CentralMensagemAdapter extends RecyclerView.Adapter<CentralMensagemAdapter.ItemMensagemCentral> {

    private ArrayList<CentralMensagens> list;
    private CentralMensagemClickListener listener;
    private Context context;

    public CentralMensagemAdapter(ArrayList<CentralMensagens> list, CentralMensagemClickListener listener, Context context) {
        this.list = list;
        this.listener = listener;
        this.context = context;
    }

    public interface CentralMensagemClickListener {
        void abrirMensagem(String uid);
    }

    @NonNull
    @Override
    public ItemMensagemCentral onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_central_mensagem, parent,false);
        return new ItemMensagemCentral(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemMensagemCentral holder, int position) {
        CentralMensagens cm = list.get(position);
        holder.hora.setText(cm.getTime().toString());
        holder.uid.setText(cm.getUid());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemMensagemCentral extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView uid, hora;

        public ItemMensagemCentral(@NonNull View itemView) {
            super(itemView);
            uid = (TextView) itemView.findViewById(R.id.title_item_msg_list);
            hora = (TextView) itemView.findViewById(R.id.timestamp_item_msg_list);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.abrirMensagem(list.get(getAdapterPosition()).getUid());
        }
    }

}
