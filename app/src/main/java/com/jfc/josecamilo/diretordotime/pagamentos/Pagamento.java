package com.jfc.josecamilo.diretordotime.pagamentos;

/**
 * Created by jose.camilo on 21/12/2016.
 */
public class Pagamento {

    private long id;
    private long id_jog;
    private long id_event;
    private int status;
    private float valor;

    public static final int STATUS_INADIMPLENTE = 0;
    public static final int STATUS_ADIMPLENTE = 1;

    public Pagamento() {
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

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
}