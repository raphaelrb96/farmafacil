package revolution.ph.developer.remediofacilmanaus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;

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
        Date date = null;

        if (cm.getFoto() != null) {
            Glide.with(context).load(cm.getFoto()).into(holder.foto);
        } else {
            holder.foto.setImageResource(R.drawable.ic_account_circle_black_24dp);
        }

        if (String.valueOf(cm.getTimeNovaMensagem()).length() > 0 && cm.getTimeNovaMensagem() != 0) {
            date = new Date(cm.getTimeNovaMensagem());
        } else {
            date = cm.getTime();
        }

        String horarioStr = DateFormatacao.dataCompletaCorrigidaSmall(date, new Date());
        holder.hora.setText(horarioStr);

        if (cm.getNomeUser() == null) {
            holder.nome.setText(cm.getUid());
        } else {
            holder.nome.setText(cm.getNomeUser());
        }

        if (cm.getDescricao() != null) {
            holder.mensagem.setText(cm.getDescricao());
        } else {
            holder.mensagem.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemMensagemCentral extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nome, hora, mensagem;
        ImageView foto;

        public ItemMensagemCentral(@NonNull View itemView) {
            super(itemView);
            nome = (TextView) itemView.findViewById(R.id.title_item_msg_list);
            hora = (TextView) itemView.findViewById(R.id.timestamp_item_msg_list);
            mensagem = (TextView) itemView.findViewById(R.id.tv_descricao_central_mensagem);
            foto = (ImageView) itemView.findViewById(R.id.img_perfil_central_mensagem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.abrirMensagem(list.get(getAdapterPosition()).getUid());
        }
    }

}
