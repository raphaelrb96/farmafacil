package revolution.ph.developer.remediofacil.objects;

public class UsuarioAnalitycs {

    private long numeroDeCompras;
    private long numeroDeViewsCheckout;
    private long numeroDeViewsCarrinho;

    private long ultimaViewApp;
    private long ultimaViewCart;
    private long ultimoCheckOut;
    private long ultimaCompra;
    private long ultimoViewChat;

    private double valorTotalEmCompras;

    public UsuarioAnalitycs() {

    }

    public UsuarioAnalitycs(long numeroDeCompras, long numeroDeViewsCheckout, long numeroDeViewsCarrinho, long ultimaViewApp, long ultimaViewCart, long ultimoCheckOut, long ultimaCompra, long ultimoViewChat, double valorTotalEmCompras) {
        this.numeroDeCompras = numeroDeCompras;
        this.numeroDeViewsCheckout = numeroDeViewsCheckout;
        this.numeroDeViewsCarrinho = numeroDeViewsCarrinho;
        this.ultimaViewApp = ultimaViewApp;
        this.ultimaViewCart = ultimaViewCart;
        this.ultimoCheckOut = ultimoCheckOut;
        this.ultimaCompra = ultimaCompra;
        this.ultimoViewChat = ultimoViewChat;
        this.valorTotalEmCompras = valorTotalEmCompras;
    }

    public long getNumeroDeCompras() {
        return numeroDeCompras;
    }

    public long getNumeroDeViewsCheckout() {
        return numeroDeViewsCheckout;
    }

    public long getNumeroDeViewsCarrinho() {
        return numeroDeViewsCarrinho;
    }

    public long getUltimaViewApp() {
        return ultimaViewApp;
    }

    public long getUltimaViewCart() {
        return ultimaViewCart;
    }

    public long getUltimoCheckOut() {
        return ultimoCheckOut;
    }

    public long getUltimaCompra() {
        return ultimaCompra;
    }

    public long getUltimoViewChat() {
        return ultimoViewChat;
    }

    public double getValorTotalEmCompras() {
        return valorTotalEmCompras;
    }
}
