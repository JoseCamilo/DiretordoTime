package com.jfc.josecamilo.diretordotime.jogos;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.jfc.josecamilo.diretordotime.MainActivity;
import com.jfc.josecamilo.diretordotime.R;
import com.jfc.josecamilo.diretordotime.auxiliares.adapters.ListaEventosAdapter;
import com.jfc.josecamilo.diretordotime.auxiliares.calendario.Event;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseHelper;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseManager;

import java.util.Calendar;
import java.util.List;

/**
 * Created by jose.camilo on 15/12/2016.
 */
public class ListJogosFragment extends Fragment {

    private Calendar newDate;
    private ListView listEventos;

    private DatabaseManager db;
    private IncJogosFragment incJogosFragment;
    private JogosFragment jogosFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jogos_list, container, false);

        if (db == null) {
            DatabaseManager.initializeInstance(new DatabaseHelper(getContext()));
            db = DatabaseManager.getInstance();
        }

        /*CARREGA AS PREFERENCIAS DO USUARIO*/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int corAcentuada = prefs.getInt("cor_acentuada_preferences", -28672);

        newDate = Calendar.getInstance();

        /*PEGA DADOS DA TELA DE CALENDARIO*/
        Bundle bundle = this.getArguments();
        long longData = 0L;
        if (bundle != null){
            longData = bundle.getLong("data");
        }

        listEventos = (ListView) view.findViewById(R.id.listeventos);
        List<Event> eventos = db.obterEventosDay(longData);

        ListaEventosAdapter eventosAdapter = new ListaEventosAdapter(getContext(),eventos);
        listEventos.setAdapter(eventosAdapter);

        /*BOTÃO FLUTUANTE INCLUIR*/
        FloatingActionButton fabIncList = (FloatingActionButton) view.findViewById(R.id.fabIncList);
        fabIncList.setBackgroundTintList(ColorStateList.valueOf(corAcentuada));
        fabIncList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                incJogosFragment = new IncJogosFragment();
                Bundle bundle = new Bundle();
                bundle.putString("funcao","incluir");
                bundle.putLong("data",newDate.getTimeInMillis());
                incJogosFragment.setArguments(bundle);

                FragmentManager fragManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,incJogosFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                MainActivity.setToolbar("Incluir Evento");
                MainActivity.countTelas++;
                //Snackbar.make(view, dateFormatter.format(newDate.getTime()), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        /*BOTÃO FLUTUANTE EDITAR*/
        FloatingActionButton fabEditList = (FloatingActionButton) view.findViewById(R.id.fabEditList);
        fabEditList.setBackgroundTintList(ColorStateList.valueOf(corAcentuada));
        fabEditList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                incJogosFragment = new IncJogosFragment();
                Bundle bundle = new Bundle();
                Event evento = new Event();
                try{
                    evento = (Event) listEventos.getAdapter().getItem(listEventos.getCheckedItemPosition());
                    bundle.putString("funcao","alterar");
                    bundle.putLong("idEvent",evento.getIdPai() == 0 ? evento.getEventId() : evento.getIdPai());
                    incJogosFragment.setArguments(bundle);

                    FragmentManager fragManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,incJogosFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    MainActivity.setToolbar("Alterar Evento");
                    MainActivity.countTelas++;
                }catch (Exception e){
                    Toast.makeText(getContext(),"Nenhum Evento selecionado!",Toast.LENGTH_LONG).show();
                }

                //Snackbar.make(view, dateFormatter.format(newDate.getTime()), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        /*BOTÃO FLUTUANTE CALENDARIO*/
        FloatingActionButton fabVisList = (FloatingActionButton) view.findViewById(R.id.fabVisList);
        fabVisList.setBackgroundTintList(ColorStateList.valueOf(corAcentuada));
        fabVisList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                jogosFragment = new JogosFragment();

                FragmentManager fragManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,jogosFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                MainActivity.setToolbar("Calendário");
                MainActivity.countTelas++;

                //Snackbar.make(view, dateFormatter.format(newDate.getTime()), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        return view;

    }
}
