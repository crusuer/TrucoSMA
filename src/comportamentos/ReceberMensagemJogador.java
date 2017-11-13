/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comportamentos;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import utils.Carta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReceberMensagemJogador extends CyclicBehaviour 
{
	Random gerador = new Random();
    public ReceberMensagemJogador(Agent a) 
    {
       super(a);
    }
      
    @Override
    public void action() 
    {
    	Object[] args = myAgent.getArguments();
    	Carta carta1 = (Carta) args[0];
    	Carta carta2 = (Carta) args[1];
    	Carta carta3 = (Carta) args[2];
    	List<Carta> cartas = ordenarCartas(carta1,carta2,carta3);

    	Carta cartaEscolhida = escolherCarta(cartas);

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
    private List<Carta> ordenarCartas(Carta carta1,Carta carta2,Carta carta3){
    	List<Carta> cartasRetorno = new ArrayList<Carta>();
    	if((carta1.getValor() < carta2.getValor()) && (carta1.getValor() < carta3.getValor())){
    		cartasRetorno.add(carta1);
    		if(carta2.getValor()<carta3.getValor()){
    			cartasRetorno.add(carta2);
    			cartasRetorno.add(carta3);
    		}
    		else{
    			cartasRetorno.add(carta3);
    			cartasRetorno.add(carta2);
    		}
    	} else if((carta2.getValor() < carta1.getValor()) && (carta2.getValor() < carta3.getValor())){
    		cartasRetorno.add(carta2);
    		if(carta1.getValor()<carta3.getValor()){
    			cartasRetorno.add(carta1);
    			cartasRetorno.add(carta3);
    		}
    		else{
    			cartasRetorno.add(carta3);
    			cartasRetorno.add(carta1);
    		}
    	} else {
    		cartasRetorno.add(carta3);
    		if(carta1.getValor()<carta2.getValor()){
    			cartasRetorno.add(carta1);
    			cartasRetorno.add(carta2);
    		}
    		else{
    			cartasRetorno.add(carta2);
    			cartasRetorno.add(carta1);
    		}
    	}
    	return cartasRetorno;
    }
    private Carta escolherCarta(List<Carta> cartas){
    	Carta carta = cartas.get(0);
    	int rodada=1;
    	int jogador=1;
    	int valorMaiorRodada=8;
    	boolean parceiroGanhando = false;
    	//rodada
    	if(rodada == 1){
    		//primeiro a jogar
    		if(jogador == 1){
    			carta = cartas.get(gerador.nextInt(3));
    		} else {
    			//parceiro ja jogou
    			if(jogador > 2){
    				//parceiro ganhando
    				if(parceiroGanhando){
    					carta = cartas.get(0);
    				} else{
    					//carta maior que a mesa
    					if(cartas.get(0).getValor() > valorMaiorRodada || cartas.get(1).getValor() > valorMaiorRodada || cartas.get(2).getValor() > valorMaiorRodada){
        					if(cartas.get(0).getValor() > valorMaiorRodada){
        						carta = cartas.get(gerador.nextInt(3));
        					} else if(cartas.get(1).getValor() > valorMaiorRodada){
        						carta = cartas.get(gerador.nextInt(2)+1);
        					} else {
        						carta = cartas.get(2);
        					}
        				}
        				else{
        					carta = cartas.get(2);
        				}
    				}
    			} else {
    				//carta maior que a mesa
    				if(cartas.get(0).getValor() > valorMaiorRodada || cartas.get(1).getValor() > valorMaiorRodada || cartas.get(2).getValor() > valorMaiorRodada){
    					if(cartas.get(0).getValor() > valorMaiorRodada){
    						carta = cartas.get(gerador.nextInt(3));
    					} else if(cartas.get(1).getValor() > valorMaiorRodada){
    						carta = cartas.get(gerador.nextInt(2)+1);
    					} else {
    						carta = cartas.get(2);
    					}
    				}
    				else{
    					carta = cartas.get(2);
    				}
    			}
    		}
    	} else{
    		
    	}
    	
    	return carta;
    }
}
