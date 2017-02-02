package com.jfc.josecamilo.diretordotime.jogos;


import android.app.DatePickerDialog;
import android.app.Service;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jfc.josecamilo.diretordotime.R;
import com.jfc.josecamilo.diretordotime.auxiliares.calendario.Event;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseHelper;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by jose.camilo on 28/11/2016.
 */
public class IncJogosFragment extends Fragment {

    private Button datainicio;
    private Button horainicio;
    private Button datafim;
    private Button horafim;
    private Spinner spinercores;
    private ImageView ivcor;
    private EditText etTitulo;
    private EditText etDescricao;
    private EditText etLocal;
    private ToggleButton toggleFin;
    private CheckBox cBbloqueado;

    private DatePickerDialog inicioDatePickerDialog;
    private DatePickerDialog fimDatePickerDialog;
    private TimePickerDialog inicioTimePickerDialog;
    private TimePickerDialog fimTimePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat hourFormatter;

    private Calendar calInicio;
    private Calendar calFim;

    private Event altEvento;

    private DatabaseManager db;


    public IncJogosFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_jogos_inc, container, false);
        if (db == null) {
            DatabaseManager.initializeInstance(new DatabaseHelper(getContext()));
            db = DatabaseManager.getInstance();
        }

        etTitulo = (EditText) view.findViewById(R.id.etevento);
        etDescricao = (EditText) view.findViewById(R.id.etdesc);
        etLocal = (EditText) view.findViewById(R.id.etlocal);
        spinercores = (Spinner) view.findViewById(R.id.spinercor);
        ivcor = (ImageView) view.findViewById(R.id.ivcor);
        toggleFin = (ToggleButton) view.findViewById(R.id.toggleFin);
        cBbloqueado = (CheckBox) view.findViewById(R.id.jogobloqueado);

        dateFormatter = new SimpleDateFormat("EEE dd/MM/yyyy", new Locale("pt", "BR"));
        hourFormatter = new SimpleDateFormat("HH:mm", new Locale("pt", "BR"));
        //Calendar newCalendar = Calendar.getInstance();



        /*DATA DO EVENTO*/
        /*DATA INICIO*/
        calInicio = Calendar.getInstance();
        datainicio = (Button) view.findViewById(R.id.btndiainicio);
        datainicio.setText(dateFormatter.format(calInicio.getTime()));
        datainicio.setInputType( InputType.TYPE_NULL );
        datainicio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inicioDatePickerDialog.setTitle("Data De");
                inicioDatePickerDialog.show();
            }
        });
        inicioDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calInicio.set(year, monthOfYear, dayOfMonth);
                datainicio.setText(dateFormatter.format(calInicio.getTime()));


                calFim.set(year, monthOfYear, dayOfMonth);
                datafim.setText(dateFormatter.format(calFim.getTime()));
            }
        },calInicio.get(Calendar.YEAR), calInicio.get(Calendar.MONTH), calInicio.get(Calendar.DAY_OF_MONTH)){
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                super.onDateChanged(view, year, month, day);
                inicioDatePickerDialog.setTitle("Data De");
            }
        };


        /*DATA FIM*/
        calFim = Calendar.getInstance();
        datafim = (Button) view.findViewById(R.id.btndiafim);
        datafim.setText(dateFormatter.format(calFim.getTime()));
        datafim.setInputType( InputType.TYPE_NULL );
        datafim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fimDatePickerDialog.setTitle("Data Ate");
                fimDatePickerDialog.show();
            }
        });
        fimDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calFim.set(year, monthOfYear, dayOfMonth);
                datafim.setText(dateFormatter.format(calFim.getTime()));
            }
        },calFim.get(Calendar.YEAR), calFim.get(Calendar.MONTH), calFim.get(Calendar.DAY_OF_MONTH)){
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                super.onDateChanged(view, year, month, day);
                fimDatePickerDialog.setTitle("Data Ate");
            }
        };



        /*HORA DO EVENTO*/
        /*HORA INICIO*/
        horainicio = (Button) view.findViewById(R.id.btnhrinicio);
        horainicio.setText(hourFormatter.format(calInicio.getTime()));
        horainicio.setInputType(InputType.TYPE_NULL);
        horainicio.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                inicioTimePickerDialog.setTitle("Hora De");
                inicioTimePickerDialog.show();
            }
        });
        inicioTimePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calInicio.set(calInicio.get(Calendar.YEAR),calInicio.get(Calendar.MONTH),calInicio.get(Calendar.DAY_OF_MONTH),hourOfDay,minute);
                horainicio.setText(hourFormatter.format(calInicio.getTime()));

                calFim.set(calFim.get(Calendar.YEAR),calFim.get(Calendar.MONTH),calFim.get(Calendar.DAY_OF_MONTH),hourOfDay,minute);
                horafim.setText(hourFormatter.format(calFim.getTime()));
            }
        },calInicio.get(Calendar.HOUR_OF_DAY),calInicio.get(Calendar.MINUTE), true ){
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                super.onTimeChanged(view, hourOfDay, minute);
                inicioTimePickerDialog.setTitle("Hora De");
            }
        };


        /*HORA FIM*/
        horafim = (Button) view.findViewById(R.id.btnhrfim);
        horafim.setText(hourFormatter.format(calFim.getTime()));
        horafim.setInputType(InputType.TYPE_NULL);
        horafim.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                fimTimePickerDialog.setTitle("Hora Ate");
                fimTimePickerDialog.show();
            }
        });
        fimTimePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calFim.set(calFim.get(Calendar.YEAR),calFim.get(Calendar.MONTH),calFim.get(Calendar.DAY_OF_MONTH),hourOfDay,minute);
                horafim.setText(hourFormatter.format(calFim.getTime()));
            }
        },calFim.get(Calendar.HOUR_OF_DAY),calFim.get(Calendar.MINUTE), true ){
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                super.onTimeChanged(view, hourOfDay, minute);
                fimTimePickerDialog.setTitle("Hora Ate");
            }
        };

        /*COMBO DE CORES*/
        List<String> listacores = new ArrayList<String>();

        for (int nx=0;nx <= Event.COLOR_TOTAL;nx++){
            listacores.add(new Event().getDescColor(nx));
        }

        ArrayAdapter<String> spineradapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, listacores );
        spineradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinercores.setAdapter(spineradapter);
        spinercores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (spinercores.getSelectedItem().toString()){
                    case "azul":
                        ivcor.setImageResource(R.drawable.blue);
                        break;
                    case "vermelho":
                        ivcor.setImageResource(R.drawable.red);
                        break;
                    case "verde":
                        ivcor.setImageResource(R.drawable.green);
                        break;
                    case "laranja":
                        ivcor.setImageResource(R.drawable.orange);
                        break;
                    case "roxo":
                        ivcor.setImageResource(R.drawable.purple);
                        break;
                    case "nenhuma":
                        ivcor.setImageResource(0);
                        break;
                    default:
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*PEGA DADOS DA TELA DE CALENDARIO*/
        Bundle bundle = this.getArguments();
        if (bundle != null){
            String tela = bundle.getString("funcao");
            if (tela.equals("incluir")) {
                long intDatainicio = bundle.getLong("data");
                long intDatafim = bundle.getLong("data");

                calInicio.setTimeInMillis(intDatainicio);
                calFim.setTimeInMillis(intDatafim);

                datainicio.setText(dateFormatter.format(calInicio.getTime()));
                datafim.setText(dateFormatter.format(calFim.getTime()));
                horainicio.setText(hourFormatter.format(calInicio.getTime()));
                horafim.setText(hourFormatter.format(calFim.getTime()));
            }else if (tela.equals("alterar")){
                long idEvent = bundle.getLong("idEvent");
                altEvento = db.obterEvent(idEvent);

                etTitulo.setText(altEvento.getTitle());
                etDescricao.setText(altEvento.getDescription());
                etLocal.setText(altEvento.getLocation());

                calInicio.setTimeInMillis(altEvento.getStart());
                calFim.setTimeInMillis(altEvento.getEnd());

                datainicio.setText(dateFormatter.format(calInicio.getTime()));
                datafim.setText(dateFormatter.format(calFim.getTime()));
                horainicio.setText(hourFormatter.format(calInicio.getTime()));
                horafim.setText(hourFormatter.format(calFim.getTime()));

                toggleFin.setChecked(altEvento.getFinanceiro() == Event.FINANCEIRO_TRUE);
                spinercores.setSelection(altEvento.getColor());
                setImagemCor(altEvento.getColor());

                cBbloqueado.setChecked(altEvento.getBloqueado() == Event.BLOQUEADO_TRUE);
            }
        }


        /*BOTAO SALVAR*/
        final Button salvarJogo = (Button) view.findViewById(R.id.btnsalvarjogo);
        salvarJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etTitulo.getText().toString().isEmpty()){
                    etTitulo.requestFocus();
                    etTitulo.setError("O título é obrigatório");
                }else {

                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(etTitulo.getWindowToken(), 0);
                    }

                    if (altEvento == null) {
                        int diaDe = calInicio.get(Calendar.DAY_OF_MONTH);
                        int diaAte = calFim.get(Calendar.DAY_OF_MONTH);
                        Event eventoPai = new Event();

                        while (diaDe <= diaAte) {

                            Calendar calDia = Calendar.getInstance();
                            calDia.set(calInicio.get(Calendar.YEAR), calInicio.get(Calendar.MONTH), diaDe, calInicio.get(Calendar.HOUR), calInicio.get(Calendar.MINUTE), calInicio.get(Calendar.SECOND));

                            Event eventonew = new Event();
                            eventonew.setName(etTitulo.getText().toString());
                            eventonew.setDescription(etDescricao.getText().toString());
                            eventonew.setLocation(etLocal.getText().toString());
                            eventonew.setStart(calInicio.getTimeInMillis());
                            eventonew.setEnd(calFim.getTimeInMillis());
                            eventonew.setData(calDia.getTimeInMillis());
                            eventonew.setColor(eventonew.colorStrToInt(spinercores.getSelectedItem().toString()));
                            eventonew.setFinanceiro(toggleFin.isChecked() ? Event.FINANCEIRO_TRUE : Event.FINANCEIRO_FALSE);
                            eventonew.setIdPai(eventoPai.getEventId());
                            eventonew.setBloqueado(cBbloqueado.isChecked() ? Event.BLOQUEADO_TRUE : Event.BLOQUEADO_FALSE);

                            db.inserirEvento(eventonew);

                            if (diaDe == calInicio.get(Calendar.DAY_OF_MONTH)) {
                                eventoPai = db.obterUltimoEvent();
                            }

                            diaDe++;
                        }
                        Toast.makeText(getContext(), "Evento " + etTitulo.getText() + " incluído!", Toast.LENGTH_LONG).show();
                    } else {

                        ArrayList<Event> eventosFilhos = db.obterEventFilho(altEvento);

                        Event eventonew = new Event();
                        eventonew.setName(etTitulo.getText().toString());
                        eventonew.setDescription(etDescricao.getText().toString());
                        eventonew.setLocation(etLocal.getText().toString());
                        eventonew.setStart(calInicio.getTimeInMillis());
                        eventonew.setEnd(calFim.getTimeInMillis());
                        eventonew.setData(altEvento.getData());
                        eventonew.setColor(eventonew.colorStrToInt(spinercores.getSelectedItem().toString()));
                        eventonew.setFinanceiro(toggleFin.isChecked() ? Event.FINANCEIRO_TRUE : Event.FINANCEIRO_FALSE);
                        eventonew.setEventId(altEvento.getEventId());
                        eventonew.setIdPai(altEvento.getIdPai());
                        eventonew.setBloqueado(cBbloqueado.isChecked() ? Event.BLOQUEADO_TRUE : Event.BLOQUEADO_FALSE);

                        db.atualizarEvento(eventonew);

                        for (Event evento : eventosFilhos) {
                            eventonew.setEventId(evento.getEventId());
                            eventonew.setData(evento.getData());
                            eventonew.setIdPai(evento.getIdPai());

                            if (evento.getData() > eventonew.getEnd()) {
                                db.removerEvento(eventonew);
                            } else {
                                db.atualizarEvento(eventonew);
                            }
                        }
                        Toast.makeText(getContext(), "Evento " + etTitulo.getText() + " atualizado!", Toast.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }
                    limparCampos();
                }
            }
        });

        return view;
    }

    private void limparCampos(){
        etTitulo.setText("");
        etTitulo.requestFocus();
        etDescricao.setText("");
        etLocal.setText("");
        toggleFin.setChecked(false);
        cBbloqueado.setChecked(false);
    }

    private void setImagemCor(int cor){
        switch (cor){
            case Event.COLOR_BLUE:
                ivcor.setImageResource(R.drawable.blue);
                break;
            case Event.COLOR_GREEN:
                ivcor.setImageResource(R.drawable.green);
                break;
            case Event.COLOR_PURPLE:
                ivcor.setImageResource(R.drawable.purple);
                break;
            case Event.COLOR_RED:
                ivcor.setImageResource(R.drawable.red);
                break;
            case Event.COLOR_ORANGE:
                ivcor.setImageResource(R.drawable.orange);
                break;
            default:
        }
    }

}
