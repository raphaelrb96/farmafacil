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

public class AdapterTermos extends RecyclerView.Adapter<AdapterTermos.TermosVh> {

    private Context context;
    private ArrayList<TermosDePesquisa> termos;

    public AdapterTermos(Context context, ArrayList<TermosDePesquisa> termos) {
        this.context = context;
        this.termos = termos;
    }

    @NonNull
    @Override
    public TermosVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_termos_pesquisa, parent, false);
        return new TermosVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermosVh holder, int position) {
        TermosDePesquisa t = termos.get(position);
        holder.num.setText(String.valueOf(t.getQuantidade()));
        holder.termo.setText(t.getTermo());
        holder.time.setText(DateFormatacao.dataCompletaCorrigidaSmall2(new Date(t.getUltimaPesquisa()), new Date()));
    }

    @Override
    public int getItemCount() {
        return termos.size();
    }

    class TermosVh extends RecyclerView.ViewHolder {

        TextView termo, time, num;

        public TermosVh(@NonNull View itemView) {
            super(itemView);
            termo = (TextView) itemView.findViewById(R.id.termo);
            num = (TextView) itemView.findViewById(R.id.num_termo);
            time = (TextView) itemView.findViewById(R.id.hora_pesquisa);
        }
    }

}
