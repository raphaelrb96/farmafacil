package revolution.ph.developer.remediofacilmanaus.objects;

import android.os.Parcel;
import android.os.Parcelable;

import revolution.ph.developer.remediofacilmanaus.CarComprasActivy;

public class CarComprasActivyParcelable implements Parcelable {

    String caminhoImg;
    String idProdut;
    String labo;
    String produtoName;
    int quantidade;
    float valorTotal;
    float valorUni;

    public CarComprasActivyParcelable(CarComprasActivy c) {
        this.caminhoImg = c.getCaminhoImg();
        this.idProdut = c.getIdProdut();
        this.labo = c.getLabo();
        this.produtoName = c.getProdutoName();
        this.quantidade = c.getQuantidade();
        this.valorTotal = c.getValorTotal();
        this.valorUni = c.getValorUni();
    }

    protected CarComprasActivyParcelable(Parcel in) {
        caminhoImg = in.readString();
        idProdut = in.readString();
        labo = in.readString();
        produtoName = in.readString();
        quantidade = in.readInt();
        valorTotal = in.readFloat();
        valorUni = in.readFloat();
    }

    public static final Creator<CarComprasActivyParcelable> CREATOR = new Creator<CarComprasActivyParcelable>() {
        @Override
        public CarComprasActivyParcelable createFromParcel(Parcel in) {
            return new CarComprasActivyParcelable(in);
        }

        @Override
        public CarComprasActivyParcelable[] newArray(int size) {
            return new CarComprasActivyParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caminhoImg);
        dest.writeString(idProdut);
        dest.writeString(labo);
        dest.writeString(produtoName);
        dest.writeInt(quantidade);
        dest.writeFloat(valorTotal);
        dest.writeFloat(valorUni);
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
