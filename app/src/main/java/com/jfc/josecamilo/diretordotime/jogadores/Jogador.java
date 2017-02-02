package com.jfc.josecamilo.diretordotime.jogadores;

/**
 * Created by jose.camilo on 19/12/2016.
 */
public class Jogador {
    private String nome;
    private long nasc;
    private int posicao;
    private String rg;
    private String cpf;
    private int dia_cobrar;
    private long id;
    private int bloqueado;

    public static final int POSICAO_GOLEIRO = 0;
    public static final int POSICAO_FIXO = 1;
    public static final int POSICAO_ALA_D = 2;
    public static final int POSICAO_ALA_E = 3;
    public static final int POSICAO_PIVO = 4;
    public static final int POSICAO_TECNICO = 5;
    public static final int POSICAO_ZAGUEIRO = 6;
    public static final int POSICAO_ZAGUEIRO_CENTRAL = 7;
    public static final int POSICAO_ZAGUEIRO_QUARTO = 8;
    public static final int POSICAO_LATERAL_D = 9;
    public static final int POSICAO_LATERAL_E = 10;
    public static final int POSICAO_VOLANTE = 11;
    public static final int POSICAO_MEIA = 12;
    public static final int POSICAO_MEIA_D = 13;
    public static final int POSICAO_MEIA_E = 14;
    public static final int POSICAO_MEIA_ATACANTE = 15;
    public static final int POSICAO_ATACANTE = 16;
    public static final int POSICAO_CENTROAVANTE = 17;
    public static final int POSICAO_PONTA_D = 18;
    public static final int POSICAO_PONTA_E = 19;
    public static final int POSICAO_TOTAL = 19;

    public static final int BLOQUEADO_FALSE = 0;
    public static final int BLOQUEADO_TRUE = 1;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getNasc() {
        return nasc;
    }

    public void setNasc(long nasc) {
        this.nasc = nasc;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getDia_cobrar() {
        return dia_cobrar;
    }

    public void setDia_cobrar(int dia_cobrar) {
        this.dia_cobrar = dia_cobrar;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescPosicao(int posicao){
        String ret;
        switch (posicao) {
            case Jogador.POSICAO_GOLEIRO:
                ret = "Goleiro";
                break;
            case Jogador.POSICAO_FIXO:
                ret = "Fixo";
                break;
            case Jogador.POSICAO_ALA_D:
                ret = "Ala-Direita";
                break;
            case Jogador.POSICAO_ALA_E:
                ret = "Ala-Esquerda";
                break;
            case Jogador.POSICAO_PIVO:
                ret = "Pivo";
                break;
            case Jogador.POSICAO_TECNICO:
                ret = "Tecnico";
                break;
            case Jogador.POSICAO_ZAGUEIRO:
                ret = "Zagueiro";
                break;
            case Jogador.POSICAO_ZAGUEIRO_CENTRAL:
                ret = "Zagueiro-Central";
                break;
            case Jogador.POSICAO_ZAGUEIRO_QUARTO:
                ret = "Quarto-Zagueiro";
                break;
            case Jogador.POSICAO_LATERAL_D:
                ret = "Lateral-Direito";
                break;
            case Jogador.POSICAO_LATERAL_E:
                ret = "Lateral-Esquerdo";
                break;
            case Jogador.POSICAO_VOLANTE:
                ret = "Volante";
                break;
            case Jogador.POSICAO_MEIA:
                ret = "Meio-Campo";
                break;
            case Jogador.POSICAO_MEIA_D:
                ret = "Meia-Direita";
                break;
            case Jogador.POSICAO_MEIA_E:
                ret = "Meia-Esquerda";
                break;
            case Jogador.POSICAO_MEIA_ATACANTE:
                ret = "Meia-Atacante";
                break;
            case Jogador.POSICAO_ATACANTE:
                ret = "Atacante";
                break;
            case Jogador.POSICAO_CENTROAVANTE:
                ret = "Centroavante";
                break;
            case Jogador.POSICAO_PONTA_D:
                ret = "Ponta-Direita";
                break;
            case Jogador.POSICAO_PONTA_E:
                ret = "Ponta-Esquerda";
                break;
            default:
                ret = "nada";
                break;
        }
        return ret;
    }

    public String getDescPosicao(){
        String ret;
        switch (this.posicao) {
            case Jogador.POSICAO_GOLEIRO:
                ret = "Goleiro";
                break;
            case Jogador.POSICAO_FIXO:
                ret = "Fixo";
                break;
            case Jogador.POSICAO_ALA_D:
                ret = "Ala-Direita";
                break;
            case Jogador.POSICAO_ALA_E:
                ret = "Ala-Esquerda";
                break;
            case Jogador.POSICAO_PIVO:
                ret = "Pivo";
                break;
            case Jogador.POSICAO_TECNICO:
                ret = "Tecnico";
                break;
            case Jogador.POSICAO_ZAGUEIRO:
                ret = "Zagueiro";
                break;
            case Jogador.POSICAO_ZAGUEIRO_CENTRAL:
                ret = "Zagueiro-Central";
                break;
            case Jogador.POSICAO_ZAGUEIRO_QUARTO:
                ret = "Quarto-Zagueiro";
                break;
            case Jogador.POSICAO_LATERAL_D:
                ret = "Lateral-Direito";
                break;
            case Jogador.POSICAO_LATERAL_E:
                ret = "Lateral-Esquerdo";
                break;
            case Jogador.POSICAO_VOLANTE:
                ret = "Volante";
                break;
            case Jogador.POSICAO_MEIA:
                ret = "Meio-Campo";
                break;
            case Jogador.POSICAO_MEIA_D:
                ret = "Meia-Direita";
                break;
            case Jogador.POSICAO_MEIA_E:
                ret = "Meia-Esquerda";
                break;
            case Jogador.POSICAO_MEIA_ATACANTE:
                ret = "Meia-Atacante";
                break;
            case Jogador.POSICAO_ATACANTE:
                ret = "Atacante";
                break;
            case Jogador.POSICAO_CENTROAVANTE:
                ret = "Centroavante";
                break;
            case Jogador.POSICAO_PONTA_D:
                ret = "Ponta-Direita";
                break;
            case Jogador.POSICAO_PONTA_E:
                ret = "Ponta-Esquerda";
                break;
            default:
                ret = "nada";
                break;
        }
        return ret;
    }

    public int getPosicaoStrToInt(String posicao){
        int ret;
        switch (posicao) {
            case "Goleiro":
                ret = Jogador.POSICAO_GOLEIRO;
                break;
            case "Fixo":
                ret = Jogador.POSICAO_FIXO;
                break;
            case "Ala-Direita":
                ret = Jogador.POSICAO_ALA_D;
                break;
            case "Ala-Esquerda":
                ret = Jogador.POSICAO_ALA_E;
                break;
            case "Pivo":
                ret = Jogador.POSICAO_PIVO;
                break;
            case "Tecnico":
                ret = Jogador.POSICAO_TECNICO;
                break;
            case "Zagueiro":
                ret = Jogador.POSICAO_ZAGUEIRO;
                break;
            case "Zagueiro-Central":
                ret = Jogador.POSICAO_ZAGUEIRO_CENTRAL;
                break;
            case "Quarto-Zagueiro":
                ret = Jogador.POSICAO_ZAGUEIRO_QUARTO;
                break;
            case "Lateral-Direito":
                ret = Jogador.POSICAO_LATERAL_D;
                break;
            case "Lateral-Esquerdo":
                ret = Jogador.POSICAO_LATERAL_E;
                break;
            case "Volante":
                ret = Jogador.POSICAO_VOLANTE;
                break;
            case "Meio-Campo":
                ret = Jogador.POSICAO_MEIA;
                break;
            case "Meia-Direita":
                ret = Jogador.POSICAO_MEIA_D;
                break;
            case "Meia-Esquerda":
                ret = Jogador.POSICAO_MEIA_E;
                break;
            case "Meia-Atacante":
                ret = Jogador.POSICAO_MEIA_ATACANTE;
                break;
            case "Atacante":
                ret = Jogador.POSICAO_ATACANTE;
                break;
            case "Centroavante":
                ret = Jogador.POSICAO_CENTROAVANTE;
                break;
            case "Ponta-Direita":
                ret = Jogador.POSICAO_PONTA_D;
                break;
            case "Ponta-Esquerda":
                ret = Jogador.POSICAO_PONTA_E;
                break;
            default:
                ret = 0;
                break;
        }
        return ret;
    }

    public int getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(int bloqueado) {
        this.bloqueado = bloqueado;
    }
}
