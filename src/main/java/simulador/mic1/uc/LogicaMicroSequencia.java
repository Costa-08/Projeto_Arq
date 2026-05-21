package simulador.mic1.uc;

public class LogicaMicroSequencia {
    private int valorCond;
    private boolean valorNFlag;
    private boolean valorZFlag;
    private boolean saida;
    private final MicroMultiplexador mux;

    public LogicaMicroSequencia(MicroMultiplexador mux){
        this.mux=mux;
    }

    public void recebeSinalControle(int valor){
        this.valorCond=valor;
    }

    public void recebeFlags(boolean Zflag, boolean Nflag){
        this.valorNFlag=Nflag;
        this.valorZFlag=Zflag;
        switch (valorCond){
            case 0 -> this.saida=false;
            case 1 -> this.saida=valorZFlag;
            case 2 -> this.saida=valorNFlag;
            case 3 -> this.saida=true;
            default -> System.out.println("valor cond errado no recebeFlags da logica");
        }
        enviaMMUX();
    }

    private void enviaMMUX(){
        mux.recebeLogicaMicroSequencia(this.saida);
    }

}
