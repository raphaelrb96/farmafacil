package revolution.ph.developer.remediofacilmanaus.objects;

import java.util.ArrayList;

import revolution.ph.developer.remediofacilmanaus.CarComprasActivy;

public class CompraFinal {

    String rua;
    double lat;
    double lng;
    ArrayList<CarComprasActivy> listaDeProdutos;
    String uidUserCompra;
    String userNome;
    String pathPhoto;
    int taxaEntrega;
    int somaProdutos;

    public CompraFinal(String rua, double lat, double lng, ArrayList<CarComprasActivy> listaDeProdutos, String uidUserCompra, String userNome, String pathPhoto, int taxaEntrega, int somaProdutos) {
        this.rua = rua;
        this.lat = lat;
        this.lng = lng;
        this.listaDeProdutos = listaDeProdutos;
        this.uidUserCompra = uidUserCompra;
        this.userNome = userNome;
        this.pathPhoto = pathPhoto;
        this.taxaEntrega = taxaEntrega;
        this.somaProdutos = somaProdutos;
    }

    public CompraFinal() {

    }

    public CompraFinal getCompraFinal() {
        return this;
    }

    public String getRua() {
        return rua;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public ArrayList<CarComprasActivy> getListaDeProdutos() {
        return listaDeProdutos;
    }

    public String getUidUserCompra() {
        return uidUserCompra;
    }

    public String getUserNome() {
        return userNome;
    }

    public String getPathPhoto() {
        return pathPhoto;
    }

    public int getTaxaEntrega() {
        return taxaEntrega;
    }

    public int getSomaProdutos() {
        return somaProdutos;
    }
}
