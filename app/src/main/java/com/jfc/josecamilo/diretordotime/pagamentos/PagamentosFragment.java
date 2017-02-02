package com.jfc.josecamilo.diretordotime.pagamentos;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jfc.josecamilo.diretordotime.R;
import com.jfc.josecamilo.diretordotime.auxiliares.adapters.ListaEventosAdapter;
import com.jfc.josecamilo.diretordotime.auxiliares.calendario.Event;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseHelper;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseManager;
import com.jfc.josecamilo.diretordotime.jogadores.Jogador;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class PagamentosFragment extends Fragment{

    private TextView tvArrecadado;
    private DatabaseManager db;
    private LinearLayout llEsquerda;
    private LinearLayout llDireita;
    private ArrayList<Jogador> jogadores;

    private Event eventoSpinner;
    private int corAcentuada;

    private View viewInflater;

    public PagamentosFragment() {
        // Required empty public constructor
            }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflater = inflater.inflate(R.layout.fragment_pagamentos, container, false);

        if (db == null) {
            DatabaseManager.initializeInstance(new DatabaseHelper(getContext()));
            db = DatabaseManager.getInstance();
        }

        /*PEGAS AS PREFERENCIAS DO USUARIO*/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        corAcentuada = prefs.getInt("cor_acentuada_preferences", -28672);

        criaEventoPagamento();
        tvArrecadado = (TextView) viewInflater.findViewById(R.id.arrecadado);
        llEsquerda = (LinearLayout) viewInflater.findViewById(R.id.topleft);
        llDireita = (LinearLayout) viewInflater.findViewById(R.id.topright);


        /*EVENTOS COMBO*/
        final ArrayList<Event> eventos = db.obterEventOnFin();
        final Spinner spinnerEvent = (Spinner) viewInflater.findViewById(R.id.spinnerEvent);
        ListaEventosAdapter spineradapter = new ListaEventosAdapter(getContext(),eventos);
        spinnerEvent.setAdapter(spineradapter);
        spinnerEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                llEsquerda.removeAllViews();
                llDireita.removeAllViews();

                eventoSpinner = eventos.get(position);
                ArrayList<Pagamento> pagamentosEvent = db.obterPagamentosEvent(eventoSpinner);


                 /* CARREGA JOGADORES DO BANCO */
                jogadores = new ArrayList<Jogador>();
                jogadores = db.obterJogadoresAll();
                for (Jogador jogador:jogadores) {

                    int statusJog = Pagamento.STATUS_INADIMPLENTE;

                    for (Pagamento pagamento:pagamentosEvent) {
                        if (pagamento.getId_jog() == jogador.getId()) {
                            statusJog = pagamento.getStatus();

                        }
                    }

                    TextView tvnew = new TextView(getContext());
                    tvnew.setId((int) jogador.getId());
                    tvnew.setText("\uD83D\uDCB0"+jogador.getNome());
                    tvnew.setTextSize(30);
                    tvnew.setTypeface(null, Typeface.BOLD);
                    tvnew.setTextColor(ContextCompat.getColor(getContext(), R.color.texto_escuro));
                    tvnew.setOnLongClickListener(new MyOnLongClickListener());
                    tvnew.setOnClickListener(new MyOnClickListener());

                    /* POPULA OS LINEARLAYOUTS */
                    if (statusJog == Pagamento.STATUS_INADIMPLENTE){
                        if (jogador.getBloqueado() == Jogador.BLOQUEADO_FALSE){
                            llEsquerda.addView(tvnew);
                        }
                    }else if (statusJog == Pagamento.STATUS_ADIMPLENTE){
                        if (jogador.getBloqueado() == Jogador.BLOQUEADO_TRUE){
                            tvnew.setTypeface(null, Typeface.BOLD_ITALIC);
                            tvnew.setPaintFlags(tvnew.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }

                        llDireita.addView(tvnew);
                    }
                }

                /*ARRECADADO*/
                setArrecadado(eventoSpinner);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*ARRECADADO*/
        if (eventos.size() > 0)
            setArrecadado(eventos.get(0));

        llEsquerda.setOnDragListener(new MyOnDragListener(Pagamento.STATUS_INADIMPLENTE));
        llDireita.setOnDragListener(new MyOnDragListener(Pagamento.STATUS_ADIMPLENTE));


        /*MENSAGEM DE AJUDA*/
        if (eventos.size() == 0){
            Snackbar.make(viewInflater, "Crie eventos financeiros para registrar pagamentos", Snackbar.LENGTH_LONG).show();
        }
        viewInflater.setOnClickListener(new MyOnClickListener());
        llDireita.setOnClickListener(new MyOnClickListener());
        llEsquerda.setOnClickListener(new MyOnClickListener());

        return viewInflater;
    }

    /*MENSAGEM DE AJUDA*/
    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (eventoSpinner == null){
                Snackbar.make(getView(), "Crie eventos financeiros para registrar pagamentos", Snackbar.LENGTH_LONG).show();
            }else if (jogadores.size() == 0){
                Snackbar.make(getView(), "Cadastre jogadores para registrar pagamentos", Snackbar.LENGTH_LONG).show();
            }else{
                Snackbar.make(getView(), "Mantenha o jogador pressionado e arraste", Snackbar.LENGTH_LONG).show();
            }
        }
    }


    /* FUNCOES DRAG IN DROP*/

    class MyOnLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("simple_text", "text");
            View.DragShadowBuilder sb = new View.DragShadowBuilder(v);
            v.startDrag(data, sb, v, 0);
            v.setVisibility(View.INVISIBLE);
            return(true);
        }
    }

    class MyOnDragListener implements View.OnDragListener {
        private int num;

        public MyOnDragListener(int num){
            super();
            this.num = num;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();

            switch(action){
                case DragEvent.ACTION_DRAG_STARTED:
                    //Log.i("--------------------------------------------- Script", num+" - ACTION_DRAG_STARTED");
                    if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                        return(true);
                    }
                    return(false);
                case DragEvent.ACTION_DRAG_ENTERED:
                    //Log.i("---------------------------------------------Script", num+" - ACTION_DRAG_ENTERED");
                    v.setBackgroundColor(corAcentuada);
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    //Log.i("---------------------------------------------Script", num+" - ACTION_DRAG_LOCATION");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //Log.i("---------------------------------------------Script", num+" - ACTION_DRAG_EXITED");
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    break;
                case DragEvent.ACTION_DROP:
                    //Log.i("---------------------------------------------Script", num+" - ACTION_DROP");
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);

                    new AtualizaDadosPag( view , ((TextView) view).getText().toString().substring(2),eventoSpinner,num);

                    view.setVisibility(View.VISIBLE);

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //Log.i("---------------------------------------------Script", num+" - ACTION_DRAG_ENDED");
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    break;
            }

            return true;
        }

    }



    private void criaEventoPagamento(){
        SimpleDateFormat dateFormatMMAAAA = new SimpleDateFormat("MM/yyyy", new Locale("pt", "BR"));
            Calendar calendarAsync = Calendar.getInstance();
            calendarAsync.set(Calendar.DAY_OF_MONTH,1);
            ArrayList<Event> eventos = db.obterEventosDay(calendarAsync.getTimeInMillis());

            if (eventos.isEmpty()){
                Event evento = new Event();
                evento.setName("Mensalidade "+dateFormatMMAAAA.format(calendarAsync.getTime()));
                evento.setDescription("Cobrança gerada mensalmente");
                evento.setStart(calendarAsync.getTimeInMillis());
                evento.setData(calendarAsync.getTimeInMillis());
                evento.setEnd(calendarAsync.getTimeInMillis());
                evento.setColor(Event.COLOR_RED);
                evento.setFinanceiro(Event.FINANCEIRO_TRUE);

                db.inserirEvento(evento);
            }
    }


    public class AtualizaDadosPag{

        private View viewAtu;
        private String nome;
        private Event evento;
        private int status;
        private boolean existPagamento = false;

        public AtualizaDadosPag(final View viewAtu, String nomeJog, Event eventoSel, int newStatus) {

            this.viewAtu = viewAtu;
            this.nome = nomeJog;
            this.evento = eventoSel;
            this.status = newStatus;

            if (newStatus == Pagamento.STATUS_ADIMPLENTE) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Valor pago");

                final EditText et = new EditText(getContext());
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                et.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
                et.addTextChangedListener(new MoneyTextWatcher(et));
                Jogador jogador = db.obterJogador(nomeJog);
                Pagamento valorJaPago = db.obterPagamento(eventoSel, jogador);
                if (valorJaPago.getValor() > 0){
                    et.setText(String.valueOf(valorJaPago.getValor()));
                    existPagamento = true;
                }

                alertDialogBuilder.setView(et);

                alertDialogBuilder.setPositiveButton("OK", null)
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!existPagamento){
                                    llDireita.removeView(viewAtu);
                                    llEsquerda.addView(viewAtu);
                                }

                    }
                });

                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {

                        Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String cleanString = et.getText().toString().replaceAll("[R$.]", "");
                                cleanString = cleanString.replaceAll("[,]",".");
                                float retDialog = Float.parseFloat(cleanString.isEmpty() ? "0.0" : cleanString);

                                if (retDialog == 0.0f){
                                    et.setError("Digite um valor");
                                }else{

                                    /* ATUALIZA A ADIMPLENCIA OU INADIMPLENCIA DO JOGADOR*/
                                    UpdatePagamento refreshPagamento = new UpdatePagamento();
                                    refreshPagamento.setNomeJog(nome);
                                    refreshPagamento.setEvento(evento);
                                    refreshPagamento.setNewStatus(status);
                                    refreshPagamento.setNewValor(retDialog);
                                    refreshPagamento.execute();

                                    alertDialog.dismiss();
                                }
                            }
                        });
                    }
                });
                alertDialog.show();


            } else {
                UpdatePagamento refreshPagamento = new UpdatePagamento();
                refreshPagamento.setNomeJog(nome);
                refreshPagamento.setEvento(evento);
                refreshPagamento.setNewStatus(status);
                refreshPagamento.setNewValor(0);
                refreshPagamento.execute();
            }
        }
    }

    public class UpdatePagamento extends AsyncTask<Void,Void,Void> {

        private String nomeJog;
        private Event evento;
        private int newStatus;
        private float newValor = 0.0f;

        public void setNewStatus(int newStatus) {
            this.newStatus = newStatus;
        }

        public void setEvento(Event evento) {
            this.evento = evento;
        }

        public void setNomeJog(String nomeJog) {
            this.nomeJog = nomeJog;
        }

        public void setNewValor(float newValor) {
            this.newValor = newValor;
        }


        public UpdatePagamento() {
            super();
        }


        private Pagamento getPagamentoJog(String nomeJogador, Event evento){
            Jogador jogador = db.obterJogador(nomeJogador);
            Pagamento ret = db.obterPagamento(evento , jogador);

            if (ret.getId() == 0){
                ret.setId_event(evento.getEventId());
                ret.setId_jog(jogador.getId());

                db.inserirPagamento(ret);
                ret = db.obterPagamento(evento , jogador);
            }
            return ret;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Pagamento pagamento = this.getPagamentoJog(this.nomeJog,this.evento);
            pagamento.setStatus(this.newStatus);
            pagamento.setValor(this.newValor);

            db.atualizarPagamento(pagamento);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setArrecadado(evento);
        }
    }

    public void setArrecadado(Event eventoAr){
        float totalArrecadado = 0;
        ArrayList<Pagamento> arrecadacao = db.obterPagamentosEvent(eventoAr);
        for (Pagamento pag:arrecadacao) {
            totalArrecadado += pag.getValor();
        }

        DecimalFormat df = new DecimalFormat("###,###,###,###,###,###.00");
        tvArrecadado.setText(df.format(totalArrecadado));
    }

    public class MoneyTextWatcher implements TextWatcher {
        private final WeakReference<EditText> editTextWeakReference;

        public MoneyTextWatcher(EditText editText) {
            editTextWeakReference = new WeakReference<EditText>(editText);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        private String current = "";

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            EditText editText = editTextWeakReference.get();
            if(!s.toString().equals(current)){
                editText.removeTextChangedListener(this);

                String cleanString = s.toString().replaceAll("[R$,.]", "");

                double parsed = Double.parseDouble(cleanString);
                String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                current = formatted;
                editText.setText(formatted);
                editText.setSelection(formatted.length());

                editText.addTextChangedListener(this);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

}
