package com.example.murilo.jogopaciencia.Pilhas;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.murilo.jogopaciencia.Carta;
import com.example.murilo.jogopaciencia.R;

import java.util.Random;

/**
 * Classe Deck (de cartas de baralho)
 */
public class Deck extends TAD_PilhaCartas
{
    private Context context;

    // Construtores
    public Deck(Context context)
    {
        super(context);
        this.context = context;
    }

    public Deck(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;

        reset();
    }

    public Deck(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }
    // Fim construtores

    /**
     * Preparacao do deck ao ser construido
     */
    public void reset()
    {
        removeAllViews();
        desempilhaTodas();

        for (int i = 0; i < Carta.naipes.length; i++)
        {
            for (int j = 1; j <= 13; j++)
            {
                Carta novaCarta = new Carta(context);
                novaCarta.setNaipe(Carta.naipes[i]);
                novaCarta.setNumero(j);
                empilha(novaCarta);
            }
        }

        for (int i = 0; i < 5; i++)
        {
            this.embaralhar();
        }
    }

    /**
     * Metodo para empilhar uma carta no deck
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
            // exibe a imagem do verso da carta
            carta.setImagem(getResources().getDrawable(R.drawable.back));

            // adiciona a carta (visualmente) ao deck
            addView(getUltimoElemento().getCarta());
        }

        return deuCerto;
    }

    /**
     * Metodo para des-empilhar a ultima carta do deck
     *
     * @return : retorna a carta desempilhada
     */
    public Carta desempilha()
    {
        Carta cartaDesempilhada;
        cartaDesempilhada = super.desempilha();

        // remove (visualmente) a carta do deck
        removeView(cartaDesempilhada);

        return cartaDesempilhada;
    }

    /**
     * Metodo para embaralhar as cartas
     */
    public void embaralhar()
    {
        TAD_PilhaCartas pilhaTemp1 = new TAD_PilhaCartas(context);
        TAD_PilhaCartas pilhaTemp2 = new TAD_PilhaCartas(context);
        Random          random     = new Random();

        while (!vazia())
        {
            int i = getTamanho() > 1 ? random.nextInt(this.getTamanho() - 1) : 0;

            for (int j = 0; j <= i; j++)
            {
                pilhaTemp1.empilha(this.desempilha());
            }

            if (!pilhaTemp1.vazia())
            {
                pilhaTemp2.empilha(pilhaTemp1.desempilha());
            }

            while (!pilhaTemp1.vazia())
            {
                this.empilha(pilhaTemp1.desempilha());
            }
        }

        while (!pilhaTemp2.vazia())
        {
            this.empilha(pilhaTemp2.desempilha());
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return true;
    }
}
