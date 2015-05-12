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
 * Classe PilhaDefinitiva
 */
public class PilhaDefinitiva extends TAD_PilhaCartas
{
    private Context context;

    private char naipe;
    private boolean completo = false;

    private MesaDoJogo mesaDoJogo;
    private CartaClicada cartaClicada = new CartaClicada();

    // Construtores
    public PilhaDefinitiva(Context context)
    {
        super(context);
        this.context = context;
    }

    public PilhaDefinitiva(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;

        mesaDoJogo = (MesaDoJogo) context;
        reset();
    }

    public PilhaDefinitiva(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }
    // Fim construtores

    // Getters e Setters
    public char getNaipe()
    {
        return this.naipe;
    }

    public void setNaipe(char naipe)
    {
        this.naipe = naipe;
    }

    public boolean getCompleto()
    {
        return completo;
    }

    public void setCompleto(boolean completo)
    {
        this.completo = completo;
    }
    // Fim getters e setters

    /**
     * Preparacao desta pilha definitiva ao ser construida
     */
    public void reset()
    {
        removeAllViews();
        desempilhaTodas();
    }

    /**
     * Metodo para empilhar uma carta nesta pilha definitiva
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

            // reseta definicoes de tamanho e posicao da carta
            RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) getResources().getDimension(R.dimen.altura_carta));

            // adiciona a carta (visualmente) a esta pilha definitiva
            addView(getUltimoElemento().getCarta(), params);
        }
        return deuCerto;
    }

    /**
     * Classe para controlar o clique em cartas desta pilha definitiva
     */
    private class CartaClicada implements OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            mesaDoJogo.cartaClicadaPilhaDefinitiva((Carta) v);
        }
    }
}
