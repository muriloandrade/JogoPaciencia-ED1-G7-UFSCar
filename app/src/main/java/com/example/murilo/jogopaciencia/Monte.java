package com.example.murilo.jogopaciencia;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by Murilo on 04/04/2015.
 */
public class Monte extends PilhaBase
{
    private char    naipe;
    private boolean completo = false;

    private MesaDoJogo mesaDoJogo;
    private CartaClicada cartaClicada = new CartaClicada();

    public Monte(Context context)
    {
        super(context);
    }

    public Monte(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mesaDoJogo = (MesaDoJogo) context;
        reset();
    }

    public Monte(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void inserirNoTopo(Carta carta)
    {
        cartas.add(carta);
        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        carta.setOnClickListener(cartaClicada);
        addView(cartas.get(cartas.size() - 1), params);
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

    public char getNaipe()
    {
        return this.naipe;
    }

    public void setNaipe(char naipe)
    {
        this.naipe = naipe;
    }

    public ArrayList<Carta> getCartas()
    {
        return cartas;
    }

    public Carta getCartaTopo()
    {
        return cartas.get(cartas.size() - 1);
    }

    private class CartaClicada implements OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            mesaDoJogo.cartaClicadaMonte((Carta) v);
        }
    }

    public void setCompleto(boolean completo)
    {
        this.completo = completo;
    }

    public boolean getCompleto()
    {
        return completo;
    }
}
