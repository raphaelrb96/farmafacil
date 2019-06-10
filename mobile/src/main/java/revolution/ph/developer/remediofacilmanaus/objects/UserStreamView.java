package revolution.ph.developer.remediofacilmanaus.objects;

public class UserStreamView {

    private String nome;
    private String uid;
    private String foto;
    private long time;

    public UserStreamView(String nome, String uid, String foto, long time) {
        this.nome = nome;
        this.uid = uid;
        this.foto = foto;
        this.time = time;
    }

    public UserStreamView() {

    }

    public String getNome() {
        return nome;
    }

    public String getUid() {
        return uid;
    }

    public String getFoto() {
        return foto;
    }

    public long getTime() {
        return time;
    }
}
