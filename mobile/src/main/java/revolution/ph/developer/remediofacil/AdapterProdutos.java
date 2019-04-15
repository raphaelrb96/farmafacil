package revolution.ph.developer.remediofacil;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static revolution.ph.developer.remediofacil.MainActivity.ids;

public class AdapterProdutos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ClickProdutoCliente clickProdutoCliente;
    private Context context;
    private ArrayList<ProdObj> produtos;

    public interface ClickProdutoCliente {
        void openDetalhe(ProdObj prodObj);
        void onclick(int i, ColorStateList colorStateList, View view, ProdObj prodObj);

        void openChat();
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
        vh.fab.setBackgroundTintList(ColorStateList.valueOf(this.context.getResources().getColor(R.color.fab1)));
        if (ids.contains(obj.getIdProduto())) {
            vh.fab.setBackgroundTintList(ColorStateList.valueOf(this.context.getResources().getColor(R.color.colorPrimaryDark)));
        }
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class ProdutoPrincipalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView nome, preco;
        private FloatingActionButton fab;

        private Context context;
        private ArrayList<ProdObj> produtos;

        public ProdutoPrincipalViewHolder(@NonNull View itemView, Context context, ArrayList<ProdObj> produtos) {
            super(itemView);
            this.context = context;
            this.produtos = produtos;
            imageView = (ImageView) itemView.findViewById(R.id.img_item_produto_principal);
            nome = (TextView) itemView.findViewById(R.id.nome_produto_principal);
            preco = (TextView) itemView.findViewById(R.id.preco_item_produto_principal);
            fab = (FloatingActionButton) itemView.findViewById(R.id.fab_produto_item);
            itemView.setOnClickListener(this);
            fab.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.fab_produto_item) {
                clickProdutoCliente.onclick(getAdapterPosition(), fab.getBackgroundTintList(), v, produtos.get(getAdapterPosition()));
            }
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
