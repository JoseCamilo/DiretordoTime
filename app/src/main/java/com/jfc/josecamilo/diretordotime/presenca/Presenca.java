package com.jfc.josecamilo.diretordotime.presenca;

/**
 * Created by jose.camilo on 21/12/2016.
 */
public class Presenca {

    private long id;
    private long id_jog;
    private long id_event;
    private int status;

    public static final int STATUS_NAOVAI = 0;
    public static final int STATUS_CONFIRMADO = 1;

    public Presenca() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_jog() {
        return id_jog;
    }

    public void setId_jog(long id_jog) {
        this.id_jog = id_jog;
    }

    public long getId_event() {
        return id_event;
    }

    public void setId_event(long id_event) {
        this.id_event = id_event;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}


