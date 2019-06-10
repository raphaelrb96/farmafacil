package revolution.ph.developer.remediofacilmanaus.analitycs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalitycsGoogle {

    private FirebaseAnalytics logger;
    private Activity activity;
    private Context context;

    public AnalitycsGoogle(Activity activity) {
        this.activity = activity;
        context = activity;
        logger = FirebaseAnalytics.getInstance(activity);
    }

    public void logAdicionarAoCarrinhoEvent (String nome, String idProduto, int categoria, boolean promocional, String imagem, double valToSum) {
        Bundle params = new Bundle();
        params.putString("Nome", nome);
        params.putString("IdProduto", idProduto);
        params.putInt("Categoria", categoria);
        params.putBoolean("Promocional", promocional);
        params.putString("Imagem", imagem);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, idProduto);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, nome);
        if (categoria == 1) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Medicamentos");
        } else if (categoria == 2) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Suplementos");
        } else {
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Variedades");
        }
        bundle.putLong(FirebaseAnalytics.Param.QUANTITY, 1);
        bundle.putDouble(FirebaseAnalytics.Param.VALUE, valToSum);
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "BRL");

        logger.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, bundle);
        logger.logEvent("AdicionarAoCarrinho", params);
    }


    public void logUsuarioAdicionaProdutoAoCartEvent (String nome, String idProduto, int categoria, boolean promocional, String imagem, String nomeDoUsuario, String uidUsuario, String emailUsuario, String fotoUsuario, double valToSum) {
        Bundle params = new Bundle();
        params.putString("Nome", nome);
        params.putString("IdProduto", idProduto);
        params.putInt("Categoria", categoria);
        params.putBoolean("Promocional", promocional);
        params.putString("Imagem", imagem);
        params.putString("NomeDoUsuario", nomeDoUsuario);
        params.putString("UidUsuario", uidUsuario);
        params.putString("EmailUsuario", emailUsuario);
        params.putString("FotoUsuario", fotoUsuario);
        logger.logEvent("UsuarioAdicionaProdutoAoCart", params);
    }


    public void logProdutoVizualizadoEvent (String nomeProduto, String idProduto, double valorProduto) {
        Bundle params = new Bundle();
        params.putString("NomeProduto", nomeProduto);
        params.putString("IdProduto", idProduto);
        params.putDouble("ValorProduto", valorProduto);
        logger.logEvent("ProdutoVizualizado", params);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "BRL");
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, idProduto);
        bundle.putString(FirebaseAnalytics.Param.ITEM_LOCATION_ID, nomeProduto);
        bundle.putDouble(FirebaseAnalytics.Param.VALUE, valorProduto);
        logger.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }


    public void logProdutoCompradoEvent (String nomeProduto, String idProduto, String imagemProduto, String idUser, int quant) {
        Bundle params = new Bundle();
        params.putString("NomeProduto", nomeProduto);
        params.putString("IdProduto", idProduto);
        params.putInt("Quantidade", quant);
        params.putString("IdUsuario", idUser);
        params.putString("ImagemProduto", imagemProduto);
        logger.logEvent("ProdutoComprado", params);
    }


    public void logUserPesquisouEnderecoEvent (String nomeUser, String idUser, String imagemUser, String endereco) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        params.putString("Endereco", endereco);
        logger.logEvent("UserPesquisouEndereco", params);
    }


    public void logUserEnviaMensagemEvent (String nomeUser, String idUser) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        gerarLead("Menssagem");
        logger.logEvent("UserEnviaMensagem", params);
    }

    public void logUserVisitaCarrinhoEvent (String nomeUser, String idUser, String imagemUser) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        logger.logEvent("UserVisitaCarrinho", params);
    }

    public void logUserVisitaChatEvent (String nomeUser, String idUser, String imagemUser) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        logger.logEvent("UserVisitaChat", params);
    }

    public void logUserStreamViewEvent (String nomeUser, String idUser, String imagemUser) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        logger.logEvent("UserStreamView", params);
    }


    public void logUserVisitaCheckoutEvent (String nomeUser, String idUser, String imagemUser, double somaDosProdutos, String tipoEntrega, double valorFrete, double total, String celular, String endereco) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        params.putDouble("SomaDosProdutos", somaDosProdutos);
        params.putString("TipoEntrega", tipoEntrega);
        params.putDouble("ValorFrete", valorFrete);
        params.putDouble("Total", total);
        params.putString("Celular", celular);
        params.putString("Endereco", endereco);

        Bundle bundle = new Bundle();
        bundle.putDouble(FirebaseAnalytics.Param.VALUE, total);
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "BRL");

        logger.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, bundle);

        logger.logEvent("UserVisitaCheckout", params);
    }

    public void logUserCompraSolicitadaEvent (String nomeUser, String idUser, String imagemUser, double somaDosProdutos, String tipoEntrega, double valorFrete, double total, String celular, String endereco, String formaPagamento) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        params.putDouble("SomaDosProdutos", somaDosProdutos);
        params.putString("TipoEntrega", tipoEntrega);
        params.putDouble("ValorFrete", valorFrete);
        params.putDouble("Total", total);
        params.putString("Celular", celular);
        params.putString("Endereco", endereco);
        params.putString("FormaPagamento", formaPagamento);
        logger.logEvent("UserCompraSolicitada", params);
    }


    public void logUserCompraConcluidaEvent (String nomeUser, String idUser, String imagemUser, double somaDosProdutos, String tipoEntrega, double valorFrete, double total, String celular, String endereco, String formaPagamento) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        params.putDouble("SomaDosProdutos", somaDosProdutos);
        params.putString("TipoEntrega", tipoEntrega);
        params.putDouble("ValorFrete", valorFrete);
        params.putDouble("Total", total);
        params.putString("Celular", celular);
        params.putString("Endereco", endereco);
        params.putString("FormaPagamento", formaPagamento);
        logger.logEvent("UserCompraConcluida", params);
    }


    public void logUserCompraCanceladaEvent (String nomeUser, String idUser, String imagemUser, double somaDosProdutos, String tipoEntrega, double valorFrete, double total, String celular, String endereco, String formaPagamento) {
        Bundle params = new Bundle();
        params.putString("NomeUser", nomeUser);
        params.putString("IdUser", idUser);
        params.putString("ImagemUser", imagemUser);
        params.putDouble("SomaDosProdutos", somaDosProdutos);
        params.putString("TipoEntrega", tipoEntrega);
        params.putDouble("ValorFrete", valorFrete);
        params.putDouble("Total", total);
        params.putString("Celular", celular);
        params.putString("Endereco", endereco);
        params.putString("FormaPagamento", formaPagamento);
        logger.logEvent("UserCompraCancelada", params);
    }

    public void logPesquisaProdutoEvent (String nomePesquisa, String nomeUsuario, String uidUsuario, boolean isSucess) {
        Bundle params = new Bundle();
        Bundle bundle = new Bundle();
        params.putString("NomePesquisa", nomePesquisa);
        params.putString("NomeUsuario", nomeUsuario);
        params.putString("UidUsuario", uidUsuario);
        params.putBoolean("ResultadoEncontrado", isSucess);
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, nomePesquisa);
        logger.logEvent("PesquisaProduto", params);
        logger.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
    }


    public void logLoginFaceEvent (boolean facebookLogin, String nomeUsuario, String uidUsuario, String foto, String email, String celular, int cv) {
        Bundle params = new Bundle();
        if(celular == null) {
            celular = "";
        }
        if (foto == null) {
            foto = "";
        }
        params.putBoolean("FacebookLogin", facebookLogin);
        params.putString("NomeUsuario", nomeUsuario);
        params.putString("UidUsuario", uidUsuario);
        params.putString("FotoUsuario", foto);
        params.putString("EmailUsuario", email);
        params.putString("CelularUsuario", celular);
        params.putInt("ControleVersao", cv);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, "Google");
        logger.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

        logger.logEvent("LoginFace", params);
    }


    public void logLoginGoogleEvent (boolean googleLogin, String nomeUsuario, String uidUsuario, String foto, String email, String celular, int cv) {
        Bundle params = new Bundle();
        if(celular == null) {
            celular = "";
        }
        if (foto == null) {
            foto = "";
        }
        params.putBoolean("GoogleLogin", googleLogin);
        params.putString("NomeUsuario", nomeUsuario);
        params.putString("UidUsuario", uidUsuario);
        params.putString("FotoUsuario", foto);
        params.putString("EmailUsuario", email);
        params.putString("CelularUsuario", celular);
        params.putInt("ControleVersao", cv);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, "Google");
        logger.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

        logger.logEvent("LoginGoogle", params);

    }

    public void gerarLead(String s) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.CONTENT, s);
        params.putInt(FirebaseAnalytics.Param.VALUE, 1);
        logger.logEvent(FirebaseAnalytics.Event.GENERATE_LEAD, params);
    }

}
