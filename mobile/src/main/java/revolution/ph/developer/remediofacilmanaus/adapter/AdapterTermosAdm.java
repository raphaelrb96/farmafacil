package revolution.ph.developer.remediofacilmanaus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

import revolution.ph.developer.remediofacilmanaus.DateFormatacao;
import revolution.ph.developer.remediofacilmanaus.R;
import revolution.ph.developer.remediofacilmanaus.objects.TermosDePesquisa;

public class AdapterTermosAdm extends RecyclerView.Adapter<AdapterTermosAdm.TemosAdmViewHolder> {

    private Context context;
    private ArrayList<TermosDePesquisa> termos;

    public AdapterTermosAdm(Context context, ArrayList<TermosDePesquisa> termos) {
        this.context = context;
        this.termos = termos;
    }

    @NonNull
    @Override
    public TemosAdmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_termos_pesquisar_analytics_adm, parent, false);
        return new TemosAdmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TemosAdmViewHolder holder, int position) {
        TermosDePesquisa termo = termos.get(position);
        holder.quantidade_termos_pesquisados.setText(String.valueOf(termo.getQuantidade()));
        holder.tv_termos_pesquisados.setText(termo.getTermo());
        holder.hora_termos_pesquisados.setText(DateFormatacao.dataCompletaCorrigidaSmall2(new Date(termo.getUltimaPesquisa()), new Date()));
    }

    @Override
    public int getItemCount() {
        return termos.size();
    }

    class TemosAdmViewHolder extends RecyclerView.ViewHolder {

        TextView hora_termos_pesquisados, tv_termos_pesquisados, quantidade_termos_pesquisados;

        public TemosAdmViewHolder(@NonNull View itemView) {
            super(itemView);
            hora_termos_pesquisados = (TextView) itemView.findViewById(R.id.hora_termos_pesquisados);
            tv_termos_pesquisados = (TextView) itemView.findViewById(R.id.tv_termos_pesquisados);
            quantidade_termos_pesquisados = (TextView) itemView.findViewById(R.id.quantidade_termos_pesquisados);
        }
    }

}
