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
import com.jfc.josecamilo.diretordotime.auxiliares.adapters.ListaJogadoresAdapter;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseHelper;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseManager;
import com.jfc.josecamilo.diretordotime.jogadores.Jogador;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GraficosJogadoresFragment extends Fragment {

    private PieChart pieChart;
    private PieDataSet dataset;
    private PieData data;

    private ArrayList<String> labels;
    private ArrayList<Entry> entries;

    private DatabaseManager db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graficos_jogadores_fragment, container, false);

        if (db == null) {
            DatabaseManager.initializeInstance(new DatabaseHelper(getContext()));
            db = DatabaseManager.getInstance();
        }

        /*VERIFICA SE EXISTE DADOS PARA EXIBIÇÃO*/
        ArrayList<Jogador> dados = db.obterJogadoresAllOn();
        if (dados.size() == 0){
            TextView tvnew = new TextView(getContext());
            tvnew.setText("Cadastre jogadores para gerar o gráfico!");
            tvnew.setTextSize(25);
            tvnew.setTextColor(ContextCompat.getColor(getContext(), R.color.texto_claro));

            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearlayout);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            linearLayout.removeAllViews();
            linearLayout.addView(tvnew);

            return view;
        }

        labels = new ArrayList<>();
        entries = new ArrayList<>();

        setEntriesLabelsDadosJogadores();

        pieChart = (PieChart) view.findViewById(R.id.piechart);
        pieChart.animateY(5000);
        pieChart.setDescription("Distribuição de jogadores em posição");

        /*labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");*/

        /* entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(12f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));*/

        dataset = new PieDataSet(entries, "# Posições");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); // set the color

        data = new PieData(labels, dataset); // initialize Piedata
        pieChart.setData(data); //set data into chart

        /*CARREGA LISTA JOGADORES*/
        ListView listaJogador = (ListView) view.findViewById(R.id.listjogador);
        ArrayList<Jogador> jogadores = db.obterJogadoresAllOn();

        //ordena por posicao
        Collections.sort(jogadores, new Comparator<Jogador>() {
            @Override
            public int compare(Jogador o1, Jogador o2) {
                if (o1.getPosicao() < o2.getPosicao()) return -1;
                else if (o1.getPosicao() > o2.getPosicao()) return +1;
                else return 0;
            }
        });

        ListaJogadoresAdapter jogadoresAdapter = new ListaJogadoresAdapter(getContext(),jogadores);
        listaJogador.setAdapter(jogadoresAdapter);

        return view;
    }

    private void setEntriesLabelsDadosJogadores(){

        /*CARREGA POSICOES NO ARRAY, COM VALOR ZERADO*/
        ArrayList<Integer[]> valores = new ArrayList<>();
        for (int n=0; n<=Jogador.POSICAO_TOTAL;n++){
            valores.add(new Integer[]{0,n});
        }

        /*ALIMENTA QTD DE JOGADORES EM CADA POSICAO*/
        ArrayList<Jogador> jogadores = db.obterJogadoresAllOn();
        for (Jogador jogador: jogadores) {

            valores.set(jogador.getPosicao(),
                    new Integer[]{valores.get(jogador.getPosicao())[0]+1,
                            jogador.getPosicao()});
        }

        /*ALIMENTA VARIAVEIS UTILIZADAS NO PIECHART*/
        for (Integer[] valor:valores) {
            if (valor[0] > 0){
                entries.add(new Entry(valor[0],valor[1]));
                labels.add(new Jogador().getDescPosicao(valor[1]));
            }
        }

    }

}
