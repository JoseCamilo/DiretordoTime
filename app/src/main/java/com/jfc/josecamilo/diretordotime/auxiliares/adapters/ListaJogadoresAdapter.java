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
import com.jfc.josecamilo.diretordotime.jogadores.Jogador;

import java.util.List;

/**
 * Created by jose.camilo on 15/12/2016.
 */
public class ListaJogadoresAdapter extends ArrayAdapter<Jogador> {

    private Context context;
    private List<Jogador> jogadores;

    public ListaJogadoresAdapter(Context context, List<Jogador> jogadores) {
        super(context, 0, jogadores);
        this.context = context;
        this.jogadores = jogadores;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_jogadores_list,null);

        Jogador jogador = jogadores.get(position);

        TextView nome = (TextView) view.findViewById(R.id.nome);
        TextView posicao = (TextView) view.findViewById(R.id.posicao);

        ImageView imagem = (ImageView) view.findViewById(R.id.imagem);

        nome.setText(jogador.getNome());
        posicao.setText(jogador.getDescPosicao(jogador.getPosicao()));


        switch (jogador.getPosicao()){
            case Jogador.POSICAO_GOLEIRO:
                imagem.setImageResource(R.drawable.football_sports_gloves);
                break;
            case Jogador.POSICAO_FIXO:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_ALA_D:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_ALA_E:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_PIVO:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_TECNICO:
                imagem.setImageResource(R.drawable.football_leader_man);
                break;
            case Jogador.POSICAO_ZAGUEIRO:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_ZAGUEIRO_CENTRAL:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_ZAGUEIRO_QUARTO:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_LATERAL_D:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_LATERAL_E:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_VOLANTE:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_MEIA:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_MEIA_D:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_MEIA_E:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_MEIA_ATACANTE:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_ATACANTE:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_CENTROAVANTE:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_PONTA_D:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            case Jogador.POSICAO_PONTA_E:
                imagem.setImageResource(R.drawable.football_shoes);
                break;
            default:
        }

        if (jogador.getBloqueado() == Jogador.BLOQUEADO_TRUE){
            nome.setTypeface(null,Typeface.ITALIC);
            nome.setPaintFlags(nome.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return view;
    }


}
