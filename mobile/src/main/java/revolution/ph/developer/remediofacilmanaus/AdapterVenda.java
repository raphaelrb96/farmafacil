package revolution.ph.developer.remediofacilmanaus;

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

import revolution.ph.developer.remediofacilmanaus.objects.CarComprasActivyParcelable;
import revolution.ph.developer.remediofacilmanaus.objects.CompraFinalizadaParcelable;



public class AdapterVenda extends RecyclerView.Adapter<AdapterVenda.VendaViewHolder> {

    private Context context;
    private ArrayList<CarComprasActivyParcelable> compras;

    public AdapterVenda(Context context, ArrayList<CarComprasActivyParcelable> compras) {
        this.context = context;
        this.compras = compras;
    }

    @NonNull
    @Override
    public VendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carrinho, parent, false);
        VendaViewHolder vendaViewHolder = new VendaViewHolder(view);
        vendaViewHolder.btAumentar.setVisibility(View.GONE);
        vendaViewHolder.btDiminuir.setVisibility(View.GONE);
        vendaViewHolder.btRemover.setVisibility(View.GONE);
        return vendaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VendaViewHolder holder, int position) {
        CarComprasActivyParcelable obj = compras.get(position);
        Glide.with(context).load(obj.getCaminhoImg()).into(holder.foto);
        int vu = (int) obj.getValorUni();
        holder.quantidade.setText(String.valueOf(obj.getQuantidade()) + " x " + String.valueOf(vu + ",00"));
        holder.total.setText(String.valueOf((int) obj.getValorTotal()) + ",00");
        holder.nome.setText(obj.getProdutoName());
    }

    @Override
    public int getItemCount() {
        return compras.size();
    }

    class VendaViewHolder extends RecyclerView.ViewHolder {

        private TextView nome, total, quantidade;
        private View btRemover, btAumentar, btDiminuir;
        private ImageView foto;

        public VendaViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = (TextView) itemView.findViewById(R.id.nome_produto_cart);
            total = (TextView) itemView.findViewById(R.id.valor_total_produto_cart);
            quantidade = (TextView) itemView.findViewById(R.id.quantidade_produto_cart);

            btAumentar = (View) itemView.findViewById(R.id.bt_aumentar_produto_cart);
            btDiminuir = (View) itemView.findViewById(R.id.bt_diminui_produto_cart);
            btRemover = (View) itemView.findViewById(R.id.bt_remove_produto_cart);

            foto = (ImageView) itemView.findViewById(R.id.img_produto_cart);

        }

    }

}
