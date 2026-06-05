package simulador.mic1.vd;

import simulador.mic1.memoriaprincipal.MemoriaPrincipal;

public class RegistradorEnderecoMemoria {
    
    private int valor;

    private boolean sinalControle;

    private final LatchB latchB;

    private final MemoriaPrincipal mp;
    private final int MASK = 0xFFF;

    public RegistradorEnderecoMemoria(MemoriaPrincipal memory, LatchB latch){
        this.mp=memory;
        this.latchB = latch;
    }

    public void recebeSinalControle(boolean sinal){
        this.sinalControle=sinal;
    }

    public void recebeClock(){
        if (sinalControle){
            valor=latchB.getConteudo()&MASK;
            mp.recebeValorMAR(valor);
        }
    }

    public void printValorMAR(){
        System.out.println("Valor MAR: "+this.valor);
    }

}
