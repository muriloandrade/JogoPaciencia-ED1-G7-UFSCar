package com.example.murilo.jogopaciencia.Pilhas;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.murilo.jogopaciencia.Carta;

/**
 * Classe TAD_PilhaCartas
 * Gerencia uma pilha de Cartas com metodos primitivos
 */
public class TAD_PilhaCartas extends RelativeLayout
{
    private Context context;

    // Elementos para controle da pilha
    private NodeCarta ultimoElemento;
    private int tamanho = 0;

    // Construtores
    public TAD_PilhaCartas(Context context)
    {
        super(context);
    }

    public TAD_PilhaCartas(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        this.context = context;
    }

    public TAD_PilhaCartas(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }
    // Fim Construtores

    //Destrutor
    public void deleteNode(NodeCarta nodeCarta)
    {
        nodeCarta = null;
    }


    // Getters e Setters
    public NodeCarta getUltimoElemento()
    {
        return ultimoElemento;
    }

    public void setUltimoElemento(NodeCarta ultimoElemento)
    {
        this.ultimoElemento = ultimoElemento;
    }

    public int getTamanho()
    {
        return tamanho;
    }

    public void setTamanho(int tamanho)
    {
        this.tamanho = tamanho;
    }
    // Fim Getters e Setters


    public boolean vazia()
    {
        return ultimoElemento == null;
    }

    /**
     * Metodo para empilhar uma carta na pilha
     *
     * @param carta: carta a ser empilhada
     * @return : verdadeiro se foi empilhada, falso se nao foi
     */
    public boolean empilha(Carta carta)
    {
        NodeCarta auxNode;

        try
        {
            auxNode = new NodeCarta();
        }
        catch (OutOfMemoryError memoryError)
        {
            return false; // faltou memoria
        }

        auxNode.setCarta(carta);
        auxNode.setNext(ultimoElemento);
        ultimoElemento = auxNode;
        setTamanho(getTamanho() + 1);

        return true;
    }

    /**
     * Metodo para des-empilhar a ultima carta da pilha
     *
     * @return : retorna a carta desempilhada
     */
    public Carta desempilha()
    {
        Carta cartaDesempilhada = null;

        if (!vazia())
        {
            NodeCarta refNode = ultimoElemento;
            cartaDesempilhada = ultimoElemento.getCarta();
            ultimoElemento = ultimoElemento.getNext();
            deleteNode(refNode);
            setTamanho(getTamanho() - 1);
        }

        return cartaDesempilhada;
    }

    /**
     * Metodo para des-empilhar todas as cartas desta pilha (utilizados nos metodos de reset)
     */
    public void desempilhaTodas()
    {
        while (!vazia())
        {
            desempilha();
        }

    }
}
