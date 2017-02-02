package com.jfc.josecamilo.diretordotime.auxiliares.calendario;

/**
 * Created by jose.camilo on 24/11/2016.
 */
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Event {

    private int color;
    private String name;
    private String description;
    private String location;
    private long start;
    private long end;
    private long data;
    private long eventId;
    private long idPai;
    private int financeiro;
    private int bloqueado;



    public static final int DEFAULT_EVENT_ICON = 0;
    public static final int COLOR_RED = 1;
    public static final int COLOR_BLUE = 2;
    public static final int COLOR_ORANGE = 3;
    public static final int COLOR_PURPLE = 4;
    public static final int COLOR_GREEN = 5;
    public static final int COLOR_TOTAL = 5;

    public static final int FINANCEIRO_FALSE = 0;
    public static final int FINANCEIRO_TRUE = 1;

    public static final int BLOQUEADO_FALSE = 0;
    public static final int BLOQUEADO_TRUE = 1;

    public Event() {
    }

    public Event(long eventID, long startMills, long endMills){
        this.eventId = eventID;
        this.start = startMills;
        this.end = endMills;
    }

    public Event(String name, String description, String location, long start, long end, long data, int financeiro, int color) {
        this.color = color;
        this.name = name;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;
        this.data = data;
        this.financeiro = financeiro;
    }

    public int getColor(){
        return color;
    }

    public void setColor(int color){
        this.color = color;
    }

    /**
     * Get the event title
     *
     * @return title
     */
    public String getTitle(){
        return name;
    }

    /**
     * Get the event description
     *
     * @return description
     */
    public String getDescription(){
        return description;
    }


    public void setDescription(String description){
        this.description = description;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getLocation(){
        return location;
    }

    /**
     * Set the name of the event
     *
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    /**
     * Gets the event id in the database
     *
     * @return event database id
     */
    public long getEventId(){
        return eventId;
    }

    /**
     * Get the start date of the event
     *
     * @return start date
     */
    public String getStartDate(String dateFormat){
        DateFormat df = new SimpleDateFormat(dateFormat,new Locale("pt", "BR")/*Locale.getDefault()*/);
        String date = df.format(start);

        return date;
    }

    /**
     * Get the end date of the event
     *
     * @return end date
     */
    public String getEndDate(String dateFormat){
        DateFormat df = new SimpleDateFormat(dateFormat,Locale.getDefault());
        String date = df.format(end);

        return date;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int colorStrToInt(String cor){
        int ret;
        switch (cor) {
            case "azul":
                ret = 2;
                break;
            case "vermelho":
                ret = 1;
                break;
            case "verde":
                ret = 5;
                break;
            case "laranja":
                ret = 3;
                break;
            case "roxo":
                ret = 4;
                break;
            default:
                ret = 0;
                break;
        }
        return ret;
    }

    public String getDescColor(int color){
        String ret;
        switch (color){
            case Event.COLOR_BLUE:
                ret = "azul";
                break;
            case Event.COLOR_GREEN:
                ret = "verde";
                break;
            case Event.COLOR_PURPLE:
                ret = "roxo";
                break;
            case Event.COLOR_RED:
                ret = "vermelho";
                break;
            case Event.COLOR_ORANGE:
                ret = "laranja";
                break;
            default:
                ret = "nenhuma";
        }
        return ret;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public int getFinanceiro() {
        return financeiro;
    }

    public void setFinanceiro(int financeiro) {
        this.financeiro = financeiro;
    }

    public long getIdPai() {
        return idPai;
    }

    public void setIdPai(long idPai) {
        this.idPai = idPai;
    }

    public int getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(int bloqueado) {
        this.bloqueado = bloqueado;
    }
}
