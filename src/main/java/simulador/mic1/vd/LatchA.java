package simulador.mic1.vd;

import simulador.mic1.vd.Barramento;

public class LatchA {

    private int valor;
    private final Barramento busOrigem;
    private Amultiplexador amux;


    public void setAmultiplexador(Amultiplexador mux){
        this.amux=mux;
    }

    public LatchA(Barramento bus){
        this.busOrigem=bus;
    }

    public void recebeClock(){ 
        this.valor=busOrigem.getConteudo();
        amux.recebeLatchA(valor);
    }

    public int getConteudo(){
        return this.valor;
    }

}
