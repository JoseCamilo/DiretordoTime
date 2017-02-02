package com.jfc.josecamilo.diretordotime.jogadores;


import android.app.DatePickerDialog;
import android.app.Service;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextWatcher;
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
import android.widget.Toast;

import com.jfc.josecamilo.diretordotime.R;
import com.jfc.josecamilo.diretordotime.auxiliares.Mask;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseHelper;
import com.jfc.josecamilo.diretordotime.auxiliares.db.DatabaseManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncJogadorFragment extends Fragment{

    private View view;
    private Button nascJog;
    private EditText nomeJog;
    private EditText rgJog;
    private TextWatcher rgMask;
    private EditText cpfJog;
    private TextWatcher cpfMask;
    /*private EditText diaCobrarJog;*/
    private Spinner spinnerPosicao;
    private ImageView imagemPosicao;
    private CheckBox cBbloqueado;
    private Button btnSalvar;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Calendar nascCalendar = Calendar.getInstance();

    private Jogador jogadorAlt;

    private DatabaseManager db;

    public IncJogadorFragment() {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_jogadores_inc, container, false);

        if (db == null) {
            DatabaseManager.initializeInstance(new DatabaseHelper(getContext()));
            db = DatabaseManager.getInstance();
        }

        /*NOME DO JOGADOR*/
        nomeJog = (EditText) view.findViewById(R.id.etnomeJog);

        /*DATA DE NASCIMENTO*/
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
        nascJog = (Button) view.findViewById(R.id.nascJog);
        nascJog.setText(dateFormatter.format(nascCalendar.getTime()));
        nascJog.setInputType( InputType.TYPE_NULL );
        nascJog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });
        fromDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                nascCalendar.set(year, monthOfYear, dayOfMonth);
                nascJog.setText(dateFormatter.format(nascCalendar.getTime()));
            }

        },nascCalendar.get(Calendar.YEAR), nascCalendar.get(Calendar.MONTH), nascCalendar.get(Calendar.DAY_OF_MONTH));

        /*RG*/
        rgJog = (EditText) view.findViewById(R.id.etRGJog);
        rgMask = Mask.insert("##.###.###-#",rgJog);
        rgJog.addTextChangedListener(rgMask);

        /*CPF*/
        cpfJog = (EditText) view.findViewById(R.id.etCPFJog);
        cpfMask = Mask.insert("###.###.###-##",cpfJog);
        cpfJog.addTextChangedListener(cpfMask);

        /*POSICAO*/
        List<String> listaPosicao = new ArrayList<String>();
        for (int nx=0;nx <= Jogador.POSICAO_TOTAL;nx++){
            listaPosicao.add(new Jogador().getDescPosicao(nx));
        }

        spinnerPosicao = (Spinner) view.findViewById(R.id.spinerPosicao);
        imagemPosicao = (ImageView) view.findViewById(R.id.ivposicao);

        ArrayAdapter<String> spineradapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, listaPosicao );
        spineradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPosicao.setAdapter(spineradapter);
        spinnerPosicao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                setImagemPosicao(spinnerPosicao.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*DIA DE COBRAR*/
       /* diaCobrarJog = (EditText) view.findViewById(R.id.etDiaCobrarJog);
        diaCobrarJog.setText("0");*/

        /*BLOQUEADO*/
        cBbloqueado = (CheckBox) view.findViewById(R.id.jogadorbloqueado);

        /*PEGA DADOS DA TELA DE PESQUISA JOGADORES*/
        Bundle bundle = this.getArguments();
        if (bundle != null){
            long idJogAlt = bundle.getLong("idJog");

            if (db == null) {
                DatabaseManager.initializeInstance(new DatabaseHelper(getContext()));
                db = DatabaseManager.getInstance();
            }

            jogadorAlt = db.obterJogador(idJogAlt);

            nomeJog.setText(jogadorAlt.getNome());
            nascCalendar.setTimeInMillis(jogadorAlt.getNasc());
            nascJog.setText(dateFormatter.format(nascCalendar.getTime()));
            rgJog.setText(jogadorAlt.getRg());
            cpfJog.setText(jogadorAlt.getCpf());
            /*diaCobrarJog.setText(String.valueOf(jogadorAlt.getDia_cobrar()));*/
            spinnerPosicao.setSelection(jogadorAlt.getPosicao());
            setImagemPosicao(jogadorAlt.getDescPosicao());
            cBbloqueado.setChecked(jogadorAlt.getBloqueado() == Jogador.BLOQUEADO_TRUE);

        }

        /*SALVAR*/
        btnSalvar = (Button) view.findViewById(R.id.btnsalvarJog);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nomeJog.getText().toString().isEmpty()) {
                    nomeJog.requestFocus();
                    nomeJog.setError("O nome é obrigatório");
                }else if (jogadorAlt == null && db.verifyExistJogador(nomeJog.getText().toString())){
                    nomeJog.requestFocus();
                    nomeJog.setError("Este jogador já existe");
                }else{
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                    if (imm != null){
                        imm.hideSoftInputFromWindow(nomeJog.getWindowToken(),0);
                    }

                    Jogador jogadornew = new Jogador();
                    jogadornew.setNome(nomeJog.getText().toString());
                    jogadornew.setNasc(nascCalendar.getTimeInMillis());
                    jogadornew.setRg(rgJog.getText().toString());
                    jogadornew.setCpf(cpfJog.getText().toString());
                    /*jogadornew.setDia_cobrar(Integer.parseInt(diaCobrarJog.getText().toString().isEmpty() ? "0" : diaCobrarJog.getText().toString()));*/
                    jogadornew.setPosicao(jogadornew.getPosicaoStrToInt(spinnerPosicao.getSelectedItem().toString()));
                    jogadornew.setBloqueado(cBbloqueado.isChecked() ? Jogador.BLOQUEADO_TRUE : Jogador.BLOQUEADO_FALSE);

                    if (jogadorAlt == null) {

                        db.inserirJogador(jogadornew);

                        Toast.makeText(getContext(), "O membro da equipe " + jogadornew.getNome() + " foi incluído!", Toast.LENGTH_LONG).show();
                    }else{

                        jogadornew.setId(jogadorAlt.getId());

                        db.atualizarJogador(jogadornew);

                        Toast.makeText(getContext(), "O membro da equipe " + jogadornew.getNome() + " foi atualizado!", Toast.LENGTH_LONG).show();

                        getActivity().onBackPressed();
                    }
                    limparCampos();
                }

            }
        });

        return view;
    }

    private void limparCampos(){
        nomeJog.setText("");
        nomeJog.requestFocus();
        rgJog.setText("");
        cpfJog.setText("");
        /*diaCobrarJog.setText("0");*/
        cBbloqueado.setChecked(false);
    }

    private void setImagemPosicao(String posicao){
        switch (posicao){
            case "Goleiro":
                imagemPosicao.setImageResource(R.drawable.football_sports_gloves);
                break;
            case "Fixo":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Ala-Direita":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Ala-Esquerda":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Pivo":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Tecnico":
                imagemPosicao.setImageResource(R.drawable.football_leader_man);
                break;
            case "Zagueiro":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Zagueiro-Central":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Quarto-Zagueiro":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Lateral-Direito":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Lateral-Esquerdo":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Volante":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Meio-Campo":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Meia-Direita":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Meia-Esquerda":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Meia-Atacante":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Atacante":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Centroavante":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Ponta-Direita":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            case "Ponta-Esquerda":
                imagemPosicao.setImageResource(R.drawable.football_shoes);
                break;
            default:
        }
    }
}
