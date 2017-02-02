package com.jfc.josecamilo.diretordotime.jogadores;


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jfc.josecamilo.diretordotime.MainActivity;
import com.jfc.josecamilo.diretordotime.R;
import com.jfc.josecamilo.diretordotime.auxiliares.adapters.ListaJogadoresAdapter;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseHelper;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseManager;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class JogadoresFragment extends Fragment {

    private View view;
    private ListView listView;
    private EditText etPesquisa;
    private Button btnPesquisa;
    private DatabaseManager db;
    private IncJogadorFragment incJogadorFragment;
    private int corAcentuada;

    public JogadoresFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*CARREGA O BANCO DE DADOS*/
        if (db == null) {
            DatabaseManager.initializeInstance(new DatabaseHelper(getContext()));
            db = DatabaseManager.getInstance();
        }

        /*CARREGA AS PREFERENCIAS DO USUARIO*/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        corAcentuada = prefs.getInt("cor_acentuada_preferences", -28672);

        /*CARREGA OBJETOS DA TELA*/
        view = inflater.inflate(R.layout.fragment_jogadores_pesq, container, false);
        listView = (ListView) view.findViewById(R.id.listJogadores);
        etPesquisa = (EditText) view.findViewById(R.id.etPesquisa);
        btnPesquisa = (Button) view.findViewById(R.id.btnPesquisa);

        /*CAMPO DE PESQUISA*/
        //btnPesquisa.setBackgroundResource(R.drawable.button_calendar_states);
        btnPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Jogador> jogadores = db.obterJogadorLike(etPesquisa.getText().toString());
                ListaJogadoresAdapter listaJogadoresAdapter = new ListaJogadoresAdapter(getContext(),jogadores);
                listView.setAdapter(listaJogadoresAdapter);
            }
        });

        /*LISTA DE JOGADORES*/
        ArrayList<Jogador> jogadores = db.obterJogadoresAll();
        ListaJogadoresAdapter listaJogadoresAdapter = new ListaJogadoresAdapter(getContext(),jogadores);
        listView.setAdapter(listaJogadoresAdapter);


        /*BOTÃO FLUTUANTE INCLUIR*/
        FloatingActionButton fabIncJog = (FloatingActionButton) view.findViewById(R.id.fabIncJog);
        fabIncJog.setBackgroundTintList(ColorStateList.valueOf(corAcentuada));
        fabIncJog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                incJogadorFragment = new IncJogadorFragment();

                FragmentManager fragManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,incJogadorFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                MainActivity.setToolbar("Incluir Jogador");
                MainActivity.countTelas++;

                //Snackbar.make(view, dateFormatter.format(newDate.getTime()), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        /*BOTÃO FLUTUANTE EDITAR*/
        FloatingActionButton fabEditJog = (FloatingActionButton) view.findViewById(R.id.fabEditJog);
        fabEditJog.setBackgroundTintList(ColorStateList.valueOf(corAcentuada));
        fabEditJog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                incJogadorFragment = new IncJogadorFragment();
                Bundle bundle = new Bundle();
                Jogador jogador = new Jogador();
                try{
                    jogador = (Jogador) listView.getAdapter().getItem(listView.getCheckedItemPosition());
                    bundle.putLong("idJog",jogador.getId());
                    incJogadorFragment.setArguments(bundle);

                    FragmentManager fragManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,incJogadorFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    MainActivity.setToolbar("Alterar Jogador");
                    MainActivity.countTelas++;
                }catch (Exception e){
                    Toast.makeText(getContext(),"Nenhum jogador selecionado!",Toast.LENGTH_LONG).show();
                }


                //Snackbar.make(view, dateFormatter.format(newDate.getTime()), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });


        return view;
    }

}
