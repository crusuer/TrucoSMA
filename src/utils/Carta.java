/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import projetosmatruco.ProjetoSMATruco;

public class Carta {
    private int numero;
    private int naipe;
    private int valor;    
    private boolean manilha;
    
    public Carta(int numero,ProjetoSMATruco.Naipes naipe,int valor, boolean manilha){
        switch (naipe){
            case Ouro:
                this.naipe = naipe.ordinal();
                break;
            case Espada:
                this.naipe = naipe.ordinal();
                break;
            case Copas:
                this.naipe = naipe.ordinal();
                break;
            case Paus:
                this.naipe = naipe.ordinal();
                break;
        }
                
        if (manilha){
            this.valor = 11 + this.naipe;
            
        } else {
            this.valor = valor;
        }
        this.numero = numero;
        this.manilha = manilha;
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

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
    
    public boolean isManilha() {
        return manilha;
    }

    public void setManilha(boolean manilha) {
        this.manilha = manilha;
    }
}
