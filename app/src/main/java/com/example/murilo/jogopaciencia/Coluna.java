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
public class Coluna extends PilhaBase
{
    private Context context;
    private ArrayList<Carta> cartas = new ArrayList<>();
    private int posicao;
    private CartaClicada cartaClicada = new CartaClicada();

    private MesaDoJogo mesaDoJogo;

    public Coluna(Context context)
    {
        super(context);
    }

    public Coluna(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        this.context = context;
        this.mesaDoJogo = (MesaDoJogo) context;
        reset();
    }

    public Coluna(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void inserirNoTopo(Carta carta)

    {
        cartas.add(carta);

        carta.setOnClickListener(cartaClicada);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) getResources().getDimension(R.dimen.altura_carta));
        params.topMargin = (int) ((cartas.size() - 1) * getResources().getDimension(R.dimen.distancia_cartas));
        addView(cartas.get(cartas.size() - 1), params);
    }

    @Override
    public Carta retirarDoTopo()
    {
        return cartas.remove(cartas.size() - 1);
    }

    @Override
    public void reset()
    {
        removeAllViews();
        cartas = new ArrayList<>();

        setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mesaDoJogo.cartaClicadaColuna((Coluna) v, null);
            }
        });
    }

    public ArrayList<Carta> getCartas()
    {
        return cartas;
    }

    public void setPosicao(int pos)
    {
        this.posicao = pos;
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
            mesaDoJogo.cartaClicadaColuna((Coluna) v.getParent(), (Carta) v);
        }
    }
}
