package revolution.ph.developer.remediofacil;

import java.util.Map;

public class ProdObj {
    int categoria;
    String descr;
    boolean disponivel;
    String idProduto;
    String imgCapa;
    String laboratorio;
    int nivel;
    String prodName;
    float prodValor;
    float precoDeCompra;
    boolean promocional;
    Map<String, Boolean> tag;

    public ProdObj() {
    }

    public ProdObj(int categoria, String descr, boolean disponivel, String idProduto, String imgCapa, String laboratorio, int nivel, String prodName, float prodValor, float precoDeCompra, boolean promocional, Map<String, Boolean> tag) {
        this.categoria = categoria;
        this.descr = descr;
        this.disponivel = disponivel;
        this.idProduto = idProduto;
        this.imgCapa = imgCapa;
        this.laboratorio = laboratorio;
        this.nivel = nivel;
        this.prodName = prodName;
        this.prodValor = prodValor;
        this.precoDeCompra = precoDeCompra;
        this.promocional = promocional;
        this.tag = tag;
    }

    public Map<String, Boolean> getTag() {
        return this.tag;
    }

    public String getIdProduto() {
        return this.idProduto;
    }

    public int getCategoria() {
        return this.categoria;
    }

    public boolean isDisponivel() {
        return this.disponivel;
    }

    public int getNivel() {
        return this.nivel;
    }

    public boolean isPromocional() {
        return this.promocional;
    }

    public String getImgCapa() {
        return this.imgCapa;
    }

    public String getProdName() {
        return this.prodName;
    }

    public String getLaboratorio() {
        return this.laboratorio;
    }

    public float getProdValor() {
        return this.prodValor;
    }

    public String getDescr() {
        return this.descr;
    }

    public float getPrecoDeCompra() {
        return precoDeCompra;
    }
}
