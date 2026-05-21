package simulador.mic1.uc;

public class Incrementador {
    private int valor;
    private final MicroMultiplexador mux;

    public Incrementador(MicroMultiplexador mux){
        this.mux=mux;
    }

    public void recebeMPC(int novoValor){
        this.valor=novoValor;
        mux.recebeIncrementador(this.valor+1);
    }
}
