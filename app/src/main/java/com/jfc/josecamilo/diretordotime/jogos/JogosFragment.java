package com.jfc.josecamilo.diretordotime.jogos;


import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.jfc.josecamilo.diretordotime.MainActivity;
import com.jfc.josecamilo.diretordotime.R;
import com.jfc.josecamilo.diretordotime.auxiliares.calendario.Day;
import com.jfc.josecamilo.diretordotime.auxiliares.calendario.ExtendedCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class JogosFragment extends Fragment {

    private View view;
    private int dia;
    private int mes;
    private int ano;
    private Calendar newDate;
    private SimpleDateFormat dateFormatter;
    private IncJogosFragment incJogosFragment;
    private ListJogosFragment listJogosFragment;



    public JogosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_jogos, container, false);

        /*CARREGA AS PREFERENCIAS DO USUARIO*/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int corAcentuada = prefs.getInt("cor_acentuada_preferences", -28672);

        /*TRATAMENTO DE DATA*/
        dateFormatter = new SimpleDateFormat("EEE dd/MM/yyyy", new Locale("pt", "BR"));
        newDate = Calendar.getInstance();


        /*IMPPLEMENTAÇÃO DO CALENDARIO*/
        ExtendedCalendarView calendario = (ExtendedCalendarView)view.findViewById(R.id.calendar);
        calendario.setGesture(1);
        calendario.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void onDayClicked(AdapterView<?> adapter, View view, int position, long id, Day day) {

                newDate.set(day.getYear(), day.getMonth(), day.getDay());

            }
        });


        /*BOTÃO FLUTUANTE INCLUIR*/
        FloatingActionButton fabIncJog = (FloatingActionButton) view.findViewById(R.id.fabIncJog);
        fabIncJog.setBackgroundTintList(ColorStateList.valueOf(corAcentuada));
        fabIncJog.setOnClickListener(new View.OnClickListener() {
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

        /*BOTÃO FLUTUANTE VISUALIZAR*/
        FloatingActionButton fabVisJog = (FloatingActionButton) view.findViewById(R.id.fabVisJog);
        fabVisJog.setBackgroundTintList(ColorStateList.valueOf(corAcentuada));
        fabVisJog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listJogosFragment = new ListJogosFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("data",newDate.getTimeInMillis());
                listJogosFragment.setArguments(bundle);

                FragmentManager fragManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,listJogosFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                MainActivity.setToolbar("Lista de Eventos");
                MainActivity.countTelas++;
                //Snackbar.make(view, dateFormatter.format(newDate.getTime()), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        return view;
    }


}
