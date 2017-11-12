/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comportamentos;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ReceberMensagemJogador extends CyclicBehaviour 
{
    public ReceberMensagemJogador(Agent a) 
    {
       super(a);
    }
      
    @Override
    public void action() 
    {
        ACLMessage msg = myAgent.receive();
           
        if(msg != null) 
        {
            ACLMessage reply = msg.createReply();
            String content = msg.getContent();
            if(content.equalsIgnoreCase("Fogo")) 
            {
               reply.setPerformative(ACLMessage.INFORM);
               reply.setContent("Recebi seu aviso! Obrigado por auxiliar meu servico");
               myAgent.send(reply);
               System.out.println("O agente "+ msg.getSender().getName() +" avisou de um incendio");
               System.out.println("Vou ativar os procedimentos de combate ao incendio!"); 
            }  
        } 
        else
            block();
    }
}
