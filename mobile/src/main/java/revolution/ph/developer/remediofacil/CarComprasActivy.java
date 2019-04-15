package revolution.ph.developer.remediofacil;

public class CarComprasActivy {
    String caminhoImg;
    String idProdut;
    String labo;
    String produtoName;
    int quantidade;
    float valorTotal;
    float valorUni;

    public CarComprasActivy(String str, String str2, String str3, String str4, float f, int i, float f2) {
        this.idProdut = str;
        this.produtoName = str2;
        this.labo = str3;
        this.caminhoImg = str4;
        this.valorUni = f;
        this.quantidade = i;
        this.valorTotal = f2;
    }

    public CarComprasActivy() {
    }

    public String getIdProdut() {
        return this.idProdut;
    }

    public String getProdutoName() {
        return this.produtoName;
    }

    public float getValorUni() {
        return this.valorUni;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public float getValorTotal() {
        return this.valorTotal;
    }

    public String getLabo() {
        return this.labo;
    }

    public String getCaminhoImg() {
        return this.caminhoImg;
    }
}