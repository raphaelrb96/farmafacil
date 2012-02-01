package revolution.ph.developer.remediofacil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterProdutosInventario extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ProdObj> produtos;
    private Context context;
    private InventarioListener listener;
    private boolean modoSecreto;
    String forneced = "";
    double menorPrecoFornecdr = 0;

    public AdapterProdutosInventario(ArrayList<ProdObj> produtos, Context context, InventarioListener listener, boolean modoSecreto) {
        this.produtos = produtos;
        this.context = context;
        this.listener = listener;
        this.modoSecreto = modoSecreto;
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
            if (!modoSecreto) {
                iVH.aSwitch.setVisibility(View.GONE);
            } else {
                iVH.aSwitch.setVisibility(View.VISIBLE);
            }
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
            String venda = String.valueOf((int) obj.getProdValor()) + ",00";
            ivh.valorVenda.setText(venda);

            String catt = analizarCategoria(obj.getCategoria());
            if(obj.isPromocional()) {
                catt = catt + "  -  " + analizarPromocao(obj.promocional);
            }

            ivh.categoria.setText(catt);

            if (!modoSecreto) {
                return;
            }

            menorPrecoFornecdr = 0;
            forneced = "";

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                if (obj.getFornecedores() != null) {
                    obj.getFornecedores().forEach(new BiConsumer<String, Double>() {
                        @Override
                        public void accept(String s, Double d) {

                            if (menorPrecoFornecdr == 0) {
                                menorPrecoFornecdr = d;
                                forneced = s;
                            }

                            if (menorPrecoFornecdr > d) {
                                menorPrecoFornecdr = d;
                                forneced = s;
                            }
                        }
                    });
                }
            }

            if(!forneced.equals("")) {
                ivh.descricao.setText("Melhor fornecedor: " + forneced);
            }

//            ivh.valorCompra.setText(String.valueOf((int)menorPrecoFornecdr + ",00"));
//
//            if (menorPrecoFornecdr == 0) {
//                ivh.valorCompra.setVisibility(View.GONE);
//            }


            if (obj.isDisponivel()) {
                ivh.aSwitch.setChecked(true);
                ivh.aSwitch.setText("Disponivel");
            } else {
                ivh.aSwitch.setChecked(false);
                ivh.aSwitch.setText("Indisponivel");
            }

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
        void disponibilidade(ProdObj obj, boolean disponivel);
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
        private Switch aSwitch;

        public InventarioProdViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = (ImageView) itemView.findViewById(R.id.img_produto_invantario);
            nome = (TextView) itemView.findViewById(R.id.nome_produto_inventario);
            descricao = (TextView) itemView.findViewById(R.id.descricao_produto_inventario);
            valorCompra = (TextView) itemView.findViewById(R.id.preco_compra_produto_inventario);
            valorVenda = (TextView) itemView.findViewById(R.id.preco_consumidor_produto_inventario);
            categoria = (TextView) itemView.findViewById(R.id.categoria_produto_inventario);
            aSwitch = (Switch) itemView.findViewById(R.id.switch_disponibilidade);
            itemView.setOnClickListener(this);
            aSwitch.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int p = getAdapterPosition() - 1;
            ProdObj o = produtos.get(p);
            if (v.getId() == R.id.switch_disponibilidade) {
                if (aSwitch.isChecked()) {
                    aSwitch.setText("Disponivel");
                } else {
                    aSwitch.setText("Indisponivel");
                }
                listener.disponibilidade(o, aSwitch.isChecked());
            } else {

                listener.abrirProduto(o);
            }
        }

    }

}
