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
public class Jogador extends Agent{
    @Override
   protected void setup() 
   {
    	addBehaviour(new comportamentos.ReceberMensagemJogador(this));
   }
}
