package revolution.ph.developer.remediofacil.objects;

public class TokenFcm {

    private String tokenFcm;
    private String nome;

    public TokenFcm(String tokenFcm, String nome) {
        this.tokenFcm = tokenFcm;
        this.nome = nome;
    }

    public TokenFcm() {
    }

    public String getTokenFcm() {
        return tokenFcm;
    }

    public String getNome() {
        return nome;
    }
}
