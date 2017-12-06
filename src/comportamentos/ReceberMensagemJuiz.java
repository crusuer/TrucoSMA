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
    int vencedorTerceiraRodada = 0;
    int meuTimeTrucou = 0;
    int vencedor = 0;
    int real = 0;
    
    int inicial = 0;
    int pontoTime1=0;
    int pontoTime2=0;
    List<Integer> time1 = new ArrayList<>();    
    List<Integer> time2 = new ArrayList<>();
    List<Integer> sairam = new ArrayList<>();
    public ReceberMensagemJuiz(Agent a) 
    {
       super(a);
    }
      
    @Override
    public void action() 
    {
    	System.out.println("DISTRIBUINDO CARTAS");
        iniciarPartida();
        boolean resp = false;
		for (int rods = 1; rods < 4; rods++) {
			System.out.println("\n" + rods + "º Rodada");
			for (int i = 0; i < 4; i++) {
				resp = jogada((vencedor + i) % 4 + 1);
				if(resp == true) { 
					break;
				}
			}
			if(resp == true) { 
				break;
			}
		}
        /*System.out.println("\nTerceira rodada");
        for(int i=0;i<4;i++){
        	System.out.println(vencedor +", "+rodada +", "+ jogador +", "+ jogadorGanhando +", "+ valorGanhando +", "+ maiorValorPossivel +", "+ vencedorPrimeiraRodada +", "+ vencedorSegundaRodada +", "+ meuTimeTrucou);
        	jogada((vencedor+i)%4 +1);
        }*/
		reseta();
		System.out.println("\nPontuação Time1: "+pontoTime1);
		System.out.println("Pontuação Time2: "+pontoTime2);
		if(pontoTime1 > 11) { 
			System.out.println("\nTime1 GANHOU!!!");
			myAgent.blockingReceive();
		} else if(pontoTime2 > 11) { 
			System.out.println("\nTime2 GANHOU!!!");
			myAgent.blockingReceive();
		}
        
    }
    private void iniciarPartida(){
    	time1.clear();
    	time2.clear();
    	time1.add(1);
    	time1.add(3);
    	time2.add(2);
    	time2.add(4);
        List<Carta> lista = embaralhar();                
        
        mensagemInicio(1,lista);
        mensagemInicio(2,lista);
        mensagemInicio(3,lista);
        mensagemInicio(4,lista);
    }
    private boolean jogada(int num) {   
    	boolean resp = false;
        try {
            ACLMessage send = new ACLMessage(ACLMessage.INFORM);
            send.addReceiver(new AID("Jogador" + num, AID.ISLOCALNAME));
            send.setLanguage("Portugues");
            int vencP=0;
            int vencS=0;
            if (vencedorPrimeiraRodada == 1) {
            	if (time1.contains(num)) {
            		vencP = vencedorPrimeiraRodada;
            	} else {
            		vencP = -vencedorPrimeiraRodada;
            	}
            } else if (vencedorPrimeiraRodada == 2) {
            	if (time2.contains(num)) {
            		vencP = vencedorPrimeiraRodada;
            	} else {
            		vencP = -vencedorPrimeiraRodada;
            	}
            }
            if (vencedorSegundaRodada == 1) {
            	if (time1.contains(num)) {
            		vencS = vencedorSegundaRodada;
            	} else {
            		vencS = -vencedorSegundaRodada;
            	}
            } else if (vencedorSegundaRodada == 2) {
            	if (time2.contains(num)) {
            		vencS = vencedorSegundaRodada;
            	} else {
            		vencS = -vencedorSegundaRodada;
            	}
            }
            int[] lista = {rodada, jogador, jogadorGanhando, valorGanhando, maiorValorPossivel, vencP, vencS, meuTimeTrucou};
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

                        real = Integer.parseInt(receive.getSender().getLocalName().substring(receive.getSender().getLocalName().length()-1))-1;
                    }
                    else if(valorGanhando == content.getValor()){
                        jogadorGanhando = 9;
                        real = Integer.parseInt(receive.getSender().getLocalName().substring(receive.getSender().getLocalName().length()-1))-1;
                    }
                    if(maiorValorPossivel == content.getValor()){
                        maiorValorPossivel--;
                        while(sairam.contains(maiorValorPossivel)){
                           maiorValorPossivel--; 
                        }
                    }
                    System.out.println("--> " + receive.getSender().getLocalName() + ": " + content);
                } catch (UnreadableException ex) {
                    Logger.getLogger(ReceberMensagemJuiz.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (receive.getPerformative() == ACLMessage.PROPOSE) {
                System.out.println("--> " + receive.getSender().getLocalName() + ": Pediu truco!");
            }
        }
        if(jogador == 4){
        	if(jogadorGanhando == 9){
        		System.out.println("Rodada empatou");
        	}
        	else if(time1.contains(real+1)){
        		System.out.println("Vencedor da rodada: Jogador"+(real+1));
        		vencedor = real;
        		if(rodada == 1){
        			vencedorPrimeiraRodada = 1;
        		}
        		else if(rodada == 2){
        			vencedorSegundaRodada = 1;
        		}
        		else {
        			vencedorTerceiraRodada = 1;
        		}
                
            } else {
            	System.out.println("Vencedor da rodada: Jogador"+(real+1));
            	vencedor = real;
            	if(rodada == 1){
        			vencedorPrimeiraRodada = 2;
        		}
        		else if(rodada == 2){
        			vencedorSegundaRodada = 2;
        		}
        		else {
        			vencedorTerceiraRodada = 2;
        		}
            }
        	resp = verificaVencedorPartida();
            rodada++;
            jogador = 0;
            jogadorGanhando = 10;
            valorGanhando = 0;
            
        }
        jogador++;
        /*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
        return resp;
    }
    private void mensagemInicio(int num, List<Carta> lista) {
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.SUBSCRIBE);
            msg.addReceiver(new AID("Jogador"+num, AID.ISLOCALNAME));
            System.out.println("Jogador"+num+": "+lista.get(1+(num-1)*3)+", "+ lista.get(2+(num-1)*3)+", "+ lista.get(3+(num-1)*3));
            msg.setContentObject(new Object[]{lista.get(1+(num-1)*3), lista.get(2+(num-1)*3), lista.get(3+(num-1)*3)});
            myAgent.send(msg);
        } catch (IOException ex) {
            Logger.getLogger(ReceberMensagemJuiz.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean verificaVencedorPartida(){
    	if(rodada == 2){
    		if(vencedorPrimeiraRodada == 0){
    			if(vencedorSegundaRodada == 1){
    				System.out.println("Vencedor da partida: Time1");
    				pontoTime1++;
    				return true;
    			} else {
    				System.out.println("Vencedor da partida: Time2");
    				pontoTime2++;
    				return true;
    			}
    		} else if((vencedorPrimeiraRodada == 1 && vencedorSegundaRodada == 1) || (vencedorPrimeiraRodada == 1 && vencedorSegundaRodada == 0)){
    			System.out.println("Vencedor da partida: Time1");
    			pontoTime1++;
    			return true;
    		} else if((vencedorPrimeiraRodada == 2 && vencedorSegundaRodada == 2) || (vencedorPrimeiraRodada == 2 && vencedorSegundaRodada == 0)){
    			System.out.println("Vencedor da partida: Time2");
    			pontoTime2++;
    			return true;
    		}
    	} else if(rodada == 3){
    		if((vencedorPrimeiraRodada == 1 && vencedorTerceiraRodada == 1)||(vencedorPrimeiraRodada == 1 && vencedorTerceiraRodada == 0)||(vencedorSegundaRodada == 1 && vencedorTerceiraRodada == 1)){
    			System.out.println("Vencedor da partida: Time1");
    			pontoTime1++;
    			return true;
    		} else if((vencedorPrimeiraRodada == 2 && vencedorTerceiraRodada == 2)||(vencedorPrimeiraRodada == 2 && vencedorTerceiraRodada == 0)||(vencedorSegundaRodada == 2 && vencedorTerceiraRodada == 2)){
    			System.out.println("Vencedor da partida: Time2");
    			pontoTime2++;
    			return true;
    		}
    	}
    	return false;
    }
    
    private void reseta(){
    	rodada = 1;
        jogador = 1;
        jogadorGanhando = 10;
        valorGanhando = 0;
        maiorValorPossivel = 14;
        vencedorPrimeiraRodada = 0;
        vencedorSegundaRodada = 0;
        vencedorTerceiraRodada = 0;
        meuTimeTrucou = 0;
        vencedor = 0;
        real = 0;
        inicial++;
        vencedor = inicial%4;
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
        System.out.println("Carta virada: "+lista.get(0));
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

