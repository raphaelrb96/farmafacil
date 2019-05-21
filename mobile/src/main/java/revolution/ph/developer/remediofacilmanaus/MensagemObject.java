package revolution.ph.developer.remediofacilmanaus;

public class MensagemObject {
    private String menssagemText;
    private String pathFoto;
    private ProdObj prodObj;
    private long timeStamp;
    private int tipoMensagem;
    private String uidUser;

    public MensagemObject(long j, String str, int i, String str2, ProdObj prodObj, String str3) {
        this.timeStamp = j;
        this.uidUser = str;
        this.tipoMensagem = i;
        this.pathFoto = str2;
        this.prodObj = prodObj;
        this.menssagemText = str3;
    }

    public MensagemObject() {
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public String getUidUser() {
        return this.uidUser;
    }

    public int getTipoMensagem() {
        return this.tipoMensagem;
    }

    public String getPathFoto() {
        return this.pathFoto;
    }

    public ProdObj getProdObj() {
        return this.prodObj;
    }

    public String getMenssagemText() {
        return this.menssagemText;
    }
}
