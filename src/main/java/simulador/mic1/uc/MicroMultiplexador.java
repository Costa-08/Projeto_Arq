package simulador.mic1.uc;

public class MicroMultiplexador {
    private int valorIncrementador;
    private int valorADDR;
    private final ContadorMicroPrograma mpc;

    public MicroMultiplexador(ContadorMicroPrograma mpc){
        this.mpc=mpc;
    }

    public void recebeIncrementador(int valor){
        this.valorIncrementador=valor;
    }

    public void recebeSinalControle(int addr){
        this.valorADDR=addr;
    }

    public void recebeLogicaMicroSequencia(boolean decide){
        if (decide){
            enviaMPC(valorADDR);
        }
        else{
            enviaMPC(valorIncrementador);
        }
    }

    public void enviaMPC(int valor){
        mpc.recebeValor(valor);
    }
    
}
