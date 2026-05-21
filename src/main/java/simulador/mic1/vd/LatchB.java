package simulador.mic1.vd;

import simulador.mic1.vd.Barramento;

public class LatchB {

    private int valor;
    private final Barramento busOrigem;
    private UnidadeLogicoAritmetica ula;


    public void setUnidadeLogicoAritmetica(UnidadeLogicoAritmetica alu){
        this.ula=alu;
    }

    public LatchB(Barramento bus){
        this.busOrigem=bus;
    }

    public void recebeClock(){ 
        this.valor=busOrigem.getConteudo();
        ula.recebeLatchB(valor);
    }

    public int getConteudo(){
        return this.valor;
    }

}
