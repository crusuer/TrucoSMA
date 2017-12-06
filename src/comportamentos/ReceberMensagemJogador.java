/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comportamentos;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
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
            if(reply.getPerformative() == ACLMessage.SUBSCRIBE){
                try {
                    cartas.clear();
                    Object[] carts = (Object[]) msg.getContentObject();
                    ordenarCartas((Carta) carts[0],(Carta) carts[1],(Carta) carts[2]);
                } catch (UnreadableException ex) {
                    Logger.getLogger(ReceberMensagemJogador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(reply.getPerformative() == ACLMessage.INFORM){
                try {
                    int [] nums = (int[]) msg.getContentObject();
                    //System.out.println(nums[0] +", "+ nums[1] +", "+ nums[2] +", "+ nums[3] +", "+ nums[4] +", "+ nums[5] +", "+ nums[6] +", "+ nums[7]);
                    decisao(cartas, reply, nums[0], nums[1], nums[2], nums[3], nums[4], nums[5], nums[6], nums[7]);
                } catch (UnreadableException ex) {
                    Logger.getLogger(ReceberMensagemJogador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
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
                    trucar(reply,meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel);
                }
                switch (jogador) {
                    case 1:
                        responderCarta(gerador.nextInt(3),reply);
                        break;
                    case 2:
                    case 3:
                        if (cartas.get(2).getValor() < valorGanhando) {
                            responderCarta(0,reply);                        
                        } else if (parceiroGanhando == true && valorGanhando > 7) {
                            responderCarta(0,reply);
                        } else {
                            responderCarta(2,reply);
                        }
                        break;
                    case 4:
                        if (parceiroGanhando == true) {
                            responderCarta(0,reply);
                        } else if (cartas.get(2).getValor() < valorGanhando) {
                            responderCarta(0,reply);
                        } else {
                            if (cartas.get(1).getValor() < valorGanhando) { 
                                trucar(reply,meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel);
                            } else if(cartas.get(1).getValor() > 7){
                                trucar(reply,meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel);
                            }
                            responderCarta(2,reply);
                        }
                        break;
                }
                break;
            //segunda rodada
            case 2:
                if (gerador.nextDouble() > 0.9) {
                    trucar(reply,meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel);
                }
                if (vencedorPrimeiraRodada == 0) {
                    if (cartaBoa(cartas) == true) {
                        trucar(reply, meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel);
                    }
                    responderCarta(1,reply);
                } else if (vencedorPrimeiraRodada > 0) {
                    if (jogador == 1) {
                        comunicar(reply);
                        if (cartaBoa(cartas) == true) {
                            trucar(reply,meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel);
                            responderCarta(1,reply);
                            
                        } else {
                            responderCarta(gerador.nextInt(2),reply);
                        }
                    } else {
                        if (blefar == true) {
                            trucar(reply,meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel);
                        }
                        if (parceiroGanhando == true && valorGanhando > 8) {
                            responderCarta(0,reply);
                        } else if (cartas.get(1).getValor() >= valorGanhando) {
                            trucar(reply,meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel);
                            responderCarta(1,reply);
                        } else {
                            responderCarta(0,reply);
                        }
                    }
                } else if (jogador == 2) {
                    comunicar(reply);
                    parceiroCartaBoa = false;
                    if (cartaBoa(cartas) == false) {
                        responderCarta(gerador.nextInt(2),reply);
                    } else {
                        if (parceiroCartaBoa == true) {
                            trucar(reply,meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel);
                        }                        
                        responderCarta(1,reply);
                    }
                } else { //if (jogador == 4)
                    if (blefar == true) {
                        trucar(reply,meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel);
                        responderCarta(1,reply);
                    } else if (parceiroGanhando == true) {
                        responderCarta(0,reply);
                    } else if (cartas.get(0).getValor() > valorGanhando) {
                        if (cartas.get(1).getValor() > 7) {
                            trucar(reply,meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel);
                        }
                        responderCarta(0,reply);
                    } else if (cartas.get(1).getValor() <= valorGanhando) {
                        responderCarta(gerador.nextInt(2),reply);                        
                    }
                    else if (cartas.get(0).getValor() < 8) {
                        responderCarta(1,reply);                        
                    } 
                    else{
                        trucar(reply,meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel);
                        responderCarta(1,reply);
                    }
                }
                break;
            //terceira rodada
            case 3:
                if (gerador.nextDouble() > 0.9) {
                    trucar(reply,meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel);
                }
                if (jogador == 4 && cartas.get(0).getValor() > valorGanhando) {
                    trucar(reply,meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel);                  
                } else {
                    double soma = 0;
                    if (jogador == 2 || jogador == 3) {
                        soma += 0.1;
                    }
                    comunicar(reply);
                    parceiroCartaBoa = false;
                    if (parceiroCartaBoa == true) {
                        soma += 0.6;
                    }
                    if (gerador.nextDouble() > (1 - soma)) {
                        trucar(reply,meuTimeTrucou, parceiroGanhando, vencedorPrimeiraRodada, vencedorSegundaRodada, valorGanhando, maiorValorPossivel);
                    }
                }
                responderCarta(0,reply);
                break;
        }
    }

    private boolean cartaBoa(List<Carta> cartas) {
        /*for (Carta carta : cartas) {
            if (carta.getValor() > 8) {
                return true;
            }
        }
        return false;*/
        return cartas.stream().anyMatch((carta) -> (carta.getValor() > 8));
    }
    
    private void responderCarta(int carta, ACLMessage reply){
        try {
            reply.setPerformative(ACLMessage.INFORM);
            reply.setContentObject(cartas.remove(carta));
            myAgent.send(reply);
            //System.out.println("O "+reply.getSender().getLocalName()+" colocou a carta " + resp + " na mesa");
        } catch (IOException ex) {
            Logger.getLogger(ReceberMensagemJogador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void comunicar(ACLMessage reply){
        reply.setPerformative(ACLMessage.REQUEST);
        //reply.setOntology("Comunicar");
        myAgent.send(reply);
    }
    private boolean receberComunicado(boolean aceitar,List<Carta> cartas){
        if(aceitar || gerador.nextDouble() < 0.8){
            return cartaBoa(cartas);
        }
        else{ 
            return !cartaBoa(cartas);
        }
    }

    private void trucar(ACLMessage reply,int meuTimeTrucou, boolean parceiroGanhando, int vencedorPrimeiraRodada, int vencedorSegundaRodada, int valorGanhando, int maiorValorPossivel) {
        /*if(!(meuTimeTrucou == 1 || (parceiroGanhando == false && valorGanhando == maiorValorPossivel && (vencedorPrimeiraRodada == -1 || vencedorSegundaRodada == -1)))){
            responderTruco(reply);
        }
        myAgent.blockingReceive(); */       
    }
    private void responderTruco(ACLMessage reply) {
        reply.setPerformative(ACLMessage.PROPOSE);
        myAgent.send(reply);
    }
    private boolean aceitarTruco(ACLMessage reply,int rodada, List<Carta> cartas,int vencedorPrimeiraRodada, int vencedorSegundaRodada, int jogador, int jogadorGanhando, int valorGanhando, int quemTrucou) {
        comunicar(reply);
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
