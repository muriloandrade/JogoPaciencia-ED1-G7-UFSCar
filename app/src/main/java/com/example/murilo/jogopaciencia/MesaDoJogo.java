package com.example.murilo.jogopaciencia;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class MesaDoJogo extends ActionBarActivity
{

    private static final int NUM_COLUNAS = 7;
    private static final int NUM_MONTES  = 4;

    public static ArrayList<Carta> cartasSelecionadas = new ArrayList<>(13);
    public static Coluna           ult_coluna         = null;
    private static PilhaAberta pilhaAberta;
    private static Monte[]  montes  = new Monte[NUM_MONTES];
    private static Coluna[] colunas = new Coluna[NUM_COLUNAS];
    private static PilhaBase    origem;
    private        Deck         deck;
    private        LinearLayout montesLayout;
    private        LinearLayout mainLayout;
    private        Button       bt_novoJogo;
    private static TextView     tx_venceu;

    private static boolean venceu = false;

    public static void cartaClicadaColuna(Coluna destino, Carta carta)
    {
        if (cartasSelecionadas.size() == 0)
        {
            origem = destino;
        }

        if (carta == null)
        {
            if (cartasSelecionadas.size() > 0 && cartasSelecionadas.get(0).getNumero() == 13)
            {
                adicionarSelecionadasApilha(origem, destino);
            }
            limpaSelecionadas();
        }
        else if (cartasSelecionadas.size() == 1 && carta == cartasSelecionadas.get(0))
        {
            limpaSelecionadas();
        }
        else if (!carta.estaMostrandoFrente() && carta == destino.getCartaTopo())
        {
            carta.setImagem(null);
            limpaSelecionadas();
        }
        else
        {
            if (ult_coluna != destino && cartasSelecionadas.size() > 0)
            {

                if (carta == destino.getCartaTopo() && cartasSelecionadas.get(0).getCor() != carta.getCor() && cartasSelecionadas.get(0).getNumero() == carta.getNumero() - 1)
                {
                    adicionarSelecionadasApilha(origem, destino);
                }
                limpaSelecionadas();
            }
            else
            {

                limpaSelecionadas();

                boolean achou = false;

                for (int i = 0; i < destino.getCartas().size(); i++)
                {
                    if (carta == destino.getCartas().get(i))
                    {
                        achou = true;
                    }

                    if (achou && destino.getCartas().get(i).estaMostrandoFrente())
                    {
                        cartasSelecionadas.add(destino.getCartas().get(i));
                        cartasSelecionadas.get(cartasSelecionadas.size() - 1).selecionar(true);
                    }
                }
            }

            ult_coluna = destino;
        }
    }

    public static void cartaClicadaPilhaAberta(Carta carta)
    {
        ult_coluna = null;

        if (carta.estaSelecionada())
        {
            limpaSelecionadas();
        }
        else
        {
            limpaSelecionadas();
            origem = pilhaAberta;
            carta.selecionar(true);
            cartasSelecionadas.add(carta);
        }
    }

    public static void cartaClicadaMonte(Carta carta)
    {
        ult_coluna = null;

        if (carta.estaSelecionada())
        {
            limpaSelecionadas();
        }
        else if (cartasSelecionadas.size() == 1)
        {
            if (cartasSelecionadas.get(0).getNaipe() == carta.getNaipe() && cartasSelecionadas.get(0).getNumero() == carta.getNumero() + 1)
            {
                Monte destino = null;

                for (int i = 0; i < NUM_MONTES; i++)
                {
                    if (cartasSelecionadas.get(0).getNaipe() == montes[i].getNaipe())
                    {
                        destino = montes[i];
                    }
                }

                adicionarSelecionadasApilha(origem, destino);

                if (destino.getCartaTopo().getNumero() == 13)
                {
                    destino.setCompleto(true);
                }

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
            carta.selecionar(true);
            cartasSelecionadas.add(carta);
        }

    }

    private static void limpaSelecionadas()
    {
        while (cartasSelecionadas.size() > 0)
        {
            cartasSelecionadas.get(0).selecionar(false);
            cartasSelecionadas.remove(0);
        }
    }

    private static void adicionarSelecionadasApilha(PilhaBase origem, PilhaBase destino)
    {
        do
        {
            cartasSelecionadas.get(0).selecionar(false);
            origem.retirarDoTopo(); // nao necessariamente a mesma
            origem.removeView(cartasSelecionadas.get(0));
            destino.inserirNoTopo(cartasSelecionadas.get(0));
            cartasSelecionadas.remove(0);
        }
        while (cartasSelecionadas.size() > 0);
    }

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
            carta = deck.retirarDoTopo();
            deck.removeView(carta);

            // se for a ultima da coluna, torne-a visivel
            if (i == primeiraColuna)
            {
                carta.setImagem(null);
            }

            // insere a carta na coluna
            colunas[i].inserirNoTopo(carta);

            distribuidas++;

            // se for a ultima coluna, reinicia a distribuicao pela primeira coluna (em forma de escadinha)
            if (i == NUM_COLUNAS - 1)
            {
                primeiraColuna++;
                i = primeiraColuna - 1;
            }
        }
    }

    /*
     *  Classe para gerenciar os clicks no Deck
     */
    private class DeckClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Carta cartaTopoDeck = deck.retirarDoTopo();
            deck.removeView(cartaTopoDeck);

            if (cartaTopoDeck != null)
            {
                pilhaAberta.inserirNoTopo(cartaTopoDeck);
            }
            else
            {
                for (int i = pilhaAberta.cartas.size() - 1; i >= 0; i--)
                {
                    Carta cartaVoltarDeck;
                    cartaVoltarDeck = pilhaAberta.retirarDoTopo();
                    pilhaAberta.removeView(cartaVoltarDeck);
                    cartaVoltarDeck.setOnClickListener(null);
                    deck.inserirNoTopo(cartaVoltarDeck);
                }
            }
        }
    }

    /*
 *  Classe para gerenciar os clicks nos montes
 */
    private class MonteClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Monte monte = (Monte) v;

            if (cartasSelecionadas.size() == 1)
            {
                if (monte.getCartas().size() == 0
                        && cartasSelecionadas.get(0).getNaipe() == monte.getNaipe()
                        && cartasSelecionadas.get(0).getNumero() == 1)
                {
                    adicionarSelecionadasApilha(origem, monte);
                }

                limpaSelecionadas();
            }
        }
    }
}