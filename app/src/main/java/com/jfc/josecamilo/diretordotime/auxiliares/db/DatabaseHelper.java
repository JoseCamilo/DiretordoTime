package com.jfc.josecamilo.diretordotime.auxiliares.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jose.camilo on 12/12/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;

    private static final String DATABASE_NAME = "diretordotime.db";
    private static final int DATABASE_VERSION = 16;

    //TABELA EVENTS
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


    public DatabaseHelper(Context context) {
/* O primeiro argumento é o contexto da aplicacao. O segundo argumento é
* o nome do banco de dados. O terceiro é um pondeiro para manipulação de
* dados, não precisaremos dele. O quarto é a versão do banco de dados */
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        createTables(db);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + JOGADORES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PAGAMENTOS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PRESENCA_TABLE);
        onCreate(db);
    }

    private void createTables(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + EVENTS_TABLE + "(" + ID + " integer primary key autoincrement, " +
                ID_PAI + " INTEGER, " + EVENT + " TEXT, " + LOCATION + " TEXT, " + DESCRIPTION + " TEXT, "
                + START + " REAL, "+ END + " REAL, "+ DATA + " REAL, " + COLOR + " INTEGER, " + FINANCEIRO + " INTEGER, "
                + BLOQUEADO + " INTEGER);");

        db.execSQL("CREATE TABLE " + JOGADORES_TABLE + "(" + ID_JOG + " integer primary key autoincrement, " +
                NOME_JOG + " TEXT, " + NASC_JOG + " REAL, " + POSICAO_JOG + " INTEGER, "
                + RG_JOG + " TEXT, "+ CPF_JOG + " TEXT, "+ DIA_COBRAR_JOG + " INTEGER, " + BLOQUEADO + " INTEGER);");

        db.execSQL("CREATE TABLE " + PAGAMENTOS_TABLE + "(" + ID_PAG + " integer primary key autoincrement, " +
                ID_EVENT_PAG + " REAL, " + ID_JOG_PAG + " REAL, " + STATUS_PAG + " INTEGER, " + VALOR_PAG + " REAL);");

        db.execSQL("CREATE TABLE " + PRESENCA_TABLE + "(" + ID_PRE + " integer primary key autoincrement, " +
                ID_EVENT_PRE + " REAL, " + ID_JOG_PRE + " REAL, " + STATUS_PRE + " INTEGER);");
    }

}