package revolution.ph.developer.remediofacil.objects;

public class Endereco {

    private String adress;
    private String complemento;
    private double latitude;
    private double longitude;
    private long numSelected;
    private String idPlaceMaps;
    private String idEndereco;
    private long timeUltimoSelected;

    public Endereco() {

    }

    public Endereco(String adress, String complemento, double latitude, double longitude, long numSelected, String idPlaceMaps, String idEndereco, long timeUltimoSelected) {
        this.adress = adress;
        this.complemento = complemento;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numSelected = numSelected;
        this.idPlaceMaps = idPlaceMaps;
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

    public String getIdPlaceMaps() {
        return idPlaceMaps;
    }

    public String getIdEndereco() {
        return idEndereco;
    }

    public long getTimeUltimoSelected() {
        return timeUltimoSelected;
    }
}
