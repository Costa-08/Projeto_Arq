/*

00: +
01: and
10: A
11: inv(A)

*/

public class ULA {
    
    // Referencia aos Latches 

    private int valorAmux;
    private int valorBusB;
    private int sinalControle;
    private int saida;
    private boolean Zflag;
    private boolean Nflag;
    private static final int MASK = 0xFFFF;

    public void recebeAmux(int valor){
        this.valorAmux=valor;
    }

    public void recebeBusB(int valor){
        this.valorBusB=valor;
    }
    
    public void recebeSinalControle(int novoValor){
        sinalControle=novoValor&3;
    }    
    
    public void operacao(){
        
        int valorA=valorAmux;
        int valorB=valorBusB;
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
        this.Nflag=(resultado<0);
    }

    public void enviaSaida(Shifter obj){
        operacao();
        obj.recebeULA(saida);
    }

    public boolean getZflag(){
        return Zflag;
    }

    public boolean getNflag(){
        return Nflag;
    }

}

/*

falta logica para enviar Zflag e Nflag para Micro Seq Logic

*/