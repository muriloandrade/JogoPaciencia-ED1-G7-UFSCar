package com.example.murilo.jogopaciencia;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Murilo on 04/04/2015.
 */
public class PilhaAberta extends PilhaBase
{
    MesaDoJogo mesaDoJogo;
    private CartaClicada cartaClicada = new CartaClicada();

    public PilhaAberta(Context context)
    {
        super(context);
    }

    public PilhaAberta(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mesaDoJogo = (MesaDoJogo) context;
        reset();
    }

    public PilhaAberta(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void inserirNoTopo(Carta carta)
    {
        carta.setImagem(null);
        cartas.add(carta);

        carta.setOnClickListener(cartaClicada);
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
        cartas = new ArrayList<>();
    }

    private class CartaClicada implements OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            mesaDoJogo.cartaClicadaPilhaAberta((Carta) v);
        }
    }
}
