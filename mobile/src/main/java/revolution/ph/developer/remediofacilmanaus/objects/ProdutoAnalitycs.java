package revolution.ph.developer.remediofacilmanaus.objects;

public class ProdutoAnalitycs {

    private long numeroVies;
    private long numeroAddCart;
    private long numeroDeItensVendidos;
    private long numeroDeCompartilhamentos;

    private long ultimaCompra;
    private long ultimaVezAdicionadoAoCart;
    private long ultimoCompartilhamento;
    private long ultimaView;

    private double valorTotalEmVendas;

    public ProdutoAnalitycs(long numeroVies, long numeroAddCart, long numeroDeItensVendidos, long numeroDeCompartilhamentos, long ultimaCompra, long ultimaVezAdicionadoAoCart, long ultimoCompartilhamento, long ultimaView, double valorTotalEmVendas) {
        this.numeroVies = numeroVies;
        this.numeroAddCart = numeroAddCart;
        this.numeroDeItensVendidos = numeroDeItensVendidos;
        this.numeroDeCompartilhamentos = numeroDeCompartilhamentos;
        this.ultimaCompra = ultimaCompra;
        this.ultimaVezAdicionadoAoCart = ultimaVezAdicionadoAoCart;
        this.ultimoCompartilhamento = ultimoCompartilhamento;
        this.ultimaView = ultimaView;
        this.valorTotalEmVendas = valorTotalEmVendas;
    }

    public ProdutoAnalitycs() {

    }

    public long getNumeroVies() {
        return numeroVies;
    }

    public long getNumeroAddCart() {
        return numeroAddCart;
    }

    public long getNumeroDeItensVendidos() {
        return numeroDeItensVendidos;
    }

    public long getNumeroDeCompartilhamentos() {
        return numeroDeCompartilhamentos;
    }

    public long getUltimaCompra() {
        return ultimaCompra;
    }

    public long getUltimaVezAdicionadoAoCart() {
        return ultimaVezAdicionadoAoCart;
    }

    public long getUltimoCompartilhamento() {
        return ultimoCompartilhamento;
    }

    public long getUltimaView() {
        return ultimaView;
    }

    public double getValorTotalEmVendas() {
        return valorTotalEmVendas;
    }
}
