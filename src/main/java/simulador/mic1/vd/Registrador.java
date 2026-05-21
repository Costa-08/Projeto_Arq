package simulador.mic1.vd;

import simulador.mic1.vd.Barramento;

public class Registrador {

    private int valor;

    private static final int MASK = 0xFFFF;

    private boolean sinalControleA;
    private final Barramento BarramentoA;
    private boolean sinalControleB;
    private final Barramento BarramentoB;
    private boolean sinalControleC;
    private final Barramento BarramentoC;


    public Registrador(Barramento aBus, Barramento bBus, Barramento cBus){
        this.BarramentoA=aBus;
        this.BarramentoB=bBus;
        this.BarramentoC=cBus;
    }

    public int getValor(){
        return this.valor;
    }

    /*
    
    No mic-1, registradores só suportam 16 bits, o que se encaixa no tipo primitivo short, porém é muito mais natural usar o tipo int do java.
    Por isso, resolvi tratar o valor interno do registrador como um inteiro e usar uma MASK para zerar qualquer bit além dos 16 permitidos e, assim,
    trabalhar como se fosse um valor de 16 bits, sem obrigar os outros desenvolvedores a trabalhar com o short.

    */

    public void recebeSinalControleA(boolean sinal){
        this.sinalControleA=sinal;
        if (sinalControleA){
            BarramentoA.recebeValor(valor);
        }
    }

    public void recebeSinalControleB(boolean sinal){
        this.sinalControleB=sinal;
        if (sinalControleB){
            BarramentoB.recebeValor(valor);
        }
    }
    
    public void recebeSinalControleC(boolean sinal){
        this.sinalControleC=sinal;
        if (sinalControleC){
            this.valor=BarramentoC.getConteudo()&MASK;
        }
    }

    public void recebeValor(int novoValor){
        this.valor=novoValor&MASK;
    }

}
