package com.example.murilo.jogopaciencia;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by Murilo on 04/04/2015.
 */
public abstract class PilhaBase extends RelativeLayout
{
    protected ArrayList<Carta> cartas;
    Context context;

    public PilhaBase(Context context)
    {
        super(context);
    }

    public PilhaBase(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        this.context = context;
    }

    public PilhaBase(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public abstract void inserirNoTopo(Carta carta);

    public abstract Carta retirarDoTopo();

    public abstract void reset();
}
