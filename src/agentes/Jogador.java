/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.Agent;
import utils.Carta;

/**
 *
 * @author robson
 */
public class Jogador extends Agent {

    @Override
    protected void setup() {
        Object[] args = getArguments();
        Carta carta1 = (Carta) args[0];
        Carta carta2 = (Carta) args[1];
        Carta carta3 = (Carta) args[2];
        ordenarCartas(carta1, carta2, carta3);

        addBehaviour(new comportamentos.ReceberMensagemJogador(this));
    }
    private void ordenarCartas(Carta carta1, Carta carta2, Carta carta3) {
        if ((carta1.compareTo(carta2) < 0) && (carta1.compareTo(carta3) < 0)) {
            if (carta2.compareTo(carta3) < 0) {
                setArguments(new Object[]{carta1, carta2, carta3});
            } else {
                setArguments(new Object[]{carta1, carta3, carta2});
            }
        } else if ((carta2.compareTo(carta1) < 0) && (carta2.compareTo(carta3) < 0)) {
            if (carta1.compareTo(carta3) < 0) {
                setArguments(new Object[]{carta2, carta1, carta3});
            } else {
                setArguments(new Object[]{carta2, carta3, carta1});
            }
        } else {
            if (carta1.compareTo(carta2) < 0) {
                setArguments(new Object[]{carta3, carta1, carta2});
            } else {
                setArguments(new Object[]{carta3, carta2, carta1});
            }
        }
    }
}
