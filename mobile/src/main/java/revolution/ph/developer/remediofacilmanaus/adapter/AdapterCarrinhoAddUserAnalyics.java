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
import java.util.Date;

import revolution.ph.developer.remediofacilmanaus.R;
import revolution.ph.developer.remediofacilmanaus.objects.ProdutoCartUserAnalyics;
import revolution.ph.developer.remediofacilmanaus.DateFormatacao;

public class AdapterCarrinhoAddUserAnalyics extends RecyclerView.Adapter<AdapterCarrinhoAddUserAnalyics.CarrinhoUserAnalyics> {

    private Context context;
    private ArrayList<ProdutoCartUserAnalyics> produtos;

    public AdapterCarrinhoAddUserAnalyics(Context context, ArrayList<ProdutoCartUserAnalyics> produtos) {
        this.context = context;
        this.produtos = produtos;
    }

    @NonNull
    @Override
    public CarrinhoUserAnalyics onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_produto_carrinho_cliente_analytics, parent, false);
        return new CarrinhoUserAnalyics(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarrinhoUserAnalyics holder, int position) {
        ProdutoCartUserAnalyics obj = produtos.get(position);
        holder.num.setText(String.valueOf(obj.getNumeroAddCart()));
        holder.nomeProd.setText(obj.getNome());
        holder.time.setText(DateFormatacao.dataCompletaCorrigidaSmall2(new Date(obj.getUltimaVezAdicionadoAoCart()), new Date()));
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    class CarrinhoUserAnalyics extends RecyclerView.ViewHolder {

        TextView nomeProd, num, time;

        public CarrinhoUserAnalyics(@NonNull View itemView) {
            super(itemView);
            nomeProd = (TextView) itemView.findViewById(R.id.nome_produto_add_cart);
            num = (TextView) itemView.findViewById(R.id.numero_produto_add_cart);
            time = (TextView) itemView.findViewById(R.id.time_produto_add_cart);
        }
    }

}
