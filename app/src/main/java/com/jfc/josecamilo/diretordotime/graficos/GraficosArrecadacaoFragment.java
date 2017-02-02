package com.jfc.josecamilo.diretordotime.graficos;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jfc.josecamilo.diretordotime.R;
import com.jfc.josecamilo.diretordotime.auxiliares.adapters.ListaEventosArrecadaçãoAdapter;
import com.jfc.josecamilo.diretordotime.auxiliares.calendario.Event;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseHelper;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseManager;
import com.jfc.josecamilo.diretordotime.pagamentos.Pagamento;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jose.camilo on 27/12/2016.
 */
public class GraficosArrecadacaoFragment extends Fragment implements
                                         OnChartGestureListener ,
                                                OnChartValueSelectedListener{

    private LineChart mChart;
    private ArrayList<float[]> arrayEntrys;

    private DatabaseManager db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        /*getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        View view = inflater.inflate(R.layout.fragment_graficos_arrecadacao, container, false);

        if (db == null) {
            DatabaseManager.initializeInstance(new DatabaseHelper(getContext()));
            db = DatabaseManager.getInstance();
        }

        /*VERIFICA SE EXISTE DADOS PARA EXIBIÇÃO*/
        ArrayList<Event> dados = db.obterEventOnFin();
        if (dados.size() == 0){
            TextView tvnew = new TextView(getContext());
            tvnew.setText("Registre eventos e pagamentos para gerar o gráfico!");
            tvnew.setTextSize(25);
            tvnew.setTextColor(ContextCompat.getColor(getContext(), R.color.texto_claro));

            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearlayout);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            linearLayout.removeAllViews();
            linearLayout.addView(tvnew);

            return view;
        }

        arrayEntrys = new ArrayList<>();


        mChart = (LineChart) view.findViewById(R.id.linechart);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);

        // add data
        setData();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);


        /*CARREGA LISTA EVENTOS*/
        ListView listaEvento = (ListView) view.findViewById(R.id.listeventos);
        ArrayList<Event> eventos = db.obterEventOnFin();

        ListaEventosArrecadaçãoAdapter eventosArrecadaçãoAdapter = new ListaEventosArrecadaçãoAdapter(getContext(),eventos);
        listaEvento.setAdapter(eventosArrecadaçãoAdapter);

        return view;
    }


    // This is used to store x-axis values
    // Meses
    private ArrayList<String> setXAxisValues(){
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Event> eventos = db.obterEventOnFin();

        float pagamentos;

        for (Event evento:eventos) {
            Calendar data = Calendar.getInstance();
            data.setTimeInMillis(evento.getStart());
            String mes = data.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

            ArrayList<Pagamento> pagamentosEvento = db.obterPagamentosEvent(evento);
            pagamentos = 0.0f;
            for (Pagamento pagamento:pagamentosEvento) {
                pagamentos += pagamento.getValor();
            }

            if (!xVals.contains(mes)){
                xVals.add(mes);
                arrayEntrys.add(new float[]{pagamentos,xVals.indexOf(mes)});
            }else{
                int ultimaPosicao = arrayEntrys.size()-1;
                float[] ultimoObjeto = arrayEntrys.get(ultimaPosicao);
                arrayEntrys.set(ultimaPosicao, new float[]{ultimoObjeto[0]+ pagamentos,ultimoObjeto[1]});
            }
        }
/*        xVals.add("10");
        xVals.add("20");
        xVals.add("30");
        xVals.add("30.5");
        xVals.add("40");*/

        return xVals;
    }

    // This is used to store Y-axis values
    // total arrecadado no mes , numero da coluna do mes
    private ArrayList<Entry> setYAxisValues(){
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (float[] valores: arrayEntrys) {
            yVals.add(new Entry(valores[0],(int) valores[1]));
        }
        /*yVals.add(new Entry(60, 0));
        yVals.add(new Entry(48, 1));
        yVals.add(new Entry(70.5f, 2));
        yVals.add(new Entry(100, 3));
        yVals.add(new Entry(180.9f, 4));*/

        return yVals;
    }

    private void setData() {
        ArrayList<String> xVals = setXAxisValues();

        ArrayList<Entry> yVals = setYAxisValues();

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "Meses");
        set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        // set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColors(ColorTemplate.COLORFUL_COLORS); // linha colorida
        //set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        //set1.setDrawFilled(true);  //cor preenchimento

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);
        mChart.animateY(5000);
        mChart.setDescription("Arrecadação por mês");

    }

    @Override
    public void onChartGestureStart(MotionEvent me,
                                    ChartTouchListener.ChartGesture
                                            lastPerformedGesture) {

        //Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
    }

    @Override
    public void onChartGestureEnd(MotionEvent me,
                                  ChartTouchListener.ChartGesture
                                          lastPerformedGesture) {

        //Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // un-highlight values after the gesture is finished and no single-tap
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            // or highlightTouch(null) for callback to onNothingSelected(...)
            mChart.highlightValues(null);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        //Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        //Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        //Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2,
                             float velocityX, float velocityY) {
        //Log.i("Fling", "Chart flinged. VeloX: "
                //+ velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        //Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        //Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        //Log.i("Entry selected", e.toString());
        //Log.i("LOWHIGH", "low: " + mChart.getLowestVisibleXIndex()
                //+ ", high: " + mChart.getHighestVisibleXIndex());

        //Log.i("MIN MAX", "xmin: " + mChart.getXChartMin()
                //+ ", xmax: " + mChart.getXChartMax()
                //+ ", ymin: " + mChart.getYChartMin()
                //+ ", ymax: " + mChart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        //Log.i("Nothing selected", "Nothing selected.");
    }
}
