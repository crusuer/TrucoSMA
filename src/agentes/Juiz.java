/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.Agent;

/**
 *
 * @author robson
 */
public class Juiz extends Agent{
    @Override
   protected void setup() 
   {       
      try
      {
         Thread.sleep(3000);
      }
      catch(Exception e)
      {
         System.out.println("Erro: " + e);
      } 
     
      addBehaviour(new comportamentos.EnviarMensagemJuiz(this));
      addBehaviour(new comportamentos.ReceberMensagemJuiz(this));     
   }
}
