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
import revolution.ph.developer.remediofacilmanaus.objects.UserStreamView;

public class AdapterUsuariosStreamView extends RecyclerView.Adapter<AdapterUsuariosStreamView.UserStreamViewHolder> {

    private Context context;
    private ArrayList<UserStreamView> usuarios;
    private StremViewListener stremViewListener;


    public AdapterUsuariosStreamView(Context context, ArrayList<UserStreamView> usuarios, StremViewListener stremViewListener) {
        this.context = context;
        this.usuarios = usuarios;
        this.stremViewListener = stremViewListener;
    }

    @NonNull
    @Override
    public UserStreamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario_strem_view, parent, false);
        return new UserStreamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserStreamViewHolder holder, int position) {
        UserStreamView user = usuarios.get(position);
        Glide.with(context).load(user.getFoto()).into(holder.imagem);
        holder.nome.setText(user.getNome());
        holder.hora.setText(DateFormatacao.dataCompletaCorrigidaSmall2(new Date(user.getTime()), new Date()));
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public interface StremViewListener {
        void analizerUsuario(String uid);
    }

    class UserStreamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nome, hora;
        ImageView imagem;

        public UserStreamViewHolder(@NonNull View itemView) {
            super(itemView);
            imagem = (ImageView) itemView.findViewById(R.id.img_perfil_stream_view);
            nome = (TextView) itemView.findViewById(R.id.nome_user_strem_view);
            hora = (TextView) itemView.findViewById(R.id.hora_user_strem_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            stremViewListener.analizerUsuario(usuarios.get(getAdapterPosition()).getUid());
        }
    }

}
