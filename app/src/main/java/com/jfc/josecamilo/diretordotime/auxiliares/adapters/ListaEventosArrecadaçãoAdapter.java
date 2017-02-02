package com.jfc.josecamilo.diretordotime.auxiliares.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfc.josecamilo.diretordotime.R;
import com.jfc.josecamilo.diretordotime.auxiliares.calendario.Event;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseHelper;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseManager;
import com.jfc.josecamilo.diretordotime.pagamentos.Pagamento;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by jose.camilo on 15/12/2016.
 */
public class ListaEventosArrecadaçãoAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List<Event> eventos;
    private DatabaseManager db;

    public ListaEventosArrecadaçãoAdapter(Context context, List<Event> eventos) {
        super(context, 0, eventos);
        this.context = context;
        this.eventos = eventos;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_jogos_list,null);

        Event evento = eventos.get(position);

        TextView titulo = (TextView) view.findViewById(R.id.titulo);
        TextView descricao = (TextView) view.findViewById(R.id.descricao);
        TextView dedatahora = (TextView) view.findViewById(R.id.dedatahora);
        TextView atedatahora = (TextView) view.findViewById(R.id.atedatahora);
        TextView local = (TextView) view.findViewById(R.id.local);
        TextView arrecadado = (TextView) view.findViewById(R.id.arrecadado);
        ImageView imagem = (ImageView) view.findViewById(R.id.imagem);

        titulo.setText(evento.getTitle());
        descricao.setText(evento.getDescription());
        local.setText(evento.getLocation());

        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm", new Locale("pt", "BR"));

        newCalendar.setTimeInMillis(evento.getStart());
        dedatahora.setText(dateFormatter.format(newCalendar.getTime()));
        newCalendar.setTimeInMillis(evento.getEnd());
        atedatahora.setText(dateFormatter.format(newCalendar.getTime()));


        switch (evento.getColor()){
            case Event.COLOR_BLUE:
                imagem.setImageResource(R.drawable.blue);
                break;
            case Event.COLOR_RED:
                imagem.setImageResource(R.drawable.red);
                break;
            case Event.COLOR_GREEN:
                imagem.setImageResource(R.drawable.green);
                break;
            case Event.COLOR_ORANGE:
                imagem.setImageResource(R.drawable.orange);
                break;
            case Event.COLOR_PURPLE:
                imagem.setImageResource(R.drawable.purple);
                break;
            default:
        }

        if (evento.getFinanceiro() == Event.FINANCEIRO_TRUE){
            if (db == null) {
                DatabaseManager.initializeInstance(new DatabaseHelper(getContext()));
                db = DatabaseManager.getInstance();
            }
            float totalArrecadado = 0;
            ArrayList<Pagamento> arrecadacao = db.obterPagamentosEvent(evento);
            for (Pagamento pag:arrecadacao) {
                totalArrecadado += pag.getValor();
            }

            DecimalFormat df = new DecimalFormat("###,###,###,###,###,###.00");
            arrecadado.setText("Arrecadado: R$" +df.format(totalArrecadado));
        }

        if (evento.getBloqueado() == Event.BLOQUEADO_TRUE){
            titulo.setTypeface(null,Typeface.ITALIC);
            titulo.setPaintFlags(titulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return this.getView(position,convertView,parent);
    }
}
