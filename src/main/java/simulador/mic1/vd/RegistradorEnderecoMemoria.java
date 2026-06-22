package simulador.mic1.vd;

import simulador.mic1.memoria.CacheL1;

public class RegistradorEnderecoMemoria {
    
    private int valor;

    private boolean sinalControle;

    private final LatchB latchB;

    private final CacheL1 cache;
    private final int MASK = 0xFFF;

    public RegistradorEnderecoMemoria(CacheL1 cacheParametro, LatchB latch){
        this.cache=cacheParametro;
        this.latchB = latch;
    }

    public void recebeSinalControle(boolean sinal){
        this.sinalControle=sinal;
    }

    public void recebeClock(){
        if (sinalControle){
            valor=latchB.getConteudo()&MASK;
            cache.recebeValorMAR(valor);
        }
    }

    public int getValorMAR(){
        return this.valor;
    }

}
