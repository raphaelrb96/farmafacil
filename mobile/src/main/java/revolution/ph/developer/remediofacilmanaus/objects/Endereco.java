package revolution.ph.developer.remediofacilmanaus.objects;

public class Endereco {

    private String adress;
    private String complemento;
    private double latitude;
    private double longitude;
    private String numeroCasa;
    private String enderecoCompleto;
    private String idEndereco;

    public Endereco() {

    }

    public Endereco(String adress, String complemento, double latitude, double longitude, String numeroCasa, String enderecoCompleto, String idEndereco) {
        this.adress = adress;
        this.complemento = complemento;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numeroCasa = numeroCasa;
        this.enderecoCompleto = enderecoCompleto;
        this.idEndereco = idEndereco;
    }

    public String getAdress() {
        return adress;
    }

    public String getComplemento() {
        return complemento;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getNumeroCasa() {
        return numeroCasa;
    }

    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }

    public String getIdEndereco() {
        return idEndereco;
    }

}
