package revolution.ph.developer.remediofacilmanaus.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class CompraFinalizadaParcelable implements Parcelable {

    String adress;
    String complemento;
    String detalhePag;
    int formaDePagar;
    long hora;
    double lat;
    double lng;
    String phoneUser;
    int tipoDeEntrega;
    String uidUserCompra;
    String userNome;
    String pathFotoUser;
    float valorTotal;
    float frete;
    float compraValor;
    int statusCompra;
    String idCompra;

    public CompraFinalizadaParcelable(CompraFinalizada cf) {
        this.adress = cf.getAdress();
        this.complemento = cf.getComplemento();
        this.detalhePag = cf.getDetalhePag();
        this.formaDePagar = cf.getFormaDePagar();
        this.hora = cf.getHora();
        this.lat = cf.getLat();
        this.lng = cf.getLng();
        this.phoneUser = cf.getPhoneUser();
        this.tipoDeEntrega = cf.getTipoDeEntrega();
        this.uidUserCompra = cf.getUidUserCompra();
        this.userNome = cf.getUserNome();
        this.pathFotoUser = cf.getPathFotoUser();
        this.valorTotal = cf.getValorTotal();
        this.frete = cf.getFrete();
        this.compraValor = cf.getCompraValor();
        this.statusCompra = cf.getStatusCompra();
        this.idCompra = cf.getIdCompra();
    }

    protected CompraFinalizadaParcelable(Parcel in) {
        adress = in.readString();
        complemento = in.readString();
        detalhePag = in.readString();
        formaDePagar = in.readInt();
        hora = in.readLong();
        lat = in.readDouble();
        lng = in.readDouble();
        phoneUser = in.readString();
        tipoDeEntrega = in.readInt();
        uidUserCompra = in.readString();
        userNome = in.readString();
        pathFotoUser = in.readString();
        valorTotal = in.readFloat();
        frete = in.readFloat();
        compraValor = in.readFloat();
        statusCompra = in.readInt();
        idCompra = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(adress);
        dest.writeString(complemento);
        dest.writeString(detalhePag);
        dest.writeInt(formaDePagar);
        dest.writeLong(hora);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(phoneUser);
        dest.writeInt(tipoDeEntrega);
        dest.writeString(uidUserCompra);
        dest.writeString(userNome);
        dest.writeString(pathFotoUser);
        dest.writeFloat(valorTotal);
        dest.writeFloat(frete);
        dest.writeFloat(compraValor);
        dest.writeInt(statusCompra);
        dest.writeString(idCompra);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CompraFinalizadaParcelable> CREATOR = new Creator<CompraFinalizadaParcelable>() {
        @Override
        public CompraFinalizadaParcelable createFromParcel(Parcel in) {
            return new CompraFinalizadaParcelable(in);
        }

        @Override
        public CompraFinalizadaParcelable[] newArray(int size) {
            return new CompraFinalizadaParcelable[size];
        }
    };


    public String getAdress() {
        return adress;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getDetalhePag() {
        return detalhePag;
    }

    public int getFormaDePagar() {
        return formaDePagar;
    }

    public long getHora() {
        return hora;
    }

    public double getLat() {
        return lat;
    }


    public double getLng() {
        return lng;
    }

    public String getPhoneUser() {
        return phoneUser;
    }

    public int getTipoDeEntrega() {
        return tipoDeEntrega;

    }

    public String getUidUserCompra() {
        return uidUserCompra;
    }

    public String getUserNome() {
        return userNome;
    }

    public String getPathFotoUser() {
        return pathFotoUser;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public float getFrete() {
        return frete;
    }

    public float getCompraValor() {
        return compraValor;
    }

    public int getStatusCompra() {
        return statusCompra;
    }

    public String getIdCompra() {
        return idCompra;
    }
}
