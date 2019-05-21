package revolution.ph.developer.remediofacilmanaus;

public class PlaceIdObj {
    String adress;
    String complemento;
    String idPlaceUsuario;
    double lat;
    double lng;
    String nome;
    String numr;
    String phone;

    public PlaceIdObj() {
    }

    public PlaceIdObj(String str, String str2, String str3, String str4, double d, double d2, String str5, String str6) {
        this.idPlaceUsuario = str;
        this.adress = str2;
        this.numr = str3;
        this.complemento = str4;
        this.lat = d;
        this.lng = d2;
        this.nome = str5;
        this.phone = str6;
    }

    public String getNome() {
        return this.nome;
    }

    public String getPhone() {
        return this.phone;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLng() {
        return this.lng;
    }

    public String getIdPlaceUsuario() {
        return this.idPlaceUsuario;
    }

    public String getAdress() {
        return this.adress;
    }

    public String getNumr() {
        return this.numr;
    }

    public String getComplemento() {
        return this.complemento;
    }
}