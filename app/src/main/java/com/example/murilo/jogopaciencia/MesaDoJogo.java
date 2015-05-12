package com.example.murilo.jogopaciencia;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.murilo.jogopaciencia.Pilhas.Coluna;
import com.example.murilo.jogopaciencia.Pilhas.Deck;
import com.example.murilo.jogopaciencia.Pilhas.Monte;
import com.example.murilo.jogopaciencia.Pilhas.NodeCarta;
import com.example.murilo.jogopaciencia.Pilhas.PilhaAberta;
import com.example.murilo.jogopaciencia.Pilhas.TAD_PilhaCartas;


public class MesaDoJogo extends ActionBarActivity
{

    private static final int NUM_COLUNAS = 7;
    private static final int NUM_MONTES  = 4;

    public static TAD_PilhaCartas cartasSelecionadas;
    public static Coluna ult_coluna = null;
    private static PilhaAberta pilhaAberta;
    private static Monte[]  montes  = new Monte[NUM_MONTES];
    private static Coluna[] colunas = new Coluna[NUM_COLUNAS];
    private static TAD_PilhaCartas origem;
    private static TextView        tx_venceu;
    private static boolean venceu = false;
    private Deck         deck;
    private LinearLayout montesLayout;
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
     * Metodo para gerenciar os cliques nas colunas
     *
     * @param destino: coluna que vai receber a(s) carta(s)
     * @param carta:   carta em que houve o clique
     */
    public static void cartaClicadaColuna(Coluna destino, Carta carta)
    {
        // se nao houver carta(s) ja selecionada(s), entao a(s) cartas(s) esta(o) sendo selecionada(s) e nao mudara(o) de coluna
        if (cartasSelecionadas.getTamanho() == 0)
        {
            origem = destino;
        }

        // se o clique foi numa coluna vazia, pode ser colocado apenas uma sequencia de cartas iniciada por um Rei
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

        // se a carta clicada estiver de costas e for a ultima da coluna, entao ela sera virada
        else if (!carta.getMostrandoFrente() && carta == destino.getUltimoElemento().getCarta())
        {
            carta.setImagem(null);
            limpaSelecionadas();
        }

        else
        {
            // tentativa para encaixar a(s) carta(s) selecionada(s) na coluna destino
            if (ult_coluna != destino && cartasSelecionadas.getTamanho() > 0)
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

            // atualiza a coluna atual como a ultima coluna em que houve atividade
            ult_coluna = destino;
        }
    }

    /**
     * Metodo para gerenciar os cliques na pilha aberta (ao lado do deck)
     *
     * @param carta: carta em que houve o clique
     */
    public static void cartaClicadaPilhaAberta(Carta carta)
    {
        // informa que a ultima atividade nao tera sido em coluna, pois esta sendo na pilha aberta
        ult_coluna = null;

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
     * Metodo para gerenciar os cliques nas cartas dos montes definitivos
     *
     * @param carta: carta em que houve o clique
     */
    public static void cartaClicadaMonte(Carta carta)
    {
        // informa que a ultima atividade nao tera sido em coluna, pois esta sendo em um monte definitivo
        ult_coluna = null;

        // a carta foi clicada novamente, entao desfaz a selecao
        if (carta.getSelecionada())
        {
            limpaSelecionadas();
        }

        // se houver apenas uma carta selecionada, tenta encaixa-la no monte definitivo
        else if (cartasSelecionadas.getTamanho() == 1)
        {
            // regra: a carta selecionada deve ser do mesmo naipe e um numero acima da carta clicada
            if (cartasSelecionadas.getUltimoElemento().getCarta().getNaipe() == carta.getNaipe()
                    && cartasSelecionadas.getUltimoElemento().getCarta().getNumero() == carta.getNumero() + 1)
            {
                Monte destino = null;

                // localiza o monte em que a carta foi clicada e define-o como destino
                for (int i = 0; i < NUM_MONTES; i++)
                {
                    if (cartasSelecionadas.getUltimoElemento().getCarta().getNaipe() == montes[i].getNaipe())
                    {
                        destino = montes[i];
                    }
                }

                adicionarSelecionadasApilha(origem, destino);

                // confirma se este monte ja esta completo
                if (destino != null && destino.getUltimoElemento().getCarta().getNumero() == 13)
                {
                    destino.setCompleto(true);
                }

                // confirma se todos montes estao completos e, se for o caso, encerra o jogo com vitoria
                for (int i = 0; i < NUM_MONTES; i++)
                {
                    if (i == 0 && montes[0].getCompleto())
                    {
                        venceu = true;
                    }
                    else
                    {
                        venceu = venceu && montes[i].getCompleto();
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

            for (int i = 0; i < NUM_MONTES; i++)
            {
                if (carta.getNaipe() == montes[i].getNaipe())
                {
                    origem = montes[i];
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
        montesLayout = (LinearLayout) findViewById(R.id.montesLayout);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        deck = (Deck) findViewById(R.id.deck);
        pilhaAberta = (PilhaAberta) findViewById(R.id.open);
        bt_novoJogo = (Button) findViewById(R.id.bt_novoJogo);
        tx_venceu = (TextView) findViewById(R.id.tx_venceu);

        // Cria o vetor dos MONTES
        for (int i = 0; i < NUM_MONTES; i++)
        {
            montes[i] = (Monte) montesLayout.getChildAt(i);
            montes[i].setNaipe(Carta.naipes[i]);
            montes[i].setOnClickListener(new MonteClick());

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

            montes[i].setBackground(naipe_fundo);
        }

        // Cria o vetor das COLUNAS
        for (int i = 0; i < NUM_COLUNAS; i++)
        {
            colunas[i] = (Coluna) mainLayout.getChildAt(i);
            colunas[i].setPosicao(i);
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

        // inicia os montes
        for (int i = 0; i < NUM_MONTES; i++)
        {
            montes[i].reset();
        }

        // inicia as colunas
        for (int i = 0; i < NUM_COLUNAS; i++)
        {
            colunas[i].reset();
        }

        // reseta cartas selecionadas
        limpaSelecionadas();

        tx_venceu.setVisibility(View.GONE);
    }

    private void distribuirCartas()
    {
        int       primeiraColuna = 0;
        final int a_distribuir   = 28;
        int       distribuidas   = 0;
        Carta     carta;

        for (int i = 0; distribuidas < a_distribuir; i++)
        {
            // Retira uma carta do deck
            carta = deck.desempilha();

            // se for a ultima da coluna, torne-a visivel
            if (i == primeiraColuna)
            {
                carta.setImagem(null);
            }

            // insere a carta na coluna
            colunas[i].empilha(carta);

            distribuidas++;

            // se for a ultima coluna, reinicia a distribuicao pela primeira coluna (em forma de escadinha)
            if (i == NUM_COLUNAS - 1)
            {
                primeiraColuna++;
                i = primeiraColuna - 1;
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
     *  Classe para gerenciar os cliques no monte vazio
     */
    private class MonteClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Monte monte = (Monte) v;

            // se houver apenas uma carta selecionada, tenta encaixa-la no monte definitivo
            if (cartasSelecionadas.getTamanho() == 1)
            {
                if (monte.getTamanho() == 0
                        && cartasSelecionadas.getUltimoElemento().getCarta().getNaipe() == monte.getNaipe()
                        && cartasSelecionadas.getUltimoElemento().getCarta().getNumero() == 1)
                {
                    adicionarSelecionadasApilha(origem, monte);
                }

                limpaSelecionadas();
            }
        }
    }
}