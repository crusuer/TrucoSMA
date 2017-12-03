/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comportamentos;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import utils.Carta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceberMensagemJogador extends Behaviour {

    Random gerador = new Random();
    List<Carta> cartas = new ArrayList<>();
    public ReceberMensagemJogador(Agent a) {
        super(a);
    }

    @Override
    public void action() {        
        //MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage msg = myAgent.blockingReceive();

        if (msg != null) {
            ACLMessage reply = msg.createReply();
            //String content = msg.getContent();
            String tipoMensagem = msg.getOntology();
            if(tipoMensagem.equalsIgnoreCase("Iniciar")){
                try {
                    cartas.clear();
                    Object[] carts = (Object[]) msg.getContentObject();
                    ordenarCartas((Carta) carts[0],(Carta) carts[1],(Carta) carts[2]);
                } catch (UnreadableException ex) {
                    Logger.getLogger(ReceberMensagemJogador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(tipoMensagem.equalsIgnoreCase("Jogada")){
                try {
                    int [] nums = (int[]) msg.getContentObject();
                    decisao(cartas, reply, nums[0], nums[1], nums[2], nums[3], nums[4], nums[5], nums[6], nums[7]);
                } catch (UnreadableException ex) {
                    Logger.getLogger(ReceberMensagemJogador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            
            /*if (content.equalsIgnoreCase("Fogo")) {
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent("Recebi seu aviso! Obrigado por auxiliar meu servico");
                myAgent.send(reply);
                System.out.println("O agente " + msg.getSender().getName() + " avisou de um incendio");
                System.out.println("Vou ativar os procedimentos de combate ao incendio!");
            }*/
        }        
        
        /*
        Object[] args = myAgent.getArguments();
        Carta carta1 = (Carta) args[0];
        Carta carta2 = (Carta) args[1];
        Carta carta3 = (Carta) args[2];
        List<Carta> cartas = new ArrayList<>();
        cartas.add(carta1);
        cartas.add(carta2);
        cartas.add(carta3);

        //Carta cartaEscolhida = escolherCarta(cartas);
        decisao(cartas, 1, 4, 1, 13, 14, 0, 0, false);
        decisao(cartas, 2, 1, 10, 0, 14, 0, 0, false);*/
        
    }

    @Override
    public boolean done() {
        return false;
    }
    
    @Override
    public int onEnd() {
        //TODO limpar parâmetros
        return 1;
    }

    private void decisao(List<Carta> cartas, ACLMessage reply, int rodada, int jogador, int jogadorGanhando, int valorGanhando, int maiorValorPossivel, int vencedorPrimeiraRodada, int vencedorSegundaRodada, int meuTimeTrucou) {
        boolean parceiroGanhando = (jogador == 3 && jogadorGanhando == 1) || (jogador == 4 && jogadorGanhando == 2);
        boolean blefar = false;
        boolean parceiroCartaBoa = false;
        switch (rodada) {
            //primeira rodada
            case 1:
                if (gerador.nextDouble() > 0.9) {
                    if (possoTrucar(meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel)) {
                        System.out.println("Truco aleatório");
                    }
                }
                switch (jogador) {
                    case 1:
                        System.out.println(cartas.remove(gerador.nextInt(3)).toString());
                        break;
                    case 2:
                    case 3:
                        if (cartas.get(2).getValor() < valorGanhando) {
                            System.out.println(cartas.remove(0).toString());                        
                        } else if (parceiroGanhando == true && valorGanhando > 7) {
                            System.out.println(cartas.remove(0).toString());
                        } else {
                            System.out.println(cartas.remove(2).toString());
                        }
                        break;
                    case 4:
                        if (parceiroGanhando == true) {
                            responderCarta(0,reply);
                        } else if (cartas.get(2).getValor() < valorGanhando) {
                            System.out.println(cartas.remove(0).toString());
                        } else {
                            if (cartas.get(1).getValor() < valorGanhando) { 
                                if (possoTrucar(meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel)) {
                                    System.out.println("Truco!");
                                }
                            } else if(cartas.get(1).getValor() > 7){
                                if (possoTrucar(meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel)) {
                                    System.out.println("Truco!");
                                }
                            }
                            System.out.println(cartas.remove(2));
                        }
                        break;
                }
                break;
            //segunda rodada
            case 2:
                if (gerador.nextDouble() > 0.9) {
                    if (possoTrucar(meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel)) {
                        System.out.println("Truco aleatório");
                    }
                }
                if (vencedorPrimeiraRodada == 0) {
                    if (cartaBoa(cartas) == true && possoTrucar(meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel)) {
                        System.out.println("Truco!");
                    }
                    System.out.println(cartas.remove(1).toString());
                } else if (vencedorPrimeiraRodada > 0) {
                    if (jogador == 1) {
                        comunicar();
                        if (cartaBoa(cartas) == true) {
                            if (possoTrucar(meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel)) {
                                System.out.println("Truco!");
                            } 
                            System.out.println(cartas.remove(1).toString());
                            
                        } else {
                            System.out.println(cartas.remove(gerador.nextInt(2)).toString());
                        }
                    } else {
                        if (blefar == true) {
                            if (possoTrucar(meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel)) {
                                System.out.println("Truco!");
                            }
                        }
                        if (parceiroGanhando == true && valorGanhando > 8) {
                            System.out.println(cartas.remove(0).toString());
                        } else if (cartas.get(1).getValor() >= valorGanhando) {
                            if (possoTrucar(meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel)) {
                                System.out.println("Truco!");
                            }
                            System.out.println(cartas.remove(1).toString());
                        } else {
                            System.out.println(cartas.remove(0).toString());
                        }
                    }
                } else if (jogador == 2) {
                    comunicar();
                    parceiroCartaBoa = false;
                    if (cartaBoa(cartas) == false) {
                        System.out.println(cartas.remove(gerador.nextInt(2)).toString());
                    } else {
                        if (parceiroCartaBoa == true) {
                            if (possoTrucar(meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel)) {
                                System.out.println("Truco!");
                            }
                        }                        
                        System.out.println(cartas.remove(1).toString());
                    }
                } else { //if (jogador == 4)
                    if (blefar == true) {
                        if (possoTrucar(meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel)) {
                            System.out.println("Truco!");
                        } 
                        System.out.println(cartas.remove(1).toString());
                    } else if (parceiroGanhando == true) {
                        System.out.println(cartas.remove(0).toString());
                    } else if (cartas.get(0).getValor() > valorGanhando) {
                        if (cartas.get(1).getValor() > 7) {
                            if (possoTrucar(meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel)) {
                                System.out.println("Truco!");
                            }
                        }
                        System.out.println(cartas.remove(0).toString());
                    } else if (cartas.get(1).getValor() <= valorGanhando) {
                        System.out.println(cartas.remove(gerador.nextInt(2)).toString());                        
                    }
                    else if (cartas.get(0).getValor() < 8) {
                        System.out.println(cartas.remove(1).toString());                        
                    } 
                    else{
                        if (possoTrucar(meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel)) {
                            System.out.println("Truco!");
                        } 
                        System.out.println(cartas.remove(1).toString());
                    }
                }
                break;
            //terceira rodada
            case 3:
                if (gerador.nextDouble() > 0.9) {
                    if (possoTrucar(meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel)) {
                        System.out.println("Truco aleatório");
                    }
                }
                if (jogador == 4 && cartas.get(0).getValor() > valorGanhando) {
                    if (possoTrucar(meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel)) {
                        System.out.println("Truco!");
                    }                    
                } else {
                    double soma = 0;
                    if (jogador == 2 || jogador == 3) {
                        soma += 0.1;
                    }
                    comunicar();
                    parceiroCartaBoa = false;
                    if (parceiroCartaBoa == true) {
                        soma += 0.6;
                    }
                    if (gerador.nextDouble() > (1 - soma)) {
                        if (possoTrucar(meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel)) {
                            System.out.println("Truco!");
                        }
                    }
                }
                System.out.println(cartas.remove(0).toString());
                break;
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
    
    private void responderCarta(int carta, ACLMessage reply){
        try {
            reply.setPerformative(ACLMessage.INFORM);
            reply.setOntology("Carta");
            Carta resp = cartas.remove(carta);
            reply.setContentObject(resp);
            myAgent.send(reply);
            //System.out.println("O "+reply.getSender().getLocalName()+" colocou a carta " + resp + " na mesa");
        } catch (IOException ex) {
            Logger.getLogger(ReceberMensagemJogador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private void comunicar(){
        System.out.println("Comunicar!");
    }
    private boolean receberComunicado(boolean aceitar,List<Carta> cartas){
        if(aceitar || gerador.nextDouble() < 0.8){
            return cartaBoa(cartas);
        }
        else{ 
            return !cartaBoa(cartas);
        }
    }

    private boolean possoTrucar(int meuTimeTrucou, boolean parceiroGanhando, int vencedorPrimeiraRodada, int vencedorSegundaRodada, int valorGanhando, int maiorValorPossivel) {
        return !(meuTimeTrucou == 1 || (parceiroGanhando == false && valorGanhando == maiorValorPossivel && (vencedorPrimeiraRodada == -1 || vencedorSegundaRodada == -1)));
    }
    private boolean aceitarTruco(int rodada, List<Carta> cartas,int vencedorPrimeiraRodada, int vencedorSegundaRodada, int jogador, int jogadorGanhando, int valorGanhando, int quemTrucou) {
        comunicar();
        boolean parceiroCartaBoa = false;
        boolean tenhoCartaBoa = cartaBoa(cartas);
        boolean parceiroGanhando = (jogador == 3 && jogadorGanhando == 1) || (jogador == 4 && jogadorGanhando == 2);
        boolean oponenteCartaBoa = false;
        double soma = 0;
        
        //TODO ignorar cartas na rodada 2 em certas circustancias
        if(parceiroCartaBoa){
            soma += 0.4;
        }
        if(tenhoCartaBoa){
            soma += 0.6;
        }
        if(vencedorPrimeiraRodada > 0 || vencedorSegundaRodada > 0){
            soma += 0.3;
            if(vencedorPrimeiraRodada > 0){
                soma += 0.1;
            } else if(vencedorPrimeiraRodada < 0){
                soma -= 0.1;
            }
                
        }
        if((parceiroGanhando || jogador == jogadorGanhando) && valorGanhando > 8){
            soma += 0.7;
        }
        if((!parceiroGanhando && jogador != jogadorGanhando) && valorGanhando > 8){
            soma -= 0.7;
        }
        if(vencedorPrimeiraRodada < 0 || vencedorSegundaRodada < 0){
            soma -= 0.3;
        }
        if(oponenteCartaBoa){
            soma -= 0.2;
        }        
        return gerador.nextDouble() > (1 - soma);
    }
    private void ordenarCartas(Carta carta1, Carta carta2, Carta carta3) {
        if ((carta1.compareTo(carta2) < 0) && (carta1.compareTo(carta3) < 0)) {
            if (carta2.compareTo(carta3) < 0) {
                cartas.add(carta1);
                cartas.add(carta2);
                cartas.add(carta3);
            } else {
                cartas.add(carta1);
                cartas.add(carta3);
                cartas.add(carta2);
            }
        } else if ((carta2.compareTo(carta1) < 0) && (carta2.compareTo(carta3) < 0)) {
            if (carta1.compareTo(carta3) < 0) {
                cartas.add(carta2);
                cartas.add(carta1);
                cartas.add(carta3);
            } else {
                cartas.add(carta2);
                cartas.add(carta3);
                cartas.add(carta1);
            }
        } else {
            if (carta1.compareTo(carta2) < 0) {
                cartas.add(carta3);
                cartas.add(carta1);
                cartas.add(carta2);
            } else {
                cartas.add(carta3);
                cartas.add(carta2);
                cartas.add(carta1);
            }
        }
    }
}
