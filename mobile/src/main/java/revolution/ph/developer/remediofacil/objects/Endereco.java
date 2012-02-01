package revolution.ph.developer.remediofacil.objects;

public class Endereco {

    private String adress;
    private String complemento;
    private double latitude;
    private double longitude;
    private long numSelected;
    private String enderecoCompleto;
    private String idEndereco;
    private long timeUltimoSelected;

    public Endereco() {

    }

    public Endereco(String adress, String complemento, double latitude, double longitude, long numSelected, String enderecoCompleto, String idEndereco, long timeUltimoSelected) {
        this.adress = adress;
        this.complemento = complemento;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numSelected = numSelected;
        this.enderecoCompleto = enderecoCompleto;
        this.idEndereco = idEndereco;
        this.timeUltimoSelected = timeUltimoSelected;
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

    public long getNumSelected() {
        return numSelected;
    }

    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }

    public String getIdEndereco() {
        return idEndereco;
    }

    public long getTimeUltimoSelected() {
        return timeUltimoSelected;
    }
}
