/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comportamentos;


import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceberMensagemJuiz extends CyclicBehaviour 
{   
    public ReceberMensagemJuiz(Agent a) 
    {
       super(a);
    }
      
    @Override
    public void action() 
    {
       ACLMessage msg = myAgent.receive();
       if(msg != null)
       {
          String content = msg.getContent();
          System.out.println("--> " + msg.getSender().getName() + ": " + content);
       }
       else 
       /*
       Com o block() bloqueamos o comportamento at� que uma nova 
       mensagem chegue ao agente e assim evitamos consumir ciclos
       da CPU.
       */
           block();
    }
}

