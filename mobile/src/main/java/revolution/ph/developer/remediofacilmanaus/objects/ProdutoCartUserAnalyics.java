package revolution.ph.developer.remediofacilmanaus.objects;

public class ProdutoCartUserAnalyics {
    private int numeroAddCart;
    private long ultimaVezAdicionadoAoCart;
    private String nome;

    public ProdutoCartUserAnalyics(int numeroAddCart, long ultimaVezAdicionadoAoCart, String nome) {
        this.numeroAddCart = numeroAddCart;
        this.ultimaVezAdicionadoAoCart = ultimaVezAdicionadoAoCart;
        this.nome = nome;
    }

    public ProdutoCartUserAnalyics() {

    }

    public int getNumeroAddCart() {
        return numeroAddCart;
    }

    public long getUltimaVezAdicionadoAoCart() {
        return ultimaVezAdicionadoAoCart;
    }

    public String getNome() {
        return nome;
    }
}
