package revolution.ph.developer.remediofacilmanaus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import revolution.ph.developer.remediofacilmanaus.R;
import revolution.ph.developer.remediofacilmanaus.objects.ProdutoMaisProdutoAnalyticsFusao;

public class AdapterProdutoAdmAnalytics extends RecyclerView.Adapter<AdapterProdutoAdmAnalytics.ProdAdmAnalyticsViewHolder> {

    private Context context;
    private ArrayList<ProdutoMaisProdutoAnalyticsFusao> produtos;

    public AdapterProdutoAdmAnalytics(Context context, ArrayList<ProdutoMaisProdutoAnalyticsFusao> produtos) {
        this.context = context;
        this.produtos = produtos;
    }

    @NonNull
    @Override
    public ProdAdmAnalyticsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_produto_analytics_adm, parent, false);
        return new ProdAdmAnalyticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdAdmAnalyticsViewHolder holder, int position) {
        ProdutoMaisProdutoAnalyticsFusao prod = produtos.get(position);
        Glide.with(context).load(prod.getProdObj().getImgCapa()).into(holder.img_prod_analystics_adm);
        holder.nome_prod_analystics_adm.setText(prod.getProdObj().getProdName());
        holder.valor_prod_analystics_adm.setText((int)prod.getProdObj().getProdValor()+",00");
        holder.numero_add_cart_analytics.setText(String.valueOf((int)prod.getProdutoAnalitycs().getNumeroAddCart()));
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    class ProdAdmAnalyticsViewHolder extends RecyclerView.ViewHolder {

        ImageView img_prod_analystics_adm;
        TextView nome_prod_analystics_adm, valor_prod_analystics_adm, numero_add_cart_analytics;

        public ProdAdmAnalyticsViewHolder(@NonNull View itemView) {
            super(itemView);
            img_prod_analystics_adm = (ImageView) itemView.findViewById(R.id.img_prod_analystics_adm);
            nome_prod_analystics_adm = (TextView) itemView.findViewById(R.id.nome_prod_analystics_adm);
            valor_prod_analystics_adm = (TextView) itemView.findViewById(R.id.valor_prod_analystics_adm);
            numero_add_cart_analytics = (TextView) itemView.findViewById(R.id.numero_add_cart_analytics);
        }
    }

}
