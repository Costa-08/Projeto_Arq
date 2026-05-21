package simulador.mic1.uc;

public class ContadorMicroPrograma {
    private int valor=0;
    private final int MASK = 0xFF;
    private final ControlStore microMemoria;
    private Incrementador inc;

    public ContadorMicroPrograma(ControlStore mm){
        this.microMemoria=mm;

    }

    public void setIncrementador(Incrementador inc){
        this.inc=inc;
    }

    public void recebeValor(int novoValor){
        this.valor=novoValor&MASK;
    }

    public void recebeClock(){
        inc.recebeMPC(valor);
        microMemoria.recebeMPC(valor);
    }
}
