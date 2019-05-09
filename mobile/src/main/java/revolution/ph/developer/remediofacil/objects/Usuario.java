package revolution.ph.developer.remediofacil.objects;

import java.util.ArrayList;

public class Usuario {

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

    private Endereco endereco;

    public Usuario() {

    }

    public Usuario(String nome, String email, String celular, int controleDeVersao, String uid, String pathFoto, int tipoDeUsuario, String provedor, Endereco endereco, long ultimoLogin, String tokenFcm, long primeiroLogin) {
        this.nome = nome;
        this.email = email;
        this.celular = celular;
        this.controleDeVersao = controleDeVersao;
        this.uid = uid;
        this.pathFoto = pathFoto;
        this.tipoDeUsuario = tipoDeUsuario;
        this.provedor = provedor;
        this.endereco = endereco;
        this.ultimoLogin = ultimoLogin;
        this.tokenFcm = tokenFcm;
        this.primeiroLogin = primeiroLogin;
    }


    public String getTokenFcm() {
        return tokenFcm;
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

    public long getUltimoLogin() {
        return ultimoLogin;
    }

    public String getPathFoto() {
        return pathFoto;
    }

    public String getProvedor() {
        return provedor;
    }

    public int getTipoDeUsuario() {
        return tipoDeUsuario;
    }

    public Endereco getEndereco() {
        return endereco;
    }
}
