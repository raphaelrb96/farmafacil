package revolution.ph.developer.remediofacil;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import revolution.ph.developer.remediofacil.objects.CompraFinalParcelable;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConfirmarCompraActivityFragment extends Fragment {

    private BottomSheetBehavior<FrameLayout> sheetBehavior;

    private TickerView tvsoma, tvtaxa, tvtotal;
    private int soma, total, taxafacil, taxarapida, taxa;
    private String rua;
    private TextView tv_nome_rua_conf_compra, mudarEndereco;

    private TextInputEditText etTroco, etCelular;

    private CheckBox cbFacil, cbRapida;

    private LinearLayout toolbar;

    private CheckBox cbDinheiro, cbDebito, cbCredito, cbAlimentacao, cbSemTroco;

    private TextView tvDebito, tvCredito, tvAlimentacao;

    private ExtendedFloatingActionButton efabFinalizarCompra;

    private String detalhePagamento = "";
    private int tipoDePagamento = 0, tipoEntrega = 0;

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

        efabFinalizarCompra = view.findViewById(R.id.efab_confirmar_compra);

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

        CompraFinalParcelable cfp = getActivity().getIntent().getParcelableExtra("cfp");
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
                    showDialog(1);
                } else {
                    tvDebito.setText("");
                    detalhePagamento = "";
                }
            }
        });

        cbSemTroco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etTroco.clearFocus();
                    esconderTeclado(etTroco);
                } else {
                    etTroco.requestFocus();
                }
            }
        });

        return view;
    }

    private void alterarFrete(int x) {
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


    private void esconderTeclado(TextInputEditText et) {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

}
