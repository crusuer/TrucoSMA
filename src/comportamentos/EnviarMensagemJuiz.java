/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comportamentos;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projetosmatruco.ProjetoSMATruco.Naipes;
import utils.Carta;

public class EnviarMensagemJuiz extends OneShotBehaviour 
{   
    public EnviarMensagemJuiz(Agent a) 
    {
       super(a);
    }
      
    @Override
    public void action() 
    {
        iniciarPartida();
        jogada();
    }
    private void iniciarPartida(){
        List<Carta> lista = embaralhar();
                
        mensagemInicio(1,lista);
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID("Jogador2", AID.ISLOCALNAME));
            msg.setOntology("Iniciar");
            Carta carta1 = new Carta(4, Naipes.Paus);
            Carta carta2 = new Carta(3, Naipes.Ouro);
            Carta carta3 = new Carta(7, Naipes.Espada);
            //lista.get(4), lista.get(5), lista.get(6)
            msg.setContentObject(new Object[]{carta1, carta2, carta3});
            myAgent.send(msg);
        } catch (IOException ex) {
            Logger.getLogger(EnviarMensagemJuiz.class.getName()).log(Level.SEVERE, null, ex);
        }
        mensagemInicio(3,lista);
        mensagemInicio(4,lista);
    }
    private void jogada() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("Jogador2", AID.ISLOCALNAME));
        msg.setOntology("Jogada");
        try {
            int[] lista = {1, 4, 2, 13, 14, 0, 0, 0};
            msg.setContentObject(lista);
        } catch (IOException ex) {
            Logger.getLogger(EnviarMensagemJuiz.class.getName()).log(Level.SEVERE, null, ex);
        }
        myAgent.send(msg);
    }
    private void mensagemInicio(int num, List<Carta> lista) {
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID("Jogador"+num, AID.ISLOCALNAME));
            msg.setOntology("Iniciar");
            msg.setContentObject(new Object[]{lista.get(1+(num-1)*3), lista.get(2+(num-1)*3), lista.get(3+(num-1)*3)});
            myAgent.send(msg);
        } catch (IOException ex) {
            Logger.getLogger(EnviarMensagemJuiz.class.getName()).log(Level.SEVERE, null, ex);
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
