/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comportamentos;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import utils.Carta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReceberMensagemJogador extends CyclicBehaviour {

    Random gerador = new Random();

    public ReceberMensagemJogador(Agent a) {
        super(a);
    }

    @Override
    public void action() {
        Object[] args = myAgent.getArguments();
        Carta carta1 = (Carta) args[0];
        Carta carta2 = (Carta) args[1];
        Carta carta3 = (Carta) args[2];
        List<Carta> cartas = ordenarCartas(carta1, carta2, carta3);

        //Carta cartaEscolhida = escolherCarta(cartas);
        System.out.println(acao(cartas, 1, 1, 10, 0, 14,0,0,false));
        ACLMessage msg = myAgent.receive();

        if (msg != null) {
            ACLMessage reply = msg.createReply();
            String content = msg.getContent();
            if (content.equalsIgnoreCase("Fogo")) {
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent("Recebi seu aviso! Obrigado por auxiliar meu servico");
                myAgent.send(reply);
                System.out.println("O agente " + msg.getSender().getName() + " avisou de um incendio");
                System.out.println("Vou ativar os procedimentos de combate ao incendio!");
            }
        } else {
            block();
        }
    }

    private String acao(List<Carta> cartas, int rodada, int jogador, int jogadorGanhando, int valorGanhando, int maiorValorPossivel, int vencedorPrimeiraRodada, int vencedorSegundaRodada, boolean meuTimeTrucou) {
        boolean parceiroGanhando = (jogador+jogadorGanhando == 4 || jogador+jogadorGanhando == 6);
        boolean blefar = false;
        boolean parceiroCartaBoa = false;
        switch (rodada) {
            //primeira rodada
            case 1:
                if (gerador.nextDouble() > 0.9) {
                    if(possoTrucar(meuTimeTrucou,parceiroGanhando,vencedorPrimeiraRodada,vencedorSegundaRodada,valorGanhando, maiorValorPossivel)){
                        return "Truco aleatório";
                    }                    
                }
                switch (jogador) {
                    case 1:
                        return cartas.remove(gerador.nextInt(3)).toString();
                    case 2:
                    case 3:
                        if (parceiroGanhando == true && valorGanhando > 7) {
                            return cartas.remove(0).toString();
                        } else if (cartas.get(2).getValor() < valorGanhando) {
                            return cartas.remove(0).toString();
                        } else {
                            return cartas.remove(2).toString();
                        }
                    case 4:
                        if (parceiroGanhando == true) {
                            return cartas.remove(0).toString();
                        } else if (cartas.get(2).getValor() < valorGanhando) {
                            return cartas.remove(0).toString();
                        } else if (cartas.get(1).getValor() < valorGanhando) {
                            if(possoTrucar(meuTimeTrucou,parceiroGanhando,vencedorPrimeiraRodada,vencedorSegundaRodada,valorGanhando, maiorValorPossivel)){
                                return "Truco!";
                            }
                        } else if (cartas.get(1).getValor() > 7) {
                            if(possoTrucar(meuTimeTrucou,parceiroGanhando,vencedorPrimeiraRodada,vencedorSegundaRodada,valorGanhando, maiorValorPossivel)){
                                return "Truco!";
                            }
                        } else {
                            return cartas.remove(2).toString();
                        }
                }
            //segunda rodada
            case 2:
                if(vencedorPrimeiraRodada == 0){
                    if(cartaBoa(cartas) == true && possoTrucar(meuTimeTrucou,parceiroGanhando,vencedorPrimeiraRodada,vencedorSegundaRodada,valorGanhando, maiorValorPossivel)){
                        return "Truco!";
                    } else {
                        return cartas.remove(1).toString();
                    }
                } else if (vencedorPrimeiraRodada == 1){
                    if(jogador == 1){
                        System.out.println("Comunicar");
                        if(cartaBoa(cartas) == true){
                            if(possoTrucar(meuTimeTrucou,parceiroGanhando,vencedorPrimeiraRodada,vencedorSegundaRodada,valorGanhando, maiorValorPossivel)){
                                return "Truco!";
                            } else{
                                return cartas.remove(1).toString();
                            }
                        }
                        else{
                            return cartas.remove(gerador.nextInt(2)).toString();
                        }
                    } else{
                        if(blefar == true){
                            if(possoTrucar(meuTimeTrucou,parceiroGanhando,vencedorPrimeiraRodada,vencedorSegundaRodada,valorGanhando, maiorValorPossivel)){
                                return "Truco!";
                            } else{
                                if(parceiroGanhando == true && valorGanhando > 8){
                                    return cartas.remove(0).toString();
                                } else {
                                    if(cartas.get(1).getValor() > valorGanhando){
                                        return cartas.remove(1).toString();
                                    } else{
                                        return cartas.remove(0).toString();
                                    }
                                }
                            }
                        }
                    }
                } else{
                    if(jogador == 2){
                        System.out.println("Comunicar");
                        parceiroCartaBoa = false;
                        if(cartaBoa(cartas) == true){
                            return cartas.remove(gerador.nextInt(2)).toString();
                        } else if(parceiroCartaBoa == true){
                            if(possoTrucar(meuTimeTrucou,parceiroGanhando,vencedorPrimeiraRodada,vencedorSegundaRodada,valorGanhando, maiorValorPossivel)){
                                return "Truco!";
                            } else {
                                return cartas.remove(1).toString();
                            }
                        } else{
                            return cartas.remove(1).toString();
                        }
                    } else if(jogador == 4){
                        if(blefar == true){
                            if(possoTrucar(meuTimeTrucou,parceiroGanhando,vencedorPrimeiraRodada,vencedorSegundaRodada,valorGanhando, maiorValorPossivel)){
                                return "Truco!";
                            } else {
                                return cartas.remove(1).toString();
                            }
                        }
                    }
                }
            //terceira rodada
            case 3:
                if(cartaBoa(cartas) == true){
                    if(possoTrucar(meuTimeTrucou,parceiroGanhando,vencedorPrimeiraRodada,vencedorSegundaRodada,valorGanhando, maiorValorPossivel)){
                        return "Truco!";
                    } else{
                        return cartas.remove(0).toString();
                    }
                } else {
                    if(jogador == 4 && cartas.get(0).getValor() > valorGanhando){
                        if(possoTrucar(meuTimeTrucou,parceiroGanhando,vencedorPrimeiraRodada,vencedorSegundaRodada,valorGanhando, maiorValorPossivel)){
                            return "Truco!";
                        } else{
                            return cartas.remove(0).toString();
                        }
                    } else {
                        double soma = 0;
                        if(jogador == 2 || jogador == 3){
                            soma += 0.1;
                        }
                        System.out.println("Comunicar");
                        parceiroCartaBoa = false;
                        if(parceiroCartaBoa == true){
                            soma += 0.6;
                        }
                        if(gerador.nextDouble(); > (1-soma)){
                            if(possoTrucar(meuTimeTrucou,parceiroGanhando,vencedorPrimeiraRodada,vencedorSegundaRodada,valorGanhando, maiorValorPossivel)){
                                return "Truco!";
                            } else{
                                return cartas.remove(0).toString();
                            }
                        }
                        else {
                            return cartas.remove(0).toString();
                        }
                    }
                }
            default:
                return "";
        }
    }

    private boolean cartaBoa(List<Carta> cartas) {
        for (Carta carta : cartas) {
            if (carta.getValor() > 8) {
                return true;
            }
        }
        return false;
    }
    
    private boolean possoTrucar(boolean meuTimeTrucou, boolean parceiroGanhando, int vencedorPrimeiraRodada, int vencedorSegundaRodada, int valorGanhando, int maiorValorPossivel) {
        return !(meuTimeTrucou || (parceiroGanhando == false && valorGanhando == maiorValorPossivel && (vencedorPrimeiraRodada == -1 || vencedorSegundaRodada == -1)));
    }

    private List<Carta> ordenarCartas(Carta carta1, Carta carta2, Carta carta3) {
        List<Carta> cartasRetorno = new ArrayList<>();
        if ((carta1.compareTo(carta2) < 0) && (carta1.compareTo(carta3) < 0)) {
            cartasRetorno.add(carta1);
            if (carta2.compareTo(carta3) < 0) {
                cartasRetorno.add(carta2);
                cartasRetorno.add(carta3);
            } else {
                cartasRetorno.add(carta3);
                cartasRetorno.add(carta2);
            }
        } else if ((carta2.compareTo(carta1) < 0) && (carta2.compareTo(carta3) < 0)) {
            cartasRetorno.add(carta2);
            if (carta1.compareTo(carta3) < 0) {
                cartasRetorno.add(carta1);
                cartasRetorno.add(carta3);
            } else {
                cartasRetorno.add(carta3);
                cartasRetorno.add(carta1);
            }
        } else {
            cartasRetorno.add(carta3);
            if (carta1.compareTo(carta2) < 0) {
                cartasRetorno.add(carta1);
                cartasRetorno.add(carta2);
            } else {
                cartasRetorno.add(carta2);
                cartasRetorno.add(carta1);
            }
        }
        return cartasRetorno;
    }

    private Carta escolherCarta(List<Carta> cartas) {
        Carta carta;
        int rodada = 1;
        int jogador = 1;
        int valorMaiorRodada = 8;
        boolean parceiroGanhando = false;
        boolean rodadaAnteriorEmpatada = false;
        //rodada
        if (rodada == 1) {
            //primeiro a jogar
            if (jogador == 1) {
                carta = cartas.get(gerador.nextInt(3));
            } else //parceiro ja jogou
            {
                if (jogador > 2) {
                    //parceiro ganhando
                    if (parceiroGanhando) {
                        carta = cartas.get(0);
                    } else //carta maior que a mesa
                    {
                        if (cartas.get(0).getValor() > valorMaiorRodada || cartas.get(1).getValor() > valorMaiorRodada || cartas.get(2).getValor() > valorMaiorRodada) {
                            if (cartas.get(0).getValor() > valorMaiorRodada) {
                                carta = cartas.get(gerador.nextInt(3));
                            } else if (cartas.get(1).getValor() > valorMaiorRodada) {
                                carta = cartas.get(gerador.nextInt(2) + 1);
                            } else {
                                carta = cartas.get(2);
                            }
                        } else {
                            carta = cartas.get(2);
                        }
                    }
                } else //carta maior que a mesa
                {
                    if (cartas.get(0).getValor() > valorMaiorRodada || cartas.get(1).getValor() > valorMaiorRodada || cartas.get(2).getValor() > valorMaiorRodada) {
                        if (cartas.get(0).getValor() > valorMaiorRodada) {
                            carta = cartas.get(gerador.nextInt(3));
                        } else if (cartas.get(1).getValor() > valorMaiorRodada) {
                            carta = cartas.get(gerador.nextInt(2) + 1);
                        } else {
                            carta = cartas.get(2);
                        }
                    } else {
                        carta = cartas.get(2);
                    }
                }
            }
        } else //primeira rodada empatou
        {
            if (rodadaAnteriorEmpatada) {
                carta = cartas.get(2);
            } else //parceiro ja jogou
            {
                if (jogador > 2) {
                    //parceiro ganhando
                    if (parceiroGanhando) {
                        carta = cartas.get(0);
                    } else //carta maior que a mesa
                    {
                        if (cartas.get(0).getValor() > valorMaiorRodada || cartas.get(1).getValor() > valorMaiorRodada || cartas.get(2).getValor() > valorMaiorRodada) {
                            if (cartas.get(0).getValor() > valorMaiorRodada) {
                                carta = cartas.get(gerador.nextInt(3));
                            } else if (cartas.get(1).getValor() > valorMaiorRodada) {
                                carta = cartas.get(gerador.nextInt(2) + 1);
                            } else {
                                carta = cartas.get(2);
                            }
                        } else {
                            carta = cartas.get(2);
                        }
                    }
                } else //carta maior que a mesa
                {
                    if (cartas.get(0).getValor() > valorMaiorRodada || cartas.get(1).getValor() > valorMaiorRodada || cartas.get(2).getValor() > valorMaiorRodada) {
                        if (cartas.get(0).getValor() > valorMaiorRodada) {
                            carta = cartas.get(gerador.nextInt(3));
                        } else if (cartas.get(1).getValor() > valorMaiorRodada) {
                            carta = cartas.get(gerador.nextInt(2) + 1);
                        } else {
                            carta = cartas.get(2);
                        }
                    } else {
                        carta = cartas.get(2);
                    }
                }
            }
        }

        return carta;
    }
}
