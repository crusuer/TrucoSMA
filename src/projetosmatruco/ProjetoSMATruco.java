/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetosmatruco;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import utils.Carta;

/**
 *
 * @author robson
 */
public class ProjetoSMATruco {

    static ContainerController containerController;
    static AgentController agentController;

    public enum CartasEnum {
        Q(11), J(12), K(13), A(14);

        public int valorCarta;

        CartasEnum(int valor) {
            valorCarta = valor;
        }
    }
    public enum Naipes{ Ouro, Espada, Copas, Paus };

    public static void main(String[] args) throws InterruptedException {
        //iniciando main container
        //startMainContainer(Profile.LOCAL_HOST, Profile.LOCAL_PORT, "UFABC");
        startMainContainer("127.0.0.1", Profile.LOCAL_PORT, "UFABC");

        Carta carta1 = new Carta(4, Naipes.Paus, 1, false);
        Carta carta2 = new Carta(3, Naipes.Ouro, 10, false);
        Carta carta3 = new Carta(7, Naipes.Espada, 4, false);
        
        //adicionando agente
        //SINTAXE: addAgent(container, nome_do_agente, classe, parametros de inicializacao)
        addAgent(containerController, "Juiz", agentes.Juiz.class.getName(), null);
        addAgent(containerController, "Jogador1", agentes.Jogador.class.getName(), new Object[]{carta1, carta2, carta3});

        //adicionando agente RMA
        //addAgent(containerController, "rma", "jade.tools.rma.rma", null);
        addAgent(containerController, "rma", jade.tools.rma.rma.class.getName(), null);

        //Criando o agente Sniffer e definindo quais agentes ele irá controlar
        addAgent(containerController, "Sniffer", "jade.tools.sniffer.Sniffer",
                new Object[]{"Juiz", ";", "Jogador1"});
    }

    public static void startMainContainer(String host, String port, String name) {
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, host);
        profile.setParameter(Profile.MAIN_PORT, port);
        profile.setParameter(Profile.PLATFORM_ID, name);

        containerController = runtime.createMainContainer(profile);
    }

    public static void addAgent(ContainerController cc, String agent, String classe, Object[] args) {
        try {
            agentController = cc.createNewAgent(agent, classe, args);
            agentController.start();
        } catch (StaleProxyException s) {
            s.printStackTrace();
        }
    }
}
