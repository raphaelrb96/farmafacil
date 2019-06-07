package revolution.ph.developer.remediofacilmanaus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MensagemDetalheAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private float factor= 0;
    private int b= 0;
    private Context context;
    private ArrayList<MensagemObject> mensagens;
    private ArrayList<MensagemSemiCarregada> semiCarregadas;
    private String uidUser;
    private long timeAtual;
    private ListenerMensagem listenerMensagem;

    public interface ListenerMensagem {
        void addCart(ProdObj obj);
        void abrirFoto(String path);
    }

    public MensagemDetalheAdapter(Context context, ArrayList<MensagemObject> mensagens, String uidUser, ListenerMensagem listenerMensagem) {
        this.context = context;
        this.listenerMensagem = listenerMensagem;
        this.mensagens = mensagens;
        this.uidUser = uidUser;//id do usuario atual
        semiCarregadas = new ArrayList<>();
        timeAtual = System.currentTimeMillis();
        if (context != null) {
            factor = context.getResources().getDisplayMetrics().density;
            b = (int) (100 * factor);
        }
    }

    public int getItemViewType(int i) {

        if (i == 0) {
            if (i <= semiCarregadas.size()-1) {
                String uid = semiCarregadas.get(i).getUidUser(); //id usuario que mandou msg

                if (uidUser.equals(uid)) {
                    return 3;//primeira enviada
                } else {
                    return 4;//primeira recebida
                }
            } else {
                String uid2 = mensagens.get(i).getUidUser();
                if (uidUser.equals(uid2)) {
                    return 3;
                } else {
                    return 4;
                }
            }
        } else {

            if (i <= semiCarregadas.size()-1) {
                String uid3 = semiCarregadas.get(i).getUidUser();
                if (uidUser.equals(uid3)) {
                    return 1;//enviada
                } else {
                    return 2;//recebida
                }
            } else {
                int position = i - semiCarregadas.size();
                if (uidUser.equals(mensagens.get(position).getUidUser())) {
                    return 1;
                } else {
                    return 2;
                }
            }

        }



    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 1) {
            MensagensEnviadas enviadas = new MensagensEnviadas(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensagem_enviada, parent, false));
//            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) enviadas.itemView.getLayoutParams();
//            ViewGroup.MarginLayoutParams mllp = (ViewGroup.MarginLayoutParams) lp;
//            mllp.bottomMargin = b;

            return enviadas;
        } else if (viewType == 2) {
            MenssagensRecebidas recebidas = new MenssagensRecebidas(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensagem_recebida, parent, false));
//        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) recebidas.itemView.getLayoutParams();
//        ViewGroup.MarginLayoutParams mllp = (ViewGroup.MarginLayoutParams) lp;
//        mllp.bottomMargin = b;
            return recebidas;
        } else if (viewType == 3) {
            MensagensEnviadas enviadas = new MensagensEnviadas(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_primeira_mensagem_enviada, parent, false));
            return enviadas;
        } else {
            MenssagensRecebidas recebidas = new MenssagensRecebidas(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_primeira_mensagem_recebida, parent, false));
            return recebidas;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int p) {

        Date attual = new Date(timeAtual);

        if (p <= semiCarregadas.size()-1) {
            MensagensEnviadas mensagensEnv = (MensagensEnviadas) holder;
            MensagemSemiCarregada semi = semiCarregadas.get(p);
            semi.imagem(mensagensEnv.imgCliente, context);
            Date dateMensagem = new Date(semi.getTimeStamp());
            String date = DateFormatacao.dataCompletaCorrigida(dateMensagem, attual);
            mensagensEnv.hora.setText(date);
            mensagensEnv.mensagem.setText(semi.getMenssagemText());
            mensagensEnv.imgCliente.setVisibility(View.VISIBLE);
            mensagensEnv.pb_mensagem_enviada.setVisibility(View.VISIBLE);
//            if (p == 0) {
//                ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) holder.itemView.getLayoutParams();
//                ViewGroup.MarginLayoutParams mllp = (ViewGroup.MarginLayoutParams) lp;
//                mllp.bottomMargin = b;
//            }
        } else {

            int position = p - semiCarregadas.size();

//            if (p == 0) {
//                ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) holder.itemView.getLayoutParams();
//                ViewGroup.MarginLayoutParams mllp = (ViewGroup.MarginLayoutParams) lp;
//                mllp.bottomMargin = b;
//            }
            MensagemObject mensagemObject = (MensagemObject) this.mensagens.get(position);
            Date dateMensagem = new Date(mensagemObject.getTimeStamp());
            String date = DateFormatacao.dataCompletaCorrigida(dateMensagem, attual);
            if (holder.getItemViewType() == 1 || holder.getItemViewType() == 3) {
                MensagensEnviadas mensagensEnviadas = (MensagensEnviadas) holder;
                mensagensEnviadas.hora.setText(date);
                mensagensEnviadas.mensagem.setText(mensagemObject.getMenssagemText());
                if (mensagemObject.getPathFoto() != null) {
                    mensagensEnviadas.imgCliente.setVisibility(View.VISIBLE);
                    Glide.with(this.context).load(mensagemObject.getPathFoto()).into(mensagensEnviadas.imgCliente);
                } else {
                    mensagensEnviadas.imgCliente.setVisibility(View.GONE);
                }
                if (mensagemObject.getProdObj() != null) {
                    mensagensEnviadas.container_mensagem_enviada.setVisibility(View.GONE);
                    mensagensEnviadas.itemProd.setVisibility(View.VISIBLE);
                    mensagensEnviadas.nomeProd.setText(mensagemObject.getProdObj().getProdName());
                    mensagensEnviadas.valor.setText(String.valueOf(mensagemObject.getProdObj().getProdValor() + ",00"));
                    Glide.with(this.context).load(mensagemObject.getProdObj().getImgCapa()).into(mensagensEnviadas.capaProd);
                    return;
                }
                mensagensEnviadas.itemProd.setVisibility(View.GONE);
                return;
            } else {
                MenssagensRecebidas menssagensRecebidas = (MenssagensRecebidas) holder;
                menssagensRecebidas.hora.setText(date);
                menssagensRecebidas.mensagem.setText(mensagemObject.getMenssagemText());
                if (mensagemObject.getPathFoto() != null) {
                    menssagensRecebidas.imgCliente.setVisibility(View.VISIBLE);
                    Glide.with(this.context).load(mensagemObject.getPathFoto()).into(menssagensRecebidas.imgCliente);
                } else {
                    menssagensRecebidas.imgCliente.setVisibility(View.GONE);
                }
                if (mensagemObject.getProdObj() != null) {
                    menssagensRecebidas.container_mensagem_recebida.setVisibility(View.GONE);
                    menssagensRecebidas.itemProd.setVisibility(View.VISIBLE);
                    menssagensRecebidas.nomeProd.setText(mensagemObject.getProdObj().getProdName());
                    menssagensRecebidas.valor.setText(String.valueOf(mensagemObject.getProdObj().getProdValor() + "0,00"));
                    Glide.with(this.context).load(mensagemObject.getProdObj().getImgCapa()).into(menssagensRecebidas.capaProd);
                    return;
                }
                menssagensRecebidas.itemProd.setVisibility(View.GONE);
            }


        }


    }

    @Override
    public int getItemCount() {
        if (mensagens != null && semiCarregadas != null) {
            return mensagens.size() + semiCarregadas.size();
        } else if (mensagens != null && semiCarregadas == null) {
            return mensagens.size();
        } else if (mensagens.size() == 0 && semiCarregadas != null) {
            return semiCarregadas.size();
        } else {
            return 0;
        }

    }

    public void clearSemiCarregadas() {
        semiCarregadas.clear();
        notifyItemRemoved(0);
    }

    public void setSemiCarregadas(ArrayList<MensagemSemiCarregada> sc) {
        semiCarregadas = sc;
        notifyItemInserted(0);
    }

    class MensagensEnviadas extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView capaProd;
        private FloatingActionButton fab;
        private TextView hora;
        private ImageView imgCliente;
        private CardView itemProd;
        private TextView mensagem;
        private TextView nomeProd;
        private TextView valor;
        private ProgressBar pb_mensagem_enviada;
        private FrameLayout container_mensagem_enviada;

        public MensagensEnviadas(View view) {
            super(view);
            this.mensagem = (TextView) view.findViewById(R.id.tv_menssagem_enviada);
            this.hora = (TextView) view.findViewById(R.id.tv_hora_mensagem_enviada);
            this.itemProd = (CardView) view.findViewById(R.id.item_produto_mensagem_enviada);
            this.fab = (FloatingActionButton) view.findViewById(R.id.fab_item_add_remv_mensagem_enviada);
            this.capaProd = (ImageView) view.findViewById(R.id.capa_produto_mensagem_enviada);
            this.imgCliente = (ImageView) view.findViewById(R.id.img_mensagens_enviada);
            this.valor = (TextView) view.findViewById(R.id.tv_item_valor_mensagem_enviada);
            this.nomeProd = (TextView) view.findViewById(R.id.tv_item_prodt_mensagem_enviada);
            this.container_mensagem_enviada = (FrameLayout) view.findViewById(R.id.container_mensagem_enviada);
            pb_mensagem_enviada = (ProgressBar) view.findViewById(R.id.pb_mensagem_enviada);
            imgCliente.setOnClickListener(this);
            fab.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.img_mensagens_enviada) {
                String s = mensagens.get(getAdapterPosition()).getPathFoto();
                listenerMensagem.abrirFoto(s);

            }
        }
    }

    class MenssagensRecebidas extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView capaProd;
        private FloatingActionButton fab;
        private TextView hora;
        private ImageView imgCliente;
        private CardView itemProd;
        private TextView mensagem;
        private TextView nomeProd;
        private TextView valor;
        private FrameLayout container_mensagem_recebida;

        public MenssagensRecebidas(View view) {
            super(view);
            this.mensagem = (TextView) view.findViewById(R.id.tv_menssagem_recebida);
            this.hora = (TextView) view.findViewById(R.id.tv_hora_mensagem_recebida);
            this.itemProd = (CardView) view.findViewById(R.id.item_produto_mensagem_recebida);
            this.fab = (FloatingActionButton) view.findViewById(R.id.fab_item_add_remv_mensagem_recebida);
            this.capaProd = (ImageView) view.findViewById(R.id.capa_produto_mensagem_recebida);
            this.imgCliente = (ImageView) view.findViewById(R.id.img_mensagens_recebida);
            this.valor = (TextView) view.findViewById(R.id.tv_item_valor_mensagem_recebida);
            this.nomeProd = (TextView) view.findViewById(R.id.tv_item_prodt_mensagem_recebida);
            this.container_mensagem_recebida = (FrameLayout) view.findViewById(R.id.container_mensagem_recebida);
            imgCliente.setOnClickListener(this);
            fab.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.img_mensagens_recebida) {
                String s = mensagens.get(getAdapterPosition()).getPathFoto();
                listenerMensagem.abrirFoto(s);
            }
        }
    }

}
