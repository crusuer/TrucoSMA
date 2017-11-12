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

public class EnviarMensagemJuiz extends OneShotBehaviour 
{   
    public EnviarMensagemJuiz(Agent a) 
    {
       super(a);
    }
      
    @Override
    public void action() 
    {
       ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
       msg.addReceiver(new AID("Jogador1",AID.ISLOCALNAME));
       msg.setLanguage("Portugues");
       msg.setOntology("Emergencia");
       msg.setContent("Fogo");
       myAgent.send(msg);
    }
}
