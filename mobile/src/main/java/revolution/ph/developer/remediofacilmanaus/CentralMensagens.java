package revolution.ph.developer.remediofacilmanaus;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class CentralMensagens {
    //Descontinuada
    private Date time;

    private long timeNovaMensagem;
    private String uid;
    private String foto;
    private String descricao;
    private long ultimaViewAdm;
    private long ultimaView;
    private String nomeUser;

    //se o timeNovaMensagem > ultimaViewAdm , significa que existe uma mensagem nao lida naquela conversa
    //implementarei isso futuramente


    public CentralMensagens(Date time, long timeNovaMensagem, String uid, String foto, String descricao, long ultimaViewAdm, long ultimaView, String nomeUser) {
        this.time = time;
        this.timeNovaMensagem = timeNovaMensagem;
        this.uid = uid;
        this.foto = foto;
        this.descricao = descricao;
        this.ultimaViewAdm = ultimaViewAdm;
        this.ultimaView = ultimaView;
        this.nomeUser = nomeUser;
    }

    public CentralMensagens() {
    }

    public String getUid() {
        return this.uid;
    }

    @ServerTimestamp
    public Date getTime() {
        return this.time;
    }

    public long getTimeNovaMensagem() {
        return timeNovaMensagem;
    }

    public String getFoto() {
        return foto;
    }

    public String getDescricao() {
        return descricao;
    }

    public long getUltimaViewAdm() {
        return ultimaViewAdm;
    }

    public long getUltimaView() {
        return ultimaView;
    }

    public String getNomeUser() {
        return nomeUser;
    }
}