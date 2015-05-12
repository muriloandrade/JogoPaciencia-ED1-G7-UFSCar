package com.example.murilo.jogopaciencia;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Classe Carta (de baralho)
 *
 */
public class Carta extends ImageView
{
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

    // Construtores
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
    // Fim construtores


    // Getters e Setters
    public int getCor()
    {
        return cor;
    }

    public void setCor(int cor)
    {
        this.cor = cor;
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
            setCor(PRETO);
        }
        else
        {
            setCor(VERMELHO);
        }
    }

    public boolean getSelecionada()
    {
        return selecionada;
    }

    public void setSelecionada(boolean selecionar)
    {
        this.selecionada = selecionar;
        setImagem(null);
    }

    public void setImagem(Drawable drawable)
    {
        // se nenhuma imagem especial for passada, por padrao, exibe-se a frente desta carta
        if (drawable == null)
        {
            StringBuilder imgUri = new StringBuilder();

            imgUri.append("drawable/");
            imgUri.append(naipe);
            imgUri.append(numero);
            if (selecionada)
            {
                imgUri.append("s");
            }

            int imageResource = getResources().getIdentifier(imgUri.toString(), null, context.getPackageName());

            setImageDrawable(context.getResources().getDrawable(imageResource));

            setMostrandoFrente(true);
        }
        else
        {
            setImageDrawable(drawable);
            setMostrandoFrente(false);
        }
    }

    public boolean getMostrandoFrente()
    {
        return mostrandoFrente;
    }

    public void setMostrandoFrente(boolean mostrandoFrente)
    {
        this.mostrandoFrente = mostrandoFrente;
    }

    // Fim getters e setters
}
