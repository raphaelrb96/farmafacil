package revolution.ph.developer.remediofacilmanaus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static revolution.ph.developer.remediofacilmanaus.CarrinhoActivity.produtoss;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.CartViewHolder> {

    private AnalizarClickPayFinal clickPayFinal;
    private Context context;

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrinho, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CarComprasActivy obj = produtoss.get(position);
        Glide.with(context).load(obj.caminhoImg).into(holder.foto);
        int vu = (int) obj.valorUni;
        holder.quantidade.setText(String.valueOf(obj.quantidade) + " x " + String.valueOf(vu + ",00"));
        holder.total.setText(String.valueOf((int) obj.valorTotal) + ",00");
        holder.nome.setText(obj.produtoName);
    }

    @Override
    public int getItemCount() {
        return produtoss.size();
    }

    public void swapDados(ArrayList<CarComprasActivy> arrayList) {
        produtoss = arrayList;
        notifyDataSetChanged();
    }

    public interface AnalizarClickPayFinal {
        void aumentarQuantidade(CarComprasActivy carComprasActivy, String str, int i);

        void diminuirQuantidade(CarComprasActivy carComprasActivy, String str, int i);

        void removerProduto(String str, int p);
    }

    public AdapterCart(AnalizarClickPayFinal clickPayFinal, Context context) {
        this.clickPayFinal = clickPayFinal;
        this.context = context;
    }

    class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nome, total, quantidade;
        private View btRemover, btAumentar, btDiminuir;
        private ImageView foto;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = (TextView) itemView.findViewById(R.id.nome_produto_cart);
            total = (TextView) itemView.findViewById(R.id.valor_total_produto_cart);
            quantidade = (TextView) itemView.findViewById(R.id.quantidade_produto_cart);

            btAumentar = (View) itemView.findViewById(R.id.bt_aumentar_produto_cart);
            btDiminuir = (View) itemView.findViewById(R.id.bt_diminui_produto_cart);
            btRemover = (View) itemView.findViewById(R.id.bt_remove_produto_cart);

            foto = (ImageView) itemView.findViewById(R.id.img_produto_cart);

            btAumentar.setOnClickListener(this);
            btRemover.setOnClickListener(this);
            btDiminuir.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            CarComprasActivy c = produtoss.get(getAdapterPosition());
            if (v.getId() == R.id.bt_aumentar_produto_cart) {

                clickPayFinal.aumentarQuantidade(c, c.idProdut, getAdapterPosition());
            } else if (v.getId() == R.id.bt_diminui_produto_cart) {
                clickPayFinal.diminuirQuantidade(c, c.idProdut, getAdapterPosition());
            } else if (v.getId() == R.id.bt_remove_produto_cart) {
                clickPayFinal.removerProduto(c.idProdut, getAdapterPosition());
            }
        }
    }

}
