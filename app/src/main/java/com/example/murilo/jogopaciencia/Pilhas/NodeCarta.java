package com.example.murilo.jogopaciencia.Pilhas;

import com.example.murilo.jogopaciencia.Carta;

/**
 * Classe NodeCarta
 * Nos para a lista encadeada das pilhas de cartas
 */
public class NodeCarta
{
    private Carta     carta;
    private NodeCarta next;

    // Getters e Setters
    public Carta getCarta()
    {
        return carta;
    }

    public void setCarta(Carta carta)
    {
        this.carta = carta;
    }

    public NodeCarta getNext()
    {
        return next;
    }

    public void setNext(NodeCarta next)
    {
        this.next = next;
    }
    // Fim getters e setters
}
