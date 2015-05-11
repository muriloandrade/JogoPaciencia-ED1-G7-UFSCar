package com.example.murilo.jogopaciencia;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Classe Carta
 *
 */
public class Carta extends ImageView
{
    public static int WIDTH  = 1000;

    public static char COPAS  = 'c';
    public static char OURO   = 'o';
    public static char PAUS   = 'p';
    public static char ESPADA = 'e';

    public static int PRETO    = 0;
    public static int VERMELHO = 1;

    public static char[] naipes = {COPAS, OURO, PAUS, ESPADA};
    Context context;
    private char    naipe;
    private int     cor;
    private int     numero;
    private boolean mostrandoFrente;
    private boolean selecionada;

    public Carta(Context context)
    {
        super(context);

        this.context = context;

        setPadding(3, 3, 3, 3);
    }

    public Carta(Context context, AttributeSet attrs)
    {
        super(context, attrs);

    }

    public Carta(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public int getCor()
    {
        return cor;
    }

    public void setImagem(Drawable drawable)
    {
        if (drawable == null)
        {
            StringBuilder imgUri = new StringBuilder();

            imgUri.append("drawable/");
            imgUri.append(naipe);
            imgUri.append(numero);
            if (selecionada) imgUri.append("s");

            int imageResource = getResources().getIdentifier(imgUri.toString(), null, context.getPackageName());

            setImageDrawable(context.getResources().getDrawable(imageResource));

            mostrandoFrente = true;
        }
        else
        {
            setImageDrawable(drawable);

            mostrandoFrente = false;
        }
    }

    public int getNumero()
    {
        return numero;
    }

    public void setNumero(int numero)
    {
        this.numero = numero;
    }

    public char getNaipe()
    {
        return naipe;
    }

    public void setNaipe(char naipe)
    {
        this.naipe = naipe;

        if (naipe == PAUS || naipe == ESPADA)
        {
            cor = PRETO;
        }
        else
        {
            cor = VERMELHO;
        }
    }

    public void selecionar(boolean selecionar)
    {
        selecionada = selecionar;
        setImagem(null);
    }

    public boolean estaMostrandoFrente()
    {
        return mostrandoFrente;
    }

    public boolean estaSelecionada()
    {
        return selecionada;
    }
}
