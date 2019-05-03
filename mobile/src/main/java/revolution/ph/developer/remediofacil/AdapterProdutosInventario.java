package revolution.ph.developer.remediofacil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterProdutosInventario extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ProdObj> produtos;
    private Context context;
    private InventarioListener listener;

    public AdapterProdutosInventario(ArrayList<ProdObj> produtos, Context context, InventarioListener listener) {
        this.produtos = produtos;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            return 2;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_criar_produto, parent, false);
            CriarProdViewHolder criarVH = new CriarProdViewHolder(view);
            return criarVH;
        } else {
            View view2 = LayoutInflater.from(context).inflate(R.layout.item_inventario_produto, parent, false);
            InventarioProdViewHolder iVH = new InventarioProdViewHolder(view2);
            return iVH;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 2) {
            InventarioProdViewHolder ivh = (InventarioProdViewHolder) holder;
            ProdObj obj = produtos.get(position - 1);
            Glide.with(context).load(obj.getImgCapa()).into(ivh.foto);
            ivh.nome.setText(obj.getProdName());
            ivh.descricao.setText(obj.getDescr());
            String compra = String.valueOf((int) obj.getPrecoDeCompra()) + ",00";
            String venda = String.valueOf((int) obj.getProdValor()) + ",00";
            String catt = analizarCategoria(obj.getCategoria());
            if(obj.isPromocional()) {
                catt = catt + "  -  " + analizarPromocao(obj.promocional);
            }
            ivh.valorVenda.setText(venda);
            ivh.valorCompra.setText(compra);
            ivh.categoria.setText(catt);
        } else {

        }
    }

    private String analizarPromocao(boolean promo) {
        if (promo) {
            return "Em Promoção";
        } else {
            return "";
        }
    }

    private String analizarCategoria(int i) {
        if (i == 1) {
            return "Medicamentos";
        } else if (i == 2) {
            return "Suplementos";
        } else {
            return "Variedades";
        }
    }

    @Override
    public int getItemCount() {
        return produtos.size() + 1;
    }

    public interface InventarioListener {
        void abrirProduto(ProdObj obj);
        void criarProduto();
    }

    class CriarProdViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ExtendedFloatingActionButton efab;

        public CriarProdViewHolder(@NonNull View itemView) {
            super(itemView);
            efab = (ExtendedFloatingActionButton) itemView.findViewById(R.id.efab_criar_produto);
            efab.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.criarProduto();
        }
    }

    class InventarioProdViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView foto;
        private TextView nome, descricao, valorVenda, valorCompra, categoria;

        public InventarioProdViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = (ImageView) itemView.findViewById(R.id.img_produto_invantario);
            nome = (TextView) itemView.findViewById(R.id.nome_produto_inventario);
            descricao = (TextView) itemView.findViewById(R.id.descricao_produto_inventario);
            valorCompra = (TextView) itemView.findViewById(R.id.preco_compra_produto_inventario);
            valorVenda = (TextView) itemView.findViewById(R.id.preco_consumidor_produto_inventario);
            categoria = (TextView) itemView.findViewById(R.id.categoria_produto_inventario);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int p = getAdapterPosition() - 1;
            ProdObj o = produtos.get(p);
            listener.abrirProduto(o);
        }
    }

}
