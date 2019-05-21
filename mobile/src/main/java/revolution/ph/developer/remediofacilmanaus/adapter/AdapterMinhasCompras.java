package revolution.ph.developer.remediofacilmanaus.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import revolution.ph.developer.remediofacilmanaus.DateFormatacao;
import revolution.ph.developer.remediofacilmanaus.R;
import revolution.ph.developer.remediofacilmanaus.objects.CompraFinalizada;

public class AdapterMinhasCompras extends RecyclerView.Adapter<AdapterMinhasCompras.CompraViewHolder> {

    private Context context;
    private ArrayList<CompraFinalizada> compraFinalizadas;

    public AdapterMinhasCompras(Context context, ArrayList<CompraFinalizada> compraFinalizadas) {
        this.context = context;
        this.compraFinalizadas = compraFinalizadas;
    }

    @NonNull
    @Override
    public CompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_minhas_compras, parent, false);
        return new CompraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompraViewHolder holder, int position) {
        CompraFinalizada compra = compraFinalizadas.get(position);
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

    class CompraViewHolder extends RecyclerView.ViewHolder {

        TextView total, taxa, soma, hora_minha_compra, status_minha_compra;

        public CompraViewHolder(@NonNull View itemView) {
            super(itemView);
            hora_minha_compra = (TextView) itemView.findViewById(R.id.hora_minha_compra);
            taxa = (TextView) itemView.findViewById(R.id.taxa_minha_compra);
            total = (TextView) itemView.findViewById(R.id.total_minhas_compras);
            soma = (TextView) itemView.findViewById(R.id.soma_minha_compra);
            status_minha_compra = (TextView) itemView.findViewById(R.id.status_minha_compra);
        }
    }

}
