package com.jfc.josecamilo.diretordotime.presenca;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jfc.josecamilo.diretordotime.R;
import com.jfc.josecamilo.diretordotime.auxiliares.adapters.ListaEventosAdapter;
import com.jfc.josecamilo.diretordotime.auxiliares.calendario.Event;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseHelper;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseManager;
import com.jfc.josecamilo.diretordotime.jogadores.Jogador;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PresencaFragment extends Fragment {

    private DatabaseManager db;
    private LinearLayout llEsquerda;
    private LinearLayout llDireita;
    private ArrayList<Jogador> jogadores;

    private int corAcentuada;

    private Event eventoSpinner;

    public PresencaFragment() {
        // Required empty public constructor
            }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_presenca, container, false);

        if (db == null) {
            DatabaseManager.initializeInstance(new DatabaseHelper(getContext()));
            db = DatabaseManager.getInstance();
        }

        /*PEGAS AS PREFERENCIAS DO USUARIO*/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        corAcentuada = prefs.getInt("cor_acentuada_preferences", -28672);

        llEsquerda = (LinearLayout) view.findViewById(R.id.topleft);
        llDireita = (LinearLayout) view.findViewById(R.id.topright);

        /*EVENTOS COMBO*/
        final ArrayList<Event> eventos = db.obterEventOffFin();
        ArrayList<String> listaEventos = new ArrayList<String>();
        for (Event evento:eventos) {
            listaEventos.add(evento.getTitle());
        }
        final Spinner spinnerEvent = (Spinner) view.findViewById(R.id.spinnerEvent);
        ListaEventosAdapter spineradapter = new ListaEventosAdapter(getContext(),eventos);
        spinnerEvent.setAdapter(spineradapter);
        spinnerEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                llEsquerda.removeAllViews();
                llDireita.removeAllViews();

                eventoSpinner = eventos.get(position);
                ArrayList<Presenca> presencasEvent = db.obterPresencaEvent(eventoSpinner);


                 /* CARREGA JOGADORES DO BANCO */
                jogadores = new ArrayList<Jogador>();
                jogadores = db.obterJogadoresAllOn();
                for (Jogador jogador:jogadores) {

                    int statusJog = Presenca.STATUS_NAOVAI;

                    for (Presenca presenca:presencasEvent) {
                        if (presenca.getId_jog() == jogador.getId()) {
                            statusJog = presenca.getStatus();
                        }
                    }

                    TextView tvnew = new TextView(getContext());
                    tvnew.setText("⚽"+jogador.getNome());
                    tvnew.setTextSize(30);
                    tvnew.setTypeface(null, Typeface.BOLD);
                    tvnew.setTextColor(ContextCompat.getColor(getContext(), R.color.texto_escuro));
                    tvnew.setOnLongClickListener(new MyOnLongClickListener());
                    tvnew.setOnClickListener(new MyOnClickListener());

                    /* POPULA OS LINEARLAYOUTS */
                    if (statusJog == Presenca.STATUS_NAOVAI){

                        llEsquerda.addView(tvnew);
                    }else if (statusJog == Presenca.STATUS_CONFIRMADO){

                        llDireita.addView(tvnew);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        llEsquerda.setOnDragListener(new MyOnDragListener(Presenca.STATUS_NAOVAI));
        llDireita.setOnDragListener(new MyOnDragListener(Presenca.STATUS_CONFIRMADO));


        /*BOTÃO FLUTUANTE COMPARTILHAR DADOS*/
        FloatingActionButton fabMsg = (FloatingActionButton) view.findViewById(R.id.fabMsg);
        fabMsg.setBackgroundTintList(ColorStateList.valueOf(corAcentuada));
        fabMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mensagem = "⚽ " + eventoSpinner.getTitle() + "\n" +
                        "\uD83C\uDFE0 " + eventoSpinner.getLocation() + "\n" +
                        "⏰ " + eventoSpinner.getStartDate("EEE dd/MM/yyyy HH:mm") + "\n\n" +
                        "\uD83D\uDC4D\uD83C\uDFFB Confirmados:";
                ArrayList<Presenca> presenca = db.obterPresencaEvent(eventoSpinner);
                for (Presenca item:presenca) {
                    if (item.getStatus() == Presenca.STATUS_CONFIRMADO){
                        Jogador tempJogador = db.obterJogador(item.getId_jog());
                        mensagem += "\n" + tempJogador.getNome();
                    }
                }

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mensagem);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                //Snackbar.make(view, dateFormatter.format(newDate.getTime()), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });


        /*MENSAGEM DE AJUDA*/
        if (eventos.size() == 0){
            Snackbar.make(view, "Crie eventos não financeiro para registrar a presença", Snackbar.LENGTH_LONG).show();
        }
        view.setOnClickListener(new MyOnClickListener());
        llDireita.setOnClickListener(new MyOnClickListener());
        llEsquerda.setOnClickListener(new MyOnClickListener());
        return view;
    }


    /*MENSAGEM DE AJUDA*/
    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (eventoSpinner == null) {
                Snackbar.make(getView(), "Crie eventos não financeiro para registrar a presença", Snackbar.LENGTH_LONG).show();
            }else if (jogadores.size() == 0){
                Snackbar.make(getView(), "Cadastre jogadores para registrar a presença", Snackbar.LENGTH_LONG).show();
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

                    /* ATUALIZA A PRESENCA DO JOGADOR*/
                    AtualizaPresenca refreshPresenca = new AtualizaPresenca();
                    refreshPresenca.setNomeJog(((TextView) view).getText().toString().substring(1));
                    refreshPresenca.setEvento(eventoSpinner);
                    refreshPresenca.setNewStatus(num);
                    refreshPresenca.execute();

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

    public class AtualizaPresenca extends AsyncTask<Void,Void,Void> {

        private String nomeJog;
        private Event evento;
        private int newStatus;

        public void setNewStatus(int newStatus) {
            this.newStatus = newStatus;
        }

        public void setEvento(Event evento) {
            this.evento = evento;
        }

        public void setNomeJog(String nomeJog) {
            this.nomeJog = nomeJog;
        }


        public AtualizaPresenca() {
            super();
        }


        private Presenca getPresencaJog(String nomeJogador, Event evento){
            Jogador jogador = db.obterJogador(nomeJogador);
            Presenca ret = db.obterPresenca(evento , jogador);

            if (ret.getId() == 0){
                ret.setId_event(evento.getEventId());
                ret.setId_jog(jogador.getId());

                db.inserirPresenca(ret);
                ret = db.obterPresenca(evento , jogador);
            }
            return ret;
        }

     @Override
        protected Void doInBackground(Void... params) {
            Presenca presenca = this.getPresencaJog(this.nomeJog,this.evento);
            presenca.setStatus(this.newStatus);

            db.atualizarPresenca(presenca);

            return null;
        }
    }

}
