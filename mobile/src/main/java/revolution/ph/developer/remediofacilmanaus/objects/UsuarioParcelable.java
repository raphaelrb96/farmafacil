package revolution.ph.developer.remediofacilmanaus.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class UsuarioParcelable implements Parcelable {
    private String nome;
    private String email;
    private String celular;
    private int controleDeVersao;
    private String uid;
    private String pathFoto;
    private int tipoDeUsuario;
    private String provedor;
    private long ultimoLogin;
    private long primeiroLogin;
    private String tokenFcm;

    public UsuarioParcelable(String nome, String email, String celular, int controleDeVersao, String uid, String pathFoto, int tipoDeUsuario, String provedor, long ultimoLogin, long primeiroLogin, String tokenFcm) {
        this.nome = nome;
        this.email = email;
        this.celular = celular;
        this.controleDeVersao = controleDeVersao;
        this.uid = uid;
        this.pathFoto = pathFoto;
        this.tipoDeUsuario = tipoDeUsuario;
        this.provedor = provedor;
        this.ultimoLogin = ultimoLogin;
        this.primeiroLogin = primeiroLogin;
        this.tokenFcm = tokenFcm;
    }

    protected UsuarioParcelable(Parcel in) {
        nome = in.readString();
        email = in.readString();
        celular = in.readString();
        controleDeVersao = in.readInt();
        uid = in.readString();
        pathFoto = in.readString();
        tipoDeUsuario = in.readInt();
        provedor = in.readString();
        ultimoLogin = in.readLong();
        primeiroLogin = in.readLong();
        tokenFcm = in.readString();
    }

    public static final Creator<UsuarioParcelable> CREATOR = new Creator<UsuarioParcelable>() {
        @Override
        public UsuarioParcelable createFromParcel(Parcel in) {
            return new UsuarioParcelable(in);
        }

        @Override
        public UsuarioParcelable[] newArray(int size) {
            return new UsuarioParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(email);
        dest.writeString(celular);
        dest.writeInt(controleDeVersao);
        dest.writeString(uid);
        dest.writeString(pathFoto);
        dest.writeInt(tipoDeUsuario);
        dest.writeString(provedor);
        dest.writeLong(ultimoLogin);
        dest.writeLong(primeiroLogin);
        dest.writeString(tokenFcm);
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCelular() {
        return celular;
    }

    public int getControleDeVersao() {
        return controleDeVersao;
    }

    public String getUid() {
        return uid;
    }

    public String getPathFoto() {
        return pathFoto;
    }

    public int getTipoDeUsuario() {
        return tipoDeUsuario;
    }

    public String getProvedor() {
        return provedor;
    }

    public long getUltimoLogin() {
        return ultimoLogin;
    }

    public long getPrimeiroLogin() {
        return primeiroLogin;
    }

    public String getTokenFcm() {
        return tokenFcm;
    }
}
