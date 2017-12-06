/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comportamentos;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projetosmatruco.ProjetoSMATruco.Naipes;
import utils.Carta;

public class ReceberMensagemJuiz extends CyclicBehaviour 
{   
    int rodada = 1;
    int jogador = 1;
    int jogadorGanhando = 10;
    int valorGanhando = 0;
    int maiorValorPossivel = 14;
    int vencedorPrimeiraRodada = 0;
    int vencedorSegundaRodada = 0;
    int meuTimeTrucou = 0;
    int real = 1;
    List<Integer> sairam = new ArrayList<>();
    public ReceberMensagemJuiz(Agent a) 
    {
       super(a);
    }
      
    @Override
    public void action() 
    {
        iniciarPartida();
        jogada(1);
        jogada(2);
        jogada(3);
        jogada(4);
        ACLMessage msg1 = myAgent.blockingReceive();
    }
    private void iniciarPartida(){
        List<Carta> lista = embaralhar();                
        
        mensagemInicio(1,lista);
        mensagemInicio(2,lista);
        mensagemInicio(3,lista);
        mensagemInicio(4,lista);
    }
    private void jogada(int num) {        
        try {
            ACLMessage send = new ACLMessage(ACLMessage.INFORM);
            send.addReceiver(new AID("Jogador" + num, AID.ISLOCALNAME));
            send.setLanguage("Portugues");
            int[] lista = {rodada, jogador, jogadorGanhando, valorGanhando, maiorValorPossivel, vencedorPrimeiraRodada, vencedorSegundaRodada, meuTimeTrucou};
            send.setContentObject(lista);
            myAgent.send(send);
        } catch (IOException ex) {
            Logger.getLogger(ReceberMensagemJuiz.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ACLMessage receive = myAgent.blockingReceive();
        if (receive != null) {
            if (receive.getPerformative() == ACLMessage.INFORM) {
                try {
                    Carta content = (Carta) receive.getContentObject();
                    sairam.add(content.getValor());
                    if(valorGanhando < content.getValor()){
                        jogadorGanhando = jogador;
                        valorGanhando = content.getValor();
                        real = jogador;
                    }
                    else if(valorGanhando == content.getValor()){
                        jogadorGanhando = 9;
                    }
                    if(maiorValorPossivel == content.getValor()){
                        maiorValorPossivel--;
                        while(sairam.contains(maiorValorPossivel)){
                           maiorValorPossivel--; 
                        }
                    }
                    System.out.println("--> " + receive.getSender().getName() + ": " + content);
                } catch (UnreadableException ex) {
                    Logger.getLogger(ReceberMensagemJuiz.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (receive.getPerformative() == ACLMessage.PROPOSE) {
                System.out.println("--> " + receive.getSender().getName() + ": Pediu truco!");
            }
        }
        if(jogador == 4){
            if(rodada == 1 && jogadorGanhando != 9){
                
            }
            rodada++;
            jogador = 0;
            jogadorGanhando = 10;
            valorGanhando = 0;
        }
        jogador++;
    }
    private void mensagemInicio(int num, List<Carta> lista) {
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.SUBSCRIBE);
            msg.addReceiver(new AID("Jogador"+num, AID.ISLOCALNAME));
            System.out.println("Jogador"+num+": (virada "+lista.get(0)+") "+lista.get(1+(num-1)*3)+", "+ lista.get(2+(num-1)*3)+", "+ lista.get(3+(num-1)*3));
            msg.setContentObject(new Object[]{lista.get(1+(num-1)*3), lista.get(2+(num-1)*3), lista.get(3+(num-1)*3)});
            myAgent.send(msg);
        } catch (IOException ex) {
            Logger.getLogger(ReceberMensagemJuiz.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private List<Carta> embaralhar() {
        List<Carta> lista = new ArrayList<>();
        lista.add(new Carta(4, Naipes.Ouro));
        lista.add(new Carta(4, Naipes.Espada));
        lista.add(new Carta(4, Naipes.Copas));
        lista.add(new Carta(4, Naipes.Paus));
        lista.add(new Carta(5, Naipes.Ouro));
        lista.add(new Carta(5, Naipes.Espada));
        lista.add(new Carta(5, Naipes.Copas));
        lista.add(new Carta(5, Naipes.Paus));
        lista.add(new Carta(6, Naipes.Ouro));
        lista.add(new Carta(6, Naipes.Espada));
        lista.add(new Carta(6, Naipes.Copas));
        lista.add(new Carta(6, Naipes.Paus));
        lista.add(new Carta(7, Naipes.Ouro));
        lista.add(new Carta(7, Naipes.Espada));
        lista.add(new Carta(7, Naipes.Copas));
        lista.add(new Carta(7, Naipes.Paus));
        lista.add(new Carta(8, Naipes.Ouro));
        lista.add(new Carta(8, Naipes.Espada));
        lista.add(new Carta(8, Naipes.Copas));
        lista.add(new Carta(8, Naipes.Paus));
        lista.add(new Carta(9, Naipes.Ouro));
        lista.add(new Carta(9, Naipes.Espada));
        lista.add(new Carta(9, Naipes.Copas));
        lista.add(new Carta(9, Naipes.Paus));
        lista.add(new Carta(10, Naipes.Ouro));
        lista.add(new Carta(10, Naipes.Espada));
        lista.add(new Carta(10, Naipes.Copas));
        lista.add(new Carta(10, Naipes.Paus));
        lista.add(new Carta(1, Naipes.Ouro));
        lista.add(new Carta(1, Naipes.Espada));
        lista.add(new Carta(1, Naipes.Copas));
        lista.add(new Carta(1, Naipes.Paus));
        lista.add(new Carta(2, Naipes.Ouro));
        lista.add(new Carta(2, Naipes.Espada));
        lista.add(new Carta(2, Naipes.Copas));
        lista.add(new Carta(2, Naipes.Paus));
        lista.add(new Carta(3, Naipes.Ouro));
        lista.add(new Carta(3, Naipes.Espada));
        lista.add(new Carta(3, Naipes.Copas));
        lista.add(new Carta(3, Naipes.Paus));
        Collections.shuffle(lista);
        /*for(Carta carta: lista){
        if(carta.getNumero() == lista.get(0).getNumero() % 10 + 1){
        carta.setValor(true);
        }
        }*/
        lista.stream().filter((carta) -> (carta.getNumero() == lista.get(0).getNumero() % 10 + 1)).forEach((carta) -> {
            carta.setValor(true);
        });
        return lista;
    }
}

