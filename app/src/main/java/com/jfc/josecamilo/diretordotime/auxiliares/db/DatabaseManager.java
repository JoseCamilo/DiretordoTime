package com.jfc.josecamilo.diretordotime.auxiliares.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jfc.josecamilo.diretordotime.auxiliares.calendario.Event;
import com.jfc.josecamilo.diretordotime.jogadores.Jogador;
import com.jfc.josecamilo.diretordotime.pagamentos.Pagamento;
import com.jfc.josecamilo.diretordotime.presenca.Presenca;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by jose.camilo on 12/12/2016.
 */
public class DatabaseManager {

    protected SQLiteDatabase db;
    private static DatabaseManager instance;
    private static DatabaseHelper mDatabaseHelper;
    private int mOpenCounter;

    private ArrayList<Event> eventosMes;

    //dados da tabela events
    private static final String EVENTS_TABLE = "events";
    public static final String EVENT = "event";
    public static final String LOCATION = "location";
    public static final String DESCRIPTION = "description";
    public static final String START = "start";
    public static final String END = "end";
    public static final String DATA = "data";
    public static final String ID = "_id";
    public static final String ID_PAI = "id_pai";
    public static final String COLOR = "color";
    public static final String FINANCEIRO = "financeiro";
    public static final String BLOQUEADO = "bloqueado";

    //TABELA JOGADORES
    private static final String JOGADORES_TABLE = "jogadores";
    public static final String NOME_JOG = "nome";
    public static final String NASC_JOG = "nasc";
    public static final String POSICAO_JOG = "posicao";
    public static final String RG_JOG = "rg";
    public static final String CPF_JOG = "cpf";
    public static final String ID_JOG = "_id";
    public static final String DIA_COBRAR_JOG = "dia_cobrar";
    //public static final String BLOQUEADO = "bloqueado";

    //TABELA PAGAMENTOS
    private static final String PAGAMENTOS_TABLE = "pagamentos";
    public static final String ID_PAG = "_id";
    public static final String ID_JOG_PAG = "id_jog";
    public static final String ID_EVENT_PAG = "id_event";
    public static final String STATUS_PAG = "status";
    public static final String VALOR_PAG = "valor";

    //TABELA PRESENCA
    private static final String PRESENCA_TABLE = "presenca";
    public static final String ID_PRE = "_id";
    public static final String ID_JOG_PRE = "id_jog";
    public static final String ID_EVENT_PRE = "id_event";
    public static final String STATUS_PRE = "status";

    public static synchronized void initializeInstance(DatabaseHelper helper) {
        if (instance == null) {
            instance = new DatabaseManager();
            mDatabaseHelper = helper;
        }
    }
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException
                    ("BD não iniciada, execute initializeInstance(..) primeiro."); }
        return instance;
    }

    public void openDB() {
        mOpenCounter++;
        if (mOpenCounter == 1) {
// Abrindo uma nova conexão caso não esteja aberta ainda
            db = mDatabaseHelper.getWritableDatabase();
        }
    }
    public void closeDB() {
        mOpenCounter--;
        if (mOpenCounter == 0) {
// Fechando a conexão caso não haja mais conexões sendo utilizadas
            db.close();
        }
    }
    protected void close(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }


    //**********************************EVENTOS*******************************************

    public ArrayList<Event> obterEventosAll() {
        ArrayList<Event> res = new ArrayList<Event>();
        openDB();
        String selectQuery = "SELECT " + ID + " , " + EVENT + " , " + LOCATION + " , " + DESCRIPTION +
                " , " + START + " , " + END + " , " + DATA + " , " + COLOR + " , " + FINANCEIRO + " , " + ID_PAI +
                " , " + BLOQUEADO +
                " FROM "+EVENTS_TABLE;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Event evento = new Event();
                evento.setEventId(c.getLong(c.getColumnIndex(ID)));
                evento.setName(c.getString(c.getColumnIndex(EVENT)));
                evento.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                evento.setLocation(c.getString(c.getColumnIndex(LOCATION)));
                evento.setStart(c.getLong(c.getColumnIndex(START)));
                evento.setEnd(c.getLong(c.getColumnIndex(END)));
                evento.setData(c.getLong(c.getColumnIndex(DATA)));
                evento.setColor(c.getInt(c.getColumnIndex(COLOR)));
                evento.setFinanceiro(c.getInt(c.getColumnIndex(FINANCEIRO)));
                evento.setIdPai(c.getLong(c.getColumnIndex(ID_PAI)));
                evento.setBloqueado(c.getInt(c.getColumnIndex(BLOQUEADO)));

                res.add(evento);
            } while (c.moveToNext());
        }
        close(c);
        closeDB();
        return res;
    }

    public ArrayList<Event> obterEventosNext(long longdata) {
        ArrayList<Event> res = new ArrayList<Event>();
        openDB();
        String selectQuery = "SELECT " + ID + " , " + EVENT + " , " + LOCATION + " , " + DESCRIPTION +
                " , " + START + " , " + END + " , " + DATA + " , " + COLOR + " , " + FINANCEIRO + " FROM "+EVENTS_TABLE +
                " WHERE " + ID_PAI + " = 0" +
                " AND " + BLOQUEADO + " = " + Event.BLOQUEADO_FALSE +
                " AND (" + START + " >= " + longdata +
                " OR " + END + " >= " + longdata + " ) " +
                " ORDER BY " + START;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Event evento = new Event();
                evento.setEventId(c.getLong(c.getColumnIndex(ID)));
                evento.setName(c.getString(c.getColumnIndex(EVENT)));
                evento.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                evento.setLocation(c.getString(c.getColumnIndex(LOCATION)));
                evento.setStart(c.getLong(c.getColumnIndex(START)));
                evento.setEnd(c.getLong(c.getColumnIndex(END)));
                evento.setData(c.getLong(c.getColumnIndex(DATA)));
                evento.setColor(c.getInt(c.getColumnIndex(COLOR)));
                evento.setFinanceiro(c.getInt(c.getColumnIndex(FINANCEIRO)));

                res.add(evento);
            } while (c.moveToNext());
        }
        close(c);
        closeDB();
        return res;
    }

    public ArrayList<Event> obterEventOnFin() {
        ArrayList<Event> res = new ArrayList<Event>();
        openDB();
        String selectQuery = "SELECT " + ID + " , " + EVENT + " , " + LOCATION + " , " + DESCRIPTION +
                " , " + START + " , " + END + " , " + DATA + " , " + COLOR + " , " + FINANCEIRO + " FROM "+EVENTS_TABLE +
                " WHERE " + FINANCEIRO + " = " + Event.FINANCEIRO_TRUE +
                " AND " + ID_PAI + " = 0" +
                " AND " + BLOQUEADO + " = " + Event.BLOQUEADO_FALSE +
                " ORDER BY " + DATA;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Event evento = new Event();
                evento.setEventId(c.getLong(c.getColumnIndex(ID)));
                evento.setName(c.getString(c.getColumnIndex(EVENT)));
                evento.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                evento.setLocation(c.getString(c.getColumnIndex(LOCATION)));
                evento.setStart(c.getLong(c.getColumnIndex(START)));
                evento.setEnd(c.getLong(c.getColumnIndex(END)));
                evento.setData(c.getLong(c.getColumnIndex(DATA)));
                evento.setColor(c.getInt(c.getColumnIndex(COLOR)));
                evento.setFinanceiro(c.getInt(c.getColumnIndex(FINANCEIRO)));

                res.add(evento);
            } while (c.moveToNext());
        }
        close(c);
        closeDB();
        return res;
    }

    public ArrayList<Event> obterEventOffFin() {
        ArrayList<Event> res = new ArrayList<Event>();
        openDB();
        String selectQuery = "SELECT " + ID + " , " + EVENT + " , " + LOCATION + " , " + DESCRIPTION +
                " , " + START + " , " + END + " , " + DATA + " , " + COLOR + " , " + FINANCEIRO + " FROM "+EVENTS_TABLE +
                " WHERE " + FINANCEIRO + " = " + Event.FINANCEIRO_FALSE +
                " AND " + ID_PAI +" = 0" +
                " AND " + BLOQUEADO + " = " + Event.BLOQUEADO_FALSE +
                " ORDER BY " + DATA;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Event evento = new Event();
                evento.setEventId(c.getLong(c.getColumnIndex(ID)));
                evento.setName(c.getString(c.getColumnIndex(EVENT)));
                evento.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                evento.setLocation(c.getString(c.getColumnIndex(LOCATION)));
                evento.setStart(c.getLong(c.getColumnIndex(START)));
                evento.setEnd(c.getLong(c.getColumnIndex(END)));
                evento.setData(c.getLong(c.getColumnIndex(DATA)));
                evento.setColor(c.getInt(c.getColumnIndex(COLOR)));
                evento.setFinanceiro(c.getInt(c.getColumnIndex(FINANCEIRO)));

                res.add(evento);
            } while (c.moveToNext());
        }
        close(c);
        closeDB();
        return res;
    }

    public ArrayList<Event> obterEventFilho(Event eventoPai) {
        ArrayList<Event> res = new ArrayList<Event>();
        openDB();
        String selectQuery = "SELECT " + ID + " , " + EVENT + " , " + LOCATION + " , " + DESCRIPTION +
                " , " + START + " , " + END + " , " + DATA + " , " + COLOR + " , " + FINANCEIRO + " FROM "+EVENTS_TABLE +
                " WHERE " + ID_PAI + " = " + eventoPai.getEventId();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Event evento = new Event();
                evento.setEventId(c.getLong(c.getColumnIndex(ID)));
                evento.setName(c.getString(c.getColumnIndex(EVENT)));
                evento.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                evento.setLocation(c.getString(c.getColumnIndex(LOCATION)));
                evento.setStart(c.getLong(c.getColumnIndex(START)));
                evento.setEnd(c.getLong(c.getColumnIndex(END)));
                evento.setData(c.getLong(c.getColumnIndex(DATA)));
                evento.setColor(c.getInt(c.getColumnIndex(COLOR)));
                evento.setFinanceiro(c.getInt(c.getColumnIndex(FINANCEIRO)));

                res.add(evento);
            } while (c.moveToNext());
        }
        close(c);
        closeDB();
        return res;
    }

    public Event obterEvent(long idEvento) {
        Event res = new Event();
        openDB();
        String selectQuery = "SELECT " + ID + " , " + EVENT + " , " + LOCATION + " , " + DESCRIPTION +
                " , " + START + " , " + END + " , " + DATA + " , " + COLOR + " , " + FINANCEIRO +
                " , " + ID_PAI + " , " + BLOQUEADO +
                " FROM "+EVENTS_TABLE +
                " WHERE " + ID + " = " + idEvento;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            res.setEventId(c.getLong(c.getColumnIndex(ID)));
            res.setName(c.getString(c.getColumnIndex(EVENT)));
            res.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
            res.setLocation(c.getString(c.getColumnIndex(LOCATION)));
            res.setStart(c.getLong(c.getColumnIndex(START)));
            res.setEnd(c.getLong(c.getColumnIndex(END)));
            res.setData(c.getLong(c.getColumnIndex(DATA)));
            res.setColor(c.getInt(c.getColumnIndex(COLOR)));
            res.setFinanceiro(c.getInt(c.getColumnIndex(FINANCEIRO)));
            res.setIdPai(c.getLong(c.getColumnIndex(ID_PAI)));
            res.setBloqueado(c.getInt(c.getColumnIndex(BLOQUEADO)));
        }
        close(c);
        closeDB();
        return res;
    }

    public Event obterUltimoEvent() {
        Event res = new Event();
        openDB();
        String selectQuery = "SELECT " + ID + " , " + EVENT + " , " + LOCATION + " , " + DESCRIPTION +
                " , " + START + " , " + END + " , " + DATA + " , " + COLOR + " , " + FINANCEIRO + " , " + ID_PAI + " , " + BLOQUEADO +
                " FROM "+EVENTS_TABLE +
                " WHERE " + ID + " = " +
                "(SELECT MAX("+ID+") FROM "+EVENTS_TABLE+")";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            res.setEventId(c.getLong(c.getColumnIndex(ID)));
            res.setName(c.getString(c.getColumnIndex(EVENT)));
            res.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
            res.setLocation(c.getString(c.getColumnIndex(LOCATION)));
            res.setStart(c.getLong(c.getColumnIndex(START)));
            res.setEnd(c.getLong(c.getColumnIndex(END)));
            res.setData(c.getLong(c.getColumnIndex(DATA)));
            res.setColor(c.getInt(c.getColumnIndex(COLOR)));
            res.setFinanceiro(c.getInt(c.getColumnIndex(FINANCEIRO)));
            res.setIdPai(c.getLong(c.getColumnIndex(ID_PAI)));
            res.setBloqueado(c.getInt(c.getColumnIndex(BLOQUEADO)));
        }
        close(c);
        closeDB();
        return res;
    }

    public ArrayList<Event> obterEventosDay(Long day) {
        ArrayList<Event> res = new ArrayList<Event>();
        openDB();

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTimeInMillis(day);
        newCalendar.set(newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH),00,00,00);
        Long periodoDe = newCalendar.getTimeInMillis();
        newCalendar.set(newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH),23,59,59);
        Long periodoAte = newCalendar.getTimeInMillis();

        String selectQuery = "SELECT " + ID + " , " + EVENT + " , " + LOCATION + " , " + DESCRIPTION +
                " , " + START + " , " + END + " , " + DATA + " , " + COLOR + " , " + FINANCEIRO + " , " + ID_PAI + " , " + BLOQUEADO +
                " FROM "+EVENTS_TABLE +
                " WHERE "+DATA+" >=? AND "+DATA+" <=?";
        Cursor c = db.rawQuery(selectQuery, new String[]{periodoDe.toString(),periodoAte.toString()});
        if (c.moveToFirst()) {
            do {
                Event evento = new Event();
                evento.setEventId(c.getLong(c.getColumnIndex(ID)));
                evento.setName(c.getString(c.getColumnIndex(EVENT)));
                evento.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                evento.setLocation(c.getString(c.getColumnIndex(LOCATION)));
                evento.setStart(c.getLong(c.getColumnIndex(START)));
                evento.setEnd(c.getLong(c.getColumnIndex(END)));
                evento.setData(c.getLong(c.getColumnIndex(DATA)));
                evento.setColor(c.getInt(c.getColumnIndex(COLOR)));
                evento.setFinanceiro(c.getInt(c.getColumnIndex(FINANCEIRO)));
                evento.setIdPai(c.getLong(c.getColumnIndex(ID_PAI)));
                evento.setBloqueado(c.getInt(c.getColumnIndex(BLOQUEADO)));

                res.add(evento);
            } while (c.moveToNext());
        }
        close(c);
        closeDB();
        return res;
    }

    public ArrayList<Event> obterEventosDayOn(Long day) {
        ArrayList<Event> res = new ArrayList<Event>();
        openDB();

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTimeInMillis(day);
        newCalendar.set(newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH),00,00,00);
        Long periodoDe = newCalendar.getTimeInMillis();
        newCalendar.set(newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH),23,59,59);
        Long periodoAte = newCalendar.getTimeInMillis();

        String selectQuery = "SELECT " + ID + " , " + EVENT + " , " + LOCATION + " , " + DESCRIPTION +
                " , " + START + " , " + END + " , " + DATA + " , " + COLOR + " , " + FINANCEIRO + " , " + ID_PAI + " , " + BLOQUEADO +
                " FROM "+EVENTS_TABLE +
                " WHERE "+DATA+" >=? AND "+DATA+" <=?" +
                " AND " + BLOQUEADO + " = " + Event.BLOQUEADO_FALSE;
        Cursor c = db.rawQuery(selectQuery, new String[]{periodoDe.toString(),periodoAte.toString()});
        if (c.moveToFirst()) {
            do {
                Event evento = new Event();
                evento.setEventId(c.getLong(c.getColumnIndex(ID)));
                evento.setName(c.getString(c.getColumnIndex(EVENT)));
                evento.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                evento.setLocation(c.getString(c.getColumnIndex(LOCATION)));
                evento.setStart(c.getLong(c.getColumnIndex(START)));
                evento.setEnd(c.getLong(c.getColumnIndex(END)));
                evento.setData(c.getLong(c.getColumnIndex(DATA)));
                evento.setColor(c.getInt(c.getColumnIndex(COLOR)));
                evento.setFinanceiro(c.getInt(c.getColumnIndex(FINANCEIRO)));
                evento.setIdPai(c.getLong(c.getColumnIndex(ID_PAI)));
                evento.setBloqueado(c.getInt(c.getColumnIndex(BLOQUEADO)));

                res.add(evento);
            } while (c.moveToNext());
        }
        close(c);
        closeDB();
        return res;
    }

    public ArrayList<Event> obterEventosMes(int mes , int ano) {
        ArrayList<Event> res = new ArrayList<Event>();
        openDB();

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(ano,mes,1,00,00,00);
        Long periodoDe = newCalendar.getTimeInMillis();
        newCalendar.set(ano,mes,newCalendar.getActualMaximum(Calendar.DAY_OF_MONTH),23,59,59);
        Long periodoAte = newCalendar.getTimeInMillis();

        String selectQuery = "SELECT " + ID + " , " + EVENT + " , " + LOCATION + " , " + DESCRIPTION +
                " , " + START + " , " + END + " , " + DATA + " , " + COLOR + " , " + FINANCEIRO + " , " + ID_PAI + " , " + BLOQUEADO +
                " FROM "+EVENTS_TABLE +
                " WHERE "+DATA+" >=? AND "+DATA+" <=?";
        Cursor c = db.rawQuery(selectQuery, new String[]{periodoDe.toString(),periodoAte.toString()});
        if (c.moveToFirst()) {
            do {
                Event evento = new Event();
                evento.setEventId(c.getLong(c.getColumnIndex(ID)));
                evento.setName(c.getString(c.getColumnIndex(EVENT)));
                evento.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
                evento.setLocation(c.getString(c.getColumnIndex(LOCATION)));
                evento.setStart(c.getLong(c.getColumnIndex(START)));
                evento.setEnd(c.getLong(c.getColumnIndex(END)));
                evento.setData(c.getLong(c.getColumnIndex(DATA)));
                evento.setColor(c.getInt(c.getColumnIndex(COLOR)));
                evento.setFinanceiro(c.getInt(c.getColumnIndex(FINANCEIRO)));
                evento.setIdPai(c.getLong(c.getColumnIndex(ID_PAI)));

                res.add(evento);
            } while (c.moveToNext());
        }
        close(c);
        closeDB();
        return res;
    }

    public void inserirEvento(Event evento) {
        openDB();
        ContentValues values = new ContentValues();
        values.put(EVENT,evento.getTitle());
        values.put(DESCRIPTION,evento.getDescription());
        values.put(LOCATION,evento.getLocation());
        values.put(START,evento.getStart());
        values.put(END,evento.getEnd());
        values.put(DATA,evento.getData());
        values.put(COLOR,evento.getColor());
        values.put(FINANCEIRO,evento.getFinanceiro());
        values.put(ID_PAI,evento.getIdPai());
        values.put(BLOQUEADO,evento.getBloqueado());


// insere posicao na tabela
        db.insert(EVENTS_TABLE, null, values);
        closeDB();
    }

    public void atualizarEvento(Event evento) {
        openDB();
        String strSQL = "UPDATE "+EVENTS_TABLE+" SET "+EVENT+" = '" + evento.getTitle() + "'"
                + ", "+DESCRIPTION+" = '" + evento.getDescription() + "'"
                + ", "+LOCATION+" = '" + evento.getLocation() + "'"
                + ", "+START+" = " + evento.getStart()
                + ", "+END+" = " + evento.getEnd()
                + ", "+DATA+" = " + evento.getData()
                + ", "+COLOR+" = " + evento.getColor()
                + ", "+FINANCEIRO+" = " + evento.getFinanceiro()
                + ", "+BLOQUEADO+" = " + evento.getBloqueado()

                + " WHERE "+ID+" = " + evento.getEventId();
        db.execSQL(strSQL);
        closeDB();
    }
    public void removerEvento(Event evento) {
        openDB();
        String strSQL = "DELETE FROM "+EVENTS_TABLE+" WHERE "+ID+" = " + evento.getEventId();
        db.execSQL(strSQL);
        closeDB();
    }


    //**********************************JOGADORES*******************************************

    public void inserirJogador(Jogador jogador) {
        openDB();
        ContentValues values = new ContentValues();
        values.put(NOME_JOG,jogador.getNome());
        values.put(NASC_JOG,jogador.getNasc());
        values.put(POSICAO_JOG,jogador.getPosicao());
        values.put(RG_JOG,jogador.getRg());
        values.put(CPF_JOG,jogador.getCpf());
        values.put(DIA_COBRAR_JOG,jogador.getDia_cobrar());
        values.put(BLOQUEADO,jogador.getBloqueado());

        // insere posicao na tabela
        db.insert(JOGADORES_TABLE, null, values);
        closeDB();
    }

    public void removerJogador(Jogador jogador) {
        openDB();
        String strSQL = "DELETE FROM "+JOGADORES_TABLE+" WHERE "+ID_JOG+" = " + jogador.getId();
        db.execSQL(strSQL);
        closeDB();
    }

    public void atualizarJogador(Jogador jogador) {
        openDB();
        String strSQL = "UPDATE "+JOGADORES_TABLE+" SET "+NOME_JOG+" = '" + jogador.getNome() + "' "
                + ", "+NASC_JOG+" = " + jogador.getNasc()
                + ", "+RG_JOG+" = '" + jogador.getRg() + "' "
                + ", "+CPF_JOG+" = '" + jogador.getCpf() + "' "
                + ", "+POSICAO_JOG+" = " + jogador.getPosicao()
                + ", "+DIA_COBRAR_JOG+" = " + jogador.getDia_cobrar()
                + ", "+BLOQUEADO+" = " + jogador.getBloqueado()

                + " WHERE "+ID_JOG+" = " + jogador.getId();
        db.execSQL(strSQL);
        closeDB();
    }

    public Jogador obterJogador(long id) {
        Jogador res = new Jogador();
        openDB();
        String selectQuery = "SELECT " + ID_JOG + " , " + NOME_JOG + " , " + NASC_JOG + " , " + POSICAO_JOG +
                " , " + RG_JOG + " , " + CPF_JOG + " , " + DIA_COBRAR_JOG + " , " + BLOQUEADO +
                " FROM "+ JOGADORES_TABLE +
                " WHERE "+ID_JOG+" = "+id;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            res.setId(c.getLong(c.getColumnIndex(ID_JOG)));
            res.setNome(c.getString(c.getColumnIndex(NOME_JOG)));
            res.setNasc(c.getLong(c.getColumnIndex(NASC_JOG)));
            res.setPosicao(c.getInt(c.getColumnIndex(POSICAO_JOG)));
            res.setRg(c.getString(c.getColumnIndex(RG_JOG)));
            res.setCpf(c.getString(c.getColumnIndex(CPF_JOG)));
            res.setDia_cobrar(c.getInt(c.getColumnIndex(DIA_COBRAR_JOG)));
            res.setBloqueado(c.getInt(c.getColumnIndex(BLOQUEADO)));
        }
        close(c);
        closeDB();
        return res;
    }

    public Jogador obterJogador(String nome) {
        Jogador res = new Jogador();
        openDB();
        String selectQuery = "SELECT " + ID_JOG + " , " + NOME_JOG + " , " + NASC_JOG + " , " + POSICAO_JOG +
                " , " + RG_JOG + " , " + CPF_JOG + " , " + DIA_COBRAR_JOG + " , " + BLOQUEADO +
                " FROM "+ JOGADORES_TABLE +
                " WHERE "+NOME_JOG+" = '" + nome + "' ";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            res.setId(c.getLong(c.getColumnIndex(ID_JOG)));
            res.setNome(c.getString(c.getColumnIndex(NOME_JOG)));
            res.setNasc(c.getLong(c.getColumnIndex(NASC_JOG)));
            res.setPosicao(c.getInt(c.getColumnIndex(POSICAO_JOG)));
            res.setRg(c.getString(c.getColumnIndex(RG_JOG)));
            res.setCpf(c.getString(c.getColumnIndex(CPF_JOG)));
            res.setDia_cobrar(c.getInt(c.getColumnIndex(DIA_COBRAR_JOG)));
            res.setBloqueado(c.getInt(c.getColumnIndex(BLOQUEADO)));
        }
        close(c);
        closeDB();
        return res;
    }

    public ArrayList<Jogador> obterJogadoresAll() {
        ArrayList<Jogador> res = new ArrayList<Jogador>();
        openDB();
        String selectQuery = "SELECT " + ID_JOG + " , " + NOME_JOG + " , " + NASC_JOG + " , " + POSICAO_JOG +
                " , " + RG_JOG + " , " + CPF_JOG + " , " + DIA_COBRAR_JOG + " , " + BLOQUEADO +
                " FROM "+ JOGADORES_TABLE;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Jogador jogador = new Jogador();
                jogador.setId(c.getLong(c.getColumnIndex(ID_JOG)));
                jogador.setNome(c.getString(c.getColumnIndex(NOME_JOG)));
                jogador.setNasc(c.getLong(c.getColumnIndex(NASC_JOG)));
                jogador.setPosicao(c.getInt(c.getColumnIndex(POSICAO_JOG)));
                jogador.setRg(c.getString(c.getColumnIndex(RG_JOG)));
                jogador.setCpf(c.getString(c.getColumnIndex(CPF_JOG)));
                jogador.setDia_cobrar(c.getInt(c.getColumnIndex(DIA_COBRAR_JOG)));
                jogador.setBloqueado(c.getInt(c.getColumnIndex(BLOQUEADO)));

                res.add(jogador);
            } while (c.moveToNext());
        }
        close(c);
        closeDB();
        return res;
    }

    public ArrayList<Jogador> obterJogadoresAllOn() {
        ArrayList<Jogador> res = new ArrayList<Jogador>();
        openDB();
        String selectQuery = "SELECT " + ID_JOG + " , " + NOME_JOG + " , " + NASC_JOG + " , " + POSICAO_JOG +
                " , " + RG_JOG + " , " + CPF_JOG + " , " + DIA_COBRAR_JOG + " , " + BLOQUEADO +
                " FROM "+ JOGADORES_TABLE +
                " WHERE " + BLOQUEADO + " = " + Jogador.BLOQUEADO_FALSE;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Jogador jogador = new Jogador();
                jogador.setId(c.getLong(c.getColumnIndex(ID_JOG)));
                jogador.setNome(c.getString(c.getColumnIndex(NOME_JOG)));
                jogador.setNasc(c.getLong(c.getColumnIndex(NASC_JOG)));
                jogador.setPosicao(c.getInt(c.getColumnIndex(POSICAO_JOG)));
                jogador.setRg(c.getString(c.getColumnIndex(RG_JOG)));
                jogador.setCpf(c.getString(c.getColumnIndex(CPF_JOG)));
                jogador.setDia_cobrar(c.getInt(c.getColumnIndex(DIA_COBRAR_JOG)));
                jogador.setBloqueado(c.getInt(c.getColumnIndex(BLOQUEADO)));

                res.add(jogador);
            } while (c.moveToNext());
        }
        close(c);
        closeDB();
        return res;
    }

    public ArrayList<Jogador> obterJogadorLike(String nomeJog) {
        ArrayList<Jogador> res = new ArrayList<Jogador>();
        openDB();
        String selectQuery = "SELECT " + ID_JOG + " , " + NOME_JOG + " , " + NASC_JOG + " , " + POSICAO_JOG +
                " , " + RG_JOG + " , " + CPF_JOG + " , " + DIA_COBRAR_JOG + " , " + BLOQUEADO +
                " FROM "+ JOGADORES_TABLE +
                " WHERE "+NOME_JOG+" LIKE '%"+nomeJog+"%'";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Jogador jogador = new Jogador();
                jogador.setId(c.getLong(c.getColumnIndex(ID_JOG)));
                jogador.setNome(c.getString(c.getColumnIndex(NOME_JOG)));
                jogador.setNasc(c.getLong(c.getColumnIndex(NASC_JOG)));
                jogador.setPosicao(c.getInt(c.getColumnIndex(POSICAO_JOG)));
                jogador.setRg(c.getString(c.getColumnIndex(RG_JOG)));
                jogador.setCpf(c.getString(c.getColumnIndex(CPF_JOG)));
                jogador.setDia_cobrar(c.getInt(c.getColumnIndex(DIA_COBRAR_JOG)));
                jogador.setBloqueado(c.getInt(c.getColumnIndex(BLOQUEADO)));

                res.add(jogador);
            } while (c.moveToNext());
        }
        close(c);
        closeDB();
        return res;
    }

    public boolean verifyExistJogador(String name) {
        boolean res = false;
        openDB();
        String selectQuery = "SELECT " + ID_JOG + " , " + NOME_JOG + " , " + NASC_JOG + " , " + POSICAO_JOG +
                " , " + RG_JOG + " , " + CPF_JOG + " , " + DIA_COBRAR_JOG + " , " + BLOQUEADO +
                " FROM "+ JOGADORES_TABLE +
                " WHERE " + NOME_JOG + " = '" + name + "' ";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            res = true;
        }
        close(c);
        closeDB();
        return res;
    }


    //**********************************PAGAMENTOS*******************************************

    public void inserirPagamento(Pagamento pagamento) {
        openDB();
        ContentValues values = new ContentValues();
        values.put(ID_JOG_PAG,pagamento.getId_jog());
        values.put(ID_EVENT_PAG,pagamento.getId_event());
        values.put(STATUS_PAG,pagamento.getStatus());
        values.put(VALOR_PAG,pagamento.getValor());


        // insere posicao na tabela
        db.insert(PAGAMENTOS_TABLE, null, values);
        closeDB();
    }


    public void atualizarPagamento(Pagamento pagamento) {
        openDB();
        String strSQL = "UPDATE "+PAGAMENTOS_TABLE+" SET "+STATUS_PAG+" = " + pagamento.getStatus()
                + ", "+VALOR_PAG+" = " + pagamento.getValor()

                + " WHERE "+ID_PAG+" = " + pagamento.getId();


        db.execSQL(strSQL);
        closeDB();
    }

    public Pagamento obterPagamento(long id) {
        Pagamento res = new Pagamento();
        openDB();
        String selectQuery = "SELECT " + ID_PAG + " , " + ID_JOG_PAG + " , " + ID_EVENT_PAG + " , " + STATUS_PAG + " , " + VALOR_PAG +
                " FROM "+ PAGAMENTOS_TABLE +
                " WHERE "+ID_PAG+" = "+id;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            res.setId(c.getLong(c.getColumnIndex(ID_PAG)));
            res.setId_jog(c.getLong(c.getColumnIndex(ID_JOG_PAG)));
            res.setId_event(c.getLong(c.getColumnIndex(ID_EVENT_PAG)));
            res.setStatus(c.getInt(c.getColumnIndex(STATUS_PAG)));
            res.setValor(c.getFloat(c.getColumnIndex(VALOR_PAG)));
        }
        close(c);
        closeDB();
        return res;
    }

    public ArrayList<Pagamento> obterPagamentosEvent(Event evento) {
        ArrayList<Pagamento> res = new ArrayList<Pagamento>();
        openDB();
        String selectQuery = "SELECT " + ID_PAG + " , " + ID_JOG_PAG + " , " + ID_EVENT_PAG + " , " + STATUS_PAG + " , " + VALOR_PAG +
                " FROM "+ PAGAMENTOS_TABLE +
                " WHERE "+ID_EVENT_PAG+" = "+evento.getEventId();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {

            do {
                Pagamento pagamento = new Pagamento();
                pagamento.setId(c.getLong(c.getColumnIndex(ID_PAG)));
                pagamento.setId_jog(c.getLong(c.getColumnIndex(ID_JOG_PAG)));
                pagamento.setId_event(c.getLong(c.getColumnIndex(ID_EVENT_PAG)));
                pagamento.setStatus(c.getInt(c.getColumnIndex(STATUS_PAG)));
                pagamento.setValor(c.getFloat(c.getColumnIndex(VALOR_PAG)));

                res.add(pagamento);
            } while (c.moveToNext());

        }
        close(c);
        closeDB();
        return res;
    }

    public Pagamento obterPagamento(Event evento, Jogador jogador) {
        Pagamento res = new Pagamento();
        openDB();
        String selectQuery = "SELECT " + ID_PAG + " , " + ID_JOG_PAG + " , " + ID_EVENT_PAG + " , " + STATUS_PAG + " , " + VALOR_PAG +
                " FROM "+ PAGAMENTOS_TABLE +
                " WHERE "+ID_EVENT_PAG+" = "+evento.getEventId() +
                " AND "+ID_JOG_PAG+" = "+jogador.getId();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {

            res.setId(c.getLong(c.getColumnIndex(ID_PAG)));
            res.setId_jog(c.getLong(c.getColumnIndex(ID_JOG_PAG)));
            res.setId_event(c.getLong(c.getColumnIndex(ID_EVENT_PAG)));
            res.setStatus(c.getInt(c.getColumnIndex(STATUS_PAG)));
            res.setValor(c.getFloat(c.getColumnIndex(VALOR_PAG)));

        }
        close(c);
        closeDB();
        return res;
    }

    //**********************************PRESENCA*******************************************

    public void inserirPresenca(Presenca presenca) {
        openDB();
        ContentValues values = new ContentValues();
        values.put(ID_JOG_PRE,presenca.getId_jog());
        values.put(ID_EVENT_PRE,presenca.getId_event());
        values.put(STATUS_PRE,presenca.getStatus());


        // insere posicao na tabela
        db.insert(PRESENCA_TABLE, null, values);
        closeDB();
    }


    public void atualizarPresenca(Presenca presenca) {
        openDB();
        String strSQL = "UPDATE "+PRESENCA_TABLE+" SET "+STATUS_PRE+" = '" + presenca.getStatus() + "' "

                + " WHERE "+ID_PRE+" = " + presenca.getId();


        db.execSQL(strSQL);
        closeDB();
    }

    public Presenca obterPresenca(long id) {
        Presenca res = new Presenca();
        openDB();
        String selectQuery = "SELECT " + ID_PRE + " , " + ID_JOG_PRE + " , " + ID_EVENT_PRE + " , " + STATUS_PRE +
                " FROM "+ PRESENCA_TABLE +
                " WHERE "+ID_PRE+" = "+id;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            res.setId(c.getLong(c.getColumnIndex(ID_PRE)));
            res.setId_jog(c.getLong(c.getColumnIndex(ID_JOG_PRE)));
            res.setId_event(c.getLong(c.getColumnIndex(ID_EVENT_PRE)));
            res.setStatus(c.getInt(c.getColumnIndex(STATUS_PRE)));
        }
        close(c);
        closeDB();
        return res;
    }

    public ArrayList<Presenca> obterPresencaEvent(Event evento) {
        ArrayList<Presenca> res = new ArrayList<Presenca>();
        openDB();
        String selectQuery = "SELECT " + ID_PRE + " , " + ID_JOG_PRE + " , " + ID_EVENT_PRE + " , " + STATUS_PRE +
                " FROM "+ PRESENCA_TABLE +
                " WHERE "+ID_EVENT_PRE+" = "+evento.getEventId();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {

            do {
                Presenca presenca = new Presenca();
                presenca.setId(c.getLong(c.getColumnIndex(ID_PRE)));
                presenca.setId_jog(c.getLong(c.getColumnIndex(ID_JOG_PRE)));
                presenca.setId_event(c.getLong(c.getColumnIndex(ID_EVENT_PRE)));
                presenca.setStatus(c.getInt(c.getColumnIndex(STATUS_PRE)));

                res.add(presenca);
            } while (c.moveToNext());

        }
        close(c);
        closeDB();
        return res;
    }

    public Presenca obterPresenca(Event evento, Jogador jogador) {
        Presenca res = new Presenca();
        openDB();
        String selectQuery = "SELECT " + ID_PRE + " , " + ID_JOG_PRE + " , " + ID_EVENT_PRE + " , " + STATUS_PRE +
                " FROM "+ PRESENCA_TABLE +
                " WHERE "+ID_EVENT_PRE+" = "+evento.getEventId() +
                " AND "+ID_JOG_PRE+" = "+jogador.getId();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {

            res.setId(c.getLong(c.getColumnIndex(ID_PRE)));
            res.setId_jog(c.getLong(c.getColumnIndex(ID_JOG_PRE)));
            res.setId_event(c.getLong(c.getColumnIndex(ID_EVENT_PRE)));
            res.setStatus(c.getInt(c.getColumnIndex(STATUS_PRE)));

        }
        close(c);
        closeDB();
        return res;
    }
}


