package farmafacil.developer;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterProdutos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ClickProdutoCliente clickProdutoCliente;
    private Context context;
    private ArrayList<ProdObj> produtos;

    public interface ClickProdutoCliente {
        void openDetalhe(ProdObj prodObj);
    }

    public AdapterProdutos(ClickProdutoCliente clickProdutoCliente, Context context, ArrayList<ProdObj> produtos) {
        this.clickProdutoCliente = clickProdutoCliente;
        this.context = context;
        this.produtos = produtos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_produto_principal, parent, false);
        return new ProdutoPrincipalViewHolder(view, context, produtos);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProdObj obj = produtos.get(position);
        ProdutoPrincipalViewHolder vh = (ProdutoPrincipalViewHolder) holder;
        vh.setImagem(obj.imgCapa);
        vh.setPreco(String.valueOf((int)obj.prodValor)+",00");
        vh.setNome(obj.prodName);
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class ProdutoPrincipalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView nome, preco;

        private Context context;
        private ArrayList<ProdObj> produtos;

        public ProdutoPrincipalViewHolder(@NonNull View itemView, Context context, ArrayList<ProdObj> produtos) {
            super(itemView);
            this.context = context;
            this.produtos = produtos;
            imageView = (ImageView) itemView.findViewById(R.id.img_item_produto_principal);
            nome = (TextView) itemView.findViewById(R.id.nome_produto_principal);
            preco = (TextView) itemView.findViewById(R.id.preco_item_produto_principal);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        public void setPreco(String p) {
            preco.setText(p);
        }

        public void setImagem(String img) {
            Glide.with(context).load(img).into(imageView);
        }

        public void setNome(String s) {
            nome.setText(s);
        }

    }

}
