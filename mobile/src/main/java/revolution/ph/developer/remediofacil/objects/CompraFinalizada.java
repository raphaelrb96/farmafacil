package revolution.ph.developer.remediofacil.objects;

import java.util.ArrayList;

import revolution.ph.developer.remediofacil.CarComprasActivy;

public class CompraFinalizada {

    String adress;
    String complemento;
    String detalhePag;
    int formaDePagar;
    long hora;
    double lat;
    ArrayList<CarComprasActivy> listaDeProdutos;
    double lng;
    String numrCasa;
    String phoneUser;
    int prioridade;
    String uidUserCompra;
    String userNome;
    float valorTotal;

    public CompraFinalizada() {
    }

    public CompraFinalizada(String adress, String complemento, String detalhePag, int formaDePagar, long hora, double lat, ArrayList<CarComprasActivy> listaDeProdutos, double lng, String numrCasa, String phoneUser, int prioridade, String uidUserCompra, String userNome, float valorTotal) {
        this.adress = adress;
        this.complemento = complemento;
        this.detalhePag = detalhePag;
        this.formaDePagar = formaDePagar;
        this.hora = hora;
        this.lat = lat;
        this.listaDeProdutos = listaDeProdutos;
        this.lng = lng;
        this.numrCasa = numrCasa;
        this.phoneUser = phoneUser;
        this.prioridade = prioridade;
        this.uidUserCompra = uidUserCompra;
        this.userNome = userNome;
        this.valorTotal = valorTotal;
    }

    public String getUserNome() {
        return this.userNome;
    }

    public String getDetalhePag() {
        return this.detalhePag;
    }

    public String getAdress() {
        return this.adress;
    }

    public String getComplemento() {
        return this.complemento;
    }

    public double getLat() {
        return this.lat;
    }

    public String getUidUserCompra() {
        return this.uidUserCompra;
    }

    public double getLng() {
        return this.lng;
    }

    public long getHora() {
        return this.hora;
    }

    public int getPrioridade() {
        return this.prioridade;
    }

    public ArrayList<CarComprasActivy> getListaDeProdutos() {
        return this.listaDeProdutos;
    }

    public float getValorTotal() {
        return this.valorTotal;
    }

    public String getNumrCasa() {
        return this.numrCasa;
    }

    public int getFormaDePagar() {
        return this.formaDePagar;
    }

    public String getPhoneUser() {
        return this.phoneUser;
    }
}

