package com.example.murilo.jogopaciencia.Pilhas;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.murilo.jogopaciencia.Carta;
import com.example.murilo.jogopaciencia.MesaDoJogo;

/**
 * Classe Pilha Aberta (ao lado do deck)
 */
public class PilhaAberta extends TAD_PilhaCartas
{
    private Context context;

    private MesaDoJogo mesaDoJogo;
    private CartaClicada cartaClicada = new CartaClicada();

    // Construtores
    public PilhaAberta(Context context)
    {
        super(context);
        this.context = context;
    }

    public PilhaAberta(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;

        mesaDoJogo = (MesaDoJogo) context;
        reset();
    }

    public PilhaAberta(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }
    // Fim construtores

    /**
     * Preparacao desta pilha aberta ao ser construida
     */
    public void reset()
    {
        removeAllViews();
        desempilhaTodas();
    }

    /**
     * Metodo para empilhar uma carta nesta Pilha Aberta
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

            // mostra a frente da carta
            carta.setImagem(null);

            // adiciona a carta (visualmente) nesta pilha aberta
            addView(getUltimoElemento().getCarta());
        }

        return deuCerto;
    }

    /**
     * Classe para controlar o clique em cartas desta pilha aberta
     */
    private class CartaClicada implements OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            mesaDoJogo.cartaClicadaPilhaAberta((Carta) v);
        }
    }
}
