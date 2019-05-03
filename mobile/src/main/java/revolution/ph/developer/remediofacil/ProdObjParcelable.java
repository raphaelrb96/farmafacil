package revolution.ph.developer.remediofacil;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class ProdObjParcelable extends ProdObj implements Parcelable {

    protected ProdObjParcelable(Parcel in) {

    }

    public ProdObjParcelable(int categoria, String descr, boolean disponivel, String idProduto, String imgCapa, String laboratorio, int nivel, String prodName, float prodValor, float precoDeCompra, boolean promocional, Map<String, Boolean> tag) {
        super(categoria, descr, disponivel, idProduto, imgCapa, laboratorio, nivel, prodName, prodValor, precoDeCompra, promocional, tag);
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
    }
}
