package com.example.murilo.jogopaciencia;

/**
 * Classe TAD_PilhaCartas
 * <p>
 * Gerencia uma pilha burra de Cartas com metodos primitivos
 */
public class TAD_PilhaCartas
{
    private NodeCarta ultimoElemento;

    public NodeCarta getUltimoElemento()
    {
        return ultimoElemento;
    }

    public void setUltimoElemento(NodeCarta ultimoElemento)
    {
        this.ultimoElemento = ultimoElemento;
    }

    public boolean vazia()
    {
        return ultimoElemento == null ? true : false;
    }

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

        auxNode.setInfo(carta);
        auxNode.setNext(ultimoElemento);
        ultimoElemento = auxNode;

        return true;
    }

    public Carta desempilha()
    {
        Carta cartaDesempilhada = null;

        if (vazia() == false)
        {
            cartaDesempilhada = ultimoElemento.getInfo();
            ultimoElemento = ultimoElemento.getNext();
        }

        return cartaDesempilhada;
    }
}
