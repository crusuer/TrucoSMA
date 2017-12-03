/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import projetosmatruco.ProjetoSMATruco;

public class Carta implements Comparable<Carta>, Serializable {

    private int numero;
    private int naipe;
    private final String naipeStr;
    private int valor;
    private transient static final Map<Integer, Integer> mapa = createMap();

    private static Map<Integer, Integer> createMap() {
        //mapa do número da carta e seu respectivo valor no jogo
        Map<Integer, Integer> maps = new HashMap<>();
        maps.put(4, 1);
        maps.put(5, 2);
        maps.put(6, 3);
        maps.put(7, 4);
        maps.put(8, 5);//Q
        maps.put(9, 6);//J
        maps.put(10, 7);//K
        maps.put(1, 8);//A
        maps.put(2, 9);
        maps.put(3, 10);

        return maps;
    }

    public Carta(int numero, ProjetoSMATruco.Naipes naipe) {
        this.naipe = naipe.ordinal();
        naipeStr = naipe.name();

        this.numero = numero;
        valor = mapa.get(numero);
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getNaipe() {
        return naipe;
    }

    public void setNaipe(int naipe) {
        this.naipe = naipe;
    }

    public String getNaipeStr() {
        return naipeStr;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(boolean manilha) {
        if (manilha) {
            valor = 11 + this.naipe;
        } else {
            valor = mapa.get(numero);
        }
    }

    @Override
    public int compareTo(Carta outraCarta) {
        return this.valor - outraCarta.valor;
    }
    @Override
    public String toString() {
        return numero+" de "+naipeStr;
    }
}
