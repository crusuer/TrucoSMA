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
import java.util.logging.Level;
import java.util.logging.Logger;
import projetosmatruco.ProjetoSMATruco;
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
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("Jogador1", AID.ISLOCALNAME));
        msg.setLanguage("Portugues");
        msg.setOntology("Iniciar");
        try {
            Carta carta1 = new Carta(4, ProjetoSMATruco.Naipes.Paus);
            Carta carta2 = new Carta(3, ProjetoSMATruco.Naipes.Ouro);
            Carta carta3 = new Carta(7, ProjetoSMATruco.Naipes.Espada);
            msg.setContentObject(new Object[]{carta1, carta2, carta3});
        } catch (IOException ex) {
            Logger.getLogger(EnviarMensagemJuiz.class.getName()).log(Level.SEVERE, null, ex);
        }
        myAgent.send(msg);
    }
    private void jogada() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("Jogador1", AID.ISLOCALNAME));
        msg.setLanguage("Portugues");
        msg.setOntology("Jogada");
        try {
            int[] lista = {1, 4, 2, 13, 14, 0, 0, 0};
            msg.setContentObject(lista);
        } catch (IOException ex) {
            Logger.getLogger(EnviarMensagemJuiz.class.getName()).log(Level.SEVERE, null, ex);
        }
        myAgent.send(msg);
    }
}
