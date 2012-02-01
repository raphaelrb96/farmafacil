package revolution.ph.developer.remediofacil;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class ProdObjParcelable implements Parcelable {

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
    Map<String, Double> fornecedores;

    public ProdObjParcelable(int categoria, String descr, boolean disponivel, String idProduto, String imgCapa, String laboratorio, int nivel, String prodName, float prodValor, boolean promocional, Map<String, Boolean> tag, Map<String, Double> fornecedores) {
        this.categoria = categoria;
        this.descr = descr;
        this.disponivel = disponivel;
        this.idProduto = idProduto;
        this.imgCapa = imgCapa;
        this.laboratorio = laboratorio;
        this.nivel = nivel;
        this.prodName = prodName;
        this.prodValor = prodValor;
        this.promocional = promocional;
        this.tag = tag;
        this.fornecedores = fornecedores;
    }

    protected ProdObjParcelable(Parcel in) {
        categoria = in.readInt();
        descr = in.readString();
        disponivel = in.readByte() != 0;
        idProduto = in.readString();
        imgCapa = in.readString();
        laboratorio = in.readString();
        nivel = in.readInt();
        prodName = in.readString();
        prodValor = in.readFloat();
        promocional = in.readByte() != 0;
    }

    public static final Creator<ProdObjParcelable> CREATOR = new Creator<ProdObjParcelable>() {
        @Override
        public ProdObjParcelable createFromParcel(Parcel in) {
            return new ProdObjParcelable(in);
        }

        @Override
        public ProdObjParcelable[] newArray(int size) {
            return new ProdObjParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(categoria);
        dest.writeString(descr);
        dest.writeByte((byte) (disponivel ? 1 : 0));
        dest.writeString(idProduto);
        dest.writeString(imgCapa);
        dest.writeString(laboratorio);
        dest.writeInt(nivel);
        dest.writeString(prodName);
        dest.writeFloat(prodValor);
        dest.writeByte((byte) (promocional ? 1 : 0));
    }

    public int getCategoria() {
        return categoria;
    }

    public String getDescr() {
        return descr;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public String getImgCapa() {
        return imgCapa;
    }

    public ProdObj getProd() {
        return new ProdObj(categoria, descr, disponivel, idProduto, imgCapa, laboratorio, nivel, prodName, prodValor, promocional, tag, fornecedores);
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public int getNivel() {
        return nivel;
    }

    public String getProdName() {
        return prodName;
    }

    public float getProdValor() {
        return prodValor;
    }

    public boolean isPromocional() {
        return promocional;
    }

    public Map<String, Boolean> getTag() {
        return tag;
    }

    public Map<String, Double> getFornecedores() {
        return fornecedores;
    }
}
