package revolution.ph.developer.remediofacilmanaus.objects;

import java.util.ArrayList;

import revolution.ph.developer.remediofacilmanaus.CarComprasActivy;

public class CompraFinalizada {

    String adress;
    String complemento;
    String detalhePag;
    int formaDePagar;
    long hora;
    double lat;
    ArrayList<CarComprasActivy> listaDeProdutos;
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

    public CompraFinalizada() {

    }

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

    public ArrayList<CarComprasActivy> getListaDeProdutos() {
        return listaDeProdutos;
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

    public CompraFinalizada(String adress, String complemento, String detalhePag, int formaDePagar, long hora, double lat, ArrayList<CarComprasActivy> listaDeProdutos, double lng, String phoneUser, int tipoDeEntrega, String uidUserCompra, String userNome, String pathFotoUser, float valorTotal, float frete, float compraValor, int statusCompra, String idCompra) {
        this.adress = adress;
        this.complemento = complemento;
        this.detalhePag = detalhePag;
        this.formaDePagar = formaDePagar;
        this.hora = hora;
        this.lat = lat;
        this.listaDeProdutos = listaDeProdutos;
        this.lng = lng;
        this.phoneUser = phoneUser;
        this.tipoDeEntrega = tipoDeEntrega;
        this.uidUserCompra = uidUserCompra;
        this.userNome = userNome;
        this.pathFotoUser = pathFotoUser;
        this.valorTotal = valorTotal;
        this.frete = frete;
        this.compraValor = compraValor;
        this.statusCompra = statusCompra;
        this.idCompra = idCompra;
    }
}

