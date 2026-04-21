/*

00: +
01: and
10: A
11: inv(A)

*/

public class ULA {
    
    private int valorA;
    private int valorB;
    private int sinalControle;

    private static final int MASK = 0xFFFF;


    public void setValorA(int novoValor){
        valorA=novoValor&MASK;
    }

    public void setValorB(int novoValor){
        valorB=novoValor&MASK;
    }

    public void setSinalControle(int novoValor){
        valorB=novoValor&7;
    }

    private int operacao(){
        switch (sinalControle){
            case 0:
                return (valorA+valorB)&MASK;
            case 1:
                return (valorA&valorB);
            case 2:
                return valorA;
            case 3:
                return (~valorA)&MASK;
            default:
                System.out.println("Operação inválida dentro da ULA");
                return 1;
        }
    }

    public int getResultULA(){
        return operacao();
    }

}
