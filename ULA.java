/*

00: +
01: and
10: A
11: inv(A)

*/

public class ULA {
    
    private int sinalControle;

    private final Latch latch1;
    private final Latch latch2;

    private static final int MASK = 0xFFFF;
    
    public ULA(Latch latch1, Latch latch2){
        this.latch1=latch1;
        this.latch2=latch2;
    }

    public void setSinalControle(int novoValor){
        sinalControle=novoValor&7;
    }

    private int operacao(){
        
        int valorA=latch1.getValor();
        int valorB=latch2.getValor();

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
