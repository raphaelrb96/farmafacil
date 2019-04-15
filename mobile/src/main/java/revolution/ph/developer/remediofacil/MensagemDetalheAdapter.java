package revolution.ph.developer.remediofacil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MensagemDetalheAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<MensagemObject> mensagens;
    private String uidUser;

    public MensagemDetalheAdapter(Context context, ArrayList<MensagemObject> mensagens, String uidUser) {
        this.context = context;
        this.mensagens = mensagens;
        this.uidUser = uidUser;
    }

    public int getItemViewType(int i) {

        if (uidUser.equals(mensagens.get(i).getUidUser())) {
            return 1;
        } else {
            return 2;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new MensagensEnviadas(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensagem_enviada, parent, false));
        }
        return new MenssagensRecebidas(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensagem_recebida, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MensagemObject mensagemObject = (MensagemObject) this.mensagens.get(position);
        String date = new Date(mensagemObject.getTimeStamp()).toString();
        if (holder.getItemViewType() == 1) {
            MensagensEnviadas mensagensEnviadas = (MensagensEnviadas) holder;
            mensagensEnviadas.hora.setText(date);
            mensagensEnviadas.mensagem.setText(mensagemObject.getMenssagemText());
            if (mensagemObject.getPathFoto() != null) {
                mensagensEnviadas.imgCliente.setVisibility(View.VISIBLE);
                Glide.with(this.context).load(mensagemObject.getPathFoto()).into(mensagensEnviadas.imgCliente);
            } else {
                mensagensEnviadas.imgCliente.setVisibility(View.GONE);
            }
            if (mensagemObject.getProdObj() != null) {
                mensagensEnviadas.itemProd.setVisibility(View.VISIBLE);
                mensagensEnviadas.nomeProd.setText(mensagemObject.getProdObj().getProdName());
                mensagensEnviadas.valor.setText(String.valueOf(mensagemObject.getProdObj().getProdValor() + ",00"));
                Glide.with(this.context).load(mensagemObject.getProdObj().getImgCapa()).into(mensagensEnviadas.capaProd);
                return;
            }
            mensagensEnviadas.itemProd.setVisibility(View.GONE);
            return;
        }
        MenssagensRecebidas menssagensRecebidas = (MenssagensRecebidas) holder;
        menssagensRecebidas.hora.setText(date);
        menssagensRecebidas.mensagem.setText(mensagemObject.getMenssagemText());
        if (mensagemObject.getPathFoto() != null) {
            menssagensRecebidas.imgCliente.setVisibility(View.VISIBLE);
            Glide.with(this.context).load(mensagemObject.getPathFoto()).into(menssagensRecebidas.imgCliente);
        } else {
            menssagensRecebidas.imgCliente.setVisibility(View.GONE);
        }
        if (mensagemObject.getProdObj() != null) {
            menssagensRecebidas.itemProd.setVisibility(View.VISIBLE);
            menssagensRecebidas.nomeProd.setText(mensagemObject.getProdObj().getProdName());
            menssagensRecebidas.valor.setText(String.valueOf(mensagemObject.getProdObj().getProdValor() + "0,00"));
            Glide.with(this.context).load(mensagemObject.getProdObj().getImgCapa()).into(menssagensRecebidas.capaProd);
            return;
        }
        menssagensRecebidas.itemProd.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    class MensagensEnviadas extends RecyclerView.ViewHolder {
        private ImageView capaProd;
        private FloatingActionButton fab;
        private TextView hora;
        private ImageView imgCliente;
        private CardView itemProd;
        private TextView mensagem;
        private TextView nomeProd;
        private TextView valor;

        public MensagensEnviadas(View view) {
            super(view);
            this.mensagem = (TextView) view.findViewById(R.id.tv_menssagem_enviada);
            this.hora = (TextView) view.findViewById(R.id.tv_hora_mensagem_enviada);
            this.itemProd = (CardView) view.findViewById(R.id.item_produto_mensagem_enviada);
            this.fab = (FloatingActionButton) view.findViewById(R.id.fab_item_add_remv_mensagem_enviada);
            this.capaProd = (ImageView) view.findViewById(R.id.capa_produto_mensagem_enviada);
            this.imgCliente = (ImageView) view.findViewById(R.id.img_mensagens_enviada);
            this.valor = (TextView) view.findViewById(R.id.tv_item_valor_mensagem_enviada);
            this.nomeProd = (TextView) view.findViewById(R.id.tv_item_prodt_mensagem_enviada);
        }
    }

    class MenssagensRecebidas extends RecyclerView.ViewHolder {
        private ImageView capaProd;
        private FloatingActionButton fab;
        private TextView hora;
        private ImageView imgCliente;
        private CardView itemProd;
        private TextView mensagem;
        private TextView nomeProd;
        private TextView valor;

        public MenssagensRecebidas(View view) {
            super(view);
            this.mensagem = (TextView) view.findViewById(R.id.tv_menssagem_recebida);
            this.hora = (TextView) view.findViewById(R.id.tv_hora_mensagem_recebida);
            this.itemProd = (CardView) view.findViewById(R.id.item_produto_mensagem_recebida);
            this.fab = (FloatingActionButton) view.findViewById(R.id.fab_item_add_remv_mensagem_recebida);
            this.capaProd = (ImageView) view.findViewById(R.id.capa_produto_mensagem_recebida);
            this.imgCliente = (ImageView) view.findViewById(R.id.img_mensagens_recebida);
            this.valor = (TextView) view.findViewById(R.id.tv_item_valor_mensagem_recebida);
            this.nomeProd = (TextView) view.findViewById(R.id.tv_item_prodt_mensagem_recebida);
        }
    }

}
