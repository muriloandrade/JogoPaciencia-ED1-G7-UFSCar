package com.example.murilo.jogopaciencia.Pilhas;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.murilo.jogopaciencia.Carta;
import com.example.murilo.jogopaciencia.MesaDoJogo;
import com.example.murilo.jogopaciencia.R;

/**
 * Classe PilhaIntermediaria
 */
public class PilhaIntermediaria extends TAD_PilhaCartas
{
    private Context context;
    private int     posicao;
    private CartaClicada cartaClicada = new CartaClicada();

    private MesaDoJogo mesaDoJogo;

    // Construtores
    public PilhaIntermediaria(Context context)
    {
        super(context);
        this.context = context;
    }

    public PilhaIntermediaria(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        this.context = context;
        this.mesaDoJogo = (MesaDoJogo) context;
        reset();
    }

    public PilhaIntermediaria(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }
    // Fim Construtores

    // Getters e Setters
    public void setPosicao(int pos)
    {
        this.posicao = pos;
    }

    // Fim getters e setters

    /**
     * Preparacao desta pilha intermediaria ao ser construida
     */
    public void reset()
    {
        removeAllViews();
        desempilhaTodas();

        setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mesaDoJogo.cartaClicadaPilhaIntermediaria((PilhaIntermediaria) v, null);
            }
        });
    }

    /**
     * Metodo para empilhar uma carta nesta pilha intermediaria
     *
     * @param carta: carta a ser empilhada
     * @return : verdadeiro se foi empilhada, falso se nao foi
     */
    public boolean empilha(Carta carta)
    {
        boolean deuCerto;
        deuCerto = super.empilha(carta);

        if (deuCerto)
        {
            // controla os cliques na carta
            carta.setOnClickListener(cartaClicada);

            // define a posicao e tamanho da carta nesta pilha intermediaria
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) getResources().getDimension(R.dimen.altura_carta));
            params.topMargin = (int) ((getTamanho() - 1) * getResources().getDimension(R.dimen.distancia_cartas));

            // adiciona a carta (visualmente) nesta pilha intermediaria
            addView(getUltimoElemento().getCarta(), params);
        }

        return deuCerto;
    }

    /**
     * Classe para controlar o clique em cartas desta pilha intermediaria
     */
    private class CartaClicada implements OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            mesaDoJogo.cartaClicadaPilhaIntermediaria((PilhaIntermediaria) v.getParent(), (Carta) v);
        }
    }
}
