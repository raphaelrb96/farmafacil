package revolution.ph.developer.remediofacilmanaus;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import revolution.ph.developer.remediofacilmanaus.analitycs.AnalitycsFacebook;
import revolution.ph.developer.remediofacilmanaus.analitycs.AnalitycsGoogle;
import revolution.ph.developer.remediofacilmanaus.objects.CompraFinalParcelable;
import revolution.ph.developer.remediofacilmanaus.objects.CompraFinalizada;
import revolution.ph.developer.remediofacilmanaus.objects.UserStreamView;

import static revolution.ph.developer.remediofacilmanaus.CarrinhoActivity.produtoss;
import static revolution.ph.developer.remediofacilmanaus.FragmentMain.pathFotoUser;
import static revolution.ph.developer.remediofacilmanaus.FragmentMain.user;
import static revolution.ph.developer.remediofacilmanaus.MainActivity.ids;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConfirmarCompraActivityFragment extends Fragment {

    private BottomSheetBehavior<FrameLayout> sheetBehavior;

    private TickerView tvsoma, tvtaxa, tvtotal;
    private int soma, total, taxafacil, taxarapida, taxa;
    private String rua;
    private TextView tv_nome_rua_conf_compra, mudarEndereco;

    private ProgressBar pb;

    private AnalitycsFacebook analitycsFacebook;
    private AnalitycsGoogle analitycsGoogle;

    private TextInputEditText etTroco, etCelular;

    private CheckBox cbFacil, cbRapida;

    private LinearLayout toolbar;

    private FirebaseFirestore firestore;

    private CheckBox cbDinheiro, cbDebito, cbCredito, cbAlimentacao, cbSemTroco;

    private TextView tvDebito, tvCredito, tvAlimentacao;

    private ExtendedFloatingActionButton efabFinalizarCompra;

    private String detalhePagamento = "";
    private int tipoDePagamento = 0, tipoEntrega = 1;

    private String[] cDebito = {
            "ELO",
            "VISA",
            "MASTERCARD",
            "CABAL"
    };

    private String[] cCredito = {
            "ELO",
            "VISA",
            "MASTERCARD",
            "CABAL",
            "HIPER",
            "HIPERCARD"
    };

    private String[] cAlimentacao = {
            "ALELO",
            "VR",
            "TICKET",
            "SODEXO"
    };
    private CompraFinalParcelable cfp = null;
    private String telefoneMain = "";
    private Toast mToast;


    public ConfirmarCompraActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmar_compra, container, false);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_confirmar_compra);

        tvsoma = (TickerView) view.findViewById(R.id.total_compras_conf_compra);
        tvtaxa = (TickerView) view.findViewById(R.id.taxa_entrega_conf_compra);
        tvtotal = (TickerView) view.findViewById(R.id.total_conf_compra);
        tv_nome_rua_conf_compra = (TextView) view.findViewById(R.id.tv_nome_rua_conf_compra);
        mudarEndereco = (TextView) view.findViewById(R.id.bt_mudar_endereco_conf);

        etTroco = (TextInputEditText) view.findViewById(R.id.text_input_troco_conf_compra);
        etCelular = (TextInputEditText) view.findViewById(R.id.et_celular_conf_compra);

        cbFacil = (CheckBox) view.findViewById(R.id.cb_entrega_facil_conf_compra);
        cbRapida = (CheckBox) view.findViewById(R.id.cb_entrega_rapida_conf_compra);

        cbDinheiro = (CheckBox) view.findViewById(R.id.cb_dinheiro);
        cbDebito = (CheckBox) view.findViewById(R.id.cb_debito);
        cbCredito = (CheckBox) view.findViewById(R.id.cb_credito);
        cbAlimentacao = (CheckBox) view.findViewById(R.id.cb_alimentacao);
        cbSemTroco = (CheckBox) view.findViewById(R.id.cb_nao_precisa_troco);

        tvDebito = (TextView) view.findViewById(R.id.tv_debito);
        tvCredito = (TextView) view.findViewById(R.id.tv_credito);
        tvAlimentacao = (TextView) view.findViewById(R.id.tv_alimentacao);

        toolbar = (LinearLayout) view.findViewById(R.id.toolbar_conf_compra);

        pb = (ProgressBar) view.findViewById(R.id.pb_confirmar_compra);

        efabFinalizarCompra = (ExtendedFloatingActionButton) view.findViewById(R.id.efab_confirmar_compra);

        firestore = FirebaseFirestore.getInstance();

        FrameLayout contentLayout = coordinatorLayout.findViewById(R.id.content_layout_conf_compra);
        sheetBehavior = BottomSheetBehavior.from(contentLayout);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        analitycsFacebook = new AnalitycsFacebook(getActivity());
        analitycsGoogle = new AnalitycsGoogle(getActivity());

        cfp = getActivity().getIntent().getParcelableExtra("cfp");
        soma = cfp.getSomaProdutos();
        taxafacil = cfp.getTaxaEntrega();
        rua = cfp.getRua();
        taxa = taxafacil;
        taxarapida = taxafacil * 2;

        tvtotal.setCharacterList(TickerUtils.getDefaultNumberList());
        tvtaxa.setCharacterList(TickerUtils.getDefaultNumberList());
        tvsoma.setCharacterList(TickerUtils.getDefaultNumberList());

        total = taxa + soma;

        tvtaxa.setText(String.valueOf(taxa) + ",00");
        tvtotal.setText(String.valueOf(total) + ",00");
        tvsoma.setText(String.valueOf(soma) + ",00");
        tv_nome_rua_conf_compra.setText(rua);

        mudarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        cbFacil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (cbRapida.isChecked()) {
                        cbRapida.setChecked(false);
                    }
                    alterarFrete(1);
                } else {
                    cbRapida.setChecked(true);
                }
            }
        });

        cbRapida.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (cbFacil.isChecked()) {
                        cbFacil.setChecked(false);
                    }
                    alterarFrete(2);
                } else {
                    cbFacil.setChecked(true);
                }
            }
        });


        cbDinheiro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etTroco.requestFocus();
                    cbAlimentacao.setChecked(false);
                    cbDebito.setChecked(false);
                    cbCredito.setChecked(false);
                    tvAlimentacao.setText("");
                    tvCredito.setText("");
                    tvDebito.setText("");
                    tipoDePagamento = 4;
                } else {
                    cbSemTroco.setChecked(false);
                    etTroco.setText("");
                }
            }
        });

        etCelular.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    esconderTeclado(etCelular);
                    etCelular.clearFocus();
                }
                return false;
            }
        });

        cbAlimentacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbAlimentacao.isChecked()) {
                    tipoDePagamento = 3;
                    showDialog(3);
                } else {
                    tvAlimentacao.setText("");
                    detalhePagamento = "";
                }
            }
        });

        cbCredito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbCredito.isChecked()) {
                    tipoDePagamento = 2;
                    showDialog(2);
                } else {
                    tvDebito.setText("");
                    detalhePagamento = "";
                }
            }
        });

        cbDebito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbDebito.isChecked()) {
                    tipoDePagamento = 1;
                    showDialog(1);
                } else {
                    tvDebito.setText("");
                    detalhePagamento = "";
                }
            }
        });

        etCelular.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                telefoneMain = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etTroco.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                detalhePagamento = etTroco.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cbSemTroco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    detalhePagamento = "está trocado";
                    etTroco.clearFocus();
                    esconderTeclado(etTroco);
                } else {
                    detalhePagamento = "";
                    etTroco.requestFocus();
                }
            }
        });

        efabFinalizarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CompraFinalizada cf = getDadosCompra();
                if (cf == null) {
                    return;
                }
                telefoneMain = etCelular.getText().toString();
                efabFinalizarCompra.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                firestore.collection("statusDrogaria").document("chave").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot == null) {
                            efabFinalizarCompra.setVisibility(View.VISIBLE);
                            pb.setVisibility(View.GONE);
                            return;
                        }
                        boolean aberto = documentSnapshot.getBoolean("aberto");

                        efabFinalizarCompra.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);

                        if (aberto) {
                            showDialogCompra(1, "Total: " + String.valueOf(total) + ",00\nEndereço: " + cfp.getRua() + "\nTefelone: " + telefoneMain, cf);
                        } else {
                            showDialogCompra(2, "Total: " + String.valueOf(total) + ",00\nEndereço: " + cfp.getRua() + "\nTefelone: " + telefoneMain, cf);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        efabFinalizarCompra.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
                        showDialogCompra(3, "", null);
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (cfp != null) {
            String tipoEntrega = "Entrega Facil";
            if (cbRapida.isChecked()) {
                tipoEntrega = "Entrega Rapida";
            }
            int itens = cfp.getItens();
            analitycsFacebook.logUserVisitaCheckoutEvent(user.getDisplayName(), user.getUid(), pathFotoUser, soma, tipoEntrega, taxa, total, etCelular.getText().toString(), rua, itens);
            analitycsGoogle.logUserVisitaCheckoutEvent(user.getDisplayName(), user.getUid(), pathFotoUser, soma, tipoEntrega, taxa, total, etCelular.getText().toString(), rua);
            UserStreamView userStreamView = new UserStreamView(user.getDisplayName(), user.getUid(), pathFotoUser, System.currentTimeMillis());
            firestore.collection("Eventos").document("stream").collection("checkout").document(user.getUid()).set(userStreamView);
        }
    }

    private void alterarFrete(int x) {
        tipoEntrega = x;
        if (x == 1) {

            //facil
            taxa = taxafacil;

        } else {
            taxa = taxarapida;
        }


        total = taxa + soma;
        tvtaxa.setText(String.valueOf(taxa) + ",00");
        tvtotal.setText(String.valueOf(total) + ",00");
    }

    private void showDialog(int tipo) {
        switch (tipo) {
            case 1:
                //debito
                AlertDialog.Builder dialogAnonimus = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialog)
                        .setTitle("Débito")
                        .setItems(cDebito, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //cb == debito + cartao
                                cbDebito.setChecked(true);
                                cbAlimentacao.setChecked(false);
                                cbCredito.setChecked(false);
                                cbDinheiro.setChecked(false);
                                cbSemTroco.setChecked(false);
                                etTroco.setText("");
                                tvAlimentacao.setText("");
                                tvCredito.setText("");
                                tvDebito.setText(cDebito[which]);
                                detalhePagamento = cDebito[which];
                            }
                        })
                        .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialogAnonimus = dialogAnonimus.create();
                alertDialogAnonimus.show();
                alertDialogAnonimus.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryDark));
                cbDebito.setChecked(false);
                break;
            case 2:
                //credito
                AlertDialog.Builder dialogOffline = new AlertDialog.Builder(getActivity())
                        .setTitle("Crédito")
                        .setItems(cCredito, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cbCredito.setChecked(true);
                                cbAlimentacao.setChecked(false);
                                cbDebito.setChecked(false);
                                cbDinheiro.setChecked(false);
                                cbSemTroco.setChecked(false);
                                etTroco.setText("");
                                tvAlimentacao.setText("");
                                tvDebito.setText("");
                                tvCredito.setText(cCredito[which]);
                                detalhePagamento = cCredito[which];
                            }
                        })
                        .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialogOff = dialogOffline.create();
                alertDialogOff.show();
                alertDialogOff.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryDark));
                cbCredito.setChecked(false);
                break;
            case 3:
                //refeicao
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Alimentação")
                        .setItems(cAlimentacao, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cbAlimentacao.setChecked(true);
                                cbDebito.setChecked(false);
                                cbCredito.setChecked(false);
                                cbDinheiro.setChecked(false);
                                cbSemTroco.setChecked(false);
                                etTroco.setText("");
                                tvDebito.setText("");
                                tvCredito.setText("");
                                tvAlimentacao.setText(cAlimentacao[which]);
                                detalhePagamento = cAlimentacao[which];
                            }
                        })
                        .setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryDark));
                cbAlimentacao.setChecked(false);
                break;
            default:
                break;
        }
    }

    private CompraFinalizada getDadosCompra() {
        if (tipoDePagamento == 0) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getActivity(), "Insira uma forma de pagamento", Toast.LENGTH_LONG);
            mToast.show();
            return null;
        }

        if (detalhePagamento.equals("")) {
            if (mToast != null) {
                mToast.cancel();
            }
            if (tipoDePagamento == 4) {
                mToast = Toast.makeText(getActivity(), "Insira quanto vai precisar de troco", Toast.LENGTH_LONG);
            } else {
                mToast = Toast.makeText(getActivity(), "Insira o cartao que será feito o pagamento", Toast.LENGTH_LONG);
            }

            mToast.show();
            return null;
        }

        if (telefoneMain.length() < 8) {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(getActivity(), "Insira os 8 digitos do seu número", Toast.LENGTH_LONG);
            mToast.show();
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return null;
        }
        CompraFinalizada compraFinalizada = new CompraFinalizada(cfp.getRua(), "", detalhePagamento, tipoDePagamento, System.currentTimeMillis(), cfp.getLat(), produtoss, cfp.getLng(), telefoneMain, tipoEntrega, cfp.getUidUserCompra(), cfp.getUserNome(), cfp.getPathPhoto(), total, taxa, soma, 1, "");
        return compraFinalizada;
    }

    private void showDialogCompra(int tipo, String msg, final CompraFinalizada compraFinalizada) {
        switch (tipo) {
            case 1:
                //R.style.AppCompatAlertDialog
                AlertDialog.Builder dialogAnonimus = new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmar Compra")
                        .setMessage(msg)
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                concluirPedidoDeCompra(compraFinalizada);
                            }
                        });
                AlertDialog alertDialogAnonimus = dialogAnonimus.create();
                alertDialogAnonimus.show();
                alertDialogAnonimus.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryDark));
                break;
            case 2:
                AlertDialog.Builder dialogOffline = new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmar Compra")
                        .setMessage("Encerramos nossas entregas por hoje, se confirmar a compra você vai receber seu pedido amanhã pela manhã.\nConfirma compra assim mesmo ?\n\n" + msg)
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                concluirPedidoDeCompra(compraFinalizada);
                            }
                        });
                AlertDialog alertDialogOff = dialogOffline.create();
                alertDialogOff.show();
                alertDialogOff.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryDark));
                break;
            case 3:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Erro")
                        .setMessage("Não foi possivel concluir sua compra agora. Tente novamente mais tarde")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getActivity().getResources().getColor(R.color.colorSecondaryDark));
                break;
            default:
                break;
        }
    }

    private void concluirPedidoDeCompra(final CompraFinalizada compraFinalizada) {

        efabFinalizarCompra.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);

        WriteBatch batch = firestore.batch();
        DocumentReference compras = firestore.collection("Compras").document();
        DocumentReference minhasCompras = firestore.collection("MinhasCompras").document("Usuario").collection(compraFinalizada.getUidUserCompra()).document(compras.getId());

        String idCompra = compras.getId();

        final CompraFinalizada novaCompra = new CompraFinalizada(compraFinalizada.getAdress(), compraFinalizada.getComplemento(), compraFinalizada.getDetalhePag(), compraFinalizada.getFormaDePagar(), compraFinalizada.getHora(), compraFinalizada.getLat(), compraFinalizada.getListaDeProdutos(), compraFinalizada.getLng(), compraFinalizada.getPhoneUser(), compraFinalizada.getTipoDeEntrega(), compraFinalizada.getUidUserCompra(), compraFinalizada.getUserNome(), compraFinalizada.getPathFotoUser(), compraFinalizada.getValorTotal(), compraFinalizada.getFrete(), compraFinalizada.getCompraValor(), compraFinalizada.getStatusCompra(), idCompra);

        batch.set(compras, novaCompra);
        batch.set(minhasCompras, novaCompra);

        CollectionReference cartRefCollection = firestore.collection("carComprasActivy").document("usuario").collection(cfp.getUidUserCompra());

        for (int i = 0; i < novaCompra.getListaDeProdutos().size(); i++) {
            batch.delete(cartRefCollection.document(novaCompra.getListaDeProdutos().get(i).getIdProdut()));
        }

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ids.clear();
                analitycsFacebook.logUserCompraSolicitadaEvent(novaCompra.getUserNome(), novaCompra.getUidUserCompra(), novaCompra.getPathFotoUser(), novaCompra.getCompraValor(), String.valueOf(novaCompra.getTipoDeEntrega()), novaCompra.getFrete(), novaCompra.getValorTotal(), novaCompra.getPhoneUser(), novaCompra.getAdress()+ " " + novaCompra.getComplemento(), novaCompra.getDetalhePag());
                analitycsGoogle.logUserCompraSolicitadaEvent(novaCompra.getUserNome(), novaCompra.getUidUserCompra(), novaCompra.getPathFotoUser(), novaCompra.getCompraValor(), String.valueOf(novaCompra.getTipoDeEntrega()), novaCompra.getFrete(), novaCompra.getValorTotal(), novaCompra.getPhoneUser(), novaCompra.getAdress()+ " " + novaCompra.getComplemento(), novaCompra.getDetalhePag());
                startActivity(new Intent(getActivity(), ConclusaoActivity.class).putExtra("uid", novaCompra.getUidUserCompra()));
                getActivity().finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                showDialogCompra(3, "", null);
            }
        });
    }

    private void esconderTeclado(TextInputEditText et) {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

}
