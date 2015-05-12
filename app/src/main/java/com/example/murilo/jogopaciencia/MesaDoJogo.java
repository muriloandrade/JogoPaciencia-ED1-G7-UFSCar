package com.example.murilo.jogopaciencia;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.murilo.jogopaciencia.Pilhas.Deck;
import com.example.murilo.jogopaciencia.Pilhas.NodeCarta;
import com.example.murilo.jogopaciencia.Pilhas.PilhaAberta;
import com.example.murilo.jogopaciencia.Pilhas.PilhaDefinitiva;
import com.example.murilo.jogopaciencia.Pilhas.PilhaIntermediaria;
import com.example.murilo.jogopaciencia.Pilhas.TAD_PilhaCartas;


public class MesaDoJogo extends ActionBarActivity
{

    private static final int NUM_PILHAS_INTERMEDIARIAS = 7;
    private static final int NUM_PILHAS_DEFINITIVAS    = 4;

    public static TAD_PilhaCartas cartasSelecionadas;
    public static PilhaIntermediaria ult_pilhaIntermediaria = null;
    private static PilhaAberta pilhaAberta;
    private static PilhaDefinitiva[]    pilhasDefinitivas    = new PilhaDefinitiva[NUM_PILHAS_DEFINITIVAS];
    private static PilhaIntermediaria[] pilhasIntermediarias = new PilhaIntermediaria[NUM_PILHAS_INTERMEDIARIAS];
    private static TAD_PilhaCartas origem;
    private static TextView        tx_venceu;
    private static boolean venceu = false;
    private Deck         deck;
    private LinearLayout pilhasDefinitivasLayout;
    private LinearLayout mainLayout;
    private Button       bt_novoJogo;

    /**
     * Metodo para des-selecionar as cartas e esvaziar a pilha de cartas selecionadas
     */
    private static void limpaSelecionadas()
    {
        while (!cartasSelecionadas.vazia())
        {
            cartasSelecionadas.getUltimoElemento().getCarta().setSelecionada(false);
            cartasSelecionadas.desempilha();
        }
    }

    // Metodos para criar e organizar um novo jogo

    /**
     * Metodo para adicionar a(s) carta(s) selecionada(s) a uma pilha destino
     *
     * @param origem:  pilha que esta enviando a(s) carta(s)
     * @param destino: pilha que esta recebendo a(s) carta(s)
     */
    private static void adicionarSelecionadasApilha(TAD_PilhaCartas origem, TAD_PilhaCartas destino)
    {
        do
        {
            cartasSelecionadas.getUltimoElemento().getCarta().setSelecionada(false);
            origem.desempilha();
            origem.removeView(cartasSelecionadas.getUltimoElemento().getCarta());
            destino.empilha(cartasSelecionadas.getUltimoElemento().getCarta());
            cartasSelecionadas.desempilha();
        }
        while (!cartasSelecionadas.vazia());
    }

    /**
     * Metodo para gerenciar os cliques nas pilhas intermediarias
     *
     * @param destino: pilha intermediari que vai receber a(s) carta(s)
     * @param carta:   carta em que houve o clique
     */
    public static void cartaClicadaPilhaIntermediaria(PilhaIntermediaria destino, Carta carta)
    {
        // se nao houver carta(s) ja selecionada(s), entao a(s) cartas(s) esta(o) sendo selecionada(s) e nao mudara(o) de pilha intermediaria
        if (cartasSelecionadas.getTamanho() == 0)
        {
            origem = destino;
        }

        // se o clique foi numa pilha intermediaria vazia, pode ser colocado apenas uma sequencia de cartas iniciada por um Rei
        if (carta == null)
        {
            if (cartasSelecionadas.getTamanho() > 0 && cartasSelecionadas.getUltimoElemento().getCarta().getNumero() == 13)
            {
                adicionarSelecionadasApilha(origem, destino);
            }
            limpaSelecionadas();
        }

        // se tem apenas uma carta selecionada e ela foi clicada novamente, entao desfaz a selecao
        else if (cartasSelecionadas.getTamanho() == 1 && carta == cartasSelecionadas.getUltimoElemento().getCarta())
        {
            limpaSelecionadas();
        }

        // se a carta clicada estiver de costas e for a ultima da pilha intermediaria, entao ela sera virada
        else if (!carta.getMostrandoFrente() && carta == destino.getUltimoElemento().getCarta())
        {
            carta.setImagem(null);
            limpaSelecionadas();
        }

        else
        {
            // tentativa para encaixar a(s) carta(s) selecionada(s) na pilha intermediaria destino
            if (ult_pilhaIntermediaria != destino && cartasSelecionadas.getTamanho() > 0)
            {
                // regra: a primeira carta da sequencia tem que ser de cor diferente e um numero abaixo da carta clicada
                if (carta == destino.getUltimoElemento().getCarta() && cartasSelecionadas.getUltimoElemento().getCarta().getCor() != carta.getCor() && cartasSelecionadas.getUltimoElemento().getCarta().getNumero() == carta.getNumero() - 1)
                {
                    adicionarSelecionadasApilha(origem, destino);
                }
                limpaSelecionadas();
            }

            // seleciona a(s) carta(s) abaixo da carta clicada, se estiver(em) mostrando a frente
            else
            {
                limpaSelecionadas();

                NodeCarta auxNode = destino.getUltimoElemento();
                Carta cartaAtual;

                for (int i = 0; i < destino.getTamanho(); i++)
                {
                    if (auxNode != null)
                    {
                        cartaAtual = auxNode.getCarta();

                        if (cartaAtual.getMostrandoFrente())
                        {
                            cartaAtual.setSelecionada(true);
                            cartasSelecionadas.empilha(cartaAtual);
                            if (cartaAtual == carta)
                            {
                                break;
                            }
                        }
                        auxNode = auxNode.getNext();
                    }
                }
            }

            // atualiza a pilha intermediaria atual como a ultima pilha intermediaria em que houve atividade
            ult_pilhaIntermediaria = destino;
        }
    }

    /**
     * Metodo para gerenciar os cliques na pilha aberta (ao lado do deck)
     *
     * @param carta: carta em que houve o clique
     */
    public static void cartaClicadaPilhaAberta(Carta carta)
    {
        // informa que a ultima atividade nao tera sido em pilha intermediaria, pois esta sendo na pilha aberta
        ult_pilhaIntermediaria = null;

        // a carta foi clicada novamente, entao desfaz a selecao
        if (carta.getSelecionada())
        {
            limpaSelecionadas();
        }

        // seleciona a carta clicada
        else
        {
            limpaSelecionadas();
            origem = pilhaAberta;
            carta.setSelecionada(true);
            cartasSelecionadas.empilha(carta);
        }
    }

    // Fim metodos para criar e organizar um novo jogo

    /**
     * Metodo para gerenciar os cliques nas cartas dos pilhasDefinitivas definitivos
     *
     * @param carta: carta em que houve o clique
     */
    public static void cartaClicadaPilhaDefinitiva(Carta carta)
    {
        // informa que a ultima atividade nao tera sido em pilha intermediaria, pois esta sendo em uma pilha definitiva
        ult_pilhaIntermediaria = null;

        // a carta foi clicada novamente, entao desfaz a selecao
        if (carta.getSelecionada())
        {
            limpaSelecionadas();
        }

        // se houver apenas uma carta selecionada, tenta encaixa-la na pilha definitiva
        else if (cartasSelecionadas.getTamanho() == 1)
        {
            // regra: a carta selecionada deve ser do mesmo naipe e um numero acima da carta clicada
            if (cartasSelecionadas.getUltimoElemento().getCarta().getNaipe() == carta.getNaipe()
                    && cartasSelecionadas.getUltimoElemento().getCarta().getNumero() == carta.getNumero() + 1)
            {
                PilhaDefinitiva destino = null;

                // localiza a pilha definitiva em que a carta foi clicada e define-o como destino
                for (int i = 0; i < NUM_PILHAS_DEFINITIVAS; i++)
                {
                    if (cartasSelecionadas.getUltimoElemento().getCarta().getNaipe() == pilhasDefinitivas[i].getNaipe())
                    {
                        destino = pilhasDefinitivas[i];
                    }
                }

                adicionarSelecionadasApilha(origem, destino);

                // confirma se esta pilha definitiva ja esta completa
                if (destino != null && destino.getUltimoElemento().getCarta().getNumero() == 13)
                {
                    destino.setCompleto(true);
                }

                // confirma se todos pilhasDefinitivas estao completos e, se for o caso, encerra o jogo com vitoria
                for (int i = 0; i < NUM_PILHAS_DEFINITIVAS; i++)
                {
                    if (i == 0 && pilhasDefinitivas[0].getCompleto())
                    {
                        venceu = true;
                    }
                    else
                    {
                        venceu = venceu && pilhasDefinitivas[i].getCompleto();
                    }
                }

                if (venceu)
                {
                    tx_venceu.setVisibility(View.VISIBLE);
                }
            }

            limpaSelecionadas();
        }

        // se nenhuma carta estiver selecionada, ou se mais de uma estiver, entao seleciona apenas a carta clicada
        else
        {
            limpaSelecionadas();

            for (int i = 0; i < NUM_PILHAS_DEFINITIVAS; i++)
            {
                if (carta.getNaipe() == pilhasDefinitivas[i].getNaipe())
                {
                    origem = pilhasDefinitivas[i];
                }
            }
            carta.setSelecionada(true);
            cartasSelecionadas.empilha(carta);
        }
    }

    /**
     * Inicio da execucao do jogo
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Busca os layouts ja instanciados
        pilhasDefinitivasLayout = (LinearLayout) findViewById(R.id.pilhasDefinitivasLayout);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        deck = (Deck) findViewById(R.id.deck);
        pilhaAberta = (PilhaAberta) findViewById(R.id.open);
        bt_novoJogo = (Button) findViewById(R.id.bt_novoJogo);
        tx_venceu = (TextView) findViewById(R.id.tx_venceu);

        // Cria o vetor das PILHAS DEFINITIVAS
        for (int i = 0; i < NUM_PILHAS_DEFINITIVAS; i++)
        {
            pilhasDefinitivas[i] = (PilhaDefinitiva) pilhasDefinitivasLayout.getChildAt(i);
            pilhasDefinitivas[i].setNaipe(Carta.naipes[i]);
            pilhasDefinitivas[i].setOnClickListener(new PilhaDefinitivaClick());

            Drawable naipe_fundo = null;

            switch (i)
            {
                case 0:
                    naipe_fundo = getResources().getDrawable(R.drawable.mc);
                    break;
                case 1:
                    naipe_fundo = getResources().getDrawable(R.drawable.mo);
                    break;
                case 2:
                    naipe_fundo = getResources().getDrawable(R.drawable.mp);
                    break;
                case 3:
                    naipe_fundo = getResources().getDrawable(R.drawable.me);
                    break;
            }

            pilhasDefinitivas[i].setBackground(naipe_fundo);
        }

        // Cria o vetor das PILHAS INTERMEDIARIAS
        for (int i = 0; i < NUM_PILHAS_INTERMEDIARIAS; i++)
        {
            pilhasIntermediarias[i] = (PilhaIntermediaria) mainLayout.getChildAt(i);
            pilhasIntermediarias[i].setPosicao(i);
        }

        // Atribui funcao para click do botao Novo Jogo
        bt_novoJogo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                novoJogo();
            }
        });

        // Atribui funcao para click no Deck
        deck.setOnClickListener(new DeckClick());

        // Cria a pilha para receber cartas selecionadas
        cartasSelecionadas = new TAD_PilhaCartas(getApplicationContext());

        // Inicia um novo jogo
        bt_novoJogo.callOnClick();
    }

    private void novoJogo()
    {
        resetarJogo();
        distribuirCartas();
    }

    private void resetarJogo()
    {
        // inicia o Deck
        deck.reset();

        // inicia a Pilha Aberta
        pilhaAberta.reset();

        // inicia os pilhasDefinitivas
        for (int i = 0; i < NUM_PILHAS_DEFINITIVAS; i++)
        {
            pilhasDefinitivas[i].reset();
        }

        // inicia as pilhasIntermediarias
        for (int i = 0; i < NUM_PILHAS_INTERMEDIARIAS; i++)
        {
            pilhasIntermediarias[i].reset();
        }

        // reseta cartas selecionadas
        limpaSelecionadas();

        tx_venceu.setVisibility(View.GONE);
    }

    private void distribuirCartas()
    {
        int primeiraPilhaIntermediaria = 0;
        final int a_distribuir   = 28;
        int       distribuidas   = 0;
        Carta     carta;

        for (int i = 0; distribuidas < a_distribuir; i++)
        {
            // Retira uma carta do deck
            carta = deck.desempilha();

            // se for a ultima da pilha intermediaria, torne-a visivel
            if (i == primeiraPilhaIntermediaria)
            {
                carta.setImagem(null);
            }

            // insere a carta na pilha intermediaria
            pilhasIntermediarias[i].empilha(carta);

            distribuidas++;

            // se for a ultima pilha intermediaria, reinicia a distribuicao pela primeira pilha intermediaria (em forma de escadinha)
            if (i == NUM_PILHAS_INTERMEDIARIAS - 1)
            {
                primeiraPilhaIntermediaria++;
                i = primeiraPilhaIntermediaria - 1;
            }
        }
    }

    /**
     * Classe para gerenciar os clicks no Deck
     */
    private class DeckClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Carta cartaTopoDeck = deck.desempilha();

            // se houver carta no deck, transfere-a para a pilha aberta
            if (cartaTopoDeck != null)
            {
                pilhaAberta.empilha(cartaTopoDeck);
            }

            // se nao houver carta no deck, retorna todas da pilha aberta para o deck
            else
            {
                for (int i = pilhaAberta.getTamanho(); i > 0; i--)
                {
                    Carta cartaVoltarDeck;
                    cartaVoltarDeck = pilhaAberta.desempilha();
                    pilhaAberta.removeView(cartaVoltarDeck);
                    cartaVoltarDeck.setOnClickListener(null);
                    deck.empilha(cartaVoltarDeck);
                }
            }
        }
    }

    /*
     *  Classe para gerenciar os cliques na pilha definitiva vazia
     */
    private class PilhaDefinitivaClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            PilhaDefinitiva pilhaDefinitiva = (PilhaDefinitiva) v;

            // se houver apenas uma carta selecionada, tenta encaixa-la na pilha definitiva
            if (cartasSelecionadas.getTamanho() == 1)
            {
                if (pilhaDefinitiva.getTamanho() == 0
                        && cartasSelecionadas.getUltimoElemento().getCarta().getNaipe() == pilhaDefinitiva.getNaipe()
                        && cartasSelecionadas.getUltimoElemento().getCarta().getNumero() == 1)
                {
                    adicionarSelecionadasApilha(origem, pilhaDefinitiva);
                }

                limpaSelecionadas();
            }
        }
    }
}