package com.jfc.josecamilo.diretordotime.graficos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jfc.josecamilo.diretordotime.R;
import com.jfc.josecamilo.diretordotime.auxiliares.adapters.ListaEventosAdapter;
import com.jfc.josecamilo.diretordotime.auxiliares.calendario.Event;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseHelper;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseManager;

import java.util.ArrayList;
import java.util.Calendar;

public class GraficosJogosFragment extends Fragment{

    private DatabaseManager db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graficos_jogos_fragment, container, false);

        if (db == null) {
            DatabaseManager.initializeInstance(new DatabaseHelper(getContext()));
            db = DatabaseManager.getInstance();
        }

        /*VERIFICA SE EXISTE DADOS PARA EXIBIÇÃO*/
        ArrayList<Event> dados = db.obterEventosNext(Calendar.getInstance().getTimeInMillis());
        if (dados.size() == 0){
            TextView tvnew = new TextView(getContext());
            tvnew.setText("Registre eventos futuros para gerar a lista!");
            tvnew.setTextSize(25);
            tvnew.setTextColor(ContextCompat.getColor(getContext(), R.color.texto_claro));

            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearlayout);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            linearLayout.removeAllViews();
            linearLayout.addView(tvnew);

            return view;
        }

        ListView listEventos = (ListView) view.findViewById(R.id.listeventos);
        ArrayList<Event> eventos = db.obterEventosNext(Calendar.getInstance().getTimeInMillis());

        ListaEventosAdapter eventosAdapter = new ListaEventosAdapter(getContext(),eventos);
        listEventos.setAdapter(eventosAdapter);
        return view;
    }
}
