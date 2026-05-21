package simulador.mic1.vd;/*

00: +
01: and
10: A
11: inv(A)

*/

import simulador.mic1.uc.LogicaMicroSequencia;
import simulador.mic1.vd.Shifter;

public class UnidadeLogicoAritmetica {

    private int valorAmux;
    private int valorLatchB;
    private int sinalControle;
    private int saida;
    private boolean Zflag;
    private boolean Nflag;
    private static final int MASK = 0xFFFF;
    private final Shifter shifterDestino;
    private LogicaMicroSequencia logms;

    public UnidadeLogicoAritmetica(Shifter destino){
        this.shifterDestino=destino;
    }

    public void setLogica(LogicaMicroSequencia log){
        this.logms=log;
    }

    public void recebeAmux(int valor){
        this.valorAmux=valor;
        operacao();
        enviaSaida();
        
    }

    public void recebeLatchB(int conteudo){
        this.valorLatchB=conteudo;
    }
    
    public void recebeSinalControle(int novoValor){
        sinalControle=novoValor&3;
    }    
    
    public void operacao(){

        int valorA=valorAmux;
        int valorB=valorLatchB;
        int resultado;

        switch (sinalControle){
            case 0 -> resultado = (valorA+valorB)&MASK;
            case 1 -> resultado = (valorA&valorB);
            case 2 -> resultado = valorA;
            case 3 -> resultado = (~valorA)&MASK;
            default -> {
                System.out.println("Operação inválida na ULA");
                resultado=0;
            }
        }

        this.saida=resultado;
        this.Zflag=(resultado==0);
        this.Nflag=((resultado/0x8000)%2==1);
    }

    public void enviaSaida(){
        shifterDestino.recebeULA(saida);
        logms.recebeFlags(Zflag, Nflag);
    }

}
