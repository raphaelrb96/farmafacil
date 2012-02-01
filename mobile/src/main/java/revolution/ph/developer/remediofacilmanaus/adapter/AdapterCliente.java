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

import revolution.ph.developer.remediofacilmanaus.DateFormatacao;
import revolution.ph.developer.remediofacilmanaus.R;
import revolution.ph.developer.remediofacilmanaus.objects.Usuario;

public class AdapterCliente extends RecyclerView.Adapter<AdapterCliente.ClienteViewHolder> {

    private ArrayList<Usuario> usuarios;
    private Context context;
    private ClienteListener clienteListener;


    public AdapterCliente(ArrayList<Usuario> usuarios, Context context, ClienteListener clienteListener) {
        this.usuarios = usuarios;
        this.context = context;
        this.clienteListener = clienteListener;
    }

    public interface ClienteListener {
        void detalhesCliente(Usuario usuario);
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cliete, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);
        Glide.with(context).load(usuario.getPathFoto()).into(holder.imageView);
        holder.time.setText(DateFormatacao.dataCompletaCorrigidaSmall2(new Date(usuario.getUltimoLogin()), new Date()));
        holder.nome.setText(usuario.getNome());
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    class ClienteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView nome, time;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_cliente);
            nome = (TextView) itemView.findViewById(R.id.nome_cliente);
            time = (TextView) itemView.findViewById(R.id.time_cliente);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clienteListener.detalhesCliente(usuarios.get(getAdapterPosition()));
        }
    }

}
