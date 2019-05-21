package revolution.ph.developer.remediofacilmanaus.adapter;

import android.content.Context;
import android.graphics.Color;
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
import revolution.ph.developer.remediofacilmanaus.DateFormatacao;
import revolution.ph.developer.remediofacilmanaus.R;
import revolution.ph.developer.remediofacilmanaus.objects.CompraFinalizada;

public class AdapterCentralCompras extends RecyclerView.Adapter<AdapterCentralCompras.CentralCompraViewHolder> {

    private Context context;
    private ArrayList<CompraFinalizada> compraFinalizadas;
    private ClickCentralCompra listene;

    public AdapterCentralCompras(Context context, ArrayList<CompraFinalizada> compraFinalizadas, ClickCentralCompra listene) {
        this.context = context;
        this.compraFinalizadas = compraFinalizadas;
        this.listene = listene;
    }

    public interface ClickCentralCompra {
        void detalhesCompra(String idCompra);
    }

    @NonNull
    @Override
    public CentralCompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_central_compra, parent, false);
        return new CentralCompraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CentralCompraViewHolder holder, int position) {
        CompraFinalizada compra = compraFinalizadas.get(position);
        Glide.with(context).load(compra.getPathFotoUser()).into(holder.img);
        String adress = compra.getAdress();
        if (compra.getComplemento().length() > 1) {
            adress = adress + " (" + compra.getComplemento() + ")";
        }
        holder.nome.setText(compra.getUserNome());
        holder.telefone.setText(compra.getPhoneUser());
        holder.endereco.setText(adress);
        holder.hora_minha_compra.setText(DateFormatacao.dataCompletaCorrigidaSmall2(new Date(compra.getHora()), new Date()));
        holder.soma.setText(String.valueOf((int) compra.getCompraValor() + ",00"));
        holder.taxa.setText(String.valueOf((int) compra.getFrete() + ",00"));
        holder.total.setText(String.valueOf((int) compra.getValorTotal() + ",00"));
        int status = compra.getStatusCompra();
        switch (status) {
            case 1:
                holder.status_minha_compra.setText("Aguarde a confirmação do pedido");
                break;
            case 2:
                holder.status_minha_compra.setText("Confirmada, você receberá em breve");
                holder.status_minha_compra.setTextColor(Color.BLUE);
                break;
            case 3:
                holder.status_minha_compra.setText("Sua compra foi canceleda");
                holder.status_minha_compra.setTextColor(Color.RED);
                break;
            case 4:
                holder.status_minha_compra.setText("Sua compra está a caminho");
                holder.status_minha_compra.setTextColor(Color.YELLOW);
                break;
            case 5:
                holder.status_minha_compra.setText("Concluida");
                holder.status_minha_compra.setTextColor(Color.GREEN);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return compraFinalizadas.size();
    }

    class CentralCompraViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView total, taxa, soma, hora_minha_compra, status_minha_compra, nome, endereco, telefone;
        ImageView img;

        public CentralCompraViewHolder(@NonNull View itemView) {
            super(itemView);
            hora_minha_compra = (TextView) itemView.findViewById(R.id.hora_central_compra);
            taxa = (TextView) itemView.findViewById(R.id.taxa_central_compra);
            total = (TextView) itemView.findViewById(R.id.total_central_compras);
            soma = (TextView) itemView.findViewById(R.id.soma_central_compra);
            status_minha_compra = (TextView) itemView.findViewById(R.id.status_central_compra);
            nome = (TextView) itemView.findViewById(R.id.nome_user_central_compras);
            endereco = (TextView) itemView.findViewById(R.id.endereco_user_central_compras);
            telefone = (TextView) itemView.findViewById(R.id.numero_user_central_compras);
            img = (ImageView) itemView.findViewById(R.id.img_user_central_compras);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            CompraFinalizada cf = compraFinalizadas.get(getAdapterPosition());
            listene.detalhesCompra(cf.getIdCompra());
        }
    }

}
