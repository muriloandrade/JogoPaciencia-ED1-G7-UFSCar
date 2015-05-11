package com.example.murilo.jogopaciencia;

/**
 * Classe NodeCarta
 * <p>
 * Nós para a lista encadeada das pilhas de cartas
 */
public class NodeCarta
{
    private Carta     info;
    private NodeCarta next;

    public Carta getInfo()
    {
        return info;
    }

    public void setInfo(Carta info)
    {
        this.info = info;
    }

    public NodeCarta getNext()
    {
        return next;
    }

    public void setNext(NodeCarta next)
    {
        this.next = next;
    }
}
