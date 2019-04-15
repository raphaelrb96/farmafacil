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
    boolean promocional;
    Map<String, Boolean> tag;

    public ProdObj() {
    }

    public ProdObj(String str, String str2, String str3, String str4, float f, int i, boolean z, int i2, boolean z2, String str5, Map<String, Boolean> map) {
        this.imgCapa = str;
        this.prodName = str2;
        this.laboratorio = str3;
        this.descr = str4;
        this.prodValor = f;
        this.categoria = i;
        this.disponivel = z;
        this.nivel = i2;
        this.promocional = z2;
        this.idProduto = str5;
        this.tag = map;
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
}
