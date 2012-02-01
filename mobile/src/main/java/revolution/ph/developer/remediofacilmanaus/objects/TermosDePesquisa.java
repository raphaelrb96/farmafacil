package revolution.ph.developer.remediofacilmanaus.objects;

public class TermosDePesquisa {

    private String termo;
    int quantidade;
    private long ultimaPesquisa;

    public TermosDePesquisa(String termo, int quantidade, long ultimaPesquisa) {
        this.termo = termo;
        this.quantidade = quantidade;
        this.ultimaPesquisa = ultimaPesquisa;
    }

    public TermosDePesquisa() {

    }

    public String getTermo() {
        return termo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public long getUltimaPesquisa() {
        return ultimaPesquisa;
    }
}
