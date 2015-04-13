package com.example.murilo.jogopaciencia;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Murilo on 04/04/2015.
 */
public class Deck extends PilhaBase
{
    public Deck(Context context)
    {
        super(context);
    }

    public Deck(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        reset();
    }

    public Deck(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void inserirNoTopo(Carta carta)
    {
        carta.setImagem(getResources().getDrawable(R.drawable.back));
        cartas.add(carta);
        addView(cartas.get(cartas.size() - 1));
    }

    @Override
    public Carta retirarDoTopo()
    {
        if (cartas.size() > 0)
        {
            return cartas.remove(cartas.size() - 1);
        }
        return null;
    }

    @Override
    public void reset()
    {
        removeAllViews();
        this.cartas = new ArrayList<>(52);

        for (int i = 0; i < Carta.naipes.length; i++)
        {
            for (int j = 1; j <= 13; j++)
            {
                Carta novaCarta = new Carta(context);
                novaCarta.setNaipe(Carta.naipes[i]);
                novaCarta.setNumero(j);
                inserirNoTopo(novaCarta);
            }
        }

        this.embaralhar();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return true;
    }

    public void embaralhar()
    {
        Collections.shuffle(cartas);
    }
}
